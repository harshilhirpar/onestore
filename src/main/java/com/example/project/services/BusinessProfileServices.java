package com.example.project.services;
import com.example.project.entities.BusinessProfileEntity;
import com.example.project.entities.UserEntity;
import com.example.project.exceptions.BusinessProfileExceptions;
import com.example.project.repositories.BusinessProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BusinessProfileServices {
    private final BusinessProfileRepository businessProfileRepository;

    public BusinessProfileServices(BusinessProfileRepository businessProfileRepository) {
        this.businessProfileRepository = businessProfileRepository;
    }

//    --> ONE USER CAN CREATE ONLY ONE BUSINESS PROFILE
//    TODO: HANDLE ABOVE PROBLEM, CHECK IF BUSINESS PROFILE EXISTS
//    This code can work for update as well.
    public BusinessProfileEntity createBusinessProfileService(
            UserEntity user,
            BusinessProfileEntity businessProfileEntity
    ){
        BusinessProfileEntity businessProfile = businessProfileRepository.findByUser(user).orElse(null);
        if(businessProfile != null){
            throw new BusinessProfileExceptions("BUSINESS PROFILE ALREADY EXISTS, YOU CAN CREATE ONLY ONE PROFILE");
        }
        businessProfileEntity.setUser(user);
        return businessProfileRepository.save(businessProfileEntity);
    }

    public Optional<BusinessProfileEntity> findUserBusinessProfileService(UserEntity user){
        return businessProfileRepository.findByUser(user);
    }
}
