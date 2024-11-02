package ojosama.talkak.video.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import ojosama.talkak.comment.domain.Comment;
import ojosama.talkak.common.entity.BaseEntity;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "video")
@Getter
public class Video extends BaseEntity {

    private Long memberId;
    private String title;
    private String videoUrl;
    private String uniqueFileName;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    private Long categoryId;
    private String thumbnail;
    private Boolean isPublic;
    private Long countLikes;
    private Long views = 0L;
    @OneToMany(mappedBy = "video", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments;

    public Video() {

    }

    public Video(String title, Long countLikes) {
        this.title = title;
        this.countLikes = countLikes;
    }

    public Video(String title, Long memberId, Long categoryId, String uniqueFileName) {
        this.title = title;
        this.memberId = memberId;
        this.categoryId = categoryId;
        this.uniqueFileName = uniqueFileName;
    }

    public Integer commentsCount() {
        return comments == null ? 0 : comments.size();
    }
}
