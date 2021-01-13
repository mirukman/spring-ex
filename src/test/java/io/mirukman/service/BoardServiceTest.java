package io.mirukman.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.mirukman.config.RootConfig;
import io.mirukman.domain.board.BoardVo;
import io.mirukman.domain.board.Criteria;
import io.mirukman.service.board.BoardService;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
@Slf4j
public class BoardServiceTest {
    
    @Autowired
    private BoardService service;

    @Test
    public void testExist() {
        assertNotNull(service);
        log.info(service.toString());

        assertTrue(service.getClass().getName().contains("Proxy"));
        log.info(service.getClass().getName());
    }

    @Test
    public void registerTest() {

        BoardVo boardVo = new BoardVo();
        boardVo.setTitle("새로 작성하는 글");
        boardVo.setContent("새로 작성하는 내용");
        boardVo.setWriter("newbie");

        service.register(boardVo);

        log.info("생성된 게시물의 번호: " + boardVo.getBno());
    }

    @Test
    public void getTest() {
        log.info(service.get(30L).toString());
    }

    @Test
    public void getListTest() {
        Criteria criteria = new Criteria(2);
        service.getList(criteria).forEach(boardVo -> log.info(boardVo.toString()));
    }

    @Test
    public void modifyTest() {
        
        BoardVo boardVo = service.get(1L);
        if (boardVo == null) {
            return;
        }
        boardVo.setTitle("제목 수정");

        assertTrue(service.modify(boardVo));
        assertTrue(service.get(1L).getTitle().equals("제목 수정"));
    }

    @Test
    public void removeTest() {
        service.remove(2L);

        BoardVo boardVo = service.get(2L);

        assertNull(boardVo);
    }
}
