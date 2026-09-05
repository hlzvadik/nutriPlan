package goltsov.nutriPlan.repositories;

import goltsov.nutriPlan.baseclasses.Eating;
import goltsov.nutriPlan.entities.UserDishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserDishRepository extends JpaRepository<UserDishEntity, Long> {
    public List<UserDishEntity> findAllByUserIdAndDishIdAndEatingAndDate(Long userId, Long dishId, Eating eating, LocalDate date);
    public List<UserDishEntity> findAllByUserIdAndDishId(Long userId, Long dishId);
    public Long deleteAllByUserIdAndDishId(Long userId, Long dishId);
}
