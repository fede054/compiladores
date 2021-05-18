package testtwo.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import testtwo.dto.TruckDTO;
import testtwo.dto.VehicleDTO;
import testtwo.entity.ParkedVehicle;
import testtwo.service.GarageService;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration()
@TestInstance(Lifecycle.PER_CLASS)
public class ParkingControllerTest {

    @Mock
    private GarageService garageService;

    @InjectMocks
    private ParkingController parkingController;

    private MockMvc mockMvc;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(this.parkingController)
                .build();
    }

    @Test
    public void parkCar() throws Exception {
        final VehicleDTO car = new CarDTO("abc123");
        final ParkedVehicle parkedVehicle = new ParkedVehicle(car);
        when(this.garageService.park(1, 1, car)).thenReturn(parkedVehicle);
        final MvcResult result = mockMvc
                .perform(post("/park/car")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("floor", "1")
                        .param("position", "1")
                        .content("{\"patent\": \"abc123\"}"))
                .andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("abc123"));
    }

    @Test
    public void parkTruck() throws Exception {
        final VehicleDTO truck = new TruckDTO("abc123");
        final ParkedVehicle parkedVehicle = new ParkedVehicle(truck);
        when(this.garageService.park(1, 2, truck)).thenReturn(parkedVehicle);
        final MvcResult result = mockMvc
                .perform(post("/park/truck")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("floor", "1")
                        .param("position", "2")
                        .content("{\"patent\": \"abc123\"}"))
                .andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("abc123"));
    }

    @Test
    public void unParkVehicle() {
    }
}