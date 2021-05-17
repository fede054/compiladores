package testtwo.repository;

import testtwo.entity.ParkedVehicle;
import testtwo.dto.Vehicle;
import org.javatuples.Pair;

public interface ParkedVehicleRepository {

    public Pair<Integer, Integer> getMaxFloorAndPosition();

    public boolean isEmpty(final Integer floor,
                           final Integer position);

    public Pair<Integer, Integer> getFirstEmptyPlace();

    public ParkedVehicle save(final Integer floor,
                              final Integer position,
                              final Vehicle vehicle);

    public String delete(final Integer floor,
                         final Integer position);

}
