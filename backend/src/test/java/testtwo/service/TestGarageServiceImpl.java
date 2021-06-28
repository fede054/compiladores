/*
package testtwo.service;

import org.javatuples.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testtwo.dto.rq.CarDTO;
import testtwo.dto.rq.VehicleDTO;
import testtwo.repository.ParkedVehicleRepositoryCustom;
import testtwo.service.impl.GarageServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class TestGarageServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(TestGarageServiceImpl.class);

    @Mock
    private ParkedVehicleRepositoryCustom parkedVehicleRepositoryWrapper;

    @DisplayName("Ok al estacionar vehiculo")
    @Test
    void whenParkVehicleThenOk() {
        final VehicleDTO car = new CarDTO("AEI123");
        final ParkedVehicle parkedCar = new ParkedVehicle(car);
        when(this.parkedVehicleRepositoryWrapper.getMaxFloorAndPosition()).thenReturn(new Pair<>(4, 4));
        when(this.parkedVehicleRepositoryWrapper.isEmpty(1, 1)).thenReturn(true);
        Mockito.lenient().when(this.parkedVehicleRepositoryWrapper.save(1, 1, car)).thenReturn(parkedCar);
        GarageServiceImpl garageServiceImpl = new GarageServiceImpl(parkedVehicleRepositoryWrapper);
        final ParkedVehicle result = garageServiceImpl.park(1, 1, car);
        assertEquals(parkedCar.getPatent(), result.getPatent());
    }

    @DisplayName("Ok al estacionar vehiculo en piso y posición nulo")
    @Test
    void givenNullParamsWhenParkVehicleThenOk() {
        final VehicleDTO car = new CarDTO("AEI123");
        final ParkedVehicle parkedCar = new ParkedVehicle(car);
        when(this.parkedVehicleRepositoryWrapper.getFirstEmptyPlace()).thenReturn(new Pair<>(1, 1));
        when(this.parkedVehicleRepositoryWrapper.getMaxFloorAndPosition()).thenReturn(new Pair<>(4, 4));
        when(this.parkedVehicleRepositoryWrapper.isEmpty(1, 1)).thenReturn(true);
        Mockito.lenient().when(this.parkedVehicleRepositoryWrapper.save(1, 1, car)).thenReturn(parkedCar);
        GarageServiceImpl garageServiceImpl = new GarageServiceImpl(parkedVehicleRepositoryWrapper);
        final ParkedVehicle result = garageServiceImpl.park(null, null, car);
        assertEquals(parkedCar.getPatent(), result.getPatent());
    }

    @DisplayName("Error al estacionar vehiculo")
    @Test
    void whenParkVehicleThenError() {
        final VehicleDTO car = new CarDTO("AEI123");
        final ParkedVehicle parkedCar = new ParkedVehicle(car);
        when(this.parkedVehicleRepositoryWrapper.getMaxFloorAndPosition()).thenReturn(new Pair<>(4, 4));
        when(this.parkedVehicleRepositoryWrapper.isEmpty(1, 1)).thenReturn(false);
        when(this.parkedVehicleRepositoryWrapper.getFirstEmptyPlace()).thenReturn(new Pair<>(2, 2));
        final RuntimeException e = Assertions.assertThrows(RuntimeException.class, () -> {
            GarageServiceImpl garageServiceImpl = new GarageServiceImpl(parkedVehicleRepositoryWrapper);
            final ParkedVehicle result = garageServiceImpl.park(1, 1, car);
            assertEquals(parkedCar.getPatent(), result.getPatent());
        });
        assertTrue(e.getMessage().contains("ya existe un vehículo en esa posicion."));
    }

    @DisplayName("Error al estacionar vehiculo")
    @Test
    void givenNoEmptyPlaceWhenParkVehicleThenError() {
        final VehicleDTO car = new CarDTO("AEI123");
        final ParkedVehicle parkedCar = new ParkedVehicle(car);
        when(this.parkedVehicleRepositoryWrapper.getFirstEmptyPlace()).thenReturn(new Pair<>(null, null));
        final RuntimeException e = Assertions.assertThrows(RuntimeException.class, () -> {
            GarageServiceImpl garageServiceImpl = new GarageServiceImpl(parkedVehicleRepositoryWrapper);
            final ParkedVehicle result = garageServiceImpl.park(null, null, car);
            assertEquals(parkedCar.getPatent(), result.getPatent());
        });
        assertEquals("No existe lugar disponible para estacionar", e.getMessage());
    }

    @DisplayName("Ok al quitar del estacionamiento un vehiculo")
    @Test
    void whenUnParkThenOk() {
        when(this.parkedVehicleRepositoryWrapper.getMaxFloorAndPosition()).thenReturn(new Pair<>(4, 4));
        when(this.parkedVehicleRepositoryWrapper.isEmpty(1, 1)).thenReturn(false);
        when(this.parkedVehicleRepositoryWrapper.delete(1, 1)).thenReturn("Precio a pagar sarasa");
        GarageServiceImpl garageServiceImpl = new GarageServiceImpl(parkedVehicleRepositoryWrapper);
        final String feeToPay = garageServiceImpl.unPark(1, 1);
        assertEquals("Precio a pagar sarasa", feeToPay);
    }

    @DisplayName("Error al quitar del estacionamiento un vehiculo")
    @Test
    void whenUnParkThenError() {
        when(this.parkedVehicleRepositoryWrapper.getMaxFloorAndPosition()).thenReturn(new Pair<>(4, 4));
        when(this.parkedVehicleRepositoryWrapper.isEmpty(1, 1)).thenReturn(true);
        RuntimeException e = Assertions.assertThrows(RuntimeException.class, () -> {
            GarageServiceImpl garageServiceImpl = new GarageServiceImpl(parkedVehicleRepositoryWrapper);
            garageServiceImpl.unPark(1, 1);
        });
        assertEquals(e.getMessage(), "No se puede remover un vehículo del piso o posición indicado");
    }

    @DisplayName("Error al quitar del estacionamiento un vehiculo con parametros nulos")
    @Test
    void givenNullParamsWhenUnParkThenError() {
        RuntimeException e = Assertions.assertThrows(RuntimeException.class, () -> {
            GarageServiceImpl garageServiceImpl = new GarageServiceImpl(parkedVehicleRepositoryWrapper);
            garageServiceImpl.unPark(null, null);
        });
        assertEquals(e.getMessage(), "Piso y posición no pueden ser nulos");
    }

    @DisplayName("Error al quitar del estacionamiento un vehiculo con parametros negativos")
    @Test
    void givenNegativeParamsWhenUnParkThenError() {
        RuntimeException e = Assertions.assertThrows(RuntimeException.class, () -> {
            GarageServiceImpl garageServiceImpl = new GarageServiceImpl(parkedVehicleRepositoryWrapper);
            garageServiceImpl.unPark(-1, -3);
        });
        assertEquals(e.getMessage(), "Piso y posición no pueden ser menor a 0");
    }

    @DisplayName("Error al quitar del estacionamiento un vehiculo con parametros fuera de rango")
    @Test
    void givenOutOfRangeParamsWhenUnParkThenError() {
        when(this.parkedVehicleRepositoryWrapper.getMaxFloorAndPosition()).thenReturn(new Pair<>(4, 4));
        RuntimeException e = Assertions.assertThrows(RuntimeException.class, () -> {
            GarageServiceImpl garageServiceImpl = new GarageServiceImpl(parkedVehicleRepositoryWrapper);
            garageServiceImpl.unPark(200, 300);
        });
        assertEquals("Piso o posición inválida", e.getMessage());
    }

}
*/
