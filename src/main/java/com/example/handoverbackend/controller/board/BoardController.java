package com.example.handoverbackend.controller.board;

import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.board.BoardCreateRequestDto;
import com.example.handoverbackend.dto.board.BoardUpdateRequestDto;
import com.example.handoverbackend.exception.MemberNotFoundException;
import com.example.handoverbackend.repository.MemberRepository;
import com.example.handoverbackend.response.Response;
import com.example.handoverbackend.service.board.BoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Api(value = "Board Controller", tags = "Board")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class BoardController {

    private static final String DEFAULT_PAGE = "0";

    private final MemberRepository memberRepository;
    private final BoardService boardService;

    @ApiOperation(value = "게시글 생성", notes = "게시글을 작성합니다.")
    @PostMapping("/boards")
    @ResponseStatus(HttpStatus.CREATED)
    public Response createBoard(@Valid @ModelAttribute BoardCreateRequestDto boardCreateRequestDto
        , @RequestParam(value = "category", required = false) Long categoryId) {
        return Response.success(boardService.createBoard(boardCreateRequestDto, getPrincipal(), categoryId));
    }

    @ApiOperation(value = "게시글 목록 조회", notes = "게시글 목록을 조회합니다.")
    @GetMapping("/boards/all{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public Response findAllBoardsWithCategory(@ApiParam(value = "카테고리 id", required = true) @PathVariable Long categoryId,
                                              @RequestParam(defaultValue = DEFAULT_PAGE) Integer page) {
        return Response.success(boardService.findAllBoardsWithCategory(page, categoryId));
    }

    @ApiOperation(value = "게시글 전체 조회", notes = "게시글 전체를 조회합니다.")
    @GetMapping("/boards")
    @ResponseStatus(HttpStatus.OK)
    public Response findAllBoards(@RequestParam(defaultValue = DEFAULT_PAGE) Integer page) {
        return Response.success(boardService.findAllBoards(page));
    }

    @ApiOperation(value = "게시글 단건 조회", notes = "게시글을 단건 조회합니다.")
    @GetMapping("/boards/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response findBoard(@ApiParam(value = "게시글 id", readOnly = true) @PathVariable Long id) {
        return Response.success(boardService.findBoard(id));
    }

    @ApiOperation(value = "게시글 수정", notes = "게시글을 수정합니다.")
    @PatchMapping("boards/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response editBoard(@ApiParam(value = "게시글 id", readOnly = true) @PathVariable Long id
        , @Valid @ModelAttribute BoardUpdateRequestDto boardUpdateRequestDto) {
        return Response.success(boardService.editBoard(id, boardUpdateRequestDto, getPrincipal()));
    }

    @ApiOperation(value = "게시글 삭제", notes = "게시글을 삭제합니다.")
    @DeleteMapping("/boards/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteBoard(@ApiParam(value = "게시글 id", required = true) @PathVariable Long id) {
        return Response.success(boardService.deleteBoard(id, getPrincipal()));
    }

    @ApiOperation(value = "게시글 즐겨찾기", notes = "사용자가 게시글 즐겨찾기를 누릅니다.")
    @PostMapping("/boards/{id}/favorites")
    @ResponseStatus(HttpStatus.OK)
    public Response favoriteBoard(@ApiParam(value = "게시글 id", required = true) @PathVariable Long id){
        return Response.success(boardService.updateFavoriteBoard(id, getPrincipal()));
    }

    @ApiOperation(value = "즐겨찾기 게시판을 조회", notes = "즐겨찾기로 등록한 게시판을 조회합니다.")
    @GetMapping("/boards/favorite")
    @ResponseStatus(HttpStatus.OK)
    public Response findFavoriteBoards(@RequestParam(defaultValue = DEFAULT_PAGE)Integer page){
        return Response.success(boardService.findFavoriteBoards(page, getPrincipal()));
    }

    @ApiOperation(value = "개시글 검색", notes = "게시글을 검색합니다.")
    @GetMapping("/boards/search/{keyword}")
    @ResponseStatus(HttpStatus.OK)
    public Response searchBoard(@PathVariable String keyword, @RequestParam(defaultValue = DEFAULT_PAGE) Integer page){
        return Response.success(boardService.searchBoard(keyword, page));
    }

    private Member getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return memberRepository.findByUsername(authentication.getName())
            .orElseThrow(MemberNotFoundException::new);
    }
}
