package com.example.handoverbackend.dto.match;

import com.example.handoverbackend.domain.match.Category;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MatchEditRequestDto {

    @ApiModelProperty(value = "수정할 카테고리 선택", notes = "수정할 카테고리 선택", required = true)
    private Category category;

    @ApiModelProperty(value = "판매할 티켓 이름 작성", notes = "수정할 티켓 이름을 작성", required = true)
    @NotBlank(message = "수정할 티켓 이름을 작성해주세요.")
    private String matchName;

    @ApiModelProperty(value = "수정할 주소 입력", notes = "수정할 주소를 입력", required = true)
    @NotBlank(message = "수정할 티켓의 주소를 입력해주세요.")
    private String address;

    @ApiModelProperty(value = "시작 날짜와 시간 입력", notes = "시작 날짜를 입력", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "마지막 날짜와 시간 입력", notes = "마지막 날짜를 입력", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "수정할 상세 정보 입력", notes = "수정할 상세 정보를 입력", required = true)
    @NotBlank(message = "수정할 상세 정보를 입력해주세요.")
    private String detailsContent;

    @ApiModelProperty(value = "수정할 가격 입력", notes = "수정할 가격를 입력", required = true)
    @NotNull(message = "수정할 티켓의 가격을 입력해주세요.")
    private int price;

    @ApiModelProperty(value = "주의 사항 입력", notes = "주의 사항을 입력", required = true)
    @NotBlank(message = "매칭에 관한 주의사항을 입력해주세요.")
    private String precaution;
}
