<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>

<html>

    <head>
        <title>LOGIN FORM TEST</title>
        <script src="/js/auth/login.js" defer></script>
    </head>
    <body>
        <form action="/login" method="POST">
            <label>아이디 : <input type="text" name="username"></label>
            <label>비밀번호 : <input type="password" name="password"></label>
            <button type="submit">로그인</button>
            <span>remember-me</span>
            <input type="checkbox" name="remember-me">
            <button type="button" id="sign-up">회원가입</button>
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <p>${errorMessage}</p>

            <button type="button" class="btn" onclick="location.href='/home'">로그인 없이 이용하기</button>

        </form>
    </body>


</html>