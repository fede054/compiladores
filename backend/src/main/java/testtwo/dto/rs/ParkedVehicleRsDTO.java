package testtwo.dto.rs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkedVehicleRsDTO {

    private Long id;

    private String patent;

    private Integer floor;

    private Integer position;

    private Long ratio;

    private LocalDateTime entryDateTime;


}
