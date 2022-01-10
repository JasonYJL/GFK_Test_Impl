package posmy.interview.boot.entity;

import lombok.*;
import posmy.interview.boot.constant.BookStatusEnum;

import javax.persistence.*;

@Setter
@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private BookStatusEnum status;
}
