/**
 * Created by TianyuanPan on 7/11/16.
 */


/****
 *
 * @param data
 *
 * json format
 *
 * [
 *   {
 *     "taskId":"String id",
 *     "enQueueTime":"String datetime",
 *     "taskName":"string name"
 *   },
 *   ... ...
 * ]
 */

function makeInQueueTaskTable(data){

    var size = data.length;
    var theBody = "";
    for (var i = 0; i < size; i++) {

        theBody +=
            "<tr>" +
            "<td>" + data[i].taskId + "</td>" +
            "<td>" + data[i].taskName + "</td>" +
            "<td>" + data[i].enQueueTime + "</td>" +
            "</tr>";
    }

    $("#inQueue-tasks").html(theBody);

}

function tableAction() {
    //Basic Example
    $("#data-table-basic").bootgrid({
        css: {
            icon: 'md icon',
            iconColumns: 'md-view-module',
            iconDown: 'md-expand-more',
            iconRefresh: 'md-refresh',
            iconUp: 'md-expand-less'
        },
    });

    //Selection
    $("#data-table-selection").bootgrid({
        css: {
            icon: 'md icon',
            iconColumns: 'md-view-module',
            iconDown: 'md-expand-more',
            iconRefresh: 'md-refresh',
            iconUp: 'md-expand-less'
        },
        selection: true,
        multiSelect: true,
        rowSelect: true,
        keepSelection: true
    });

    //Command Buttons
    $("#data-table-command").bootgrid({
        css: {
            icon: 'md icon',
            iconColumns: 'md-view-module',
            iconDown: 'md-expand-more',
            iconRefresh: 'md-refresh',
            iconUp: 'md-expand-less'
        },
        formatters: {
            "commands": function (column, row) {
                return "<button type=\"button\" class=\"btn btn-icon command-edit\" data-row-id=\"" + row.id + "\"><span class=\"md md-edit\"></span></button> " +
                    "<button type=\"button\" class=\"btn btn-icon command-delete\" data-row-id=\"" + row.id + "\"><span class=\"md md-delete\"></span></button>";
            }
        }
    });
}

function getInQueueTask() {

    $.ajax({
        type: "POST",
        url: "/api/getInQueueTask",
        data: {},
        dataType: "json",
        beforeSend: function () {

        },
        success: function (data) {
            if (!data.status && data.isRedirect) {

                window.location = data.location;
                return false;
            }

            makeInQueueTaskTable(data);
            tableAction();

        },
        complete: function () {
        },
        error: function () {
            tableAction();
        }
    });
}


function update_inQueue_tasks() {

    getInQueueTask();
    setTimeout(update_inQueue_tasks, 60000);

}


$(document).ready(update_inQueue_tasks());