package testtwo.repository;

import org.javatuples.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import testtwo.entity.ParkedVehicle;
import testtwo.repository.impl.CustomParkedVehicleRepositoryImpl;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebAppConfiguration()
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestParkedVehicleCustomRepositoryImpl {

    private CustomParkedVehicleRepositoryImpl parkedVehicleRepository;
    @Mock
    private ParkedVehicleRepository parkedVehicleRepositoryMocked;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.parkedVehicleRepository = new CustomParkedVehicleRepositoryImpl(this.parkedVehicleRepositoryMocked);
    }

    @DisplayName("Guardar vehiculo ok")
    @Test
    void whenSaveCarThenOk() {
        final ParkedVehicle vehicle = ParkedVehicle.builder().patent("a").build();
        whenSaveThenReturnModifiedObject();
        final ParkedVehicle parkedVehicle = this.parkedVehicleRepository.save(1, 1, vehicle);
        assertEquals(vehicle.getPatent(), parkedVehicle.getPatent());
    }

    @DisplayName("Guardar vehiculo en posición nula ok")
    @Test
    void givenNullPositionWhenSaveVehicleThenOk() {
        final ParkedVehicle vehicle = ParkedVehicle.builder().patent("a").build();
        whenSaveThenReturnModifiedObject();
        final ParkedVehicle savedVehicle = this.parkedVehicleRepository.save(null, null, vehicle);
        assertNotNull(savedVehicle);
    }

    @DisplayName("Obtener el piso y posición máximo ok")
    @Test
    void whenMaxFloorAndPositionThenOk() {
        final Pair<Integer, Integer> emptyPlace = this.parkedVehicleRepository.getMaxFloorAndPosition();
        assertNotNull(emptyPlace.getValue0());
        assertNotNull(emptyPlace.getValue1());
    }


    @DisplayName("Obtener el primer lugar vacío ok")
    @Test
    void whenGetFirstEmptyPlaceThenOk() {
        when(this.parkedVehicleRepositoryMocked.existsByFloorAndPositionAndExitDateTimeIsNull(any(), any())).thenReturn(true);
        final Pair<Integer, Integer> emptyPlace = this.parkedVehicleRepository.getFirstEmptyPlace();
        assertNotNull(emptyPlace.getValue0());
        assertNotNull(emptyPlace.getValue1());
    }

    @DisplayName("Obtener el primer lugar vacío error")
    @Test
    void whenGetFirstEmptyPlaceThenError() {
        when(this.parkedVehicleRepositoryMocked.findByFloorAndPosition(any(), any())).thenReturn(Optional.of(new ParkedVehicle()));
        final Pair<Integer, Integer> emptyPlace = this.parkedVehicleRepository.getFirstEmptyPlace();
        assertNull(emptyPlace.getValue0());
        assertNull(emptyPlace.getValue1());
    }


    @DisplayName("Ok al guardar en posición ocupada")
    @Test
    void givenBusyPositionWhenSaveCarThenOk() {
        final ParkedVehicle vehicle = ParkedVehicle.builder().patent("a").build();
        whenSaveThenReturnModifiedObject();
        final ParkedVehicle newParkedVehicle = this.parkedVehicleRepository.save(1, 1, vehicle);
        assertEquals(vehicle.getPatent(), newParkedVehicle.getPatent());
    }

    @DisplayName("Ok al guardar en piso erroneo")
    @Test
    void givenWrongFloorWhenSaveThenOk() {
        final ParkedVehicle vehicle = ParkedVehicle.builder().patent("a").build();
        whenSaveThenReturnModifiedObject();
        final ParkedVehicle newParkedVehicle = this.parkedVehicleRepository.save(1111, 1, vehicle);
        assertEquals(vehicle.getPatent(), newParkedVehicle.getPatent());
    }

    @DisplayName("Ok al guardar en posición erronea")
    @Test
    void givenWrongPositionWhenSaveThenError() {
        final ParkedVehicle vehicle = ParkedVehicle.builder().patent("a").build();
        whenSaveThenReturnModifiedObject();
        final ParkedVehicle parkedVehicle = this.parkedVehicleRepository.save(1, 125, vehicle);
        assertEquals(vehicle.getPatent(), parkedVehicle.getPatent());
    }


    @DisplayName("Ok al guardar en piso nulo")
    @Test
    void givenNullFloorWhenSaveThenError() {
        final ParkedVehicle vehicle = ParkedVehicle.builder().patent("a").build();
        whenSaveThenReturnModifiedObject();
        final ParkedVehicle savedVehicle = this.parkedVehicleRepository.save(null, 1, vehicle);
        assertNotNull(savedVehicle);
    }


    @DisplayName("Ok al guardar en posición nula")
    @Test
    void givenNullPositionWhenSaveThenError() {
        final ParkedVehicle vehicle = ParkedVehicle.builder().patent("a").build();
        whenSaveThenReturnModifiedObject();
        final ParkedVehicle savedVehicle = this.parkedVehicleRepository.save(1, null, vehicle);
        assertNotNull(savedVehicle);
    }

    @DisplayName("Ok al borrar vehículo")
    @Test
    void whenDeleteCarThenOk() {
        final ParkedVehicle vehicle = ParkedVehicle.builder().entryDateTime(LocalDateTime.now()).patent("a").ratio(2L).build();
        when(this.parkedVehicleRepositoryMocked.findByFloorAndPositionAndExitDateTimeIsNull(any(), any())).thenReturn(Optional.of(vehicle));
        final String amountToPay = this.parkedVehicleRepository.delete(1, 1);
        assertTrue(amountToPay.contains("El importe a pagar es: $"));
    }

    @DisplayName("Piso erroneo al borrar vehículo")
    @Test
    void givenWrongFloorWhenDeleteVehicleThenError() {
        when(this.parkedVehicleRepositoryMocked.findByFloorAndPositionAndExitDateTimeIsNull(any(), any())).thenReturn(Optional.empty());
        final RuntimeException e = Assertions.assertThrows(RuntimeException.class, () -> this.parkedVehicleRepository.delete(2, 1));
        assertNotNull(e.getMessage());
    }

    @DisplayName("Posición erronea al borrar vehículo")
    @Test
    void givenWrongPositionWhenDeleteVehicleThenError() {
        when(this.parkedVehicleRepositoryMocked.findByFloorAndPositionAndExitDateTimeIsNull(any(), any())).thenReturn(Optional.empty());
        final RuntimeException e = Assertions.assertThrows(RuntimeException.class, () -> this.parkedVehicleRepository.delete(1, 2));
        assertNotNull(e.getMessage());
    }

    @DisplayName("Posición vacía ok")
    @Test
    void whenIsEmptyIsInvokedThenTrue() {
        when(this.parkedVehicleRepositoryMocked.existsByFloorAndPositionAndExitDateTimeIsNull(any(), any())).thenReturn(false);
        assertTrue(this.parkedVehicleRepository.isEmpty(1, 1));
    }

    @DisplayName("Piso vacío error")
    @Test
    void givenWrongFloorWhenIsEmptyIsInvokedThenError() {
        assertFalse(this.parkedVehicleRepository.isEmpty(15, 1));
    }

    @DisplayName("Posición vacía error")
    @Test
    void givenWrongPositionWhenIsEmptyIsInvokedThenError() {
        assertFalse(this.parkedVehicleRepository.isEmpty(1, 25));
    }

    @DisplayName("Piso negativo error")
    @Test
    void givenNegativeFloorWhenIsEmptyIsInvokedThenError() {
        assertFalse(this.parkedVehicleRepository.isEmpty(-15, 1));
    }

    @DisplayName("Posición negativa error")
    @Test
    void givenNegativePositionWhenIsEmptyIsInvokedThenError() {
        assertFalse(this.parkedVehicleRepository.isEmpty(1, -25));
    }

    @DisplayName("Piso nulo error")
    @Test
    void givenNullFloorWhenIsEmptyIsInvokedThenError() {
        assertFalse(this.parkedVehicleRepository.isEmpty(null, 1));
    }

    @DisplayName("Posición nula error")
    @Test
    void givenNullPositionWhenIsEmptyIsInvokedThenError() {
        assertFalse(this.parkedVehicleRepository.isEmpty(1, null));
    }

    private void whenSaveThenReturnModifiedObject() {
        when(this.parkedVehicleRepositoryMocked.save(any(ParkedVehicle.class))).thenAnswer((Answer<ParkedVehicle>) invocation -> {
            final ParkedVehicle vehicle = (ParkedVehicle) invocation.getArguments()[0];
            vehicle.setId(1L);
            return vehicle;
        });
    }

}
