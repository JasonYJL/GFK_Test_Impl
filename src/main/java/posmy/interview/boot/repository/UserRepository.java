package posmy.interview.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import posmy.interview.boot.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByRole(String role);
}
