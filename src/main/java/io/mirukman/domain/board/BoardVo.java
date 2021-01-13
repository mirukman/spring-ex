package io.mirukman.domain.board;

import java.time.LocalDateTime;
import java.util.List;

import io.mirukman.domain.attach.BoardAttachVo;
import lombok.Data;

@Data
public class BoardVo {
    
    private Long bno;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime regdate;
    private LocalDateTime updatedate;

    private int replyCount;
    private List<BoardAttachVo> attachList;
}
