package testtwo.controller;

import org.springframework.http.MediaType;
import testtwo.dto.CarDTO;
import testtwo.dto.TruckDTO;
import testtwo.dto.VehicleDTO;
import testtwo.entity.ParkedVehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import testtwo.service.GarageService;

@RestController
@RequestMapping(value = "/park")
public class ParkingController {

    private final GarageService garageService;

    @Autowired
    public ParkingController(final GarageService garageService) {
        this.garageService = garageService;
    }

    @PostMapping(
            value = "/car",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParkedVehicle> parkVehicle(@RequestParam final Integer floor,
                                                     @RequestParam final Integer position,
                                                     @RequestBody final CarDTO carDTO) {
        final ParkedVehicle parkedVehicle = this.garageService.park(floor, position, carDTO);
        return new ResponseEntity<>(parkedVehicle, HttpStatus.OK);
    }

    @PostMapping(
            value = "/truck",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParkedVehicle> parkTruck(@RequestParam final Integer floor,
                                                   @RequestParam final Integer position,
                                                   @RequestBody final TruckDTO truckDTO) {
        return new ResponseEntity<>(this.garageService.park(floor, position, truckDTO), HttpStatus.OK);
    }

}
