package testtwo.entity;

import testtwo.dto.VehicleDTO;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ParkedVehicle {

    VehicleDTO vehicleDTO;
    LocalDateTime entryDateTime;
    LocalDateTime exitDateTime;

    public ParkedVehicle(VehicleDTO vehicleDTO) {
        this.vehicleDTO = vehicleDTO;
        this.entryDateTime = LocalDateTime.now();
    }

    public Long getFeeToPay(){
        this.exitDateTime = LocalDateTime.now();
        long hoursAmount = ChronoUnit.HOURS.between(this.entryDateTime, this.exitDateTime);
        return this.vehicleDTO.getRatio() * hoursAmount;
    }

    public String getPatent() {
        return this.vehicleDTO.getPatent();
    }

}
