<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="/css/order/order-list.css" rel="stylesheet">

<section>
  <table>
     <thead>
        <tr>
            <th>주문일</th>
            <th>주문번호</th>
            <th>주문 상품</th>
        </tr>
     </thead>
     <tbody>
           <%-- <c:forEach var="order" items="${orderList}" begin="0" end="${pageSize}"> --%>
           <c:forEach var="order" items="${orderList}">
           <tr>
              <td>${order.orderDate}</td>
              <td>${order.orderNo}</td>
              <td>${order.prodName}</td>
           </tr>
           </c:forEach>
     </tbody>
  </table>

  <jsp:include page="/WEB-INF/jsp/common/pagination.jsp"/>
</section>
