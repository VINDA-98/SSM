/**
 * @funciton 转换textarea存入数据库的回车换行和空格
 textarea ---  数据库,用val取数据，置换'\n'
 */
function textareaTo(str){
    var reg=new RegExp("\n","g");
    var regSpace=new RegExp(" ","g");
    str = str.replace(reg,"<br>");
    str = str.replace(regSpace,"&nbsp;");
    return str;
}

/**
 * @funciton  数据库 ---  编辑页面.val(str)
 */
function toTextarea(str){
    var reg=new RegExp("<br>","g");
    var regSpace=new RegExp("&nbsp;","g");
    str = str.replace(reg,"\n");
    str = str.replace(regSpace," ");
    return str;
}


/**
 * 获取点击的行内容数据
 */
//table 确定点击的这一行，实现捕捉当前行的数据
$(function () {
    $("#table").on("click", "tr", function () {
        var td = $(this).find("td");
        // var data = new Array();
        /*for (var i = 0; i < td.length; i++) {
            data.push(td.eq(i).text());
        }*/
        var txt =  td.eq(7).text();
        var data = textareaTo(txt); //存入数据库用这个
        SimpleBsDialog.show({
            width: '900px',
            autoWidth: false,
            height: '30%',
            autoHeight: true,
            title: '靓仔周报',
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
                action: function(dialogRef) {
                    console.log('OK button pressed!');
                    dialogRef.close();
                },
            }, {
                id: 'btn-cancel',
                label: 'Cancel',
                cssClass: 'btn-warning',
                action: function(dialogRef) {
                    console.log('Cancel button pressed!');
                    dialogRef.close();
                },
            },
            ],
            onShow: function(dialogRef) {
                console.log('onShow');

                dialogRef.getButtons().prop('disabled', true);
            },
            onShown: function(dialogRef) {
                console.log('onShown');

                setTimeout(function() {
                    dialogRef.set({
                        'spinner': false,
                    }).getModalBody().html(data);

                    dialogRef.getButtons().prop('disabled', false);
                }, 50);
            },
            onHide: function(dialogRef) {
                console.log('onHide');
            },
            onHidden: function(dialogRef) {
                console.log('onHidden');
            },
        });

    });

    //给导出周报信息按钮添加事件
    $("#ExportWeekLy ").click(async function() {
        window.location.href = "/FileDownload?fileName=百色学院蓝桥四班周报.xlsx";
    });

    //选中周报导出
    $("#ExportNowWeekLy").click(async function() {
        var options = $("#week_t option:selected");
        var week = options.text();
        swal.fire(options.val());
        window.location.href = "/FileWeekDownload?fileName=百色学院蓝桥四班第"+week+"周周报.xlsx&week="+week;
    });

});