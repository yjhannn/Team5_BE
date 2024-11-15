package ojosama.talkak.reaction;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;
import ojosama.talkak.member.domain.Age;
import ojosama.talkak.member.domain.Member;
import ojosama.talkak.member.domain.MembershipTier;
import ojosama.talkak.member.repository.MemberRepository;
import ojosama.talkak.member.service.MemberService;
import ojosama.talkak.reaction.service.ReactionService;
import ojosama.talkak.recommendation.domain.VideoInfo;
import ojosama.talkak.recommendation.repository.VideoInfoRepository;
import ojosama.talkak.redis.config.RedisTestContainerConfig;
import ojosama.talkak.video.repository.VideoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Import({ReactionService.class, MemberService.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith({RedisTestContainerConfig.class})
public class ReactionTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ReactionService reactionService;
    @Autowired
    private VideoInfoRepository videoInfoRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private VideoRepository videoRepository;
    
    private static Member member;
    private Long videoId = 1L;
    private Long categoryId = 1L;

    @BeforeAll
    static void setUp(@Autowired MemberRepository memberRepository) {
        member = memberRepository.save(demoMember());
    }

    @DisplayName("좋아요 최초 증가 이후 불변 확인 테스트")
    @ParameterizedTest
    @ValueSource(longs = {0L, 10L, 124L, 9342L})
    @Transactional
    void like_at_first_and_immutability_like_count(Long countLikes) {
        VideoInfo videoInfo = videoInfoRepository.save(categoryId, videoId,
            VideoInfo.of(LocalDateTime.now(), 0L, countLikes));

        for (int i=0; i<50;i++) {
            reactionService.toggleLike(member.getId(), videoId);
        }
        VideoInfo updatedVideoInfo = videoInfoRepository.findByCategoryAndVideoId(categoryId,
            videoId).get();
        assertThat(updatedVideoInfo.getLikeCount()).isEqualTo(countLikes + 1L);
    }

    @AfterEach
    void tearDown() {
        Set<String> keys = redisTemplate.keys("*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }


    private static Member demoMember() {
        return new Member("철수 김", "https://",
            "abc123@a.com", true, Age.TWENTY, MembershipTier.Basic, 0, new ArrayList<>(), true);
    }
}
