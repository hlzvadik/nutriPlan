package goltsov.nutriPlan.controllers;

import goltsov.nutriPlan.dto.MealDto;
import goltsov.nutriPlan.entities.*;
import goltsov.nutriPlan.services.DishService;
import goltsov.nutriPlan.services.MealService;
import goltsov.nutriPlan.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/meal")
public class MealController {


    private final DishService dishService;
    private final MealService mealService;
    private final UserService userService;


    public MealController(DishService dishService, MealService mealService, UserService userService) {
        this.dishService = dishService;
        this.mealService = mealService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<MealDto> addMealToUser(Authentication authentication, @RequestBody MealDto mealDto) throws AccessDeniedException {
        User user = userService.getUserById(mealDto.id());
        if (!user.getUsername().equals(authentication.getName()) && !authentication.getAuthorities().contains(RoleName.ROLE_ADMIN)) {
            throw new AccessDeniedException("No authority for user");
        }
        Dish dish = dishService.getDishById(mealDto.dishDto().id());
        Meal meal = mealService.addMeal(mealService.mealDtoToMeal(mealDto));
        return ResponseEntity.ok(mealService.mealToMealDto(meal));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMealByMealId(Authentication authentication, @PathVariable Long id) throws AccessDeniedException {
        Meal meal = mealService.getMealById(id);
        User user = meal.getUser();
        if (!user.getUsername().equals(authentication.getName()) && !authentication.getAuthorities().contains(RoleName.ROLE_ADMIN)) {
            throw new AccessDeniedException("No authority for user");
        }
        mealService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
