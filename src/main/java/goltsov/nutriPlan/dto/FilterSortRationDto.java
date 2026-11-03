package goltsov.nutriPlan.dto;

import goltsov.nutriPlan.entities.Gender;

import java.util.Collection;
import java.util.List;

public record FilterSortRationDto (
        Range<Double> kcal,
        Range<Double> protein,
        Range<Double> carbohydrates,
        Range<Double> fats,
        Range<Integer> rawWeight,

        List<Integer> percents,

        List<String> badIngredients,

        Gender gender,
        Integer weight,

        Integer page,
        Integer size
) {
    public FilterSortRationDto {
        boolean needWeight = kcal == null || protein == null || carbohydrates == null || fats == null;
        if (needWeight && (weight == null || weight <= 0)) {
            throw new IllegalArgumentException("weight must be > 0 when kcal/protein/carbohydrates/fats ranges are not set");
        }
        page = page != null ? page : 0;
        size = size != null ? size : 20;
        kcal = kcal != null ? kcal : new Range<>(25.0 * weight, 30.0 * weight);
        protein = protein != null ? protein : new Range<>(0.8 * weight, 1.2 * weight);
        carbohydrates = carbohydrates != null ? carbohydrates : new Range<>(3.0 * weight, 4.0 * weight);
        fats = fats != null ? fats : new Range<>(1.0 * weight, 2.0 * weight);
        rawWeight = rawWeight != null ? rawWeight : new Range<>(0, Integer.MAX_VALUE);
        if (percents == null || percents.size() != 3 || Math.abs(percents.stream().mapToInt(Integer::intValue).sum() - 100) > 5) {
            percents = List.of(30, 45, 25);
        }
    }
}
