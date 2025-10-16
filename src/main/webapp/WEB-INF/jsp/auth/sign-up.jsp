<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">


<!DOCTYPE html>


<html>
    <head>
        <title>회원가입</title>
        <script src="/js/common/public.js" defer></script>
        <script src="/js/auth/sign-up.js" defer></script>
        <meta name="_csrf" content="${_csrf.token}">
    </head>

    <body>
        <form id="signUpForm">
            <label>
                아이디 : <input type="text" name="username"/>
                <button type="button" id="existId">중복확인</button>
            </label>
            <label>
                비밀번호 : <input type="password" name="password" /><button type="button" class="showPwBtn"><i class="bi bi-eye"></i></button>
            </label>
            <label>
                비밀번호 재확인 : <input type="password" name="check-password" /><button type="button" class="showPwBtn"><i class="bi bi-eye"></i></button>
            </label>

            <button type="submit" id="signUpButton">회원가입</button>
            <input type="hidden" name="_csrf" value="${_csrf.token}">
        </form>
    </body>
</html>