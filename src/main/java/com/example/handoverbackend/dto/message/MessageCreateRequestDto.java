package com.example.handoverbackend.dto.message;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageCreateRequestDto {

    @ApiModelProperty(value = "쪽지 제목", notes = "쪽지 제목을 입력해주세요.", required = true, example = "제목")
    @NotBlank(message = "쪽지 제목을 입력해주세요.")
    private String title;

    @ApiModelProperty(value = "쪽지 내용", notes = "쪽지 내용을 입력해주세요.", required = true, example = "내용")
    @NotBlank(message = "메세지 내용을 입력해주세요.")
    private String content;

    @ApiModelProperty(value = "수신자", notes = "수신자 아이디를 입력해주세요.", required = true, example = "poll9999")
    @NotBlank(message = "수신자 아이디를 입력해주세요.")
    private String receiverUsername;
}
