package com.example.project.controllers;

import com.example.project.dtos.CreateProductDto;
import com.example.project.entities.ProductEntity;
import com.example.project.entities.UserEntity;
import com.example.project.exceptions.BusinessProfileNotFoundException;
import com.example.project.exceptions.GlobalExceptionHandler;
import com.example.project.exceptions.ProductAlreadyExistsException;
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
    public ResponseEntity<?> createOrUpdateProduct(@RequestBody CreateProductDto createProductDto){
        UserEntity currentUser = GetAuthenticatedUser.getAuthenticatedUser();
        try{
            ProductEntity product = productServices.createProduct(currentUser, createProductDto);
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        }catch (BusinessProfileNotFoundException e){
            return globalExceptionHandler.handleBusinessProfileNotFoundException(e);
        }catch (ProductAlreadyExistsException e){
            return globalExceptionHandler.handleProductAlreadyExistsException(e);
        }catch (Exception e) {
            return globalExceptionHandler.handleGenericExceptions(e);
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('BUSINESS')")
    public ResponseEntity<?> getAllUserProducts(){
        UserEntity currentUser = GetAuthenticatedUser.getAuthenticatedUser();
        try{
            List<Optional<ProductEntity>> allProducts = productServices.findAllProductForUser(currentUser);
            return new ResponseEntity<>(allProducts, HttpStatus.OK);
        } catch (Exception e) {
            return globalExceptionHandler.handleGenericExceptions(e);
        }
    }

    @GetMapping("/{productId}")
    @PreAuthorize("hasAnyRole('BUSINESS')")
    public ResponseEntity<?> getProductById(@PathVariable String productId){
        return new ResponseEntity<>("ONE", HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyRole('BUSINESS')")
    public ResponseEntity<?> deleteProductById(@PathVariable String productId){
        return new ResponseEntity<>("DELETED", HttpStatus.OK);
    }

    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('BUSINESS)")
    public ResponseEntity<?> getAllProductCountForBusiness(){
        return new ResponseEntity<>("COUNT", HttpStatus.OK);
    }

    @GetMapping("/status/{productStatus}")
    @PreAuthorize("hasAnyRole('BUSINESS)")
    public ResponseEntity<?> getAllProductForUserBasedOnStatus(@PathVariable String productStatus){
        return new ResponseEntity<>("ALL PRODUCTS", HttpStatus.OK);
    }

//    TODO: IMPLEMENT SEARCH
//    TODO: IMPLEMENT PAGINATION
//    TODO: IMPLEMENT PRODUCT IMAGE UPLOAD
//    TODO: IMPLEMENT PRODUCT IMAGES FOR ANOTHER SECTION UPLOAD
}
