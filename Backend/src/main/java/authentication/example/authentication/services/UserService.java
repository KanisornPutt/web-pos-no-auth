package authentication.example.authentication.services;

import authentication.example.authentication.auth.StoreRequest;
import authentication.example.authentication.dto.UserDto;
import authentication.example.authentication.entities.User;

import java.util.List;
import java.util.Optional;


public interface UserService {

    UserDto mapToDto(User userEntity);
    Optional<User> getUserByEmail(String email);
    Optional<User> findUserById(Long id);
    Boolean isExistsById(Long id);
    UserDto updateUser(Long id, UserDto userDto);

    UserDto setUserToStore(Long userId, StoreRequest storeRequest);

    UserDto removeStore(Long id);

    void deleteUser(Long id, String password);

    boolean checkPassword(Long id, String password);

    List<UserDto> getUserByStoreId(Long storeId);

    UserDto makeAdmin(Long userId, Long storeId);

    UserDto removeAsAdmin(Long userId, Long storeId);

    UserDto removeFromStore(Long userId, Long storeId);
}
