package com.example.handoverbackend.service.comment;

import com.example.handoverbackend.domain.comment.MatchComment;
import com.example.handoverbackend.domain.match.Match;
import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.comment.CommentEditRequestDto;
import com.example.handoverbackend.dto.comment.MatchCommentCreateRequestDto;
import com.example.handoverbackend.dto.comment.MatchCommentFindAllWithPagingResponseDto;
import com.example.handoverbackend.dto.comment.MatchCommentWithMatchNumber;
import com.example.handoverbackend.exception.CommentNotFoundException;
import com.example.handoverbackend.exception.MatchNotFoundException;
import com.example.handoverbackend.exception.MemberNotEqualsException;
import com.example.handoverbackend.factory.MatchCommentMaker;
import com.example.handoverbackend.factory.MatchMaker;
import com.example.handoverbackend.repository.comment.MatchCommentRepository;
import com.example.handoverbackend.repository.match.MatchRepository;
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

import static com.example.handoverbackend.factory.MatchMaker.createMatch;
import static com.example.handoverbackend.factory.MemberMaker.createMember;
import static com.example.handoverbackend.factory.MemberMaker.createMember2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MatchCommentServiceTest {

    @InjectMocks
    MatchCommentService matchCommentService;
    @Mock
    MatchRepository matchRepository;
    @Mock
    MatchCommentRepository matchCommentRepository;

    private static final String SUCCESS_CREATE_COMMENT = "댓글을 작성하였습니다.";
    private static final String SUCCESS_UPDATE_COMMENT = "댓글을 수정하였습니다.";
    private static final String SUCCESS_DELETE_COMMENT = "댓글을 삭제하였습니다.";

    @DisplayName("매칭의 댓글 조회")
    @Test
    void findMatchComments() {
        //given
        Long number = 0L;
        Integer page = 0;
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("id").descending());
        Match match = createMatch();
        Member member = createMember();
        List<MatchComment> matchComments = List.of(MatchCommentMaker.createComment(match, member));
        Page<MatchComment> commentsWithPaging = new PageImpl<>(matchComments);
        given(matchCommentRepository.findAllByMatch(match, pageRequest)).willReturn(commentsWithPaging);
        given(matchRepository.findById(number)).willReturn(Optional.of(match));

        //when
        MatchCommentFindAllWithPagingResponseDto result = matchCommentService.findMatchComments(number, page);

        //then
        assertThat(result.getComments().size()).isEqualTo(1);
    }

    @DisplayName("댓글 작성")
    @Test
    void createMatchComment() {
        //given
        MatchCommentCreateRequestDto requestDto = new MatchCommentCreateRequestDto(1L, "content");
        Match match = createMatch();
        Member member = createMember();
        given(matchRepository.findById(1L)).willReturn(Optional.of(match));

        //when
        String result = matchCommentService.createMatchComment(requestDto, member);

        //then
        assertThat(result).isEqualTo(SUCCESS_CREATE_COMMENT);
    }

    @DisplayName("댓글 수정")
    @Test
    void editMatchComment() {
        //given
        CommentEditRequestDto requestDto = new CommentEditRequestDto("change");
        Match match = createMatch();
        Member member = createMember();
        Long commentId = 1L;
        MatchComment matchComment = MatchCommentMaker.createComment(match, member);
        given(matchCommentRepository.findById(commentId)).willReturn(Optional.of(matchComment));

        //when
        String result = matchCommentService.editMatchComment(requestDto, member, commentId);

        //then
        assertThat(result).isEqualTo(SUCCESS_UPDATE_COMMENT);
    }

    @DisplayName("댓글 삭제")
    @Test
    void deleteMatchComment() {
        //given
        Match match = MatchMaker.createMatch();
        Member member = createMember();
        Long commentId = 1L;
        MatchComment matchComment = MatchCommentMaker.createComment(match, member);

        given(matchCommentRepository.findById(commentId)).willReturn(Optional.of(matchComment));

        //when
        String result = matchCommentService.deleteMatchComment(member, commentId);

        //then
        assertThat(result).isEqualTo(SUCCESS_DELETE_COMMENT);
    }

    @DisplayName("본인의 댓글이 아닌 다른 댓글을 수정하려할시 예외발생")
    @Test
    void memberNotEqualsException() {
        //given
        CommentEditRequestDto requestDto = new CommentEditRequestDto("change");
        Match match = createMatch();
        Member member = createMember();
        Member member2 = createMember2();
        Long commentId = 1L;
        MatchComment matchComment = MatchCommentMaker.createComment(match, member2);
        given(matchCommentRepository.findById(commentId)).willReturn(Optional.of(matchComment));

        //when, then
        assertThatThrownBy(() -> matchCommentService.editMatchComment(requestDto, member, commentId))
            .isInstanceOf(MemberNotEqualsException.class);
    }

    @DisplayName("매칭이 존재하지 않을 경우 예외발생")
    @Test
    void matchNotExistException() {
        //given
        MatchCommentCreateRequestDto requestDto = new MatchCommentCreateRequestDto(1L, "content");
        Member member = createMember();
        given(matchRepository.findById(1L)).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> matchCommentService.createMatchComment(requestDto, member))
            .isInstanceOf(MatchNotFoundException.class);
    }

    @DisplayName("댓글이 존재하지 않을경우 예외발생")
    @Test
    void matchCommentNotExistException() {
        //given
        Member member = createMember();
        Long commentId = 1L;
        given(matchCommentRepository.findById(commentId)).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> matchCommentService.deleteMatchComment(member, commentId))
            .isInstanceOf(CommentNotFoundException.class);
    }
}
