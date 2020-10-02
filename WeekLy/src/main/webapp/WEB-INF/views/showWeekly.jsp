<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="top.weidaboy.entity.Weekinfo" %>
<%@ page import="top.weidaboy.entity.User" %><%--
  Created by IntelliJ IDEA.
  User: Vinda_Boy
  Date: 2020/7/13
  Time: 11:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <!-- Bootstrap -->
    <link href="/static/css/bootstrap.min.css" rel="stylesheet">
    <link href="/static/css/ChangeWeekly.css" rel="stylesheet">
    <link href="/static/css/sweetalert2.css" rel="stylesheet">
    <!-- jQuery (Bootstrap 的所有 JavaScript 插件都依赖 jQuer
    y，所以必须放在前边) -->
    <script language="JavaScript" src="/static/js/jquery-3.3.1.min.js"></script>
    <!-- 加载 Bootstrap 的所有 JavaScript 插件。你也可以根据需要只加载单个插件。 -->
    <script src="/static/js/bootstrap.min.js"></script>

    <%--    弹出框样式--%>
    <script src="/static/js/es6-promise.auto.js" type="text/javascript"></script>
    <script src="/static/js/sweetalert2.min.js" type="text/javascript"></script>

    <%--时间范围选择器--%>
    <link rel="stylesheet" type="text/css" href="/static/css/timecss/daterangepicker.css"/>
    <script type="text/javascript" src="/static/js/moment.min.js"></script>
    <script type="text/javascript" src="/static/js/daterangepicker.min.js"></script>

    <link rel="shortcut icon" href="../images/redbird.png"/>

    <title>周报内容修改</title>


</head>
<%--background-image:url('images/bj.jpg');  先不要背景看看--%>
<body style="background-color:#dff0d8;
                background-repeat:no-repeat;
                background-size:100% 100%">
<div style="text-align: center; margin-top: 1.5rem;margin-bottom: 1.5rem">
         <span class="label label-success" style="font-size: 3.5rem">
            <input type="hidden" id="id" name="id" value="${sessionScope.user.id}"/>
            加油吧${sessionScope.user.username}</span>

</div>
<script>

    $(function () {

        //返回首页
        <c:if test="${sessionScope.user == null}">
        <c:redirect url="../../newlogin.jsp" >
        </c:redirect>
        </c:if>


        //获取用户修改内容
        <c:if test="${requestScope.msg != null}">
        swal("您的内容保存成功");
        </c:if>

        //获取用户新建周报提示信息
        <c:if test="${requestScope.newmsg != null}">
        swal("新建周报成功！");
        </c:if>
    });
</script>

</h1>


<a href="/Exit" style="float:right;font-size:1.75rem;
	 text-decoration:none">退出登录</a>


