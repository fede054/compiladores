package garage;

import entity.ParkedVehicle;
import entity.Vehicle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DBFacker implements GarageDB {

    private static final int FLOORS = 4;
    private static final int POSITIONS = 20;

    private final Map<Integer, Map<Integer, ParkedVehicle>> garage = initializeGarage();

    private static Map<Integer, Map<Integer, ParkedVehicle>> initializeGarage() {
        final Map<Integer, Map<Integer, ParkedVehicle>> toReturn = new HashMap<>();
        for (int i = 1; i < FLOORS; i++) {
            final HashMap<Integer, ParkedVehicle> toInsert = new HashMap<>();
            for (int j = 1; j < POSITIONS; j++) {
                toInsert.put(j, null);
            }
            toReturn.put(i, toInsert);
        }
        return toReturn;
    }

    public ParkedVehicle save(final Integer floor,
                              final Integer position,
                              final Vehicle vehicle) {

        Integer floorToUse = floor;
        Integer positionToUse = position;

        if (floor == null && position == null) {
            final Integer[] freePosition = getFirstFreePark();
            floorToUse = freePosition[0];
            positionToUse = freePosition[1];
        }

        if (floorToUse != null && positionToUse != null) {
            if (floorToUse > FLOORS || floorToUse < 1 || positionToUse > POSITIONS || positionToUse < 1) {
                throw new RuntimeException("No existe esa posicion");
            }
            Map<Integer, ParkedVehicle> temp = garage.getOrDefault(floorToUse, null);
            if (temp.get(positionToUse) != null) {
                final Integer[] freePosition = getFirstFreePark();
                throw new RuntimeException("ya existe un vehículo en esa posicion. La primera posición vacía es piso: "
                        + freePosition[0] + ", posición: " + freePosition[1]);
            }
            ParkedVehicle parkedVehicle = new ParkedVehicle(vehicle);
            temp.put(positionToUse, parkedVehicle);
            return parkedVehicle;
        }

        throw new RuntimeException("Piso y posición deben estar ambos completos o ambos nulos");

    }

    @Override
    public String delete(final Integer floor,
                         final Integer position,
                         final ParkedVehicle parkedVehicle) {
        if (floor != null && position != null && parkedVehicle != null) {
            if (floor > FLOORS || floor < 1 || position > POSITIONS || position < 1) {
                throw new RuntimeException("No existe esa posicion");
            }
            Map<Integer, ParkedVehicle> temp = garage.getOrDefault(floor, null);
            final ParkedVehicle vehicleToUnpark = temp.get(position);
            if (vehicleToUnpark != null && parkedVehicle.getPatent().equalsIgnoreCase(vehicleToUnpark.getPatent())) {
                final Long feeToPay = vehicleToUnpark.getFeeToPay();
                temp.put(floor, null);
                return "El importe a pagar es: $" + feeToPay;
            } else {
                throw new RuntimeException("No existe un vehículo en esa posicion");
            }
        }
        throw new RuntimeException("Piso, posición, y vehículo no pueden ser nulos");
    }

    private Integer[] getFirstFreePark() {
        for (int i = 1; i <= FLOORS; i++) {
            Map<Integer, ParkedVehicle> temp = garage.get(i);
            for (int j = 1; j <= POSITIONS; j++) {
                if (temp.get(j) == null) {
                    return new Integer[]{i, j};
                }
            }
        }
        throw new RuntimeException("No existen posiciones vacías");
    }

}
