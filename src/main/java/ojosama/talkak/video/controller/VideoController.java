package ojosama.talkak.video.controller;

import static org.hibernate.query.sqm.tree.SqmNode.log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import ojosama.talkak.reaction.service.ReactionService;
import ojosama.talkak.video.domain.Video;
import ojosama.talkak.video.request.VideoCategoryRequest;
import ojosama.talkak.video.request.VideoRequest;
import ojosama.talkak.video.request.YoutubeCategoryRequest;
import ojosama.talkak.video.request.YoutubeUrlValidationRequest;
import ojosama.talkak.video.response.VideoDetailsResponse;
import ojosama.talkak.video.response.VideoInfoResponse;
import ojosama.talkak.video.response.YoutubeApiResponse;
import ojosama.talkak.video.response.YoutubeUrlValidationResponse;
import ojosama.talkak.video.service.AwsS3Service;
import ojosama.talkak.video.service.VideoService;
import ojosama.talkak.video.service.YoutubeService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/videos")
public class VideoController implements VideoApiController {

    private final VideoService videoService;
    private final YoutubeService youtubeService;
    private final AwsS3Service awsS3Service;
    private final ReactionService reactionService;

    public VideoController(VideoService videoService, YoutubeService youtubeService,
        AwsS3Service awsS3Service, ReactionService reactionService) {
        this.videoService = videoService;
        this.youtubeService = youtubeService;
        this.awsS3Service = awsS3Service;
        this.reactionService = reactionService;
    }

    @GetMapping
    public ResponseEntity<List<VideoInfoResponse>> getPopularVideosByCategory(
        @RequestParam("categoryId") Long categoryId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<VideoInfoResponse> videos = videoService.getVideoByCategory(
            new VideoCategoryRequest(categoryId), pageable);
        return ResponseEntity.ok(videos);
    }

    @GetMapping("/{videoId}")
    public ResponseEntity<VideoDetailsResponse> getVideoDetails(@PathVariable Long videoId) {
        VideoDetailsResponse response = videoService.getVideoDetailsByVideoId(videoId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{videoId}/extract")
    public ResponseEntity<URL> downloadVideo(@PathVariable Long videoId)
        throws MalformedURLException {
        URL downloadUrl = awsS3Service.generateDownloadUrl(videoId);
        return ResponseEntity.ok(downloadUrl);
    }

    @PostMapping("/create")
    public ResponseEntity<VideoInfoResponse> createVideo(@RequestBody VideoRequest videoRequest) {
        Video video = videoService.createVideo(
            videoRequest.title(),
            videoRequest.memberId(),
            videoRequest.categoryId(),
            videoRequest.fileName()
        );
        return new ResponseEntity<>(
            new VideoInfoResponse(video.getId(), video.getThumbnail(), video.getTitle(),
                video.getMemberId(), video.getCreatedAt()), HttpStatus.CREATED);
    }

    @PostMapping("/youtube-url-validation")
    public ResponseEntity<YoutubeUrlValidationResponse> validateYoutubeUrl(
        @RequestBody YoutubeUrlValidationRequest req) {
        YoutubeUrlValidationResponse response = videoService.validateYoutubeUrl(req);
        return ResponseEntity.ok(response);
    }

    // 메인페이지에서 유튜브 관련 영상 불러오기(카테고리 지정 X)
    @GetMapping("/youtube")
    public ResponseEntity<List<YoutubeApiResponse>> getPopularYoutubeShorts() throws IOException {
        long start = System.currentTimeMillis();
        List<YoutubeApiResponse> response = youtubeService.getPopularShorts();
        long end = System.currentTimeMillis();
        log.info("인기 쇼츠 Cache 수행시간 : " + (end - start));

        return ResponseEntity.ok(response);
    }

    // 메인페이지에서 유튜브 관련 영상 불러오기(카테고리 지정)
    @GetMapping("/youtube/{categoryId}")
    public ResponseEntity<List<YoutubeApiResponse>> getPopularYoutubeShortsByCategory(
        @PathVariable Long categoryId) throws IOException {
        long start = System.currentTimeMillis();
        List<YoutubeApiResponse> response = youtubeService.getShortsByCategory(
            new YoutubeCategoryRequest(categoryId));
        long end = System.currentTimeMillis();
        log.info("카테고리별 쇼츠 Cache 수행시간 : " + (end - start));

        return ResponseEntity.ok(response);
    }

}