import entity.Car;
import entity.ParkedVehicle;
import entity.Truck;
import entity.Vehicle;
import garage.DBFacker;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestDBFacker {

    @DisplayName("Garage test Mock ok")
    @Test
    void save() {
        DBFacker dbFacker = new DBFacker();
        final Vehicle car = new Car("a");
        final ParkedVehicle parkedVehicle = dbFacker.save(1, 1, car);
        assertEquals(car.getPatent(), parkedVehicle.getPatent());

    }

    @DisplayName("Garage test Mock dos autos ok")
    @Test
    void saveTwoCars() {
        DBFacker dbFacker = new DBFacker();
        final Vehicle car = new Car("a");
        final ParkedVehicle parkedVehicle = dbFacker.save(1, 1, car);
        assertEquals(car.getPatent(), parkedVehicle.getPatent());

        RuntimeException e = Assertions.assertThrows(RuntimeException.class, () -> {
            final Vehicle truck = new Truck("b");
            dbFacker.save(1, 1, truck);
        });
        assertTrue(e.getMessage().contains("ya existe un vehículo en esa posicion"));

    }

    @DisplayName("Garage test Mock not ok")
    @Test
    void saveWithError() {

        RuntimeException e = Assertions.assertThrows(RuntimeException.class, () -> {
            DBFacker dbFacker = new DBFacker();
            final Vehicle truck = new Truck("b");
            final ParkedVehicle parkedVehicle = dbFacker.save(10, 125, truck);
        });
        assertEquals(e.getMessage(), "No existe esa posicion");

    }


}
