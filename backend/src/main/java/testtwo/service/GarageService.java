package testtwo.service;

import testtwo.dto.Vehicle;
import testtwo.entity.ParkedVehicle;

public interface GarageService {

    public ParkedVehicle park(final Integer floor,
                              final Integer position,
                              final Vehicle vehicle);

    public String unPark(final Integer floor,
                         final Integer position);

}
