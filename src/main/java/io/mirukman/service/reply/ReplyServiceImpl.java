package io.mirukman.service.reply;

import org.springframework.stereotype.Service;

import io.mirukman.domain.board.Criteria;
import io.mirukman.domain.reply.ReplyPageDto;
import io.mirukman.domain.reply.ReplyVo;
import io.mirukman.mapper.BoardMapper;
import io.mirukman.mapper.ReplyMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

	private final ReplyMapper replyMapper;
	private final BoardMapper boardMapper;


	@Override
	public int register(ReplyVo replyVo) {
		boardMapper.updateReplyCount(replyVo.getBno(), 1);
		return replyMapper.insert(replyVo);
	}

	@Override
	public ReplyVo get(Long rno) {
        return replyMapper.read(rno);
	}

	@Override
	public int modify(ReplyVo replyVo) {
        return replyMapper.update(replyVo);
	}

	@Override
	public int remove(Long rno) {
		ReplyVo replyVo = get(rno);
		boardMapper.updateReplyCount(replyVo.getBno(), -1);
        return replyMapper.delete(rno);
	}

	@Override
	public ReplyPageDto getList(Long bno, Criteria criteria) {
		return new ReplyPageDto(replyMapper.getTotalCount(bno), replyMapper.getList(bno, criteria));
	}
	
	@Override
	public int getTotalCount(Long bno) {
		return replyMapper.getTotalCount(bno);
	}
}
