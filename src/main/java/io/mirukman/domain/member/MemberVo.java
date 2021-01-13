package io.mirukman.domain.member;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class MemberVo {
    
    private String userId;
    private String userPw;
    private String userName;
    private boolean enabled;

    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private List<AuthVo> authList;
}
