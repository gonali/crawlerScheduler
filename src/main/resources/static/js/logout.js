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

            if (!confirm("确定退出？"))
                return false;
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