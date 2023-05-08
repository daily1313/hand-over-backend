package com.example.handoverbackend.dto.comment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MatchCommentWithMatchNumber {

    @NotNull(message = "매칭 번호를 입력해주세요.")
    @PositiveOrZero(message = "올바른 매칭 번호를 입력해주세요.")
    private Long matchId;
}
