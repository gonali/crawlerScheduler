/**
 * Created by TianyuanPan on 7/11/16.
 */


/***

 date format:
 [
  {
    "taskId":"String",
    "taskName":"String",
    "heartbeatNumber":int,
    "startTime":"YYYY-MM-dd HH:mm:ss",
    "costTime":"String"
  },
 ... ...
 ]

 ***/

function makeTaskTableView(data){

    var size = data.length;
    var theBody = "";
    var color = ["active","info","warning", "success", "danger"];
    for (var i = 0; i < size; i++) {

        theBody +=
            "<tr class = \"" + color[i%5] +"\">" +
            "<td>" + (i+1) + "</td>" +
            "<td>" + data[i].taskId + "</td>" +
            "<td>" + data[i].taskName + "</td>" +
            "<td>" + data[i].heartbeatNumber + "</td>" +
            "<td>" + data[i].startTime + "</td>" +
            "<td>" + data[i].costTime + "</td>" +
            "</tr>";
    }

    $("#task-state-table-body").html(theBody);

}


function getCurrentTask() {

    $.ajax({
        type: "POST",
        url: "/api/getCurrentTask",
        data: {},
        dataType: "json",
        beforeSend: function () {

        },
        success: function (data) {
            if (!data.status && data.isRedirect){

                window.location = data.location;
                return false;
            }

            makeTaskTableView(data);
        },
        complete: function () {
        },
        error: function () {
        }
    });
}


function update_current_tasks(){

    getCurrentTask();
    setTimeout(update_current_tasks, 10000);

}


$(document).ready(update_current_tasks());