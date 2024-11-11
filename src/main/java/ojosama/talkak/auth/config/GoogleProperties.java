package ojosama.talkak.auth.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.google")
public record GoogleProperties(
    String clientId,
    String redirectUri,
    List<String> scope
) {}
