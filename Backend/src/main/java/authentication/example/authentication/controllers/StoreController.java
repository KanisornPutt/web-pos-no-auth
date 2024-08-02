package authentication.example.authentication.controllers;

import authentication.example.authentication.auth.StoreRequest;
import authentication.example.authentication.dto.StoreAdminDto;
import authentication.example.authentication.dto.StoreDto;
import authentication.example.authentication.dto.UserDto;
import authentication.example.authentication.entities.StoreEntity;
import authentication.example.authentication.entities.User;
import authentication.example.authentication.exceptions.CustomErrorResponse;
import authentication.example.authentication.services.StoreService;
import authentication.example.authentication.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final UserService userService;

    @PostMapping(path = "/{userId}")
    public ResponseEntity<StoreDto> createStore(
            @PathVariable("userId") Long id ,
            @RequestBody StoreRequest storeRequest) {

        Optional<User> foundUser = userService.findUserById(id);
        if (foundUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = foundUser.get();
        if (user.getStore() != null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        return new ResponseEntity<>(storeService.createStore(id , storeRequest), HttpStatus.CREATED);
    }


    @GetMapping(path = "/users/{storeId}/{userId}")
    public ResponseEntity<List<UserDto>> getUsersFromStoreId(
            @PathVariable("storeId") Long storeId,
            @PathVariable("userId") Long userId) {

        if (!storeService.isStaff(userId, storeId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        List<UserDto> users = userService.getUserByStoreId(storeId);
        return new ResponseEntity<>(users, HttpStatus.OK);

    }

    @GetMapping(path = "/{storeId}/{userId}")
    public ResponseEntity<StoreDto> getStoreById(
            @PathVariable("storeId") Long storeId,
            @PathVariable("userId") Long userId) {

        if (!storeService.isStaff(userId, storeId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Optional<StoreEntity> foundStore = storeService.findById(storeId);
        return foundStore.map(storeEntity -> {
            StoreDto storeDto = storeService.mapToDto(storeEntity);
            return new ResponseEntity<>(storeDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/admin/{storeId}/{userId}")
    public ResponseEntity<StoreAdminDto> getAdminData(
            @PathVariable("storeId") Long storeId,
            @PathVariable("userId") Long userId) {

        if (!storeService.isAdmin(userId, storeId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Optional<StoreEntity> foundStoreEntity = storeService.findById(storeId);

        if (foundStoreEntity.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        StoreEntity storeEntity = foundStoreEntity.get();

        StoreAdminDto storeAdminDto = storeService.mapToAdminDto(storeEntity);
        return new ResponseEntity<>(storeAdminDto, HttpStatus.OK);
    }


    @PutMapping(path = "/{storeId}/{userId}")
    public ResponseEntity<StoreDto> UpdateStore(
            @PathVariable("storeId") Long storeId,
            @PathVariable("userId") Long userId,
            @RequestBody StoreAdminDto storeAdminDto) {
        if (!storeService.isAdmin(userId, storeId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);


        if (!storeService.isExistsById(storeId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        StoreEntity storeEntity = storeService.mapAdminToEntity(storeAdminDto);
        StoreDto savedStoreDto = storeService.partialUpdateStore(storeId ,storeEntity);

        return new ResponseEntity<>(savedStoreDto, HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{storeId}/{userId}")
    public ResponseEntity deleteStore(
            @PathVariable("storeId") Long storeId,
            @PathVariable("userId") Long userId,
            @RequestParam("userPassword") String userPassword) {

        System.out.println(userPassword);

        if (!userService.checkPassword(userId, userPassword))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if (!storeService.isAdmin(userId, storeId))
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        storeService.delete(storeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PutMapping(path = "/join/{userId}")
    public ResponseEntity setUserToStore(
            @PathVariable("userId") Long userId,
            @RequestBody StoreRequest storeRequest) {
        if (!userService.isExistsById(userId)) {
            return new ResponseEntity<>(new CustomErrorResponse("user does not exist"), HttpStatus.NOT_FOUND);
        }
        if (!storeService.isExistsByStoreCode(storeRequest.getStoreCode())) {
            return new ResponseEntity<>(new CustomErrorResponse("Incorrect Store Code or Password"), HttpStatus.NOT_FOUND);
        }

        UserDto userDto = userService.mapToDto(userService.findUserById(userId).get());
        if (userDto.getStoreId() != null) {
            return new ResponseEntity<>(new CustomErrorResponse("User is linked to an existing store!! Please remove user from the other store before setting to a new store."), HttpStatus.NOT_FOUND);
        }

        if (!storeService.checkCredential(storeRequest)) {
            return new ResponseEntity<>(new CustomErrorResponse("Incorrect Store Code or Password"), HttpStatus.NOT_ACCEPTABLE);
        }

        UserDto savedUserDto = userService.setUserToStore(userId, storeRequest);
        return new ResponseEntity<>(savedUserDto, HttpStatus.OK);
    }

    @PutMapping(path = "/resetPassword/{storeId}/{userId}")
    public ResponseEntity<StoreDto> resetPassword(
            @PathVariable("storeId") Long storeId,
            @PathVariable("userId") Long userId,
            @RequestParam("userPassword") String userPassword,
            @RequestParam("newStorePassword") String storePassword) {

        if (!storeService.isAdmin(userId, storeId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if (!userService.checkPassword(userId, userPassword))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        StoreDto savedStore = storeService.setPassword(storeId, storePassword);
        return new ResponseEntity<>(savedStore, HttpStatus.OK);
    }

    @PutMapping(path = "/makeAdmin/{storeId}/{adminId}/{userId}")
    public ResponseEntity<UserDto> makeAdmin(
            @PathVariable("storeId")  Long storeId,
            @PathVariable("adminId") Long adminId,
            @PathVariable("userId")  Long userId,
            @RequestParam("adminPassword") String adminPassword) {

        if (!storeService.isAdmin(adminId, storeId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if (!storeService.isStaff(userId, storeId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if (!userService.checkPassword(adminId, adminPassword))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        UserDto savedUser = userService.makeAdmin(userId, storeId);

        return new ResponseEntity<>(savedUser, HttpStatus.OK);

    }

    @PutMapping(path = "/removeAsAdmin/{storeId}/{adminId}/{userId}")
    public ResponseEntity<UserDto> removeAsAdmin(
            @PathVariable("storeId")  Long storeId,
            @PathVariable("adminId") Long adminId,
            @PathVariable("userId")  Long userId,
            @RequestParam("adminPassword") String adminPassword) {

        if (!storeService.isAdmin(adminId, storeId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if (!storeService.isAdmin(userId, storeId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if (!userService.checkPassword(adminId, adminPassword))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        UserDto savedUser = userService.removeAsAdmin(userId, storeId);

        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }


    @PutMapping(path = "/removeFromStore/{storeId}/{adminId}/{userId}")
    public ResponseEntity<UserDto> removeFromStore(
            @PathVariable("storeId")  Long storeId,
            @PathVariable("adminId") Long adminId,
            @PathVariable("userId")  Long userId,
            @RequestParam("adminPassword") String adminPassword) {

        if (!storeService.isAdmin(adminId, storeId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if (!storeService.isStaff(userId, storeId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if (!userService.checkPassword(adminId, adminPassword))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        UserDto savedUser = userService.removeFromStore(userId, storeId);

        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }


    @GetMapping(path = "")
    public List<StoreDto> listStores() {
        return storeService.findAll();
    }


}
