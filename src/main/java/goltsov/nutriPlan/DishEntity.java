package goltsov.nutriPlan;

import jakarta.persistence.*;

@Entity
@Table(name = "dishes")
public class DishEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String name;
    @Column
    private int kcal;
    @Column
    private int protein;
    @Column
    private int carbohydrates;
    @Column
    private int fats;
    @Column(name = "raw_weight")
    private int rawWeight;

    public DishEntity() {
    }

    public DishEntity(Long id, String name, int kcal, int protein, int carbohydrates, int fats, int rawWeight) {
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

    public void setId(Long id) {
        this.id = id;
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

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(int carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public int getFats() {
        return fats;
    }

    public void setFats(int fats) {
        this.fats = fats;
    }

    public int getRawWeight() {
        return rawWeight;
    }

    public void setRawWeight(int rawWeight) {
        this.rawWeight = rawWeight;
    }
}
