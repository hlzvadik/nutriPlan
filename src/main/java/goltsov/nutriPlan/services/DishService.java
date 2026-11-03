package goltsov.nutriPlan.services;

import goltsov.nutriPlan.dto.DishDto;
import goltsov.nutriPlan.dto.FilterSortRequestDishDto;
import goltsov.nutriPlan.entities.Dish;
import goltsov.nutriPlan.entities.Eating;
import goltsov.nutriPlan.entities.EatingName;
import goltsov.nutriPlan.entities.Ingredient;
import goltsov.nutriPlan.repositories.DishRepository;
import goltsov.nutriPlan.repositories.EatingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DishService {
    private final IngredientService ingredientService;

    private final DishRepository dishRepository;
    private final EatingRepository eatingRepository;

    public DishService(IngredientService ingredientService, DishRepository dishRepository, EatingRepository eatingRepository) {
        this.ingredientService = ingredientService;
        this.dishRepository = dishRepository;
        this.eatingRepository = eatingRepository;
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

    public Page<Dish> getFilteredDishes(FilterSortRequestDishDto dto) {
        List<Sort.Order> orders = dto.sort() == null ? List.of() : dto.sort().stream()
                .map(s -> new Sort.Order(
                        "DESC".equalsIgnoreCase(s.direction()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                        s.field()
                ))
                .toList();

        Sort sort = orders.isEmpty() ? Sort.unsorted() : Sort.by(orders);
        PageRequest pageable = PageRequest.of(dto.page(), dto.size(), sort);
        Double avgKcal = dto.kcal().max() >= Double.MAX_VALUE ? null : (dto.kcal().min() + dto.kcal().max()) / 2;
        Page<Dish> result = dishRepository.getWithFilterAndSort(dto, avgKcal, pageable);
        if (avgKcal == null) {
            return result;
        }
        return result.map(dish -> scaleToKcal(dish, avgKcal));
    }

    private Dish scaleToKcal(Dish dish, double targetKcal) {
        double k = targetKcal / dish.getKcal();
        Map<Ingredient, Integer> scaledIngredients = new HashMap<>();
        dish.getIngredientWeightMap().forEach((ingredient, weight) -> scaledIngredients.put(ingredient, (int) Math.round(weight * k)));
        Dish copy = new Dish(
                dish.getName(),
                targetKcal,
                dish.getProtein() * k,
                dish.getCarbohydrates() * k,
                dish.getFats() * k,
                (int) Math.round(dish.getRawWeight() * k),
                dish.getCountLikes() != null ? dish.getCountLikes() : 0,
                new HashSet<>(dish.getEatings()),
                scaledIngredients
        );
        copy.setId(dish.getId());
        copy.setRecipe(dish.getRecipe());
        return copy;
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
                dish.getRecipe() != null ? dish.getRecipe().getId() : 0L,
                dish.getCountLikes() != null ? dish.getCountLikes() : 0,
                dish.getEatings().stream().map((eating) -> eating.getEatingName().toString()).collect(Collectors.toSet()),
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
        Set<Eating> eatings = new HashSet<>();
        for (String eating: dishDto.eatings()) {
            Eating eating1 = eatingRepository.findByEatingName(EatingName.valueOf(eating));
            eatings.add(eating1);
        }
        return new Dish(
                dishDto.name(),
                dishDto.kcal(),
                dishDto.protein(),
                dishDto.carbohydrates(),
                dishDto.fats(),
                dishDto.rawWeight(),
                dishDto.countLikes(),
                eatings,
                ingredientWeightMap
        );
    }
}
