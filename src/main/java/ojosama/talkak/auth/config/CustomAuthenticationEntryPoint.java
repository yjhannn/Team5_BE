package ojosama.talkak.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
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

        Exception exception = (Exception) request.getAttribute("exception");

        HttpStatus status = null;
        String message = null;
        String error = null;

        if (exception != null) {
            if (exception instanceof ExpiredJwtException) {
                status = HttpStatus.UNAUTHORIZED;
                message = "토큰이 만료되었습니다.";
                error = "Token Expired";
            }
            // ... 다른 예외 처리
        } else {
            status = HttpStatus.UNAUTHORIZED;
            message = "인증에 실패했습니다.";
            error = "Unauthorized";
        }

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", new Date());
        errorResponse.put("status", status.value());
        errorResponse.put("error", error);
        errorResponse.put("message", message);
        errorResponse.put("path", request.getRequestURI());

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
