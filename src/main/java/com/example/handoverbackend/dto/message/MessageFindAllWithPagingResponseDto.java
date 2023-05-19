package com.example.handoverbackend.dto.message;

import com.example.handoverbackend.dto.member.MemberResponseDto;
import com.example.handoverbackend.dto.page.PageInfoDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageFindAllWithPagingResponseDto {

    private List<MessageResponseDto> messages;
    private PageInfoDto pageInfoDto;

    public static MessageFindAllWithPagingResponseDto toDto(List<MessageResponseDto> messages, PageInfoDto pageInfoDto) {
        return new MessageFindAllWithPagingResponseDto(messages, pageInfoDto);
    }
}
