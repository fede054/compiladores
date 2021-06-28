package testtwo.service;

import testtwo.dto.rq.VehicleDTO;
import testtwo.dto.rs.ParkedVehicleRsDTO;

public interface GarageService {

    public ParkedVehicleRsDTO park(final Integer floor,
                                   final Integer position,
                                   final VehicleDTO vehicleDTO);

    public String unPark(final Integer floor,
                         final Integer position);

}
