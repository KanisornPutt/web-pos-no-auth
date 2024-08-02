package authentication.example.authentication.controllers;

import authentication.example.authentication.auth.AuthenticationRequest;
import authentication.example.authentication.dto.UserDto;
import authentication.example.authentication.entities.User;
import authentication.example.authentication.services.MinioService;
import authentication.example.authentication.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    MinioService minioService;

    @GetMapping("/{email}")
    public ResponseEntity<UserDto> getUser(@PathVariable String email ){
        Optional<User> foundUser = userService.getUserByEmail(email);
        return foundUser.map(user -> {
            UserDto userDto = userService.mapToDto(user);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id){
        Optional<User> foundUser = userService.findUserById(id);
        return foundUser.map(user -> {
            UserDto userDto = userService.mapToDto(user);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @RequestParam("firstname") String firstname,
            @RequestParam("lastname") String lastname,
            @RequestParam("showName") String showName,
            @RequestParam(value = "image", required = false) MultipartFile image) throws Exception {

        if(!userService.isExistsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        UserDto userDto = UserDto.builder()
                .firstname(firstname)
                .lastname(lastname)
                .showName(showName)
                .build();

        if (image != null) {
            String imageUrl = minioService.uploadFile(image);
            userDto.setImageUrl(imageUrl);
        }


        UserDto updatedUserDto = userService.updateUser(id, userDto);

        return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
    }

    @PutMapping(path = "removeStore/{id}")
    public ResponseEntity<UserDto> removeStore(
            @PathVariable("id") Long id) {
        if(!userService.isExistsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        UserDto savedUserDto = userService.removeStore(id);
        return new ResponseEntity<>(savedUserDto, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteUser(
            @PathVariable("id") Long id,
            @RequestParam String password)  {
        userService.deleteUser(id, password);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(path = "/checkPassword/{id}")
    public ResponseEntity checkPassword(
            @PathVariable("id") Long id,
            @RequestBody AuthenticationRequest authenticationRequest)  {

        if (userService.checkPassword(id, authenticationRequest.getPassword())) {
            return  new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
