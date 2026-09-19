package goltsov.nutriPlan.dto;

public record AuthenticationResponse(
        String accessToken,
        String refreshToken
) {
}
