package authentication.example.authentication.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "transaction_histories")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TransactionHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "transaction_history_id")
    private List<OrderedProductEntity> orderedProductEntities;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoreEntity storeEntity;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private String merchantName;

    private String storeName;

    private Long userId;

    private BigDecimal totalPrice;

    private Integer totalAmount;

    private Integer historyNumber;
}
