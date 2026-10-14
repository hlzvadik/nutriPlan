package goltsov.nutriPlan.repositories;

import goltsov.nutriPlan.entities.Role;
import goltsov.nutriPlan.entities.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    public Role getRoleByRoleName(RoleName roleName);
}
