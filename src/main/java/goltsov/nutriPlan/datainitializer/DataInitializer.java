package goltsov.nutriPlan.datainitializer;

import goltsov.nutriPlan.entities.Role;
import goltsov.nutriPlan.entities.RoleName;
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
        if (roleRepository.getRoleByRoleName(RoleName.ROLE_USER) == null) {
            roleRepository.save(new Role(RoleName.ROLE_USER));
        }
        if (roleRepository.getRoleByRoleName(RoleName.ROLE_ADMIN) == null) {
            roleRepository.save(new Role(RoleName.ROLE_ADMIN));
        }
    }
}
