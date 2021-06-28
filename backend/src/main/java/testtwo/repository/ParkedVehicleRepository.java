package testtwo.repository;

import org.javatuples.Pair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import testtwo.dto.rq.VehicleDTO;
import testtwo.entity.ParkedVehicle;

import java.util.Optional;

@Repository
public interface ParkedVehicleRepository extends JpaRepository<ParkedVehicle, Long>, ParkedVehicleRepositoryCustom {

    public boolean existByFloorAndPositionAndExitDateTimeIsNull(final Integer floor, final Integer position);

    public Optional<ParkedVehicle> findByFloorAndPositionExitDateTimeIsNull(final Integer floor, final Integer position);

}
