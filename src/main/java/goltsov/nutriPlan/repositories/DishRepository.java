package goltsov.nutriPlan.repositories;

import goltsov.nutriPlan.dto.FilterSortRequestDishDto;
import goltsov.nutriPlan.entities.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DishRepository extends JpaRepository<Dish, Long> {

    @Query("""
            SELECT dish FROM Dish dish
            WHERE
                dish.kcal BETWEEN :#{#dto.kcal.min} AND :#{#dto.kcal.max}
            AND dish.protein BETWEEN :#{#dto.protein.min} AND :#{#dto.protein.max}
            AND dish.carbohydrates BETWEEN :#{#dto.carbohydrates.min} AND :#{#dto.carbohydrates.max}
            AND dish.fats BETWEEN :#{#dto.fats.min} AND :#{#dto.fats.max}
            AND dish.rawWeight BETWEEN :#{#dto.rawWeight.min} AND :#{#dto.rawWeight.max}
            
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
            """)
    Page<Dish> getWithFilterAndSort(@Param("dto") FilterSortRequestDishDto dto, Pageable pageable);
}