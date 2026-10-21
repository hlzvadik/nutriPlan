package goltsov.nutriPlan.security;

import goltsov.nutriPlan.dto.AuthenticationResponse;
import goltsov.nutriPlan.dto.LoginRequest;
import goltsov.nutriPlan.dto.RegisterRequest;
import goltsov.nutriPlan.entities.Role;
import goltsov.nutriPlan.entities.RoleName;
import goltsov.nutriPlan.entities.User;
import goltsov.nutriPlan.repositories.RoleRepository;
import goltsov.nutriPlan.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class ApiAuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final RoleRepository roleRepository;


    public ApiAuthenticationController(AuthenticationManager authenticationManager, JwtService jwtService, UserService userService, RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
        this.roleRepository = roleRepository;
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
        if (request.email() == null || request.email().isBlank() || request.password() == null || request.password().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        if (userService.getUserByEmail(request.email()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Roles from the request are intentionally ignored: otherwise anyone could register as ROLE_ADMIN.
        Role userRole = roleRepository.getRoleByRoleName(RoleName.ROLE_USER);
        if (userRole == null) {
            throw new IllegalStateException("Role " + RoleName.ROLE_USER + " is not initialized");
        }
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        userService.createUser(new User(request.name(), request.email(), request.password(), request.age(), LocalDateTime.now(), roles));
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        String refreshToken = jwtService.generateRefreshToken(authentication.getName());
        String accessToken = jwtService.generateAccessToken(authentication.getName());
        return ResponseEntity.status(200).body(new AuthenticationResponse(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> postRefreshToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }
        String refreshToken = authHeader.substring(7);

        if (!jwtService.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(401).build();
        }
        String userEmail = jwtService.extractEmailFromRefreshToken(refreshToken);
        if (userEmail == null || userService.getUserByEmail(userEmail) == null) {
            return ResponseEntity.status(401).build();
        }
        String newAccessToken = jwtService.generateAccessToken(userEmail);
        return ResponseEntity.ok(new AuthenticationResponse(newAccessToken, refreshToken));
    }
}
