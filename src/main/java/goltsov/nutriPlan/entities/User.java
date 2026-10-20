package goltsov.nutriPlan.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    private Integer age;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Meal> meals = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "inventory", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyJoinColumn(name = "ingredient_id")
    @Column(name = "weight", nullable = false)
    private Map<Ingredient, Integer> inventory = new HashMap<>();

    public User(Long id, String name, String email, String password, Integer age, LocalDateTime createdAt, Set<Role> roles, List<Meal> meals, Map<Ingredient, Integer> inventory) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.createdAt = createdAt;
        this.roles = roles;
        this.meals = meals;
        this.inventory = inventory;
    }

    public User(String name, String email, String password, Integer age, LocalDateTime createdAt, Set<Role> roles, List<Meal> meals, Map<Ingredient, Integer> inventory) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.createdAt = createdAt;
        this.roles = roles;
        this.meals = meals;
        this.inventory = inventory;
    }

    public User(String name, String email, String password, Integer age, LocalDateTime createdAt, Set<Role> roles, List<Meal> meals) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.createdAt = createdAt;
        this.roles = roles;
        this.meals = meals;
    }

    public User(String name, String email, String password, Integer age, LocalDateTime createdAt, Set<Role> roles) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.createdAt = createdAt;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role: roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName().toString()));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    public void addDish(Dish dish, Eating eating, double portionShare, LocalDateTime time) {
        Meal meal = new Meal(this, dish, eating, portionShare, time);
        this.meals.add(meal);
    }

    public void addRole(Role role) {
        if (this.roles.contains(role)) {
            throw new IllegalArgumentException("Role "+role.getRoleName()+" already exists for user "+getUsername());
        }
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        if (!this.roles.contains(role)) {
            throw new IllegalArgumentException("Role "+role.getRoleName()+" no for user "+getUsername());
        }
        this.roles.remove(role);
    }
}
