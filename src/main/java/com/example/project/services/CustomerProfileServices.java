package com.example.project.services;

import com.example.project.dtos.CreateCustomerProfileDto;
import com.example.project.entities.CartEntity;
import com.example.project.entities.CustomerProfileEntity;
import com.example.project.entities.ProductEntity;
import com.example.project.entities.UserEntity;
import com.example.project.enums.LoggerInfoMessages;
import com.example.project.exceptions.AlreadyExistsExceptions;
import com.example.project.exceptions.NotFoundException;
import com.example.project.repositories.CartRepository;
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
    private final CartRepository cartRepository;
    private static final Logger logger = GlobalLogger.getLogger(CustomerProfileServices.class);

    public CustomerProfileServices(
            CustomerProfileRepository customerProfileRepository,
            ProductServices productServices,
            ProductRepository productRepository,
            CartRepository cartRepository
    ) {
        this.customerProfileRepository = customerProfileRepository;
        this.productServices = productServices;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
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

    public ProductEntity getProductFromId(String productId){
        logger.info("Trying to find product with id: {}", productId);
        ProductEntity product = productRepository.findById(productId).orElse(null);
        if(product == null){
            logger.error("Product not found with id: {}", productId);
            throw new NotFoundException("Product Not Found");
        }
        logger.info("Product found with id: {}", productId);
        return product;
    }

//    TODO: ADD TO CART
//    TODO: NEED USER ID
//    TODO: NEED PRODUCT ID
//    TODO: NEED QUANTITY
    public CartEntity addToCart(String userId, String productId){
        logger.info("Trying to add product to cart");
        logger.info("Check if product exists");
        ProductEntity product = productRepository.findById(productId).orElse(null);
        if(product == null){
            logger.info("Product not found with id: {}", productId);
            throw new NotFoundException("Product Not Found");
        }
        logger.info("Product found to add in cart with id: {}", productId);
        CartEntity cartEntity = new CartEntity();
        cartEntity.setUserId(userId);
        cartEntity.setProductId(productId);
        cartEntity.setQuantity(1);
        logger.info("Adding the product in cart");
        return cartRepository.save(cartEntity);
    }

    public CartEntity increaseQuantityInCart(String userId, String productId){
        logger.info("Trying to increase quantity of product in cart");
        ProductEntity product = productRepository.findById(productId).orElse(null);
        if(product == null){
            logger.info("Product not found with id: {}", productId);
            throw new NotFoundException("Product Not Found");
        }
        logger.info("Product found to increase quantity in cart: {}", productId);
        CartEntity cart = cartRepository.findByUserIdAndProductId(userId, productId).orElse(null);
        if(cart == null){
            throw new NotFoundException("Something went wrong");
        }
        cart.setQuantity(cart.getQuantity() + 1);
        return cartRepository.save(cart);
    }

    public CartEntity decreaseQuantityInCart(String userId, String productId){
        logger.info("Trying to decrease quantity of product in cart");
        ProductEntity product = productRepository.findById(productId).orElse(null);
        if(product == null){
            logger.info("Product not found with id: {}", productId);
            throw new NotFoundException("Product Not Found");
        }
        logger.info("Product found to decrease quantity in cart: {}", productId);
        CartEntity cart = cartRepository.findByUserIdAndProductId(userId, productId).orElse(null);
        if(cart == null){
            throw new NotFoundException("Something went wrong");
        }
        Integer quantity = cart.getQuantity();
        if(quantity == 1){
//            TODO: QUANTITY IS ALREADY 1 SO IF DECREASE MEANS REMOVE PRODUCT FROM CART
            cartRepository.delete(cart);
            return cart;
        }
        cart.setQuantity(quantity - 1);
        return cartRepository.save(cart);
    }
}
