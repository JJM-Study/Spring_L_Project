<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<%@ include file="/WEB-INF/jsp/layout/main-layout.jsp" %>--%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/product/product_detail.css">


<%--<main class="flex-grow-1">--%>
    <section class="detail-container mt-4">
        <div class="image-container">
            <c:forEach var="item" items="${itemList.imageList}">
                <c:if test="${not item.isMain}">
                    <div class="img-main col-md-3 mb-3">
                        <img src="${item.imageUrl}"
                             alt="${'메인 상품 이미지'}"
                             class="card-img-top" style="height: 300px; object-fit: contain;"/>
                    </div>
                </c:if>
                <div class="card">
                    <c:if test="${not item.isMain}" />
                    <ul class="list-group md">
                        <c:if test="${item.isMain} == 1">
                        <li class="list-group-item">
                            <img src="${item.imageUrl}"
                                 alt="${'서브 상품 이미지'}"
                                 class="temp" />
                        </li>
                        </c:if>
                    </ul>
                </div>
            </c:forEach>
        </div>


      <p>가격 : "${itemList.price}"원</p>

        <p>원문 : </p>
        <div class="product-detail">
            <c:choose>
                <c:when test="${not empty itemList.detailDesc}">
        <pre style="white-space:pre-wrap; background:#f7f7f7; padding:8px;">
          RAW: [<c:out value="${itemList.detailDesc}" />]
        </pre>

        <p>렌더링 결과:</p>
                <div>
  <c:out value="${itemList.detailDesc}" escapeXml="false" />
                </div>
                </c:when>
                <c:otherwise>
                    <p>상품 정보가 없습니다.</p>
                </c:otherwise>
            </c:choose>
        </div>


        <div>
            <button id="pBtn" type="button">+</button>
            <input type="number" class="qty" value="1" min="1"/>
            <button id="mBtn" type="button">-</button>
        </div>
      <button class="btn btn-primary add-to-cart-btn" data-prodno="${itemList.prodNo}">장바구니 담기</button>
      <button class="btn btn-primary btn-ord-now" data-prodno="${itemList.prodNo}">주문하기</button>
    </section>
<%--</main>--%>



<script src="/js/cart/cart.js"></script>
<script src="/js/product/product_detail.js" defer></script>

<%--<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>--%>