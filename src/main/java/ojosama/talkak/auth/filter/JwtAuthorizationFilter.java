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
import ojosama.talkak.common.exception.code.MemberError;
import ojosama.talkak.common.util.RedisUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException
    {
        String headerValue = request.getHeader("Authorization");

        if (isValidToken(headerValue)) {
            SecurityContextHolder.getContext()
                .setAuthentication(
                    createUsernamePasswordAuthenticationToken(resolveToken(headerValue)));

            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = redisUtil.getValues(
                String.format("REFRESH_TOKEN:%d", getIdFromToken(resolveToken(headerValue))))
            .orElseThrow(() -> TalKakException.of(MemberError.NOT_EXISTING_MEMBER));

        if (jwtUtil.isValidToken(refreshToken)) {
            throw TalKakException.of(AuthError.INVALID_ACCESS_TOKEN);
        }
        throw TalKakException.of(AuthError.INVALID_REFRESH_TOKEN);
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
