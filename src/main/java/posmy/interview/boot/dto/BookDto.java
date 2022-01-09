package posmy.interview.boot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class BookDto {
    private int id;

    @NotNull
    private String title;

    @NotNull
    private String description;

    private String status;
}
