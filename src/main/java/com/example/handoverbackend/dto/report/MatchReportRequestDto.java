package com.example.handoverbackend.dto.report;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiOperation(value = "매칭 신고 처리 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchReportRequestDto {

    @ApiModelProperty(value = "신고 할 매칭 아이디", notes = "신고할 게시글 매칭을 입력해주세요.", required = true, example = "1")
    @NotNull(message = "신고할 매칭의 아이디를 입력해주세요.")
    private Long reportedMatchId;

    @ApiModelProperty(value = "신고 사유", notes = "신고 사유를 입력해주세요.", required = true, example = "Insulting")
    @NotBlank(message = "신고 사유를 입력하세요. ")
    private String content;
}

