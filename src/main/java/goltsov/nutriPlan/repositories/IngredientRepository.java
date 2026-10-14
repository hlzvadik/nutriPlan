package goltsov.nutriPlan.repositories;

import goltsov.nutriPlan.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
