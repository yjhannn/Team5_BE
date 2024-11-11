package ojosama.talkak.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import ojosama.talkak.auth.config.AuthProperties;
import ojosama.talkak.auth.config.GoogleProperties;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

@RequiredArgsConstructor
public class StatelessAuthorizationRequestRepository
    implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private final String REGISTRATION_ID = CommonOAuth2Provider.GOOGLE.name().toLowerCase();
    private final AuthProperties authProperties;
    private final GoogleProperties googleProperties;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return OAuth2AuthorizationRequest.authorizationCode()
            .authorizationUri(authProperties.authorizationUri())
            .clientId(googleProperties.clientId())
            .redirectUri(googleProperties.redirectUri())
            .scope(googleProperties.scope().toArray(String[]::new))
            .state(request.getParameter(OAuth2ParameterNames.STATE))
            .attributes(Map.of(OAuth2ParameterNames.REGISTRATION_ID, REGISTRATION_ID))
            .build();
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest,
        HttpServletRequest request, HttpServletResponse response) {
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
        HttpServletResponse response) {
        return loadAuthorizationRequest(request);
    }
}
