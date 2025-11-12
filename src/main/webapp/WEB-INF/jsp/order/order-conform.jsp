<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<%@ include file="/WEB-INF/views/common/header.jsp" %>--%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/order/order-conform.css">

    <section class="container mt-4">

        <h2>주문 완료</h2>
        <p>주문번호 ${orderId}</p>
        <a href="/order/ordlist">주문 리스트 확인</a>

        <h2>상품 정보</h2>
        <table>
            <th>이미지</th>
            <th>상품명</th>
            <th>상품 가격</th>
            <th>주문 수량</th>

            <c:forEach var="item" items="${orderInfo.ordInfoProdList}">
                <tr>
                    <td>이미지</td>
                    <td>${item.prodName}</td>
                    <td>${item.price}</td>
                    <td>${item.orderQty}</td>
                </tr>
            </c:forEach>
        </table>



        <%-- <img src="${orderInfo.orderInfoProductDtos[0].imageList[0].imageUrl}" /> --%>
    </section>


<%--<%@ include file="/WEB-INF/views/common/footer.jsp" %>--%>