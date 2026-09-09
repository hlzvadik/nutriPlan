package goltsov.nutriPlan.repositories;

import goltsov.nutriPlan.baseclasses.Role;
import goltsov.nutriPlan.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    public RoleEntity getRoleEntityByRole(Role role);
}
