package io.mirukman.domain.reply;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReplyVo {
    
    private Long rno;
    private Long bno;

    private String reply;
    private String replyer;
    private LocalDateTime replyDate;
    private LocalDateTime updateDate;
}
