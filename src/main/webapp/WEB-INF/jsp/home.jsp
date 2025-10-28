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
        </li>
    </ul>
