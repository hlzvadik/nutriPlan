package goltsov.nutriPlan.controllers;

import goltsov.nutriPlan.entities.Ingredient;
import goltsov.nutriPlan.entities.RoleName;
import goltsov.nutriPlan.services.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ingredient")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping
    public ResponseEntity<Ingredient> newIngredient(Authentication authentication, @RequestBody Ingredient ingredient) {
        checkAccess(authentication);
        return ResponseEntity.ok(ingredientService.createIngredient(ingredient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredientById(Authentication authentication, @PathVariable Long id) {
        checkAccess(authentication);
        ingredientService.deleteIngredientById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private void checkAccess(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> RoleName.ROLE_ADMIN.name().equals(authority.getAuthority()));
        if (!isAdmin) {
            throw new AccessDeniedException("No authority for user");
        }
    }

}
