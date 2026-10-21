package goltsov.nutriPlan.dto;

import java.util.Map;

public record DishDto(
        long id,
        String name,
        double kcal,
        double protein,
        double carbohydrates,
        double fats,
        int rawWeight,
        Map<String, Integer> ingredientWeightMap
) {
}
