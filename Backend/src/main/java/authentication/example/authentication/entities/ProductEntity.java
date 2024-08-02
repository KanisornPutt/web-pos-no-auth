package authentication.example.authentication.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private boolean hasStock;

    private Long stock;

    @Column(nullable = true)
    private String category;

    @Column(nullable = true, length = 1000)
    private String imageUrl;

    @Column(precision = 15, scale = 2)
    private BigDecimal price;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "store_id")
    private StoreEntity store;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProductOptionEntity> additionalOptions;

    @Override
    public String toString() {
        return "ProductEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", stock=" + stock +
                ", category='" + category + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", price=" + price +
                ", storeId=" + (store != null ? store.getId() : null) +
                ", additionalOptions=" + additionalOptions +
                '}';
    }
}
