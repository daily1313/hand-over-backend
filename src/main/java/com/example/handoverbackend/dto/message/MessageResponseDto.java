package com.example.handoverbackend.dto.message;

import com.example.handoverbackend.domain.common.BaseEntity;
import com.example.handoverbackend.domain.message.Message;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDto {

    private Long id;
    private String title;
    private String content;
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createAt;
    private String senderUsername;
    private String receiverUsername;

    public static MessageResponseDto toDto(Message message) {
        return new MessageResponseDto(
                message.getId(),
                message.getTitle(),
                message.getContent(),
                message.getCreatedAt(),
                message.getSender().getUsername(),
                message.getReceiver().getUsername()
        );
    }
}
