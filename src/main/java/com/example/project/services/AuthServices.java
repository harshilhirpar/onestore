package com.example.project.services;
import com.example.project.dtos.LoginResponseDto;
import com.example.project.dtos.LoginUserDto;
import com.example.project.dtos.RegisterUserDto;
import com.example.project.entities.RoleEntity;
import com.example.project.entities.UserEntity;
import com.example.project.enums.RoleEnum;
import com.example.project.exceptions.RoleNotFoundException;
import com.example.project.exceptions.UserAlreadyExistsException;
import com.example.project.exceptions.UserNotFoundException;
import com.example.project.repositories.RoleRepository;
import com.example.project.repositories.UserRepository;
import com.example.project.utils.GlobalLogger;
import com.example.project.utils.JwtService;
import org.slf4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.EnumUtils;

@Service
public class AuthServices {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
//    Configure Logger
    private static final Logger logger = GlobalLogger.getLogger(AuthServices.class);

    public AuthServices(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }


    public UserEntity registerUserService(RegisterUserDto registerUserDto){
//        TODO: CHECK IF USER ALREADY EXISTS, THROWS EXCEPTION
        UserEntity isUserExists = userRepository.findByEmail(registerUserDto.getEmail()).orElseThrow(() -> new UserAlreadyExistsException("USER ALREADY EXISTS PLEASE LOGIN"));
        logger.info("Registering user with email: {}", registerUserDto.getEmail());
        RoleEntity role = roleRepository.findByName(RoleEnum.valueOf(registerUserDto.getRole())).orElseThrow(() -> new RoleNotFoundException(registerUserDto.getRole()));
        logger.debug("Role '{}' found for user registration.", role.getName());
        UserEntity user = new UserEntity();
        user.setFirstName(registerUserDto.getFirstName());
        user.setLastName(registerUserDto.getLastName());
        user.setEmail(registerUserDto.getEmail());
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        UserEntity savedUser = userRepository.save(user);
        logger.info("User registered successfully with email: {}", savedUser.getEmail());
        return savedUser;
    }

    public LoginResponseDto loginUserService(LoginUserDto loginUserDto){
//        TODO: CHECK IF USER EXISTS OR NOT, IF NOT THEN THROW EXCEPTION
        logger.info("Attempting to login user with email: {}", loginUserDto.getEmail());
        UserEntity authenticatedUser = authenticateUser(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setToken(jwtToken);
        loginResponseDto.setExpiresIn(jwtService.getExpirationTime());
        logger.info("User logged in successfully with email: {}", loginUserDto.getEmail());
        return loginResponseDto;
    }

    public UserEntity authenticateUser(LoginUserDto loginUserDto){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserDto.getEmail(),
                        loginUserDto.getPassword()
                )
        );
        UserEntity user =  userRepository.findByEmail(loginUserDto.getEmail()).orElseThrow(() -> new UserNotFoundException(loginUserDto.getEmail()));
        logger.debug("User authenticated successfully with email: {}", user.getEmail());
        return user;
    }

}
