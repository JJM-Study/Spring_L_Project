<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/product/product-list.css">


<%--<main class="flex-grow-1">--%>
<script src="/js/product/product.js" defer></script>
<script src="/js/product/product-list.js" defer></script>
<section class="container mt-4">
<div class="filter">
      <div id="search-container">
        <input type="text" class="search-title"></input>
        <button class="search-title-btn">검색</button>
      </div>
      <div class="btn-group" role="group" aria-label="Page Size">
              <span>페이지당 표시 건수 :</span>
              <ul>
                  <li>
                    <a href="#" class="btn-array btn btn-outline-primary" data-count="30">30</a>
                  </li>
                  <li>
                    <a href="#" class="btn-array btn btn-outline-primary" data-count="50">50</a>
                  </li>
                  <li>
                    <a href="#" class="btn-array btn btn-outline-primary" data-count="100">100</a>
                  </li>
              </ul>
      </div>
  </div>
  <div class="row">
    <c:forEach var="item" items="${itemList}">
      <div class="col-md-3 mb-4">
        <div class="card h-100">
          <div class="product-image-container">
              <c:choose>
                <c:when test="${empty item.imageUrl}">
                    <a href="/product/detail/${item.prodNo}">
                        <img src="/assets/images/No_Image.png" class="card-img-top product-image" alt="기본 이미지">
                    </a>
                </c:when>
                <c:otherwise>
                    <a href="/product/detail/${item.prodNo}">
                        <img src="${item.imageUrl}" class="card-img-top product-image" alt="상품 이미지">
                    </a>
                </c:otherwise>
              </c:choose>
          </div>
          <div class="card-body">
            <div class="product-title-container">
                <h5 class="card-title">
                    <a href="/product/detail/${item.prodNo}" style="text-decoration: none;">
                ${item.prodName}
                </a>
                </h5>
            </div>
            <div class="product-info-container">
                <p class="card-text">${item.price}원</p>
                <p class="card-text">
                    <c:choose>
                        <c:when test="${item.prodType == 'DIGITAL'}">
                            다운로드 상품
                        </c:when>
                        <c:when test="${item.prodType == 'PHYSICAL'}">
                            배송 상품
                        </c:when>
                        <c:otherwise>
                            기타
                        </c:otherwise>
                    </c:choose>
                </p>
            </div>
            <c:choose>
                <c:when test="${item.isInLyb==true}">
                    <button class="btn btn-primary in-cart-btn" data-prodno="${item.prodNo}" onclick="location.href='/library/my'">소유 중</button>
                </c:when>
                <c:when test="${item.isInCart==false}">
                    <button class="btn btn-primary add-to-cart-btn" data-prodno="${item.prodNo}">장바구니 담기</button>
                <input type="hidden" class="qty" value="1" />
                </c:when>
                <c:otherwise>
                    <button class="btn btn-primary in-cart-btn" data-prodno="${item.prodNo}" onclick="location.href='/cart/cartlist'">담기 완료</button>
                </c:otherwise>
            </c:choose>
          </div>
        </div>
      </div>
    </c:forEach>
  </div>


  <jsp:include page="/WEB-INF/jsp/common/pagination.jsp"/>
<%--    <nav aria-abel="Pagination" id="pages">
        <ul class="pagination">

            <!-- 이전 -->
            <c:url var="prevUrl" value="/product/products">
                <c:param name="cPage" value="${pagination.prevPage}" />
                <c:if test = "${not empty title}">
                    <c:param name="title" value="${title}" />
                </c:if>
            </c:url>
            <li class="page-item">
                <a class="page-link" href="${prevUrl}">이전</a>
            </li>

            <c:forEach var="i" begin="${pagination.startPage}" end="${pagination.endPage}" step="1">
                <c:url var="pageUrl" value="/product/products" >
                    <c:param name="cPage" value="${i}" />
                    <c:param name="pageSize" value="${pagination.pageSize}" />
                    <c:if test="${not empty title}">
                        <c:param name="title" value="${title}" />
                    </c:if>
                </c:url>
                <li class="page-item"><a class="page-link" href="${pageUrl}">${i}</a></li>
            </c:forEach>

            <c:url var="nextUrl" value="/product/products">
                <c:param name="cPage" value="${pagination.nextPage}" />
                <c:if test="${not empty title}">
                   <c:param name="title" value="${title}" />
                </c:if>
            </c:url>
            <li class="page-item">
                <a class="page-link" href="${nextUrl}">다음</a>
            </li>
        </ul>
    </nav> --%>

</section>
<%--</main>--%>

<script src="/js/cart/cart.js"></script>
<%--<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>--%>