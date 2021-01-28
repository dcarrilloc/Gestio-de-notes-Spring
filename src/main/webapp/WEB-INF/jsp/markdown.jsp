<%--
  Created by IntelliJ IDEA.
  User: Dani
  Date: 17/11/2020
  Time: 11:39
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Note</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
</head>
<body style="margin: 0; padding: 0; box-sizing: border-box; width: 100%; background-color: #303030; color: white;">
<jsp:include page="header.jsp" />

<div style="width: 40%; margin: 200px auto;">
    <form action="${pageContext.request.contextPath}/editNote" method="POST">
        <div style="background-color: #424242 !important; border-radius: 5px;">
            <div class="form-group" style="height: 50px; padding: 1rem 2rem 0 2rem;">
                <input type="text" id="exampleFormControlInput1" name="title" value="${note.title}" placeholder="Title" style="background-color: #424242 !important; border: none; color: white; font-size: larger; width: 100%; outline: none;">
            </div>
            <div class="form-group" style="padding: 1rem 2rem 0 2rem;">
                <textarea id="exampleFormControlTextarea1" rows="15" name="body" placeholder="Body" style="background-color: #424242 !important; border: none; color: white; width: 100%; outline: none;">${note.body}</textarea>
            </div>
        </div>
        <input type="hidden" name="noteid" value="${note.noteid}">
        <input type="hidden" name="_csrftoken" value="${csrfToken}">
        <button type="submit" class="btn btn-success">Save changes</button>
    </form>
</div>

</body>
</html>
