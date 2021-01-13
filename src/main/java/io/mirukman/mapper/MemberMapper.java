package io.mirukman.mapper;

import io.mirukman.domain.member.MemberVo;

public interface MemberMapper {
    
    public MemberVo read(String userId);
}
