package goltsov.nutriPlan.controllers;

import goltsov.nutriPlan.dto.DishDto;
import goltsov.nutriPlan.entities.Dish;
import goltsov.nutriPlan.entities.Ingredient;
import goltsov.nutriPlan.services.DishService;
import goltsov.nutriPlan.services.IngredientService;
import goltsov.nutriPlan.services.MealService;
import goltsov.nutriPlan.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {


    private final MealService mealService;
    private final UserService userService;
    private final DishService dishService;
    private final IngredientService ingredientService;


    public DishController(MealService mealService, UserService userService, DishService dishService, IngredientService ingredientService) {
        this.mealService = mealService;
        this.userService = userService;
        this.dishService = dishService;
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public ResponseEntity<List<DishDto>> getDishes(Authentication authentication, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timeAfter, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timeBefore) {
        List<Dish> dishes = mealService.getDishesByUserAndTime(userService.getUserByEmail(authentication.getName()), timeAfter, timeBefore);
        List<DishDto> dishDtos = new ArrayList<>();
        for (Dish dish: dishes) {
            Map<String, Integer> ingredientWeightMap = new HashMap<>();
            for (Map.Entry<Ingredient, Integer> entry: dish.getIngredientWeightMap().entrySet()) {
                ingredientWeightMap.put(entry.getKey().getName(), entry.getValue());
            }
            dishDtos.add(new DishDto(
                    dish.getId(),
                    dish.getName(),
                    dish.getKcal(),
                    dish.getProtein(),
                    dish.getCarbohydrates(),
                    dish.getFats(),
                    dish.getRawWeight(),
                    ingredientWeightMap
            ));
        }
        return ResponseEntity.ok(dishDtos);
    }

    @PostMapping
    public ResponseEntity<DishDto> newDish(@RequestBody DishDto dishDto) {
        Dish savedDish = dishService.createDish(dishDtoToDish(dishDto));
        return ResponseEntity.ok(dishToDishDto(savedDish));
    }

    private DishDto dishToDishDto(Dish dish) {
        Map<String, Integer> ingredientWeightMap = new HashMap<>();
        for (Map.Entry<Ingredient, Integer> entry: dish.getIngredientWeightMap().entrySet()) {
            ingredientWeightMap.put(entry.getKey().getName(), entry.getValue());
        }
        return new DishDto(
                dish.getId(),
                dish.getName(),
                dish.getKcal(),
                dish.getProtein(),
                dish.getCarbohydrates(),
                dish.getFats(),
                dish.getRawWeight(),
                ingredientWeightMap
        );
    }

    private Dish dishDtoToDish(DishDto dishDto) {
        Map<Ingredient, Integer> ingredientWeightMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry: dishDto.ingredientWeightMap().entrySet()) {
            ingredientWeightMap.put(ingredientService.getIngredientByName(entry.getKey()), entry.getValue());
        }
        return new Dish(
                dishDto.name(),
                dishDto.kcal(),
                dishDto.protein(),
                dishDto.carbohydrates(),
                dishDto.fats(),
                dishDto.rawWeight(),
                ingredientWeightMap
        );
    }
}
