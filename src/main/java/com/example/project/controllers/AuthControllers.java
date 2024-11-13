package com.example.project.controllers;
import com.example.project.dtos.LoginResponseDto;
import com.example.project.dtos.LoginUserDto;
import com.example.project.dtos.RegisterUserDto;
import com.example.project.entities.UserEntity;
import com.example.project.exceptions.RoleNotFoundException;
import com.example.project.services.AuthServices;
import com.example.project.utils.GlobalLogger;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthControllers {
    private static final Logger logger = GlobalLogger.getLogger(AuthControllers.class);
    private final AuthServices authServices;

    public AuthControllers(AuthServices authServices) {
        this.authServices = authServices;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserDto registerUserDto){
        logger.info("Received request to register user with email: {}", registerUserDto.getEmail());
        try{
            UserEntity registeredUser = authServices.registerUserService(registerUserDto);
            logger.info("User registered successfully with email: {}", registeredUser.getEmail());
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        }catch (RoleNotFoundException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (Exception e) {
            logger.error("Registration failed for user with email {}: {}", registerUserDto.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(500).body("An unexpected error occurred during registration");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginUserDto loginUserDto){
        logger.info("Received request to log in user with email: {}", loginUserDto.getEmail());
        try {
            LoginResponseDto loginResponseDto = authServices.loginUserService(loginUserDto);
            logger.info("User logged in successfully with email: {}", loginUserDto.getEmail());
            return ResponseEntity.ok(loginResponseDto);
        } catch (BadCredentialsException bad_ex){
            return new ResponseEntity<>(bad_ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e) {
            logger.error("Login failed for user with email {}: {}", loginUserDto.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(401).body(null); // Unauthorized
        }
    }
}
