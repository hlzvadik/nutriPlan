package goltsov.nutriPlan.controllers;

import goltsov.nutriPlan.dto.FilterSortRequestIngredientDto;
import goltsov.nutriPlan.entities.Ingredient;
import goltsov.nutriPlan.services.IngredientService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/ingredient")
public class PublicIngredientController {

    private final IngredientService ingredientService;

    public PublicIngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("/{name}")
    public ResponseEntity<Ingredient> getByName(@PathVariable String name) {
        Ingredient ingredient = ingredientService.getIngredientByName(name);
        if (ingredient == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ingredient);
    }

    @RequestMapping(value = "/filters", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Page<Ingredient>> getIngredientsWithFiltersAndSort(@RequestBody FilterSortRequestIngredientDto filterSortRequestIngredientDto) {
        return ResponseEntity.ok(ingredientService.getFilteredIngredients(filterSortRequestIngredientDto));
    }
}
