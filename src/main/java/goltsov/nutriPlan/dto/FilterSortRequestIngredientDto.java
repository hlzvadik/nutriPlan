package goltsov.nutriPlan.dto;

import java.util.List;

public record FilterSortRequestIngredientDto (
        Range<Double> kcal,
        Range<Double> protein,
        Range<Double> carbohydrates,
        Range<Double> fats,
        Range<Integer> weight,

        List<String> goodNames,
        List<String> badNames,

        Integer page,
        Integer size,
        List<FilterSortRequestDishDto.SortCriterial> sort
) {
    public FilterSortRequestIngredientDto {
        page = page != null ? page : 0;
        size = size != null ? size : 20;
        kcal = kcal != null ? kcal : new Range<>(0.0, Double.MAX_VALUE);
        protein = protein != null ? protein : new Range<>(0.0, Double.MAX_VALUE);
        carbohydrates = carbohydrates != null ? carbohydrates : new Range<>(0.0, Double.MAX_VALUE);
        fats = fats != null ? fats : new Range<>(0.0, Double.MAX_VALUE);
        weight = weight != null ? weight : new Range<>(0, Integer.MAX_VALUE);
    }


    public record Range<T extends Number & Comparable<T>>(T min, T max) {
        public Range {
            boolean isInteger = (min instanceof Integer) || (max instanceof Integer);

            if (min == null) {
                min = cast(isInteger ? Integer.valueOf(0) : Double.valueOf(0.0));
            }
            if (max == null) {
                max = cast(isInteger ? Integer.valueOf(Integer.MAX_VALUE)
                        : Double.valueOf(Double.MAX_VALUE));
            }

            if (min.compareTo(max) > 0) {
                throw new IllegalArgumentException(
                        "Range min (%s) must be <= max (%s)".formatted(min, max));
            }
        }
        @SuppressWarnings("unchecked")
        private static <T> T cast(Object o) {
            return (T) o;
        }
    }

    public record SortCriterial(
            String field,
            String direction //"ASC" or "DESC"
    ) {}
}
