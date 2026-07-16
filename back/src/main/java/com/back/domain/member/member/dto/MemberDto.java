package com.back.domain.member.member.dto;

import com.back.domain.member.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record MemberDto(
        int id,
        LocalDateTime createDate,
        LocalDateTime modifyDate,
        String name,
        @JsonProperty("isAdmin")
        boolean isAdmin,
        String profileImageUrl
) {
    public MemberDto(Member member) {
        this(
                member.getId(),
                member.getCreateDate(),
                member.getModifyDate(),
                member.getName(),
                member.isAdmin(),
                member.getProfileImgUrlOrDefault()
        );
    }
}
