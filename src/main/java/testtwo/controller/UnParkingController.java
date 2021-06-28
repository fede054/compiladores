package testtwo.controller;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import testtwo.dto.rs.ErrorRsDTO;
import testtwo.service.GarageService;

@RestController
@RequestMapping(value = "/unpark")
public class UnParkingController {

    private final GarageService garageService;

    @Autowired
    public UnParkingController(final GarageService garageService) {
        this.garageService = garageService;
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = String.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorRsDTO.class)
    })
    @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> unParkVehicle(@RequestParam final Integer floor,
                                                @RequestParam final Integer position) {
        return new ResponseEntity<>(this.garageService.unPark(floor, position), HttpStatus.OK);
    }

}
