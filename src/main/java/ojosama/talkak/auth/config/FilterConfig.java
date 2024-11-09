package ojosama.talkak.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import ojosama.talkak.auth.filter.JwtAuthorizationFilter;
import ojosama.talkak.auth.filter.SuccessHandler;
import ojosama.talkak.auth.utils.JwtUtil;
import ojosama.talkak.common.util.RedisUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public SuccessHandler successHandler(JwtUtil jwtUtil, RedisUtil redisUtil, ObjectMapper objectMapper, JwtProperties jwtProperties) {
        return new SuccessHandler(jwtUtil, redisUtil, objectMapper, jwtProperties);
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(JwtUtil jwtUtil, ObjectMapper objectMapper) {
        return new JwtAuthorizationFilter(jwtUtil, objectMapper);
    }
}
