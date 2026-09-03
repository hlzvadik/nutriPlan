package goltsov.nutriPlan;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public class DishService {
    private final DishRepository dishRepository;

    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public Dish getDishById(Long dishId) {
        DishEntity dishEntity = dishRepository.getById(dishId);
        return dishEntityToDish(dishEntity);
    }

    public List<Dish> getAllDishes() {
        List<DishEntity> allDishEntities = dishRepository.findAll();
        return allDishEntities.stream().map(
                dishEntity -> dishEntityToDish(dishEntity)
        ).toList();
    }

    public Dish createDish(Dish dish) {
        if (dish.getName() == null) {
            throw new IllegalArgumentException("Name must be not null");
        }
        DishEntity dishEntity = dishToDishEntity(dish);
        var savedDishEntity = dishRepository.save(dishEntity);
        return dishEntityToDish(savedDishEntity);
    }

    public Dish updateDish(Long id, Dish dish) {
        DishEntity oldDishEntity = dishRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found by id="+id));
        DishEntity newDishEntity = dishToDishEntity(dish);
        newDishEntity.setId(id);
        dishRepository.save(newDishEntity);
        return dishEntityToDish(newDishEntity);
    }

    public void deleteDishById(Long id) {
        if (!dishRepository.existsById(id)) {
            throw new EntityNotFoundException("Not found by id="+id);
        }
        dishRepository.deleteById(id);
    }


    private Dish dishEntityToDish(DishEntity dishEntity) {
        return new Dish(
                dishEntity.getId(),
                dishEntity.getName(),
                dishEntity.getKcal(),
                dishEntity.getProtein(),
                dishEntity.getCarbohydrates(),
                dishEntity.getFats(),
                dishEntity.getRawWeight()
        );
    }

    private DishEntity dishToDishEntity(Dish dish) {
        return new DishEntity(
                dish.getId(),
                dish.getName(),
                dish.getKcal(),
                dish.getProtein(),
                dish.getCarbohydrates(),
                dish.getFats(),
                dish.getRawWeight()
        );
    }
}
