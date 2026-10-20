package goltsov.nutriPlan.repositories;

import goltsov.nutriPlan.entities.Eating;
import goltsov.nutriPlan.entities.EatingName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EatingRepository extends JpaRepository<Eating, Long> {
    Eating findByEatingName(EatingName eatingName);
}
