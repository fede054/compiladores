package garage;

import entity.ParkedVehicle;
import entity.Vehicle;

public interface GarageDB {

    public ParkedVehicle save(final Integer floor,
                              final Integer position,
                              final Vehicle vehicle);

    public String delete(final Integer floor,
                         final Integer position,
                         final ParkedVehicle parkedVehicle);

}
