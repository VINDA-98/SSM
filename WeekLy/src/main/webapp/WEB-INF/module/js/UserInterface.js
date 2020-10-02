(function ($) {
    window.Ewin = function () {
        var html = '<div id="[Id]" class="modal fade" role="dialog" aria-labelledby="modalLabel">' +
            '<div class="modal-dialog modal-sm">' +
            '<div class="modal-content">' +
            '<div class="modal-header">' +
            '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>' +
            '<h4 class="modal-title" id="modalLabel">[Title]</h4>' +
            '</div>' +
            '<div class="modal-body">' +
            '<p>[Message]</p>' +
            '</div>' +
            '<div class="modal-footer">' +
            '<button type="button" class="btn btn-default cancel" data-dismiss="modal">[BtnCancel]</button>' +
            '<button type="button" class="btn btn-primary ok" data-dismiss="modal">[BtnOk]</button>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>';


        var dialogdHtml = '<div id="[Id]" class="modal fade" role="dialog" aria-labelledby="modalLabel">' +
            '<div class="modal-dialog">' +
            '<div class="modal-content">' +
            '<div class="modal-header">' +
            '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>' +
            '<h4 class="modal-title" id="modalLabel">[Title]</h4>' +
            '</div>' +
            '<div class="modal-body">' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>';
        var reg = new RegExp("\\[([^\\[\\]]*?)\\]", 'igm');
        var generateId = function () {
            var date = new Date();
            return 'mdl' + date.valueOf();
        }
        var init = function (options) {
            options = $.extend({}, {
                title: "操作提示",
                message: "提示内容",
                btnok: "想好了",
                btncl: "冷静一下",
                width: 200,
                auto: false
            }, options || {});
            var modalId = generateId();
            var content = html.replace(reg, function (node, key) {
                return {
                    Id: modalId,
                    Title: options.title,
                    Message: options.message,
                    BtnOk: options.btnok,
                    BtnCancel: options.btncl
                }[key];
            });
            $('body').append(content);
            $('#' + modalId).modal({
                width: options.width,
                backdrop: 'static'
            });
            $('#' + modalId).on('hide.bs.modal', function (e) {
                $('body').find('#' + modalId).remove();
            });
            return modalId;
        }

        return {
            alert: function (options) {
                if (typeof options == 'string') {
                    options = {
                        message: options
                    };
                }
                var id = init(options);
                var modal = $('#' + id);
                modal.find('.ok').removeClass('btn-success').addClass('btn-primary');
                modal.find('.cancel').hide();

                return {
                    id: id,
                    on: function (callback) {
                        if (callback && callback instanceof Function) {
                            modal.find('.ok').click(function () {
                                callback(true);
                            });
                        }
                    },
                    hide: function (callback) {
                        if (callback && callback instanceof Function) {
                            modal.on('hide.bs.modal', function (e) {
                                callback(e);
                            });
                        }
                    }
                };
            },
            confirm: function (options) {
                var id = init(options);
                var modal = $('#' + id);
                modal.find('.ok').removeClass('btn-primary').addClass('btn-success');
                modal.find('.cancel').show();
                return {
                    id: id,
                    on: function (callback) {
                        if (callback && callback instanceof Function) {
                            modal.find('.ok').click(function () {
                                callback(true);
                            });
                            modal.find('.cancel').click(function () {
                                callback(false);
                            });
                        }
                    },
                    hide: function (callback) {
                        if (callback && callback instanceof Function) {
                            modal.on('hide.bs.modal', function (e) {
                                callback(e);
                            });
                        }
                    }
                };
            },
            dialog: function (options) {
                options = $.extend({}, {
                    title: 'title',
                    url: '',
                    width: 800,
                    height: 550,
                    onReady: function () {
                    },
                    onShown: function (e) {
                    }
                }, options || {});
                var modalId = generateId();

                var content = dialogdHtml.replace(reg, function (node, key) {
                    return {
                        Id: modalId,
                        Title: options.title
                    }[key];
                });
                $('body').append(content);
                var target = $('#' + modalId);
                target.find('.modal-body').load(options.url);
                if (options.onReady())
                    options.onReady.call(target);
                target.modal();
                target.on('shown.bs.modal', function (e) {
                    if (options.onReady(e))
                        options.onReady.call(target, e);
                });
                target.on('hide.bs.modal', function (e) {
                    $('body').find(target).remove();
                });
            }
        }
    }();
})(jQuery);


