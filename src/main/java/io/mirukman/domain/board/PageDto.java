package io.mirukman.domain.board;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PageDto {
    
    private int startPage;
    private int lastPage;

    boolean prev;
    boolean next;

    private int total;

    Criteria criteria;

    private final int PAGE_VIEW_COUNT = 10;

    public PageDto(Criteria criteria, int total) {
        this.criteria = criteria;
        this.total = total;

        this.lastPage = (this.criteria.getPage() / 10 + 1) * PAGE_VIEW_COUNT;
        if (criteria.getPage() % PAGE_VIEW_COUNT == 0) {
            lastPage -= PAGE_VIEW_COUNT;
        }
        
        this.startPage = this.lastPage - PAGE_VIEW_COUNT + 1;

        int realLastPage = (this.total / criteria.getAmount() + 1);

        if (lastPage > realLastPage) {
            this.lastPage = realLastPage;
        }

        this.prev = this.startPage > PAGE_VIEW_COUNT;
        this.next = this.lastPage < realLastPage;
    }
}
