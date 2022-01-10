package posmy.interview.boot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ApiExceptionResponseDto {
    private String message;
    private String code;
}
