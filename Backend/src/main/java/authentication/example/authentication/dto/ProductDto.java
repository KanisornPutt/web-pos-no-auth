package authentication.example.authentication.dto;

import authentication.example.authentication.entities.ProductOptionEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long id;

    private String name;

    private String description;

    private boolean hasStock;

    private Long stock;

    private String category;

    private String imageUrl;

    private BigDecimal price;

    private Long storeId;

    private List<ProductOptionEntity> additionalOptions;

}
