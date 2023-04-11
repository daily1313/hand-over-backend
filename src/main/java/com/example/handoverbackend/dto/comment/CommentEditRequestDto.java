package com.example.handoverbackend.dto.comment;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@ApiOperation(value = "댓글 수정 요청")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentEditRequestDto {

    @ApiModelProperty(value = "댓글", notes = "댓글을 입력해주세요.", required = true, example = "example comment")
    @NotBlank(message = "댓글을 입력해주세요.")
    private String content;
}
