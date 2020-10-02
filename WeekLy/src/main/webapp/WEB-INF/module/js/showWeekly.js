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


    // /*时间显示*/
    // var locale = {
    //     "format": 'YYYY.MM.DD',
    //     "separator": " 到 ",
    //     "applyLabel": "确定",
    //     "cancelLabel": "取消",
    //     "fromLabel": "起始时间",
    //     "toLabel": "结束时间'",
    //     "customRangeLabel": "自定义",
    //     "weekLabel": "W",
    //     "daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
    //     "monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
    //     "firstDay": 1
    // };


    // $('#demo').daterangepicker({
    //     'locale': locale,
    //     ranges: {
    //         '今日': [moment(), moment()],
    //         '昨日': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
    //         '最近7日': [moment().subtract(6, 'days'), moment()],
    //         '本周': [moment().startOf('week').add(1, 'days'), moment().endOf('week').add(1, 'days')],
    //         '上一周': [moment().startOf('week').subtract(6, 'days'), moment().endOf('week').subtract(6, 'days')],
    //         '最近30日': [moment().subtract(29, 'days'), moment()],
    //         '本月': [moment().startOf('month'), moment().endOf('month')],
    //         '上月': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')
    //         ]
    //     },
    //     "alwaysShowCalendars": true,
    //     "startDate": moment().startOf('week').add(1, 'days'),
    //     "endDate": moment().endOf('week').add(1, 'days'),
    //     "opens": "center",
    // }, function (start, end, label) {
    //     console.log('New date range selected: ' + start.format('YYYY.MM.DD') + ' to ' +
    //         end.format('YYYY.MM.DD') + ' (predefined range: ' + label + ')');
    // });


    // //提交留言
    // $("#liuyansimt").click(function () {
    //         Ewin.confirm({message: "欢迎提交留言，文明发言，共创美好蓝桥！"}).on(function (e) {
    //             if (!e) {
    //                 return;
    //             }else{
    //                 var week = $("#week").val();
    //                 var content = $("#liuyandata").val();
    //
    //                 if(window.name == ""){
    //                     window.name = "Load"
    //                     window.location.href = "/sumbitLiuYan?week="+week+"&content="+content;
    //                 }else if (window.name == "Load"){
    //                     window.location.href = "/sumbitLiuYan";
    //                 }
    //             }
    //
    //         });
    // });

    //提交个人周报
    $("#submitbut").click(function () {
        //获取最大周数
        var maxWeek = $("#maxWeek").val();
        var week = $("#week").val();
        if (maxWeek == week) {
            Ewin.confirm({message: "请您确认周次信息，周数，周报内容是否修改正确，提交到新的个人周报内容吗？"}).on(function (e) {
                if (!e) {
                    return;
                }
                var id = $("#id").val();
                var week = $("#week").val();
                var content = $("#contentdata").val();
                var limits = $("#demo").val();
                document.getElementById('limits').value = limits;
                document.getElementById('content').value = content;
                $("#datafrom").submit();// jquery写法 表单提交
            });

        } else {
            swal("抱歉靓仔，已经过了周报提交时间咯");
        }
    });

    //提交小组周报
    $("#teamsubmitbut").click(function () {
        var id = $("#id").val();
        if (id.toString().endsWith("001")) {
            //获取最大周数
            var maxWeek = $("#maxWeek").val();
            var week = $("#week").val();
            if (maxWeek == week) {
                Ewin.confirm({message: "请您确认周次信息，周数，周报内容是否修改正确，提交到新的小组周报内容吗？"}).on(function (e) {
                    if (!e) {return;}
                    var week = $("#week").val();
                    var content = $("#contentdata").val();
                    var flag = "team";
                    var limits = $("#demo").val();
                    document.getElementById('limits').value = limits;
                    document.getElementById('tcontent').value = content;
                    document.getElementById('flag').value = flag;
                    $("#datafrom").submit();// jquery写法 表单提交
                });
            } else {
                swal("抱歉靓仔，已经过了周报提交时间咯");
            }
        } else {
            swal("你不是组长，你点这个干嘛咧");
        }
    });


    //给小组周报切换按钮添加按钮
    $("#teamweekly ").click(async function () {
        var id = $("#id").val();
        if (id.toString().endsWith("001")) {
            $("#contentdata").val(document.getElementById('tcontent').value);
            $("#titletip").text("小组周报")
        } else swal("不是组长就别点了啊");
    });


    //给个人周报按钮添加按钮
    $("#peopleweekly").click(async function () {
        $("#contentdata").val(document.getElementById('content').value);
        $("#titletip").text("个人周报")
    });

    $("#liuyansimt").click(function () {
        var content = $("#liuyandata").val();
        if(content.toString() == "" ) swal("啥玩意也不写，你提交什么啥啊");
        if(content.length > 100) swal("靓仔，你意见有点多啊？");
        if(content.toString() != ""  && content.length < 100 ){
            Ewin.confirm({message: "欢迎提交留言，文明发言，共创美好蓝桥！"}).on(function (e) {
                if (!e) {
                    return;
                }else{
                    var week = $("#week").val();
                    window.location.href = "/sumbitLiuYan?week="+week+"&content="+content;
                }
            });
        }
    });
})(jQuery);





// swal({
//     title: '确定要提交新的内容吗?',
//     text: '',
//     type: 'warning',
//     showCancelButton: true,
//     confirmButtonColor: "#DD6B55", //可有可无
//     confirmButtonText: "确定",   //可有可无
//     closeOnConfirm: false,  //必须设置为false
//     confirmButtonColor: "#DD6B55",
// }).then(function (isConfirm) {
//     if (isConfirm) {
//         swal(isConfirm);
//         var id = $("#id1").val();
//         var week = $("#week").val();
//         var content = $('textarea').val();
//         document.getElementById('content').value = content;
//         $("#datafrom").submit();// jquery写法 表单提交
//     }
// });


