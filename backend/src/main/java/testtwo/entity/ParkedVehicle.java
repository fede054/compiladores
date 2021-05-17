package testtwo.entity;

import testtwo.dto.Vehicle;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ParkedVehicle {

    Vehicle vehicle;
    LocalDateTime entryDateTime;
    LocalDateTime exitDateTime;

    public ParkedVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.entryDateTime = LocalDateTime.now();
    }

    public Long getFeeToPay(){
        this.exitDateTime = LocalDateTime.now();
        long hoursAmount = ChronoUnit.HOURS.between(this.entryDateTime, this.exitDateTime);
        return this.vehicle.getRatio() * hoursAmount;
    }

    public String getPatent() {
        return this.vehicle.getPatent();
    }

}
