package goltsov.nutriPlan.controllers;

import goltsov.nutriPlan.dto.DishDto;
import goltsov.nutriPlan.dto.FilterSortRequestDish;
import goltsov.nutriPlan.entities.Dish;
import goltsov.nutriPlan.services.DishService;
import goltsov.nutriPlan.services.MealService;
import goltsov.nutriPlan.services.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/dish")
public class DishController {
    private final MealService mealService;
    private final UserService userService;
    private final DishService dishService;

    public DishController(MealService mealService, UserService userService, DishService dishService) {
        this.mealService = mealService;
        this.userService = userService;
        this.dishService = dishService;
    }

    @PostMapping
    public ResponseEntity<DishDto> newDish(@RequestBody DishDto dishDto) {
        Dish savedDish = dishService.createDish(dishService.dishDtoToDish(dishDto));
        return ResponseEntity.ok(dishService.dishToDishDto(savedDish));
    }

    @GetMapping("/filters")
    public ResponseEntity<List<DishDto>> getDishesWithFiltersAndSort(@RequestBody FilterSortRequestDish filterSortRequestDish) {

    }
}
