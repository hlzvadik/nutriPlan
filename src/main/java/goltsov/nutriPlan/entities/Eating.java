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
@Table(name = "eatings")
public class Eating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "eating_name", nullable = false, unique = true)
    private EatingName eatingName;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Eating eating)) return false;
        return getEatingName() == eating.getEatingName();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getEatingName());
    }
}
