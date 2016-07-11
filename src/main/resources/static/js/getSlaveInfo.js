/**
 * Created by TianyuanPan on 7/8/16.
 */


/***

 data format

 [
 {
   "slaveAppPath":"~/",
   "slaveId":"1",
   "slaveIp":"110.110.10.101",
   "slavePassword":"crawler",
   "slaveSshPort":22,
   "slaveUsername":"crawler"
 },
 ... ...
 ]

 ***/
function makeSlaveTable(data) {

    var size = data.length;
    var theBody = "";
    for (var i = 0; i < size; i++) {

        theBody +=
            "<tr>" +
            "<td>" + (i+1) + "</td>" +
            "<td>" + data[i].slaveIp + "</td>" +
            "<td>" + data[i].slaveSshPort + "</td>" +
            "<td>" + data[i].slaveUsername + "</td>" +
            "<td>" + data[i].slavePassword + "</td>" +
            "<td>" + data[i].slaveAppPath + "</td>" +
            "</tr>";
    }

    writeSlaveNumber(size);
    $("#slave-table-body").html(theBody);

}

function writeSlaveNumber(i){

    $("#slave-number").html(i);
}

/**
 *
 */
function getSlaveInfo() {

    $.ajax({
        type: "POST",
        url: "/api/slaveInfo",
        data: {},
        dataType: "json",
        beforeSend: function () {

        },
        success: function (data) {
            if (!data.status && data.isRedirect){

                window.location = data.location;
                return false;
            }

            makeSlaveTable(data);
        },
        complete: function () {
        },
        error: function () {
        }
    });

}

$(document).ready(getSlaveInfo());