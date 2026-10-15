package goltsov.nutriPlan.repositories;

import goltsov.nutriPlan.entities.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Long> {
    public Dish getDishById(Long id);
}
