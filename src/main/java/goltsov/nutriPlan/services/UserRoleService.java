package goltsov.nutriPlan.services;

import goltsov.nutriPlan.baseclasses.Role;
import goltsov.nutriPlan.entities.UserRoleEntity;
import goltsov.nutriPlan.repositories.RoleRepository;
import goltsov.nutriPlan.repositories.UserRoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository, RoleRepository roleRepository) {
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    public UserRoleEntity add(Long userId, Long roleId) {
        List<UserRoleEntity> entities = userRoleRepository.findAllByUserIdAndRoleId(userId, roleId);
        if (entities.isEmpty()) {
            UserRoleEntity newEntity = new UserRoleEntity(userId, roleId);
            userRoleRepository.save(newEntity);
            return newEntity;
        } else {
            return entities.getFirst();
        }
    }

    public List<UserRoleEntity> addUser(Long userId, List<Role> roles) {
        for (Role role: roles) {
            Long roleId = roleRepository.getRoleEntityByRole(role).getId();
            if (userRoleRepository.findAllByUserIdAndRoleId(userId, roleId).isEmpty()) {
                userRoleRepository.save(new UserRoleEntity(userId, roleId));
            }
        }
        return userRoleRepository.findAllByUserId(userId);
    }

    public void delete(Long userId, Long roleId) {
        List<UserRoleEntity> entities = userRoleRepository.findAllByUserIdAndRoleId(userId, roleId);
        if (entities.isEmpty()) {
            throw new EntityNotFoundException("No entity with userId="+userId+", roleId="+roleId);
        } else {
            userRoleRepository.delete(entities.getFirst());
        }
    }

    public void deleteUser(Long userId) {
        List<UserRoleEntity> entities = userRoleRepository.findAllByUserId(userId);
        if (entities.isEmpty()) {
            throw new EntityNotFoundException("No entity with userId="+userId);
        } else {
            userRoleRepository.deleteAll(entities);
        }
    }
}
