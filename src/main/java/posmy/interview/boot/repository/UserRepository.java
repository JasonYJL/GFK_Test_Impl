package posmy.interview.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import posmy.interview.boot.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
