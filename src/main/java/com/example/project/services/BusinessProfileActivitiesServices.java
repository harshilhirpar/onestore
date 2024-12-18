package com.example.project.services;

import com.example.project.dtos.responses.SoldProductDto;
import com.example.project.dtos.responses.SoldProductsResponseDto;
import com.example.project.entities.BusinessProfileEntity;
import com.example.project.entities.ProductEntity;
import com.example.project.entities.UserEntity;
import com.example.project.entities.UserPurchasesEntity;
import com.example.project.exceptions.NotFoundException;
import com.example.project.repositories.BusinessProfileRepository;
import com.example.project.repositories.ProductRepository;
import com.example.project.repositories.UserPurchaseRepository;
import com.example.project.utils.GlobalLogger;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BusinessProfileActivitiesServices {

    private final BusinessProfileRepository businessProfileRepository;
    private final UserPurchaseRepository userPurchaseRepository;
    private final ProductRepository productRepository;
    private static final Logger logger = GlobalLogger.getLogger(BusinessProfileActivitiesServices.class);

    public BusinessProfileActivitiesServices(
            BusinessProfileRepository businessProfileRepository,
            UserPurchaseRepository userPurchaseRepository,
            ProductRepository productRepository
    ) {
        this.businessProfileRepository = businessProfileRepository;
        this.userPurchaseRepository = userPurchaseRepository;
        this.productRepository = productRepository;
    }

    public SoldProductsResponseDto getAllSoldProducts(UserEntity user){
//        TODO: GET BUSINESS PROFILE
        logger.info("Business: Trying to find business profile");
        BusinessProfileEntity businessProfile = businessProfileRepository.findByUser(user).orElse(null);
        if(businessProfile == null){
            logger.error("Business: Business profile not found");
            throw new NotFoundException("Business Profile Not Found, Something Went Wrong");
        }
//        TODO: GET LIST OF PRODUCTS FROM USER PURCHASE TABLE
        List<UserPurchasesEntity> listOfProducts = userPurchaseRepository.findAllByBusinessProfileId(businessProfile.getId());
        List<SoldProductDto> listOfSoldProducts = new ArrayList<>();
        for(UserPurchasesEntity userPurchases: listOfProducts){
            logger.info("Business: Trying to find Product");
            String productId = userPurchases.getProductId();
            ProductEntity product = productRepository.findById(productId).orElse(null);
            if(product == null){
                logger.error("Business: Product not found");
                throw new NotFoundException("Product Not Found, Something went Wrong");
            }
            logger.info("Business: Product found, initiate dto");
            SoldProductDto soldProductDto = new SoldProductDto();
            soldProductDto.setProduct(product);
            soldProductDto.setUserId(userPurchases.getUserId());
            soldProductDto.setQuantity(userPurchases.getQuantity());
            listOfSoldProducts.add(soldProductDto);
        }
        logger.info("Business: Initiate response DTO");
        SoldProductsResponseDto soldProductsResponseDto = new SoldProductsResponseDto();
        soldProductsResponseDto.setMessage("All sold products");
        soldProductsResponseDto.setError(false);
        soldProductsResponseDto.setSoldProducts(listOfSoldProducts);
        return soldProductsResponseDto;
    }
}
