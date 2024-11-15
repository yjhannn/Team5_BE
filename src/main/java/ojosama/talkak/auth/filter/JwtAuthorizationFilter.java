package ojosama.talkak.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import ojosama.talkak.auth.utils.JwtUtil;
import ojosama.talkak.common.exception.ErrorResponse;
import ojosama.talkak.common.exception.code.AuthError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final List<String> skipPaths;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return skipPaths.stream()
            .anyMatch(pattern -> antPathMatcher.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException
    {
        String headerValue = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (headerValue == null) {
            filterChain.doFilter(request, response);
            return;
        }
        if (isValidToken(headerValue)) {
            if (jwtUtil.isRefreshToken(jwtUtil.resolveToken(headerValue))) {
                filterChain.doFilter(request, response);
                return;
            }
            SecurityContextHolder.getContext()
                .setAuthentication(
                    createUsernamePasswordAuthenticationToken(jwtUtil.resolveToken(headerValue)));

            filterChain.doFilter(request, response);
            return;
        }
        handleInvalidTokenException(response);
    }

    private boolean isValidToken(String value) {
        return !Objects.isNull(value) &&
            value.startsWith("Bearer ") &&
            jwtUtil.isValidToken(value.split(" ")[1]);
    }

    private UsernamePasswordAuthenticationToken createUsernamePasswordAuthenticationToken(String token) {
        Long id = getIdFromToken(token);
        return new UsernamePasswordAuthenticationToken(id, null, new ArrayList<>());
    }

    private Long getIdFromToken(String token) {
        return jwtUtil.getIdFromToken(token);
    }

    private void handleInvalidTokenException(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        AuthError error = AuthError.EXPIRED_TOKEN;
        response.getWriter()
            .write(objectMapper.writeValueAsString(
                ErrorResponse.of(error.status(), error.code(), error.message())));
    }
}
