$(function () {
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

// $("#yulanliuyan").click(function () {
//     //表格点击：
    $("#liuyanTable").on("click", "tr", function () {
        var td = $(this).find("td");
        var txt =  td.eq(3).text();
        var weekly = textareaTo(txt); //存入数据库用这个
        SimpleBsDialog.show({
            width: '900px',
            autoWidth: false,
            height: '30%',
            autoHeight: true,
            title: '来自某个靓仔的留言',
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
                    }).getModalBody().html(weekly);

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
// });
})

