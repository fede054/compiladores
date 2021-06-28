package testtwo.repository.impl;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import testtwo.entity.ParkedVehicle;
import testtwo.repository.CustomParkedVehicleRepository;
import testtwo.repository.ParkedVehicleRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class CustomParkedVehicleRepositoryImpl implements CustomParkedVehicleRepository {

    private static final Integer FLOORS = 4;
    private static final Integer POSITIONS = 20;
    private final ParkedVehicleRepository parkedVehicleRepository;

    @Autowired
    public CustomParkedVehicleRepositoryImpl(@Lazy final ParkedVehicleRepository parkedVehicleRepository) {
        this.parkedVehicleRepository = parkedVehicleRepository;
    }

    @Override
    public Pair<Integer, Integer> getMaxFloorAndPosition() {
        return new Pair<>(FLOORS, POSITIONS);
    }

    @Override
    public boolean isEmpty(final Integer floor, final Integer position) {
        return (floor != null && position != null) &&
                (floor > 0 && position > 0) &&
                (floor <= FLOORS && position <= POSITIONS) &&
                !this.parkedVehicleRepository.existsByFloorAndPositionAndExitDateTimeIsNull(floor, position);
    }

    @Override
    public Pair<Integer, Integer> getFirstEmptyPlace() {
        for (int i = 1; i <= FLOORS; i++) {
            for (int j = 1; j <= POSITIONS; j++) {
                final Optional<ParkedVehicle> exists = this.parkedVehicleRepository.findByFloorAndPosition(i, j);
                if (exists.isEmpty() || (exists.get().getExitDateTime() != null)) {
                    return new Pair<>(i, j);
                }
            }
        }
        return new Pair<>(null, null);
    }

    @Override
    public ParkedVehicle save(final Integer floor, final Integer position, final ParkedVehicle parkedVehicle) {
        parkedVehicle.setFloor(floor);
        parkedVehicle.setPosition(position);
        return this.parkedVehicleRepository.save(parkedVehicle);
    }

    @Override
    public String delete(final Integer floor, final Integer position) {
        final Optional<ParkedVehicle> foundVehicle = this.parkedVehicleRepository.findByFloorAndPositionAndExitDateTimeIsNull(floor, position);
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
