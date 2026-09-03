package goltsov.nutriPlan;

import java.util.Map;
import java.util.NoSuchElementException;

public class Dish {
    private final Long id;
    private String name;
    private int kcal;
    private int protein;
    private int carbohydrates;
    private int fats;
    private int rawWeight;
    private Map<Ingredient, Integer> ingredientWeightMap;

    public Dish(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Dish(Long id, String name, int kcal, int protein, int carbohydrates, int fats, int rawWeight) {
        this.id = id;
        this.name = name;
        this.kcal = kcal;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
        this.fats = fats;
        this.rawWeight = rawWeight;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKcal() {
        return kcal;
    }

    public int getProtein() {
        return protein;
    }

    public int getCarbohydrates() {
        return carbohydrates;
    }

    public int getFats() {
        return fats;
    }

    public int getRawWeight() {
        return rawWeight;
    }

    public Map<Ingredient, Integer> getIngredientWeightMap() {
        return ingredientWeightMap;
    }

    public void addIngredient(Ingredient ingredient, int weight) {
        int ingredientKcal = weight / ingredient.getWeight() * ingredient.getKcal();
        int ingredientProtein = weight / ingredient.getWeight() * ingredient.getProtein();
        int ingredientCarbohydrates = weight / ingredient.getWeight() * ingredient.getCarbohydrates();
        int ingredientFats = weight / ingredient.getWeight() * ingredient.getFats();
        
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

        int ingredientKcal = weight / ingredient.getWeight() * ingredient.getKcal();
        int ingredientProtein = weight / ingredient.getWeight() * ingredient.getProtein();
        int ingredientCarbohydrates = weight / ingredient.getWeight() * ingredient.getCarbohydrates();
        int ingredientFats = weight / ingredient.getWeight() * ingredient.getFats();

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

        int ingredientKcal = weight / ingredient.getWeight() * ingredient.getKcal();
        int ingredientProtein = weight / ingredient.getWeight() * ingredient.getProtein();
        int ingredientCarbohydrates = weight / ingredient.getWeight() * ingredient.getCarbohydrates();
        int ingredientFats = weight / ingredient.getWeight() * ingredient.getFats();

        ingredientWeightMap.remove(ingredient);

        kcal -= ingredientKcal;
        protein -= ingredientProtein;
        carbohydrates -= ingredientCarbohydrates;
        fats -= ingredientFats;
        rawWeight -= weight;
    }
}
