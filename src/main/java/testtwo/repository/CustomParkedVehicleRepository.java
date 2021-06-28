package testtwo.repository;

import org.javatuples.Pair;
import testtwo.entity.ParkedVehicle;

public interface CustomParkedVehicleRepository {

    public Pair<Integer, Integer> getMaxFloorAndPosition();

    public boolean isEmpty(final Integer floor,
                           final Integer position);

    public Pair<Integer, Integer> getFirstEmptyPlace();

    public ParkedVehicle save(final Integer floor,
                              final Integer position,
                              final ParkedVehicle parkedVehicle);

    public String delete(final Integer floor,
                         final Integer position);

}
