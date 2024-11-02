package ojosama.talkak.video.response;

import lombok.Getter;
import ojosama.talkak.member.domain.Member;
import ojosama.talkak.redis.domain.VideoInfo;
import ojosama.talkak.video.domain.Video;

@Getter
public class VideoDetailsResponse {
    private Long id;
    private Long categoryId;
    private String videoUrl;
    private MemberInfoResponse memberInfo;
    private Long likeCount;
    private Long viewCount;
    private int commentsCount;

    // Video, VideoInfo, Member 객체를 받는 생성자
    public VideoDetailsResponse(Video video, VideoInfo videoInfo, Member member) {
        this.id = video.getId();
        this.categoryId = video.getCategoryId();
        this.videoUrl = video.getVideoUrl();
        this.likeCount = videoInfo.getLikeCount();
        this.viewCount = videoInfo.getViewCount();
        this.commentsCount = video.commentsCount();

        // Member 객체를 MemberInfoResponse로 변환
        this.memberInfo = new MemberInfoResponse(
            member.getId(),
            member.getImageUrl(),
            member.getUsername()
        );
    }

}
