package testtwo.repository.impl;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import testtwo.converter.rq.VehicleRqConverter;
import testtwo.converter.rs.ParkedVehicleRsConverter;
import testtwo.dto.rq.VehicleDTO;
import testtwo.dto.rs.ParkedVehicleRsDTO;
import testtwo.entity.ParkedVehicle;
import testtwo.repository.ParkedVehicleRepository;
import testtwo.repository.ParkedVehicleRepositoryCustom;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ParkedVehicleRepositoryCustomImpl implements ParkedVehicleRepositoryCustom {

    private static final Integer FLOORS = 4;
    private static final Integer POSITIONS = 20;
    private final ParkedVehicleRepository parkedVehicleRepository;
    private final VehicleRqConverter vehicleRqConverter;
    private final ParkedVehicleRsConverter parkedVehicleRsConverter;

    @Autowired
    public ParkedVehicleRepositoryCustomImpl(final ParkedVehicleRepository parkedVehicleRepository,
                                             final VehicleRqConverter vehicleRqConverter,
                                             final ParkedVehicleRsConverter parkedVehicleRsConverter) {
        this.parkedVehicleRepository = parkedVehicleRepository;
        this.vehicleRqConverter = vehicleRqConverter;
        this.parkedVehicleRsConverter = parkedVehicleRsConverter;
    }


    @Override
    public Pair<Integer, Integer> getMaxFloorAndPosition() {
        return new Pair<>(FLOORS, POSITIONS);
    }

    @Override
    public boolean isEmpty(final Integer floor, final Integer position) {
        return !this.parkedVehicleRepository.existByFloorAndPositionAndExitDateTimeIsNull(floor, position);
    }

    @Override
    public Pair<Integer, Integer> getFirstEmptyPlace() {
        for (int i = 1; i <= FLOORS; i++) {
            for (int j = 1; j <= POSITIONS; j++) {
                final boolean isEmpty = this.parkedVehicleRepository.existByFloorAndPositionAndExitDateTimeIsNull(i, j);
                if (isEmpty) {
                    return new Pair<>(i, j);
                }
            }
        }
        return new Pair<>(null, null);
    }

    @Override
    public ParkedVehicleRsDTO save(final Integer floor, final Integer position, final VehicleDTO vehicleDTO) {
        final ParkedVehicle parkedVehicle = this.vehicleRqConverter.convert(vehicleDTO);
        parkedVehicle.setFloor(floor);
        parkedVehicle.setPosition(position);
        final ParkedVehicle savedVehicle = this.parkedVehicleRepository.save(parkedVehicle);
        return this.parkedVehicleRsConverter.convert(savedVehicle);
    }

    @Override
    public String delete(final Integer floor, final Integer position) {
        final Optional<ParkedVehicle> foundVehicle = this.parkedVehicleRepository.findByFloorAndPositionExitDateTimeIsNull(floor, position);
        if (foundVehicle.isEmpty()) {
            throw new RuntimeException("No existe un vehículo en ese piso y posición");
        } else {
            final ParkedVehicle parkedVehicle = foundVehicle.get();
            final String feeToPay = "El importe a pagar es: $" + parkedVehicle.getFeeToPay();
            parkedVehicle.setExitDateTime(LocalDateTime.now());
            this.parkedVehicleRepository.save(parkedVehicle);
            return feeToPay;
        }
    }

}
