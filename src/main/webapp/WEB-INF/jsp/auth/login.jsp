<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth/login.css">

<!DOCTYPE html>

<html>

    <head>
        <title>LOGIN FORM TEST</title>
        <script src="/js/auth/login.js" defer></script>
    </head>
    <body>
        <div>
            <form action="/login" method="POST">
                <h1>LOGIN</h1>
                <div id="input-user-info">
                    <label><i class="bi bi-person-fill"></i><input type="text" name="username" id="id-input"></label>
                    <label><i class="bi bi-lock-fill"></i><input type="password" name="password" id="pass-input"></label>
                </div>
                <div>
                    <span>remember-me</span>
                    <input type="checkbox" name="remember-me">
                </div>
                <div>
                    <button type="submit">로그인</button>
                </div>

                <button type="button" id="sign-up">회원가입</button>
                <input type="hidden" name="_csrf" value="${_csrf.token}">
                <p>${errorMessage}</p>

                <button type="button" class="btn" onclick="location.href='/home'">로그인 없이 이용하기</button>

            </form>
        </div>
    </body>


</html>