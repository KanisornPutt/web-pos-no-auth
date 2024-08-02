package authentication.example.authentication.services.impl;

import authentication.example.authentication.auth.StoreRequest;
import authentication.example.authentication.dto.StoreAdminDto;
import authentication.example.authentication.dto.StoreDto;
import authentication.example.authentication.entities.ProductEntity;
import authentication.example.authentication.entities.Role;
import authentication.example.authentication.entities.StoreEntity;
import authentication.example.authentication.entities.User;
import authentication.example.authentication.repositories.ProductRepository;
import authentication.example.authentication.repositories.StoreRepository;
import authentication.example.authentication.repositories.UserRepository;
import authentication.example.authentication.services.StoreService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoreServiceImpl implements StoreService {

    private StoreRepository storeRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    public StoreServiceImpl(StoreRepository storeRepository, UserRepository userRepository, ProductRepository productRepository, PasswordEncoder passwordEncoder) {
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public StoreDto mapToDto(StoreEntity storeEntity) {
        return StoreDto.builder()
                .id(storeEntity.getId())
                .name(storeEntity.getName())
                .categories(storeEntity.getCategories())
                .promptpayId(storeEntity.getPromptpayId())
                .build();
    }


    @Override
    public boolean isAdmin(Long userId, Long storeId) {
        Optional<User> foundUser = userRepository.findById(userId);

        if (foundUser.isEmpty()) return false;

        User existingUser = foundUser.get();

        if(existingUser.getStore().getId() == null) return false;

        if (!existingUser.getStore().getId().equals(storeId)) return false;

        return existingUser.getRole() == Role.ADMIN;
    }

    @Override
    public boolean isStaff(Long userId, Long storeId) {
        Optional<User> foundUser = userRepository.findById(userId);

        if (foundUser.isEmpty()) return false;

        User existingUser = foundUser.get();

        if(existingUser.getStore().getId() == null) return false;

        if (existingUser.getStore().getId() != storeId) return false;

        return existingUser.getRole() == Role.ADMIN ||
                existingUser.getRole() == Role.STAFF;

    }

    @Override
    public List<StoreDto> findAll() {
        List<StoreEntity> storeEntities = storeRepository.findAll();
        return storeEntities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<StoreEntity> findById(Long id) {
        return storeRepository.findById(id);
    }

    @Override
    public boolean isExistsById(Long id) {
        return storeRepository.existsById(id);
    }


    @Override
    public StoreDto partialUpdateStore(Long id, StoreEntity storeEntity) {
        storeEntity.setId(id);

        return storeRepository.findById(id).map(existingStore -> {
            Optional.ofNullable(storeEntity.getName()).ifPresent(
                    existingStore::setName);
            Optional.ofNullable(storeEntity.getCategories()).ifPresent(
                    existingStore::setCategories);
            Optional.ofNullable(storeEntity.getStoreCode()).ifPresent(
                    existingStore::setStoreCode);
            Optional.ofNullable(storeEntity.getPromptpayId()).ifPresent(
                    existingStore::setPromptpayId);
            StoreEntity savedStoreEntity = storeRepository.save(existingStore);
            return this.mapToDto(savedStoreEntity);
        }).orElseThrow(() -> new RuntimeException("Store does not exist"));
    }

    @Override
    public StoreEntity createEntity(StoreRequest request) {
        return StoreEntity.builder()
                .name(request.getName())
                .categories(List.of())
                .promptpayId(request.getPromptpayId())
                .storeCode(request.getStoreCode())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
    }

    @Override
    public StoreEntity mapAdminToEntity(StoreAdminDto storeAdminDto) {
        return StoreEntity.builder()
                .id(storeAdminDto.getId())
                .name(storeAdminDto.getName())
                .categories(storeAdminDto.getCategories())
                .storeCode(storeAdminDto.getStoreCode())
                .promptpayId(storeAdminDto.getPromptpayId())
                .build();
    }


    @Override
    public StoreDto createStore(Long userId, StoreRequest storeRequest) {
        StoreEntity storeEntity = createEntity(storeRequest);
        Optional<User> foundUser =  userRepository.findById(userId);
        if (foundUser.isEmpty())
            throw new RuntimeException("User does not exist");
        User user = foundUser.get();
        user.setStore(storeEntity);
        user.setRole(Role.ADMIN);
        storeRepository.save(storeEntity);
        return mapToDto(storeEntity);
    }

    @Override
    public void delete(Long id) {
        List<User> users = userRepository.findByStoreId(id);
        for (User user : users) {
            user.setStore(null);
            user.setRole(Role.NONE);
            userRepository.save(user);
        }

        List<ProductEntity> products = productRepository.findByStoreId(id);
        for (ProductEntity product: products) {
            productRepository.delete(product);
        }

        storeRepository.deleteById(id);
    }

    @Override
    public Optional<StoreEntity> findByStoreCode(String storeCode) {
        return storeRepository.findByStoreCode(storeCode);
    }

    @Override
    public boolean isExistsByStoreCode(String storeCode) {
        return findByStoreCode(storeCode).isPresent();
    }

    @Override
    public boolean checkCredential(StoreRequest storeRequest) {
        StoreEntity storeEntity = findByStoreCode(storeRequest.getStoreCode()).get();
        return passwordEncoder.matches(storeRequest.getPassword(),storeEntity.getPassword());
    }

    @Override
    public StoreAdminDto mapToAdminDto(StoreEntity storeEntity) {
        return StoreAdminDto.builder()
                .id(storeEntity.getId())
                .name(storeEntity.getName())
                .categories(storeEntity.getCategories())
                .storeCode(storeEntity.getStoreCode())
                .promptpayId(storeEntity.getPromptpayId())
                .build();
    }

    @Override
    public StoreDto setPassword(Long storeId, String storePassword) {
        Optional<StoreEntity> foundStore = storeRepository.findById(storeId);

        if (foundStore.isEmpty())
            throw new RuntimeException("Store not found");

        StoreEntity existingStore = foundStore.get();

        existingStore.setPassword(passwordEncoder.encode(storePassword));

        StoreEntity savedStore = storeRepository.save(existingStore);

        return mapToDto(savedStore);
    }

}
