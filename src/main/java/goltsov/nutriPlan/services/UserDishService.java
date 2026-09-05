package goltsov.nutriPlan.services;

import goltsov.nutriPlan.baseclasses.Eating;
import goltsov.nutriPlan.entities.UserDishEntity;
import goltsov.nutriPlan.repositories.UserDishRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserDishService {
    private final UserDishRepository userDishRepository;

    public UserDishService(UserDishRepository userDishRepository) {
        this.userDishRepository = userDishRepository;
    }

    public UserDishEntity add(Long userId, Long dishId, Long count, Eating eating, LocalDate date) {
        List<UserDishEntity> entities = userDishRepository.findAllByUserIdAndDishIdAndEatingAndDate(userId, dishId, eating, date);
        if (!entities.isEmpty()) {
            entities.getFirst().setCount(entities.getFirst().getCount() + count);
        }
        return userDishRepository.save(entities.getFirst());
    }

    public void delete(Long userId, Long dishId) {
        if (userDishRepository.deleteAllByUserIdAndDishId(userId, dishId) == 0) {
            throw new EntityNotFoundException("No entity with userId="+userId+", dishId="+dishId);
        }
    }

    public UserDishEntity reduceWeight(Long userId, Long dishId, Long count) {
        List<UserDishEntity> entities = userDishRepository.findAllByUserIdAndDishId(userId, dishId);
        if (entities.isEmpty()) {
            throw new EntityNotFoundException("No entity with userId="+userId+", dishId="+dishId);
        }
        if (entities.getFirst().getCount() < count) {
            throw new IllegalArgumentException("Count should not be greater than "+entities.getFirst().getCount());
        }
        entities.getFirst().setCount(entities.getFirst().getCount() - count);
        userDishRepository.save(entities.getFirst());
        return entities.getFirst();
    }


}
