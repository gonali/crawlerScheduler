/**
 * Created by TianyuanPan on 7/13/16.
 */


function get_scheduler_state() {

    $.ajax({
        type: "POST",
        url: "/api/getRuntimeControlMsg",
        data: {},
        dataType: "json",
        beforeSend: function () {

        },
        success: function (data) {
            if (!data.status && data.isRedirect) {

                window.location = data.location;
                return false;
            }

            if (data.taskRunning) {
                $('#state-value-hidden').attr('value', 'running');
                $('#state-bar-running').attr('style', 'background-color: green');
                $('#state-bar-waiting').attr('style', 'background-color: grey');
                $('#state-bar-stop').attr('style', 'background-color: grey');
            } else if (!data.taskRunning && !data.currentTasksFinished) {

                $('#state-value-hidden').attr('value', 'waiting');
                $('#state-bar-running').attr('style', 'background-color: grey');
                $('#state-bar-waiting').attr('style', 'background-color: darkorange');
                $('#state-bar-stop').attr('style', 'background-color: grey');
            } else {

                $('#state-value-hidden').attr('value', 'stop');
                $('#state-value-hidden').attr('value', 'waiting');
                $('#state-bar-running').attr('style', 'background-color: grey');
                $('#state-bar-waiting').attr('style', 'background-color: grey');
                $('#state-bar-stop').attr('style', 'background-color: red');
            }
        },
        complete: function () {
        },
        error: function () {

            $('#state-value-hidden').attr('value', 'err');
            $('#state-bar-running').attr('style', 'background-color: grey');
            $('#state-bar-waiting').attr('style', 'background-color: grey');
            $('#state-bar-stop').attr('style', 'background-color: grey');
        }
    });

}

function scheduler_start_btn() {
    var state = $('#state-value-hidden').val();
    if (state == 'err') {

        swal({
            title: "页面错误，请刷新后重试!",
            text: "错误消息.",
            type: 'error',
            timer: 3000,
            showConfirmButton: false
        });

        return false;
    }
    if (state != 'waiting' && state != 'stop') {

        swal({
            title: "调度器已经运行!",
            text: "错误消息.",
            type: 'error',
            timer: 3000,
            showConfirmButton: false
        });

        return false;
    }

    $.ajax({

        type: "POST",
        url: "/api/startScheduler",
        data: {},
        dataType: "json",
        beforeSend: function () {

        },
        success: function (data) {

            if (!data.status && data.isRedirect) {

                window.location = data.location;
                return false;
            }

            if (data.status) {
                swal({
                    title: "操作成功!",
                    text: "成功消息.",
                    type: 'success',
                    timer: 3000,
                    showConfirmButton: false
                });
            } else {

                swal({
                    title: "操作错误!",
                    text: "错误消息.",
                    type: 'error',
                    timer: 3000,
                    showConfirmButton: false
                });
            }
        },
        complete: function () {

        },
        error: function () {
            swal({
                title: "操作错误!",
                text: "错误消息.",
                type: 'error',
                timer: 3000,
                showConfirmButton: false
            });
        }

    });

    get_scheduler_state();
}

function scheduler_stop_btn() {

    var state = $('#state-value-hidden').val();
    if (state == 'err') {

        swal({
            title: "页面错误，请刷新后重试!",
            text: "错误消息.",
            type: 'error',
            timer: 3000,
            showConfirmButton: false
        });

        return false;
    }
    if (state != 'running') {

        swal({
            title: "调度器处于非运行状态，无法进行操作!",
            text: "错误消息.",
            type: 'error',
            timer: 3000,
            showConfirmButton: false
        });

        return false;
    }

    swal({
        title: "确定停止调度?",
        text: "停止调度警告!",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Yes",
        closeOnConfirm: false

    }, function () {
        $.ajax({

            type: "POST",
            url: "/api/stopScheduler",
            data: {},
            dataType: "json",
            beforeSend: function () {

            },
            success: function (data) {

                if (!data.status && data.isRedirect) {

                    window.location = data.location;
                    return false;
                }

                if (data.status) {
                    swal({
                        title: "操作成功!",
                        text: "成功消息.",
                        type: 'success',
                        timer: 3000,
                        showConfirmButton: false
                    });
                } else {

                    swal({
                        title: "操作错误!",
                        text: "错误消息.",
                        type: 'error',
                        timer: 3000,
                        showConfirmButton: false
                    });
                }
            },
            complete: function () {

            },
            error: function () {
                swal({
                    title: "操作错误!",
                    text: "错误消息.",
                    type: 'error',
                    timer: 3000,
                    showConfirmButton: false
                });
            }

        });
    });

    get_scheduler_state();

}


