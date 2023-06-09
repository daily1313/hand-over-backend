package com.example.handoverbackend.dto.jwt;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "회원가입 요청")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {

    @ApiModelProperty(value = "아이디", notes = "아이디를 입력해주세요", required = true, example = "kimsb7218")
    @NotBlank(message = "아이디를 입력해주세요.")
    private String username;

    @ApiModelProperty(value = "비밀번호", required = true, example = "123456")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "비밀번호는 최소 8자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
    private String password;

    @ApiModelProperty(value = "이메일", notes = "이메일 형식에 맞춰주세요.", required = true, example = "kimsb7218@naver.com")
    @Email(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "유효한 이메일 주소를 입력하세요.")
    private String email;

    @ApiModelProperty(value = "사용자 별명", notes = "사용자 별명 한글 또는 알파벳으로 입력해주세요.", required = true, example = "김승범")
    @NotBlank(message = "사용자 별명을 입력해주세요.")
    @Size(min = 2, message = "사용자 별명이 너무 짧습니다.")
    private String nickname;

    @ApiModelProperty(value = "이메일 인증 코드", notes = "이메일 인증코드를 입력해주세요", required = true, example = "321ADS3A")
    @NotBlank(message = "유효한 이메일 인증 코드를 입력해주세요.")
    private String emailAuthKey;

}
