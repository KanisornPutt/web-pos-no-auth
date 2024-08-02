package authentication.example.authentication.services;

import authentication.example.authentication.auth.StoreRequest;
import authentication.example.authentication.dto.StoreAdminDto;
import authentication.example.authentication.dto.StoreDto;
import authentication.example.authentication.entities.StoreEntity;

import java.util.List;
import java.util.Optional;

public interface StoreService {
    StoreDto createStore(Long userId, StoreRequest storeRequest);
    StoreEntity createEntity(StoreRequest storeRequest);
    StoreEntity mapAdminToEntity(StoreAdminDto storeAdminDto);

    StoreDto mapToDto(StoreEntity storeEntity);

    boolean isAdmin(Long userId, Long storeId);

    boolean isStaff(Long userId, Long storeId);


    List<StoreDto> findAll();

    Optional<StoreEntity> findById(Long id);

    boolean isExistsById(Long id);

    StoreDto partialUpdateStore(Long id ,StoreEntity storeEntity);

    void delete(Long id);

    Optional<StoreEntity> findByStoreCode(String storeCode);

    boolean isExistsByStoreCode(String storeCode);

    boolean checkCredential(StoreRequest storeRequest);

    StoreAdminDto mapToAdminDto(StoreEntity storeEntity);

    StoreDto setPassword(Long storeId, String storePassword);
}
