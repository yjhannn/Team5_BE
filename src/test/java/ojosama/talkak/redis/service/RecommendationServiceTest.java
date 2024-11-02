package ojosama.talkak.redis.service;

import static org.assertj.core.api.Assertions.*;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import ojosama.talkak.category.domain.CategoryType;
import ojosama.talkak.redis.config.RecommendTestContainerConfig;
import ojosama.talkak.redis.config.RedisTestContainerConfig;
import ojosama.talkak.redis.domain.EventQueue;
import ojosama.talkak.redis.domain.Reaction;
import ojosama.talkak.redis.domain.VideoInfo;
import ojosama.talkak.redis.repository.EventQueueRepository;
import ojosama.talkak.redis.repository.ReactionsRepository;
import ojosama.talkak.redis.repository.VideoInfoRepository;
import ojosama.talkak.video.domain.Video;
import ojosama.talkak.video.repository.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
@Slf4j
@ExtendWith({RedisTestContainerConfig.class, RecommendTestContainerConfig.class})
class RecommendationServiceTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private VideoInfoRepository videoInfoRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private ReactionsRepository reactionsRepository;

    @Autowired
    private EventQueueRepository eventQueueRepository;

    @Autowired
    private RecommendationService recommendationService;

    @BeforeEach
    void setUp() throws IOException {

        for (int i = 1; i <= CategoryType.values().length; i++) {
            for (int j = 1; j <= 10; j++) {
                videoRepository.save(new Video("title", 0L));
                videoInfoRepository.save((long) i, (long) j,
                    VideoInfo.of(LocalDateTime.now(), 0L, 0L));
            }
        }
    }


    @Test
    @DisplayName("기본 추천 영상 리스트 받아오기")
    void get_default_recommendation_videos() throws InterruptedException {
        Long categoryId = 1L;
        TimeUnit.SECONDS.sleep(10);
        List<Video> videoList = recommendationService.getDefaultRecommendationVideos(
            categoryId);

        assertThat(videoList).hasSize((int) RecommendationService.RECOMMENDATION_VIDEOS_COUNT);
    }

    /**
     * 초기 상태: 모든 영상들 조회수,좋아요 0,0 시청,좋아요를 누른 1번 영상이 우선 추천 나머지 동일한 점수..
     */
    @Test
    @DisplayName("유저 추천 영상 리스트 받아오기")
    void get_member_recommendation_videos() throws InterruptedException {
        Long categoryId = 1L;
        Long memberId = 1L;
        TimeUnit.SECONDS.sleep(10);
        reactionsRepository.save(memberId, 1L, Reaction.of(1, 1));
        reactionsRepository.save(memberId, 4L, Reaction.of(1, 1));
        eventQueueRepository.save(
            EventQueue.of(memberId, categoryId, LocalDateTime.of(2024, 1, 1, 12, 0, 0)));
        List<Video> recommendedVideos = recommendationService.getRecommendedVideos(memberId,
            categoryId);
        assertThat(recommendedVideos.getFirst()
            .getId()).isEqualTo(1L);
    }


}