<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<%@ include file="/WEB-INF/jsp/layout/main-layout.jsp" %>--%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/product_detail.css">


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
      <div class="product-detail-desc">
         <c:out value="${itemList.detailDesc}" escapeXml="false" />
      </div>
      <button class="btn btn-primary add-to-cart-btn" data-prodno="${itemList.prodNo}">장바구니 담기</button>
    </section>
<%--</main>--%>


<script src="/js/cart/cart.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        document.querySelectorAll('.add-to-cart-btn').forEach(function (btn) {
            btn.addEventListener('click', function() {
                const prodNo = this.dataset.prodno;

                addToCart(prodNo).then(data => {
                    if (data.success) {
                       if (confirm("담기 성공했습니다. 장바구니로 이동하시겠습니까?")) {
                            window.location.href="cart/cartlist";
                       } else {

                       }

                    } else {
                        alert(data.message);
                    }
                }).catch(err => {
                    alert("에러 발생 : " + err.message);
                });
            });
        });
    });

</script>

<%--<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>--%>