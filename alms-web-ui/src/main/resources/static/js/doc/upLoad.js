 var upLoadPage={};
// Cobject.ControllerUrl ="doc";
var form = null;
var laydate = null;
var app = null;
var mydata = null;
var tableIns = null;
var laytpl=null;
var table=null;
var element = null;
var businessId = "";


$(function () {

    window.layinit(function (htConfig) {
        var basePath=htConfig.coreBasePath;
        var businessId=getQueryString("businessId");
        var docTypeUrl=basePath+"doc/getAfterLoanDocPageInfo?businessId="+businessId;
        upLoadPage.businessID = businessId;

        axios.get(docTypeUrl)
            .then(function(result) {
            var pageInfo = result.data.data;
            if (pageInfo) {
                upLoadPage.upLoadView = {
                    data: pageInfo.docTypeList,
                    wpelem: '#file-wrap',
                    elem: '#tree',
                    multiple: true
                };

                upLoadPage.upLoadView.onBeforeDelFile = function (e) {
                    //待审核的文档列表
                    var delDocs = "";
                    if (e && e.del_docIDList) {
                        $(e.del_docIDList).each(function (ind, item) {
                            if (delDocs == "") {
                                delDocs = item;
                            }
                            else {
                                delDocs += "," + item;
                            }
                        });
                    }
                    if (delDocs != "") {
                        var iindex = layer.msg('提交中..', {icon: 16, time: 8000});
                        axios.get(basePath+"/doc/delDocs?docIds=" + delDocs).then( function (result) {
                            layer.close(iindex);
                            e.delFile();
                        });
                    }
                    else {
                        upLoadPage.alert("找不到删除的文件！");
                    }
                };

                var tree = $.tree(upLoadPage.upLoadView);
                var index = null;
                $('.myFile').fileupload({
                    url: basePath+"doc/upload",
                    dataType: 'json',
                    singleFileUploads: false,
                    acceptFileTypes: '', // Allowed file types
                    add: function (e, data) {
                        // var currentRow=  upLoadPage.upLoadView.getCurrentRow();
                        // upLoadPage.upLoadView.setCurrentFileCount(55);
                        //loading层
                        index = layer.load(1, {
                            shade: [0.3, '#000'] //0.1透明度的白色背景
                        });
                        jqXHRData = data;
                        if (jqXHRData) {
                            count = 0;
                            error_count = 0;
                            error_message = "";
                            result_message = "";
                            $("#divStatus").html("请选择要上传的文件! ");
                            jqXHRData.submit();
                        }
                    },
                    done: function (event, data) {
                        layer.close(index);
                        var result = data.result;
                        if (result) {
                            if (result.uploaded == false) {
                                layer.alert(result.message);
                            }
                            else {
                                if (result.docItemListJson) {
                                    var docItemList = JSON.parse(result.docItemListJson);
                                    if (docItemList) {
                                        $(docItemList).each(function (ind, item) {
                                            //上传成功
                                            upLoadPage.upLoadView.addFile(item);
                                        });
                                    }
                                }
                            }
                        }
                    },
                    fail: function (event, data) {
                        layer.close(index);
                        if (data && data.files && data.files.length > 0 && data.files[0].error) {
                            layer.alert(data.files[0].error);
                            // if (data.files[0].error) {
                            //     $("#divStatus").html(data.files[0].error);
                            // }
                        }
                    }
                });

                $('.myFile').bind('fileuploadsubmit', function (e, data) {
                    if (data && data.originalFiles && data.originalFiles.length > 0) {
                        data.formData = {fileName: data.originalFiles[0].name};
                        data.formData.businessId = upLoadPage.businessID;
                        var currentRow = upLoadPage.upLoadView.getCurrentRow();
                        if (currentRow) {
                            var newRow = $.extend({}, currentRow);
                            newRow.files = [];
                            newRow.sons = [];
                            data.formData.docTypeItemJson = JSON.stringify(newRow);
                        }
                    }
                });
            }

        });
    })


});