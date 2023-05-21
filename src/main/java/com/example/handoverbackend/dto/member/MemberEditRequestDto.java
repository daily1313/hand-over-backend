package com.example.handoverbackend.dto.member;

import com.example.handoverbackend.domain.member.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberEditRequestDto {

    @NotBlank(message = "사용자 별명을 입력해주세요.")
    @Size(min = 2, message = "사용자 별명이 너무 짧습니다.")
    private String nickname;

    @NotBlank(message = "변경하실 비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "비밀번호는 최소 8자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
    private String password;
}
