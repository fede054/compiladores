package testtwo.dto.rs;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorRsDTO {

    private final int code;
    private final String description;

}
