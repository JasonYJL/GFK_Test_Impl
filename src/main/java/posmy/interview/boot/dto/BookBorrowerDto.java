package posmy.interview.boot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookBorrowerDto {
    private String userName;
    private int bookId;
}
