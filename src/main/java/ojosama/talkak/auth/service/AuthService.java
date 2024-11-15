package ojosama.talkak.auth.service;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import ojosama.talkak.auth.config.JwtProperties;
import ojosama.talkak.auth.dto.TokenResponse;
import ojosama.talkak.auth.utils.JwtUtil;
import ojosama.talkak.common.exception.TalKakException;
import ojosama.talkak.common.exception.code.AuthError;
import ojosama.talkak.common.exception.code.MemberError;
import ojosama.talkak.common.util.RedisKeyUtil;
import ojosama.talkak.common.util.RedisUtil;
import ojosama.talkak.member.domain.Member;
import ojosama.talkak.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final MemberRepository memberRepository;
    private final JwtProperties jwtProperties;

    public TokenResponse reissue(String refreshToken) {
        if (!jwtUtil.isValidToken(refreshToken)) {
            throw TalKakException.of(AuthError.INVALID_REFRESH_TOKEN);
        }
        Long id = jwtUtil.getIdFromToken(refreshToken);
        Member member = findMemberById(id);

        String accessToken = jwtUtil.generateAccessToken(id, member.getEmail(), member.getUsername());
        refreshToken = jwtUtil.generateRefreshToken(id);

        String key = RedisKeyUtil.REFRESH_TOKEN.of(id);
        Duration duration = Duration.ofSeconds(jwtProperties.refreshTokenExpireIn());
        redisUtil.setValues(key, refreshToken, duration);

        return TokenResponse.of(accessToken, refreshToken, !member.getAdditionalInfoProvided());
    }

    public void logout(Long id) {
        redisUtil.deleteValues(RedisKeyUtil.REFRESH_TOKEN.of(id));
    }

    public TokenResponse issue() {
        Member member = memberRepository.save(Member.of("test", "", "test@test.com"+ LocalDateTime.now()));
        String accessToken = jwtUtil.generateAccessToken(member.getId(), member.getEmail(), member.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(member.getId());
        return TokenResponse.of(accessToken, refreshToken, !member.getAdditionalInfoProvided());
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> TalKakException.of(MemberError.NOT_EXISTING_MEMBER));
    }
}
