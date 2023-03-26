package com.example.handoverbackend.exception;

public class CategoryAlreadyExistException extends RuntimeException{
    public CategoryAlreadyExistException(String message) {
        super(message);
    }
}
