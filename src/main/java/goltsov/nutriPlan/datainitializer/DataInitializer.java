package goltsov.nutriPlan.datainitializer;

import goltsov.nutriPlan.baseclasses.Role;
import goltsov.nutriPlan.entities.RoleEntity;
import goltsov.nutriPlan.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.getRoleEntityByRole(Role.USER) == null) {
            roleRepository.save(new RoleEntity(Role.USER));
        }
        if (roleRepository.getRoleEntityByRole(Role.ADMIN) == null) {
            roleRepository.save(new RoleEntity(Role.ADMIN));
        }
    }
}
