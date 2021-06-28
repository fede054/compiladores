package testtwo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import testtwo.service.GarageService;

@RestController
@RequestMapping(value = "/unpark")
public class UnParkingController {

    private final GarageService garageService;

    @Autowired
    public UnParkingController(final GarageService garageService) {
        this.garageService = garageService;
    }

    @GetMapping(
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public ResponseEntity<String> unParkVehicle(@RequestParam final Integer floor,
                                                @RequestParam final Integer position) {
        return new ResponseEntity<>(this.garageService.unPark(floor, position), HttpStatus.OK);
    }

}
