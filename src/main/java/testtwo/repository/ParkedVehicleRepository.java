package testtwo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import testtwo.entity.ParkedVehicle;

import java.util.Optional;

@Repository
public interface ParkedVehicleRepository extends JpaRepository<ParkedVehicle, Long>, CustomParkedVehicleRepository {

    public boolean existsByFloorAndPositionAndExitDateTimeIsNull(final Integer floor, final Integer position);

    public Optional<ParkedVehicle> findByFloorAndPosition(final Integer floor, final Integer position);

    public Optional<ParkedVehicle> findByFloorAndPositionAndExitDateTimeIsNull(final Integer floor, final Integer position);

}
