/**
 * Created by TianyuanPan on 7/15/16.
 */


/**

 $('#userId')
 $('#taskId')
 $('#taskName')
 $('#taskType')
 $('#taskRemark')
 $('#taskSeedUrl')
 $('#taskCrawlerDepth')
 $('#taskDynamicDepth')
 $('#taskPass')
 $('#taskWeight')
 $('#taskStartTime')
 $('#taskRecrawlerDays')
 $('#taskTemplatePath')
 $('#taskTagPath')
 $('#taskSeedPath')
 $('#taskConfigPath')
 $('#taskClickRegexPath')
 $('#taskProtocolFilterPath')
 $('#taskSuffixFilterPath')
 $('#taskRegexFilterPath')
 $('#taskStatus')
 $('#taskDeleteFlag')
 $('#taskSeedAmount')
 $('#taskSeedImportAmount')
 $('#taskCompleteTimes')
 $('#taskInstanceThreads')
 $('#taskNodeInstances')
 $('#taskStopTime')
 $('#taskCrawlerAmountInfo')
 $('#taskCrawlerInstanceInfo')


 **/



function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null)return unescape(r[2]);
    return null;
}


function getTaskDetail() {

    var tid = GetQueryString('taskId');

    if (tid == null) {

        swal({
            title: "参数错误!",
            text: "错误消息.",
            type: 'error',
            timer: 3000,
            showConfirmButton: false
        });
        return false;
    }

    $('#task-detail-id').html(tid);

    $.ajax({
        type: "POST",
        url: "/api/getTaskDetailById",
        data: {
            taskId: tid
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



            $('#userId').html(data.userId);
            $('#taskId').html(data.taskId);
            $('#taskName').html(data.taskName);
            $('#taskType').html(data.taskType);
            $('#taskRemark').html(data.taskRemark);
            $('#taskSeedUrl').html(data.taskSeedUrl.seedurlJsonString);
            $('#taskCrawlerDepth').html(data.taskCrawlerDepth);
            $('#taskDynamicDepth').html(data.taskDynamicDepth);
            $('#taskPass').html(data.taskPass);
            $('#taskWeight').html(data.taskWeight);
            $('#taskStartTime').html(new Date(data.taskStartTime));
            $('#taskRecrawlerDays').html(data.taskRecrawlerDays);
            $('#taskTemplatePath').html(data.taskTemplatePath);
            $('#taskTagPath').html(data.taskTagPath);
            $('#taskSeedPath').html(data.taskSeedPath);
            $('#taskConfigPath').html(data.taskConfigPath);
            $('#taskClickRegexPath').html(data.taskClickRegexPath);
            $('#taskProtocolFilterPath').html(data.taskProtocolFilterPath);
            $('#taskSuffixFilterPath').html(data.taskSuffixFilterPath);
            $('#taskRegexFilterPath').html(data.taskRegexFilterPath);
            $('#taskStatus').html(data.taskStatus);
            $('#taskDeleteFlag').html(data.taskDeleteFlag.toString());
            $('#taskSeedAmount').html(data.taskSeedAmount);
            $('#taskSeedImportAmount').html(data.taskSeedImportAmount);
            $('#taskCompleteTimes').html(data.taskCompleteTimes);
            $('#taskInstanceThreads').html(data.taskInstanceThreads);
            $('#taskNodeInstances').html(data.taskNodeInstances);
            $('#taskStopTime').html(new Date(data.taskStopTime));
            $('#taskCrawlerAmountInfo').html(data.taskCrawlerAmountInfo.crawlerAmountInfoJsonString);
            $('#taskCrawlerInstanceInfo').html(data.taskCrawlerInstanceInfo.crawlerInstanceInfoJsonString);

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

}


$(document).ready(function () {
    //alert(GetQueryString('taskId'));
    getTaskDetail();
});