package io.mirukman.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.mirukman.config.RootConfig;
import io.mirukman.domain.board.BoardVo;
import io.mirukman.domain.board.Criteria;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RootConfig.class })
@Slf4j
public class BoardMapperTest {
    
    @Setter(onMethod_ = @Autowired)
    private BoardMapper mapper;

    //C
    @Test
    public void insertTest() {

        BoardVo boardVo = new BoardVo();
        boardVo.setTitle("새로 작성하는 글");
        boardVo.setContent("새로 작성하는 내용");
        boardVo.setWriter("newbie");

        mapper.insert(boardVo);

        log.info(boardVo.toString());
    }

    @Test
    public void insertSelectKeyTest() {

        BoardVo boardVo = new BoardVo();
        boardVo.setTitle("새로 작성하는 글");
        boardVo.setContent("새로 작성하는 내용");
        boardVo.setWriter("newbie");

        mapper.insertSelectKey(boardVo);

        log.info(boardVo.toString());
    }

    //R
    @Test
    public void getListTest() {
        Criteria criteria = new Criteria(2);
        List<BoardVo> list = mapper.getList(criteria);
        list.forEach(boardVo -> {
            log.info(boardVo.toString());
        });
    }

    @Test
    public void readTest() {
        BoardVo boardVo = mapper.read(5);
        log.info(boardVo.toString());
    }
    
    //U
    @Test
    public void updateTest() {
        BoardVo boardVo = new BoardVo();
        boardVo.setBno(5L);
        boardVo.setTitle("수정된 제목");
        boardVo.setContent("수정된 내용");
        boardVo.setWriter("user22");
        boardVo.setUpdatedate(LocalDateTime.now());

        int count = mapper.update(boardVo);

        log.info("update count: " + count);
    }

    //D
    @Test
    public void deleteTest() {
        long count = mapper.delete(9);
        log.info("delete count: " + count);
    }

    @Test
    public void testSearch() {
        Criteria criteria = new Criteria(1);
        criteria.setKeyword("안녕");
        criteria.setType("TC");

        List<BoardVo> list = mapper.getList(criteria);

        list.forEach(boardVo -> log.info(boardVo.toString()));
    }
}
