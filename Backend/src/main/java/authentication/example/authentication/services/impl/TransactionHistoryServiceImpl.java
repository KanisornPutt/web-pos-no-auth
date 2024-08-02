package authentication.example.authentication.services.impl;

import authentication.example.authentication.dto.TransactionHistoryDto;
import authentication.example.authentication.entities.OrderedProductEntity;
import authentication.example.authentication.entities.StoreEntity;
import authentication.example.authentication.entities.TransactionHistoryEntity;
import authentication.example.authentication.entities.User;
import authentication.example.authentication.repositories.TransactionHistoryRepository;
import authentication.example.authentication.repositories.UserRepository;
import authentication.example.authentication.services.TransactionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionHistoryServiceImpl implements TransactionHistoryService {

    private final TransactionHistoryRepository transactionHistoryRepository;
    private final UserRepository userRepository;

    @Override
    public TransactionHistoryDto mapToDto(TransactionHistoryEntity transactionHistoryEntity) {

        Optional<User> foundUser = userRepository.findById(transactionHistoryEntity.getUserId());

        if (foundUser.isPresent()) {
            User user = foundUser.get();
            transactionHistoryEntity.setMerchantName(user.getFirstname() + " "+ user.getLastname());
            transactionHistoryRepository.save(transactionHistoryEntity);
        }

        return TransactionHistoryDto.builder()
                .id(transactionHistoryEntity.getId())
                .storeId(transactionHistoryEntity.getStoreEntity().getId())
                .userId(transactionHistoryEntity.getUserId())
                .historyNumber(transactionHistoryEntity.getHistoryNumber())
                .merchantName(transactionHistoryEntity.getMerchantName())
                .storeName(transactionHistoryEntity.getStoreName())
                .createdAt(transactionHistoryEntity.getCreatedAt())
                .totalPrice(transactionHistoryEntity.getTotalPrice())
                .totalAmount(transactionHistoryEntity.getTotalAmount())
                .orderedProducts(transactionHistoryEntity.getOrderedProductEntities())
                .build();
    }

    @Override
    public TransactionHistoryDto saveTransaction(StoreEntity storeEntity, User user, TransactionHistoryDto requestDto) {
        TransactionHistoryEntity transactionHistory = TransactionHistoryEntity.builder()
                .storeEntity(storeEntity)
                .totalPrice(requestDto.getTotalPrice())
                .totalAmount(requestDto.getTotalAmount())
                .userId(user.getId())
                .createdAt(LocalDateTime.now())
                .merchantName(user.getFirstname() + " " + user.getLastname())
                .storeName(storeEntity.getName())
                .build();

        List<OrderedProductEntity> orderedProductEntities = requestDto.getOrderedProducts().stream().map(productDTO -> {
            OrderedProductEntity entity = new OrderedProductEntity();
            entity.setProductId(productDTO.getProductId());
            entity.setName(productDTO.getName());
            entity.setCategory(productDTO.getCategory());
            entity.setAddedPrice(productDTO.getAddedPrice());
            entity.setAmount(productDTO.getAmount());
            entity.setOriginalPrice(productDTO.getOriginalPrice());
            entity.setSinglePrice(productDTO.getSinglePrice());
            entity.setNote(productDTO.getNote());
            entity.setSelectedChoices(productDTO.getSelectedChoices());
            return entity;
        }).collect(Collectors.toList());

        int number = transactionHistoryRepository.countByStoreId(storeEntity.getId());

        transactionHistory.setHistoryNumber(number + 1);

        transactionHistory.setOrderedProductEntities(orderedProductEntities);

        TransactionHistoryEntity savedHistory = transactionHistoryRepository.save(transactionHistory);

        return mapToDto(savedHistory);
    }

    @Override
    public List<TransactionHistoryDto> findByStoreId(Long storeId) {
        List<TransactionHistoryEntity> historyEntities = transactionHistoryRepository.findByStoreId(storeId);
        return historyEntities.stream().map(this::mapToDto).toList();
    }

    @Override
    public List<TransactionHistoryDto> findByStoreIdAndUserID(Long storeId, Long userId) {
        List<TransactionHistoryEntity> historyEntities = transactionHistoryRepository.findByStoreIdAndUserId(storeId, userId);
        return historyEntities.stream().map(this::mapToDto).toList();
    }


}