/**
 * @funciton  数据库 ---  编辑页面.val(str)
 */
function toTextarea(str) {
    var reg = new RegExp("<br>", "g");
    var regSpace = new RegExp("&nbsp;", "g");
    str = str.replace(reg, "\n");
    str = str.replace(regSpace, " ");
    return str;
}

/**
 * @funciton 转换textarea存入数据库的回车换行和空格
 textarea ---  数据库,用val取数据，置换'\n'
 */
function textareaTo(str) {
    var reg = new RegExp("\n", "g");
    var regSpace = new RegExp(" ", "g");
    str = str.replace(reg, "<br>");
    str = str.replace(regSpace, "&nbsp;");
    return str;
}


//给删除按钮添加事件 :出现转换异常，就得需要用.trim（）方法，去除网页带过去的空格
$(".deleteweekly").click(function () {
    swal("逗你玩呢靓仔，怎么可能让你删除，改改就上交把");
});


$(".yulanbtn").click(function () {
    //第二种方式按照id获取值,推荐
    var txt = $(this).parents("tr").find("#yulandata").text();
    var weekly = textareaTo(txt); //存入数据库用这个
    SimpleBsDialog.show({
        width: '900px',
        autoWidth: false,
        height: '30%',
        autoHeight: true,
        title: '靓仔周报不错哟',
        closable: true,
        spinner: true,
        spinnerIcon: '<span class="spinner-border text-primary" role="status"></span>',
        closeByBackdrop: true,
        closeByKeyboard: true,
        html: '',
        cssClass: 'login-dialog',
        buttons: [{
            id: 'btn-ok',
            label: 'OK',
            cssClass: 'btn-primary',
            action: function (dialogRef) {
                console.log('OK button pressed!');
                dialogRef.close();
            },
        }, {
            id: 'btn-cancel',
            label: 'Cancel',
            cssClass: 'btn-warning',
            action: function (dialogRef) {
                console.log('Cancel button pressed!');
                dialogRef.close();
            },
        },
        ],
        onShow: function (dialogRef) {
            console.log('onShow');

            dialogRef.getButtons().prop('disabled', true);
        },
        onShown: function (dialogRef) {
            console.log('onShown');

            setTimeout(function () {
                dialogRef.set({
                    'spinner': false,
                }).getModalBody().html(weekly);

                dialogRef.getButtons().prop('disabled', false);
            }, 50);
        },
        onHide: function (dialogRef) {
            console.log('onHide');
        },
        onHidden: function (dialogRef) {
            console.log('onHidden');
        },
    });
});
// });

$("#showLiuYan").click(function () {
    window.location.href = "/flushLiuYan"
});

$("#showAllWeekly").click(function () {
    window.location.href = "/refreshALL";
});

