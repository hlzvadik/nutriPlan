package goltsov.nutriPlan.services;

import goltsov.nutriPlan.entities.Recipe;
import goltsov.nutriPlan.repositories.RecipeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe getById(Long id) {
        return recipeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No recipe with id=" + id));
    }
}
