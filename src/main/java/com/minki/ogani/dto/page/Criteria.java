package com.minki.ogani.dto.page;

import lombok.Data;

@Data
public class Criteria {
    private int page; //페이지
    private int perPageNum; //한 페이지에 몇 줄 나올지

    private String searchText; //검색어

    private String searchType; // 검색 시 컬럼명(품목명/순번/가격/분류)

    private String sortColumn; // 정렬할때 컬럼명(품목명/순번/가격/분류)

    private String sortType; // 정렬할때 오름차순인지 내림차순인지

    private String sort; // 품목명 내림차순 >> 이렇게 합쳐서 들어가진다

    private String idx; //  일단 보류


    public Criteria() {
        this.page = 1;
        this.perPageNum = 10; //10으로 설정
    }

    public void setPage(int page) {
        if (page <= 0) {
            this.page = 1;
            return;
        }
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setPerPageNum(int perPageNum) {
        if (perPageNum <= 0 || perPageNum > 100) {
            this.perPageNum = 10;
            return;
        }
        this.perPageNum = perPageNum;
    }

    public int getPerPageNum() {
        return this.perPageNum;
    }

    public int getPageStart() {
        return (this.page - 1) * perPageNum;
    }

    public String getSort(){
        return this.sortColumn+ " " + this.sortType;
    }
}
