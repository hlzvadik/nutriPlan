package goltsov.nutriPlan.repositories;

import goltsov.nutriPlan.entities.Dish;
import goltsov.nutriPlan.entities.Meal;
import goltsov.nutriPlan.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {
    Meal findByUserAndDish(User user, Dish dish);
    void removeAllByDishAndUser(User user, Dish dish);
    List<Meal> findAllByUserAndTimeBetween(User user, LocalDateTime timeAfter, LocalDateTime timeBefore);

    @Query("""
        SELECT DISTINCT m FROM Meal m
        JOIN FETCH m.dish d
        LEFT JOIN FETCH d.ingredientWeightMap
        WHERE m.user = :user AND m.time BETWEEN :timeAfter AND :timeBefore
    """)
    List<Meal> findAllByUserAndTimeBetweenWithDetails(
            @Param("user") User user,
            @Param("timeAfter") LocalDateTime timeAfter,
            @Param("timeBefore") LocalDateTime timeBefore
    );
}
