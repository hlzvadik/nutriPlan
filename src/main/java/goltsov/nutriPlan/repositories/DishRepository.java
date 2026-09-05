package goltsov.nutriPlan.repositories;

import goltsov.nutriPlan.entities.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<DishEntity, Long> {
}
