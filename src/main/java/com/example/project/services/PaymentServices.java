package com.example.project.services;

import com.example.project.dtos.ProductsInCartDto;
import com.example.project.dtos.responses.ReviewCartResponseDto;
import com.example.project.entities.*;
import com.example.project.enums.PaymentStatusEnum;
import com.example.project.enums.PaymentTypeEnum;
import com.example.project.exceptions.NotFoundException;
import com.example.project.exceptions.PaymentExceptions;
import com.example.project.exceptions.SomethingWentWrongException;
import com.example.project.repositories.CartRepository;
import com.example.project.repositories.ProductRepository;
import com.example.project.repositories.TransactionRepository;
import com.example.project.repositories.UserPurchaseRepository;
import com.example.project.utils.GlobalLogger;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class PaymentServices {

    private final TransactionRepository transactionRepository;
    private final CustomerProfileServices customerProfileServices;
    private final UserPurchaseRepository userPurchaseRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private static final Logger logger = GlobalLogger.getLogger(PaymentServices.class);

    public PaymentServices(
            TransactionRepository transactionRepository,
            CustomerProfileServices customerProfileServices,
            UserPurchaseRepository userPurchaseRepository,
            ProductRepository productRepository,
            CartRepository cartRepository
    ){
        this.transactionRepository = transactionRepository;
        this.customerProfileServices = customerProfileServices;
        this.userPurchaseRepository = userPurchaseRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    public TransactionsEntity initiatePaymentService(UserEntity user, String paymentType){
//        TODO: Check if the Payment is already is initiated or not, if initiated then do not initiate another one.
        TransactionsEntity isAnyTransactionInitiated = isPaymentInitiatedByUser(user.getId());
        logger.info("Payment: Check if payment is initiated or not by this user.");
        if(isAnyTransactionInitiated != null){
            logger.error("Payment: Payment is already initiated by this user.");
            throw new PaymentExceptions("Payment is Already initiated, by this user. Please finish that first.");
        }
//        TODO: What is in Entity: User, PaymentId, transactionDate, PaymentType, PaymentStatus, Products
//        For the testing purpose paymentId is randomly generated
        logger.info("Payment: Trying to Initiate Transaction");
        String paymentId = UUID.randomUUID().toString();
        LocalDateTime transactionDate = LocalDateTime.now();
        logger.info("Payment: Trying to get all products from cart");
        ReviewCartResponseDto reviewCartResponse = customerProfileServices.reviewCart(user.getId());
        List<ProductsInCartDto> productsInCart = reviewCartResponse.getProductsInCart();
        List<String> productIds = new ArrayList<>();
        for (ProductsInCartDto product: productsInCart){
            productIds.add(product.getProduct().getId());
        }
        logger.info("Payment: Trying to add all details into DB");
        TransactionsEntity transaction = new TransactionsEntity();
        transaction.setPaymentId(paymentId);
        transaction.setUserProfile(user);
        transaction.setCardType(PaymentTypeEnum.valueOf(paymentType));
        transaction.setTransactionDate(transactionDate);
        transaction.setAmount((long) reviewCartResponse.getTotalWithTax());
        transaction.setProducts(productIds);
        transaction.setPaymentStatus(PaymentStatusEnum.PENDING);
        logger.info("Payment: All details are saved into DB");
        return transactionRepository.save(transaction);
    }


//    TODO: For payment success what needs to be consider, first find payment with paymentId, mark status success and then remove all the products from cart, and move all of them to userPurchases, decrease quantity from that product as well.
    public TransactionsEntity paymentSuccessService(UserEntity user){
        logger.info("Payment: Trying to find Transaction");
        TransactionsEntity findPendingTransaction = isPaymentInitiatedByUser(user.getId());
        if(findPendingTransaction == null){
            logger.error("Payment: Not found something went wrong");
            throw new NotFoundException("There is no initiated payment, Something went wrong");
        }
        logger.info("Payment: Payment found");
        logger.info("Payment: Update the Payment Status to success");
        findPendingTransaction.setPaymentStatus(PaymentStatusEnum.SUCCESS);
        transactionRepository.save(findPendingTransaction);
        logger.info("Payment: Trying to get cart information to store user purchase");
        ReviewCartResponseDto reviewCartResponse = customerProfileServices.reviewCart(user.getId());
        List<ProductsInCartDto> productsInCart = reviewCartResponse.getProductsInCart();
        for (ProductsInCartDto product: productsInCart){
            logger.info("Payment: Trying to save product that is purchased DB.");
            UserPurchasesEntity userPurchases = new UserPurchasesEntity();
            userPurchases.setUserId(user.getId());
            userPurchases.setProductId(product.getProduct().getId());
            userPurchases.setQuantity(product.getQuantityInCart());
            userPurchaseRepository.save(userPurchases);
            logger.info("Payment: Trying to update stock in DB for product that is purchased");
            ProductEntity purchasedProduct = product.getProduct();
            Integer quantityPurchased = product.getQuantityInCart();
            Integer quantityBeforePurchased = purchasedProduct.getQuantity();
            purchasedProduct.setQuantity(quantityBeforePurchased - quantityPurchased);
            productRepository.save(purchasedProduct);
        }
        logger.info("Payment: Everything is set up , now remove products from Cart.");
        List<CartEntity> cart = cartRepository.findAllByUserId(user.getId()).orElse(null);
        assert cart != null;
        if(cart.isEmpty()){
            logger.info("Payment: Something went Wrong while removing products from cart.");
            throw new SomethingWentWrongException("Something Went Wrong");
        }
        for(CartEntity productInCart : cart){
            logger.info("Payment: Trying to remove product form cart");
            cartRepository.delete(productInCart);
        }
        return findPendingTransaction;
    }


    public void paymentFailureService(){}


//    TODO: HELPER FUNCTIONS
    TransactionsEntity isPaymentInitiatedByUser(String userId){
        return transactionRepository.findTransactionsOfUserWithPandingStatus(userId).orElse(null);
    }

}
