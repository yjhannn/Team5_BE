package ojosama.talkak.auth.config;

import ojosama.talkak.auth.service.OAuth2Service;
import ojosama.talkak.auth.service.StatelessAuthorizationRequestRepository;
import ojosama.talkak.member.repository.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;

@Configuration
public class SecurityServiceConfig {

    @Bean
    public DefaultOAuth2UserService defaultOAuth2UserService() {
        return new DefaultOAuth2UserService();
    }

    @Bean
    public OAuth2Service oAuth2Service(DefaultOAuth2UserService defaultOAuth2UserService, MemberRepository memberRepository) {
        return new OAuth2Service(defaultOAuth2UserService, memberRepository);
    }

    @Bean
    public StatelessAuthorizationRequestRepository statelessAuthorizationRequestRepository(AuthProperties authProperties, GoogleProperties googleProperties) {
        return new StatelessAuthorizationRequestRepository(authProperties, googleProperties);
    }
}
