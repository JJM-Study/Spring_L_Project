<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>



<c:set var="dataCount" value="${param.dataCount}" />
<c:set var="pageSize" value="${param.pageSize}" />
<c:set var="colCount" value="${param.colCount}" />

<c:set var="remainder" value="${pageSize-dataCount}" />
<%-- <p>param.dataCount: **${param.dataCount}**</p>
<p>${pageSize}</p>
<p>${colCount}</p> --%>
<c:if test="${remainder > 0}">
    <c:forEach begin="1" end="${remainder}">
        <tr>
            <td colspan="${colCount}">&nbsp;</td>
        </tr>
    </c:forEach>
</c:if>