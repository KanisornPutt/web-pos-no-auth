package authentication.example.authentication.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "ordered_products")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderedProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private String name;
    private String category;
    private int amount;
    private BigDecimal originalPrice;
    private BigDecimal singlePrice;
    private BigDecimal addedPrice;
    private String note;

    @ElementCollection
    @CollectionTable(name = "selected_choices", joinColumns = @JoinColumn(name = "ordered_product_id"))
    @Column(name = "choice")
    private List<String> selectedChoices;

}
