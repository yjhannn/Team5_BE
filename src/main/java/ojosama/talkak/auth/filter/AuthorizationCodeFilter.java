package ojosama.talkak.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import ojosama.talkak.auth.config.AuthProperties;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class AuthorizationCodeFilter extends OncePerRequestFilter {

    private final AuthProperties authProperties;
    private final ObjectMapper objectMapper;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException
    {
        if ("POST".equalsIgnoreCase(request.getMethod()) &&
            antPathMatcher.match(authProperties.redirectionUri(), request.getRequestURI()))
        {
            Map<String, Object> params = objectMapper.readValue(request.getInputStream(), Map.class);
            String state = (String) params.get(OAuth2ParameterNames.STATE);
            String code = (String) params.get(OAuth2ParameterNames.CODE);
            code = URLDecoder.decode(code, StandardCharsets.UTF_8);
            String scope = (String) params.get(OAuth2ParameterNames.SCOPE);
            String authuser = (String) params.get("authuser");
            String prompt = (String) params.get("prompt");

            Map<String, String[]> parameterMap = new LinkedHashMap<>(request.getParameterMap());
            parameterMap.put(OAuth2ParameterNames.STATE, new String[]{state});
            parameterMap.put(OAuth2ParameterNames.CODE, new String[]{code});
            parameterMap.put(OAuth2ParameterNames.SCOPE, new String[]{scope});
            parameterMap.put("authuser", new String[]{authuser});
            parameterMap.put("prompt", new String[]{prompt});

            request = new HttpServletRequestWrapper(request) {
                @Override
                public String getParameter(String name) {
                    String[] values = parameterMap.get(name);
                    return values != null ? values[0] : super.getParameter(name);
                }

                @Override
                public Map<String, String[]> getParameterMap() {
                    return parameterMap;
                }
            };
        }
        filterChain.doFilter(request, response);
    }
}
