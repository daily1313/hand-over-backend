package com.example.handoverbackend.advice;

import com.example.handoverbackend.exception.*;
import com.example.handoverbackend.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    //403 권한 정보 없음
    @ExceptionHandler(MemberAccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Response memberAccessDeniedException() {
        return Response.failure(403, "회원을 탈퇴할 권한이 없습니다.");
    }

    //404 회원 정보 없음
    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response userNotFoundException() {
        return Response.failure(404, "회원을 찾을 수 없습니다.");
    }

    //404 이메일 인증 정보 없음
    @ExceptionHandler(EmailAuthNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response emailNotFoundException() {
        return Response.failure(404, "이메일 인증 정보를 찾을 수 없습니다.");
    }

    // 404 응답
    // Image 형식 지원하지 않음
    @ExceptionHandler(UnsupportedImageFormatException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response unsupportedImageFormatException() {
        return Response.failure(404, "이미지 형식을 지원하지 않습니다.");
    }

    // 404 응답
    // 파일 업로드 실패
    @ExceptionHandler(FileUploadFailureException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response fileUploadFailureException(FileUploadFailureException e) {
        log.error("e = {}", e.getMessage());
        return Response.failure(404, "이미지 업로드 실패");
    }

    // 404 응답
    // 카테고리 찾을 수 없음
    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response categoryNotFoundException() {
        return Response.failure(404, "카테고리를 찾을 수 없습니다.");
    }

    // 404 응답
    // 게시글 찾기 실패
    @ExceptionHandler(BoardNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response boardNotFoundException() {
        return Response.failure(404, "게시글을 찾을 수 없습니다.");
    }

    // 404 응답
    // 댓글 찾기 실패
    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response commentNotFoundException() {
        return Response.failure(404, "댓글을 찾을 수 없습니다.");
    }

    // 404 응답
    // 매칭글 찾기 실패
    @ExceptionHandler(MatchNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response matchNotFoundException() {
        return Response.failure(404, "매칭글을 찾을 수 없습니다.");
    }

    // 404 응답
    // 요청한 Favorite 찾을 수 없음
    @ExceptionHandler(FavoriteNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response favoriteNotFoundException() {
        return Response.failure(404, "요청한 즐겨찾기를 찾을 수 없습니다.");
    }

    // 404 응답
    // 요청한 신고기록를 찾을 수 없음
    @ExceptionHandler(NotReportedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response notReportedException() {
        return Response.failure(404, "요청한 신고기록을 찾을 수 없습니다.");
    }

    // 400 응답
    // 자기 자신을 신고할 수 없다.
    @ExceptionHandler(NotSelfReportException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response notSelfReportException() {
        return Response.failure(400, "자기 자신은 신고할 수 없습니다.");
    }

    // 409 응답
    // 이미 매칭완료 처리된 매칭글에 대한 예외
    @ExceptionHandler(AlreadyMatchedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response alreadyMatchedException() {
        return Response.failure(409, "이미 매칭완료 처리된 매칭글입니다.");
    }

    // 409 응답
    // 이미 매칭중인 매칭글에 대한 예외
    @ExceptionHandler(AlreadyMatchingException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response alreadyMatchingException() {
        return Response.failure(409, "이미 매칭중인 매칭글입니다.");
    }

    // 409 응답
    // 이미 신고 기록이 있어서 신고 실패
    @ExceptionHandler(AlreadyReportException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response alreadyReportException() {
        return Response.failure(409, "이미 신고를 했습니다.");
    }

    // 401 응답
    // 아이디 혹은 비밀번호 오류시 발생
    @ExceptionHandler(LoginFailureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response loginFailureException() {
        return Response.failure(401, "로그인에 실패하였습니다.");
    }


    // 401 응답
    // 이메일 인증 정보 가 일치하지 않음
    @ExceptionHandler(EmailAuthNotEqualsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response emailAuthNotEqualsException() {
        return Response.failure(401, "메일 인증 정보가 일치하지 않습니다.");
    }

    // 401 응답
    // 요청자와 요청한 유저의 정보가 일치하지 않을시에 발생
    @ExceptionHandler(MemberNotEqualsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response memberNotEqualsException() {
        return Response.failure(401, "유저 정보가 일치하지 않습니다.");
    }

    // 409 응답
    // username 중복(아이디 중복)
    @ExceptionHandler(UsernameAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response memberEmailAlreadyExistsException(UsernameAlreadyExistException e) {
        return Response.failure(409, e.getMessage() + "은 중복된 아이디 입니다.");
    }

    // 409 응답
    // nickname 중복(별명 중복)
    @ExceptionHandler(NicknameAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response memberNicknameAlreadyExistsException(NicknameAlreadyExistException e) {
        return Response.failure(409, e.getMessage() + "은 중복된 별명 입니다.");
    }

    // 409 응답
    // 카테고리 중복
    @ExceptionHandler(CategoryAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response CategoryAlreadyExistsException(CategoryAlreadyExistException e) {
        return Response.failure(409, e.getMessage() + "은 중복된 카테고리 입니다.");
    }

    // 409 응답
    // email 중복(이메일 중복)
    @ExceptionHandler(EmailAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response memberEmailAlreadyExistsException(EmailAlreadyExistException e) {
        return Response.failure(409, e.getMessage() + "은 중복된 이메일 입니다.");
    }


}
