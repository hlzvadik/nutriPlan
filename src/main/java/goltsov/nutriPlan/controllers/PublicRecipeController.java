package goltsov.nutriPlan.controllers;

import goltsov.nutriPlan.services.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/recipe")
public class PublicRecipeController {

    private final RecipeService recipeService;

    public PublicRecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getRecipeById(@PathVariable Long id) {
        return ResponseEntity.ok(recipeService.getById(id).getRecipe());
    }
}
