package com.example.handoverbackend.domain.ticket;

public enum Category {
    숙소("숙소"),
    캠핑("캠핑"),
    공연("공연"),
    기타("기타");

    private final String value;

    Category(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
