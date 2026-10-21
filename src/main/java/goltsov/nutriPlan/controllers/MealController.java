package goltsov.nutriPlan.controllers;

import goltsov.nutriPlan.dto.MealDto;
import goltsov.nutriPlan.entities.Meal;
import goltsov.nutriPlan.entities.RoleName;
import goltsov.nutriPlan.entities.User;
import goltsov.nutriPlan.services.MealService;
import goltsov.nutriPlan.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meal")
public class MealController {

    private final MealService mealService;
    private final UserService userService;

    public MealController(MealService mealService, UserService userService) {
        this.mealService = mealService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<MealDto> addMealToUser(Authentication authentication, @RequestBody MealDto mealDto) {
        if (mealDto.userDto() == null || mealDto.userDto().id() == null) {
            throw new IllegalArgumentException("userDto.id is required");
        }
        User user = userService.getUserById(mealDto.userDto().id());
        checkAccess(user, authentication);
        Meal meal = mealService.addMeal(mealService.mealDtoToMeal(mealDto));
        return ResponseEntity.ok(mealService.mealToMealDto(meal));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMealByMealId(Authentication authentication, @PathVariable Long id) {
        Meal meal = mealService.getMealById(id);
        checkAccess(meal.getUser(), authentication);
        mealService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void checkAccess(User user, Authentication authentication) {
        boolean isOwner = user.getUsername().equals(authentication.getName());
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> RoleName.ROLE_ADMIN.name().equals(authority.getAuthority()));
        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("No authority for user");
        }
    }
}
