package authentication.example.authentication.repositories;

import authentication.example.authentication.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByStoreId(Long storeId);
}
