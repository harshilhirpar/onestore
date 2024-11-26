package com.example.project.utils;

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
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Seeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final BusinessProfileRepository businessProfileRepository;

    public Seeder(
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


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.loadRoles();
        this.loadUsers();
        this.loadBusinessProfile();
//        this.addProducts();
    }

    private void loadUsers(){
//        TODO: CREATE 3 USERS FOR EACH ROLE
        RoleEntity userRole = roleRepository.findByName(RoleEnum.valueOf("CUSTOMER")).orElse(null);
        RoleEntity businessRole = roleRepository.findByName(RoleEnum.valueOf("BUSINESS")).orElse(null);
        RoleEntity adminRole = roleRepository.findByName(RoleEnum.valueOf("ADMIN")).orElse(null);

        UserEntity user1 = new UserEntity();
        user1.setFirstName("Harshil");
        user1.setLastName("Hirpara");
        user1.setEmail("harshil@gmail.com");
        user1.setPassword(passwordEncoder.encode("123"));
        user1.setRole(userRole);

        UserEntity user2 = new UserEntity();
        user2.setFirstName("Jainam");
        user2.setLastName("Patel");
        user2.setEmail("jainam@gmail.com");
        user2.setPassword(passwordEncoder.encode("123"));
        user2.setRole(businessRole);

        UserEntity user3 = new UserEntity();
        user3.setFirstName("Om");
        user3.setLastName("Patel");
        user3.setEmail("om@gmail.com");
        user3.setPassword(passwordEncoder.encode("123"));
        user3.setRole(adminRole);
        if(userRepository.findByEmail(user1.getEmail()).orElse(null) == null &&
                userRepository.findByEmail(user2.getEmail()).orElse(null) == null &&
                userRepository.findByEmail(user3.getEmail()).orElse(null) == null
        ){
            System.out.println("USER NOT FOUND SO CREATE");

            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
        }
    }

    private void loadRoles() {
        RoleEnum[] roleNames = new RoleEnum[] { RoleEnum.CUSTOMER, RoleEnum.ADMIN, RoleEnum.BUSINESS };
        Map<RoleEnum, String> roleDescriptionMap = Map.of(
                RoleEnum.CUSTOMER, "End User",
                RoleEnum.ADMIN, "Administrator role",
                RoleEnum.BUSINESS, "Business Person"
        );

        Arrays.stream(roleNames).forEach((roleName) -> {
            System.out.println(roleName);
            Optional<RoleEntity> optionalRole = roleRepository.findByName(roleName);
            System.out.println(optionalRole);
            optionalRole.ifPresentOrElse(System.out::println, () -> {
                RoleEntity roleToCreate = new RoleEntity();
                roleToCreate.setName(roleName);
                roleToCreate.setDescription(roleDescriptionMap.get(roleName));
                roleRepository.save(roleToCreate);
            });
        });
    }

    private void loadBusinessProfile(){
        UserEntity finduser = userRepository.findByEmail("jainam@gmail.com").orElse(null);
        if(finduser == null){
            System.out.println("USER NOT FOUND");
        }
        if(businessProfileRepository.findByUser(finduser).orElse(null) == null){
            BusinessProfileEntity businessProfile = new BusinessProfileEntity();
            businessProfile.setBusinessName("TechGizmos");
            businessProfile.setBusinessType("Electronics Retailer");
            businessProfile.setRegistrationNumber("TG123456789");
            businessProfile.setTaxIdNumber("TG-987654321");
            businessProfile.setBusinessEmail("contact@techgizmos.com");
            businessProfile.setBusinessPhone("+1-800-123-4567");
            businessProfile.setBusinessAddress("123 Tech Street, Silicon Valley, CA");
            businessProfile.setBusinessWebsite("website");
            businessProfile.setBusinessFacebook("facebook");
            businessProfile.setBusinessInstagram("instagram");
            businessProfile.setBusinessLogo("logo");
            businessProfile.setStatus(BusinessStatus.valueOf("ACTIVE"));
            businessProfile.setUser(finduser);
            businessProfileRepository.save(businessProfile);
        }
    }

    private void addProducts(){
        List<ProductEntity> productEntities = new ArrayList<>();
        List<String> names = new ArrayList<>();
        for(int i=0; i<100; i++){
            String name = "TP" + i;
            names.add(name);
        }

        for (String name : names) {
            ProductEntity product = createProduct(name);
            System.out.println("product Created");
            productRepository.save(product);
        }
    }

    private ProductEntity createProduct(String name){
        UserEntity finduser = userRepository.findByEmail("jainam@gmail.com").orElse(null);
        if(finduser == null){
            System.out.println("USER NOT FOUND");
        }
        BusinessProfileEntity businessProfile = businessProfileRepository.findByUser(finduser).orElse(null);
        if(businessProfile == null){
            System.out.println("BUSINESS PROFILE NOT FOUND");
        }
        ProductEntity product = new ProductEntity();
        product.setName(name);
        product.setDescription("TEST DESC");
        product.setPrice(69.99);
        product.setQuantity(154);
        product.setCategory("ELECTRONICS");
        product.setStatus(ProductStatusEnum.valueOf("ACTIVE"));
        product.setBusinessProfile(businessProfile);
        return product;
    }
}
