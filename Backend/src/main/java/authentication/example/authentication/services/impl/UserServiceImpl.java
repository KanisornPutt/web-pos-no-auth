package authentication.example.authentication.services.impl;

import authentication.example.authentication.auth.StoreRequest;
import authentication.example.authentication.dto.UserDto;
import authentication.example.authentication.entities.Role;
import authentication.example.authentication.entities.StoreEntity;
import authentication.example.authentication.entities.User;
import authentication.example.authentication.repositories.StoreRepository;
import authentication.example.authentication.repositories.UserRepository;
import authentication.example.authentication.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDto mapToDto(User userEntity) {
        return UserDto.builder()
                .userId(userEntity.getId())
                .showName(userEntity.getShow_name())
                .firstname(userEntity.getFirstname())
                .lastname(userEntity.getLastname())
                .email(userEntity.getEmail())
                .role(userEntity.getRole())
                .imageUrl(userEntity.getImageUrl())
                .storeId(userEntity.getStore() != null ? userEntity.getStore().getId() : null)
                .build();
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);

    }

    @Override
    public Boolean isExistsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        userDto.setUserId(id);
        return userRepository.findById(id).map(existingUser -> {
            Optional.ofNullable(userDto.getShowName()).ifPresent(
                            existingUser::setShow_name);
            Optional.ofNullable(userDto.getFirstname()).ifPresent(
                            existingUser::setFirstname);
            Optional.ofNullable(userDto.getLastname()).ifPresent(
                            existingUser::setLastname);
            Optional.ofNullable(userDto.getImageUrl()).ifPresent(
                            existingUser::setImageUrl);
            User savedUserEntity = userRepository.save(existingUser);
            return this.mapToDto(savedUserEntity);
        }).orElseThrow(() -> new RuntimeException("User does not exist"));
    }

    @Override
    public UserDto setUserToStore(Long userId, StoreRequest storeRequest) {
        User user = userRepository.findById(userId).get();
        StoreEntity storeEntity = storeRepository.findByStoreCode(storeRequest.getStoreCode()).get();
        user.setStore(storeEntity);
        user.setRole(Role.STAFF);
        User savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }

    @Override
    public UserDto removeStore(Long id) {
        User user = userRepository.findById(id).get();
        user.setStore(null);
        user.setRole(Role.NONE);
        return mapToDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id, String password) {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isPresent()) {
            User existingUser = foundUser.get();
            if (passwordEncoder.matches(password, existingUser.getPassword()))
                userRepository.deleteById(id);
        }
    }

    @Override
    public boolean checkPassword(Long id, String password) {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isPresent()) {
            User existingUser = foundUser.get();
            return passwordEncoder.matches(password, existingUser.getPassword());
        } else
            return false;
    }

    @Override
    public List<UserDto> getUserByStoreId(Long storeId) {
        return userRepository.findByStoreId(storeId).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public UserDto makeAdmin(Long userId, Long storeId) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isEmpty()) throw new RuntimeException("User not found");
        Optional<StoreEntity> foundStore = storeRepository.findById(storeId);
        if (foundStore.isEmpty()) throw new RuntimeException("Store not found");

        User user = foundUser.get();
        StoreEntity storeEntity = foundStore.get();

        if (user.getStore() != storeEntity) throw new RuntimeException("Improper assignment");

        user.setRole(Role.ADMIN);

        User savedUser = userRepository.save(user);

        return mapToDto(savedUser);
    }

    @Override
    public UserDto removeAsAdmin(Long userId, Long storeId) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isEmpty()) throw new RuntimeException("User not found");
        Optional<StoreEntity> foundStore = storeRepository.findById(storeId);
        if (foundStore.isEmpty()) throw new RuntimeException("Store not found");

        User user = foundUser.get();
        StoreEntity storeEntity = foundStore.get();

        if (user.getStore() != storeEntity) throw new RuntimeException("Improper assignment");

        user.setRole(Role.STAFF);

        User savedUser = userRepository.save(user);

        return mapToDto(savedUser);
    }

    @Override
    public UserDto removeFromStore(Long userId, Long storeId) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isEmpty()) throw new RuntimeException("User not found");
        Optional<StoreEntity> foundStore = storeRepository.findById(storeId);
        if (foundStore.isEmpty()) throw new RuntimeException("Store not found");

        User user = foundUser.get();
        StoreEntity storeEntity = foundStore.get();

        if (user.getStore() != storeEntity) throw new RuntimeException("Improper assignment");

        user.setRole(Role.NONE);
        user.setStore(null);

        User savedUser = userRepository.save(user);

        return mapToDto(savedUser);
    }
}
