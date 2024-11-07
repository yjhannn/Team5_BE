package ojosama.talkak.video.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import ojosama.talkak.common.exception.TalKakException;
import ojosama.talkak.member.domain.Member;
import ojosama.talkak.member.repository.MemberRepository;
import ojosama.talkak.reaction.service.ReactionService;
import ojosama.talkak.recommendation.domain.VideoInfo;
import ojosama.talkak.video.domain.Video;
import ojosama.talkak.video.repository.VideoRepository;
import ojosama.talkak.video.request.VideoCategoryRequest;
import ojosama.talkak.video.response.VideoDetailsResponse;
import ojosama.talkak.video.response.VideoInfoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
public class MockVideoServiceTest {
    @Mock
    private VideoRepository videoRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ReactionService reactionService;

    @InjectMocks
    @Autowired
    private VideoService videoService;

    private Member testMember;
    private Video testVideo;
    private VideoInfo testVideoInfo;

    @BeforeEach
    void setUp() {
        // 테스트 멤버 생성 및 ID 설정
        testMember = Member.of("username", "imageUrl", "email");
        testMember.createIdForTest(1L);

        // 테스트 비디오 생성 및 ID 설정
        testVideo = new Video("Test Video", testMember.getId(), 1L, "testUniqueFileName");
        testVideo.createIdForTest(1L);

        // 테스트 비디오 정보 생성
        testVideoInfo = VideoInfo.of(LocalDateTime.now(), 100L, 10L);
    }
    @Test
    @DisplayName("특정 영상 불러오기 성공")
    void testGetVideoDetailsByVideoId() {
        // given
        Long videoId = testVideo.getId();
        Long memberId = testMember.getId();

        // mock 동작 정의
        given(videoRepository.findById(videoId)).willReturn(Optional.of(testVideo));
        given(memberRepository.findById(memberId)).willReturn(Optional.of(testMember));
        given(reactionService.incrementViewCount(testVideo.getCategoryId(), videoId)).willReturn(testVideoInfo);

        // when
        VideoDetailsResponse response = videoService.getVideoDetailsByVideoId(videoId);

        // then
        assertThat(response.getId()).isEqualTo(videoId);
        assertThat(response.getLikeCount()).isEqualTo(testVideoInfo.getLikeCount());
        assertThat(response.getViewCount()).isEqualTo(testVideoInfo.getViewCount());
    }

    @Test
    @DisplayName("특정 영상 불러오기 실패 - 잘못된 비디오 ID")
    void testGetVideoDetailsByVideoIdInvalidId() {
        // given
        Long invalidVideoId = 999L;

        // mock 동작 정의
        given(videoRepository.findById(invalidVideoId)).willReturn(Optional.empty());

        // then
        assertThrows(TalKakException.class, () -> videoService.getVideoDetailsByVideoId(invalidVideoId));
    }

    @Test
    @DisplayName("카테고리별 영상 리스트 불러오기 성공")
    void testGetVideoByCategory() {
        // given
        VideoCategoryRequest request = new VideoCategoryRequest(1L);
        Pageable pageable = PageRequest.of(0, 5);
        List<Video> videoList = Arrays.asList(testVideo);
        Page<Video> videoPage = new PageImpl<>(videoList);

        // mock 동작 정의
        given(videoRepository.findByCategoryId(request.categoryId(), pageable)).willReturn(videoPage);

        // when
        List<VideoInfoResponse> responseList = videoService.getVideoByCategory(request, pageable);

        // then
        assertThat(responseList).hasSize(1);
        assertThat(responseList.get(0).videoId()).isEqualTo(testVideo.getId());
    }
}

