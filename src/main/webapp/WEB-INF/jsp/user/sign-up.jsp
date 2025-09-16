<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">


<!DOCTYPE html>

<html>
    <head>
        <title>회원가입</title>
    </head>

    <body>
        <form method="POST" action="#">
            <label>
                아이디 : <input type="text" />
            </label>
            <label>
                비밀번호 : <input type="password" /><button><i class="bi bi-eye"></i></button>
            </label>
            <label>
                비밀번호 재확인 : <input type="password"><button><i class="bi bi-eye"></i></button>
            </label>

            <button type="submit">회원가입</button>
        </form>
    </body>
</html>