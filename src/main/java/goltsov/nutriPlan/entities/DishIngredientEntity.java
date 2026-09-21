package goltsov.nutriPlan.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "dish_ingredient")
public class DishIngredientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "dish_id")
    private Long dishId;
    @Column(name = "ingredient_id")
    private Long ingredientId;
    @Column(name = "weight_of_ingredient")
    private Integer weight;

    public DishIngredientEntity(Long id, Long dishId, Long ingredientId, Integer weight) {
        this.id = id;
        this.dishId = dishId;
        this.ingredientId = ingredientId;
        this.weight = weight;
    }

    public DishIngredientEntity(Long dishId, Long ingredientId, Integer weight) {
        this.dishId = dishId;
        this.ingredientId = ingredientId;
        this.weight = weight;
    }

    public DishIngredientEntity() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
