package goltsov.nutriPlan.controllers;

import goltsov.nutriPlan.dto.DishDto;
import goltsov.nutriPlan.entities.Dish;
import goltsov.nutriPlan.entities.RoleName;
import goltsov.nutriPlan.services.DishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dish")
public class DishController {
    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDishById(Authentication authentication, @PathVariable Long id) {
        checkAccess(authentication);
        dishService.deleteDishById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<DishDto> newDish(Authentication authentication, @RequestBody DishDto dishDto) {
        checkAccess(authentication);
        Dish savedDish = dishService.createDish(dishService.dishDtoToDish(dishDto));
        return ResponseEntity.ok(dishService.dishToDishDto(savedDish));
    }

    private void checkAccess(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> RoleName.ROLE_ADMIN.name().equals(authority.getAuthority()));
        if (!isAdmin) {
            throw new AccessDeniedException("No authority for user");
        }
    }
}
