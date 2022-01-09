package posmy.interview.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.entity.User;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
