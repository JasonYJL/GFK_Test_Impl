package posmy.interview.boot.dto;

import lombok.*;
import posmy.interview.boot.constant.UserRoleEnum;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String userName;
    @NotNull
    private String name;
    @NotNull
    private UserRoleEnum role;
    private String password;
}
