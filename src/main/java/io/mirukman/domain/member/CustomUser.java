package io.mirukman.domain.member;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class CustomUser extends User {
    
    private static final long serialVersionUID = 1L;

    private MemberVo member;

    public CustomUser(String userName, String password, Collection<? extends GrantedAuthority> authorities) {
        super(userName, password, authorities);
    }

    public CustomUser(MemberVo memberVo) {
        super(memberVo.getUserId(), memberVo.getUserPw(), memberVo.getAuthList().stream()
                .map(auth -> new SimpleGrantedAuthority(auth.getAuth())).collect(Collectors.toList()));
    
        this.member = memberVo;
    }
}