//给重置密码按钮添加事件：
$("#changepassword").click(async function () {
    const {value: password} = await Swal.fire({
        title: '密码修改',
        input: 'password',
        inputPlaceholder: '请输入您想要修改的密码',
        inputAttributes: {
            maxlength: 16,
            autocapitalize: 'off',
            autocorrect: 'off'
        }
    });
    reg = /[~#^$@%&!?%*]/gi;
    if (password.length < 6 || password.length > 16 || reg.test(password))
        swal("输入密码长度有错误（6-16位）,也可能你乱输入了啥玩意 ");
    else {
        Ewin.confirm({message: "确认要更新密码为：" + password + "吗？"}).on(function (e) {
            if (!e) {
                return;
            }
            var id = $("#id").val();
            window.location.href = "/changePassword?id=" + id + "&password=" + password;
        });
    }
});


// //给查看周报按钮添加事件
// $("#showteamweekly ").click(async function() {
//     var id = $("#id").val();
//     var week = $("#week").val();
//     if(id.toString().endsWith("001")){
//         window.location.href = "changeWeekServlet?id="+id+"&week="+week+"&people=no";
//     }else swal("干啥哈?不是组长你点这个干什么？？？");
// });


// //给新建周报按钮添加事件
// $("#newweekly").click(async function() {
//     const { value: newweek } = await Swal.fire({
//         title: '需要新建哪周周报内容？',
//         input: 'text',
//         inputPlaceholder: '新建周报周数：',
//         inputAttributes: {
//             maxlength: 2,
//             autocapitalize: 'off',
//             autocorrect: 'off'
//         }
//     })
//     if (newweek != null && newweek != "" && (!(isNaN(newweek))) && newweek < 40){
//         var id = $("#id").val();
//         window.location.href="newWeekly?id="+id+"&week="+newweek;
//     }else {
//         swal("请输入正确的周数!");
//     }
// });


//新建周报1.0
// var newweek=prompt("请输入你想要新建的周报周数：","").trim();
// //显示用户输入，确定是数字输入才可以新建
// if (newweek != null && newweek != "" && (!(isNaN(newweek))) && newweek < 40){
//     var week = newweek.trim().toString();
//     var id = $("#id").val();
//     // alert(id);
//     // alert(week);
//     window.location.href="newWeeklyServlet?id="+id+"&newweek="+week;
// }else {
//     swal("请输入正确的周数!");
// }


//修改密码1.0
// var password = prompt("请输入你要修改的密码：","");
//显示用户输入，确定是数字输入才可以新建
// if (password != null && password != ""){
//     if(password.length>=6  && password.length <= 20){
//         const { value: passwordagain } = await Swal.fire({
//             title: '二次输入',
//             input: '密码',
//             inputPlaceholder: '请输入您想要修改的密码',
//             inputAttributes: {
//                 maxlength: 20,
//                 autocapitalize: 'off',
//                 autocorrect: 'off'
//             }
//         })
//
//         if(password == passwordagain){
//             var id = $("#id").val();
//             window.location.href="changePasswordServlet?id="+id+"&password="+password;
//         }else {
//             swal("两次密码不一致");
//         }
//     }else {
//         swal("请输入密码长度为：6-20位");
//     }
// } else {
//     swal("请输入正确的密码!");
// }


//     $.ajax({
//     type: "POST",                           //传数据的方式
//     url: "/newWeeklyServlet", //servlet地址
//     data: $("#allData").serialize(),     //传的数据  form表单 里面的数据
//     success: function(result){           //传数据成功之后的操作   result是servlet传过来的数据  这个函数对result进行处理，让它显示在 输入框中
//         alert(result);                   //找到输入框 并且将result的值 传进去
//     }
// });


// <%--分页--%>
// <%--        <tr style="background-color:orange;">--%>
//     <%--            <td colspan="7">--%>
//     <%--                <center>--%>
//     <%--                    <b style="margin-left:10px;">--%>
//     <%--                        <a href="#">1</a>--%>
//     <%--                    </b>--%>
//     <%--                </center>--%>
//     <%--            </td>--%>
//     <%--        </tr>--%>
//
//
//     <%--// //在页面加载完成后--%>
//     <%--// $(function () {--%>
//     <%--//     //给userweek绑定blur事件--%>
//     <%--//     $("#userweek").blur(function () {--%>
//     <%--//         //获取userweek周期数--%>
//     <%--//         var userweek = $(this).val();--%>
//     <%--//         //发送ajax请求--%>
//     <%--//         $.post("changeWeekServlet",{userweek:userweek},function (data) {--%>
//     <%--//             $("weekly").html(data.weekly);--%>
//     <%--//         });--%>
//     <%--//     });--%>
//     <%--// });--%>

//     $.ajax({
//     type: "POST",                           //传数据的方式
//     url: "/newWeeklyServlet", //servlet地址
//     data: $("#allData").serialize(),     //传的数据  form表单 里面的数据
//     success: function(result){           //传数据成功之后的操作   result是servlet传过来的数据  这个函数对result进行处理，让它显示在 输入框中
//         alert(result);                   //找到输入框 并且将result的值 传进去
//     }
// });
// //如果不存在，则创建 是不是用ajax 和 josn 会好一点？
// var info = $("#info").val();
// //alert(info);
// //设置新的周数内容：获取ID
// }