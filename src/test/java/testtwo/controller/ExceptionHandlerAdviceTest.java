package testtwo.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import testtwo.dto.rs.ErrorRsDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests the UserController class.
 */
@ExtendWith(MockitoExtension.class)
public class ExceptionHandlerAdviceTest {

    @Spy
    private ExceptionHandlerController exceptionHandlerController;

    @Test
    public void handlerGenericException() {
        //given
        final Exception genericException = new RuntimeException();

        //When
        final ResponseEntity<ErrorRsDTO> responseEntity = this.exceptionHandlerController.handleException(genericException);

        //Then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody().getCode());
        assertNotNull(responseEntity.getBody().getDescription());
    }
}