<br>
<form action="${pageContext.request.contextPath}/updateWeekly" method="post" id="datafrom" style="width: 100%">
    <table align="center" style="text-align:center;width: 100%" cellspacing="0px">
        <tr style="background-color:rgba(124,112,128,0.31); font-size: 2rem;">
            <th style="width:18%;text-align:center;">工号</th>
            <th style="width:18%;text-align:center;">姓名</th>

            <th style="width:25%;text-align:center;">周次信息</th>
            <th style="width:25%;text-align:center;">上一次周报修改时间</th>
            <th style="width:10%;text-align:center;">周数</th>
        </tr>

        <c:if test="${not empty weekinfo}">

        <tr style=" font-size: 2rem">
            <td id="iddata" name="iddata" style="width:18%;text-align:center">
                    ${sessionScope.user.id}</td>
            <td id="usernamedata" name="usernamedata" style="width:18%;text-align: center">
                    ${sessionScope.user.username}</td>
            <td id="timelimits" name="timelimits" style="width:25%;text-align: center">
                    <%--                <input id="demo"  name="demo" type="hidden" style="border: none;text-align: center;--%>
                    <%--                background-color:transparent"/>--%>
                    ${requestScope.weekinfo.limits}
            </td>
            <td style="width:25%;text-align: center">
                    ${requestScope.weekinfo.time}</td>
            <td id="weekdata" name="weekdata" style="width:10%;text-align: center">
                    ${requestScope.weekinfo.week}</td>

        </tr>

        <tr style="background-color:rgba(124,112,128,0.31);font-size: 2rem;">
            <td colspan="5" style="height: 4.5rem">
                <span class="label label-success" id="titletip" style="font-size:2.5rem">个人周报</span>
            </td>
        </tr>

        <tr>
            <td colspan="5">
                    <%--                readonly : 不可写 --%>
                <pre><textarea name="contentdata" id="contentdata" rows="15" cols="20" style="background-color:transparent; border:0.2rem solid black;font-family:华文楷体; font-size: 2.0rem;  OVERFLOW:scroll; width: 100%;/*height: 500%;*/ resize: none">${requestScope.weekinfo.content}</textarea></pre>
            </td>
        </tr>

        <tr>
            <td>
                <input type="button" id="peopleweekly" class="btn btn-primary"
                       style="font-family: 仿宋;font-size: 1.7rem" value="个人周报"/>&nbsp;&nbsp;&nbsp;
                <input type="button" id="teamweekly" class="btn btn-primary"
                       style="font-family: 仿宋;font-size: 1.7rem" value="小组周报"/>
            </td>
            <td>
            </td>
            <td>
                <input type="button" id="teamsubmitbut" class="btn btn-primary"
                       style="font-family: 仿宋;font-size: 1.7rem" value="提交到小组周报修改"/>&nbsp;&nbsp;
            </td>
            <td>
                <input type="button" id="submitbut" class="btn btn-primary"
                       style="font-family: 仿宋;font-size: 1.7rem" value="提交到个人周报修改"/>
            </td>

            <td>
                <a style="text-decoration:none;font-size: 2rem;font-family: 仿宋"
                   class="btn btn-warning"
                   href="/Back">返回</a>
            </td>
        </tr>

            <%--周报内容:用input来接收--%>
        <tr>
            <td>
                <input type="hidden" name="id" id="id" value="${requestScope.weekinfo.id}"/>
                    <%--判断提交的是小组周报还是个人周报--%>
                <input type="hidden" name="flag" id="flag" value=""/>

            </td>

            <td>
                <input type="hidden" name="week" id="week" value="${requestScope.weekinfo.week}"/>
            </td>

                <%--格式化显示数据--%>
            <script>
                //转换函数
                function toTextarea(str) {
                    var reg = new RegExp("<br>", "g");
                    var regSpace = new RegExp("&nbsp;", "g");
                    str = str.replace(reg, "\n");
                    str = str.replace(regSpace, " ");
                    return str;
                }

            </script>
            <td>
                    <%--获取周次信息--%>
                <input type="hidden" name="maxWeek"  id="maxWeek"  value="${requestScope.maxWeek}"/>
                <input type="hidden" name="limits"   id="limits"   value="${requestScope.weekinfo.limits}"/>
                <input type="hidden" name="content"  id="content"  value="${requestScope.weekinfo.content}"/>
                <input type="hidden" name="tcontent" id="tcontent" value="${requestScope.weekinfo.tcontent}"/>
            </td>
        </tr>

        <tr>
            <td colspan="5" style="height: 9.0rem">
                <span class="label label-success" style="font-size: 2.5rem">留言板</span>
            </td>
        </tr>

        <tr >
            <td colspan="5" >
                <pre><textarea name="liuyandata" id="liuyandata" rows="5" cols="20" style="background-color:transparent; border:0.2rem solid black;font-family:华文楷体; font-size: 2.0rem;  OVERFLOW:scroll; width: 100%;/*height: 500%;*/ resize: none"></textarea></pre>
            </td>
        </tr>

            <%-- 间隔--%>
        <tr>
            <td colspan="5">
                <input type="button" id="liuyansimt" value="提交留言" onclick="msgsubmit()"
                       class="btn btn-primary" data-toggle="button" aria-pressed="false" autocomplete="off"
                       style="float: right"/>
            </td>
        </tr>
    </table>

</form>
</c:if>
<script type="text/javascript" src="/static/js/showWeekly.js"></script>
</body>

</html>
