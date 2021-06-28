package testtwo.dto.rq;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class TruckDTO extends VehicleDTO {

    private static final Long RATIO = 15L;

    public TruckDTO(final String patent) {
        super();
        super.setPatent(patent);
    }

    @Override
    public Long getRatio() {
        return RATIO;
    }

}
