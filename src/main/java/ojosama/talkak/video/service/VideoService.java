package ojosama.talkak.video.service;

import java.util.List;
import java.util.stream.Collectors;
import ojosama.talkak.common.exception.TalKakException;
import ojosama.talkak.common.exception.code.MemberError;
import ojosama.talkak.common.exception.code.VideoError;
import ojosama.talkak.member.domain.Member;
import ojosama.talkak.member.repository.MemberRepository;
import ojosama.talkak.video.domain.Video;
import ojosama.talkak.video.repository.VideoRepository;
import ojosama.talkak.video.request.VideoCategoryRequest;
import ojosama.talkak.video.response.MemberInfoResponse;
import ojosama.talkak.video.response.VideoDetailsResponse;
import ojosama.talkak.video.response.VideoInfoResponse;
import ojosama.talkak.video.response.VideoResponse;
import ojosama.talkak.video.response.YoutubeUrlValidationAPIResponse;
import ojosama.talkak.video.request.YoutubeUrlValidationRequest;
import ojosama.talkak.video.response.YoutubeUrlValidationResponse;
import ojosama.talkak.video.util.IdExtractor;
import ojosama.talkak.common.util.WebClientUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public VideoService(WebClientUtil webClientUtil, VideoRepository videoRepository, MemberRepository memberRepository) {
        this.webClientUtil = webClientUtil;
        this.videoRepository = videoRepository;
        this.memberRepository = memberRepository;
    }

    public VideoDetailsResponse getVideoDetailsByVideoId(Long videoId) {
        Video video = videoRepository.findById(videoId)
            .orElseThrow(() -> TalKakException.of(VideoError.VIDEO_NOT_FOUND));
        Member member = memberRepository.findById(video.getMember().getId())
            .orElseThrow(() -> TalKakException.of(MemberError.NOT_EXISTING_MEMBER));
        MemberInfoResponse memberInfoResponse = new MemberInfoResponse(member.getId(),
            member.getImageUrl(), member.getUsername());
        return new VideoDetailsResponse(video.getId(), video.getVideoUrl(), memberInfoResponse, video.getCountLikes(),
            video.commentsCount());
    }

    public List<VideoInfoResponse> getVideoByCategory(VideoCategoryRequest req, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Video> videos = videoRepository.findByCategoryIdOrderByViewsDesc(
            req.categoryId(), pageable);

        if (videos.isEmpty()) {
            throw TalKakException.of(VideoError.VIDEO_NOT_FOUND);
        }

        return videos.stream()
            .map(video -> new VideoInfoResponse(
                video.getId(),
                video.getThumbnail(),
                video.getTitle(),
                video.getMember().getId(),
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
