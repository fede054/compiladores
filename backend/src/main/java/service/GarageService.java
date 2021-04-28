package service;

import entity.ParkedVehicle;
import dto.Vehicle;
import repository.ParkedVehicleRepository;

public class GarageService {

    private final ParkedVehicleRepository vehicleRepository;

    public GarageService(final ParkedVehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public ParkedVehicle park(final Integer floor,
                              final Integer position,
                              final Vehicle vehicle) {
        if (this.vehicleRepository.isEmpty(floor, position)) {
            return this.vehicleRepository.save(floor, position, vehicle);
        } else {
            throw new RuntimeException("No se puede estacionar en el piso o posición solicitada");
        }
    }

    public String unPark(final Integer floor,
                         final Integer position) {
        if (!this.vehicleRepository.isEmpty(floor, position)) {
            return this.vehicleRepository.delete(floor, position);
        } else {
            throw new RuntimeException("No se puede remover un vehículo del piso o posición indicado");
        }
    }

}
