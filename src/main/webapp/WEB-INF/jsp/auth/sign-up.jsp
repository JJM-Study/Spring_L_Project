<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth/sign-up.css">


<!DOCTYPE html>
<html>
    <head>
        <title>회원가입</title>
        <script src="/js/common/public.js" defer></script>
        <script src="/js/auth/sign-up.js" defer></script>
        <meta name="_csrf" content="${_csrf.token}">

    </head>

    <body>
        <div>
            <form id="signUpForm">
                <h1>SIGN UP</h1>

                <div class="form-group">
                    <label>
                        <i class="bi bi-person-fill"></i>
                        <input type="text" name="username" placeholder="아이디">
                        <button type="button" id="existId" class="check-btn">중복확인</button>
                    </label>
                    <div id="idMessage"></div>
                </div>

                <div class="form-group">
                    <label>
                        <i class="bi bi-lock-fill"></i>
                        <input type="password" name="password" placeholder="비밀번호">
                        <button type="button" class="icon-btn showPwBtn" tabindex="-1">
                            <i class="bi bi-eye"></i>
                        </button>
                    </label>
                </div>

                <div class="form-group">
                    <label>
                        <i class="bi bi-lock-fill"></i>
                        <input type="password" name="check-password" placeholder="비밀번호 재확인">
                        <button type="button" class="icon-btn showPwBtn" tabindex="-1">
                            <i class="bi bi-eye"></i>
                        </button>
                    </label>
                </div>

                <div class="button-group">
                    <button type="submit" id="signUpButton">회원가입</button>
                    <button type="button" class="back-btn" onclick="location.href='/login'">뒤로가기</button>
                </div>

                <input type="hidden" name="_csrf" value="${_csrf.token}">
            </form>
        </div>
    </body>
</html>