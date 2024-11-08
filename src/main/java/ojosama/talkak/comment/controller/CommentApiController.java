package ojosama.talkak.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import ojosama.talkak.comment.dto.CommentRequest;
import ojosama.talkak.comment.dto.CommentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "댓글 API", description = "댓글 관련 API를 담당합니다.")
public interface CommentApiController {

    @Operation(summary = "댓글 생성", description = "댓글을 생성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "댓글 생성 성공"),
        @ApiResponse(responseCode = "C001", description = "인증되지 않은 유저입니다."),
        @ApiResponse(responseCode = "C002", description = "회원을 조회할 수 없습니다."),
        @ApiResponse(responseCode = "C003", description = "존재하지 않는 videoId 입니다.")
    })
    ResponseEntity<CommentResponse> createComment(@PathVariable("videoId") Long videoId,
        @RequestBody CommentRequest commentRequest, Authentication authentication);

    @Operation(summary = "댓글 조회", description = "영상의 모든 댓글을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "댓글 생성 성공"),
        @ApiResponse(responseCode = "C001", description = "인증되지 않은 유저입니다."),
        @ApiResponse(responseCode = "C002", description = "회원을 조회할 수 없습니다."),
        @ApiResponse(responseCode = "C003", description = "존재하지 않는 videoId 입니다."),
        @ApiResponse(responseCode = "C004", description = "존재하지 않는 commentId 입니다.")
    })
    ResponseEntity<List<CommentResponse>> getCommentsByVideoId(@PathVariable Long videoId);

    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "댓글 생성 성공"),
        @ApiResponse(responseCode = "C001", description = "인증되지 않은 유저입니다."),
        @ApiResponse(responseCode = "C002", description = "회원을 조회할 수 없습니다."),
        @ApiResponse(responseCode = "C003", description = "존재하지 않는 videoId 입니다."),
        @ApiResponse(responseCode = "C004", description = "존재하지 않는 commentId 입니다.")
    })
    ResponseEntity<String> updateComment(@PathVariable("commentId") Long commentId,
        @RequestBody CommentRequest commentRequest,
        Authentication authentication);

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "댓글 생성 성공"),
        @ApiResponse(responseCode = "C001", description = "인증되지 않은 유저입니다."),
        @ApiResponse(responseCode = "C002", description = "회원을 조회할 수 없습니다."),
        @ApiResponse(responseCode = "C003", description = "존재하지 않는 videoId 입니다."),
        @ApiResponse(responseCode = "C004", description = "존재하지 않는 commentId 입니다.")
    })
    ResponseEntity<String> deleteComment(@PathVariable("commentId") Long commentId,
        Authentication authentication);


}