function get_scheduler_config() {

    $.ajax({
        type: "POST",
        url: "/api/getSchedulerConfig",
        data: {},
        dataType: "json",
        beforeSend: function () {

        },
        success: function (data) {
            if (!data.status && data.isRedirect) {

                window.location = data.location;
                return false;
            }


            var theBody = "";

            theBody +=
                "<tr>" +
                "<td>调度器配置</td>" +
                "<td>" + data.configId + "</td>" +
                "<td>" + data.redisHost + "</td>" +
                "<td>" + data.redisPort + "</td>" +
                "<td>" + data.maxTaskQueueSize + "</td>" +
                "<td>" + data.maxTaskRun + "</td>" +
                "<td>" + data.slaveHeartbeatInterval + "</td>" +
                "<td>" + data.maxHeartbeatTimeoutCount + "</td>" +
                "<td>" + data.slaveAppScript + "</td>" +
                "<td>" + data.adminPassword + "</td>" +
                "</tr>";

            $("#scheduler-config-table-body").html(theBody);

            $('#scheduler-config-id').attr('value', data.configId);
            $('#scheduler-config-redis-ip').attr('value', data.redisHost);
            $('#scheduler-config-redis-port').attr('value', data.redisPort);
            $('#scheduler-config-max-task-queue').attr('value', data.maxTaskQueueSize);
            $('#scheduler-config-max-task-run').attr('value', data.maxTaskRun);
            $('#scheduler-config-checkinterval').attr('value', data.slaveHeartbeatInterval);
            $('#scheduler-config-max-timeout-count').attr('value', data.maxHeartbeatTimeoutCount);
            $('#scheduler-config-app-script').attr('value', data.slaveAppScript);
            $('#scheduler-config-admin-password').attr('value', data.adminPassword);
        },
        complete: function () {
        },
        error: function () {
        }
    });

}


function update_scheduler_config() {

    $.ajax({
        type: "POST",
        url: "/api/updateSchedulerConfig",
        data: {
            configId: $('#scheduler-config-id').val(),
            redisHost: $('#scheduler-config-redis-ip').val(),
            redisPort: $('#scheduler-config-redis-port').val(),
            maxTaskQueueSize: $('#scheduler-config-max-task-queue').val(),
            maxTaskRun: $('#scheduler-config-max-task-run').val(),
            maxHeartbeatTimeoutCount: $('#scheduler-config-checkinterval').val(),
            slaveHeartbeatInterval: $('#scheduler-config-max-timeout-count').val(),
            slaveAppScript: $('#scheduler-config-app-script').val(),
            adminPassword: $('#scheduler-config-admin-password').val()
        },
        dataType: "json",
        beforeSend: function () {

        },
        success: function (data) {

            if (!data.status && data.isRedirect) {

                window.location = data.location;
                return false;
            }

            if (data.status) {
                swal({
                    title: "更新操作成功!",
                    text: "成功消息.",
                    type: 'success',
                    timer: 3000,
                    showConfirmButton: false
                });
            } else {

                swal({
                    title: "更新操作错误!",
                    text: "错误消息.",
                    type: 'error',
                    timer: 3000,
                    showConfirmButton: false
                });
            }
            //delay(3000);
            //window.location = 'scheduler-control.html';

            $('#modal-config-info').modal('hide');
        },
        complete: function () {

        },
        error: function () {
            swal({
                title: "更新操作错误!",
                text: "错误消息.",
                type: 'error',
                timer: 3000,
                showConfirmButton: false
            });

            $('#modal-config-info').modal('hide');
        }
    });

    get_scheduler_config();
}


function slave_add() {

    $.ajax({
        type: "POST",
        url: "/api/addSlave",
        data: {
            slaveId: $('#slave-id').val(),
            slaveIp: $('#slave-ip').val(),
            slaveSshPort: $('#slave-port').val(),
            slaveUsername: $('#slave-username').val(),
            slavePassword: $('#slave-password').val(),
            slaveAppPath: $('#slave-app-path').val()
        },
        dataType: "json",
        beforeSend: function () {

        },
        success: function (data) {

            if (!data.status && data.isRedirect) {

                window.location = data.location;
                return false;
            }

            if (data.status) {
                swal({
                    title: "添加操作成功!",
                    text: "成功消息.",
                    type: 'success',
                    timer: 3000,
                    showConfirmButton: false
                });
            } else {

                swal({
                    title: "添加操作错误!",
                    text: "错误消息.",
                    type: 'error',
                    timer: 3000,
                    showConfirmButton: false
                });
            }
            //delay(3000);
            //window.location = 'scheduler-control.html';

            $('#modal-slave-edit').modal('hide');
        },
        complete: function () {

        },
        error: function () {
            swal({
                title: "添加操作错误!",
                text: "错误消息.",
                type: 'error',
                timer: 3000,
                showConfirmButton: false
            });

            $('#modal-slave-edit').modal('hide');
        }
    });

}


