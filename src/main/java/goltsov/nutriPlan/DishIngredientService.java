package goltsov.nutriPlan;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DishIngredientService {
    private final DishIngredientRepository dishIngredientRepository;
    private final IngredientRepository ingredientRepository;

    public DishIngredientService(DishIngredientRepository dishIngredientRepository, IngredientRepository ingredientRepository) {
        this.dishIngredientRepository = dishIngredientRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public Map<Ingredient, Long> getIngredientsOfDishById(Long dishId) {
        List<DishIngredientEntity> ingredients = dishIngredientRepository.findAllByDishId(dishId);
        Map<Ingredient, Long> ingredientWeightMap = new HashMap<>();
        for (int i = 0; i < ingredients.size(); ++i) {
            Long weight = ingredients.get(i).getWeight();
            IngredientEntity ingredientEntity = ingredientRepository.getById(ingredients.get(i).getId());
            Ingredient ingredient = new Ingredient(
                    ingredientEntity.getId(),
                    ingredientEntity.getName(),
                    ingredientEntity.getKcal(),
                    ingredientEntity.getProtein(),
                    ingredientEntity.getCarbohydrates(),
                    ingredientEntity.getFats(),
                    ingredientEntity.getWeight()
            );
            if (ingredientWeightMap.containsKey(ingredient)) {
                ingredientWeightMap.put(ingredient, ingredientWeightMap.get(ingredient) + weight);
            } else {
                ingredientWeightMap.put(ingredient, weight);
            }
        }
        return ingredientWeightMap;
    }

    public void addDish(Long dishId, Map<Ingredient, Long> ingredientWeightMap) {
        if (!dishIngredientRepository.findAllByDishId(dishId).isEmpty()) {
            throw new IllegalArgumentException("Dish allready in table");
        }
        for (Map.Entry<Ingredient, Long> i : ingredientWeightMap.entrySet()) {
            dishIngredientRepository.save(new DishIngredientEntity(dishId, i.getKey().getId(), i.getValue()));
        }
    }

    public void deleteDishById(Long dishId) {
        if (dishIngredientRepository.deleteAllByDishId(dishId).equals(0L)) {
            throw new NoSuchElementException("Not found dish with id=" + dishId);
        }
    }

    public void addIngredientWeightByDishIdIngredientIdWeight(Long dishId, Long ingredientId, Long weight) {
        List<DishIngredientEntity> dishIngredientEntities = dishIngredientRepository.findAllByDishIdAndIngredientId(dishId, ingredientId);
        if (!dishIngredientEntities.isEmpty()) {
            Long newWeight = dishIngredientEntities.getFirst().getWeight() + weight;
            dishIngredientEntities.getFirst().setWeight(newWeight);
            dishIngredientRepository.save(dishIngredientEntities.getFirst());
        } else {
            DishIngredientEntity dishIngredientEntity = new DishIngredientEntity(dishId, ingredientId, weight);
            dishIngredientRepository.save(dishIngredientEntity);
        }
    }

    public void reduceIngredientWeightByDishIdIngredientIdWeight(Long dishId, Long ingredientId, Long weight) {
        List<DishIngredientEntity> dishIngredientEntities = dishIngredientRepository.findAllByDishIdAndIngredientId(dishId, ingredientId);
        if (dishIngredientEntities.isEmpty()) {
            throw new NoSuchElementException("Not found entity with dishId="+dishId+", ingredientId="+ingredientId);
        }
        if (dishIngredientEntities.getFirst().getWeight().equals(weight)) {
            dishIngredientRepository.deleteAllByDishIdAndIngredientId(dishId, ingredientId);
        } else if (dishIngredientEntities.getFirst().getWeight() > weight) {
            throw new IllegalArgumentException("Weight should not be greater than " + dishIngredientEntities.getFirst().getWeight());
        } else {
            Long newWeight = dishIngredientEntities.getFirst().getWeight() - weight;
            dishIngredientRepository.save(new DishIngredientEntity(dishId, ingredientId, newWeight));
        }
    }

    public void deleteIngredientByIngredientIdAndDishId(Long dishId, Long ingredientId) {
        if (dishIngredientRepository.deleteAllByDishIdAndIngredientId(dishId, ingredientId).equals(0L)) {
            throw new NoSuchElementException("Not found entity with dishId="+dishId+", ingredientId="+ingredientId);
        }
    }
}
