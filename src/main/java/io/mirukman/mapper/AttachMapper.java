package io.mirukman.mapper;

import java.util.List;

import io.mirukman.domain.attach.BoardAttachVo;

public interface AttachMapper {
    
    public void insert(BoardAttachVo vo);

    public void delete(String uuid);

    public List<BoardAttachVo> findByBno(Long bno);

    public void deleteAll(Long bno);

    public List<BoardAttachVo> getOldFiles();
}
