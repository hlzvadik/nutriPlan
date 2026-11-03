package goltsov.nutriPlan.dto;

import goltsov.nutriPlan.entities.EatingName;

import java.util.List;

public record FilterSortRequestDishDto(
        Range<Double> kcal,
        Range<Double> protein,
        Range<Double> carbohydrates,
        Range<Double> fats,
        Range<Integer> rawWeight,

        List<String> goodNames,
        List<String> badNames,

        List<String> mandatoryIngredients,
        List<String> badIngredients,

        List<EatingName> mandatoryEatingNames,

        Integer page,
        Integer size,
        List<SortCriterial> sort
) {
    public FilterSortRequestDishDto {
        page = page != null ? page : 0;
        size = size != null ? size : 20;
        kcal = kcal != null ? kcal : new Range<>(0.0, Double.MAX_VALUE);
        protein = protein != null ? protein : new Range<>(0.0, Double.MAX_VALUE);
        carbohydrates = carbohydrates != null ? carbohydrates : new Range<>(0.0, Double.MAX_VALUE);
        fats = fats != null ? fats : new Range<>(0.0, Double.MAX_VALUE);
        rawWeight = rawWeight != null ? rawWeight : new Range<>(0, Integer.MAX_VALUE);
        goodNames = nullIfEmpty(goodNames);
        badNames = nullIfEmpty(badNames);
        mandatoryIngredients = nullIfEmpty(mandatoryIngredients);
        badIngredients = nullIfEmpty(badIngredients);
        mandatoryEatingNames = nullIfEmpty(mandatoryEatingNames);
    }

    private static <T> List<T> nullIfEmpty(List<T> list) {
        return list == null || list.isEmpty() ? null : list;
    }

    public Long mandatoryIngredientsCount() {
        return mandatoryIngredients == null ? null : (long) mandatoryIngredients.size();
    }

    public Long mandatoryEatingNamesCount() {
        return mandatoryEatingNames == null ? null : (long) mandatoryEatingNames.size();
    }

    public record SortCriterial(
            String field,
            String direction //"ASC" or "DESC"
    ) {}
}