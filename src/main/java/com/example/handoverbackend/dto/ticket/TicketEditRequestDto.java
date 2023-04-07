package com.example.handoverbackend.dto.ticket;

import com.example.handoverbackend.domain.ticket.Category;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TicketEditRequestDto {

    @ApiModelProperty(value = "수정할 카테고리 선택", notes = "수정할 카테고리 선택", required = true)
    @NotBlank(message = "수정할 카테고리를 선택해주세요.")
    private Category category;

    @ApiModelProperty(value = "판매할 티켓 이름 작성", notes = "수정할 티켓 이름을 작성", required = true)
    @NotBlank(message = "수정할 티켓 이름을 작성해주세요.")
    private String ticketName;

    @ApiModelProperty(value = "수정할 주소 입력", notes = "수정할 주소를 입력", required = true)
    @NotBlank(message = "수정할 티켓의 주소를 입력해주세요.")
    private String address;

    @ApiModelProperty(value = "수정할 날짜 입력", notes = "수정할 시작 날짜를 입력", required = true)
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    @NotBlank(message = "수정할 시작 날짜를 입력해주세요.(시작 날짜)")
    private LocalDate startDate;

    @ApiModelProperty(value = "수정할 날짜 입력", notes = "수정할 마지막 날짜를 입력", required = true)
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    @NotBlank(message = "수정할 마지막 날짜를 입력해주세요.(마지막 날짜)")
    private LocalDate endDate;

    @ApiModelProperty(value = "수정할 상세 정보 입력", notes = "수정할 상세 정보를 입력", required = true)
    @NotBlank(message = "수정할 상세 정보를 입력해주세요.")
    private String detailsContent;

    @ApiModelProperty(value = "수정할 가격 입력", notes = "수정할 가격를 입력", required = true)
    @NotBlank(message = "수정할 티켓의 가격을 입력해주세요.")
    private int price;
}
