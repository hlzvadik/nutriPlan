package goltsov.nutriPlan.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "dishes")
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int kcal;
    @Column(nullable = false)
    private int protein;
    @Column(nullable = false)
    private int carbohydrates;
    @Column(nullable = false)
    private int fats;
    @Column(nullable = false)
    private int rawWeight;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "dishes_ingredients", joinColumns = @JoinColumn(name = "dish_id"))
    @MapKeyJoinColumn(name = "ingredient_id")
    @Column(name = "weight", nullable = false)
    private Map<Ingredient, Integer> ingredientWeightMap = new HashMap<>();


    public Dish(String name, int kcal, int protein, int carbohydrates, int fats, int rawWeight, Map<Ingredient, Integer> ingredientWeightMap) {
        this.name = name;
        this.kcal = kcal;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
        this.fats = fats;
        this.rawWeight = rawWeight;
        this.ingredientWeightMap = new HashMap<>(ingredientWeightMap);
    }

    public Dish(String name, int kcal, int protein, int carbohydrates, int fats, int rawWeight) {
        this.name = name;
        this.kcal = kcal;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
        this.fats = fats;
        this.rawWeight = rawWeight;
    }

    public void addIngredient(Ingredient ingredient, int weight) {
        int ingredientKcal = (weight * ingredient.getKcal()) / ingredient.getWeight();
        int ingredientProtein = (weight * ingredient.getProtein()) / ingredient.getWeight();
        int ingredientCarbohydrates = (weight * ingredient.getCarbohydrates()) / ingredient.getWeight();
        int ingredientFats = (weight * ingredient.getFats()) / ingredient.getWeight();
        
        if (ingredientWeightMap.containsKey(ingredient)) {
            ingredientWeightMap.put(ingredient, ingredientWeightMap.get(ingredient) + weight);
        }
        else {
            ingredientWeightMap.put(ingredient, weight);
        }
        
        kcal += ingredientKcal;
        protein += ingredientProtein;
        carbohydrates += ingredientCarbohydrates;
        fats += ingredientFats;
        rawWeight += weight;
    }

    public void reduceIngredient(Ingredient ingredient, int weight) {
        if (!ingredientWeightMap.containsKey(ingredient)) {
            throw new NoSuchElementException("No such ingredient");
        }
        if (weight > ingredientWeightMap.get(ingredient)) {
            throw new IllegalArgumentException("Weight must be less than in the dish");
        }

        int ingredientKcal = (weight * ingredient.getKcal()) / ingredient.getWeight();
        int ingredientProtein = (weight * ingredient.getProtein()) / ingredient.getWeight();
        int ingredientCarbohydrates = (weight * ingredient.getCarbohydrates()) / ingredient.getWeight();
        int ingredientFats = (weight * ingredient.getFats()) / ingredient.getWeight();

        if (weight == ingredientWeightMap.get(ingredient)) {
            ingredientWeightMap.remove(ingredient);
        } else {
            ingredientWeightMap.put(ingredient, ingredientWeightMap.get(ingredient) - weight);
        }

        kcal -= ingredientKcal;
        protein -= ingredientProtein;
        carbohydrates -= ingredientCarbohydrates;
        fats -= ingredientFats;
        rawWeight -= weight;
    }

    public void removeIngredient(Ingredient ingredient) {
        if (!ingredientWeightMap.containsKey(ingredient)) {
            throw new NoSuchElementException("No such ingredient");
        }
        int weight = ingredientWeightMap.get(ingredient);

        int ingredientKcal = (weight  * ingredient.getKcal()) / ingredient.getWeight();
        int ingredientProtein = (weight * ingredient.getProtein()) / ingredient.getWeight();
        int ingredientCarbohydrates = (weight * ingredient.getCarbohydrates()) / ingredient.getWeight();
        int ingredientFats = (weight * ingredient.getFats()) / ingredient.getWeight();

        ingredientWeightMap.remove(ingredient);

        kcal -= ingredientKcal;
        protein -= ingredientProtein;
        carbohydrates -= ingredientCarbohydrates;
        fats -= ingredientFats;
        rawWeight -= weight;
    }
}
