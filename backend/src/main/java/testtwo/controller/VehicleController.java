package testtwo.controller;

import testtwo.dto.Car;
import testtwo.dto.Truck;
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
@RequestMapping(value = "/vehicule")
public class VehicleController {

    private final GarageService garageService;

    @Autowired
    public VehicleController(final GarageService garageService) {
        this.garageService = garageService;
    }

    @PostMapping(value = "/car")
    public ResponseEntity<ParkedVehicle> parkCar(@RequestParam final Integer floor,
                                                 @RequestParam final Integer position,
                                                 @RequestBody final Car car) {
        return new ResponseEntity<>(this.garageService.park(floor, position, car), HttpStatus.OK);
    }

    @PostMapping(value = "/truck")
    public ResponseEntity<ParkedVehicle> parkTruck(@RequestParam final Integer floor,
                                                   @RequestParam final Integer position,
                                                   @RequestBody final Truck truck) {
        return new ResponseEntity<>(this.garageService.park(floor, position, truck), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<String> unParkVehicle(@RequestParam final Integer floor,
                                                @RequestParam final Integer position) {
        return new ResponseEntity<>(this.garageService.unPark(floor, position), HttpStatus.OK);
    }

}
