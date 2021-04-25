import entity.Car;
import entity.ParkedVehicle;
import entity.Vehicle;
import garage.GarageDB;
import garage.GarageService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class TestGarageService {

    private static final Logger logger = LoggerFactory.getLogger(TestGarageService.class);

    @Mock
    private GarageDB garage;

    @DisplayName("Garage test Mock ok")
    @Test
    void garage() {
        final Vehicle car = new Car("AEI123");
        final ParkedVehicle parkedCar = new ParkedVehicle(car);
        Mockito.lenient().when(garage.save(1, 1, car)).thenReturn(parkedCar);
        GarageService garageService = new GarageService(garage);
        final ParkedVehicle result = garageService.park(1, 1, car);
        assertEquals(parkedCar.getPatent(), result.getPatent());
    }

    @DisplayName("Garage test Exception")
    @Test
    void garageException() {
        final Vehicle car = new Car("AEI123");
        final ParkedVehicle parkedCar = new ParkedVehicle(car);
        Mockito.lenient().when(garage.save(1, 1, car)).thenThrow(new RuntimeException("Ouch!"));
        RuntimeException e = Assertions.assertThrows(RuntimeException.class, () -> {
            GarageService garageService = new GarageService(garage);
            final ParkedVehicle result = garageService.park(1, 1, car);
            assertEquals(parkedCar.getPatent(), result.getPatent());
        });
        assertEquals(e.getMessage(), "Ouch!");
    }

}
