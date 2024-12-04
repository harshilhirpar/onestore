package com.example.project.exceptions;

import com.example.project.dtos.responses.ExceptionResponseDto;
import com.example.project.enums.ErrorMessagesEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<String> handleRoleNotFoundException(RoleNotFoundException ex){
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

//    @ExceptionHandler(Unknown.class)
    public ResponseEntity<String> handleUserNotFoundException(UserExceptions ex){
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED
        );
    }

//    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericExceptions(Exception ex){
        return new ResponseEntity<>(
                "An Unexpected Error Occurred " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

//    @ExceptionHandler(BusinessProfileExceptions.class)
    public ResponseEntity<String> handleBusinessProfileNotFoundException(BusinessProfileExceptions ex){
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

//    @ExceptionHandler(UserExceptions.class)
    public ResponseEntity<ExceptionResponseDto> handleUserAlreadyException(UserExceptions ex){
        ExceptionResponseDto responseDto = new ExceptionResponseDto();
        responseDto.setIsError(true);
        responseDto.setMessage(ex.getMessage());
        return new ResponseEntity<>(
                responseDto,
                HttpStatus.BAD_REQUEST
        );
    }

//    @ExceptionHandler(ProductExceptions.class)
    public ResponseEntity<String> handleProductAlreadyExistsException(ProductExceptions ex){
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

//    @ExceptionHandler(BusinessProfileExceptions.class)
    public ResponseEntity<ExceptionResponseDto> handleBusinessProfileAlreadyExists(BusinessProfileExceptions ex){
        ExceptionResponseDto responseDto = new ExceptionResponseDto();
        responseDto.setIsError(true);
        responseDto.setMessage(String.valueOf(ErrorMessagesEnum.BUSINESS_PROFILE_ALREADY_EXISTS));
        return new ResponseEntity<>(
                responseDto,
                HttpStatus.BAD_REQUEST
        );
    }

//    @ExceptionHandler(ProductExceptions.class)
    public ResponseEntity<ExceptionResponseDto> handleProductNotFound(ProductExceptions ex) {
        ExceptionResponseDto responseDto = new ExceptionResponseDto();
        responseDto.setIsError(true);
        responseDto.setMessage(String.valueOf(ErrorMessagesEnum.PRODUCT_NOT_FOUND));
        return new ResponseEntity<>(
                responseDto,
                HttpStatus.BAD_REQUEST
        );
    }

    public ResponseEntity<ExceptionResponseDto> handleCustomerProfileNotFound(CustomerProfileNotFoundException ex){
        ExceptionResponseDto responseDto = new ExceptionResponseDto();
        responseDto.setIsError(true);
        responseDto.setMessage(ex.getMessage());
        return new ResponseEntity<>(
                responseDto,
                HttpStatus.NOT_FOUND
        );
    }

    public ResponseEntity<?> handleProductThumbnailAlreadyExist(ProductExceptions ex){
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    public ResponseEntity<?> handleAlreadyExistException(AlreadyExistsExceptions ex){
        ExceptionResponseDto responseDto = new ExceptionResponseDto();
        responseDto.setIsError(true);
        responseDto.setMessage(ex.getMessage());
        return new ResponseEntity<>(
                responseDto,
                HttpStatus.BAD_REQUEST
        );
    }

    public ResponseEntity<?> handleNotFoundException(NotFoundException ex){
        ExceptionResponseDto responseDto = new ExceptionResponseDto();
        responseDto.setIsError(true);
        responseDto.setMessage(ex.getMessage());
        return new ResponseEntity<>(
                responseDto,
                HttpStatus.NOT_FOUND
        );
    }
}
