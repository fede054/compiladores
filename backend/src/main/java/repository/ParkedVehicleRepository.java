package repository;

import entity.ParkedVehicle;
import dto.Vehicle;

public interface ParkedVehicleRepository {

    public boolean isEmpty(final Integer floor,
                           final Integer position);

    public ParkedVehicle save(final Integer floor,
                              final Integer position,
                              final Vehicle vehicle);

    public String delete(final Integer floor,
                         final Integer position);

}
