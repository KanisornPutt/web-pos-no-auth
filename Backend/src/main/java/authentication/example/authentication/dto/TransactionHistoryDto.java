package authentication.example.authentication.dto;

import authentication.example.authentication.entities.OrderedProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionHistoryDto {

    private Long id;

    private List<OrderedProductEntity> orderedProducts;

    private Long storeId;

    private Long userId;

    private LocalDateTime createdAt;

    private String merchantName;

    private BigDecimal totalPrice;

    private Integer totalAmount;

    private String storeName;

    private Integer historyNumber;

}
