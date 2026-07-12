package org.day2.electionmanagmentsystem.common.exception;

import org.day2.electionmanagmentsystem.common.dto.response.ApiResponse;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ErrorCode;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException( BusinessException exception){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error(exception.getErrorCode().name(),exception.getErrorCode().getMessage()));
    }
@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatchException(MethodArgumentNotValidException exception){
    String message = exception.getBindingResult()
            .getFieldErrors()
            .stream()
            .findFirst()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .orElse(ErrorCode.INVALID_REQUEST.getMessage());

    return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.INVALID_REQUEST.name(),message));


    }

//    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ApiResponse<Void>> handleHeaderMissingException(MissingRequestHeaderException exception){


        return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.INVALID_REQUEST_HEADER.name(),ErrorCode.INVALID_REQUEST_HEADER.getMessage()));


    }

//    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<ApiResponse<Void>> handleException(Exception exception){
        String errorMessage=exception.getMessage().isBlank()?exception.getMessage():ErrorCode.INVALID_REQUEST.getMessage();
        return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.INVALID_REQUEST.name(),errorMessage));
    }

@ExceptionHandler(CsvValidationException.class)
public ResponseEntity<ApiResponse<List<CsvValidationError>>> handleException(CsvValidationException exception){
    return ResponseEntity.badRequest().body(
            ApiResponse.<List<CsvValidationError>>builder()
                    .success(false)
                    .code(exception.getErrorCode().getCode())
                    .message(exception.getErrorCode().getMessage())
                    .data(exception.getErrors())
                    .build()
    );
}

//    @ExceptionHandler(Exception.class) //commenting for dev enviroment
    public ResponseEntity<ApiResponse<Void>> handleException(){

        return ResponseEntity.internalServerError().body(ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR.name(),ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));

    }

}
