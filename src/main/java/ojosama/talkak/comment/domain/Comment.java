package ojosama.talkak.comment.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import ojosama.talkak.common.entity.BaseEntity;
import ojosama.talkak.member.domain.Member;
import ojosama.talkak.video.domain.Video;

@Entity
@Table(name = "comment")
@Getter
public class Comment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;
    private String content;

    public Comment() {
    }

    public Comment(Member member, Video video, String content) {
        this.member = member;
        this.video = video;
        this.content = content;
    }


    public void updateContent(String newContent) {
        this.content = newContent;
    }

}
