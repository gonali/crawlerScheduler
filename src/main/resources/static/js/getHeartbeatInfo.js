/**
 * Created by TianyuanPan on 7/12/16.
 */

function makeHeartbeatTable(data){

    var size = data.length;
    var theBody = "";
    for (var i = 0; i < size; i++) {

        theBody +=
            "<tr>" +
            "<td>" + (i+1) + "</td>" +
            "<td>" + data[i].taskId + "</td>" +
            "<td>" + data[i].host + "</td>" +
            "<td>" + data[i].pid + "</td>" +
            "<td>" + data[i].threads + "</td>" +
            "<td>" + data[i].statusCode + "</td>" +
            "<td>" + data[i].timeoutCount + "</td>" +
            "<td>" + data[i].time + "</td>" +
            "</tr>";
    }

    $("#heartbeat-table").html(theBody);

}


function getHeartbeatInfo() {

    $.ajax({
        type: "POST",
        url: "/api/getHeartbeatInfo",
        data: {},
        dataType: "json",
        beforeSend: function () {

        },
        success: function (data) {
            if (!data.status && data.isRedirect) {

                window.location = data.location;
                return false;
            }

            makeHeartbeatTable(data);

        },
        complete: function () {
        },
        error: function () {
            tableAction();
        }
    });
}


function update_heartbeat_info() {

    getHeartbeatInfo();
    setTimeout(update_heartbeat_info, 10000);

}


$(document).ready(update_heartbeat_info());