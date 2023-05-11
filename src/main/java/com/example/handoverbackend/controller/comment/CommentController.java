package com.example.handoverbackend.controller.comment;

import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.comment.CommentCreateRequestDto;
import com.example.handoverbackend.dto.comment.CommentEditRequestDto;
import com.example.handoverbackend.dto.comment.CommentWithBoardNumber;
import com.example.handoverbackend.exception.MemberNotEqualsException;
import com.example.handoverbackend.repository.MemberRepository;
import com.example.handoverbackend.response.Response;
import com.example.handoverbackend.service.comment.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Api(value = "Comment Controller", tags = "Comment")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private static final String DEFAULT_PAGE = "0";

    private final CommentService commentService;
    private final MemberRepository memberRepository;

    @Operation(summary = "게시글의 댓글 조회", description = "게시글의 댓글을 조회합니다.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Response findComments(@Valid @RequestBody CommentWithBoardNumber requestDto
        , @RequestParam(defaultValue = DEFAULT_PAGE) Integer page) {
        return Response.success(commentService.findComments(requestDto, page));
    }

    @Operation(summary = "댓글을 작성합니다.", description = "댓글을 작성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response createComment(@Valid @RequestBody CommentCreateRequestDto requestDto) {
        return Response.success(commentService.createComment(requestDto, getPrincipal()));
    }

    @Operation(summary = "댓글을 수정합니다.", description = "댓글을 수정합니다.")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response editComment(@ApiParam(value = "댓글 id", readOnly = true) @PathVariable Long id
        , @Valid @RequestBody CommentEditRequestDto requestDto) {
        return Response.success(commentService.editComment(requestDto, getPrincipal(), id));
    }

    @ApiOperation(value = "댓글을 삭제합니다.", notes = "댓글을 삭제합니다.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteComment(@ApiParam(value = "댓글 id", readOnly = true) @PathVariable Long id) {
        return Response.success(commentService.deleteComment(getPrincipal(), id));
    }

    private Member getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return memberRepository.findByUsername(authentication.getName())
            .orElseThrow(MemberNotEqualsException::new);
    }
}
