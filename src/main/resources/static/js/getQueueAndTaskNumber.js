/**
 * Created by TianyuanPan on 7/11/16.
 */


function getQueueAndTaskNumber() {

    $.ajax({
        type: "POST",
        url: "/api/getQueueAndTaskNumber",
        data: {},
        dataType: "json",
        beforeSend: function () {

        },
        success: function (data) {
            if (!data.status && data.isRedirect){

                window.location = data.location;
                return false;
            }

            $("#max-task").html(data.maxTaskRun);
            $("#max-queue").html(data.maxQueue);
            $("#current-queue").html(data.currentQueue);

        },
        complete: function () {
        },
        error: function () {
        }
    });
}


function update_queue_and_task_number(){

    getQueueAndTaskNumber();
    setTimeout(update_queue_and_task_number, 30000);

}


$(document).ready(update_queue_and_task_number());