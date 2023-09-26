package com.minki.ogani.dto.page;

import lombok.Data;

@Data
public class PageMaker {
    private int totalCount; // 총 데이터 개수 : 품목이 100이면 100개
    private int startPage;
    private int maxPage; // 1~7까지 7~13페이지 이런식으로 최대치 보여주는
    private int endPage;
    private int displayPageNum = 6;
    public Criteria criteria;

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        calcData();
    }

    private void calcData() {
        startPage = ((criteria.getPage() - 1) / displayPageNum) * displayPageNum + 1;
        endPage = startPage + displayPageNum - 1;
        maxPage = (int) (Math.ceil(totalCount / (double) criteria.getPerPageNum()))+1;
        if (endPage > maxPage) {
            endPage = maxPage;
        }
    }
}
