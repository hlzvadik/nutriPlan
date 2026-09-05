package goltsov.nutriPlan.repositories;

import goltsov.nutriPlan.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
