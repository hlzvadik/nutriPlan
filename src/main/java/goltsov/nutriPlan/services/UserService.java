package goltsov.nutriPlan.services;

import goltsov.nutriPlan.entities.*;
import goltsov.nutriPlan.dto.UserDto;
import goltsov.nutriPlan.repositories.RoleRepository;
import goltsov.nutriPlan.repositories.UserRepository;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.getReferenceById(id);
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(@NonNull String email) {
        return userRepository.getByEmail(email);
    }

    public UserDto getUserDtoByEmail(String email) {
        User user = getUserByEmail(email);
        Set<RoleName> roleNames = new HashSet<>();
        for (Role role: user.getRoles()) {
            roleNames.add(role.getRoleName());
        }
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                roleNames
        );
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(Long id, User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User oldUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found by id="+id));
        return userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Not found by id="+id);
        }
        userRepository.deleteById(id);
    }

    public User addRoleById(Long id, Role role) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No user with id="+id));
        user.addRole(role);
        return userRepository.save(user);
    }

    public User removeRoleById(Long id, Role role) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No user with id="+id));
        user.removeRole(role);
        return userRepository.save(user);
    }

    public User addMealById(Long id, Dish dish, Eating eating, Double portionShare) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No user with id="+id));
        user.addDish(dish, eating, portionShare);
        return userRepository.save(user);
    }



    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return getUserByEmail(username);
    }
}
