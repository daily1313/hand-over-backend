package com.example.handoverbackend.advice;

import com.example.handoverbackend.exception.EmailAlreadyExistException;
import com.example.handoverbackend.exception.LoginFailureException;
import com.example.handoverbackend.exception.MemberNotFoundException;

import com.example.handoverbackend.exception.NicknameAlreadyExistException;
import com.example.handoverbackend.exception.UsernameAlreadyExistException;
import com.example.handoverbackend.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    //404 회원 정보 없음
    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response userNotFoundException() {
        return Response.failure(404,"회원을 찾을 수 없습니다.");
    }

    // 401 응답
    // 아이디 혹은 비밀번호 오류시 발생
    @ExceptionHandler(LoginFailureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response loginFailureException() {
        return Response.failure(401, "로그인에 실패하였습니다.");
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
    // email 중복(이메일 중복)
    @ExceptionHandler(EmailAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response memberEmailAlreadyExistsException(EmailAlreadyExistException e) {
        return Response.failure(409, e.getMessage() + "은 중복된 이메일 입니다.");
    }




}
