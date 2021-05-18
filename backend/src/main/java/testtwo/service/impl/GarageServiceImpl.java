package testtwo.service.impl;

import testtwo.entity.ParkedVehicle;
import testtwo.dto.VehicleDTO;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import testtwo.repository.ParkedVehicleRepository;
import testtwo.service.GarageService;

@Service
public class GarageServiceImpl implements GarageService {

    private final ParkedVehicleRepository vehicleRepository;

    @Autowired
    public GarageServiceImpl(final ParkedVehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public ParkedVehicle park(final Integer floor,
                              final Integer position,
                              final VehicleDTO vehicleDTO) {
        Integer floorToUse = floor;
        Integer positionToUse = position;
        if (floorToUse == null && positionToUse == null) {
            final Pair<Integer, Integer> freePosition = this.vehicleRepository.getFirstEmptyPlace();
            if (freePosition.getValue0() == null || freePosition.getValue1() == null) {
                throw new RuntimeException("No existe lugar disponible para estacionar");
            }
            floorToUse = freePosition.getValue0();
            positionToUse = freePosition.getValue1();
        }
        validateFieldsNotNegative(floorToUse, positionToUse);
        validateFieldsWithinMaxRange(floorToUse, positionToUse);
        if (this.vehicleRepository.isEmpty(floorToUse, positionToUse)) {
            return this.vehicleRepository.save(floorToUse, positionToUse, vehicleDTO);
        } else {
            final Pair<Integer, Integer> freePosition = this.vehicleRepository.getFirstEmptyPlace();
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
        if (!this.vehicleRepository.isEmpty(floor, position)) {
            return this.vehicleRepository.delete(floor, position);
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
        final Pair<Integer, Integer> maxFloorAndPosition = this.vehicleRepository.getMaxFloorAndPosition();
        if (floor > maxFloorAndPosition.getValue0() || position > maxFloorAndPosition.getValue1()) {
            throw new RuntimeException("Piso o posición inválida");
        }
    }

}
