package goltsov.nutriPlan.security;

import goltsov.nutriPlan.dto.UserDto;
import goltsov.nutriPlan.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class ApiUserController {

    private final UserService userService;

    public ApiUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public UserDto getProfile(Authentication authentication) {
        String email = authentication.getName();
        return userService.getUserDtoByEmail(email);
    }
}
