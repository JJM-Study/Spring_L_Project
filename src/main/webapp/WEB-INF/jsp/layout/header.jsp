<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="_csrf" content="${_csrf.token}">
    <title><c:out value="${pageTitle}" default="Project_Shop" /></title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="/js/layout/header.js" defer></script>
    <script src="/js/common/public.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <link href="/css/layout/header.css" rel="stylesheet">
</head>
<body>
<div class="wrapper d-flex flex-column min-vh-100">

<c:if test="${showNav !=false}">
    <header>
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
          <div class="container-fluid">
            <a class="navbar-brand" href="/">ShopLegacy</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarNav" aria-controls="navbarNav"
                    aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="/">홈</a>
                    </li>
                     <li class="nav-item">
                        <a class="nav-link" href="/cart/cartlist">장바구니</a>
                     </li>
                     <li class="nav-item">
                        <a class="nav-link" href="/product/products">상품</a>
                     </li>
                     <li class="nav-item">
                        <a class="nav-link" href="/">메뉴</a>
                     </li>
                     <li class="nav-item">
                         <ul>
                             <sec:authorize access="isAuthenticated()">
                                    <a class="nav-link logout" href="#">로그아웃</a>
                             </sec:authorize>
                             <sec:authorize access="isAnonymous()">
                                   <a class="nav-link" href="/login">로그인</a>
                             </sec:authorize>
                         </ul>
                     </li>
                     <li class="nav-item">
                        <a class="nav-link" href="/errorrrr">전역 에러 테스트용</a>
                     </li>
                     <li class="nav-item">
                         <sec:authorize access="isAuthenticated()">
                            <div class="userInfo">
                                안녕하세요! <sec:authentication property="principal.username" />님!
                                <%-- 드롭다운 추가해서, 유저 ID 클릭 시 로그아웃 버튼이 나오도록 하는 방법 고민. --%>
                            </div>
                         </sec:authorize>
                     </li>

                </ul>
            </div>
          </div>
        </nav>
    </header>
</c:if>
