<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="/css/home/home.css">
<script src="/js/cart/cart.js" defer></script>

<!DOCTYPE html>


    <ul>
        <li>
            <p>MAIN HOME</p>
        </li>
        <li>
            <sec:authorize access="isAuthenticated()" >
               <p>접속 유저 : <sec:authentication property="principal.username" /></p>
            </sec:authorize>
            <sec:authorize access="isAnonymous()" >
               <p>현재 비로그인 상태입니다.</p>
            </sec:authorize>


            <p>홈 구성 추가 구성 중 .</p>

            <p>1. 메인 배너나 캐러셀</p>
            <p>2. 베스트셀러나 추천 상품 (product_master에 count 추가하자)</p>
            <div class="px-2 py-2 div-prod">
             <%-- 라이브러리 사용 가능성, SEO, 스크린 리더 등 감안. --%>
                <ul class="list-unstyled ul-prod">
                    <c:forEach var="item" items="${bestList}">
                        <li class="col-6">
                            <div class="card h-100 custom-best-card">
                              <div class="product-image-container">
                                  <c:choose>
                                    <c:when test="${empty item.imageUrl}">
                                        <a href="/product/detail/${item.prodNo}">
                                            <img src="/assets/images/No_Image.png" class="card-img-top product-image" alt="기본 이미지">
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="/product/detail/${item.prodNo}">
                                            <img src="${item.imageUrl}" class="card-img-top product-image" alt="상품 이미지">
                                        </a>
                                    </c:otherwise>
                                  </c:choose>
                              </div>
                              <div class="card-body d-flex flex-column justify-content-between">
                                <div class="product-title-container">
                                    <h6 class="card-title">
                                            <a href="/product/detail/${item.prodNo}" style="text-decoration: none;">
                                        ${item.prodName}
                                        </a>
                                    </h6>
                                </div>
                                <div class="product-info-container">
                                    <p class="card-text">${item.price}원</p>
                                    <p class="card-text">
                                        <c:choose>
                                            <c:when test="${item.prodType == 'DIGITAL'}">
                                                다운로드 상품
                                            </c:when>
                                            <c:when test="${item.prodType == 'PHYSICAL'}">
                                                배송 상품
                                            </c:when>
                                            <c:otherwise>
                                                기타
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                </div>
                                <c:choose>
                                    <c:when test="${item.isInLyb==true}">
                                        <button class="btn btn-primary btn-sm w-100 in-cart-btn" data-prodno="${item.prodNo}" onclick="location.href='/library/my'">소유 중</button>
                                    </c:when>
                                    <c:when test="${item.isInCart==false}">
                                        <button class="btn btn-primary btn-sm w-100 add-to-cart-btn" data-prodno="${item.prodNo}">장바구니 담기</button>
                                        <input type="hidden" class="qty" value="1" />
                                    </c:when>
                                    <c:otherwise>
                                        <button class="btn btn-primary btn-sm w-100 in-cart-btn" data-prodno="${item.prodNo}" onclick="location.href='/cart/cartlist'">담기 완료</button>
                                    </c:otherwise>
                                </c:choose>
                              </div>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </div>

            <p>3. 신규 출시 상품</p>

        </li>
    </ul>
