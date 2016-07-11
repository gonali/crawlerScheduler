/**
 * Created by TianyuanPan on 7/11/16.
 */



function getPidAndTaskState() {

    $.ajax({
        type: "POST",
        url: "/api/getPidAndTaskState",
        data: {},
        dataType: "json",
        beforeSend: function () {

        },
        success: function (data) {
            if (!data.status && data.isRedirect) {

                window.location = data.location;
                return false;
            }

            if (data.state) {

                $("#scheduler-state").html("Running")
                    .attr("style", "color: green");

            } else if (data.wait) {

                $("#scheduler-state").html("WAITING")
                    .attr("style", "color: orange");
            } else {

                $("#scheduler-state").html("STOP")
                    .attr("style", "color: red");
            }

            $("#scheduler-pid").html(data.pid);
        },
        complete: function () {
        },
        error: function () {
        }
    });
}


function update_task_state() {

    getPidAndTaskState();
    setTimeout(update_task_state, 60000);

}


$(document).ready(update_task_state());