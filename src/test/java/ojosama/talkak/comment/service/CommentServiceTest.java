package ojosama.talkak.comment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import ojosama.talkak.comment.dto.CommentRequest;
import ojosama.talkak.comment.dto.CommentResponse;
import ojosama.talkak.comment.domain.Comment;
import ojosama.talkak.comment.repository.CommentRepository;
import ojosama.talkak.common.exception.TalKakException;
import ojosama.talkak.member.domain.Member;
import ojosama.talkak.member.repository.MemberRepository;
import ojosama.talkak.video.domain.Video;
import ojosama.talkak.video.repository.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private CommentRepository commentRepository;

    private Member member;
    private Video video;
    private Comment comment;
    Long memberId = 1L;
    Long videoId = 1L;
    Long commentId = 1L;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        member = Member.of("username", "imageurl", "email");
        member.createIdForTest(1L);
        video = new Video("video title", 0L);
        video.createIdForTest(1L);
        comment = new Comment(member, video, "This is a comment.");
        comment.createIdForTest(1L);
    }

    @Test
    public void testCreateComment() {
        // Given
        CommentRequest commentRequest = new CommentRequest("This is a comment.");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(videoRepository.findById(videoId)).thenReturn(Optional.of(video));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // When
        CommentResponse response = commentService.createComment(memberId, videoId, commentRequest);

        // Then
        assertEquals(commentRequest.content(), response.content());
        assertEquals(member.getId(), response.member().memberId());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    public void testCreateComment_InvalidMember() {
        // Given
        CommentRequest commentRequest = new CommentRequest("This is a comment.");

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());
        when(videoRepository.findById(videoId)).thenReturn(Optional.of(video));

        // When & Then
        TalKakException exception = assertThrows(TalKakException.class, () -> {
            commentService.createComment(memberId, videoId, commentRequest);
        });
        assertEquals("C002", exception.code());
    }

    @Test
    public void testCreateComment_InvalidVideo() {
        // Given
        CommentRequest commentRequest = new CommentRequest("This is a comment.");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(videoRepository.findById(videoId)).thenReturn(Optional.empty());

        // When & Then
        TalKakException exception = assertThrows(TalKakException.class, () -> {
            commentService.createComment(memberId, videoId, commentRequest);
        });
        assertEquals("C003", exception.code());
    }

    @Test
    public void testGetCommentsByVideoId() {
        // Given
        when(commentRepository.findByVideoId(videoId)).thenReturn(Collections.singletonList(comment));
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

        // When
        List<CommentResponse> responses = commentService.getCommentsByVideoId(videoId);

        // Then
        assertEquals(1, responses.size());
        assertEquals(comment.getContent(), responses.get(0).content());
        assertEquals(member.getId(), responses.get(0).member().memberId());
    }

    @Test
    public void testUpdateComment() {
        // Given
        CommentRequest commentRequest = new CommentRequest("Updated comment content.");

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(commentRepository.save(comment)).thenReturn(comment);

        // When
        CommentResponse response = commentService.updateComment(commentId, memberId, commentRequest);

        // Then
        assertEquals(commentRequest.content(), response.content());
        verify(commentRepository).save(comment);
    }

    @Test
    public void testUpdateComment_InvalidComment() {
        // Given
        CommentRequest commentRequest = new CommentRequest("Updated comment content.");

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // When & Then
        TalKakException exception = assertThrows(TalKakException.class, () -> {
            commentService.updateComment(commentId, memberId, commentRequest);
        });
        assertEquals("C004", exception.code());
    }

    @Test
    public void testUpdateComment_UnauthorizedUser() {
        // Given
        Long memberId = 2L;
        CommentRequest commentRequest = new CommentRequest("Updated comment content.");

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(memberRepository.findById(memberId)).thenReturn(
            Optional.of(Member.of("anotherUser", "imageurl", "email")));

        // When & Then
        TalKakException exception = assertThrows(TalKakException.class, () -> {
            commentService.updateComment(commentId, memberId, commentRequest);
        });
        assertEquals("C001", exception.code());
    }

    @Test
    public void testDeleteComment() {
        // Given

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        // When
        commentService.deleteComment(commentId, memberId);

        // Then
        verify(commentRepository).delete(comment);
    }

    @Test
    public void testDeleteComment_InvalidComment() {
        // Given

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // When & Then
        TalKakException exception = assertThrows(TalKakException.class, () -> {
            commentService.deleteComment(commentId, memberId);
        });
        assertEquals("C004", exception.code());
    }

    @Test
    public void testDeleteComment_UnauthorizedUser() {
        // Given
        Long memberId = 2L; // Different member

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(memberRepository.findById(memberId)).thenReturn(
            Optional.of(Member.of( "anotherUser", "imageurl", "email")));

        // When & Then
        TalKakException exception = assertThrows(TalKakException.class, () -> {
            commentService.deleteComment(commentId, memberId);
        });
        assertEquals("C001", exception.code());
    }
}