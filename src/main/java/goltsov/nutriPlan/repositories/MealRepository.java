package goltsov.nutriPlan.repositories;

import goltsov.nutriPlan.entities.Dish;
import goltsov.nutriPlan.entities.Meal;
import goltsov.nutriPlan.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Long> {
    public Meal findByUserAndDish(User user, Dish dish);
    public void removeAllByDishAndUser(User user, Dish dish);
}
