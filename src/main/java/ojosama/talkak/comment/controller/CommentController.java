package ojosama.talkak.comment.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import ojosama.talkak.comment.dto.CommentRequest;
import ojosama.talkak.comment.dto.CommentResponse;
import ojosama.talkak.comment.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/videos/{videoId}/comments")
@RequiredArgsConstructor
public class CommentController implements CommentApiController {

    private final CommentService commentService;


    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
        @PathVariable("videoId") Long videoId, @RequestBody CommentRequest commentRequest,
        Authentication authentication) {
        Long memberId = Long.valueOf(authentication.getPrincipal().toString());
        return ResponseEntity.ok(commentService.createComment(videoId, memberId, commentRequest));
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getCommentsByVideoId(@PathVariable Long videoId) {
        List<CommentResponse> comments = commentService.getCommentsByVideoId(videoId);
        return ResponseEntity.ok().body(comments);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<String> updateComment(
        @PathVariable("commentId") Long commentId, @RequestBody CommentRequest commentRequest,
        Authentication authentication) {
        Long memberId = Long.valueOf(authentication.getPrincipal().toString());
        commentService.updateComment(commentId, memberId, commentRequest);
        return ResponseEntity.ok("Comment updated successfully");
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(
        @PathVariable("commentId") Long commentId,
        Authentication authentication) {
        Long memberId = Long.valueOf(authentication.getPrincipal().toString());
        commentService.deleteComment(commentId, memberId);
        return ResponseEntity.ok("Comment deleted successfully");
    }
}
