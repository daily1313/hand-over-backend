package com.example.handoverbackend.controller.comment;

import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.comment.CommentEditRequestDto;
import com.example.handoverbackend.dto.comment.MatchCommentCreateRequestDto;
import com.example.handoverbackend.dto.comment.MatchCommentWithMatchNumber;
import com.example.handoverbackend.exception.MemberNotEqualsException;
import com.example.handoverbackend.repository.MemberRepository;
import com.example.handoverbackend.response.Response;
import com.example.handoverbackend.service.comment.MatchCommentService;
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

@Api(value = "MatchComment Controller", tags = "MatchComment")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/match/comments")
public class MatchCommentController {

    private static final String DEFAULT_PAGE = "0";

    private final MatchCommentService matchCommentService;
    private final MemberRepository memberRepository;

    @Operation(summary = "매칭의 댓글 조회", description = "매칭의 댓글을 조회합니다.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Response findMatchComments(@Valid @RequestBody MatchCommentWithMatchNumber requestDto
        , @RequestParam(defaultValue = DEFAULT_PAGE) Integer page) {
        return Response.success(matchCommentService.findMatchComments(requestDto, page));
    }

    @Operation(summary = "댓글을 작성합니다.", description = "댓글을 작성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response createMatchComment(@Valid @RequestBody MatchCommentCreateRequestDto requestDto) {
        return Response.success(matchCommentService.createMatchComment(requestDto, getPrincipal()));
    }

    @Operation(summary = "댓글을 수정합니다.", description = "댓글을 수정합니다.")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response editComment(@ApiParam(value = "댓글 id", readOnly = true) @PathVariable Long id
        , @Valid @RequestBody CommentEditRequestDto requestDto) {
        return Response.success(matchCommentService.editMatchComment(requestDto, getPrincipal(), id));
    }

    @Operation(summary = "댓글을 삭제합니다.", description = "댓글을 삭제합니다.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteComment(@ApiParam(value = "댓글 id", readOnly = true) @PathVariable Long id) {
        return Response.success(matchCommentService.deleteMatchComment(getPrincipal(), id));
    }

    private Member getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return memberRepository.findByUsername(authentication.getName())
            .orElseThrow(MemberNotEqualsException::new);
    }
}
