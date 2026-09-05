package goltsov.nutriPlan.services;

import goltsov.nutriPlan.baseclasses.User;
import goltsov.nutriPlan.entities.UserEntity;
import goltsov.nutriPlan.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long userId) {
        UserEntity userEntity = userRepository.getById(userId);
        return userEntityToUser(userEntity);
    }

    public List<User> getAllUsers() {
        List<UserEntity> allUserEntities = userRepository.findAll();
        return allUserEntities.stream().map(
            userEntity -> userEntityToUser(userEntity)
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
        return userEntityToUser(savedUserEntity);
    }

    public User updateUser(Long id, User user) {
        UserEntity oldUserEntity = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found by id="+id));
        UserEntity newUserEntity = userToUserEntity(user);
        newUserEntity.setId(id);
        userRepository.save(newUserEntity);
        return userEntityToUser(newUserEntity);
    }

    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Not found by id="+id);
        }
        userRepository.deleteById(id);
    }


    private User userEntityToUser(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getAge(),
                userEntity.getCreatedAt()
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
