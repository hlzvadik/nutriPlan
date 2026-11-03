package goltsov.nutriPlan.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "recipes")
@NoArgsConstructor
@Getter
@Setter
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "dish_id")
    private Dish dish;

    @Column(columnDefinition = "TEXT")
    private String recipe;

    public Recipe(Dish dish, String recipe) {
        this.dish = dish;
        this.recipe = recipe;
    }
}
