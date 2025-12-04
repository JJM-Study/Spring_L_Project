<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<nav aria-abel="Pagination" id="page">
    <ul class="pagination">

    <c:set var="baseUrl" value="${pageUrl}" />

            <!-- 이전 -->
            <c:url var="prevUrl" value="${baseUrl}">
                <c:param name="cPage" value="${pagination.prevPage}" />
                <c:if test = "${not empty title}">
                    <c:param name="title" value="${title}" />
                </c:if>
            </c:url>
            <li class="page-item">
                <a class="page-link" href="${prevUrl}">이전</a>
            </li>
            <c:forEach var="i" begin="${pagination.startPage}" end="${pagination.endPage}" step="1">
                <c:url var="pageUrl" value="${baseUrl}" >
                    <c:param name="cPage" value="${i}" />
                    <c:param name="pageSize" value="${pagination.pageSize}" />
                    <c:if test="${not empty title}">
                        <c:param name="title" value="${title}" />
                    </c:if>
                </c:url>
                <%-- <li class="page-item"><a class="page-link" href="${pageUrl}">${i}</a></li> --%>
                <li class="page-item <c:if test="${i eq pagination.curPage}">active</c:if>"><a class="page-link" href="${pageUrl}">${i}</a></li>

            </c:forEach>

            <c:url var="nextUrl" value="${baseUrl}">
                <c:param name="cPage" value="${pagination.nextPage}" />
                <c:if test="${not empty title}">
                   <c:param name="title" value="${title}" />
                </c:if>
            </c:url>
            <li class="page-item">
                <a class="page-link" href="${nextUrl}">다음</a>
            </li>
    </ul>
</nav>