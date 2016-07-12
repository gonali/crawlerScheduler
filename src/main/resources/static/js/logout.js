/**
 * Created by TianyuanPan on 7/8/16.
 */

function do_logout() {

    $.ajax({
        type: "POST",
        url: "/api/logout",
        data: {},
        dataType: "json",
        beforeSend: function () {
            swal({
                title: "确定退出?",
                text: "退出登录警告!",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes",
                closeOnConfirm: false
            })
        },
        success: function (data) {

            if (data.status)
                window.location = "login.html";
        },
        complete: function () {
        },
        error: function () {
        }
    });

}