package testtwo.converter.rs;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import testtwo.dto.rs.ParkedVehicleRsDTO;
import testtwo.entity.ParkedVehicle;

@Component
public class ParkedVehicleRsConverter implements Converter<ParkedVehicle, ParkedVehicleRsDTO> {

    @Override
    public ParkedVehicleRsDTO convert(final ParkedVehicle parkedVehicle) {
        final ParkedVehicleRsDTO parkedVehicleRsDTO = new ParkedVehicleRsDTO();
        parkedVehicleRsDTO.setId(parkedVehicle.getId());
        parkedVehicleRsDTO.setPatent(parkedVehicle.getPatent());
        parkedVehicleRsDTO.setFloor(parkedVehicle.getFloor());
        parkedVehicleRsDTO.setPosition(parkedVehicle.getPosition());
        parkedVehicleRsDTO.setEntryDateTime(parkedVehicle.getEntryDateTime());
        parkedVehicleRsDTO.setRatio(parkedVehicle.getRatio());
        return parkedVehicleRsDTO;
    }


}
