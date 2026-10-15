package goltsov.nutriPlan.services;

import goltsov.nutriPlan.entities.Dish;
import goltsov.nutriPlan.entities.Meal;
import goltsov.nutriPlan.entities.Role;
import goltsov.nutriPlan.entities.User;
import goltsov.nutriPlan.repositories.DishRepository;
import goltsov.nutriPlan.repositories.MealRepository;
import goltsov.nutriPlan.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MealService {
    private final MealRepository mealRepository;
    private final UserRepository userRepository;
    private final DishRepository dishRepository;

    public MealService(MealRepository mealRepository, UserRepository userRepository, DishRepository dishRepository) {
        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
        this.dishRepository = dishRepository;
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
}
