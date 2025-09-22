package org.example.myproject.config;

import lombok.Getter;

@Getter
public class Pagination {

//    한 페이지당 게시글 수 (프론트 엔드에서 가져오는)
//    private final int pageSize;

    /// 총 게시글 수
    private final int listCnt;

    // 현재 페이지
    private final int curPage;

    // 총 페이지 개수
    private final int pageCnt;

    // 한 블럭(range) 당 페이지 수
    /* 한 블럭당 페이지 수는, 이후 페이지네이션 기능 확장하여서 좀 더 유연하게 할 것을 고민.*/
    private final int rangeSize;

    // 총 블럭 수
    private final int rangeCnt;

    // 시작 페이지
    private int startPage;

    // 끝 페이지
    private int endPage;

    // 시작 index
    private int offset;

    private int pageSize;

    // 다음 페이지
    private int nextPage;

    // 이전 페이지
    private int prevPage;

    // 이후 확장 시, 페이지 size의 상하한을 정하는 방법을 추가할 것을 고민해볼 것
    // pageSize : 총 display 할 페이지 수(프론트 엔드에서 동적 조절) ,
    // curPage : 현재 페이지,
    // listCnt : 게시물 개수
    public Pagination(int curPage, int pageSize, int listCnt) {
        this(curPage, pageSize, listCnt, 10);
    }

    public Pagination(int curPage, int pageSize, int listCnt, int rangeSize) {
       this.pageSize = Math.max(1, pageSize);
       this.listCnt = Math.max(0, listCnt);
       this.rangeSize = Math.max(1, rangeSize);

       int pages = (int) Math.ceil((double) this.listCnt / this.pageSize);
       this.pageCnt = Math.max(pages, 1);

       int p = Math.min(Math.max(curPage, 1), this.pageCnt);
       this.curPage = p;

       this.offset = (p - 1) * this.pageSize;

       int curRangeIdx = (p - 1) / this.rangeSize;
       int sp = curRangeIdx * this.rangeSize + 1;
       int ep = Math.min(sp + this.rangeSize - 1, this.pageCnt);
       this.startPage = sp;
       this.endPage = ep;

       this.rangeCnt = (int)Math.ceil((double)this.pageCnt / this.rangeSize);

       this.prevPage = (p > 1) ? p - 1 : 1;
       this.nextPage = (p < this.pageCnt) ? p + 1 : this.pageCnt;
    }
}
