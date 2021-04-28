package service;

import dto.Car;
import entity.ParkedVehicle;
import dto.Vehicle;
import repository.ParkedVehicleRepository;
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
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class TestGarageService {

    private static final Logger logger = LoggerFactory.getLogger(TestGarageService.class);

    @Mock
    private ParkedVehicleRepository parkedVehicleRepository;

    @DisplayName("Ok al estacionar vehiculo")
    @Test
    void whenParkVehicleThenOk() {
        final Vehicle car = new Car("AEI123");
        final ParkedVehicle parkedCar = new ParkedVehicle(car);
        when(this.parkedVehicleRepository.isEmpty(1, 1)).thenReturn(true);
        Mockito.lenient().when(this.parkedVehicleRepository.save(1, 1, car)).thenReturn(parkedCar);
        GarageService garageService = new GarageService(parkedVehicleRepository);
        final ParkedVehicle result = garageService.park(1, 1, car);
        assertEquals(parkedCar.getPatent(), result.getPatent());
    }

    @DisplayName("Error al estacionar vehiculo")
    @Test
    void whenParkVehicleThenError() {
        final Vehicle car = new Car("AEI123");
        final ParkedVehicle parkedCar = new ParkedVehicle(car);
        when(this.parkedVehicleRepository.isEmpty(1, 1)).thenReturn(false);
        RuntimeException e = Assertions.assertThrows(RuntimeException.class, () -> {
            GarageService garageService = new GarageService(parkedVehicleRepository);
            final ParkedVehicle result = garageService.park(1, 1, car);
            assertEquals(parkedCar.getPatent(), result.getPatent());
        });
        assertEquals(e.getMessage(), "No se puede estacionar en el piso o posición solicitada");
    }

    @DisplayName("Ok al quitar del estacionamiento un vehiculo")
    @Test
    void whenUnParkThenOk() {
        when(this.parkedVehicleRepository.isEmpty(1, 1)).thenReturn(false);
        Mockito.lenient().when(this.parkedVehicleRepository.delete(1, 1)).thenReturn("Precio a pagar sarasa");
        GarageService garageService = new GarageService(parkedVehicleRepository);
        final String feeToPay = garageService.unPark(1, 1);
        assertEquals("Precio a pagar sarasa", feeToPay);
    }

    @DisplayName("Error al quitar del estacionamiento un vehiculo")
    @Test
    void whenUnParkThenError() {
        when(this.parkedVehicleRepository.isEmpty(1, 1)).thenReturn(true);
        RuntimeException e = Assertions.assertThrows(RuntimeException.class, () -> {
            GarageService garageService = new GarageService(parkedVehicleRepository);
            garageService.unPark(1, 1);
        });
        assertEquals(e.getMessage(), "No se puede remover un vehículo del piso o posición indicado");
    }

}
