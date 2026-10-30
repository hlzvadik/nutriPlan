package goltsov.nutriPlan.dto;

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
