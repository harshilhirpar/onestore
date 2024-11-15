package com.example.project.controllers;

import com.example.project.dtos.CreateProductDto;
import com.example.project.entities.ProductEntity;
import com.example.project.entities.UserEntity;
import com.example.project.exceptions.BusinessProfileExceptions;
import com.example.project.exceptions.GlobalExceptionHandler;
import com.example.project.exceptions.ProductExceptions;
import com.example.project.services.ProductServices;
import com.example.project.utils.GetAuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductControllers {

    private final ProductServices productServices;
    private final GlobalExceptionHandler globalExceptionHandler;

    public ProductControllers(
            ProductServices productServices,
            GlobalExceptionHandler globalExceptionHandler
    ) {
        this.productServices = productServices;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('BUSINESS')")
    public ResponseEntity<?> createOrUpdateProduct(@RequestBody CreateProductDto createProductDto) {
        UserEntity currentUser = GetAuthenticatedUser.getAuthenticatedUser();
        try {
            ProductEntity product = productServices.createProduct(currentUser, createProductDto);
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        } catch (BusinessProfileExceptions e) {
            return globalExceptionHandler.handleBusinessProfileNotFoundException(e);
        } catch (ProductExceptions e) {
            return globalExceptionHandler.handleProductAlreadyExistsException(e);
        } catch (Exception e) {
            return globalExceptionHandler.handleGenericExceptions(e);
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('BUSINESS')")
    public ResponseEntity<?> getAllUserProducts() {
        UserEntity currentUser = GetAuthenticatedUser.getAuthenticatedUser();
        try {
            List<Optional<ProductEntity>> allProducts = productServices.findAllProductForUser(currentUser);
            return new ResponseEntity<>(allProducts, HttpStatus.OK);
        } catch (Exception e) {
            return globalExceptionHandler.handleGenericExceptions(e);
        }
    }

    @GetMapping("/{productId}")
    @PreAuthorize("hasAnyRole('BUSINESS')")
    public ResponseEntity<?> getProductById(@PathVariable String productId) {
        try {
            ProductEntity product = productServices.findProductById(productId);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (ProductExceptions e) {
            return globalExceptionHandler.handleProductNotFound(e);
        } catch (Exception e) {
            return globalExceptionHandler.handleGenericExceptions(e);
        }

    }

    @DeleteMapping("/delete/{productId}")
    @PreAuthorize("hasAnyRole('BUSINESS')")
    public ResponseEntity<?> deleteProductById(@PathVariable String productId) {
        try{
            String isDeleted = productServices.deleteProductById(productId);
            return new ResponseEntity<>(isDeleted, HttpStatus.OK);
        }catch (ProductExceptions ex){
            return globalExceptionHandler.handleProductNotFound(ex);
        } catch (Exception e) {
            return globalExceptionHandler.handleGenericExceptions(e);
        }
    }

    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('BUSINESS')")
    public ResponseEntity<?> getAllProductCountForBusiness() {
        UserEntity currentUser = GetAuthenticatedUser.getAuthenticatedUser();
        try{
            Integer productCount = productServices.getCountOfAllProductsForBusinessProfile(currentUser);
            return new ResponseEntity<>(productCount, HttpStatus.OK);
        }catch (Exception ex){
            return globalExceptionHandler.handleGenericExceptions(ex);
        }
    }

//    @GetMapping("/status/{productStatus}")
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('BUSINESS')")
    public ResponseEntity<?> getAllProductForUserBasedOnStatus(@PathVariable String status) {
        System.out.println("START");
        UserEntity currentUser = GetAuthenticatedUser.getAuthenticatedUser();
        try{
            System.out.println("HERE");
            List<?> products = productServices.getListOfProductFromStatus(currentUser, status);
//            TODO: PROVIDE A RESPONSE IF 0 PRODUCTS ARE AVAILABLE
            if(products.isEmpty()){
                return new ResponseEntity<>("SORRY, NO PRODUCTS FOUND", HttpStatus.OK);
            }
            return new ResponseEntity<>(products, HttpStatus.FOUND);
        }catch (Exception ex){
            return globalExceptionHandler.handleGenericExceptions(ex);
        }
    }

//    TODO: IMPLEMENT SEARCH
//    TODO: IMPLEMENT PAGINATION
//    TODO: IMPLEMENT PRODUCT IMAGE UPLOAD
//    TODO: IMPLEMENT PRODUCT IMAGES FOR ANOTHER SECTION UPLOAD
}
