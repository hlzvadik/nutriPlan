package goltsov.nutriPlan;

import jakarta.persistence.criteria.CriteriaBuilder;

public class Ingredient {
    private final Long id;
    private String name;
    private Integer kcal;
    private Integer protein;
    private Integer carbohydrates;
    private Integer fats;
    private Integer weight;

    public Ingredient(Long id, String name, Integer kcal, Integer protein, Integer carbohydrates, Integer fats) {
        this.id = id;
        this.name = name;
        this.kcal = kcal;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
        this.fats = fats;
        this.weight = 100;
    }

    public Ingredient(Long id, String name, Integer kcal, Integer protein, Integer carbohydrates, Integer fats, Integer weight) {
        this.id = id;
        this.name = name;
        this.kcal = kcal;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
        this.fats = fats;
        this.weight = weight;
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

    public Integer getKcal() {
        return kcal;
    }

    public void setKcal(Integer kcal) {
        this.kcal = kcal;
    }

    public Integer getProtein() {
        return protein;
    }

    public void setProtein(Integer protein) {
        this.protein = protein;
    }

    public Integer getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(Integer carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public Integer getFats() {
        return fats;
    }

    public void setFats(Integer fats) {
        this.fats = fats;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
