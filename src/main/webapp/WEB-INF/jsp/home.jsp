<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

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

            <p>3. 신규 출시 상품</p>

        </li>
    </ul>
