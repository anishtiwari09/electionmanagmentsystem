package org.day2.electionmanagmentsystem.common.exception;

import org.day2.electionmanagmentsystem.common.dto.ApiResponse;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException( BusinessException exception){
        ApiResponse<Void> response=
                ApiResponse.<Void>builder()
                        .success(false)
                            .code(exception.getErrorCode().name())
                        .message(exception.getErrorCode().getMessage())
                        .data(null)
                        .build();

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(response);
    }
@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatchException(MethodArgumentNotValidException exception){
        ApiResponse<Void> response =
                ApiResponse.<Void>builder()
                        .code(ErrorCode.INVALID_REQUEST.name())
                        .success(false)
                        .message(ErrorCode.INVALID_REQUEST.getMessage())
                        .data(null)
                        .build();

        return ResponseEntity.badRequest().body(response);


    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ApiResponse<Void>> handleHeaderMissingException(MissingRequestHeaderException exception){
        ApiResponse<Void> response =
                ApiResponse.<Void>builder()
                        .code(ErrorCode.INVALID_REQUEST_HEADER.name())
                        .success(false)
                        .message(ErrorCode.INVALID_REQUEST_HEADER.getMessage())
                        .data(null)
                        .build();

        return ResponseEntity.badRequest().body(response);


    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleException(){
        ApiResponse<Void> response =
                ApiResponse.<Void>builder()
                        .code(ErrorCode.INVALID_REQUEST.name())
                        .message(ErrorCode.INVALID_REQUEST.getMessage())
                        .success(false)
                        .data(null)
                        .build();
        return ResponseEntity.badRequest().body(response);
    }

//    @ExceptionHandler(Exception.class) commenting for dev enviroment
    public ResponseEntity<ApiResponse<Void>> handlException(){
        ApiResponse<Void> response =
                ApiResponse.<Void>builder()
                        .code(ErrorCode.INTERNAL_SERVER_ERROR.name())
                        .message(ErrorCode.INVALID_REQUEST.getMessage())
                        .success(false)
                        .data(null)
                        .build();

        return ResponseEntity.internalServerError().body(response);

    }

}
