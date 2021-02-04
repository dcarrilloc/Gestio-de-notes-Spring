<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <script type="text/javascript" src="https://cdn.jsdelivr.net/jquery/latest/jquery.min.js"></script>
    <script type="text/javascript" src="https://cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css"/>
    <title>Note Manager</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css"
          integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            height: 100vh;
            background-color: #424242 !important;
        }

        .boddy-wrapper {
            height: 100vh;
            color: white;
            display: flex;
            flex-flow: column nowrap;
        }

        .h-wrapper {
            margin-top: 150px;
            margin-bottom: 50px;
        }

        .h-wrapper > h1, .h-wrapper > h4 {
            width: 100%;
            text-align: center;
        }

        h1 {
            font-size: 3rem;
        }

        h4 {
            font-size: 2rem;
        }

        .signup-signin-wrapper {
            display: flex;
            flex-flow: column nowrap;
            margin: 0 auto;
            justify-content: center;
            padding: 30px;
            background-color: #303030;
            border-radius: 3px;
            width: 25%;
        }

        .native-wrapper {
            width: 100%;
            display: flex;
            flex-flow: row nowrap;
            justify-content: space-evenly;
        }

        .auth-wrapper, .auth-wrapper:hover {
            padding: 5px 14px;
            text-decoration: none;
            color: white;
            background-color: #424242;
            border-radius: 0.25rem;
            border: solid 1px white;
        }

        .button {
            background-color: white;
            border-radius: .25rem;
            font-weight: bold;
            margin-bottom: 20px;
        }

        .twitter {
            color: white;
            background-color: #1DA1F2;
        }

        .btn {
            width: 100%;
        }

    </style>
</head>
<body>

<div class="boddy-wrapper">
    <div class="h-wrapper">
        <h1>Note Manager</h1>
        <h4>Get Started!</h4>
    </div>

    <div class="signup-signin-wrapper">
        <h5 style="text-align: center; margin-bottom: 20px">Sign in to Note Manager</h5>
        <div class="button google">
            <div>
                <a class="btn btn-outline-dark font-weight-bold" href="/googleLogin" role="button" style="text-transform:none">
                    <img width="20px" style="margin-bottom:3px; margin-right:5px" alt="Google sign-in" src="https://upload.wikimedia.org/wikipedia/commons/thumb/5/53/Google_%22G%22_Logo.svg/512px-Google_%22G%22_Logo.svg.png" />
                    Continue with Google
                </a>
            </div>
        </div>

        <div class="button twitter">
            <div>
                <a class="btn btn-outline-dark font-weight-bold" href="/twitterLogin" role="button" style="text-transform:none">
                    <img width="20px" style="margin-bottom:3px; margin-right:5px" alt="Twitter sign-in" src="http://pngimg.com/uploads/twitter/twitter_PNG15.png" />
                    Continue with Twitter
                </a>
            </div>
        </div>

        <!--a href="${pageContext.request.contextPath}/facebookLogin">Facebook</a-->

        <p style="margin: auto; text-align: center">or</p>
        <div class="native-wrapper">
            <a href="/register" class="register-wrapper auth-wrapper">Register</a>
            <a href="/login" class="login-wrapper auth-wrapper">Sign In</a>
        </div>
    </div>
</div>
</body>
</html>
