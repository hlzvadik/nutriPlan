package goltsov.nutriPlan.services;

import goltsov.nutriPlan.dto.DishDto;
import goltsov.nutriPlan.entities.Dish;
import goltsov.nutriPlan.entities.Ingredient;
import goltsov.nutriPlan.repositories.DishRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DishService {
    private final IngredientService ingredientService;

    private final DishRepository dishRepository;

    public DishService(IngredientService ingredientService, DishRepository dishRepository) {
        this.ingredientService = ingredientService;
        this.dishRepository = dishRepository;
    }

    public Dish getDishById(Long dishId) {
        return dishRepository.findById(dishId).orElseThrow(() -> new EntityNotFoundException("Not found by id=" + dishId));
    }

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public Dish createDish(Dish dish) {
        return dishRepository.save(dish);
    }

    public Dish updateDish(Long id, Dish dish) {
        Dish oldDish = dishRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found by id="+id));
        dish.setId(id);
        return dishRepository.save(dish);
    }

    public void deleteDishById(Long id) {
        if (!dishRepository.existsById(id)) {
            throw new EntityNotFoundException("Not found by id="+id);
        }
        dishRepository.deleteById(id);
    }

    public Map<Ingredient, Integer> getIngredientsOfDishById(Long id) {
        Dish dish = dishRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found by id="+id));
        return dish.getIngredientWeightMap();
    }

    public void addIngredientsToDishById(Long id, Map<Ingredient, Integer> ingredientWeightMap) {
        Dish dish = dishRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found by id="+id));
        ingredientWeightMap.forEach(dish::addIngredient);
        dishRepository.save(dish);
    }

    public void addIngredientWeightToDishById(Long id, Ingredient ingredient, Integer weight) {
        Dish dish = dishRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found by id="+id));
        dish.addIngredient(ingredient, weight);
        dishRepository.save(dish);
    }

    public void reduceIngredientWeightToDishById(Long id, Ingredient ingredient, Integer weight) {
        Dish dish = dishRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found by id="+id));
        dish.reduceIngredient(ingredient, weight);
        dishRepository.save(dish);
    }

    public void deleteIngredientById(Long id, Ingredient ingredient) {
        Dish dish = dishRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found by id="+id));
        dish.removeIngredient(ingredient);
        dishRepository.save(dish);
    }

    public DishDto dishToDishDto(Dish dish) {
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

    public Dish dishDtoToDish(DishDto dishDto) {
        Map<Ingredient, Integer> ingredientWeightMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry: dishDto.ingredientWeightMap().entrySet()) {
            Ingredient ingredient = ingredientService.getIngredientByName(entry.getKey());
            if (ingredient == null) {
                throw new EntityNotFoundException("No ingredient with name=" + entry.getKey());
            }
            ingredientWeightMap.put(ingredient, entry.getValue());
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
