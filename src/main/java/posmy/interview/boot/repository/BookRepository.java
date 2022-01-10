package posmy.interview.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import posmy.interview.boot.constant.BookStatusEnum;
import posmy.interview.boot.entity.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByStatus(BookStatusEnum status);
}
