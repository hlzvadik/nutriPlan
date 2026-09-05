package goltsov.nutriPlan.entities;

import goltsov.nutriPlan.baseclasses.Eating;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "user_dish")
public class UserDishEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "dish_id")
    private Long dishId;
    @Column(name = "count")
    private Double count;
    @Enumerated(EnumType.STRING)
    @Column(name = "eating")
    private Eating eating;
    @Column(name = "date")
    private LocalDate date = LocalDate.now();

    public UserDishEntity(Long id, Long userId, Long dishId, Double count, Eating eating) {
        this.id = id;
        this.userId = userId;
        this.dishId = dishId;
        this.count = count;
        this.eating = eating;
    }

    public UserDishEntity(Long id, Long userId, Long dishId, Double count, Eating eating, LocalDate date) {
        this.id = id;
        this.userId = userId;
        this.dishId = dishId;
        this.count = count;
        this.eating = eating;
        this.date = date;
    }

    public UserDishEntity(Long userId, Long dishId, Double count, Eating eating) {
        this.userId = userId;
        this.dishId = dishId;
        this.count = count;
        this.eating = eating;
    }

    public UserDishEntity(Long userId, Long dishId, Double count, Eating eating, LocalDate date) {
        this.userId = userId;
        this.dishId = dishId;
        this.count = count;
        this.eating = eating;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public Eating getEating() {
        return eating;
    }

    public void setEating(Eating eating) {
        this.eating = eating;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
