package testtwo.service.impl;

import testtwo.converter.rq.VehicleRqConverter;
import testtwo.converter.rs.ParkedVehicleRsConverter;
import testtwo.dto.rs.ParkedVehicleRsDTO;
import testtwo.dto.rq.VehicleDTO;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import testtwo.entity.ParkedVehicle;
import testtwo.repository.ParkedVehicleRepository;
import testtwo.service.GarageService;

@Service
public class GarageServiceImpl implements GarageService {

    private final ParkedVehicleRepository parkedVehicleRepository;
    private final VehicleRqConverter vehicleRqConverter;
    private final ParkedVehicleRsConverter parkedVehicleRsConverter;

    @Autowired
    public GarageServiceImpl(final ParkedVehicleRepository parkedVehicleRepository,
                             final VehicleRqConverter vehicleRqConverter,
                             final ParkedVehicleRsConverter parkedVehicleRsConverter) {
        this.parkedVehicleRepository = parkedVehicleRepository;
        this.vehicleRqConverter = vehicleRqConverter;
        this.parkedVehicleRsConverter = parkedVehicleRsConverter;
    }

    @Override
    public ParkedVehicleRsDTO park(final Integer floor,
                                   final Integer position,
                                   final VehicleDTO vehicleDTO) {
        var floorToUse = floor;
        var positionToUse = position;
        if (floorToUse == null && positionToUse == null) {
            final Pair<Integer, Integer> freePosition = this.parkedVehicleRepository.getFirstEmptyPlace();
            if (freePosition.getValue0() == null || freePosition.getValue1() == null) {
                throw new RuntimeException("No existe lugar disponible para estacionar");
            }
            floorToUse = freePosition.getValue0();
            positionToUse = freePosition.getValue1();
        }
        validateFieldsNotNegative(floorToUse, positionToUse);
        validateFieldsWithinMaxRange(floorToUse, positionToUse);
        if (this.parkedVehicleRepository.isEmpty(floorToUse, positionToUse)) {
            final var vehicleToPark = this.vehicleRqConverter.convert(vehicleDTO);
            final var parkedVehicle = this.parkedVehicleRepository.save(floorToUse, positionToUse, vehicleToPark);
            return this.parkedVehicleRsConverter.convert(parkedVehicle);
        } else {
            final Pair<Integer, Integer> freePosition = this.parkedVehicleRepository.getFirstEmptyPlace();
            throw new RuntimeException("ya existe un vehículo en esa posicion. La primera posición vacía es piso: "
                    + freePosition.getValue0() + ", posición: " + freePosition.getValue1());
        }
    }

    @Override
    public String unPark(final Integer floor,
                         final Integer position) {
        validateFieldsNotNull(floor, position);
        validateFieldsNotNegative(floor, position);
        validateFieldsWithinMaxRange(floor, position);
        if (!this.parkedVehicleRepository.isEmpty(floor, position)) {
            return this.parkedVehicleRepository.delete(floor, position);
        } else {
            throw new RuntimeException("No se puede remover un vehículo del piso o posición indicado");
        }
    }

    private void validateFieldsNotNull(final Integer floor,
                                       final Integer position) {
        if (floor == null || position == null) {
            throw new RuntimeException("Piso y posición no pueden ser nulos");
        }
    }

    private void validateFieldsNotNegative(final Integer floor,
                                           final Integer position) {
        if (floor < 0 || position < 0) {
            throw new RuntimeException("Piso y posición no pueden ser menor a 0");
        }
    }

    private void validateFieldsWithinMaxRange(final Integer floor,
                                              final Integer position) {
        final Pair<Integer, Integer> maxFloorAndPosition = this.parkedVehicleRepository.getMaxFloorAndPosition();
        if (floor > maxFloorAndPosition.getValue0() || position > maxFloorAndPosition.getValue1()) {
            throw new RuntimeException("Piso o posición inválida");
        }
    }

}
