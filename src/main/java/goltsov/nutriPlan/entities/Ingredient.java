package goltsov.nutriPlan.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ingredient")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private Double kcal;
    @Column(nullable = false)
    private Double protein;
    @Column(nullable = false)
    private Double carbohydrates;
    @Column(nullable = false)
    private Double fats;
    @Column(nullable = false)
    private Integer weight = 100;

    public Ingredient(String name, Double kcal, Double protein, Double carbohydrates, Double fats) {
        this.name = name;
        this.kcal = kcal;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
        this.fats = fats;
    }

    public Ingredient(String name, Double kcal, Double protein, Double carbohydrates, Double fats, Integer weight) {
        this.name = name;
        this.kcal = kcal;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
        this.fats = fats;
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // instanceof + getters (not getClass()/fields) so that Hibernate proxies compare correctly.
        // Name is unique, so it is used as a stable natural key.
        if (!(o instanceof Ingredient that)) return false;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }
}
