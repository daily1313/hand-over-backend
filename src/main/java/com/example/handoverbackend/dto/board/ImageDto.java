package com.example.handoverbackend.dto.board;

import com.example.handoverbackend.domain.board.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageDto {

    private Long id;
    private String originName;
    private String uniqueName;

    public static ImageDto toDto(Image image) {
        return new ImageDto(image.getId(), image.getOriginName(), image.getUniqueName());
    }
}
