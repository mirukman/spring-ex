package io.mirukman.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * 로그인 성공 후 특정 동작을 처리하고싶은 경우 여기에 해당 내용 작성
 */

@Slf4j
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication auth) throws IOException, ServletException {
        
        log.info("login success");

        List<String> roleNames = auth.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());
      
        if (roleNames.contains("ROLE_ADMIN")) {
            response.sendRedirect("/sample/admin");
            return;
        }

        if (roleNames.contains("ROLE_MEMBER")) {
            response.sendRedirect("/sample/member");
            return;
        }

        response.sendRedirect("/sample/all");
    }
}
