package goltsov.nutriPlan.dto;

import goltsov.nutriPlan.entities.RoleName;

import java.time.LocalDateTime;
import java.util.Set;

public record RegisterRequest (
        String name,
        String email,
        String password,
        Integer age,
        LocalDateTime createdAt,
        Set<RoleName> roles
) {
}
