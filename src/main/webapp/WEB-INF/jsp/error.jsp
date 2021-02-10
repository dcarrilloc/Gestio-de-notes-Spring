<%--
  Created by IntelliJ IDEA.
  User: Dani
  Date: 07/02/2021
  Time: 11:52
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css"
          integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
</head>
<body style="height: 100vh; width: 100%; background-color: #303030; display: flex; flex-flow: column nowrap; justify-content: flex-start;">
<jsp:include page="header.jsp"/>
<h1 style="margin-top: 150px; width: 100%; text-align: center; color: white; ">Upssss</h1>
<div class="alert alert-danger" role="alert" style="text-align: center; margin: 50px auto;">
    ${err}
</div>
</body>
</html>
