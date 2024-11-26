package com.example.project.services;

import com.example.project.entities.BusinessProfileEntity;
import com.example.project.entities.ProductEntity;
import com.example.project.entities.RoleEntity;
import com.example.project.entities.UserEntity;
import com.example.project.enums.BusinessStatus;
import com.example.project.enums.ProductStatusEnum;
import com.example.project.enums.RoleEnum;
import com.example.project.repositories.BusinessProfileRepository;
import com.example.project.repositories.ProductRepository;
import com.example.project.repositories.RoleRepository;
import com.example.project.repositories.UserRepository;
import com.example.project.utils.GlobalLogger;
import org.slf4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SeederService {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final BusinessProfileRepository businessProfileRepository;
    private static final Logger logger = GlobalLogger.getLogger(ProductServices.class);
    Random rand = new Random();

    public SeederService(
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            ProductRepository productRepository,
            BusinessProfileRepository businessProfileRepository
    ) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.businessProfileRepository = businessProfileRepository;
    }

    public void loadData() {
        loadRoles();
        load();
    }

    private void loadRoles() {
        logger.info("Trying to add roles");
        RoleEnum[] roleNames = new RoleEnum[]{RoleEnum.CUSTOMER, RoleEnum.ADMIN, RoleEnum.BUSINESS};
        Map<RoleEnum, String> roleDescriptionMap = Map.of(
                RoleEnum.CUSTOMER, "End User",
                RoleEnum.ADMIN, "Administrator role",
                RoleEnum.BUSINESS, "Business Person"
        );

        Arrays.stream(roleNames).forEach((roleName) -> {
            logger.info("Add this role: {}", roleName);
            Optional<RoleEntity> optionalRole = roleRepository.findByName(roleName);
            optionalRole.ifPresentOrElse(System.out::println, () -> {
                RoleEntity roleToCreate = new RoleEntity();
                roleToCreate.setName(roleName);
                roleToCreate.setDescription(roleDescriptionMap.get(roleName));
                roleRepository.save(roleToCreate);
            });
        });
    }

    private void load() {
        for (int i = 0; i < 10; i++) {
            logger.info("Generating names and emails");
            String cFName = "c_user_f_name" + i;
            String cLName = "c_user_l_name" + i;
            String cEmail = "c_user_email" + i + "@gmail.com";
            UserEntity user = createUser("CUSTOMER", cFName, cLName, cEmail);

            String aFName = "a_user_f_name" + i;
            String aLName = "a_user_l_name" + i;
            String aEmail = "a_user_email" + i + "@gmail.com";
            UserEntity adminUser = createUser("ADMIN", aFName, aLName, aEmail);

            String bFName = "b_user_f_name" + i;
            String bLName = "b_user_l_name" + i;
            String bEmail = "b_user_email" + i + "@gmail.com";
            UserEntity businessProfileUser = createUser("BUSINESS", bFName, bLName, bEmail);
            String businessName = "b_name" + i;
            String businessType = "Wholesale";
            if (i % 2 == 0) {
                businessType = "retail";
            }
            String rNum = "TH12345678" + i;
            BusinessProfileEntity businessProfile = createBusinessProfile(businessProfileUser, businessName, businessType, rNum);
            for (int p = 0; p < 20; p++) {
                String name = "product_name" + businessProfile.getBusinessName() + i;
                Double price = rand.nextDouble() * 100;
                Integer quantity = rand.nextInt(200);
                ProductEntity product = new ProductEntity();
                product.setName(name);
                product.setDescription("TEST DESC");
                product.setPrice(price);
                product.setQuantity(quantity);
                product.setCategory("ELECTRONICS");
                product.setStatus(ProductStatusEnum.valueOf("ACTIVE"));
                product.setBusinessProfile(businessProfile);
                productRepository.save(product);
            }
        }
    }

    private BusinessProfileEntity createBusinessProfile(UserEntity user, String bName, String bType, String rNum) {
        BusinessProfileEntity businessProfile = new BusinessProfileEntity();
        businessProfile.setBusinessName(bName);
        businessProfile.setBusinessType(bType);
        businessProfile.setRegistrationNumber(rNum);
        businessProfile.setTaxIdNumber("TG-987654321");
        businessProfile.setBusinessEmail("contact@techgizmos.com");
        businessProfile.setBusinessPhone("+1-800-123-4567");
        businessProfile.setBusinessAddress("123 Tech Street, Silicon Valley, CA");
        businessProfile.setBusinessWebsite("website");
        businessProfile.setBusinessFacebook("facebook");
        businessProfile.setBusinessInstagram("instagram");
        businessProfile.setBusinessLogo("logo");
        businessProfile.setStatus(BusinessStatus.valueOf("ACTIVE"));
        businessProfile.setUser(user);
        return businessProfileRepository.save(businessProfile);
    }

    private UserEntity createUser(String role, String fName, String lName, String email) {
        UserEntity user = new UserEntity();
        user.setFirstName(fName);
        user.setLastName(lName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("123"));
        if (Objects.equals(role, "CUSTOMER")) {
            RoleEntity userRole = roleRepository.findByName(RoleEnum.valueOf("CUSTOMER")).orElse(null);
            user.setRole(userRole);
        }
        if (Objects.equals(role, "ADMIN")) {
            RoleEntity adminRole = roleRepository.findByName(RoleEnum.valueOf("ADMIN")).orElse(null);
            user.setRole(adminRole);
        }
        if (Objects.equals(role, "BUSINESS")) {
            RoleEntity businessRole = roleRepository.findByName(RoleEnum.valueOf("BUSINESS")).orElse(null);
            user.setRole(businessRole);
        }
        return userRepository.save(user);
    }

}
