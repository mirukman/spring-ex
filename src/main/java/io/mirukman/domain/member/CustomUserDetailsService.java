package io.mirukman.domain.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import io.mirukman.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        log.info("loadUserByUsername: " + userName);

        MemberVo memberVo = memberMapper.read(userName);

        log.info("memberVo: " + memberVo);

        return memberVo == null ? null : new CustomUser(memberVo);
    }
}
