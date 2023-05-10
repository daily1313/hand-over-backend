package com.example.handoverbackend.dto.message;

import com.example.handoverbackend.domain.message.Message;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageResponseDto {

    private String title;
    private String content;
    private LocalDateTime sentAt;
    private String senderUsername;
    private String receiverUsername;

    public static MessageResponseDto toDto(Message message) {
        return new MessageResponseDto(
                message.getTitle(),
                message.getContent(),
                message.getSender().getUsername(),
                message.getReceiver().getUsername()
        );
    }

    public MessageResponseDto(String title, String content, String senderUsername, String receiverUsername) {
        this.title = title;
        this.content = content;
        this.sentAt = LocalDateTime.now();
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
    }
}