function slave_add_btn() {

    $('#slave-id').attr('value', '');
    $('#slave-ip').attr('value', '');
    $('#slave-port').attr('value', '');
    $('#slave-username').attr('value', '');
    $('#slave-password').attr('value', '');
    $('#slave-app-path').attr('value', '');
    $('#modal-slave-edit-title').html('添加节点');
    $('#modal-slave-edit-btn').html('添 加');
    $('#modal-slave-edit-btn').attr('onclick', 'slave_add()');
    $('#modal-slave-edit').modal('show');
}


function slave_edit_btn(row_id) {

    $.ajax({
        type: "POST",
        url: "/api/getEditSlave",
        data: {
            slaveId: row_id
        },
        dataType: "json",
        beforeSend: function () {
        },
        success: function (data) {
            if (!data.status && data.isRedirect) {

                window.location = data.location;
                return false;
            }
            $('#slave-id').attr('value', data.slaveId);
            $('#slave-ip').attr('value', data.slaveIp);
            $('#slave-port').attr('value', data.slaveSshPort);
            $('#slave-username').attr('value', data.slaveUsername);
            $('#slave-password').attr('value', data.slavePassword);
            $('#slave-app-path').attr('value', data.slaveAppPath);

        },
        complete: function () {

        },
        error: function () {

        }
    });
    $('#modal-slave-edit-title').html('修改节点');
    $('#modal-slave-edit-btn').html('修 改');
    $('#modal-slave-edit-btn').attr('onclick', 'slave_update_btn()');
    $('#modal-slave-edit').modal('show');

}


function slave_delete_btn(row_id) {


    swal({
        title: "确定删除此节点?",
        text: "删除节点警告!",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "确 定",
        closeOnConfirm: false

    }, function () {
        $.ajax({
            type: "POST",
            url: "/api/deleteSlaveById",
            data: {
                slaveId: row_id
            },
            dataType: "json",
            beforeSend: function () {
            },
            success: function (data) {
                if (!data.status && data.isRedirect) {

                    window.location = data.location;
                    return false;
                }

                if (data.status) {

                    swal({
                        title: "删除节点成功!",
                        text: "成功消息.",
                        type: 'success',
                        timer: 3000,
                        showConfirmButton: false
                    });

                    return true;
                }

                swal({
                    title: "删除节点错误!",
                    text: "错误消息.",
                    type: 'error',
                    timer: 3000,
                    showConfirmButton: false
                });

            },
            complete: function () {

            },
            error: function () {

                swal({
                    title: "删除节点错误!",
                    text: "错误消息.",
                    type: 'error',
                    timer: 3000,
                    showConfirmButton: false
                });
            }
        });

    });

}


function slave_update_btn() {

    $.ajax({
        type: "POST",
        url: "/api/updateSlave",
        data: {
            slaveId: $('#slave-id').val(),
            slaveIp: $('#slave-ip').val(),
            slaveSshPort: $('#slave-port').val(),
            slaveUsername: $('#slave-username').val(),
            slavePassword: $('#slave-password').val(),
            slaveAppPath: $('#slave-app-path').val()
        },
        dataType: "json",
        beforeSend: function () {

        },
        success: function (data) {

            if (!data.status && data.isRedirect) {

                window.location = data.location;
                return false;
            }

            if (data.status) {
                swal({
                    title: "更新操作成功!",
                    text: "成功消息.",
                    type: 'success',
                    timer: 3000,
                    showConfirmButton: false
                });
            } else {

                swal({
                    title: "更新操作错误!",
                    text: "错误消息.",
                    type: 'error',
                    timer: 3000,
                    showConfirmButton: false
                });
            }
            //delay(3000);
            //window.location = 'scheduler-control.html';

            $('#modal-slave-edit').modal('hide');
        },
        complete: function () {

        },
        error: function () {
            swal({
                title: "更新操作错误!",
                text: "错误消息.",
                type: 'error',
                timer: 3000,
                showConfirmButton: false
            });

            $('#modal-slave-edit').modal('hide');
        }
    });
}


