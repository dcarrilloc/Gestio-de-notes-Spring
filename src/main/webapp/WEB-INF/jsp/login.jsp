<%--
  Created by IntelliJ IDEA.
  User: Dani
  Date: 11/11/2020
  Time: 1:29
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z" crossorigin="anonymous">
    <title>Login</title>
</head>
<body style="margin: 0; padding: 0; box-sizing: border-box; width: 100%; background-color: #303030; color: white;">
<h1 style="margin: 100px auto auto; text-align: center">Notes Manager | Login</h1>
<div style="width: 20%; margin: 50px auto auto;" >
    <form method="POST" action="${pageContext.request.contextPath}/login">
        <div class="form-group">
            <label>Email</label>
            <div class="input-group">
                <input type="text" class="form-control ${usernameValidation}" placeholder="chucknorris@gmail.com" aria-label="Email" aria-describedby="addon-wrapping" name="email" value="${username}" style="background-color: #424242 !important; color: white;">
                <c:if test = "${status == 1}">
                    <div class="invalid-feedback">
                        We couldn't find this user in our database. You may want to register first.
                    </div>
                </c:if>
                <c:if test = "${status == 2}">
                    <div class="valid-feedback">
                        This looks good!
                    </div>
                </c:if>
            </div>
        </div>
        <div class="form-group">
            <label for="exampleInputPassword1">Password</label>
            <input type="password" class="form-control ${password}" id="exampleInputPassword1" name="password" style="background-color: #424242 !important; color: white;">
            <c:if test = "${status == 2}">
                <div class="invalid-feedback">
                    Invalid password. Let's try that again!
                </div>
            </c:if>
        </div>
        <input type="hidden" name="_csrftoken" value="${csrfToken}">
        <button type="submit" class="btn btn-primary">Submit</button>
    </form>
    <div style="margin-top: 20px;">
        <p>Don't have an account?</p>
        <a href="${pageContext.request.contextPath}/register" style="text-decoration: none; color: rgba(255, 255, 255, 0.7);">Create new account.</a>
    </div>
</div>
</body>
</html>
