package goltsov.nutriPlan.datainitializer;

import goltsov.nutriPlan.entities.Eating;
import goltsov.nutriPlan.entities.EatingName;
import goltsov.nutriPlan.entities.Role;
import goltsov.nutriPlan.entities.RoleName;
import goltsov.nutriPlan.repositories.EatingRepository;
import goltsov.nutriPlan.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final EatingRepository eatingRepository;

    public DataInitializer(RoleRepository roleRepository, EatingRepository eatingRepository) {
        this.roleRepository = roleRepository;
        this.eatingRepository = eatingRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        for (RoleName roleName : RoleName.values()) {
            if (roleRepository.getRoleByRoleName(roleName) == null) {
                roleRepository.save(new Role(roleName));
            }
        }
        for (EatingName eatingName : EatingName.values()) {
            if (eatingRepository.findByEatingName(eatingName) == null) {
                Eating eating = new Eating();
                eating.setEatingName(eatingName);
                eatingRepository.save(eating);
            }
        }
    }
}
