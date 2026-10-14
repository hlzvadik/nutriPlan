package goltsov.nutriPlan.repositories;

import goltsov.nutriPlan.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    public User getByEmail(String email);
}
