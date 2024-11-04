package ojosama.talkak.video.service;

import java.util.List;
import java.util.stream.Collectors;
import ojosama.talkak.common.exception.TalKakException;
import ojosama.talkak.common.exception.code.MemberError;
import ojosama.talkak.common.exception.code.VideoError;
import ojosama.talkak.member.domain.Member;
import ojosama.talkak.member.repository.MemberRepository;
import ojosama.talkak.recommendation.domain.VideoInfo;
import ojosama.talkak.recommendation.repository.VideoInfoRepository;
import ojosama.talkak.reaction.service.ReactionService;
import ojosama.talkak.video.domain.Video;
import ojosama.talkak.video.repository.VideoRepository;
import ojosama.talkak.video.request.VideoCategoryRequest;
import ojosama.talkak.video.response.MemberInfoResponse;
import ojosama.talkak.video.response.VideoDetailsResponse;
import ojosama.talkak.video.response.VideoInfoResponse;
import ojosama.talkak.video.response.YoutubeUrlValidationAPIResponse;
import ojosama.talkak.video.request.YoutubeUrlValidationRequest;
import ojosama.talkak.video.response.YoutubeUrlValidationResponse;
import ojosama.talkak.video.util.IdExtractor;
import ojosama.talkak.common.util.WebClientUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class VideoService {

    @Value("${youtube.api-key}")
    private String YOUTUBE_API_KEY;
    @Value("${youtube.api-url}")
    private String YOUTUBE_API_BASE_URL;

    private final WebClientUtil webClientUtil;
    private final VideoRepository videoRepository;
    private final MemberRepository memberRepository;
    private final VideoInfoRepository videoInfoRepository;
    private final ReactionService reactionService;

    public VideoService(WebClientUtil webClientUtil, VideoRepository videoRepository, MemberRepository memberRepository,
        VideoInfoRepository videoInfoRepository, ReactionService reactionService) {
        this.webClientUtil = webClientUtil;
        this.videoRepository = videoRepository;
        this.memberRepository = memberRepository;
        this.videoInfoRepository = videoInfoRepository;
        this.reactionService = reactionService;
    }

    public VideoDetailsResponse getVideoDetailsByVideoId(Long videoId) {
        Video video = videoRepository.findById(videoId)
            .orElseThrow(() -> TalKakException.of(VideoError.INVALID_VIDEO_ID));
        Member member = memberRepository.findById(video.getMemberId())
            .orElseThrow(() -> TalKakException.of(MemberError.NOT_EXISTING_MEMBER));
        VideoInfo videoInfo = videoInfoRepository.findByCategoryAndVideoId(video.getCategoryId(), videoId)
            .orElseThrow(() -> TalKakException.of(VideoError.INVALID_VIDEO_ID));

        MemberInfoResponse memberInfoResponse = new MemberInfoResponse(member.getId(),
            member.getImageUrl(), member.getUsername());

        reactionService.createReaction(member.getId(), videoId);
        // 조회수 증가
        reactionService.incrementViewCount(video.getCategoryId(), videoId);
        return new VideoDetailsResponse(video, videoInfo, member);
    }

    public List<VideoInfoResponse> getVideoByCategory(VideoCategoryRequest req, Pageable pageable) {
        Page<Video> videos = videoRepository.findByCategoryId(req.categoryId(), pageable);

        return videos.stream()
            .map(video -> new VideoInfoResponse(
                video.getId(),
                video.getThumbnail(),
                video.getTitle(),
                video.getId(),
                video.getCreatedAt()
            ))
            .collect(Collectors.toList());
    }
    public YoutubeUrlValidationResponse validateYoutubeUrl(YoutubeUrlValidationRequest req) {
        String videoId = extractVideoIdOrThrow(req.url());

        String YOUTUBE_API_URL = youtubeApiRequestUrlBuilder(videoId);

        YoutubeUrlValidationAPIResponse response = webClientUtil.get(YOUTUBE_API_URL, YoutubeUrlValidationAPIResponse.class);

        return new YoutubeUrlValidationResponse(response);
    }

    private String youtubeApiRequestUrlBuilder(String videoId) {
        StringBuilder youtubeApiRequestUrl = new StringBuilder();
        youtubeApiRequestUrl.append(YOUTUBE_API_BASE_URL)
                .append("?part=snippet")
                .append("&id=")
                .append(videoId)
                .append("&key=")
                .append(YOUTUBE_API_KEY);
        return youtubeApiRequestUrl.toString();
    }

    private String extractVideoIdOrThrow(String url) {
        String videoId = Optional.ofNullable(IdExtractor.extract(url))
                .orElseThrow(() -> TalKakException.of(VideoError.INVALID_VIDEO_ID));
        return videoId;
    }
}
