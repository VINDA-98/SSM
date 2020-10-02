<%--
  Created by IntelliJ IDEA.
  User: Vinda_Boy
  Date: 2020/8/4
  Time: 21:14
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <meta charset="utf-8">
    <link rel="shortcut icon" href="./images/bluebird.png"/>
<%--    <link rel="shortcut icon" type="image/x-icon" href="/static/images/redbird.png"/>--%>
    <title>乾坤未定，你我皆黑马</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- CSS -->
    <link rel="stylesheet" href="css/reset.css">
    <link rel="stylesheet" href="css/supersized.css">
    <link rel="stylesheet" href="css/style.css">


</head>
    <c:if test="${not empty sessionScope.user}">
        <script>
            window.location.href="/refresh"
        </script>
    </c:if>


<body oncontextmenu="return false">

<div class="page-container">
    <h1>百院蓝桥四班加油</h1>
    <form action="/UserLogin" method="post" name="data">
        <div>
            <input type="text" name="id" class="id" placeholder="ID" autocomplete="off"/>
        </div>
        <div>
            <input type="password" name="password" class="password" placeholder="Password"
                   oncontextmenu="return false" onpaste="return false"/>
        </div>
        <button id="submit" type="submit">奥力给</button>
        <br>
        <br>
        <br>
        <div tyle="text-align: center">
            <span style="font-size: 1rem;color: #ac2925">
             <c:if test="${not empty requestScope.errors}">
                 ${requestScope.errors}</c:if>
            </span>
        </div>

    </form>
    <div class="connect">
            <p>A thousand journey takes every step.</p>
            <p style="margin-top:20px;">不积跬步,无以至千里。</p>
    </div>
</div>
<div class="alert" style="display:none">
    <h2>消息</h2>
    <div class="alert_con">
        <p id="ts"></p>
        <p style="line-height:70px"><a class="btn">确定</a></p>
    </div>
</div>

<!-- Javascript -->
<script src="http://apps.bdimg.com/libs/jquery/1.6.4/jquery.min.js" type="text/javascript"></script>
<script src="js/supersized.3.2.7.min.js"></script>
<script src="js/supersized-init.js"></script>
<script>

    window.onload = function () {
        $(".connect p").eq(0).animate({"left": "0%"}, 600);
        $(".connect p").eq(1).animate({"left": "0%"}, 400);
    }

    function is_hide() {
        $(".alert").animate({"top": "-40%"}, 300)
    }

    function is_show() {
        $(".alert").show().animate({"top": "45%"}, 300)
    }
</script>
</body>
</html>
