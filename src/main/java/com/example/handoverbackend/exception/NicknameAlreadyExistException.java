package com.example.handoverbackend.exception;

public class NicknameAlreadyExistException extends RuntimeException {

    public NicknameAlreadyExistException(String message) {
        super(message);
    }
}
