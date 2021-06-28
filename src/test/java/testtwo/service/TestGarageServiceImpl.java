package testtwo.service;

import org.javatuples.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import testtwo.converter.rq.VehicleRqConverter;
import testtwo.converter.rs.ParkedVehicleRsConverter;
import testtwo.dto.rq.CarDTO;
import testtwo.dto.rq.TruckDTO;
import testtwo.dto.rq.VehicleDTO;
import testtwo.dto.rs.ParkedVehicleRsDTO;
import testtwo.entity.ParkedVehicle;
import testtwo.repository.ParkedVehicleRepository;
import testtwo.service.impl.GarageServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class TestGarageServiceImpl {

    @Mock
    private ParkedVehicleRepository parkedVehicleRepository;
    @Spy
    private VehicleRqConverter vehicleRqConverter;
    @Spy
    private ParkedVehicleRsConverter parkedVehicleRsConverter;

    @InjectMocks
    private GarageServiceImpl garageService;

    @DisplayName("Ok al estacionar vehiculo")
    @Test
    void whenParkVehicleThenOk() {
        final var floor = 1;
        final var position = 1;
        final VehicleDTO car = new CarDTO("AEI123");
        final ParkedVehicle parkedCar = ParkedVehicle.builder().patent(car.getPatent()).build();
        when(this.parkedVehicleRepository.getMaxFloorAndPosition()).thenReturn(new Pair<>(4, 4));
        when(this.parkedVehicleRepository.isEmpty(floor, position)).thenReturn(true);
        whenSaveThenReturnModifiedObject();
        final ParkedVehicleRsDTO result = this.garageService.park(floor, position, car);
        assertEquals(parkedCar.getPatent(), result.getPatent());
        assertEquals(floor, result.getFloor());
        assertEquals(position, result.getPosition());
    }

    @DisplayName("Ok al estacionar vehiculo en piso y posición nulo")
    @Test
    void givenNullParamsWhenParkVehicleThenOk() {
        final Integer floor = 1;
        final Integer position = 1;
        final VehicleDTO truck = new TruckDTO("AEI123");
        final ParkedVehicle parkedCar = ParkedVehicle.builder().patent(truck.getPatent()).build();
        when(this.parkedVehicleRepository.getFirstEmptyPlace()).thenReturn(new Pair<>(1, 1));
        when(this.parkedVehicleRepository.getMaxFloorAndPosition()).thenReturn(new Pair<>(4, 4));
        when(this.parkedVehicleRepository.isEmpty(floor, position)).thenReturn(true);
        whenSaveThenReturnModifiedObject();
        final ParkedVehicleRsDTO result = this.garageService.park(null, null, truck);
        assertEquals(parkedCar.getPatent(), result.getPatent());
        assertEquals(floor, result.getFloor());
        assertEquals(position, result.getPosition());
        assertNotNull(result.getRatio());
    }

    @DisplayName("Error al estacionar vehiculo")
    @Test
    void whenParkVehicleThenError() {
        final VehicleDTO car = new CarDTO("AEI123");
        final ParkedVehicle parkedCar = ParkedVehicle.builder().patent(car.getPatent()).build();
        when(this.parkedVehicleRepository.getMaxFloorAndPosition()).thenReturn(new Pair<>(4, 4));
        when(this.parkedVehicleRepository.isEmpty(1, 1)).thenReturn(false);
        when(this.parkedVehicleRepository.getFirstEmptyPlace()).thenReturn(new Pair<>(2, 2));
        final RuntimeException e = Assertions.assertThrows(RuntimeException.class, () -> {
            final ParkedVehicleRsDTO result = this.garageService.park(1, 1, car);
            assertEquals(parkedCar.getPatent(), result.getPatent());
        });
        assertTrue(e.getMessage().contains("ya existe un vehículo en esa posicion."));
    }

    @DisplayName("Error al estacionar vehiculo")
    @Test
    void givenNoEmptyPlaceWhenParkVehicleThenError() {
        final VehicleDTO truck = new TruckDTO("AEI123");
        final ParkedVehicle parkedCar = ParkedVehicle.builder().patent(truck.getPatent()).build();
        when(this.parkedVehicleRepository.getFirstEmptyPlace()).thenReturn(new Pair<>(null, null));
        final RuntimeException e = Assertions.assertThrows(RuntimeException.class, () -> {
            final ParkedVehicleRsDTO result = this.garageService.park(null, null, truck);
            assertEquals(parkedCar.getPatent(), result.getPatent());
        });
        assertEquals("No existe lugar disponible para estacionar", e.getMessage());
    }

    @DisplayName("Ok al quitar del estacionamiento un vehiculo")
    @Test
    void whenUnParkThenOk() {
        when(this.parkedVehicleRepository.getMaxFloorAndPosition()).thenReturn(new Pair<>(4, 4));
        when(this.parkedVehicleRepository.isEmpty(1, 1)).thenReturn(false);
        when(this.parkedVehicleRepository.delete(1, 1)).thenReturn("Precio a pagar sarasa");
        final String feeToPay = this.garageService.unPark(1, 1);
        assertEquals("Precio a pagar sarasa", feeToPay);

    }

    @DisplayName("Error al quitar del estacionamiento un vehiculo")
    @Test
    void whenUnParkThenError() {
        when(this.parkedVehicleRepository.getMaxFloorAndPosition()).thenReturn(new Pair<>(4, 4));
        when(this.parkedVehicleRepository.isEmpty(1, 1)).thenReturn(true);
        RuntimeException e = Assertions.assertThrows(RuntimeException.class, () -> this.garageService.unPark(1, 1));
        assertEquals(e.getMessage(), "No se puede remover un vehículo del piso o posición indicado");
    }

    @DisplayName("Error al quitar del estacionamiento un vehiculo con parametros nulos")
    @Test
    void givenNullParamsWhenUnParkThenError() {
        RuntimeException e = Assertions.assertThrows(RuntimeException.class, () -> this.garageService.unPark(null, null));
        assertEquals(e.getMessage(), "Piso y posición no pueden ser nulos");
    }

    @DisplayName("Error al quitar del estacionamiento un vehiculo con parametros negativos")
    @Test
    void givenNegativeParamsWhenUnParkThenError() {
        RuntimeException e = Assertions.assertThrows(RuntimeException.class, () -> this.garageService.unPark(-1, -3));
        assertEquals(e.getMessage(), "Piso y posición no pueden ser menor a 0");
    }

    @DisplayName("Error al quitar del estacionamiento un vehiculo con parametros fuera de rango")
    @Test
    void givenOutOfRangeParamsWhenUnParkThenError() {
        when(this.parkedVehicleRepository.getMaxFloorAndPosition()).thenReturn(new Pair<>(4, 4));
        RuntimeException e = Assertions.assertThrows(RuntimeException.class, () -> this.garageService.unPark(200, 300));
        assertEquals("Piso o posición inválida", e.getMessage());
    }

    private void whenSaveThenReturnModifiedObject() {
        when(this.parkedVehicleRepository.save(any(), any(), any(ParkedVehicle.class))).thenAnswer((Answer<ParkedVehicle>) invocation -> {
            final ParkedVehicle vehicle = (ParkedVehicle) invocation.getArguments()[2];
            vehicle.setId(1L);
            vehicle.setFloor((Integer) invocation.getArguments()[0]);
            vehicle.setPosition((Integer) invocation.getArguments()[1]);
            return vehicle;
        });
    }

}

