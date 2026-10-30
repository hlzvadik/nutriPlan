package goltsov.nutriPlan.repositories;

import goltsov.nutriPlan.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
