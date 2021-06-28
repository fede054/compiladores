package testtwo.dto.rq;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class CarDTO extends VehicleDTO {

    private static final Long RATIO = 10L;

    public CarDTO(final String patent) {
        super();
        super.setPatent(patent);
    }

    @Override
    public Long getRatio() {
        return RATIO;
    }

}
