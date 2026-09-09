package goltsov.nutriPlan.services;

import goltsov.nutriPlan.baseclasses.Role;
import goltsov.nutriPlan.baseclasses.User;
import goltsov.nutriPlan.entities.UserEntity;
import goltsov.nutriPlan.entities.UserRoleEntity;
import goltsov.nutriPlan.repositories.RoleRepository;
import goltsov.nutriPlan.repositories.UserRepository;
import goltsov.nutriPlan.repositories.UserRoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserRoleService userRoleService;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository, UserRoleService userRoleService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.userRoleService = userRoleService;
        this.roleRepository = roleRepository;
    }

    public User getUserById(Long userId) {
        UserEntity userEntity = userRepository.getById(userId);
        List<UserRoleEntity> userRoleEntities = userRoleRepository.findAllByUserId(userId);
        return userEntityToUser(userEntity, userRoleEntities);
    }

    public User getUserByEmail(String email) {
        UserEntity userEntity = userRepository.getByEmail(email);
        List<UserRoleEntity> userRoleEntities = userRoleRepository.findAllByUserId(userEntity.getId());
        return userEntityToUser(userEntity, userRoleEntities);
    }

    public List<User> getAllUsers() {
        List<UserEntity> allUserEntities = userRepository.findAll();
        Map<Long, List<UserRoleEntity>> allUserRoleEntities = new HashMap<>();
        for (UserEntity userEntity: allUserEntities) {
            allUserRoleEntities.put(userEntity.getId(), userRoleRepository.findAllByUserId(userEntity.getId()));
        }
        return allUserEntities.stream().map(
            userEntity -> userEntityToUser(userEntity, allUserRoleEntities.get(userEntity.getId()))
        ).toList();
    }

    public User createUser(User user) {
        if (user.getEmail() == null) {
            throw new IllegalArgumentException("Email must be not null");
        }
        if (user.getPassword() == null) {
            throw new IllegalArgumentException("Password must be not null");
        }
        UserEntity userEntity = userToUserEntity(user);
        var savedUserEntity = userRepository.save(userEntity);
        for (Role role: user.getRoles()) {
            userRoleService.add(user.getId(), roleRepository.getRoleEntityByRole(role).getId());
        }
        var savedUserRoleEntities = userRoleRepository.findAllByUserId(user.getId());
        return userEntityToUser(savedUserEntity, savedUserRoleEntities);
    }

    public User updateUser(Long id, User user) {
        UserEntity oldUserEntity = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found by id="+id));
        userRepository.delete(oldUserEntity);
        UserEntity newUserEntity = userToUserEntity(user);
        newUserEntity.setId(id);
        userRepository.save(newUserEntity);
        userRoleService.deleteUser(id);
        userRoleService.addUser(user.getId(), user.getRoles());
        return userEntityToUser(newUserEntity, userRoleRepository.findAllByUserId(user.getId()));
    }

    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Not found by id="+id);
        }
        userRepository.deleteById(id);
        userRoleService.deleteUser(id);
    }


    private User userEntityToUser(UserEntity userEntity, List<UserRoleEntity> userRoleEntities) {
        return new User(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getAge(),
                userEntity.getCreatedAt(),
                userRoleEntities.stream().map(userRoleEntity -> roleRepository.getById(userRoleEntity.getRoleId()).getRole()).toList()
        );
    }

    private UserEntity userToUserEntity(User user) {
        return new UserEntity(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getAge(),
                user.getCreatedAt()
        );
    }
}
