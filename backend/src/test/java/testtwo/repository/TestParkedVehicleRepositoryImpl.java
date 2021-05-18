package testtwo.repository;

import org.javatuples.Pair;
import testtwo.dto.CarDTO;
import testtwo.dto.TruckDTO;
import testtwo.dto.VehicleDTO;
import testtwo.entity.ParkedVehicle;
import testtwo.repository.impl.ParkedVehicleRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestParkedVehicleRepositoryImpl {

    @DisplayName("Guardar auto ok")
    @Test
    void whenSaveCarThenOk() {
        ParkedVehicleRepositoryImpl parkedVehicleRepositoryImpl = new ParkedVehicleRepositoryImpl();
        final VehicleDTO vehicleDTO = new CarDTO("a");
        final ParkedVehicle parkedVehicle = parkedVehicleRepositoryImpl.save(1, 1, vehicleDTO);
        assertEquals(vehicleDTO.getPatent(), parkedVehicle.getPatent());
    }

    @DisplayName("Guardar camioneta ok")
    @Test
    void whenSaveTruckThenOk() {
        ParkedVehicleRepositoryImpl parkedVehicleRepositoryImpl = new ParkedVehicleRepositoryImpl();
        final VehicleDTO vehicleDTO = new TruckDTO("a");
        final ParkedVehicle parkedVehicle = parkedVehicleRepositoryImpl.save(1, 1, vehicleDTO);
        assertEquals(vehicleDTO.getPatent(), parkedVehicle.getPatent());
    }

    @DisplayName("Guardar vehiculo en posición nula ok")
    @Test
    void givenNullPositionWhenSaveVehicleThenOk() {
        ParkedVehicleRepositoryImpl parkedVehicleRepositoryImpl = new ParkedVehicleRepositoryImpl();
        final VehicleDTO vehicleDTO = new CarDTO("a");
        final NullPointerException e = Assertions.assertThrows(NullPointerException.class, () -> {
            parkedVehicleRepositoryImpl.save(null, null, vehicleDTO);
        });
        assertNull(e.getMessage());
    }

    @DisplayName("Obtener el piso y posición máximo ok")
    @Test
    void whenMaxFloorAndPositionThenOk() {
        ParkedVehicleRepositoryImpl parkedVehicleRepositoryImpl = new ParkedVehicleRepositoryImpl();
        final Pair<Integer, Integer> emptyPlace = parkedVehicleRepositoryImpl.getMaxFloorAndPosition();
        assertNotNull(emptyPlace.getValue0());
        assertNotNull(emptyPlace.getValue1());
    }

    @DisplayName("Obtener el primer lugar vacío ok")
    @Test
    void whenGetFirstEmptyPlaceThenOk() {
        ParkedVehicleRepositoryImpl parkedVehicleRepositoryImpl = new ParkedVehicleRepositoryImpl();
        final Pair<Integer, Integer> emptyPlace = parkedVehicleRepositoryImpl.getFirstEmptyPlace();
        assertNotNull(emptyPlace.getValue0());
        assertNotNull(emptyPlace.getValue1());
    }

    @DisplayName("Obtener el primer lugar vacío error")
    @Test
    void whenGetFirstEmptyPlaceThenError() {
        ParkedVehicleRepositoryImpl parkedVehicleRepositoryImpl = new ParkedVehicleRepositoryImpl();
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 20; j++) {
                final VehicleDTO vehicleDTO = new CarDTO(i + String.valueOf(j));
                parkedVehicleRepositoryImpl.save(i, j, vehicleDTO);
            }
        }
        final Pair<Integer, Integer> emptyPlace = parkedVehicleRepositoryImpl.getFirstEmptyPlace();
        assertNull(emptyPlace.getValue0());
        assertNull(emptyPlace.getValue1());
    }

    @DisplayName("Error al guardar en posición ocupada")
    @Test
    void givenBusyPositionWhenSaveCarThenThrowError() {
        ParkedVehicleRepositoryImpl parkedVehicleRepositoryImpl = new ParkedVehicleRepositoryImpl();
        final VehicleDTO car = new CarDTO("a");
        final ParkedVehicle parkedVehicle = parkedVehicleRepositoryImpl.save(1, 1, car);
        assertEquals(car.getPatent(), parkedVehicle.getPatent());
        final VehicleDTO truck = new TruckDTO("b");
        final ParkedVehicle newParkedVehicle = parkedVehicleRepositoryImpl.save(1, 1, truck);
        assertEquals(truck.getPatent(), newParkedVehicle.getPatent());
    }

    @DisplayName("Error al guardar en piso erroneo")
    @Test
    void givenWrongFloorWhenSaveThenError() {
        RuntimeException e = Assertions.assertThrows(RuntimeException.class, () -> {
            ParkedVehicleRepositoryImpl parkedVehicleRepositoryImpl = new ParkedVehicleRepositoryImpl();
            final VehicleDTO truck = new TruckDTO("b");
            parkedVehicleRepositoryImpl.save(10, 1, truck);
        });
        assertNull(e.getMessage());
    }

    @DisplayName("Ok al guardar en posición erronea")
    @Test
    void givenWrongPositionWhenSaveThenError() {
        ParkedVehicleRepositoryImpl parkedVehicleRepositoryImpl = new ParkedVehicleRepositoryImpl();
        final VehicleDTO truck = new TruckDTO("b");
        final ParkedVehicle parkedVehicle = parkedVehicleRepositoryImpl.save(1, 125, truck);
        assertEquals(truck.getPatent(), parkedVehicle.getPatent());
    }

    @DisplayName("Ok al guardar en piso nulo")
    @Test
    void givenNullFloorWhenSaveThenError() {
        ParkedVehicleRepositoryImpl parkedVehicleRepositoryImpl = new ParkedVehicleRepositoryImpl();
        final VehicleDTO truck = new TruckDTO("b");
        final NullPointerException e = Assertions.assertThrows(NullPointerException.class, () -> {
            parkedVehicleRepositoryImpl.save(null, 1, truck);
        });
        assertNull(e.getMessage());
    }

    @DisplayName("Ok al guardar en posición nula")
    @Test
    void givenNullPositionWhenSaveThenError() {
        ParkedVehicleRepositoryImpl parkedVehicleRepositoryImpl = new ParkedVehicleRepositoryImpl();
        final VehicleDTO truck = new TruckDTO("b");
        final ParkedVehicle parkedVehicle = parkedVehicleRepositoryImpl.save(1, null, truck);
        assertEquals(truck.getPatent(), parkedVehicle.getPatent());
    }

    @DisplayName("Ok al borrar auto")
    @Test
    void whenDeleteCarThenOk() {
        ParkedVehicleRepositoryImpl parkedVehicleRepositoryImpl = new ParkedVehicleRepositoryImpl();
        final VehicleDTO vehicleDTO = new CarDTO("a");
        final ParkedVehicle parkedVehicle = parkedVehicleRepositoryImpl.save(1, 1, vehicleDTO);
        assertEquals(vehicleDTO.getPatent(), parkedVehicle.getPatent());
        final String amountToPay = parkedVehicleRepositoryImpl.delete(1, 1);
        assertTrue(amountToPay.contains("El importe a pagar es: $"));
    }


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

}
