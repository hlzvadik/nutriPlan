package goltsov.nutriPlan.repositories;

import goltsov.nutriPlan.dto.FilterSortRequestIngredientDto;
import goltsov.nutriPlan.entities.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Ingredient findByName(String name);

    @Query("""
                SELECT ingredient FROM Ingredient ingredient
                WHERE
                    ingredient.kcal BETWEEN :#{#dto.kcal.min} AND :#{#dto.kcal.max}
                AND ingredient.protein BETWEEN :#{#dto.protein.min} AND :#{#dto.protein.max}
                AND ingredient.carbohydrates BETWEEN :#{#dto.carbohydrates.min} AND :#{#dto.carbohydrates.max}
                AND ingredient.fats BETWEEN :#{#dto.fats.min} AND :#{#dto.fats.max}
                AND ingredient.weight BETWEEN :#{#dto.weight.min} AND :#{#dto.weight.max}
            
                AND (:#{#dto.goodNames} IS NULL OR ingredient.name IN :#{#dto.goodNames})
                AND (:#{#dto.badNames} IS NULL OR ingredient.name NOT IN :#{#dto.badNames})
            """)
    Page<Ingredient> getWithFilterAndSort(@Param("dto") FilterSortRequestIngredientDto filterSortRequestIngredientDto, Pageable pageable);
}
