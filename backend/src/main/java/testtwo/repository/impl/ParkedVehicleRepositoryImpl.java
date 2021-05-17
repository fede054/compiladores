package testtwo.repository.impl;

import testtwo.entity.ParkedVehicle;
import testtwo.dto.Vehicle;
import org.javatuples.Pair;
import org.springframework.stereotype.Component;
import testtwo.repository.ParkedVehicleRepository;

import java.util.HashMap;
import java.util.Map;

@Component
public class ParkedVehicleRepositoryImpl implements ParkedVehicleRepository {

    private final Map<Integer, Map<Integer, ParkedVehicle>> garage;

    private static final Integer FLOORS = 4;
    private static final Integer POSITIONS = 20;

    public ParkedVehicleRepositoryImpl() {
        this.garage = initializeGarage();
    }

    private Map<Integer, Map<Integer, ParkedVehicle>> initializeGarage() {
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

    @Override
    public Pair<Integer, Integer> getMaxFloorAndPosition() {
        return new Pair<>(FLOORS, POSITIONS);
    }

    @Override
    public boolean isEmpty(final Integer floor, final Integer position) {
        final Map<Integer, ParkedVehicle> obtainedFloor = this.garage.getOrDefault(floor, null);
        return obtainedFloor.getOrDefault(position, null) == null;
    }

    @Override
    public Pair<Integer, Integer> getFirstEmptyPlace() {
        for (int i = 1; i <= FLOORS; i++) {
            Map<Integer, ParkedVehicle> temp = garage.get(i);
            for (int j = 1; j <= POSITIONS; j++) {
                if (temp.get(j) == null) {
                    return new Pair<>(i, j);
                }
            }
        }
        return new Pair<>(null, null);
    }

    @Override
    public ParkedVehicle save(final Integer floor,
                              final Integer position,
                              final Vehicle vehicle) {
        final Map<Integer, ParkedVehicle> temp = this.garage.get(floor);
        final ParkedVehicle parkedVehicle = new ParkedVehicle(vehicle);
        temp.put(position, parkedVehicle);
        return parkedVehicle;
    }

    @Override
    public String delete(final Integer floor,
                         final Integer position) {
        Map<Integer, ParkedVehicle> temp = garage.get(floor);
        final ParkedVehicle vehicleToUnPark = temp.get(position);
        final Long feeToPay = vehicleToUnPark.getFeeToPay();
        temp.put(floor, null);
        return "El importe a pagar es: $" + feeToPay;
    }

}
