package com.example.project.services;

import com.example.project.dtos.CreateProductDto;
import com.example.project.entities.BusinessProfileEntity;
import com.example.project.entities.ProductEntity;
import com.example.project.entities.UserEntity;
import com.example.project.enums.ProductStatusEnum;
import com.example.project.exceptions.BusinessProfileExceptions;
import com.example.project.exceptions.ProductExceptions;
import com.example.project.repositories.BusinessProfileRepository;
import com.example.project.repositories.ProductRepository;
import com.example.project.utils.GlobalLogger;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
            throw new ProductExceptions("PRODUCT ALREADY EXISTS IN YOUR PROFILE");
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

    public ProductEntity findProductById(String productId){
        ProductEntity product = productRepository.findById(productId).orElse(null);
        if(product == null){
            throw new ProductExceptions("PRODUCT NOT FOUND");
        }
        return product;
    }

    public BusinessProfileEntity findBusinessProfile(UserEntity user){
        logger.info("Attempting to find Business Profile with user: {}", user.getEmail());
        BusinessProfileEntity businessProfile = businessProfileRepository.findByUser(user).orElse(null);
        if(businessProfile == null){
            throw new BusinessProfileExceptions("BUSINESS PROFILE NOT FOUND");
        }
        logger.info("Successfully found Business Profile");
        return businessProfile;
    }

    public String deleteProductById(String productId){
        logger.info("Attempting to delete Product with id: {}", productId);
        ProductEntity product = productRepository.findById(productId).orElse(null);
        if (product == null){
            throw new ProductExceptions("PRODUCT NOT FOUND, PLEASE PROVIDE VALID INFO");
        }
        productRepository.deleteById(productId);
//        TODO: CAN ADD CHECK IF DATA IS REALLY DELETED
        logger.info("Product Deleted Successfully");
        return "TRUE";
    }

    public Integer getCountOfAllProductsForBusinessProfile(UserEntity user){
//        TODO: FIND BUSINESS PROFILE
        BusinessProfileEntity businessProfile = businessProfileRepository.findByUser(user).orElse(null);
//        TODO: FROM BUSINESS PROFILE FIND PRODUCTS
        return productRepository.findAllByBusinessProfile(businessProfile).size();
    }

    public List<?> getListOfProductFromStatus(UserEntity user, String productStatus){
//        TODO: HANDLE WRONG ENUM TYPE EXCEPTION
//        TODO: FIND BUSINESS PROFILE
        logger.info("TRY TO FIND BUSINESS PROFILE");
        BusinessProfileEntity businessProfile = businessProfileRepository.findByUser(user).orElse(null);
        logger.info("BUSINESS PROFILE FOUND");
        if(businessProfile == null){
            throw new BusinessProfileExceptions("BUSINESS PROFILE NOT FOUND, SOMETHING WENT WRONG");
        }
//        TODO: FROM BUSINESS PROFILE FIND PRODUCTS
        List<Optional<ProductEntity>> products = productRepository.findAllByBusinessProfile(businessProfile);
        logger.info("FOUNT PRODUCTS: {}", products.size());
//        TODO: GET ALL THE PRODUCTS
        List<ProductEntity> productWithSameStatus = new ArrayList<ProductEntity>();
//        TODO: FROM ALL THE PRODUCTS SAVE THE PRODUCTS WITH SAME STATUS INTO ONE LIST AND RETURN IT
        for(Optional<ProductEntity> product : products){
            if(ProductStatusEnum.valueOf(String.valueOf(product.get().getStatus())) == ProductStatusEnum.valueOf(productStatus)){
                productWithSameStatus.add(product.get());
            }
        }
        return productWithSameStatus;
    }
}
