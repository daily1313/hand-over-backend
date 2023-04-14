package com.example.handoverbackend.dto.ticket;

import com.example.handoverbackend.domain.ticket.Category;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TicketCreateRequestDto {

    @ApiModelProperty(value = "카테고리 선택", notes = "카테고리 선택", required = true)
    private Category category;

    @ApiModelProperty(value = "판매할 티켓 이름 작성", notes = "티켓 이름을 작성", required = true)
    @NotBlank(message = "티켓 이름을 작성해주세요.")
    private String ticketName;

    @ApiModelProperty(value = "주소 입력", notes = "주소를 입력", required = true)
    @NotBlank(message = "판매할 티켓의 주소를 입력해주세요.")
    private String address;

    @ApiModelProperty(value = "시작 날짜 입력", notes = "시작 날짜를 입력", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ApiModelProperty(value = "마지막 날짜 입력", notes = "마지막 날짜를 입력", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ApiModelProperty(value = "상세 정보 입력", notes = "상세 정보를 입력", required = true)
    @NotBlank(message = "티켓의 상세 정보를 입력해주세요.")
    private String detailsContent;

    @ApiModelProperty(value = "가격 입력", notes = "가격를 입력", required = true)
    @NotNull(message = "티켓의 가격을 입력해주세요.")
    private int price;
}
