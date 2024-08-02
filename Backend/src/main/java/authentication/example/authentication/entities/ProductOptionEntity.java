package authentication.example.authentication.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "product_options")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductOptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer min;

    @Column(nullable = false)
    private Integer max;

    @Column(nullable = false)
    private boolean required;

    @ElementCollection
    @CollectionTable(name = "option_choices", joinColumns = @JoinColumn(name = "option_id"))
    @Column(name = "choice")
    private List<String> choices;

    @ElementCollection
    @CollectionTable(name = "option_prices", joinColumns = @JoinColumn(name = "option_id"))
    @Column(name = "additional_price")
    private List<BigDecimal> prices;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private ProductEntity product;

    @Override
    public String toString() {
        return "ProductOptionEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", required=" + required +
                ", choices=" + choices +
                ", productId=" + (product != null ? product.getId() : null) +
                '}';
    }
}
