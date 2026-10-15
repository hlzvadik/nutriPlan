package goltsov.nutriPlan.dto;

import goltsov.nutriPlan.entities.Eating;

import java.time.LocalDateTime;

public record MealDto (
        Long id,
        UserDto userDto,
        DishDto dishDto,
        Eating eating,
        Double partitionShare,
        LocalDateTime time
) {
}
