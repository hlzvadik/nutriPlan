package goltsov.nutriPlan.dto;

import java.util.Map;
import java.util.Set;

public record DishDto(
        long id,
        String name,
        double kcal,
        double protein,
        double carbohydrates,
        double fats,
        int rawWeight,
        long recipeId,
        int countLikes,
        Set<String> eatings,
        Map<String, Integer> ingredientWeightMap
) {
}