function task_add_btn() {

    //title id
    $('#modal-task-title').html('添加新任务');
    //modal box confirm button
    $('#confirm-btn').html('添 加');
    $('#confirm-btn').attr('onclick', 'add_new_or_update_task("/api/addNewTask")');


    $('#userId').attr('value', '');
    $('#taskId').attr('value', '');
    $('#taskId').attr('readonly', false);
    $('#taskName').attr('value', '');
    $('#taskType').attr('value', '');
    $('#taskRemark').attr('value', '');
    $('#taskSeedUrl').attr('value', '');
    $('#taskCrawlerDepth').attr('value', '');
    $('#taskDynamicDepth').attr('value', '');
    $('#taskPass').attr('value', '');
    $('#taskWeight').attr('value', '');
    $('#taskStartTime').attr('value', '');
    $('#taskRecrawlerDays').attr('value', '');
    $('#taskTemplatePath').attr('value', '');
    $('#taskTagPath').attr('value', '');
    $('#taskSeedPath').attr('value', '');
    $('#taskConfigPath').attr('value', '');
    $('#taskClickRegexPath').attr('value', '');
    $('#taskProtocolFilterPath').attr('value', '');
    $('#taskSuffixFilterPath').attr('value', '');
    $('#taskRegexFilterPath').attr('value', '');
    $('#taskStatus').attr('value', '');
    $('#taskInstanceThreads').attr('value', '');
    $('#taskNodeInstances').attr('value', '');
    $('#taskStopTime').attr('value', '');


    $('#modal-task-table').modal('show');
}


function add_new_or_update_task(api_path) {

    var t1 = "" + $('#taskStartTime').val();
    var t2 = "" + $('#taskStopTime').val();

    var y1 = t1.split('/')[2].split(' ')[0];
    var m1 = t1.split('/')[0];
    var d1 = t1.split('/')[1];
    var h1;
    if (t1.split('/')[2].split(' ')[2] == 'PM') {
        var hh = (t1.split('/')[2].split(' ')[1].split(':')[0]) * 1 + 12;
        h1 = hh + ":" + (t1.split('/')[2].split(' ')[1].split(':')[1] + ":00");
    } else {
        h1 = (t1.split('/')[2].split(' ')[1]) + ":00";
    }


    var y2 = t2.split('/')[2].split(' ')[0];
    var m2 = t2.split('/')[0];
    var d2 = t2.split('/')[1];
    var h2;
    if (t1.split('/')[2].split(' ')[2] == 'PM') {
        var hh = (t1.split('/')[2].split(' ')[1].split(':')[0]) * 1 + 12;
        h2 = hh + ":" + (t1.split('/')[2].split(' ')[1].split(':')[1] + ":00");
    } else {
        h2 = (t1.split('/')[2].split(' ')[1]) + ":00";
    }

    $.ajax({
        type: "POST",
        url: api_path,
        data: {
            taskId: $('#taskId').val(),
            userId: $('#userId').val(),
            taskName: $('#taskName').val(),
            taskType: $('#taskType').val(),
            taskStatus: $('#taskStatus').val(),
            taskRemark: $('#taskRemark').val(),
            taskSeedUrl: $('#taskSeedUrl').val(),
            taskCrawlerDepth: $('#taskCrawlerDepth').val(),
            taskPass: $('#taskPass').val(),
            taskNodeInstances: $('#taskNodeInstances').val(),
            taskInstanceThreads: $('#taskInstanceThreads').val(),
            taskWeight: $('#taskWeight').val(),
            taskStartTime: y1 + '-' + m1 + '-' + d1 + ' ' + h1,//$('#taskStartTime').val(),
            taskStopTime: y2 + '-' + m2 + '-' + d2 + ' ' + h2,//$('#taskStopTime').val(),
            taskRecrawlerDays: $('#taskRecrawlerDays').val(),
            taskTemplatesPath: $('#taskTemplatesPath').val(),
            taskTagPath: $('#taskTagPath').val(),
            taskSeedPath: $('#taskSeedPath').val(),
            taskConfigPath: $('#taskConfigPath').val(),
            taskClickRegexPath: $('#taskClickRegexPath').val(),
            taskProtocolFilterPath: $('#taskProtocolFilterPath').val(),
            taskSuffixFilterPath: $('#taskSuffixFilterPath').val(),
            taskRegexFilterPath: $('#taskRegexFilterPath').val()
        },
        dataType: "json",
        beforeSend: function () {

        },
        success: function (data) {

            if (!data.status && data.isRedirect) {

                window.location = data.location;
                return false;
            }

            if (data.status) {
                swal({
                    title: "更新操作成功!",
                    text: "成功消息.",
                    type: 'success',
                    timer: 3000,
                    showConfirmButton: false
                });
            } else {

                swal({
                    title: "更新操作错误!",
                    text: "错误消息.",
                    type: 'error',
                    timer: 3000,
                    showConfirmButton: false
                });
            }

            $('#modal-task-table').modal('hide');
        },
        complete: function () {

        },
        error: function () {
            swal({
                title: "更新操作错误!",
                text: "错误消息.",
                type: 'error',
                timer: 3000,
                showConfirmButton: false
            });

            $('#modal-slave-edit').modal('hide');
        }
    });


}


