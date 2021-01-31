<%--
  Created by IntelliJ IDEA.
  User: danie
  Date: 26/11/2020
  Time: 19:03
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark" style="background-color: #424242 !important; height: 80px;">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/feed">Note Manager</a>
    <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a class="nav-link" href="${pageContext.request.contextPath}/profile">@${username}</a>
            </li>
            <li class="nav-item">
                <form action="${pageContext.request.contextPath}/logout" method="post" style="position: relative; top: 7px;">
                    <input type="hidden" name="_csrftoken" value="${csrfToken}">
                    <button type="submit" class="text-danger" style="background-color: #424242; border-radius: 5px; border: none;">Logout</button>
                </form>
            </li>
        </ul>
    </div>
</nav>
