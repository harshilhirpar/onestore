package com.example.project.services;

import com.example.project.dtos.CreateCustomerProfileDto;
import com.example.project.entities.CustomerProfileEntity;
import com.example.project.entities.ProductEntity;
import com.example.project.entities.UserEntity;
import com.example.project.enums.ErrorMessagesEnum;
import com.example.project.enums.LoggerInfoMessages;
import com.example.project.exceptions.AlreadyExistsExceptions;
import com.example.project.exceptions.CustomerProfileNotFoundException;
import com.example.project.exceptions.NotFoundException;
import com.example.project.repositories.CustomerProfileRepository;
import com.example.project.repositories.ProductRepository;
import com.example.project.utils.GlobalLogger;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerProfileServices {
    private final CustomerProfileRepository customerProfileRepository;
    private final ProductServices productServices;
    private final ProductRepository productRepository;
    private static final Logger logger = GlobalLogger.getLogger(CustomerProfileServices.class);

    public CustomerProfileServices(
            CustomerProfileRepository customerProfileRepository,
            ProductServices productServices,
            ProductRepository productRepository
    ) {
        this.customerProfileRepository = customerProfileRepository;
        this.productServices = productServices;
        this.productRepository = productRepository;
    }

    public CustomerProfileEntity createProfile(UserEntity user, CreateCustomerProfileDto response){
//        TODO: EDGE CASE WHAT IF USER ALREADY HAVE PROFILE
        logger.info("Trying to check if user already have customer profile");
        CustomerProfileEntity isCustomerProfile = customerProfileRepository.findByUser(user).orElse(null);
        if(isCustomerProfile != null){
            logger.error("Customer profile already exists, cannot create profile now");
            throw new AlreadyExistsExceptions("Customer Profile Already Exists");
        }
        logger.info(String.valueOf(LoggerInfoMessages.TRY_TO_CREATE_CUSTOMER_PROFILE));
        CustomerProfileEntity customerProfile = new CustomerProfileEntity();
        customerProfile.setPhoneNumber(response.getPhoneNumber());
        customerProfile.setAddress(response.getAddress());
        customerProfile.setCity(response.getCity());
        customerProfile.setState(response.getState());
        customerProfile.setZipCode(response.getZipCode());
        customerProfile.setCountry(response.getCountry());
        customerProfile.setUser(user);
        logger.info("Saving the updated information");
        customerProfileRepository.save(customerProfile);
        return customerProfile;
    }

    public CustomerProfileEntity updateProfile(UserEntity user, CreateCustomerProfileDto response){
//        TODO: CHECK IF PROFILE EXISTS
        logger.info("Trying to find customer profile for update");
        CustomerProfileEntity customerProfile = customerProfileRepository.findByUser(user).orElse(null);
        if(customerProfile == null){
            logger.error("Customer profile not found for update");
            throw new NotFoundException("Customer Profile Not Found");
        }
        logger.info("Customer profile found for update");
        logger.info("Trying to update profile");
//        IF PROFILE EXISTS THEN UPDATE THE PROFILE
        customerProfile.setPhoneNumber(response.getPhoneNumber());
        customerProfile.setAddress(response.getAddress());
        customerProfile.setCity(response.getCity());
        customerProfile.setState(response.getState());
        customerProfile.setZipCode(response.getZipCode());
        customerProfile.setCountry(response.getCountry());
        logger.info("Saving the updated information");
        customerProfileRepository.save(customerProfile);
        return customerProfile;
    }


    public CustomerProfileEntity getProfile(UserEntity user){
        logger.info("Trying to fetching customer profile");
        CustomerProfileEntity customerProfile = customerProfileRepository.findByUser(user).orElse(null);
        if(customerProfile == null){
            logger.error("Customer profile not found");
            throw new NotFoundException("Customer profile not found");
        }
        logger.info("Customer profile fetched");
        return customerProfile;
    }

    public List<ProductEntity> getAllProductForCustomer(int page, int size){
//        What if someone requests more pages that are not available, then in that cas we can just send the last page, if requested page is exceded then we can send the last page only no matter how many request user sends.

        logger.info("Trying to fetching products for customer");
        List<ProductEntity> allProducts = (List<ProductEntity>) productRepository.findAll();
        int pages = (allProducts.size()) / size;
        if(page > pages){
            logger.info("Requested");
            return productServices.getPaginatedProducts(pages, size);
        }
        return productServices.getPaginatedProducts(page, size);
    }
}
