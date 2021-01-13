package io.mirukman.domain.board;

import lombok.Getter;
import lombok.ToString;


@ToString
@Getter
public class Criteria {
    
    private int page;
    private int amount;
    private int offset;
    private String type;
    private String keyword;

    public Criteria() {
        this(1);
    }

    public Criteria(int page) {
        this.page = page;
        this.amount = 10;
        this.offset = (page - 1) * amount;
    }

    public void setPage(int page) {
        this.page = page;
        this.offset = (page - 1) * amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    

    public String[] getTypeArr() {
        return type == null ? new String[] {} : type.split("");
    }
}
