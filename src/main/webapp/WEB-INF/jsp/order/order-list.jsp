<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="/js/order/order-list.js" defer></script>
<link href="/css/order/order-list.css" rel="stylesheet">

<section>
  <div id="ordListContainer">
      <table>
         <thead>
            <tr>
                <th id="ord-date">주문일</th>
                <th id="ord-no">주문번호</th>
                <th id="ord-prod">주문 상품</th>
                <th id="ord-type">상품 종류</th>
            </tr>
         </thead>
         <tbody id="ordListTbody">
               <%-- <c:forEach var="order" items="${orderList}" begin="0" end="${pageSize}"> --%>
               <c:choose>
                   <c:when test="${not empty orderList}">
                           <c:forEach var="order" items="${orderList}">
                               <tr>
                                  <td class="ord-date">${order.orderDate}</td>
                                  <td class="ord-no">${order.orderNo}</td>
                                  <td class="ord-prod">${order.prodName}</td>
                                  <td class="ord-type">
                                  <c:choose>
                                            <c:when test="${order.prodType == 'DIGITAL'}">
                                                다운로드
                                            </c:when>
                                            <c:when test="${order.prodType == 'PHYSICAL'}">
                                                배송
                                            </c:when>
                                            <c:otherwise>
                                                기타
                                            </c:otherwise>
                                  </c:choose>
                                  </td>
                               </tr>
                           <%--    <c:set var="currentDataCount" value="${status.count}" /> --%>
                           </c:forEach>

                           <%-- <p>test : "${currentDataCount}"</p> <p>pageSize : "${pagination.pageSize}" </p>

                             <c:if test="${currentDataCount < pagination.pageSize}" >
                             <jsp:include page="../common/emptyRowFilter.jsp">
                                 <jsp:param name="dataCount" value="${currentDataCount}" />
                                 <jsp:param name="colCount" value="3" />
                                 <jsp:param name="pageSize" value="${pagination.pageSize}" />
                              </jsp:include>
                           </c:if> --%>
                   </c:when>
                   <c:otherwise>
                        <tr>
                            <td colspan="4">주문 내역이 없습니다.</td>
                        </tr>
                   </c:otherwise>
               </c:choose>
         </tbody>
      </table>
  </div>
  <jsp:include page="/WEB-INF/jsp/common/pagination.jsp"/>
</section>

<script>
    const pageSize = ${pagination.pageSize};
</script>
