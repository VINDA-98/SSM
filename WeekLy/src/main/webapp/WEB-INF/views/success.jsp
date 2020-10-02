<%--
  Created by IntelliJ IDEA.
  User: Vinda_Boy
  Date: 2020/7/28
  Time: 23:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>成功</title>
</head>
<body>
        登录成功
        ${requestScope.user.getId()} :${requestScope.user.getUsername()}

</body>
</html>

