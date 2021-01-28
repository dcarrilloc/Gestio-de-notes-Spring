<%--
  Created by IntelliJ IDEA.
  User: Dani
  Date: 14/11/2020
  Time: 16:38
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>@${user.username}</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
</head>
<body style="margin: 0; padding: 0; box-sizing: border-box; width: 100%; background-color: #303030; color: white;">
<jsp:include page="header.jsp" />

<div style="width: 100%; margin: 50px auto 100px auto; text-align: center;">
    <h1>Welcome, ${user.username}!</h1>
</div>

<div style="display: flex; flex-flow: column wrap; width: 40%; justify-content: center; align-items: center; background-color: #424242 !important; border-radius: 15px; margin: 120px auto;">
    <div style="display: flex; flex-flow: column wrap; align-items: center; justify-content: center;position: relative; bottom: 50px;">
        <img src="https://i.pinimg.com/564x/b2/09/48/b209480353c489e0f0616131d585defd.jpg" id="imgshow" alt="profile pic" style="width: 150px; height: 150px; border-radius: 50% !important; object-fit: cover;">
        <input type="file" id="imgload" class="form-control-file" name="profile-pic" style="width: 150px; border-radius: 50%; flex-basis: 100%;"/>
    </div>

    <script>
        // Little code to charge the input file on the img
        $('document').ready(function () {
            $("#imgload").change(function () {
                if (this.files && this.files[0]) {
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        $('#imgshow').attr('src', e.target.result);
                    }
                    reader.readAsDataURL(this.files[0]);
                }
            });
        });
    </script>

    <div class="tab-pane" id="edit">
        <h3>Edit your profile</h3>
        <form action="${pageContext.request.contextPath}/profile" method="post">
            <div class="form-group row">
                <label class="col-lg-3 col-form-label form-control-label">Username</label>
                <div class="input-group col-lg-9">
                    <input class="form-control ${usernameValidation}" type="text" value="${user.username}" name="username">
                    <c:if test = "${status == 1}">
                        <div class="invalid-feedback">
                            Your username must contain at least 3 characters and must be made up of only uppercase or lowercase letters, numbers, dots or underscore.
                        </div>
                    </c:if>
                    <c:if test = "${status == 2 || status == 3 || status == 4 || status == 5}">
                        <div class="valid-feedback">
                            This looks good!
                        </div>
                    </c:if>
                    <c:if test = "${status == 6}">
                        <div class="invalid-feedback">
                            This username already exists. Please try another one!
                        </div>
                    </c:if>
                    <c:if test = "${status == 7}">
                        <div class="invalid-feedback">
                            You didn't changed anything!
                        </div>
                    </c:if>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-lg-3 col-form-label form-control-label">Email</label>
                <div class="input-group col-lg-9">
                    <input class="form-control ${emailValidation}" type="email" value="${user.email}" name="email">
                    <c:if test = "${status == 2}">
                        <div class="invalid-feedback">
                            Please provide a valid email.
                        </div>
                    </c:if>
                    <c:if test = "${status == 1 || status == 3 || status == 4 || status == 5 || status == 6}">
                        <div class="valid-feedback">
                            This looks good!
                        </div>
                    </c:if>
                    <c:if test = "${status == 7}">
                        <div class="invalid-feedback">
                            You didn't changed anything!
                        </div>
                    </c:if>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-lg-3 col-form-label form-control-label">Password</label>
                <div class="input-group col-lg-9">
                    <input class="form-control ${passwordValidation}" type="password" value="" name="pass1">
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
                    <c:if test = "${status == 7}">
                        <div class="invalid-feedback">
                            You didn't changed anything!
                        </div>
                    </c:if>
                    <c:if test = "${status == 8}">
                        <div class="invalid-feedback">
                            You have to enter your password to confirm the changes!
                        </div>
                    </c:if>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-lg-3 col-form-label form-control-label">Confirm password</label>
                <div class="input-group col-lg-9">
                    <input class="form-control" type="password" value="" name="pass2">
                </div>
            </div>
            <div class="form-group row">
                <label class="col-lg-3 col-form-label form-control-label"></label>
                <div class="input-group col-lg-9">
                    <a class="navbar-brand" href="${pageContext.request.contextPath}/feed">Cancel</a>
                    <input type="submit" class="btn btn-primary" value="Save Changes">
                </div>
            </div>
            <input type="hidden" name="_csrftoken" value="${csrfToken}">
        </form>
    </div>
</div>

</body>
</html>
