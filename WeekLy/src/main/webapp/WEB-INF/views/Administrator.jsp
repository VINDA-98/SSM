<%@ page import="java.util.List" %>
<%@ page import="top.weidaboy.entity.User" %>
<%@ page import="top.weidaboy.entity.Weekinfo" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <!-- Bootstrap -->
    <link href="/static/css/bootstrap.min.css" rel="stylesheet">
    <link href="/static/css/sweetalert2.css" rel="stylesheet">
    <link href="/static/css/Administrator.css" rel="stylesheet">
    <!-- jQuery (Bootstrap 的所有 JavaScript 插件都依赖 jQuery，所以必须放在前边) -->

    <script src="/static/js/jquery-3.3.1.min.js" language="JavaScript" ></script>
    <!-- 加载 Bootstrap 的所有 JavaScript 插件。你也可以根据需要只加载单个插件。 -->
    <script src="/static/js/bootstrap.min.js"></script>

    <link href="/static/dist/css/simple-bs-dialog.min.css" rel="stylesheet">
    <script src="/static/dist/js/simple-bs-dialog.min.js"></script>
    <link href="/static/css/sweetalert2.css" rel="stylesheet">
    <script src="/static/js/es6-promise.auto.js" type="text/javascript"></script>
    <script src="/static/js/sweetalert2.min.js" type="text/javascript"></script>

    <link rel="shortcut icon" href="./images/redbird.png"/>
    <title>管理员界面</title>
    <link rel="icon" type="image/png" href="././images/鸟.png"/>

</head>
<body>
<div>
    <table cellspacing="25">
        <tr>
            <td width="20%">
                <p>小组：</p>
                <select name="team" id="team_t" style="color: orange">
                    <option>全部</option>
                    <c:forEach begin="1" end="8" var="i" varStatus="vS">
                        <option>${i}</option>
                    </c:forEach>
                </select>
            </td>
            <td width="20%">
                <p>周数：</p>
                <select id="week_t" name="week" style="color: orange">
                    <option>全部</option>
                </select>
            </td>
            <td width="10%">
                <input type="button" onclick="getInfo()" id="search" value="查&nbsp;&nbsp;询">
            </td>

            <td width="20%">
                <input  type="button" id="ExportNowWeekLy" value="选中周报导出"
                        class="btn btn-primary" data-toggle="button"
                        aria-pressed="false" autocomplete="off"/>
<%--                <button class="btnEx">单&nbsp;独&nbsp;导&nbsp;出</button>--%>
            </td>
            <td width="20%">
                <input  type="button" id="ExportWeekLy" value="导出周报信息"
                        class="btn btn-primary" data-toggle="button"
                        aria-pressed="false" autocomplete="off"/>
<%--                <button class="btnEx">全&nbsp;部&nbsp;导&nbsp;出</button>--%>
            </td>

            <td width="10%">
                <a href="/Exit">退出登录</a>
            </td>
        </tr>
    </table>
</div>

<table width='80%' border='0' cellspacing='0' cellpadding='0' align='center'
       id="table">
    <tr style="text-align: center">
        <td>工号</td>
        <th>姓名</th>
        <th>性别</th>
        <th>专业</th>
        <th>组号</th>
        <th>周数</th>
        <th>提交时间</th>
        <th>       </th>
    </tr>

    <c:forEach items="${weekInfos}" var="weekInfo">
        <c:forEach items="${users}" var="user">
            <c:if test="${user.getId() == weekInfo.getId()}">
                <tr>
                    <td>${user.getId()}</td>
                    <td>${user.getUsername()}</td>
                    <td>${user.getSex()}</td>
                    <td>${user.getMajor()}</td>
                    <td>${user.getTeam()}</td>
                    <td>${weekInfo.getWeek()}</td>
                    <td>${weekInfo.getTime()}</td>
                    <td style="display: none">
                            ${weekInfo.getContent()}
                    <td>
                </tr>
            </c:if>
        </c:forEach>
    </c:forEach>
</table>
</div>

<script>
    var se = document.getElementById('week_t');
    <%
        List<Integer> weeks = (List<Integer>) request.getAttribute("weeks");
        for (int w : weeks){
    %>
    se.options.add(new Option(<%=w%>));
    <%
        }
    %>

    function getInfo() {
        var gT =   $("#team_t ").get(0).selectedIndex;
        var gW =   $("#week_t ").get(0).selectedIndex;
        var team = $("#team_t ").find("option:selected").text();
        var week = $("#week_t ").find("option:selected").text();
        window.location.href = "administrator?gt=" + gT + "&gw=" + gW
            + "&team=" + team + "&week=" + week;
    }

    $("#team_t").find("option").eq(${gT}).attr("selected", true);
    $("#week_t").find("option").eq(${gW}).attr("selected", true);
</script>
<script src="/static/js/Administrator.js"     type="text/javascript"></script>
</body>
</html>
