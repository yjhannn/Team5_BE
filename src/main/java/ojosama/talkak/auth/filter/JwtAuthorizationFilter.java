package ojosama.talkak.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import ojosama.talkak.auth.utils.JwtUtil;
import ojosama.talkak.common.exception.TalKakException;
import ojosama.talkak.common.exception.code.AuthError;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException
    {
        String headerValue = request.getHeader("Authorization");

        if (headerValue == null) {
            filterChain.doFilter(request, response);
            return;
        }
        if (isValidToken(headerValue)) {
            SecurityContextHolder.getContext()
                .setAuthentication(
                    createUsernamePasswordAuthenticationToken(resolveToken(headerValue)));

            filterChain.doFilter(request, response);
            return;
        }
        throw TalKakException.of(AuthError.INVALID_ACCESS_TOKEN);
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

    private String resolveToken(String headerValue) {
        return headerValue.split(" ")[1].trim();
    }
}
