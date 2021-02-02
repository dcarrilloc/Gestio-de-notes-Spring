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
<body>
<script>
    window.fbAsyncInit = function () {
        FB.init({
            appId: '1397628140595505',
            cookie: true,
            xfbml: true,
            version: 'v9.0'
        });

        FB.AppEvents.logPageView();
        FB.getLoginStatus(function(response){
            if(response.session){
                top.location.href="http://localhost:8080/feed";
                window.close();
            }
            else{
                top.location.href="http://localhost:8080/auth/oauth2facebookcallback/";
                window.close();
            }
        })
    };

    (function (d, s, id) {
        var js, fjs = d.getElementsByTagName(s)[0];
        if (d.getElementById(id)) {
            return;
        }
        js = d.createElement(s);
        js.id = id;
        js.src = "https://connect.facebook.net/en_US/sdk.js";
        fjs.parentNode.insertBefore(js, fjs);
    }(document, 'script', 'facebook-jssdk'));
</script>
<div id="fb-root"></div>
<script async defer crossorigin="anonymous"
        src="https://connect.facebook.net/es_ES/sdk.js#xfbml=1&autoLogAppEvents=1&version=v9.0&appId=1397628140595505"
        nonce="rhL1wNuT"></script>
<script>
    window.open("${url}", "login facebook", "width=600, height=800");
</script>

<div class="fb-login-button" data-width="" data-size="large" data-button-type="continue_with"
     data-layout="default" data-auto-logout-link="false" data-use-continue-as="true"></div>

</body>
</html>
