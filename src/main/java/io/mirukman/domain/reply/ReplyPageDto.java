package io.mirukman.domain.reply;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class ReplyPageDto {

    private int replyCount;
    private List<ReplyVo> list;
}
