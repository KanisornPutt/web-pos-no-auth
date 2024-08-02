package authentication.example.authentication.repositories;

import authentication.example.authentication.entities.TransactionHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistoryEntity, Long> {

    @Query("SELECT COUNT(t) FROM TransactionHistoryEntity t WHERE t.storeEntity.id = :storeId")
    int countByStoreId(@Param("storeId") Long storeId);

    @Query("SELECT t FROM TransactionHistoryEntity t WHERE t.storeEntity.id = :storeId AND t.userId = :userId")
    List<TransactionHistoryEntity> findByStoreIdAndUserId(@Param("storeId") Long storeId, @Param("userId") Long userId);

    @Query("SELECT t FROM TransactionHistoryEntity t WHERE t.storeEntity.id = :storeId")
    List<TransactionHistoryEntity> findByStoreId(@Param("storeId") Long storeId);
}
