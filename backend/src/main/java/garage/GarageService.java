package garage;

import entity.ParkedVehicle;
import entity.Vehicle;

public class GarageService {

    private final GarageDB garage;

    public GarageService(final GarageDB garage) {
        this.garage = garage;
    }

    public ParkedVehicle park(final Integer floor,
                              final Integer position,
                              final Vehicle vehicle) {
        return this.garage.save(floor, position, vehicle);
    }

    public String unPark(final Integer floor,
                         final Integer position,
                         final ParkedVehicle parkedVehicle) {
        return this.garage.delete(floor, position, parkedVehicle);
    }

}
