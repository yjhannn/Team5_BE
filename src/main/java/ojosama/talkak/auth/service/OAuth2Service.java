package ojosama.talkak.auth.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import ojosama.talkak.auth.dto.GoogleUserDetails;
import ojosama.talkak.auth.dto.OAuth2UserDetails;
import ojosama.talkak.member.domain.Member;
import ojosama.talkak.member.repository.MemberRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RequiredArgsConstructor
public class OAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService defaultOAuth2UserService;
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);
        GoogleUserDetails userDetails = GoogleUserDetails.from(oAuth2User.getAttributes());

        Optional<Member> member = memberRepository.findByEmail(userDetails.email());
        return member.map(OAuth2UserDetails::of)
            .orElseGet(() -> OAuth2UserDetails.of(register(userDetails)));
    }

    private Member register(GoogleUserDetails userDetails) {
        return memberRepository.save(userDetails.toEntity());
    }
}
