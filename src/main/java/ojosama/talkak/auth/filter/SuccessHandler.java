package ojosama.talkak.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import ojosama.talkak.auth.config.JwtProperties;
import ojosama.talkak.auth.dto.OAuth2UserDetails;
import ojosama.talkak.auth.dto.TokenResponse;
import ojosama.talkak.auth.utils.JwtUtil;
import ojosama.talkak.common.util.RedisKeyUtil;
import ojosama.talkak.common.util.RedisUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RequiredArgsConstructor
public class SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final ObjectMapper objectMapper;
    private final JwtProperties jwtProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        OAuth2UserDetails oAuth2User = (OAuth2UserDetails) authentication.getPrincipal();
        String accessToken = jwtUtil.generateAccessToken(oAuth2User.id(), oAuth2User.email(), oAuth2User.username());
        String refreshToken = jwtUtil.generateRefreshToken();

        String key = RedisKeyUtil.REFRESH_TOKEN.of(oAuth2User.id());
        Duration duration = Duration.ofSeconds(jwtProperties.refreshTokenExpireIn());
        redisUtil.setValues(key, refreshToken, duration);

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter()
                .write(objectMapper.writeValueAsString(TokenResponse.of(accessToken, refreshToken)));
    }
}
