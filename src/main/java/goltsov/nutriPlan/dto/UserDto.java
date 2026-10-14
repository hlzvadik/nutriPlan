package goltsov.nutriPlan.dto;

import goltsov.nutriPlan.entities.RoleName;

import java.util.List;

public record UserDto (
        Long id,
        String name,
        String email,
        Integer age,
        List<RoleName>roles
) {
}
