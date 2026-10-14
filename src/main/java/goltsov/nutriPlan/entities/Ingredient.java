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
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer kcal;
    @Column(nullable = false)
    private Integer protein;
    @Column(nullable = false)
    private Integer carbohydrates;
    @Column(nullable = false)
    private Integer fats;
    @Column(nullable = false)
    private Integer weight = 100;

    public Ingredient(String name, Integer kcal, Integer protein, Integer carbohydrates, Integer fats) {
        this.name = name;
        this.kcal = kcal;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
        this.fats = fats;
    }

    public Ingredient(String name, Integer kcal, Integer protein, Integer carbohydrates, Integer fats, Integer weight) {
        this.name = name;
        this.kcal = kcal;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
        this.fats = fats;
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(name, that.name) && Objects.equals(kcal, that.kcal) && Objects.equals(protein, that.protein) && Objects.equals(carbohydrates, that.carbohydrates) && Objects.equals(fats, that.fats) && Objects.equals(weight, that.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, kcal, protein, carbohydrates, fats, weight);
    }
}
