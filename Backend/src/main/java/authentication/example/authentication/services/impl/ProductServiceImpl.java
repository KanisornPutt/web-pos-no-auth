package authentication.example.authentication.services.impl;

import authentication.example.authentication.dto.ProductDto;
import authentication.example.authentication.entities.ProductEntity;
import authentication.example.authentication.entities.ProductOptionEntity;
import authentication.example.authentication.entities.StoreEntity;
import authentication.example.authentication.repositories.ProductRepository;
import authentication.example.authentication.repositories.StoreRepository;
import authentication.example.authentication.repositories.UserRepository;
import authentication.example.authentication.services.ProductService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    public ProductServiceImpl(ProductRepository productRepository, UserRepository userRepository, StoreRepository storeRepository) {
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
    }

    @Override
    public ProductDto mapToDto(ProductEntity productEntity) {
        return ProductDto.builder()
                .name(productEntity.getName())
                .id(productEntity.getId())
                .description(productEntity.getDescription())
                .imageUrl(productEntity.getImageUrl())
                .price(productEntity.getPrice())
                .storeId(productEntity.getStore().getId())
                .hasStock(productEntity.isHasStock())
                .stock(productEntity.getStock())
                .category(productEntity.getCategory())
                .additionalOptions(productEntity.getAdditionalOptions())
                .build();
    }


    @Override
    public ProductDto createProduct(ProductDto productDto, StoreEntity storeEntity, List<ProductOptionEntity> productOptionEntities) {
        productDto.setStoreId(storeEntity.getId());

        ProductEntity productEntity = ProductEntity.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .imageUrl(productDto.getImageUrl())
                .price(productDto.getPrice())
                .hasStock(productDto.isHasStock())
                .stock(productDto.getStock())
                .store(storeEntity)
                .additionalOptions(new ArrayList<>())
                .category(productDto.getCategory())
                .build();

        List<String> categories = storeEntity.getCategories();

        if (productDto.getCategory() != null && !categories.contains(productDto.getCategory())) {
            categories.add(productDto.getCategory());
        }

        storeEntity.setCategories(categories);
        storeRepository.save(storeEntity);

        for (ProductOptionEntity option : productOptionEntities) {
            option.setProduct(productEntity);
            productEntity.getAdditionalOptions().add(option);
        }

        ProductEntity savedProductEntity = productRepository.save(productEntity);

        return mapToDto(savedProductEntity);
    }

    @Override
    public List<ProductDto> getProductsByStoreId(Long storeId) {
        return productRepository.findByStoreId(storeId).stream().map(this::mapToDto).collect(Collectors.toList());
    }
    @Override
    public Optional<ProductEntity> findById(Long productId) {
        return productRepository.findById(productId);
    }

    @Override
    public ProductDto updateProduct(Long productId, ProductDto productDto, StoreEntity storeEntity) {
        productDto.setId(productId);

        List<String> categories = storeEntity.getCategories();

        if (productDto.getCategory() != null && !categories.contains(productDto.getCategory())) {
            categories.add(productDto.getCategory());
        }

        storeEntity.setCategories(categories);
        storeRepository.save(storeEntity);

        return productRepository.findById(productId).map(existingProduct -> {
            Optional.ofNullable(productDto.getName()).ifPresent(
                    existingProduct::setName);
            Optional.ofNullable(productDto.getDescription()).ifPresent(
                    existingProduct::setDescription);
            Optional.ofNullable(productDto.getDescription()).ifPresent(
                    existingProduct::setDescription);
            Optional.ofNullable(productDto.getCategory()).ifPresent(
                    existingProduct::setCategory);
            Optional.ofNullable(productDto.getImageUrl()).ifPresent(
                    existingProduct::setImageUrl);
            Optional.ofNullable(productDto.getPrice()).ifPresent(
                    existingProduct::setPrice);

            // handle stock
            if (Optional.ofNullable(productDto.isHasStock()).isPresent()) {
                existingProduct.setHasStock(productDto.isHasStock());
                if (productDto.isHasStock()) {
                    existingProduct.setStock(productDto.getStock());
                } else {
                    existingProduct.setStock(0L);
                }
            }


            // Handle additionalOptions
            List<ProductOptionEntity> existingOptions = existingProduct.getAdditionalOptions();
            List<ProductOptionEntity> updatedOptions = new ArrayList<>();

            // Map updated options to entities
            if (productDto.getAdditionalOptions() != null) {
                updatedOptions = productDto.getAdditionalOptions().stream()
                        .map(optionDto -> {
                            ProductOptionEntity optionEntity = new ProductOptionEntity();
                            optionEntity.setName(optionDto.getName());
                            optionEntity.setMin(optionDto.getMin());
                            optionEntity.setMax(optionDto.getMax());
                            optionEntity.setRequired(optionDto.isRequired());
                            optionEntity.setChoices(optionDto.getChoices());
                            optionEntity.setPrices(optionDto.getPrices());
                            optionEntity.setProduct(existingProduct);
                            return optionEntity;
                        })
                        .collect(Collectors.toList());
            }



            // Update existing options and add new ones
            Iterator<ProductOptionEntity> iterator = existingOptions.iterator();
            while (iterator.hasNext()) {
                ProductOptionEntity existingOption = iterator.next();
                boolean optionUpdated = false;
                for (ProductOptionEntity updatedOption : updatedOptions) {
                    if (existingOption.getId().equals(updatedOption.getId())) {
                        existingOption.setName(updatedOption.getName());
                        existingOption.setMin(updatedOption.getMin());
                        existingOption.setMax(updatedOption.getMax());
                        existingOption.setRequired(updatedOption.isRequired());
                        existingOption.setChoices(updatedOption.getChoices());
                        existingOption.setPrices(updatedOption.getPrices());
                        optionUpdated = true;
                        break;
                    }
                }
                // Remove options not present in updatedOptions
                if (!optionUpdated) {
                    iterator.remove();
                }
            }

            // Add new options
            for (ProductOptionEntity updatedOption : updatedOptions) {
                if (updatedOption.getId() == null) {
                    existingProduct.getAdditionalOptions().add(updatedOption);
                }
            }


            // Save the updated product
            ProductEntity savedProductEntity = productRepository.save(existingProduct);
            return this.mapToDto(savedProductEntity);
        }).orElseThrow(() -> new RuntimeException("Product does not exist"));
    }

    @Override
    public void deleteProductById(Long productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product with id " + productId + " not found"));
        product.setStore(null);
        productRepository.save(product);
        productRepository.delete(product);
    }

    @Override
    public void reduceProductStock(Long id, int amount) {
        Optional<ProductEntity> foundProduct = productRepository.findById(id);

        if (foundProduct.isEmpty()) throw new RuntimeException("Product not found");

        ProductEntity product = foundProduct.get();

        if (product.isHasStock()) {
            if (product.getStock() < amount) throw new RuntimeException("Amount exceeds stock");
            else product.setStock(product.getStock() - amount);
            productRepository.save(product);
        }
    }
}
