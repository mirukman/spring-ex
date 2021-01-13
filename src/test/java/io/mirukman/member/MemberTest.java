package io.mirukman.member;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.mirukman.config.RootConfig;
import io.mirukman.config.SecurityConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RootConfig.class, SecurityConfig.class })
public class MemberTest {
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DataSource dataSource;

    @Test
    public void testInsertMember() {

        String sql = "insert into tbl_member(userid, userpw, username) values (?,?,?)";

        for (int i = 0; i < 100; i++) {
            Connection conn = null;
            PreparedStatement pstmt = null;
        
            try {
                conn = dataSource.getConnection();
                pstmt = conn.prepareStatement(sql);
            
                pstmt.setString(2, passwordEncoder.encode("pw" + i));

                if(i < 80) {
                    pstmt.setString(1, "user" + i);
                    pstmt.setString(3, "일반사용자" + i);
                } else if(i < 90) {
                    pstmt.setString(1, "manager" + i);
                    pstmt.setString(3, "운영자" + i);
                } else {
                    pstmt.setString(1, "admin" + i);
                    pstmt.setString(3, "관리자" + i);
                }

                pstmt.executeUpdate();
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (Exception e) {}
                }
                
                if (conn != null) {
                    try {
                        conn.close();
                    } catch(Exception e) {}
                }
            }
        }
    }
}
