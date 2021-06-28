package testtwo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import testtwo.dto.rs.ErrorRsDTO;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRsDTO> handleException(final Exception e) {
        final String errorMsg = e.getMessage() == null || e.getMessage().equals("") ? "internal server error" : e.getMessage();
        return new ResponseEntity<>(new ErrorRsDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMsg), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
