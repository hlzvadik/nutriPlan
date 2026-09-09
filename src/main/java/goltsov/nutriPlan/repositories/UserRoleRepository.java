package goltsov.nutriPlan.repositories;

import goltsov.nutriPlan.entities.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
    public List<UserRoleEntity> findAllByUserIdAndRoleId(Long userId, Long roleId);
    public List<UserRoleEntity> findAllByUserId(Long userId);
}
