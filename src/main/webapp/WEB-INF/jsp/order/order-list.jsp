<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
        <tr>
           <c:forEach var="order" items="${orderList}" begin="0" end="9">
              <td>${order.orderDate}</td>
              <td>${order.orderNo}</td>
              <td>${order.prodName}</td>
           </c:forEach>
        </tr>
     </tbody>
  </table>
</section>
