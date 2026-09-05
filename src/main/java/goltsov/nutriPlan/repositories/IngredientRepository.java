package goltsov.nutriPlan.repositories;

import goltsov.nutriPlan.entities.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<IngredientEntity, Long> {
}
