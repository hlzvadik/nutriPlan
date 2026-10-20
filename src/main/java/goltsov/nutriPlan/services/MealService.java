package goltsov.nutriPlan.services;

import goltsov.nutriPlan.dto.MealDto;
import goltsov.nutriPlan.entities.Dish;
import goltsov.nutriPlan.entities.Meal;
import goltsov.nutriPlan.entities.Role;
import goltsov.nutriPlan.entities.User;
import goltsov.nutriPlan.repositories.DishRepository;
import goltsov.nutriPlan.repositories.EatingRepository;
import goltsov.nutriPlan.repositories.MealRepository;
import goltsov.nutriPlan.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MealService {
    private final MealRepository mealRepository;
    private final UserRepository userRepository;
    private final DishRepository dishRepository;
    private final UserService userService;
    private final DishService dishService;
    private final EatingRepository eatingRepository;

    public MealService(MealRepository mealRepository, UserRepository userRepository, DishRepository dishRepository, UserService userService, DishService dishService, EatingRepository eatingRepository) {
        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
        this.dishRepository = dishRepository;
        this.userService = userService;
        this.dishService = dishService;
        this.eatingRepository = eatingRepository;
    }

    public Meal getMealById(Long id) {
        return mealRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No entity with id="+id.toString()));
    }

    public void deleteById(Long id) {
        mealRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Now meal with id="+id));
        mealRepository.deleteById(id);
    }

    public void reducePortionShareByIds(Long id, Double portitionShare) {
        Meal meal = mealRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Now meal with id="+id));

        if (meal.getPortionShare() < portitionShare) {
            throw new IllegalArgumentException("Portion share must be lower then "+meal.getPortionShare());
        }
        meal.setPortionShare(meal.getPortionShare() - portitionShare);
    }

    public List<Dish> getDishesByUserAndTime(User user, LocalDateTime timeAfter, LocalDateTime timeBefore) {
        List<Meal> meals = mealRepository.findAllByUserAndTimeBetweenWithDetails(user, timeAfter, timeBefore);
        List<Dish> dishes = new ArrayList<>();
        for (Meal meal: meals) {
            meal.getDish().getIngredientWeightMap();
            dishes.add(meal.getDish());
        }
        return dishes;
    }

    public Meal addMeal(Meal meal) {
        return mealRepository.save(meal);
    }

    public MealDto mealToMealDto(Meal meal) {
        return new MealDto(
                meal.getId(),
                userService.userToUserDto(meal.getUser()),
                dishService.dishToDishDto(meal.getDish()),
                meal.getEating().getEatingName(),
                meal.getPortionShare(),
                meal.getTime()
        );
    }

    public Meal mealDtoToMeal(MealDto mealDto) {
        return new Meal(
                mealDto.id(),
                userService.getUserById(mealDto.userDto().id()),
                dishService.dishDtoToDish(mealDto.dishDto()),
                eatingRepository.findByEatingName(mealDto.eatingName()),
                mealDto.partitionShare(),
                mealDto.time()
        );
    }
}
