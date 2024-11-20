package com.example.project.services;

import com.example.project.dtos.CreateProductDto;
import com.example.project.dtos.responses.UploadMultipleImagesResponseDto;
import com.example.project.dtos.responses.UploadThumbnailResponseDto;
import com.example.project.entities.BusinessProfileEntity;
import com.example.project.entities.ProductEntity;
import com.example.project.entities.UserEntity;
import com.example.project.enums.LoggerErrorMessages;
import com.example.project.enums.LoggerInfoMessages;
import com.example.project.enums.ProductStatusEnum;
import com.example.project.exceptions.BusinessProfileExceptions;
import com.example.project.exceptions.ProductExceptions;
import com.example.project.repositories.BusinessProfileRepository;
import com.example.project.repositories.ProductJpaRepository;
import com.example.project.repositories.ProductPagingRepository;
import com.example.project.repositories.ProductRepository;
import com.example.project.utils.GlobalLogger;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServices {

    private final ProductRepository productRepository;
    private final BusinessProfileRepository businessProfileRepository;
    private final ProductJpaRepository productJpaRepository;
    private final ProductPagingRepository productPagingRepository;
    private static final Logger logger = GlobalLogger.getLogger(ProductServices.class);

    public ProductServices(
            ProductRepository productRepository,
            BusinessProfileRepository businessProfileRepository,
            ProductJpaRepository productJpaRepository,
            ProductPagingRepository productPagingRepository
    ) {
        this.productRepository = productRepository;
        this.businessProfileRepository = businessProfileRepository;
        this.productJpaRepository = productJpaRepository;
        this.productPagingRepository = productPagingRepository;
    }
    private final String UPLOAD_DIRECTORY = "uploads/";

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

    public List<ProductEntity> findAllProductForUser(UserEntity user, int page, int size){
        BusinessProfileEntity businessProfile = findBusinessProfile(user);
        logger.info("Attempting to find all products");
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductEntity> allProducts = productJpaRepository.findAllByBusinessProfile(businessProfile, pageable);
        logger.info("Successfully found all products");
        return allProducts.getContent();
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

    public List<ProductEntity> searchProducts(String keyword, String category, Double minPrice, Double maxPrice){
        logger.info("Searching Products");
        return productJpaRepository.searchProducts(keyword, category, minPrice, maxPrice);
    }

    public List<ProductEntity> getPaginatedProducts(int page, int size){
        logger.info("Fetching paginated products");
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductEntity> paginatedProducts = productPagingRepository.findAll(pageable);
        return paginatedProducts.getContent();
    }

    public UploadThumbnailResponseDto uploadThumbnailImage(String productId, MultipartFile file) throws IOException {
//        TODO: CHECK IF PRODUCT EXISTS
        UploadThumbnailResponseDto thumbnailResponseDto = new UploadThumbnailResponseDto();
        logger.info("Trying to upload product thumbnail image");
        ProductEntity product = productRepository.findById(productId).orElse(null);
        if(product == null){
            logger.error("Product not found, please provide valid productId");
            throw new ProductExceptions("PRODUCT NOT FOUND");
        }
//        TODO: CHECK IF PRODUCT IS EXISTS, THEN CHECK THUMBNAIL IS EXISTS
        if(!(product.getThumbnail().isEmpty())){
            logger.error("Thumbnail is already exist");
            thumbnailResponseDto.setIsError(true);
            thumbnailResponseDto.setMessage("THUMBNAIL ALREADY EXISTS");
            return thumbnailResponseDto;
        }
//        GENERATING FILENAME
        String filename = "thumbnail_" + productId + "_" + file.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIRECTORY + filename);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        String imageUrl = "/uploads/" + filename;
        product.setThumbnail(imageUrl);
        productRepository.save(product);

//        TODO: FORMATING RESPONSE
        thumbnailResponseDto.setIsError(false);
        thumbnailResponseDto.setImageUrl(imageUrl);
        thumbnailResponseDto.setMessage("IMAGE UPLOADED");

        return thumbnailResponseDto;
    }

    public UploadMultipleImagesResponseDto uploadSupportingImages(String productId, List<MultipartFile> images) throws IOException {
        logger.info(String.valueOf(LoggerInfoMessages.TRY_TO_UPLOAD_THUMBNAIL));
        UploadMultipleImagesResponseDto multipleImagesResponseDto = new UploadMultipleImagesResponseDto();
        ProductEntity product = productRepository.findById(productId).orElse(null);
        if(product == null){
            logger.error(String.valueOf(LoggerErrorMessages.PRODUCT_NOT_FOUND));
            throw new ProductExceptions("PRODUCT NOT FOUND");
        }
        List<String> imageUrls = new ArrayList<>();
        // Save each file and generate URLs
        for (MultipartFile file : images) {
            String filename = "image_" + productId + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIRECTORY + filename);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());

            String imageUrl = "/uploads/" + filename;
            imageUrls.add(imageUrl);
        }

        product.setSupportingImages(imageUrls);
        productRepository.save(product);
        multipleImagesResponseDto.setIsError(false);
        multipleImagesResponseDto.setMessage("IMAGES UPLOADED");
        multipleImagesResponseDto.setImageUrls(imageUrls);
        return multipleImagesResponseDto;
    }

    public ProductEntity updateProductById(String productId, CreateProductDto requestBody){
        logger.info(String.valueOf(LoggerInfoMessages.TRY_TO_FIND_PRODUCT));
        ProductEntity product = productRepository.findById(productId).orElse(null);
        if(product == null){
            logger.error(String.valueOf(LoggerErrorMessages.PRODUCT_NOT_FOUND));
            throw new ProductExceptions("PRODUCT NOT FOUND");
        }
        product.setName(requestBody.getName());
        product.setDescription(requestBody.getDescription());
        product.setPrice(requestBody.getPrice());
        product.setQuantity(requestBody.getQuantity());
        product.setCategory(requestBody.getCategory());
        product.setStatus(ProductStatusEnum.valueOf(requestBody.getStatus()));

        logger.info(String.valueOf(LoggerInfoMessages.SAVE_PRODUCT));
        productRepository.save(product);
        return product;
    }

    public List<String> getAllProductStatus(){
        logger.info(String.valueOf(LoggerInfoMessages.TRY_TO_GET_PRODUCT_STATUS));
        return Arrays.stream(ProductStatusEnum.values())
                .map(Enum::name)
                .toList();
    }
}
