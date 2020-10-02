<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import="top.weidaboy.entity.User" %>
<%@ page import="top.weidaboy.entity.Weekinfo" %>
<%--
  Created by IntelliJ IDEA.
  User: Vinda_Boy
  Date: 2020/7/13
  Time: 9:46
  To change this template use File | Settings | File Templates.
--%>
<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <!-- Bootstrap -->
    <link href="/static/css/bootstrap.min.css" rel="stylesheet">
    <link href="/static/css/UserInterface.css" rel="stylesheet">
    <link href="/static/css/sweetalert2.css" rel="stylesheet">
    <!-- jQuery (Bootstrap 的所有 JavaScript 插件都依赖 jQuery，所以必须放在前边) -->

    <script src="/static/js/jquery-3.3.1.min.js" language="JavaScript" ></script>
    <!-- 加载 Bootstrap 的所有 JavaScript 插件。你也可以根据需要只加载单个插件。 -->

    <script src="/static/js/bootstrap.min.js"></script>
    <%--    弹出框样式--%>
    <link href="/static/css/sweetalert2.css" rel="stylesheet">
    <script src="/static/js/es6-promise.auto.js" type="text/javascript"></script>
    <script src="/static/js/sweetalert2.min.js" type="text/javascript"></script>

    <link href="/static/dist/css/simple-bs-dialog.min.css" rel="stylesheet">
    <script src="/static/dist/js/simple-bs-dialog.min.js"></script>

    <script>
        $(function () {
        //获取登录的用户信息 如果request没有反应，记得添加jar包：tomcat8.0
            <%--        //获取用户修改密码状态--%>
            <c:if test="${requestScope.changePassword != null}">
            swal("密码修改成功，请务必记住你的密码");
            </c:if>

            <c:if test="${requestScope.newmsg != null}">
            swal("已经存在该周报");
            </c:if>
        });

    </script>

    <style>
        #changepassword,#showLiuYan,#showAllWeekly{
            float:right;
            font-size:1.75rem;
            margin-left:1.75rem;
        }
    </style>
    <link rel="shortcut icon" href="../images/redbird.png"/>
    <title>蓝桥四班同事用户界面</title>
    <%--    user的session存在，才可以进入用户揭秘那--%>
    <c:if test="${empty sessionScope.user}">
        <c:redirect url="../../newlogin.jsp" />
    </c:if>
</head>

<body>
    <div style="text-align: center; margin-top: 1.5rem;margin-bottom: 1.5rem">
         <span class="label label-success" style="font-size: 3.5rem">
            加油吧${sessionScope.user.username}
         </span>
    </div>
    <a href="/Exit" style="float:right;font-size:1.75rem; margin-left: 1.2rem;
	 text-decoration:none"  class="btn btn-danger">退出登录</a>

    <input  type="button" id="showLiuYan" value="查看留言"
            class="btn btn-primary" data-toggle="button" aria-pressed="false" autocomplete="off"/>

    <input  type="button" id="changepassword" value="重置密码"
            class="btn btn-primary" data-toggle="button" aria-pressed="false" autocomplete="off"/>

    <input  type="button" id="showAllWeekly" value="显示全部"
            class="btn btn-primary" data-toggle="button"
            aria-pressed="false" autocomplete="off"/>


    <br>

<form  method="post" name="data" id="data">

    <table class="table table-condensed" id="allData" >
        <tr style="background-color:gray;
                    font-size: 1.75rem;">
            <th style="text-align: center; width:15%">工号</th>
            <th style="text-align: center; width:15%">姓名</th>
            <th style="text-align: center; width:15%">组号</th>
            <th style="text-align: center; width:15%">周数</th>
            <th style="text-align: center">    </th>
            <th style="text-align: center; width:5% ">    </th>
            <th style="text-align: center">    </th>
        </tr>

        <%--        //返回首页--%>
        <c:if test="${sessionScope.user == null}">
            <c:redirect url="../../newlogin.jsp" >
            </c:redirect>
        </c:if>


        <%--  获取用户登录信息--%>
                <c:if test="${not empty weekinfos}">
                     <c:forEach items="${weekinfos}" var="weekinfo" varStatus="w">
                        <tr style="font-size: 1.65rem;">
                            <c:if test="${not empty user}">

                                <td><%--工号--%>
                                        ${user.id}
                                </td>
                                <td><%-- 姓名--%>
                                        ${user.username}
                                </td>
                                <td><%--组号--%>
                                        ${user.team}
                                </td>
                            </c:if>

                            <td><%-- 周数--%>
                                ${weekinfo.week}
                            </td>
                                <%--修改与删除--%>
                            <td>
                                <a  style="text-decoration:none"
                                    class="btn btn-primary"
                                    href="${pageContext.request.contextPath}/showWeekly?id=${weekinfo.id}&week=${weekinfo.week}">
                                     修改周报内容</a>
                            </td>

                            <td>
                                <input  type="button"  id="deleteweekly"
                                        value="删除内容" class="deleteweekly"/>
                            </td>

                            <td style="display: none" id="yulandata">
                                    ${weekinfo.content}
                            </td>

                            <td>
                                <input  type="button" id="yulanbtn"   class="yulanbtn" value="预览" />
                            </td>
                        </tr>
                         <%--id唯一，所以用id的button，只能第一行提示删除，我得使用class--%>
                         <%--隐藏新的周数、id、新建周数--%>
                         <input  type="hidden" id="yulan" value="${weekinfo.content}"/>
                         <input  type="hidden" id="id"    value="${weekinfo.id}"/>
                         <input  type="hidden" id="week"  value="${weekinfo.week}"/>
                        <%--  最大周数--%>
                         <input type="hidden" name="maxWeek" id="maxWeek"  value="${requestScope.maxWeek}"/>
                     </c:forEach>
                </c:if>
        <%--    导入相关的js文件--%>
        <script type="text/javascript" src="/static/js/UserInterface.js"> </script>
    </table>
</form>

</body>
</html>

