<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<%@ include file="/WEB-INF/jsp/layout/main-layout.jsp" %>--%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/product/product_detail.css">



<%--<main class="flex-grow-1">--%>
    <section class="detail-container mt-4">
        <div class="info-container">
            <div class="image-container">
                   <div class="main-image-container">
                       <img src="${mainImages.imageUrl}"
                             alt="${'메인 상품 이미지'}"
                             class="card-img-top"/>
                   </div>
                   <div class="sub-container">
                       <ul class="sub-list">
                           <c:forEach var="item" items="${subImages}">
                                       <li>
                                               <img src="${item.imageUrl}"
                                                    alt="${'서브 상품 이미지'}"
                                                    class="temp" />
                                       </li>
                           </c:forEach>
                       </ul>
                   </div>
            </div>


                <p>가격 : "${itemList.price}"원</p>

                <p>원문 : </p>
            <div class="product-detail">
              <p>상세 내용</p>
                <c:choose>
                    <c:when test="${not empty itemList.detailDesc}">
                        <pre style="white-space:pre-wrap; background:#f7f7f7; padding:8px;">
                          RAW: [<c:out value="${itemList.detailDesc}" />]
                        </pre>

                        <p>렌더링 결과:</p>
                        <div>
                             <p><c:out value="${itemList.detailDesc}" escapeXml="false" /></p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <p>상품 정보가 없습니다.</p>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="notice">
                <p>공지</p>
                <c:choose>
                    <c:when test="${not empty itemList.notice}">
                       <p>${itemList.notice}</p>
                    </c:when>
                    <c:otherwise>
                       <p>판매자가 등록한 공지 사항이 없습니다.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="order-container">
            <div class="qtyBtn">
                <button id="pBtn" type="button">+</button>
                <input type="number" class="qty" value="1" min="1"/>
                <button id="mBtn" type="button">-</button>
            </div>
            <div class="orderBtn">
              <button class="btn btn-primary add-to-cart-btn" data-prodno="${itemList.prodNo}">장바구니 담기</button>
              <button class="btn btn-primary btn-ord-now" data-prodno="${itemList.prodNo}">주문하기</button>
           </div>
        </div>



    </section>
<%--</main>--%>



<script src="/js/cart/cart.js"></script>
<script src="/js/product/product_detail.js" defer></script>


<script type="text/javascript">

   const prod = ${itemListJson};
   console.log(prod);

</script>

<%--<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>--%>