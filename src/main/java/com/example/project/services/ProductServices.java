package com.example.project.services;

import com.example.project.dtos.CreateProductDto;
import com.example.project.entities.BusinessProfileEntity;
import com.example.project.entities.ProductEntity;
import com.example.project.entities.UserEntity;
import com.example.project.enums.ProductStatusEnum;
import com.example.project.exceptions.BusinessProfileNotFoundException;
import com.example.project.exceptions.ProductAlreadyExistsException;
import com.example.project.repositories.BusinessProfileRepository;
import com.example.project.repositories.ProductRepository;
import com.example.project.utils.GlobalLogger;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServices {

    private final ProductRepository productRepository;
    private final BusinessProfileRepository businessProfileRepository;
    private static final Logger logger = GlobalLogger.getLogger(ProductServices.class);

    public ProductServices(
            ProductRepository productRepository,
            BusinessProfileRepository businessProfileRepository
    ) {
        this.productRepository = productRepository;
        this.businessProfileRepository = businessProfileRepository;
    }

    public ProductEntity createProduct(UserEntity user, CreateProductDto createProductDto){
//        TODO: FIND THE BUSINESS PROFILE BECAUSE IT IS LINKED WITH PRODUCTS
//        TODO: CHECK IF THE PRODUCT WITH SAME NAME ALREADY EXISTS, WITH SAME BUSINESS PROFILE
        BusinessProfileEntity businessProfile = findBusinessProfile(user);
        ProductEntity isProductExists = productRepository.findByNameAndBusinessProfile(createProductDto.getName(), businessProfile).orElse(null);
        if(isProductExists != null){
            throw new ProductAlreadyExistsException("PRODUCT ALREADY EXISTS IN YOUR PROFILE");
        }
        logger.info("Attempting to create product");
        ProductEntity newProduct = new ProductEntity();
//        TODO: CREATE NEW PRODUCT
        newProduct.setName(createProductDto.getName());
        newProduct.setDescription(createProductDto.getDescription());
        newProduct.setPrice(createProductDto.getPrice());
        newProduct.setQuantity(createProductDto.getQuantity());
        newProduct.setCategory(createProductDto.getCategory());
        newProduct.setStatus(ProductStatusEnum.valueOf(createProductDto.getStatus()));
        newProduct.setBusinessProfile(businessProfile);
        ProductEntity product = productRepository.save(newProduct);
        logger.info("Product Created Successfully with name: {}", product.getName());
        return product;
    }

    public List<Optional<ProductEntity>> findAllProductForUser(UserEntity user){
        BusinessProfileEntity businessProfile = findBusinessProfile(user);
        logger.info("Attempting to find all products");
        List<Optional<ProductEntity>> allProducts = productRepository.findAllByBusinessProfile(businessProfile);
        logger.info("Successfully found all products");
        return allProducts;
    }

    public BusinessProfileEntity findBusinessProfile(UserEntity user){
        logger.info("Attempting to find Business Profile with user: {}", user.getEmail());
        BusinessProfileEntity businessProfile = businessProfileRepository.findByUser(user).orElseThrow(()-> new BusinessProfileNotFoundException("Business profile Not Found"));
        logger.info("Successfully found Business Profile");
        return businessProfile;
    }
}
