package com.example.handoverbackend.service.comment;

import com.example.handoverbackend.domain.comment.MatchComment;
import com.example.handoverbackend.domain.match.Match;
import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.comment.*;
import com.example.handoverbackend.dto.page.PageInfoDto;
import com.example.handoverbackend.exception.CommentNotFoundException;
import com.example.handoverbackend.exception.MatchNotFoundException;
import com.example.handoverbackend.exception.MemberNotEqualsException;
import com.example.handoverbackend.repository.comment.MatchCommentRepository;
import com.example.handoverbackend.repository.match.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchCommentService {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final String DEFAULT_PAGE_SORT = "id";
    private static final String SUCCESS_CREATE_COMMENT = "댓글을 작성하였습니다.";
    private static final String SUCCESS_UPDATE_COMMENT = "댓글을 수정하였습니다.";
    private static final String SUCCESS_DELETE_COMMENT = "댓글을 삭제하였습니다.";

    private final MatchCommentRepository matchCommentRepository;
    private final MatchRepository matchRepository;

    @Transactional(readOnly = true)
    public MatchCommentFindAllWithPagingResponseDto findMatchComments(MatchCommentWithMatchNumber number, Integer page) {
        Match match = findMatch(number.getMatchId());
        PageRequest pageRequest = getPageRequest(page);
        Page<MatchComment> matchComments = matchCommentRepository.findAllByMatch(match, pageRequest);
        List<MatchCommentResponseDto> commentsWithDto = matchComments.stream()
            .map(MatchCommentResponseDto::toDto)
            .toList();
        return MatchCommentFindAllWithPagingResponseDto.toDto(commentsWithDto, new PageInfoDto(matchComments));
    }

    @Transactional
    public String createMatchComment(MatchCommentCreateRequestDto requestDto, Member member) {
        Match match = findMatch(requestDto.getMatchId());
        MatchComment matchComment = new MatchComment(member, match, requestDto.getContent());
        matchCommentRepository.save(matchComment);
        return SUCCESS_CREATE_COMMENT;
    }

    @Transactional
    public String editMatchComment(CommentEditRequestDto requestDto, Member member, Long commentId) {
        MatchComment matchComment = findMatchComment(commentId);
        validateMatchCommentWriter(matchComment, member);
        matchComment.update(requestDto.getContent());
        return SUCCESS_UPDATE_COMMENT;
    }

    @Transactional
    public String deleteMatchComment(Member member, Long commentId) {
        MatchComment matchComment = findMatchComment(commentId);
        validateMatchCommentWriter(matchComment, member);
        matchCommentRepository.delete(matchComment);
        return SUCCESS_DELETE_COMMENT;
    }

    private Match findMatch(Long id) {
        return matchRepository.findById(id)
            .orElseThrow(MatchNotFoundException::new);
    }

    private MatchComment findMatchComment(Long id) {
        return matchCommentRepository.findById(id)
            .orElseThrow(CommentNotFoundException::new);
    }

    private void validateMatchCommentWriter(MatchComment matchComment, Member member) {
        if (!matchComment.isOwnComment(member)) {
            throw new MemberNotEqualsException();
        }
    }

    private PageRequest getPageRequest(Integer page) {
        return PageRequest.of(page, DEFAULT_PAGE_SIZE, Sort.by(DEFAULT_PAGE_SORT)
            .descending());
    }
}
