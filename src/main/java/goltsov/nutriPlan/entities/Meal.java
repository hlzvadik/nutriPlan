package goltsov.nutriPlan.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "meals")
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eating_id", nullable = false)
    private Eating eating;

    @Column(name = "portion_share", nullable = false)
    private Double portionShare;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;

    public Meal(User user, Dish dish, Eating eating, Double portionShare, LocalDateTime time) {
        this.user = user;
        this.dish = dish;
        this.eating = eating;
        this.portionShare = portionShare;
        this.time = time;
    }
}
