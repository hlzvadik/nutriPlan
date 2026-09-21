package goltsov.nutriPlan.security;

import goltsov.nutriPlan.baseclasses.User;
import goltsov.nutriPlan.dto.AuthenticationResponse;
import goltsov.nutriPlan.dto.LoginRequest;
import goltsov.nutriPlan.dto.RegisterRequest;
import goltsov.nutriPlan.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class ApiAuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;


    public ApiAuthenticationController(AuthenticationManager authenticationManager, JwtService jwtService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> postLogin(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        String refreshToken = jwtService.generateRefreshToken(authentication.getName());
        String accessToken = jwtService.generateAccessToken(authentication.getName());
        return ResponseEntity.status(200).body(new AuthenticationResponse(accessToken, refreshToken));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> postRegister(@RequestBody RegisterRequest request) {
        User newUser = userService.createUser(new User(null, request.name(), request.email(), request.password(), request.age(), request.createdAt(), request.roles()));
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        String refreshToken = jwtService.generateRefreshToken(authentication.getName());
        String accessToken = jwtService.generateAccessToken(authentication.getName());
        return ResponseEntity.status(200).body(new AuthenticationResponse(accessToken, refreshToken));
    }
}
