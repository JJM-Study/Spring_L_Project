<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--<%@ include file="/WEB-INF/jsp/layout/main-layout.jsp" %>--%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/product/product_detail.css">

<%
    System.out.println("=== JSP 디버깅 ===");
    System.out.println("itemDetailJson: " + request.getAttribute("itemDetailJson"));
    System.out.println("itemList: " + request.getAttribute("itemList"));

%>


<%--<main class="flex-grow-1">--%>
    <section class="detail-container mt-4">
        <div class="info-container">
            <div class="image-container">
                   <div class="main-image-container">
                       <img src="${mainImages.imageUrl}"
                             alt="${'메인 상품 이미지'}"
                             class="card-img-top"/>
                   </div>
                   <div class="image-sub-container">
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

            <div id="basic-info-container" class="info-sub-container">
            <div id="basic-info-header">
                <h5>${itemList.prodName}</h5>
            </div>
                <table>
                  <tr>
                    <td id="js-price">가격 : ${itemList.price}원</td>
                  </tr>
                  <tr>
                    <td>판매일 : ${itemList.salesDtFormatted}</td>
                  </tr>
                  <tr>
                    <td>판매자 : ${itemList.sellerId}</td>
                  </tr>
                </table>
            </div>

            <div class="product-detail">

               <div class="info-title">
                   <h2>상세 내용</h2>
               </div>
               <div id="prod-detail" class="info-sub-container">
                   <c:choose>
                       <c:when test="${not empty itemList.detailDesc}">
                           <%-- <pre style="white-space:pre-wrap; background:#f7f7f7; padding:8px;">
                             RAW: [<c:out value="${itemList.detailDesc}" />]
                           </pre> --%>

                           <div><c:out value="${itemList.detailDesc}" escapeXml="false" /></div>
                       </c:when>
                       <c:otherwise>
                           <p>상품 정보가 없습니다.</p>
                       </c:otherwise>
                   </c:choose>
               </div>
               <div class="info-title"><h2>공지</h2></div>
               <div id="notice" class="info-sub-container">
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
        </div>
        <div class="order-container">
            <div>
                <div class="qtyBtn">
                    <button id="pBtn" type="button">+</button>
                    <input type="number" class="qty" value="1" min="1"/>
                    <button id="mBtn" type="button">-</button>
                </div>
                <p>가격 : <span id="total-price">${itemList.price}</span></p>
                <div class="orderBtn">
                  <c:choose>
                    <c:when test="${isInCart == false}">
                        <button class="btn btn-primary add-to-cart-btn" data-prodno="${itemList.prodNo}">장바구니 담기</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-primary in-cart-btn" onclick="location.href='/cart/cartlist'">장바구니에 있음</button>
                    </c:otherwise>
                  </c:choose>
                  <button class="btn btn-primary btn-ord-now" data-prodno="${itemList.prodNo}">주문하기</button>
               </div>
           </div>
        </div>


    </section>

            <input type="hidden" id="console" value='${itemDetailJson}'>

<%--</main>--%>



<script src="/js/cart/cart.js"></script>
<script src="/js/product/product_detail.js" defer></script>
<script>
    // 콘솔 정보 출력도 모듈화 하는 것을 고민.

    function checkLog() {
        const prod = document.getElementById("console").value;

        console.log("===== 상품의 JSON 데이터 =====");
        console.log(prod);
        console.log("======");
    }

    console.log("상품의 세부 JSON 정보 확인을 원할 시 prod() 입력.");
    window.prod = checkLog;

</script>
<%--<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>--%>