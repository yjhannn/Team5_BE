package ojosama.talkak.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import ojosama.talkak.common.exception.ErrorResponse;
import ojosama.talkak.common.exception.code.AuthError;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException {

        String path = request.getServletPath();

        // 로그인 경로에 대해서만 리다이렉트
        if (path.startsWith("/login")) {
            // OAuth2 로그인 페이지로 리다이렉트
            response.sendRedirect("/api/login/google");
            return;
        }

        // 나머지 모든 요청은 JSON 응답 처리
        handleJsonResponse(request, response, authException);
    }

    private void handleJsonResponse(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException {

        AuthError errorType = AuthError.from(
            (Exception) request.getAttribute("exception")
        );

        response.setStatus(errorType.status().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ErrorResponse errorResponse = ErrorResponse.of(
            errorType.status(),
            errorType.message(),
            errorType.code()
        );

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
