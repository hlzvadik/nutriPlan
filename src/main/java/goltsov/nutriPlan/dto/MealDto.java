package goltsov.nutriPlan.dto;

import goltsov.nutriPlan.entities.EatingName;

import java.time.LocalDateTime;

public record MealDto (
        Long id,
        UserDto userDto,
        DishDto dishDto,
        EatingName eatingName,
        Double partitionShare,
        LocalDateTime time
) {}
