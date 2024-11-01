package ojosama.talkak.video.repository;

import java.util.List;
import ojosama.talkak.video.domain.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    Page<Video> findByCategoryId(Long categoryId, Pageable pageable);
}
