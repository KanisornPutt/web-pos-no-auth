package authentication.example.authentication.services;

import authentication.example.authentication.dto.ProductDto;
import authentication.example.authentication.entities.ProductEntity;
import authentication.example.authentication.entities.ProductOptionEntity;
import authentication.example.authentication.entities.StoreEntity;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    ProductDto mapToDto(ProductEntity productEntity);

    ProductDto createProduct(ProductDto productDto, StoreEntity storeEntity, List<ProductOptionEntity> productOptionEntities);

    List<ProductDto> getProductsByStoreId(Long storeId);

    Optional<ProductEntity> findById(Long productId);

    ProductDto updateProduct(Long productId, ProductDto productDto, StoreEntity storeEntity);

    void deleteProductById(Long id);

    void reduceProductStock(Long id, int amount);
}
