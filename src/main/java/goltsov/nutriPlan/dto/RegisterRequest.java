package goltsov.nutriPlan.dto;

import goltsov.nutriPlan.baseclasses.Role;

import java.time.LocalDateTime;
import java.util.List;

public record RegisterRequest (
        String name,
        String email,
        String password,
        Integer age,
        LocalDateTime createdAt,
        List<Role> roles
) {
}
