package com.example.handoverbackend.service.comment;

import com.example.handoverbackend.domain.board.Board;
import com.example.handoverbackend.domain.comment.Comment;
import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.comment.CommentCreateRequestDto;
import com.example.handoverbackend.dto.comment.CommentEditRequestDto;
import com.example.handoverbackend.dto.comment.CommentFindAllWithPagingResponseDto;
import com.example.handoverbackend.dto.comment.CommentWithBoardNumber;
import com.example.handoverbackend.exception.BoardNotFoundException;
import com.example.handoverbackend.exception.CommentNotFoundException;
import com.example.handoverbackend.exception.MemberNotEqualsException;
import com.example.handoverbackend.factory.CommentMaker;
import com.example.handoverbackend.repository.board.BoardRepository;
import com.example.handoverbackend.repository.comment.CommentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static com.example.handoverbackend.factory.BoardMaker.createBoard;
import static com.example.handoverbackend.factory.MemberMaker.createMember;
import static com.example.handoverbackend.factory.MemberMaker.createMember2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @InjectMocks
    CommentService commentService;
    @Mock
    CommentRepository commentRepository;
    @Mock
    BoardRepository boardRepository;

    private static final String SUCCESS_CREATE_COMMENT = "댓글을 작성하였습니다.";
    private static final String SUCCESS_UPDATE_COMMENT = "댓글을 수정하였습니다.";
    private static final String SUCCESS_DELETE_COMMENT = "댓글을 삭제하였습니다.";

    @DisplayName("게시글의 댓글 조회")
    @Test
    void findComments() {
        //given
        Long number = 1L;
        Integer page = 0;
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("id").descending());
        Board board = createBoard();
        Member member = createMember();
        List<Comment> comments = List.of(CommentMaker.createComment(board, member));
        Page<Comment> commentsWithPaging = new PageImpl<>(comments);
        given(commentRepository.findAllByBoard(board, pageRequest)).willReturn(commentsWithPaging);
        given(boardRepository.findById(number)).willReturn(Optional.of(board));

        //when
        CommentFindAllWithPagingResponseDto result = commentService.findComments(number, page);

        //then
        assertThat(result.getComments().size()).isEqualTo(1);
    }

    @DisplayName("댓글 작성")
    @Test
    void createComment() {
        //given
        CommentCreateRequestDto requestDto = new CommentCreateRequestDto(1L, "content");
        Board board = createBoard();
        Member member = createMember();
        given(boardRepository.findById(1L)).willReturn(Optional.of(board));

        //when
        String result = commentService.createComment(requestDto, member);

        //then
        assertThat(result).isEqualTo(SUCCESS_CREATE_COMMENT);
    }

    @DisplayName("댓글 수정")
    @Test
    void editComment() {
        //given
        CommentEditRequestDto requestDto = new CommentEditRequestDto("change");
        Board board = createBoard();
        Member member = createMember();
        Long commentId = 1L;
        Comment comment = CommentMaker.createComment(board, member);
        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        //when
        String result = commentService.editComment(requestDto, member, commentId);

        //then
        assertThat(result).isEqualTo(SUCCESS_UPDATE_COMMENT);
    }

    @DisplayName("댓글 삭제")
    @Test
    void deleteComment() {
        //given
        Board board = createBoard();
        Member member = createMember();
        Long commentId = 1L;
        Comment comment = CommentMaker.createComment(board, member);
        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        //when
        String result = commentService.deleteComment(member, commentId);

        //then
        assertThat(result).isEqualTo(SUCCESS_DELETE_COMMENT);
    }

    @DisplayName("본인의 댓글이 아닌 다른 댓글을 수정하려할시 예외발생")
    @Test
    void memberNotEqualsException() {
        //given
        CommentEditRequestDto requestDto = new CommentEditRequestDto("change");
        Board board = createBoard();
        Member member = createMember();
        Member member2 = createMember2();
        Long commentId = 1L;
        Comment comment = CommentMaker.createComment(board, member2);
        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        //when, then
        assertThatThrownBy(() -> commentService.editComment(requestDto, member, commentId))
            .isInstanceOf(MemberNotEqualsException.class);
    }

    @DisplayName("게시판이 존재하지 않을 경우 예외발생")
    @Test
    void boardNotExistException() {
        //given
        CommentCreateRequestDto requestDto = new CommentCreateRequestDto(1L, "content");
        Member member = createMember();
        given(boardRepository.findById(1L)).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> commentService.createComment(requestDto, member))
            .isInstanceOf(BoardNotFoundException.class);
    }

    @DisplayName("댓글이 존재하지 않을경우 예외발생")
    @Test
    void commentNotExistException() {
        //given
        Board board = createBoard();
        Member member = createMember();
        Long commentId = 1L;
        Comment comment = CommentMaker.createComment(board, member);
        given(commentRepository.findById(commentId)).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> commentService.deleteComment(member, commentId))
            .isInstanceOf(CommentNotFoundException.class);
    }
}
