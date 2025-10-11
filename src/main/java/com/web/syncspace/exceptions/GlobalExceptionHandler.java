package com.web.syncspace.exceptions;

import com.web.syncspace.constants.Constants;
import com.web.syncspace.dto.response.ErrorResponseDTO;
import com.web.syncspace.dto.response.ResponseDTO;
import com.web.syncspace.exceptions.others.AuthException;
import com.web.syncspace.exceptions.others.BadCredentialException;
import com.web.syncspace.exceptions.others.DuplicateResourceException;
import com.web.syncspace.exceptions.others.ResourceNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NotNull MethodArgumentNotValidException ex, @NotNull HttpHeaders headers, @NotNull HttpStatusCode status, @NotNull WebRequest request) {
        List<String> errorList = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ", " + fieldError.getDefaultMessage())
                .toList();

        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .apiUri(((ServletWebRequest) request).getRequest().getRequestURI())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .errorMessage(String.join(", ", errorList))
                .timestamp(OffsetDateTime.now(ZoneOffset.UTC))
                .build();

        ResponseDTO<ErrorResponseDTO> response = ResponseDTO.<ErrorResponseDTO>builder()
                .statusCode(Constants.BAD_REQUEST_CODE)
                .statusMessage(Constants.BAD_REQUEST_MESSAGE)
                .data(errorResponseDTO)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDTO<ErrorResponseDTO>> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .apiUri(((ServletWebRequest) request).getRequest().getRequestURI())
                .httpStatus(HttpStatus.NOT_FOUND)
                .errorMessage(ex.getMessage())
                .timestamp(OffsetDateTime.now(ZoneOffset.UTC))
                .build();

        ResponseDTO<ErrorResponseDTO> response = ResponseDTO.<ErrorResponseDTO>builder()
                .statusCode(Constants.NOT_FOUND_CODE)
                .statusMessage(Constants.NOT_FOUND_MESSAGE)
                .data(errorResponseDTO)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ResponseDTO<ErrorResponseDTO>> handleDuplicateResourceException(DuplicateResourceException ex, WebRequest request) {
        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .apiUri(((ServletWebRequest) request).getRequest().getRequestURI())
                .httpStatus(HttpStatus.CONFLICT)
                .errorMessage(ex.getMessage())
                .timestamp(OffsetDateTime.now(ZoneOffset.UTC))
                .build();

        ResponseDTO<ErrorResponseDTO> response = ResponseDTO.<ErrorResponseDTO>builder()
                .statusCode(Constants.CONFLICT_CODE)
                .statusMessage(Constants.CONFLICT_MESSAGE)
                .data(errorResponseDTO)
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ResponseDTO<ErrorResponseDTO>> handleAuthException(AuthException ex, WebRequest request) {
        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .apiUri(((ServletWebRequest) request).getRequest().getRequestURI())
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .errorMessage(ex.getMessage())
                .timestamp(OffsetDateTime.now(ZoneOffset.UTC))
                .build();

        ResponseDTO<ErrorResponseDTO> response = ResponseDTO.<ErrorResponseDTO>builder()
                .statusCode(Constants.AUTH_FAILURE_CODE)
                .statusMessage(Constants.AUTH_FAILURE_MESSAGE)
                .data(errorResponseDTO)
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(BadCredentialException.class)
    public ResponseEntity<ResponseDTO<ErrorResponseDTO>> handleBadCredentialException(BadCredentialException ex, WebRequest request) {
        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .apiUri(((ServletWebRequest) request).getRequest().getRequestURI())
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .errorMessage(ex.getMessage())
                .timestamp(OffsetDateTime.now(ZoneOffset.UTC))
                .build();

        ResponseDTO<ErrorResponseDTO> response = ResponseDTO.<ErrorResponseDTO>builder()
                .statusCode(Constants.BAD_REQUEST_CODE)
                .statusMessage(Constants.BAD_REQUEST_MESSAGE)
                .data(errorResponseDTO)
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<ErrorResponseDTO>> handleGlobalException(Exception ex, WebRequest request) {
        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .apiUri(((ServletWebRequest) request).getRequest().getRequestURI())
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .errorMessage(ex.getMessage())
                .timestamp(OffsetDateTime.now(ZoneOffset.UTC))
                .build();


        ResponseDTO<ErrorResponseDTO> response = ResponseDTO.<ErrorResponseDTO>builder()
                .statusCode(Constants.INTERNAL_SERVER_ERROR_CODE)
                .statusMessage(Constants.INTERNAL_SERVER_ERROR_MESSAGE)
                .data(errorResponseDTO)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
