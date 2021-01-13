package io.mirukman.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.mirukman.config.RootConfig;
import io.mirukman.domain.board.Criteria;
import io.mirukman.domain.reply.ReplyVo;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RootConfig.class })
@Slf4j
public class ReplyMapperTest {
    
    @Setter(onMethod_ = @Autowired)
    private ReplyMapper mapper;

    @Test
    public void mapperBeanInjectionTest() {
        assertNotNull(mapper);
    }

    @Test
    public void insertTest() {

        IntStream.rangeClosed(0, 9).forEach(i -> {
            ReplyVo vo = new ReplyVo();

            vo.setBno(i % 5 + 1L);
            vo.setReply("댓글 테스트 " + (i + 1));
            vo.setReplyer("replyer " + (i + 1));

            mapper.insert(vo);
        });

        ReplyVo vo = new ReplyVo();

        vo.setBno(1L);
        vo.setReply("누구인가?");
        vo.setReplyer("궁예");

        mapper.insert(vo);

        ReplyVo obj = mapper.read(11L);

        assertEquals("누구인가?", obj.getReply());
        assertEquals("궁예", obj.getReplyer());
    }

    @Test
    public void readTest() {
        ReplyVo vo = mapper.read(3L);

        assertNotNull(vo);

        log.info(vo.toString());
    }

    @Test
    public void deleteTest() {
        mapper.delete(2L);

        ReplyVo vo = mapper.read(2L);
        assertNull(vo);
    }

    @Test
    public void updateTest() {
        ReplyVo vo = mapper.read(10L);
        vo.setReply("수정했다");

        int count = mapper.update(vo);

        assertEquals(1, count);

        ReplyVo obj = mapper.read(10L);

        assertEquals("수정했다", obj.getReply());
    }

    @Test
    public void getListTest() {
        
        Criteria criteria = new Criteria();
        List<ReplyVo> list = mapper.getList(3L, criteria);

        assertNotNull(list);

        list.forEach(replyVo -> log.info(replyVo.toString()));
    }
}
