<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>


<html>
    <head>
        <title>Home</title>
    </head>

    <body>
    <ul>
        <li>
            <a href="/login"></a>
        </li>
        <li>
            <sec:authorize access="isAuthenticated()" >
                   <p><sec:authentication property="principal.username" /></p>
            </sec:authorize>
        </li>
    </ul>
    </body>


    <footer>

    </footer>
</html>