package com.example.handoverbackend.domain.match;

public enum Category {

    노인돌봄("노인돌봄"),
    아이돌봄("아이돌봄"),
    반려동물("반려동물"),
    기타("기타");

    private final String value;

    Category(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
