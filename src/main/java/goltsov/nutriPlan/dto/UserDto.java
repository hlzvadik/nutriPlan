package goltsov.nutriPlan.dto;

import goltsov.nutriPlan.entities.RoleName;

import java.util.Set;

public record UserDto (
        Long id,
        String name,
        String email,
        Integer age,
        Set<RoleName>roles
) {
}
