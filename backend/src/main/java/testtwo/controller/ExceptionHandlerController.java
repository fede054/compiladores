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
        final ErrorRsDTO errorToReturn = new ErrorRsDTO(e.getCause().getMessage(), e.getMessage());
        return new ResponseEntity<>(errorToReturn, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
