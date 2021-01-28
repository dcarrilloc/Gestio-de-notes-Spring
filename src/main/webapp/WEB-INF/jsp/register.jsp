<%--
  Created by IntelliJ IDEA.
  User: Dani
  Date: 09/11/2020
  Time: 21:48
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z" crossorigin="anonymous">
    <title>Register</title>
</head>
<body style="margin: 0; padding: 0; box-sizing: border-box; width: 100%; background-color: #303030; color: white;">
<h1 style="margin: 100px auto auto; text-align: center">Notes Manager | Create new account</h1>
<div style="width: 30%; margin: 50px auto auto;" >
    <form method="POST" action="${pageContext.request.contextPath}/register">
        <div class="form-group">
            <label for="exampleInputEmail1">Username</label>
            <div class="input-group">
                <div class="input-group-prepend" style="background-color: #424242 !important;">
                    <span class="input-group-text" id="addon-wrapping">@</span>
                </div>
                <input type="text" class="form-control ${usernameValidation}" placeholder="chucknorris" aria-label="Username" aria-describedby="addon-wrapping" name="username" value="${username}" style="background-color: #424242 !important; color: white;">
                <c:if test = "${status == 1}">
                    <div class="invalid-feedback">
                        Your username must contain at least 3 characters and must be made up of only uppercase or lowercase letters, numbers, dots or underscore.
                    </div>
                </c:if>
                <c:if test = "${status == 2 || status == 3}">
                    <div class="valid-feedback">
                        This looks good!
                    </div>
                </c:if>
                <c:if test = "${status == 4}">
                    <div class="invalid-feedback">
                        This username already exists. Please try another one!
                    </div>
                </c:if>
            </div>
        </div>
        <div class="form-group">
            <label for="exampleInputEmail1">Email address</label>
            <input type="email" class="form-control ${emailValidation}" placeholder="chucknorris@esliceu.net"  id="exampleInputEmail1" aria-describedby="emailHelp" name="email" value="${email}" style="background-color: #424242 !important; color: white;">
            <c:if test = "${status == 2}">
                <div class="invalid-feedback">
                    Please provide a valid email.
                </div>
            </c:if>
            <c:if test = "${status == 1 || status == 3}">
                <div class="valid-feedback">
                    This looks good!
                </div>
            </c:if>
        </div>
        <div class="form-group">
            <label for="exampleInputPassword1">Password</label>
            <input type="password" class="form-control ${passwordValidation}" id="exampleInputPassword1" name="password1" style="background-color: #424242 !important; color: white;">
            <c:if test = "${status == 3}">
                <div class="invalid-feedback">
                    Your password must be at least 8 characters long and must not contain blank spaces.
                </div>
            </c:if>
            <c:if test = "${status == 4}">
                <div class="invalid-feedback">
                    Passwords does not match. Try again.
                </div>
            </c:if>
            <c:if test = "${status == 5}">
                <div class="invalid-feedback">
                    Password must not be the same as the username.
                </div>
            </c:if>
        </div>
        <div class="form-group">
            <label for="exampleInputPassword2">Repeat your password</label>
            <input type="password" class="form-control ${passwordValidation}" id="exampleInputPassword2" name="password2" style="background-color: #424242 !important; color: white;">
            <small id="emailHelp" class="form-text text-muted">We'll never share your data with anyone else. We promise.</small>
        </div>
        <input type="hidden" name="_csrftoken" value="${csrfToken}">
        <button type="submit" class="btn btn-primary">Submit</button>
    </form>
    <div style="margin-top: 20px;">
        <p>Already have account?</p>
        <a href="${pageContext.request.contextPath}/login" style="text-decoration: none; color: rgba(255, 255, 255, 0.7);">Sign In.</a>
    </div>
</div>
</body>
</html>
