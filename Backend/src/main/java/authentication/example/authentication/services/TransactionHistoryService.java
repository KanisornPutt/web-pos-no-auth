package authentication.example.authentication.services;

import authentication.example.authentication.dto.TransactionHistoryDto;
import authentication.example.authentication.entities.StoreEntity;
import authentication.example.authentication.entities.TransactionHistoryEntity;
import authentication.example.authentication.entities.User;

import java.util.List;

public interface TransactionHistoryService {

    TransactionHistoryDto mapToDto(TransactionHistoryEntity transactionHistoryEntity);

    TransactionHistoryDto saveTransaction(StoreEntity storeEntity, User user, TransactionHistoryDto requestDto);

    List<TransactionHistoryDto> findByStoreId(Long storeId);

    List<TransactionHistoryDto> findByStoreIdAndUserID(Long storeId, Long userId);
}
