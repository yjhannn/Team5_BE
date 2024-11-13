package ojosama.talkak.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import ojosama.talkak.auth.filter.AuthorizationCodeFilter;
import ojosama.talkak.auth.filter.JwtAuthorizationFilter;
import ojosama.talkak.auth.filter.SuccessHandler;
import ojosama.talkak.auth.utils.JwtUtil;
import ojosama.talkak.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final AuthProperties authProperties;

    @Value("${springdoc.swagger-ui.path}")
    private String swaggerAlias;

    @Bean
    public SuccessHandler successHandler(JwtUtil jwtUtil, RedisUtil redisUtil, ObjectMapper objectMapper, JwtProperties jwtProperties) {
        return new SuccessHandler(jwtUtil, redisUtil, objectMapper, jwtProperties);
    }

    @Bean
    public AuthorizationCodeFilter authorizationCodeFilter(AuthProperties authProperties, ObjectMapper objectMapper) {
        return new AuthorizationCodeFilter(authProperties, objectMapper);
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(JwtUtil jwtUtil, ObjectMapper objectMapper, List<String> skipPaths) {
        return new JwtAuthorizationFilter(jwtUtil, objectMapper, skipPaths);
    }

    @Bean
    public List<String> skipPaths() {
        return Arrays.asList(
            "/h2-console/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            swaggerAlias,
            authProperties.authorizationUri(),
            "/api/issue",
            "/api/videos",
            "/api/videos/{videoId:\\d+}",
            "/api/videos/youtube/**"
        );
    }
}
