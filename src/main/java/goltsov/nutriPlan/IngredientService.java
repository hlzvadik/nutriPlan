package goltsov.nutriPlan;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient getIngredientById(Long ingredientId) {
        IngredientEntity ingredientEntity = ingredientRepository.getById(ingredientId);
        return ingredientEntityToIngredient(ingredientEntity);
    }

    public List<Ingredient> getAllIngredients() {
        List<IngredientEntity> allIngredientEntities = ingredientRepository.findAll();
        return allIngredientEntities.stream().map(
                ingredientEntity -> ingredientEntityToIngredient(ingredientEntity)
        ).toList();
    }

    public Ingredient createIngredient(Ingredient ingredient) {
        if (ingredient.getName() == null) {
            throw new IllegalArgumentException("Name must be not null");
        }
        IngredientEntity ingredientEntity = ingredientToIngredientEntity(ingredient);
        var savedIngredientEntity = ingredientRepository.save(ingredientEntity);
        return ingredientEntityToIngredient(savedIngredientEntity);
    }

    public Ingredient updateIngredient(Long id, Ingredient ingredient) {
        IngredientEntity oldIngredientEntity = ingredientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found by id="+id));
        IngredientEntity newIngredientEntity = ingredientToIngredientEntity(ingredient);
        newIngredientEntity.setId(id);
        ingredientRepository.save(newIngredientEntity);
        return ingredientEntityToIngredient(newIngredientEntity);
    }

    public void deleteIngredientById(Long id) {
        if (!ingredientRepository.existsById(id)) {
            throw new EntityNotFoundException("Not found by id="+id);
        }
        ingredientRepository.deleteById(id);
    }


    private Ingredient ingredientEntityToIngredient(IngredientEntity ingredientEntity) {
        return new Ingredient(
                ingredientEntity.getId(),
                ingredientEntity.getName(),
                ingredientEntity.getKcal(),
                ingredientEntity.getProtein(),
                ingredientEntity.getCarbohydrates(),
                ingredientEntity.getFats(),
                ingredientEntity.getWeight()
        );
    }

    private IngredientEntity ingredientToIngredientEntity(Ingredient ingredient) {
        return new IngredientEntity(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getKcal(),
                ingredient.getProtein(),
                ingredient.getCarbohydrates(),
                ingredient.getFats(),
                ingredient.getWeight()
        );
    }
}
