package com.example.handoverbackend.service.comment;

import com.example.handoverbackend.domain.board.Board;
import com.example.handoverbackend.domain.comment.Comment;
import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.comment.*;
import com.example.handoverbackend.dto.page.PageInfoDto;
import com.example.handoverbackend.exception.BoardNotFoundException;
import com.example.handoverbackend.exception.CommentNotFoundException;
import com.example.handoverbackend.exception.MemberNotEqualsException;
import com.example.handoverbackend.repository.board.BoardRepository;
import com.example.handoverbackend.repository.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final String DEFAULT_PAGE_SORT = "id";
    private static final String SUCCESS_CREATE_COMMENT = "댓글을 작성하였습니다.";
    private static final String SUCCESS_UPDATE_COMMENT = "댓글을 수정하였습니다.";
    private static final String SUCCESS_DELETE_COMMENT = "댓글을 삭제하였습니다.";

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public CommentFindAllWithPagingResponseDto findComments(Long boardId, Integer page) {
        Board board = findBoard(boardId);
        PageRequest pageRequest = getPageRequest(page);
        Page<Comment> comments = commentRepository.findAllByBoard(board, pageRequest);
        List<CommentResponseDto> commentsWithDto = comments.stream()
            .map(CommentResponseDto::toDto)
            .toList();
        return CommentFindAllWithPagingResponseDto.toDto(commentsWithDto, new PageInfoDto(comments));
    }

    @Transactional
    public String createComment(CommentCreateRequestDto requestDto, Member member) {
        Board board = findBoard(requestDto.getBoardId());
        Comment comment = new Comment(board, member, requestDto.getContent());
        commentRepository.save(comment);
        return SUCCESS_CREATE_COMMENT;
    }

    @Transactional
    public String editComment(CommentEditRequestDto requestDto, Member member, Long commentId) {
        Comment comment = findComment(commentId);
        validateCommentWriter(comment, member);
        comment.update(requestDto.getContent());
        return SUCCESS_UPDATE_COMMENT;
    }

    @Transactional
    public String deleteComment(Member member, Long commentId) {
        Comment comment = findComment(commentId);
        validateCommentWriter(comment, member);
        commentRepository.delete(comment);
        return SUCCESS_DELETE_COMMENT;
    }

    private Board findBoard(Long id) {
        return boardRepository.findById(id)
            .orElseThrow(BoardNotFoundException::new);
    }

    private Comment findComment(Long id) {
        return commentRepository.findById(id)
            .orElseThrow(CommentNotFoundException::new);
    }

    private void validateCommentWriter(Comment comment, Member member) {
        if (!comment.isOwnComment(member)) {
            throw new MemberNotEqualsException();
        }
    }

    private PageRequest getPageRequest(Integer page) {
        return PageRequest.of(page, DEFAULT_PAGE_SIZE, Sort.by(DEFAULT_PAGE_SORT)
            .descending());
    }
}
