package org.example.myproject.config;

import jakarta.validation.constraints.Max;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Getter
public class Pagination {

    private static final Logger logger = LogManager.getLogger(Pagination.class);

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
    private final int rangeSize = 10;

    // 총 블럭 수
    private final int rangeCnt;

    // 다음 페이지까지의 간격.

    private final int step = 5;


    // 시작 페이지
    private final int startPage;

    // 끝 페이지
    private final int endPage;

    // 시작 index
    private final int offset;

    private final int pageSize;

    // 다음 페이지
    private final int nextPage;

    // 이전 페이지
    private final int prevPage;

    // 이후 확장 시, 페이지 size의 상하한을 정하는 방법을 추가할 것을 고민해볼 것
    // pageSize : 총 display 할 페이지 수(프론트 엔드에서 동적 조절) ,
    // curPage : 현재 페이지,
    // listCnt : 게시물 개수
//    public Pagination(int curPage, int pageSize, int listCnt) {
//        this(curPage, pageSize, listCnt);
//    }

    /**
     *
     * @param curPage 현재 페이지
     * @param pageSize 한 페이지당 보여줄 포스팅 수
     * @param listCnt 전체 데이터 개수
     */

    public Pagination(int curPage, int pageSize, int listCnt) {

        this.pageSize = Math.max(1, pageSize);
        this.listCnt = Math.max(0, listCnt);

        int pages = (int) Math.ceil((double) this.listCnt / this.pageSize);
        this.pageCnt = Math.max(pages, 1);
        // this.rangeSize = Math.max(1, rangeSize)

        int p = Math.min(Math.max(curPage, 1), this.pageCnt);
        this.curPage = p;


        this.offset = (p - 1) * this.pageSize;

        int rangeSize = this.rangeSize;
        int totalPages = this.pageCnt;


        int range = Math.max(1, Math.min(this.rangeSize, totalPages));


        int step = Math.max(1, Math.min(this.step, range - 1));


        int curRangeIdx = (p - 1) / range;
        int sp = curRangeIdx * range + 1;
        int ep = Math.min(sp + range - 1, totalPages);


        int maxStart = Math.max(1, totalPages - range + 1);
        int start = sp;
        if (p == ep && totalPages > range) {
            start = Math.min(sp + step, maxStart);
        } else if (p == sp && totalPages > range) {
            start = Math.max(sp - step, 1);
        }
        int end = Math.min(start + range - 1, totalPages);


        int prev = (p > 1) ? (p == start ? Math.max(start - 1, 1) : p - 1) : 1;
        int next = (p < totalPages) ? (p == end ? Math.min(end + 1, totalPages) : p + 1) : totalPages;


        this.startPage = start;
        this.endPage = end;
        this.prevPage = prev;
        this.nextPage = next;
        this.rangeCnt = (int) Math.ceil((double) totalPages / range);
    }
}
