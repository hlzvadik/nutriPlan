package goltsov.nutriPlan.repositories;

import goltsov.nutriPlan.entities.DishIngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DishIngredientRepository extends JpaRepository<DishIngredientEntity, Long> {
    public List<DishIngredientEntity> findAllByDishId(Long dishId);
    public List<DishIngredientEntity> findAllByDishIdAndIngredientId(Long dishId, Long ingredientId);
    public Long deleteAllByDishId(Long DishId);
    public Long deleteAllByDishIdAndIngredientId(Long dishId, Long ingredientId);
}
