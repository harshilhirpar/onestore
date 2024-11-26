package com.example.project.controllers;

import com.example.project.dtos.CreateProductDto;
import com.example.project.dtos.responses.UploadMultipleImagesResponseDto;
import com.example.project.dtos.responses.UploadThumbnailResponseDto;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


// THIS CONTROLLER WILL HANDLE BELLOW REQUESTS
// /ADD PRODUCT
// /UPDATE PRODUCT
// /GET PRODUCT BY ID
// /GET ALL PRODUCTS FOR LOGGED IN USER
// /GET PAGINATED  PRODUCTS
@CrossOrigin("*")
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
    public ResponseEntity<?> getAllUserProducts(@RequestParam int page, @RequestParam int size) {
        UserEntity currentUser = GetAuthenticatedUser.getAuthenticatedUser();
        try {
            List<ProductEntity> allProducts = productServices.findAllProductForUser(currentUser, page, size);
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
        try {
            String isDeleted = productServices.deleteProductById(productId);
            return new ResponseEntity<>(isDeleted, HttpStatus.OK);
        } catch (ProductExceptions ex) {
            return globalExceptionHandler.handleProductNotFound(ex);
        } catch (Exception e) {
            return globalExceptionHandler.handleGenericExceptions(e);
        }
    }

    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('BUSINESS')")
    public ResponseEntity<?> getAllProductCountForBusiness() {
        UserEntity currentUser = GetAuthenticatedUser.getAuthenticatedUser();
        try {
            Integer productCount = productServices.getCountOfAllProductsForBusinessProfile(currentUser);
            return new ResponseEntity<>(productCount, HttpStatus.OK);
        } catch (Exception ex) {
            return globalExceptionHandler.handleGenericExceptions(ex);
        }
    }

    //    @GetMapping("/status/{productStatus}")
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('BUSINESS')")
    public ResponseEntity<?> getAllProductForUserBasedOnStatus(@PathVariable String status) {
        UserEntity currentUser = GetAuthenticatedUser.getAuthenticatedUser();
        try {
            List<?> products = productServices.getListOfProductFromStatus(currentUser, status);
//            TODO: PROVIDE A RESPONSE IF 0 PRODUCTS ARE AVAILABLE
            if (products.isEmpty()) {
                return new ResponseEntity<>("SORRY, NO PRODUCTS FOUND", HttpStatus.OK);
            }
            return new ResponseEntity<>(products, HttpStatus.FOUND);
        } catch (Exception ex) {
            return globalExceptionHandler.handleGenericExceptions(ex);
        }
    }

    //    TODO: IMPLEMENT PAGINATION
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('BUSINESS', 'ADMIN', 'CUSTOMER')")
    public ResponseEntity<?> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        System.out.println("dnsvsjkdnvfnahfdnvdsfhjvfdnviufnvijf");
        List<ProductEntity> products = productServices.searchProducts(keyword, category, minPrice, maxPrice);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/paginated")
    @PreAuthorize("hasAnyRole('BUSINESS')")
    public ResponseEntity<?> getPaginatedProducts(@RequestParam int page, @RequestParam int size) {
        List<ProductEntity> paginatedProducts = productServices.getPaginatedProducts(page, size);
        return new ResponseEntity<>(paginatedProducts, HttpStatus.OK);
    }

//    TODO: THUMBNAIL IMAGE UPLOAD
    @PostMapping("/thumbnail/{productId}")
    @PreAuthorize("hasAnyRole('BUSINESS')")
    public ResponseEntity<?> uploadThumbnail(@PathVariable String productId, @RequestParam("image") MultipartFile file){
        try{
            UploadThumbnailResponseDto responseDto = productServices.uploadThumbnailImage(productId, file);
            if(responseDto.getIsError()){
                return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch (ProductExceptions ex){
            return globalExceptionHandler.handleProductNotFound(ex);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //    TODO: SUPPORT IMAGES UPLOAD
    @PostMapping("/supportImages/{productId}")
    @PreAuthorize("hasAnyRole('BUSINESS')")
    public ResponseEntity<?> uploadMultipleImages(@PathVariable String productId, @RequestParam("images") List<MultipartFile> files){
        try{
            UploadMultipleImagesResponseDto response = productServices.uploadSupportingImages(productId, files);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ProductExceptions ex){
            return globalExceptionHandler.handleProductNotFound(ex);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    TODO: UPDATE HANDLER
    @PutMapping("/update/{productId}")
    @PreAuthorize("hasAnyRole('BUSINESS')")
    public ResponseEntity<?> updateProductById(@PathVariable String productId, @RequestBody CreateProductDto requestBody){
        try{
            ProductEntity updatedProduct = productServices.updateProductById(productId, requestBody);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        }catch (ProductExceptions ex){
            return globalExceptionHandler.handleProductNotFound(ex);
        }
    }

//    TODO: IMPLEMENT GET ALL PRODUCT STATUS
    @GetMapping("/status/all")
    public ResponseEntity<?> getAllProductStatus(){
        List<String> productStatuses = productServices.getAllProductStatus();
        return new ResponseEntity<>(productStatuses, HttpStatus.OK);
    }
}
