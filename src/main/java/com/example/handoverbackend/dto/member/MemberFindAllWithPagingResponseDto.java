package com.example.handoverbackend.dto.member;

import com.example.handoverbackend.dto.page.PageInfoDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberFindAllWithPagingResponseDto {

    private List<MemberResponseDto> members;
    private PageInfoDto pageInfoDto;

    public static MemberFindAllWithPagingResponseDto toDto(List<MemberResponseDto> members, PageInfoDto pageInfoDto) {
        return new MemberFindAllWithPagingResponseDto(members, pageInfoDto);
    }
}
