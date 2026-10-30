package goltsov.nutriPlan.repositories;

import goltsov.nutriPlan.dto.FilterSortRequestDishDto;
import goltsov.nutriPlan.entities.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface DishRepository extends JpaRepository<Dish, Long> {

    @Transactional(readOnly = true)
    @Query("""
            SELECT dish FROM Dish dish
            WHERE
                dish.protein * :#{#avgKcal} / dish.kcal BETWEEN :#{#dto.protein.min} AND :#{#dto.protein.max}
            AND dish.carbohydrates * :#{#avgKcal} / dish.kcal BETWEEN :#{#dto.carbohydrates.min} AND :#{#dto.carbohydrates.max}
            AND dish.fats * :#{#avgKcal} / dish.kcal BETWEEN :#{#dto.fats.min} AND :#{#dto.fats.max}
            AND dish.rawWeight * :#{#avgKcal} / dish.kcal BETWEEN :#{#dto.rawWeight.min} AND :#{#dto.rawWeight.max}
            
            AND (:#{#dto.goodNames} IS NULL OR dish.name IN :#{#dto.goodNames})
            AND (:#{#dto.badNames} IS NULL OR dish.name NOT IN :#{#dto.badNames})
            
            AND (:#{#dto.mandatoryIngredients} IS NULL OR
                (SELECT COUNT(DISTINCT KEY(m).name)
                 FROM Dish d2
                 JOIN d2.ingredientWeightMap m
                 WHERE d2 = dish
                   AND KEY(m).name IN :#{#dto.mandatoryIngredients})
                = :#{#dto.mandatoryIngredients.size()})
            
            AND (:#{#dto.badIngredients} IS NULL OR
                (SELECT COUNT(DISTINCT KEY(m).name)
                 FROM Dish d2
                 JOIN d2.ingredientWeightMap m
                 WHERE d2 = dish
                   AND KEY(m).name IN :#{#dto.badIngredients})
                = 0)
            
            AND (:#{#dto.mandatoryEatingNames()} IS NULL OR
                       (SELECT COUNT(DISTINCT e.eatingName)
                        FROM Dish d2
                        JOIN d2.eatings e
                        WHERE d2 = dish
                        AND e.eatingName IN :#{#dto.mandatoryEatingNames()})
                 = 1)
            """)
    Page<Dish> getWithFilterAndSort(@Param("dto") FilterSortRequestDishDto dto, @Param("avgKcal") Double avgKcal, Pageable pageable);
}