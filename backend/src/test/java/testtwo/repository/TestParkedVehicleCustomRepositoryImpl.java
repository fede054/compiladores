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
import org.mockito.Spy;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import testtwo.converter.rq.VehicleRqConverter;
import testtwo.converter.rs.ParkedVehicleRsConverter;
import testtwo.dto.rq.CarDTO;
import testtwo.dto.rq.TruckDTO;
import testtwo.dto.rq.VehicleDTO;
import testtwo.dto.rs.ParkedVehicleRsDTO;
import testtwo.entity.ParkedVehicle;
import testtwo.repository.impl.ParkedVehicleRepositoryCustomImpl;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebAppConfiguration()
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestParkedVehicleCustomRepositoryImpl {

    private ParkedVehicleRepositoryCustomImpl parkedVehicleRepository;
    @Mock
    private ParkedVehicleRepository parkedVehicleRepositoryMocked;
    @Spy
    private VehicleRqConverter vehicleRqConverter;
    @Spy
    private ParkedVehicleRsConverter parkedVehicleRsConverter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.parkedVehicleRepository = new ParkedVehicleRepositoryCustomImpl(this.parkedVehicleRepositoryMocked, this.vehicleRqConverter, this.parkedVehicleRsConverter);
    }

    @DisplayName("Guardar auto ok")
    @Test
    void whenSaveCarThenOk() {
        final VehicleDTO vehicleDTO = new CarDTO("a");
        whenSaveThenReturnModifiedObject();
        final ParkedVehicleRsDTO parkedVehicle = this.parkedVehicleRepository.save(1, 1, vehicleDTO);
        assertEquals(vehicleDTO.getPatent(), parkedVehicle.getPatent());
    }

    @DisplayName("Guardar camioneta ok")
    @Test
    void whenSaveTruckThenOk() {
        final VehicleDTO vehicleDTO = new TruckDTO("a");
        whenSaveThenReturnModifiedObject();
        final ParkedVehicleRsDTO parkedVehicle = this.parkedVehicleRepository.save(1, 1, vehicleDTO);
        assertEquals(vehicleDTO.getPatent(), parkedVehicle.getPatent());
    }


    @DisplayName("Guardar vehiculo en posición nula ok")
    @Test
    void givenNullPositionWhenSaveVehicleThenOk() {
        final VehicleDTO vehicleDTO = new CarDTO("a");
        final NullPointerException e = Assertions.assertThrows(NullPointerException.class, () -> {
            this.parkedVehicleRepository.save(null, null, vehicleDTO);
        });
        assertNull(e.getMessage());
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
        when(this.parkedVehicleRepositoryMocked.existByFloorAndPositionAndExitDateTimeIsNull(any(), any())).thenReturn(true);
        final Pair<Integer, Integer> emptyPlace = this.parkedVehicleRepository.getFirstEmptyPlace();
        assertNotNull(emptyPlace.getValue0());
        assertNotNull(emptyPlace.getValue1());
    }

    @DisplayName("Obtener el primer lugar vacío error")
    @Test
    void whenGetFirstEmptyPlaceThenError() {
        when(this.parkedVehicleRepositoryMocked.existByFloorAndPositionAndExitDateTimeIsNull(any(), any())).thenReturn(false);
        final Pair<Integer, Integer> emptyPlace = this.parkedVehicleRepository.getFirstEmptyPlace();
        assertNull(emptyPlace.getValue0());
        assertNull(emptyPlace.getValue1());
    }


    @DisplayName("Ok al guardar en posición ocupada")
    @Test
    void givenBusyPositionWhenSaveCarThenOk() {
        final VehicleDTO truck = new TruckDTO("b");
        whenSaveThenReturnModifiedObject();
        final ParkedVehicleRsDTO newParkedVehicle = this.parkedVehicleRepository.save(1, 1, truck);
        assertEquals(truck.getPatent(), newParkedVehicle.getPatent());
    }

    @DisplayName("Ok al guardar en piso erroneo")
    @Test
    void givenWrongFloorWhenSaveThenOk() {
        final VehicleDTO truck = new TruckDTO("b");
        whenSaveThenReturnModifiedObject();
        final ParkedVehicleRsDTO newParkedVehicle = this.parkedVehicleRepository.save(1111, 1, truck);
        assertEquals(truck.getPatent(), newParkedVehicle.getPatent());
    }

    @DisplayName("Ok al guardar en posición erronea")
    @Test
    void givenWrongPositionWhenSaveThenError() {
        final VehicleDTO truck = new TruckDTO("b");
        whenSaveThenReturnModifiedObject();
        final ParkedVehicleRsDTO parkedVehicle = this.parkedVehicleRepository.save(1, 125, truck);
        assertEquals(truck.getPatent(), parkedVehicle.getPatent());
    }


    @DisplayName("Ok al guardar en piso nulo")
    @Test
    void givenNullFloorWhenSaveThenError() {
        final VehicleDTO truck = new TruckDTO("b");
        final NullPointerException e = Assertions.assertThrows(NullPointerException.class, () -> {
            this.parkedVehicleRepository.save(null, 1, truck);
        });
        assertNull(e.getMessage());
    }


    @DisplayName("Ok al guardar en posición nula")
    @Test
    void givenNullPositionWhenSaveThenError() {
        final VehicleDTO truck = new TruckDTO("b");
        final NullPointerException e = Assertions.assertThrows(NullPointerException.class, () -> {
            this.parkedVehicleRepository.save(1, null, truck);
        });
    }

    @DisplayName("Ok al borrar auto")
    @Test
    void whenDeleteCarThenOk() {
        final ParkedVehicle vehicle = ParkedVehicle.builder().entryDateTime(LocalDateTime.now()).patent("a").ratio(2L).build();
        when(this.parkedVehicleRepositoryMocked.findByFloorAndPositionExitDateTimeIsNull(any(), any())).thenReturn(Optional.of(vehicle));
        final String amountToPay = this.parkedVehicleRepository.delete(1, 1);
        assertTrue(amountToPay.contains("El importe a pagar es: $"));
    }

    /*
    @DisplayName("Ok al borrar camioneta")
    @Test
    void whenDeleteTruckThenOk() {
        ParkedVehicleRepositoryImpl parkedVehicleRepositoryImpl = new ParkedVehicleRepositoryImpl();
        final VehicleDTO vehicleDTO = new TruckDTO("a");
        final ParkedVehicle parkedVehicle = parkedVehicleRepositoryImpl.save(1, 1, vehicleDTO);
        assertEquals(vehicleDTO.getPatent(), parkedVehicle.getPatent());
        final String amountToPay = parkedVehicleRepositoryImpl.delete(1, 1);
        assertTrue(amountToPay.contains("El importe a pagar es: $"));
    }

    @DisplayName("Piso erroneo al borrar vehículo")
    @Test
    void givenWrongFloorWhenDeleteVehicleThenError() {
        ParkedVehicleRepositoryImpl parkedVehicleRepositoryImpl = new ParkedVehicleRepositoryImpl();
        final VehicleDTO vehicleDTO = new TruckDTO("a");
        final ParkedVehicle parkedVehicle = parkedVehicleRepositoryImpl.save(1, 1, vehicleDTO);
        assertEquals(vehicleDTO.getPatent(), parkedVehicle.getPatent());
        final NullPointerException e = Assertions.assertThrows(NullPointerException.class, () -> {
            parkedVehicleRepositoryImpl.delete(2, 1);
        });
        assertNull(e.getMessage());
    }

    @DisplayName("Posición erronea al borrar vehículo")
    @Test
    void givenWrongPositionWhenDeleteVehicleThenError() {
        ParkedVehicleRepositoryImpl parkedVehicleRepositoryImpl = new ParkedVehicleRepositoryImpl();
        final VehicleDTO vehicleDTO = new TruckDTO("a");
        final ParkedVehicle parkedVehicle = parkedVehicleRepositoryImpl.save(1, 1, vehicleDTO);
        assertEquals(vehicleDTO.getPatent(), parkedVehicle.getPatent());
        RuntimeException e = Assertions.assertThrows(RuntimeException.class, () -> {
            parkedVehicleRepositoryImpl.delete(1, 2);
        });
        assertNull(e.getMessage());
    }

    @DisplayName("Posición vacía ok")
    @Test
    void whenIsEmptyIsInvokedThenTrue() {
        final ParkedVehicleRepositoryImpl parkedVehicleRepositoryImpl = new ParkedVehicleRepositoryImpl();
        assertTrue(parkedVehicleRepositoryImpl.isEmpty(1, 1));
    }

    @DisplayName("Posición no está vacía")
    @Test
    void whenIsEmptyIsInvokedThenPositionOccupied() {
        final ParkedVehicleRepositoryImpl parkedVehicleRepositoryImpl = new ParkedVehicleRepositoryImpl();
        parkedVehicleRepositoryImpl.save(1, 1, new CarDTO("sarasa"));
        assertFalse(parkedVehicleRepositoryImpl.isEmpty(1, 1));
    }

    @DisplayName("Piso vacío error")
    @Test
    void givenWrongFloorWhenIsEmptyIsInvokedThenError() {
        final ParkedVehicleRepositoryImpl parkedVehicleRepositoryImpl = new ParkedVehicleRepositoryImpl();
        final NullPointerException e = Assertions.assertThrows(NullPointerException.class, () -> {
            parkedVehicleRepositoryImpl.isEmpty(15, 1);
        });
        assertNull(e.getMessage());
    }

    @DisplayName("Posición vacía error")
    @Test
    void givenWrongPositionWhenIsEmptyIsInvokedThenError() {
        final ParkedVehicleRepositoryImpl parkedVehicleRepositoryImpl = new ParkedVehicleRepositoryImpl();
        assertTrue(parkedVehicleRepositoryImpl.isEmpty(1, 25));
    }

    @DisplayName("Piso negativo error")
    @Test
    void givenNegativeFloorWhenIsEmptyIsInvokedThenError() {
        final ParkedVehicleRepositoryImpl parkedVehicleRepositoryImpl = new ParkedVehicleRepositoryImpl();
        final NullPointerException e = Assertions.assertThrows(NullPointerException.class, () -> {
            parkedVehicleRepositoryImpl.isEmpty(-15, 1);
        });
        assertNull(e.getMessage());
    }

    @DisplayName("Posición negativa error")
    @Test
    void givenNegativePositionWhenIsEmptyIsInvokedThenError() {
        final ParkedVehicleRepositoryImpl parkedVehicleRepositoryImpl = new ParkedVehicleRepositoryImpl();
        assertTrue(parkedVehicleRepositoryImpl.isEmpty(1, -25));
    }

    @DisplayName("Piso nulo error")
    @Test
    void givenNullFloorWhenIsEmptyIsInvokedThenError() {
        NullPointerException e = Assertions.assertThrows(NullPointerException.class, () -> {
            ParkedVehicleRepositoryImpl parkedVehicleRepositoryImpl = new ParkedVehicleRepositoryImpl();
            parkedVehicleRepositoryImpl.isEmpty(null, 1);
        });
        assertNull(e.getMessage());
    }

    @DisplayName("Posición nula error")
    @Test
    void givenNullPositionWhenIsEmptyIsInvokedThenError() {
        final ParkedVehicleRepositoryImpl parkedVehicleRepositoryImpl = new ParkedVehicleRepositoryImpl();
        assertTrue(parkedVehicleRepositoryImpl.isEmpty(1, null));
    }
*/

    private void whenSaveThenReturnModifiedObject() {
        when(this.parkedVehicleRepositoryMocked.save(any(ParkedVehicle.class))).thenAnswer((Answer<ParkedVehicle>) invocation -> {
            final ParkedVehicle vehicle = (ParkedVehicle) invocation.getArguments()[0];
            vehicle.setId(1L);
            return vehicle;
        });
    }

}
