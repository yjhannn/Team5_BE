package ojosama.talkak.common.config;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import ojosama.talkak.category.domain.Category;
import ojosama.talkak.category.domain.CategoryType;
import ojosama.talkak.category.repository.CategoryRepository;
import ojosama.talkak.comment.domain.Comment;
import ojosama.talkak.comment.repository.CommentRepository;
import ojosama.talkak.common.util.HashConverter;
import ojosama.talkak.common.util.RedisUtil;
import ojosama.talkak.member.domain.Member;
import ojosama.talkak.member.repository.MemberRepository;
import ojosama.talkak.recommendation.domain.VideoInfo;
import ojosama.talkak.recommendation.key.VideoKey;
import ojosama.talkak.recommendation.repository.VideoInfoRepository;
import ojosama.talkak.video.domain.Video;
import ojosama.talkak.video.repository.VideoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

//@Profile("local")
@Component
public class DemoDataInitializer {
    private final MemberRepository memberRepository;
    private final VideoRepository videoRepository;
    private final VideoInfoRepository videoInfoRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final HashConverter hashConverter;
    private final RedisUtil redisUtil;
    private final List<String> thumbnailUrls = Arrays.asList(
        "https://i.ytimg.com/vi/93KBXzJduBk/default.jpg",
        "https://i.ytimg.com/vi/z06UCIGlY8U/default.jpg",
        "https://i.ytimg.com/vi/UDea058tJ9A/default.jpg",
        "https://i.ytimg.com/vi/rp6NN525gfQ/default.jpg",
        "https://i.ytimg.com/vi/mCfCQHKEV8U/default.jpg"
    );
    private final Random random = new Random();

    public DemoDataInitializer(MemberRepository memberRepository, VideoRepository videoRepository,
        VideoInfoRepository videoInfoRepository, CategoryRepository categoryRepository,
        CommentRepository commentRepository, HashConverter hashConverter, RedisUtil redisUtil) {
        this.memberRepository = memberRepository;
        this.videoRepository = videoRepository;
        this.videoInfoRepository = videoInfoRepository;
        this.categoryRepository = categoryRepository;
        this.commentRepository = commentRepository;
        this.hashConverter = hashConverter;
        this.redisUtil = redisUtil;
    }

    @PostConstruct
    public void init() {
        // 멤버 데이터
        Member member1 = Member.of("user1", "image1.jpg", "user1@example.com");
        Member member2 = Member.of("user2", "image2.jpg", "user2@example.com");
        memberRepository.save(member1);
        memberRepository.save(member2);
        List<Member> members = List.of(member1, member2);

        // 카테고리 데이터
        Arrays.stream(CategoryType.values())
            .filter(type -> categoryRepository.findByCategoryType(type).isEmpty())
            .forEach(type -> categoryRepository.save(new Category(type)));
        List<Category> categories = categoryRepository.findAll();


        // 비디오 데이터
        int thumbnailIndex = 0;
        for (Category category : categories) {
            for (int i = 0; i < 10; i++) {
                String title = category.getCategoryType().getName() + " Video " + (i + 1);
                Long views = random.nextLong(1000); // 조회수 랜덤 설정
                Long likes = random.nextLong(100); // 좋아요 수 랜덤 설정
                String thumbnail = thumbnailUrls.get(thumbnailIndex); // 썸네일 설정

                // 생성자를 통해 모든 값 설정
                Video video = new Video(title, member1.getId(), category.getId(),
                    thumbnail, views, likes);
                videoRepository.save(video);

                // 댓글 생성 (2~3개)
                int commentCount = new Random().nextInt(2) + 2; // 2~3개의 댓글 생성
                for (int j = 0; j < commentCount; j++) {
                    Comment comment = new Comment(member1, video, "댓글 내용 " + j);
                    commentRepository.save(comment);
                }

                // Redis에 VideoInfo 저장
                VideoInfo videoInfo = VideoInfo.of(video.getCreatedAt(), video.getViews(),
                    video.getCountLikes());
                videoInfoRepository.save(category.getId(), video.getId(), videoInfo);

                // 썸네일 인덱스 증가 (순환)
                thumbnailIndex = (thumbnailIndex + 1) % thumbnailUrls.size();
            }
        }
    }
}
