package posmy.interview.boot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class UserDto {

    private String userName;
    @NotNull
    private String name;
    @NotNull
    private String role;
}
