package io.mirukman.service.board;

import java.util.List;

import io.mirukman.domain.attach.BoardAttachVo;
import io.mirukman.domain.board.BoardVo;
import io.mirukman.domain.board.Criteria;

public interface BoardService {
    
    public void register(BoardVo boardVO);

    public BoardVo get(long bno);

    public boolean modify(BoardVo boardVo);

    public boolean remove(long bno);

    public List<BoardVo> getList(Criteria criteria);

    public int getTotalCount();

    public List<BoardAttachVo> getAttachList(Long bno);

    public void deleteFiles(List<BoardAttachVo> attachList);
}
