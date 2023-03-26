package com.example.handoverbackend.repository;


import static com.example.handoverbackend.domain.member.QMember.member;

import com.example.handoverbackend.dto.member.MemberResponseDto;
import com.example.handoverbackend.dto.member.MemberSearchCondition;
import com.example.handoverbackend.dto.member.QMemberResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MemberResponseDto> searchMember(MemberSearchCondition condition, Pageable pageable) {

        QueryResults<MemberResponseDto> result = queryFactory
            .select(new QMemberResponseDto(
                member.name,
                member.username,
                member.nickname,
                member.authority))
            .from(member)
            .where(containsNameAndNickname(condition.getName(), condition.getNickname()))
            .fetchResults();

        List<MemberResponseDto> content = result.getResults();

        return new PageImpl<>(content, pageable, result.getTotal());

    }

    private BooleanBuilder containsNameAndNickname(String name, String nickname) {
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.hasText(name)) {
            builder.or(member.name.contains(name));
        }
        if (StringUtils.hasText(nickname)) {
            builder.or(member.nickname.contains(nickname));
        }
        return builder;
    }


}
