<html>
<head>
    <script type="text/javascript" src="https://cdn.jsdelivr.net/jquery/latest/jquery.min.js"></script>
    <script type="text/javascript" src="https://cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" />
    <title>Note Manager</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
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
            justify-content: center;
            align-items: center;
        }

        .h-wrapper {
            margin-bottom: 70px;
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
            background-color: #303030;
            display: flex;
            flex-flow: column nowrap;
            justify-content: center;
            align-items: center;
            padding: 15px;
            border-radius: 3px;
        }

        .signup-signin-wrapper > * {
            text-align: center;
            flex-basis: 100%;
            border-radius: 3px;
            width: 300px;
        }

        .native-wrapper {
            display: flex;
            flex-flow: row wrap;
            width: 100%;
        }

        .register-wrapper {
            margin-top: 5px;
            flex-basis: 50%;
        }

        .login-wrapper {
            margin-top: 5px;
            flex-basis: 50%;
        }

        .auth-wrapper {
            border-radius: 3px;
            background-color: #424242;
            border: 1px solid white;
        }

        a {
            text-decoration: none;
            color: white;
            height: 100%;
            width: 100%;
            cursor: pointer;
        }

        a:hover {
            color: #ced4da;
            text-decoration: none;
        }

        .google-btn {
            display: flex;
            flex-flow: row nowrap;
            width: 100%;
            height: 35px;
            background-color: #4285f4;
            border-radius: 2px;
            box-shadow: 0 3px 4px 0 rgba(0, 0, 0, 0.25);
            cursor: pointer;
            padding: 1px;
        }

        .google-btn .google-icon-wrapper {
            position: relative;
            width: 15%;
            height: 100%;
            border-radius: 2px;
            background-color: #fff;
        }

        .google-btn .google-icon {
            width: 23px;
            position: absolute;
            margin: auto;
            top: 0;
            left: 0;
            bottom: 0;
            right: 0;
        }

        .google-btn .btn-text {
            font-size: 1rem;
            margin: auto;
        }
        
        .google-btn:hover {
            box-shadow: 0 0 6px #4285f4;
        }

        .google-btn:active {
            background: #1669f2;
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
            <h6>Sign in to Note Manager</h6>

            <div class="google-btn">
                <div class="google-icon-wrapper">
                    <img class="google-icon" src="https://upload.wikimedia.org/wikipedia/commons/5/53/Google_%22G%22_Logo.svg"/>
                </div>
                <p class="btn-text"><b>Continue with google</b></p>
            </div>
            <!--div class="twitter-wrapper auth-wrapper"></div-->

            <div class="native-wrapper">
                <a href="/register" class="register-wrapper auth-wrapper">Register</a>
                <a href="/login" class="login-wrapper auth-wrapper">Sign In</a>
            </div>
        </div>
    </div>
</body>
</html>
