<%--
  Created by IntelliJ IDEA.
  User: Vinda_Boy
  Date: 2020/8/8
  Time: 9:19
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <!-- Bootstrap -->
    <link href="/static/css/bootstrap.min.css" rel="stylesheet">
    <link href="/static/css/sweetalert2.css" rel="stylesheet">
    <!-- jQuery (Bootstrap 的所有 JavaScript 插件都依赖 jQuery，所以必须放在前边) -->

    <script src="/static/js/jquery-3.3.1.min.js" language="JavaScript" ></script>
    <!-- 加载 Bootstrap 的所有 JavaScript 插件。你也可以根据需要只加载单个插件。 -->

    <script src="/static/js/bootstrap.min.js"></script>
    <%--    弹出框样式--%>
    <link   href="/static/css/sweetalert2.css" rel="stylesheet">
    <script src="/static/js/es6-promise.auto.js" type="text/javascript"></script>
    <script src="/static/js/sweetalert2.min.js" type="text/javascript"></script>

    <link href="/static/dist/css/simple-bs-dialog.min.css" rel="stylesheet">
    <script src="/static/dist/js/simple-bs-dialog.min.js"></script>

    <link rel="shortcut icon" href="../images/redbird.png"/>
    <title>我们的小舞台</title>
</head>

    <%--        //返回首页--%>
    <c:if test="${sessionScope.user == null}">
        <c:redirect url="../../newlogin.jsp" >
        </c:redirect>
    </c:if>
    <body style="background-image: url('../../images/bk.jpg');background-repeat: no-repeat;background-size: 100% 100%;">

    <a href="javascript:history.go(-1)" style="text-decoration:none;float:right"
       class="btn btn-primary">返回上一级</a>
    <table id="liuyanTable" name ="liuyanTable" class="table table-hover">
        <tr >
            <th style="width:15% ;text-align: center" >条数</th>
            <th style="width:15%;text-align: center"> 周数</th>
            <th style="width:20%;text-align: center">提交时间</th>
            <th style="width:35%;text-align: center">留言内容</th>
<%--            <th style="width:15%">内容预览</th>--%>
        </tr>

        <c:forEach items="${requestScope.Messages}" var="Message" varStatus="m">
        <tr>
            <td style="width:15%;text-align: center">${m.count}</td>
            <td style="width:15%;text-align: center">${Message.week}</td>
            <td style="width:20%;text-align: center">${Message.time}</td>
            <td style="width:50%;text-align: center"><textarea cols="80"  rows="1" style="overflow:hidden;white-space:nowrap;text-overflow:ellipsis;resize:none;">${Message.content}</textarea>
            </td>
<%--            <td style="width:15%">--%>
<%--                <input  type="button" id="yulanliuyan" name="yulanliuyan"--%>
<%--                        class="btn btn-primary" data-toggle="button" aria-pressed="false" autocomplete="off"--%>
<%--                        value="预览"/>--%>
<%--            </td>--%>
        </tr>
        </c:forEach>
    </table>
    <script src="static/js/liuyan.js"></script>

    </body>
</html>
