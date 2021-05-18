package testtwo.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import testtwo.dto.CarDTO;
import testtwo.dto.VehicleDTO;
import testtwo.entity.ParkedVehicle;
import testtwo.service.GarageService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration()
@TestInstance(Lifecycle.PER_CLASS)
public class UnParkingControllerTest {

    @Mock
    private GarageService garageService;

    @InjectMocks
    private UnParkingController unParkingController;

    private MockMvc mockMvc;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(this.unParkingController)
                .build();
    }

    @Test
    public void unParkVehicle() throws Exception {
        final String feeToPay = "123";
        when(this.garageService.unPark(1, 1)).thenReturn(feeToPay);
        final MvcResult result = mockMvc
                .perform(get("/unpark")
                        .accept(MediaType.TEXT_PLAIN_VALUE)
                        .param("floor", "1")
                        .param("position", "1"))
                .andExpect(status().isOk()).andReturn();
        assertEquals(feeToPay, result.getResponse().getContentAsString());
    }
}