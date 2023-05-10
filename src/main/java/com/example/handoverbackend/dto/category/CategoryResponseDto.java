package com.example.handoverbackend.dto.category;

import com.example.handoverbackend.domain.board.Board;
import com.example.handoverbackend.domain.category.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {

    private String name;

    public static CategoryResponseDto toDto(Category category) {
        return new CategoryResponseDto(category.getName());
    }
}
