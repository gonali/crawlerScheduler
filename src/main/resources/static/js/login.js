/**
 */


/**
 * data:
 * {"status":true/false}
 */
function do_login() {

    $.ajax({
        type: "POST",
        url: "/api/login",
        data: {
            username: $("#username").val(),
            password: $("#password").val()
        },
        dataType: "json",
        beforeSend: function () {
        },
        success: function (data) {
            //var status = eval(data);
            if (!data.status) {
                swal({
                    title: "登录密码错误!",
                    text: "错误消息.",
                    type: 'error',
                    timer: 3000,
                    showConfirmButton: false
                });
                //window.location = "/login.html"
            } else {
                window.location = "/index.html";
            }
        },
        complete: function () {
        },
        error: function () {

            swal({
                title: "登录错误!",
                text: "错误消息.",
                type: 'error',
                timer: 3000,
                showConfirmButton: false
            });

        }
    });

}