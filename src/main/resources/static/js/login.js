/**
 */


/**
 * data:
 * {"status":true/false}
 */
function do_login(){

    $.ajax({
        type: "POST",
        url: "/api/login",
        data: {
            username: $("#username").val(),
            password: $("#password").val()
        },
        dataType: "json",
        success: function(data){
            var status = eval(data);
            if (!status.status){
                alert("Password Error !!!");
                window.location = "/login.html"
            }else{

                window.location = "/index.html";
            }
        }
    });

}