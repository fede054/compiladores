package testtwo.service;

import org.javatuples.Pair;
import testtwo.dto.Car;
import testtwo.entity.ParkedVehicle;
import testtwo.dto.Vehicle;
import testtwo.repository.ParkedVehicleRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testtwo.service.impl.GarageServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class TestGarageServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(TestGarageServiceImpl.class);

    @Mock
    private ParkedVehicleRepository parkedVehicleRepository;

    @DisplayName("Ok al estacionar vehiculo")
    @Test
    void whenParkVehicleThenOk() {
        final Vehicle car = new Car("AEI123");
        final ParkedVehicle parkedCar = new ParkedVehicle(car);
        when(this.parkedVehicleRepository.isEmpty(1, 1)).thenReturn(true);
        Mockito.lenient().when(this.parkedVehicleRepository.save(1, 1, car)).thenReturn(parkedCar);
        GarageServiceImpl garageServiceImpl = new GarageServiceImpl(parkedVehicleRepository);
        final ParkedVehicle result = garageServiceImpl.park(1, 1, car);
        assertEquals(parkedCar.getPatent(), result.getPatent());
    }

    @DisplayName("Error al estacionar vehiculo")
    @Test
    void whenParkVehicleThenError() {
        final Vehicle car = new Car("AEI123");
        final ParkedVehicle parkedCar = new ParkedVehicle(car);
        when(this.parkedVehicleRepository.getMaxFloorAndPosition()).thenReturn(new Pair<>(4, 4));
        when(this.parkedVehicleRepository.isEmpty(1, 1)).thenReturn(false);
        when(this.parkedVehicleRepository.getFirstEmptyPlace()).thenReturn(new Pair<>(2, 2));
        final RuntimeException e = Assertions.assertThrows(RuntimeException.class, () -> {
            GarageServiceImpl garageServiceImpl = new GarageServiceImpl(parkedVehicleRepository);
            final ParkedVehicle result = garageServiceImpl.park(1, 1, car);
            assertEquals(parkedCar.getPatent(), result.getPatent());
        });
        assertTrue(e.getMessage().contains("ya existe un vehículo en esa posicion."));
    }

    @DisplayName("Ok al quitar del estacionamiento un vehiculo")
    @Test
    void whenUnParkThenOk() {
        when(this.parkedVehicleRepository.isEmpty(1, 1)).thenReturn(false);
        Mockito.lenient().when(this.parkedVehicleRepository.delete(1, 1)).thenReturn("Precio a pagar sarasa");
        GarageServiceImpl garageServiceImpl = new GarageServiceImpl(parkedVehicleRepository);
        final String feeToPay = garageServiceImpl.unPark(1, 1);
        assertEquals("Precio a pagar sarasa", feeToPay);
    }

    @DisplayName("Error al quitar del estacionamiento un vehiculo")
    @Test
    void whenUnParkThenError() {
        when(this.parkedVehicleRepository.isEmpty(1, 1)).thenReturn(true);
        RuntimeException e = Assertions.assertThrows(RuntimeException.class, () -> {
            GarageServiceImpl garageServiceImpl = new GarageServiceImpl(parkedVehicleRepository);
            garageServiceImpl.unPark(1, 1);
        });
        assertEquals(e.getMessage(), "No se puede remover un vehículo del piso o posición indicado");
    }

}
