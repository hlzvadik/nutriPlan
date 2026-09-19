package goltsov.nutriPlan.dto;

import goltsov.nutriPlan.baseclasses.Role;

import java.time.LocalDateTime;
import java.util.List;

public record UserDto (
        Long id,
        String name,
        String email,
        Integer age,
        List<Role>roles
) {
}