function task_table_detail_btn(row_id) {

    window.location = "task-detail.html?taskId=" + row_id;
}


function task_table_edit_btn(row_id) {

    $.ajax({
        type: "POST",
        url: "/api/getTaskDetailById",
        data: {
            taskId: row_id
        },
        dataType: "json",
        beforeSend: function () {

        },
        success: function (data) {

            if (!data.status && data.isRedirect) {

                swal({
                    title: "获取任务详情失败!",
                    text: "错误消息.",
                    type: 'error',
                    timer: 3000,
                    showConfirmButton: false
                });

                window.location = data.location;
                return false;
            }

            $('#userId').attr('value', data.userId);
            $('#taskId').attr('value', data.taskId);
            $('#taskId').attr('readonly', true);
            $('#taskName').attr('value', data.taskName);
            $('#taskType').attr('value', data.taskType);
            $('#taskRemark').attr('value', data.taskRemark);
            $('#taskSeedUrl').attr('value', data.taskSeedUrl.seedurlJsonString);
            $('#taskCrawlerDepth').attr('value', data.taskCrawlerDepth);
            $('#taskDynamicDepth').attr('value', data.taskDynamicDepth);
            $('#taskPass').attr('value', data.taskPass);
            $('#taskWeight').attr('value', data.taskWeight);
            $('#taskStartTime').attr('value', new Date(data.taskStartTime));
            $('#taskRecrawlerDays').attr('value', data.taskRecrawlerDays);
            $('#taskTemplatePath').attr('value', data.taskTemplatePath);
            $('#taskTagPath').attr('value', data.taskTagPath);
            $('#taskSeedPath').attr('value', data.taskSeedPath);
            $('#taskConfigPath').attr('value', data.taskConfigPath);
            $('#taskClickRegexPath').attr('value', data.taskClickRegexPath);
            $('#taskProtocolFilterPath').attr('value', data.taskProtocolFilterPath);
            $('#taskSuffixFilterPath').attr('value', data.taskSuffixFilterPath);
            $('#taskRegexFilterPath').attr('value', data.taskRegexFilterPath);
            $('#taskStatus').attr('value', data.taskStatus);
            $('#taskInstanceThreads').attr('value', data.taskInstanceThreads);
            $('#taskNodeInstances').attr('value', data.taskNodeInstances);
            $('#taskStopTime').attr('value', new Date(data.taskStopTime));


        },
        complete: function () {

        },
        error: function () {
            swal({
                title: "操作错误!",
                text: "错误消息.",
                type: 'error',
                timer: 3000,
                showConfirmButton: false
            });
        }
    });

    //title id
    $('#modal-task-title').html('更新任务配置');
    //modal box confirm button
    $('#confirm-btn').html('更 新');
    $('#confirm-btn').attr('onclick', 'add_new_or_update_task("/api/updateTaskById")');
    $('#modal-task-table').modal('show');

}


function task_table_delete_btn(row_id) {

    swal({
        title: "确定删除此任务?",
        text: "删除任务警告!",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "确 定",
        closeOnConfirm: false

    }, function () {
        $.ajax({
            type: "POST",
            url: "/api/deleteTaskById",
            data: {
                taskId: row_id
            },
            dataType: "json",
            beforeSend: function () {
            },
            success: function (data) {
                if (!data.status && data.isRedirect) {

                    window.location = data.location;
                    return false;
                }

                if (data.status) {

                    swal({
                        title: "删除任务成功!",
                        text: "成功消息.",
                        type: 'success',
                        timer: 3000,
                        showConfirmButton: false
                    });

                    return true;
                }

                swal({
                    title: "删除任务错误!",
                    text: "错误消息.",
                    type: 'error',
                    timer: 3000,
                    showConfirmButton: false
                });

            },
            complete: function () {

            },
            error: function () {

                swal({
                    title: "删除任务错误!",
                    text: "错误消息.",
                    type: 'error',
                    timer: 3000,
                    showConfirmButton: false
                });
            }
        });

    });
}


