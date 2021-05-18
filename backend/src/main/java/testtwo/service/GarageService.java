package testtwo.service;

import testtwo.dto.VehicleDTO;
import testtwo.entity.ParkedVehicle;

public interface GarageService {

    public ParkedVehicle park(final Integer floor,
                              final Integer position,
                              final VehicleDTO vehicleDTO);

    public String unPark(final Integer floor,
                         final Integer position);

}
