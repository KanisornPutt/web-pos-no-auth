package authentication.example.authentication.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "stores")
public class StoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String storeCode;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String promptpayId;

    private List<String> categories;

    @Override
    public String toString() {
        return "StoreEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", storeCode='" + storeCode + '\'' +
                ", promptpayId='" + promptpayId + '\'' +
                ", categories=" + categories +
                '}';
    }


}
