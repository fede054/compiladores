package testtwo.converter.rq;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import testtwo.dto.rq.VehicleDTO;
import testtwo.entity.ParkedVehicle;

@Component
public class VehicleRqConverter implements Converter<VehicleDTO, ParkedVehicle> {

    @Override
    public ParkedVehicle convert(final VehicleDTO vehicleDTO) {
        final ParkedVehicle parkedVehicle = new ParkedVehicle();
        parkedVehicle.setPatent(vehicleDTO.getPatent());
        parkedVehicle.setRatio(vehicleDTO.getRatio());
        return parkedVehicle;
    }

}
