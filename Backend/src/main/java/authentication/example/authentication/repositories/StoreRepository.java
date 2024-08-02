package authentication.example.authentication.repositories;

import authentication.example.authentication.entities.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

    Optional<StoreEntity> findByStoreCode(String storeCode);
}
