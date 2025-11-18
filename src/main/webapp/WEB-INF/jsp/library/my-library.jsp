<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<%@ include file="/WEB-INF/views/common/header.jsp" %>--%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/library/my-library.css">
<script src="/js/library/my-library.js" defer></script>

<section>
    <h1>내 서재</h1>
    <div id="div-item-list">
      <ul>
         <c:forEach var="item" items="${library}" >
             <li>
                <div class="div-item">
                    <div class="div-item-info">
                        <div class="div-item-img">
                            <c:choose>
                                <c:when test="${item.imageUrl == null}">
                                    <img src="/assets/images/No_Image.png" alt="상품 이미지" />
                                </c:when>
                                <c:otherwise>
                                    <img src="${item.imageUrl}" alt="상품 이미지" />
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="div-item-detail">
                            <h3><a href="/product/detail/${item.prodNo}">${item.prodName}</a></h3>
                            <p>${item.sellerId}</p>
                            <p>${item.lybRegDt}</p>
                        </div>
                    </div>
                    <div class="div-download">
                        <button type="button" class="btn-download btn btn-primary" onclick="downloadFile('${item.prodNo}')">Download</button>
                    </div>
                </div>
             </li>
          </c:forEach>
      </ul>
    </div>
</section>