package io.mirukman.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.mirukman.domain.board.BoardVo;
import io.mirukman.domain.board.Criteria;

public interface BoardMapper {
    
    //C
    public void insert(BoardVo boardVo);

    public void insertSelectKey(BoardVo boardVo);
    
    //R
    public List<BoardVo> getList(Criteria criteria);
    
    public BoardVo read(long bno);

    public int getTotalCount();
    
    //U
    public int update(BoardVo boardVo);

    //D
    public int delete(long bno);

    public void updateReplyCount(@Param("bno") Long bno, @Param("count") int inc);

}
