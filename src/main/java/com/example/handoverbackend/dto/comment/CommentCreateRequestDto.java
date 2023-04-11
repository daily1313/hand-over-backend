package com.example.handoverbackend.dto.comment;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiOperation(value = "댓글 생성 요청")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateRequestDto {

    @ApiModelProperty(value = "게시글 번호", notes = "게시글 번호를 입력해주세요.", required = true, example = "1")
    @NotNull(message = "게시글 번호를 입력해주세요.")
    @Positive(message = "게시글 번호를 입력해주세요.")
    private Long boardId;

    @ApiModelProperty(value = "댓글", notes = "댓글을 입력해주세요.", required = true, example = "example comment")
    @NotBlank(message = "댓글을 입력해주세요.")
    private String content;
}