function slave_table_edit() {

    //slave table
    $("#slave-table-command").bootgrid({
        css: {
            icon: 'md icon',
            iconColumns: 'md-view-module',
            iconDown: 'md-expand-more',
            iconRefresh: 'md-refresh',
            iconUp: 'md-expand-less'
        },
        formatters: {
            "commands": function (column, row) {
                return "<button  type=\"button\" class=\"btn btn-icon command-edit\" onclick=\"slave_edit_btn('" + row.id + "')\" data-row-id=\"" + row.id + "\"><span class=\"md md-edit\"></span></button> " +
                    "<button type=\"button\" class=\"btn btn-icon command-delete\" onclick=\"slave_delete_btn('" + row.id + "')\" data-row-id=\"" + row.id + "\"><span class=\"md md-delete\"></span></button>";
            }
        }
    });
}


function task_table_edit() {

    //task table
    $("#task-table-command").bootgrid({
        css: {
            icon: 'md icon',
            iconColumns: 'md-view-module',
            iconDown: 'md-expand-more',
            iconRefresh: 'md-refresh',
            iconUp: 'md-expand-less'
        },
        formatters: {
            "commands": function (column, row) {
                return "<button type=\"button\" class=\"btn btn-default btn-icon\" onclick=\"task_table_detail_btn('" + row.id + "')\" data-row-id=\"" + row.id + "\"><span class=\"md md-apps\"></span></button> " +
                    "<button type=\"button\" class=\"btn btn-icon command-edit\" onclick=\"task_table_edit_btn('" + row.id + "')\" data-row-id=\"" + row.id + "\"><span class=\"md md-edit\"></span></button> " +
                    "<button type=\"button\" class=\"btn btn-icon command-delete\" onclick=\"task_table_delete_btn('" + row.id + "')\" data-row-id=\"" + row.id + "\"><span class=\"md md-delete\"></span></button>";
            }
        }
    });
}


function getAllSlave() {

    $.ajax({
        type: "POST",
        url: "/api/getAllSlaveInfo",
        data: {},
        dataType: "json",
        beforeSend: function () {

        },
        success: function (data) {
            if (!data.status && data.isRedirect) {

                window.location = data.location;
                return false;
            }

            var size = data.length;
            var theBody = "";
            for (var i = 0; i < size; i++) {

                theBody +=
                    "<tr>" +
                    "<td>" + data[i].slaveId + "</td>" +
                    "<td>" + data[i].slaveIp + "</td>" +
                    "<td>" + data[i].slaveSshPort + "</td>" +
                    "<td>" + data[i].slaveUsername + "</td>" +
                    "<td>" + data[i].slavePassword + "</td>" +
                    "<td>" + data[i].slaveAppPath + "</td>" +
                    "</tr>";
            }

            $("#slave-table-body").html(theBody);
            slave_table_edit();
        },
        complete: function () {
        },
        error: function () {
        }
    });

    //slave_table_edit();
}


function getAllTaskShortcut() {

    $.ajax({
        type: "POST",
        url: "/api/getAllTaskShortcut",
        data: {},
        dataType: "json",
        beforeSend: function () {

        },
        success: function (data) {
            if (!data.status && data.isRedirect) {

                window.location = data.location;
                return false;
            }

            var size = data.length;
            var theBody = "";
            for (var i = 0; i < size; i++) {

                theBody +=
                    "<tr>" +
                    "<td>" + data[i].taskId + "</td>" +
                    "<td>" + data[i].userId + "</td>" +
                    "<td>" + data[i].taskName + "</td>" +
                    "<td>" + data[i].taskType + "</td>" +
                    "<td>" + data[i].taskRemark + "</td>" +
                    "</tr>";
            }

            $("#task-table-body").html(theBody);
            task_table_edit();
        },
        complete: function () {
        },
        error: function () {
        }
    });

    //task_table_edit();
}


$(document).ready(function () {
    get_scheduler_state();
    get_scheduler_config();
    getAllSlave();
    getAllTaskShortcut();
});
