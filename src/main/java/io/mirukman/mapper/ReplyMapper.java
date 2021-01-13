package io.mirukman.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.mirukman.domain.board.Criteria;
import io.mirukman.domain.reply.ReplyVo;

public interface ReplyMapper {
    
    public Integer insert(ReplyVo replyVo);

    public ReplyVo read(Long rno);

    public Integer delete(Long rno);

    public Integer update(ReplyVo replyVo);

    public List<ReplyVo> getList(@Param("bno") Long bno, @Param("criteria") Criteria criteria);

    public Integer getTotalCount(Long bno);
}
