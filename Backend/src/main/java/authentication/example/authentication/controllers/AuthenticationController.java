package authentication.example.authentication.controllers;

import authentication.example.authentication.auth.RegisterRequest;
import authentication.example.authentication.auth.AuthenticationRequest;
import authentication.example.authentication.auth.AuthenticationResponse;
import authentication.example.authentication.exceptions.CustomErrorResponse;
import authentication.example.authentication.exceptions.EmailAlreadyExistsException;
import authentication.example.authentication.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request//hold all the request information
    ){
        if (!authenticationService.isExistsByEmail(request)) {
            return ResponseEntity.ok(authenticationService.register(request));
        }
        CustomErrorResponse errorResponse = new CustomErrorResponse("Email already exists");
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request//hold all the request information
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/oauth")
    public ResponseEntity<AuthenticationResponse> oauth(
            @RequestBody RegisterRequest request//hold all the request information
    ){
        return ResponseEntity.ok(authenticationService.oauth(request));
    }
}
