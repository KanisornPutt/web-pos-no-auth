package authentication.example.authentication.controllers;

import authentication.example.authentication.dto.ProductDto;
import authentication.example.authentication.entities.ProductEntity;
import authentication.example.authentication.entities.ProductOptionEntity;
import authentication.example.authentication.entities.StoreEntity;
import authentication.example.authentication.services.MinioService;
import authentication.example.authentication.services.ProductService;
import authentication.example.authentication.services.StoreService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ObjectMapper objectMapper;
    private final ProductService productService;
    private final StoreService storeService;
    private final MinioService minioService;

    @PostMapping(path = "/{storeId}/{userId}")
    public ResponseEntity<ProductDto> addProduct(@PathVariable Long storeId,
                                                 @PathVariable Long userId,
                                                 @RequestParam("name") String name,
                                                 @RequestParam("description") String description,
                                                 @RequestParam("price") double price,
                                                 @RequestParam(value = "image", required = false) MultipartFile image,
                                                 @RequestParam(value = "category", required = false) String category,
                                                 @RequestParam(value = "additionalOptions") String additionalOptionsJson,
                                                 @RequestParam(value = "hasStock") boolean hasStock,
                                                 @RequestParam(value = "stock") Integer stock) throws Exception {

        if (!storeService.isAdmin(userId, storeId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Optional<StoreEntity> foundStore = storeService.findById(storeId);

        if (foundStore.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        ProductDto productDto = ProductDto.builder()
                .name(name)
                .description(description)
                .price(BigDecimal.valueOf(price))
                .category(category)
                .hasStock(hasStock)
                .stock(0L)
                .build();

        List<ProductOptionEntity> productOptionEntities = objectMapper.readValue(additionalOptionsJson, new TypeReference<List<ProductOptionEntity>>() {});

        if (hasStock) productDto.setStock(Long.valueOf(stock));

        if (image != null) {
            String imageUrl = minioService.uploadFile(image);
            productDto.setImageUrl(imageUrl);
        }

        ProductDto savedProductDto = productService.createProduct(productDto, foundStore.get(), productOptionEntities);
        return new ResponseEntity<>(savedProductDto, HttpStatus.CREATED);
    }

    @GetMapping(path = "/{storeId}/{userId}")
    public ResponseEntity<List<ProductDto>> getProductsByStoreId(
            @PathVariable("storeId") Long storeId,
            @PathVariable("userId") Long userId) {

        if (!storeService.isStaff(userId, storeId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        List<ProductDto> products = productService.getProductsByStoreId(storeId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PutMapping(path = "/{storeId}/{userId}/{productId}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long storeId,
            @PathVariable Long userId,
            @RequestParam("id") Long productId,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "additionalOptions") String additionalOptionsJson,
            @RequestParam(value = "hasStock") boolean hasStock,
            @RequestParam(value = "stock", required = false) Integer stock) throws Exception {


        if (!storeService.isAdmin(userId, storeId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Optional<ProductEntity> foundProduct = productService.findById(productId);

        if (foundProduct.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Optional<StoreEntity> foundStore = storeService.findById(storeId);

        if (foundStore.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        StoreEntity storeEntity = foundStore.get();

        ProductDto productDto = ProductDto.builder()
                .id(productId)
                .name(name)
                .description(description)
                .price(BigDecimal.valueOf(price))
                .category(category)
                .stock(0L)
                .hasStock(hasStock)
                .build();

        List<ProductOptionEntity> productOptionEntities = objectMapper.readValue(additionalOptionsJson, new TypeReference<List<ProductOptionEntity>>() {});
        productDto.setAdditionalOptions(productOptionEntities);

        if (hasStock) productDto.setStock(Long.valueOf(stock));

        if (image != null) {
            String imageUrl = minioService.uploadFile(image);
            productDto.setImageUrl(imageUrl);
        }

        ProductDto updatedProductDto = productService.updateProduct(productId, productDto, storeEntity);

        return new ResponseEntity<>(updatedProductDto, HttpStatus.OK);
    }


    @DeleteMapping(path = "/{storeId}/{userId}/{productId}")
    public ResponseEntity deleteProduct(
            @PathVariable("storeId") Long storeId,
            @PathVariable("userId") Long userId,
            @PathVariable("productId") Long productId) {

        if (!storeService.isAdmin(userId, storeId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Optional<ProductEntity> foundProduct = productService.findById(productId);

        if (foundProduct.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        productService.deleteProductById(productId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
