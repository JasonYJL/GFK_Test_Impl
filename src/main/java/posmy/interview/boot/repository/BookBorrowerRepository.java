package posmy.interview.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import posmy.interview.boot.entity.BookBorrower;

public interface BookBorrowerRepository extends JpaRepository<BookBorrower, Integer> {
}
