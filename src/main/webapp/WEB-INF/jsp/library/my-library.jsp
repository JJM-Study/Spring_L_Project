<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<%@ include file="/WEB-INF/views/common/header.jsp" %>--%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/library/my-library.css">


<section>
    <h1>내 서재</h1>
    <div id="div-item-list">
      <ul>
         <c:forEach var="item" items="${library}" >
             <li>
                <div class="div-item">
                    <div class="div-item-info">
                        <div class="div-item-img"><img src="/assets/images/No_Image.png" /></div>
                        <div class="div-item-detail">
                            <h3>Title</h3>
                            <p>seller</p>
                            <p>purchase-date</p>
                        </div>
                    </div>
                    <div class="div-download">
                        <button type="button" class="btn-download btn btn-primary">Download</button>
                    </div>
                </div>
             </li>
          </c:forEach>
      </ul>
    </div>
</section>