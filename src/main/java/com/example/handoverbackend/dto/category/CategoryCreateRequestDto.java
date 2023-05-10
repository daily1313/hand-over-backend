package com.example.handoverbackend.dto.category;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "카테고리 생성 요청 처리")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCreateRequestDto {

    @ApiModelProperty(value = "카테고리 명", notes = "카테고리 명을 입력하세요.", required = true, example = "카테고리 이름")
    @NotBlank(message = "카테고리 명을 입력하세요.")
    private String name;
}
