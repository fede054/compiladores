package testtwo.repository;

import testtwo.dto.rs.ParkedVehicleRsDTO;
import testtwo.dto.rq.VehicleDTO;
import org.javatuples.Pair;

public interface ParkedVehicleRepositoryCustom {

    public Pair<Integer, Integer> getMaxFloorAndPosition();

    public boolean isEmpty(final Integer floor,
                           final Integer position);

    public Pair<Integer, Integer> getFirstEmptyPlace();

    public ParkedVehicleRsDTO save(final Integer floor,
                                   final Integer position,
                                   final VehicleDTO vehicleDTO);

    public String delete(final Integer floor,
                         final Integer position);

}
