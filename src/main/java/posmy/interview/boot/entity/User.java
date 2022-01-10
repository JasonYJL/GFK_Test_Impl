package posmy.interview.boot.entity;

import lombok.*;
import posmy.interview.boot.constant.UserRoleEnum;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String userName;
    private String name;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    private String password;
}
