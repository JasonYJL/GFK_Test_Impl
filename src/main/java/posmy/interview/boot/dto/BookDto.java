package posmy.interview.boot.dto;

import lombok.*;
import posmy.interview.boot.constant.BookStatusEnum;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private int id;

    @NotNull
    private String title;

    @NotNull
    private String description;

    private BookStatusEnum status;
}
