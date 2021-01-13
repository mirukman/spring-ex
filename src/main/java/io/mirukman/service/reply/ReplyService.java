package io.mirukman.service.reply;

import io.mirukman.domain.board.Criteria;
import io.mirukman.domain.reply.ReplyPageDto;
import io.mirukman.domain.reply.ReplyVo;

public interface ReplyService {
    
    public int register(ReplyVo replyVo);

    public ReplyVo get(Long rno);

    public int modify(ReplyVo replyVo);

    public int remove(Long rno);

    public ReplyPageDto getList(Long bno, Criteria criteria);

    public int getTotalCount(Long bno);

}
