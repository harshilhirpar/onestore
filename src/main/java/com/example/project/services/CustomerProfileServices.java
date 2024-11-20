package com.example.project.services;

import com.example.project.dtos.CreateCustomerProfileDto;
import com.example.project.entities.CustomerProfileEntity;
import com.example.project.entities.UserEntity;
import com.example.project.enums.LoggerInfoMessages;
import com.example.project.repositories.CustomerProfileRepository;
import com.example.project.utils.GlobalLogger;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class CustomerProfileServices {
    private final CustomerProfileRepository customerProfileRepository;
    private static final Logger logger = GlobalLogger.getLogger(CustomerProfileServices.class);

    public CustomerProfileServices(CustomerProfileRepository customerProfileRepository) {
        this.customerProfileRepository = customerProfileRepository;
    }

    public CustomerProfileEntity createProfile(UserEntity user, CreateCustomerProfileDto response){
        logger.info(String.valueOf(LoggerInfoMessages.TRY_TO_CREATE_CUSTOMER_PROFILE));
        CustomerProfileEntity customerProfile = new CustomerProfileEntity();
        customerProfile.setPhoneNumber(response.getPhoneNumber());
        customerProfile.setAddress(response.getAddress());
        customerProfile.setCity(response.getCity());
        customerProfile.setState(response.getState());
        customerProfile.setZipCode(response.getZipCode());
        customerProfile.setCountry(response.getCountry());
        customerProfile.setUser(user);
        customerProfileRepository.save(customerProfile);
        return customerProfile;
    }

    public void updateProfile(UserEntity user, CreateCustomerProfileDto response){

    }
    public void getProfile(){}
}
