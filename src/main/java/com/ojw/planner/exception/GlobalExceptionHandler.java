package com.ojw.planner.exception;

import com.ojw.planner.core.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

@Slf4j
@RestControllerAdvice //@RestController 예외 AOP 처리
public class GlobalExceptionHandler {

    private void loggingError(Exception e) {

        try(ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PrintStream ps = new PrintStream(out);
            e.printStackTrace(ps);
            log.error(out.toString());
        } catch (IOException ioe) {
            log.error("error occurred while close IO");
        }

    }

    //클라이언트 에러 등 400번대 에러
    @ExceptionHandler(ResponseException.class)
    public ResponseEntity<?> handleResponseException(ResponseException e){

        loggingError(e);

        HttpStatus status = ObjectUtils.isEmpty(e.getStatus()) ? HttpStatus.INTERNAL_SERVER_ERROR : e.getStatus();
        return new ResponseEntity<>(new ApiResponse<>(getResult(status), e.getMessage()), e.getStatus());

    }

    private boolean getResult(HttpStatus status) {
        return status.value() < 400;
    }

    //request 검증 에러
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidException(MethodArgumentNotValidException e){

        BindingResult bindingResult = e.getBindingResult();

        int index = 1;
        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append(fieldError.getField() + " : ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" -> 입력된 값 : " + fieldError.getRejectedValue());
            if(index < bindingResult.getFieldErrors().size()) builder.append(", ");
            index++;
        }

        return new ResponseEntity<>(new ApiResponse<>(false, builder.toString()), HttpStatus.BAD_REQUEST);

    }

    //인증 에러
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<?> handleAuthException(AuthorizationDeniedException e){
        loggingError(e);
        return new ResponseEntity<>(new ApiResponse<>(false, e.getMessage()), HttpStatus.FORBIDDEN);
    }

    //그 외 시스템 에러 등
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e){
        loggingError(e);
        return new ResponseEntity<>(new ApiResponse<>(false, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
