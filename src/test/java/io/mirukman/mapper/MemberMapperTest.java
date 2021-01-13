package io.mirukman.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.mirukman.config.RootConfig;
import io.mirukman.config.SecurityConfig;
import io.mirukman.domain.member.MemberVo;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RootConfig.class, SecurityConfig.class })
@Slf4j
public class MemberMapperTest {
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MemberMapper memberMapper;

    @Test
    public void insertMember() {

        String sql = "insert into tbl_member(userid, userpw, username) values (?,?,?)";

        for (int i = 0; i < 100; i++) {
            Connection conn = null;
            PreparedStatement pstmt = null;

            try {
                conn = dataSource.getConnection();
                pstmt = conn.prepareStatement(sql);

                pstmt.setString(2, passwordEncoder.encode("pw" + i));

                if (i < 80) {
                    pstmt.setString(1, "user" + i);
                    pstmt.setString(3, "일반사용자" + i);
                } else if (i < 90) {
                    pstmt.setString(1, "manager" + i);
                    pstmt.setString(3, "운영자" + i);
                } else {
                    pstmt.setString(1, "admin" + i);
                    pstmt.setString(3, "관리자" + i);
                }

                pstmt.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (Exception e) {
                    }
                }

                if (conn != null) {
                    try {
                        conn.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
    
    @Test
    public void insertAuthTest() {
        String sql = "insert into tbl_auth(userid, auth) values (?,?)";

        for (int i = 0; i < 100; i++) {
            Connection conn = null;
            PreparedStatement pstmt = null;

            try {
                conn = dataSource.getConnection();
                pstmt = conn.prepareStatement(sql);

                if (i < 80) {
                    pstmt.setString(1, "user" + i);
                    pstmt.setString(2, "ROLE_USER");
                } else if (i < 90) {
                    pstmt.setString(1, "manager" + i);
                    pstmt.setString(2, "ROLE_MEMBER");
                } else {
                    pstmt.setString(1, "admin" + i);
                    pstmt.setString(2, "ROLE_ADMIN");
                }

                pstmt.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (Exception e) {
                    }
                }

                if (conn != null) {
                    try {
                        conn.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    @Test
    public void readTest() {
        MemberVo memberVo = memberMapper.read("admin90");

        assertNotNull(memberVo);
        assertEquals("$2a$10$.zKRKwjoX2F./crXixMUJOYUtA12PC3A5a/lirTmp.DFDtLo6pAAq", memberVo.getUserPw());

        List<String> auths = memberVo.getAuthList().stream().map(vo -> vo.getAuth()).collect(Collectors.toList());
        assertTrue(auths.contains("ROLE_ADMIN"));

        log.info(memberVo.toString());
    }
}
