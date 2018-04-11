"use strict";
(function($){
    var fileupLoad={
        //单个文件的移除
        removeImg:function (r){
            $("#myFile").val('');//清空input的值以避免下一次相同图片无法触发change事件
            $(r).parent().parent().remove();
        },

        //图片自适应
        DrawImages:function(imgsource, iwidth, iheight){
            var image = new Image();
            image.src = imgsource.src;
            if (image.width > 0 && image.height > 0) {
                if (image.width / image.height >= iwidth / iheight) {
                    if (image.width > iwidth) {
                        imgsource.width = iwidth;
                        imgsource.height = (image.height * iwidth) / image.width;
                    } else {
                        imgsource.width = image.width;
                        imgsource.height = image.height;
                    }
                } else {
                    if (image.height > iheight) {
                        imgsource.height = iheight;
                        imgsource.width = (image.width * iheight) / image.height;
                    } else {
                        imgsource.width = image.width;
                        imgsource.height = image.height;
                    }
                }
            }

        },

        //选中效果
        cboxEffect:function(){
            //选中效果
            $(".cbox").click(function(){
                if($(this).is(':checked')){
                    $(this).parents(".isFile,.isImg").css("border","5px solid #ffc4c4");
                }else{
                    $(this).parents(".isFile,.isImg").css("border","5px solid #e5e5e5");
                }
            });
        }

    };

    function setCurrentDom(currentVal){
        var imgDiv = $('#imgDiv'+currentVal);
        imgDiv.empty();
        var list = $('#filesbox'+currentVal).find('.isFile');
        for(var i=0;i<list.length;i++){
            var item=$(list[i]),
                attrVal=item.attr('data-value');
            if(attrVal==currentVal){
                var cloneItem=item.clone();
                imgDiv.append(cloneItem);
            }
        }
    }

    $.tree=function(options){
        fileupLoad.options=options;
        console.log(options);
        if(fileupLoad.options)
        {
            //返回当前行对象
            fileupLoad.options.getCurrentRowObj=function () {
                return  fileupLoad.options.currentRowObj;
            };

            //返回当前行Li
            fileupLoad.options.getCurrentLi=function () {
                var currentRow=null;
                var currentRowObj=fileupLoad.options.getCurrentRowObj();
                return currentRowObj;
            };

            //返回当前行数据结构
            fileupLoad.options.getCurrentRow=function () {
                var currentRow=null;
                var currentRowObj=fileupLoad.options.getCurrentRowObj();
                if(currentRowObj)
                {
                    currentRow=$(currentRowObj).data("Current");
                }
                return currentRow;
            };

            //设置当前行数量
            fileupLoad.options.setCurrentFileCount=function (count) {
                var currentRowObj=fileupLoad.options.getCurrentRowObj();
                if(currentRowObj)
                {
                    var em=$(currentRowObj).data("em");
                    if(em)
                    {
                        $(em).html(count);
                    }
                }
            };
            //获取当前行数量
            fileupLoad.options.getCurrentFileCount=function () {
                var currentRowObj=fileupLoad.options.getCurrentRowObj();
                if(currentRowObj)
                {
                    var em=$(currentRowObj).data("em");
                    if(em)
                    {
                        return $(em).html();
                    }
                }
            };

            //我提供一个son对象，界面上要取我这个结果。
            fileupLoad.options.addFile=function (fileItem) {
                var ext = fileItem.ext;
                var imgtypes =["jpg","png","gif"];
                var filetypes =["jpg","png","txt","gif","pdf","doc","docx","xls","xlsx"];
                var newval = fileupLoad.options.getCurrentLi().attr("data-value");
                var pf = $('#pic-fail');//图片超出限制大小上传失败
                var img_html = '';
                if($.inArray(ext,filetypes)>=0){
                    if($.inArray(ext,imgtypes)<0){
                        img_html = "<div class='isFile' data-value='"+ newval + "' data-url='" + fileItem.url + "' data-id='" + fileItem.docID + "'>"+
                            "<img class='finish' src='images/finish.png' width='120'/>"+
                            "<div class='icover'>"+
                            "<button class='removeBtn'>╳</button>"+
                            "<input type='checkbox' class='cbox'>"+
                            "</div>"+
                            "<p class='fj-name' data-ext='" + fileItem.ext + "'>" + fileItem.title + "</p>"+
                            "</div>";
                    }else{
                        img_html = "<div class='isFile' data-value='"+ newval + "' data-url='" + fileItem.url + "' data-id='" + fileItem.docID + "'>"+
                            "<img class='finish' src='" + fileItem.url + "?x-oss-process=image/resize,w_300' width='120'/>"+
                            "<div class='icover'>"+
                            "<button class='removeBtn'>╳</button>"+
                            "<input type='checkbox' class='cbox'>"+
                            "</div>"+
                            "<p class='fj-name' data-ext='" + fileItem.ext + "'>" + fileItem.title + "</p>"+
                            "</div>";
                    }
                }else{
                    pf.show();
                    pf.html("请选择格式正确的文件");
                    setTimeout(function(){pf.hide();},2000);
                    //target.value ="";
                }
                var addItems_doc=$(img_html);
                $(".myFile").parent(".a-upload").after(addItems_doc);
                //var addItems=addItems_doc;
                if(addItems_doc)
                {
                    //当前文件绑定当前数据
                    $(addItems_doc).data("item",fileItem);
                }
                console.log(addItems_doc.data("item"));
                $(".chk-list").append(img_html);
                var addnum = parseInt(fileupLoad.options.getCurrentFileCount());
                addnum++;
                fileupLoad.options.setCurrentFileCount(addnum);
                fileupLoad.cboxEffect();
            };
        }

        var jsondata=options.data;
        var multiple=options.multiple;
        var subItems = '';
        var Items = '';
        var fjItems = '';
        var f_fjItems = '';
        var select_val=0;
        fjItems += "<div class='filesbox'>";
        for (var i = 0; i < jsondata.length; i++) {
            subItems += '<li><span class="showmenu">' + jsondata[i].text + '<i></i></span><ul class="tree2">';
            if (jsondata[i].sons != undefined) {
                for (var m = 0; m < jsondata[i].sons.length; m++) {
                    var _value = jsondata[i].sons[m].value;
                    var _text = jsondata[i].sons[m].text;
                    var _flen = jsondata[i].sons[m].files.length;
                    var limyName="li_i"+i+"_m"+m;
                    var emmyName="em_li_i"+i+"_m"+m;
                    subItems += '<li name="'+limyName+'" data-value="' + _value + '">' + _text + '<em name="'+emmyName+'">' + _flen + '</em></li>';
                    Items += "<div class='filebox' id='filesbox"+_value+"' data-value='" + _value + "'>"+
                        "<div class='file-nav-box'>"+
                        "<ul class='file-nav clearfix'>"+
                        "<li class='upload select'>上传文件</li>"+
                        "<li class='chk'>查看附件</li>"+
                        "</ul>"+
                        "<button class='del-file'>删除</button>"+
                        "<div class='s-all-box'><input type='checkbox' id='s-all' class='s-all'><label for='s-all'>全选</label></div>"+
                        "</div>"+
                        "<ul class='file-list'>"+
                        "<li class='upload-list'>"+
                        "<div class='Upload_picture'>"+
                        "<div class='img_div'>"+
                        "<h2>" + _text + "</h2>"+
                        "<a href='javascript:;' class='a-upload'>"+
                        "<input type='file' name='myFile' class='myFile' id='myFile'/>"+
                        "</a><div id='imgDiv"+jsondata[i].sons[m].value+"'></div>"+
                        "</div>"+
                        "<div class='clear'></div>"+
                        "</div>"+
                        "</li>"+
                        "<li class='chk-list img_div'>"+
                        "</li>"+
                        "</ul>"+
                        "</div>";
                    if (jsondata[i].sons[m].files != undefined) {
                        for (var n = 0; n < jsondata[i].sons[m].files.length; n++) {
                            var _title = jsondata[i].sons[m].files[n].title;
                            var _url = jsondata[i].sons[m].files[n].url;
                            var _ext = jsondata[i].sons[m].files[n].ext;
                            var _id = jsondata[i].sons[m].files[n].docID;
                            var file_name=limyName+"_file_"+n;
                            var imgtypes =["jpg","png","gif"];
                            if($.inArray(_ext,imgtypes)<0){
                                fjItems +=
                                    "<div name='"+file_name+"' class='isFile' data-value='"+ _value + "' data-url='" + _url + "' data-id='" + _id + "'>"+
                                    "<img class='finish' src='images/finish.png' width='120'/>"+
                                    "<div class='icover'>"+
                                    "<button class='removeBtn'>╳</button>"+
                                    "<input type='checkbox' class='cbox'>"+
                                    "</div>"+
                                    "<p class='fj-name' data-ext='" + _ext + "'>" + _title + "</p>"+
                                    "</div>";
                            }else{
                                fjItems +=
                                    "<div name='"+file_name+"' class='isFile' data-value='"+ _value + "' data-url='" + _url + "' data-id='" + _id + "'>"+
                                    "<img class='finish'  src='" + _url + "?x-oss-process=image/resize,w_300' width='120'/>"+
                                    "<div class='icover'>"+
                                    "<button class='removeBtn'>╳</button>"+
                                    "<input type='checkbox' class='cbox'>"+
                                    "</div>"+
                                    "<p class='fj-name' data-ext='" + _ext + "'>" + _title + "</p>"+
                                    "</div>";
                            }
                        }
                    }
                }
            }
            subItems += '</ul></li>';
        }
        fjItems += "</div>";
        select_val=jsondata[0].sons[0].value;
        //向当前文件节点，追加当前行对象
        var subItems_doc=$(subItems);
        var fjItems_doc=$(fjItems);
        $(options.elem).append(subItems_doc);
        $('#file-wrap').append(Items);
        $(".chk-list").append(fjItems_doc);
        //$(".upload-list").find(".img_div").append(fjItems_doc);
        //多文件上传与否
        if(multiple){
            $(".myFile").attr("multiple","multiple");
        }
        for (var i = 0; i < jsondata.length; i++)
        {
            for (var m = 0; m < jsondata[i].sons.length; m++)
            {
                var limyName="li_i"+i+"_m"+m;
                var emmyName="em_li_i"+i+"_m"+m;
                var liItem=   $(subItems_doc.find('*[name='+limyName+']')[0]);
                if(liItem)
                {
                    //当前li绑定当前行数据
                    $(liItem).data("Current",jsondata[i].sons[m]);
                }
                //console.log(liItem.data("Current"));
                var emItem= $(subItems_doc.find('*[name='+emmyName+']')[0]);
                if(emItem)
                {
                    //当前li绑定总数
                    $(liItem).data("em",emItem);
                }

                for (var n = 0; n < jsondata[i].sons[m].files.length; n++) {

                    var file_name=limyName+"_file_"+n;
                    var file=jsondata[i].sons[m].files[n];
                    //console.log(file_name);
                    var fileItem=$(fjItems_doc.find('*[name='+file_name+']')[0]);
                    if(fileItem)
                    {
                        //当前文件绑定当前数据
                        $(fileItem).data("item",file);
                    }
                    console.log(fileItem.data("item"));
                }
            }
        }

        //左侧导航显示隐藏
        $("#tree").on("click",".showmenu",function(){
            $(this).next(".tree2").toggle();
            $(this).find("i").toggleClass("tri-top");
        });
        //文件选项卡
        $(".file-nav").on("click","li",function(){debugger
            var idx = $(this).index();
            $("#s-all , .cbox").prop("checked",false);
            $(".icover").hide();
            $(".isFile,.isImg").css("border","5px solid #e5e5e5");
            $(this).addClass("select").siblings().removeClass("select");
            $(this).parents(".file-nav-box").next(".file-list").find("li").eq(idx).show().siblings().hide();
        });
        //右侧内容切换
        $(".filebox:eq(0)").show();
        var firstv = $(".tree2:eq(0) li:eq(0)").attr("data-value");
        $(".filebox:eq(0)").find(".file-list").find(".upload-list").find(".isFile[data-value!="+ firstv +"]").hide();//显示第一个子栏目对应的附件
        $(".tree li").on("click","li",function(){debugger
            console.log(fileupLoad.options.getCurrentRow());
            if(fileupLoad.options)
            {
                fileupLoad.options.currentRowObj=$(this);
            }
            console.log($(this));
            $(this).addClass("tselect").siblings().removeClass("tselect");
            $(this).parent().parent().siblings().find("li").removeClass("tselect");
            var thisval = $(this).attr("data-value");
            $("#s-all , .cbox").prop("checked",false);
            $(".icover").hide();
            $(".isFile,.isImg").css("border","5px solid #e5e5e5");
            $(".filebox[data-value="+ thisval +"]").show().siblings(".filebox").hide();
            setCurrentDom(thisval);
            //$(".filebox[data-value="+ thisval +"]").find(".file-list").find(".upload-list").find(".isFile[data-value!="+ thisval +"]").hide();
        });
        $(".tree2:eq(0) li:eq(0)").click(); //页面加载初始状态显示第一个子栏目
        //附件点击预览
        $(".file-list").on("click",".isFile",function(){
            var fext = $(this).find("p").attr("data-ext");
            //var fStart = ftitle.lastIndexOf(".");
            //var fname = ftitle.substring(fStart, ftitle.length);
            var imgtypes =["jpg","png","gif"];
            var urlstr = $(this).attr("data-url");
            if($.inArray(fext,imgtypes)>=0){
                //iframe层
                layer.open({
                    type: 2,
                    title: '文件预览',
                    shadeClose: true,
                    shade: 0.8,
                    area: ['80%', '90%'],
                    content: urlstr //iframe的url
                });
            }else{
                window.location.href=urlstr;
            }
        });
        $(".img_div").on("click",".cbox",function(e){
            e.stopPropagation();
        });
        $(".img_div").on("click",".removeBtn",function(e){
            e.stopPropagation();
        });
        //文件遮盖层的显示隐藏
        $(".img_div").on("mouseover mouseout",".isFile,.isImg",function(event){
            if(event.type == "mouseover"){
                $(this).find(".icover").show();
            }else if(event.type == "mouseout"){
                $(this).find(".icover").hide();
            }
        });
        //文件全选
        $(".s-all").on("click",function(){
            if($(this).is(':checked')){
                $(this).parent().parent().siblings(".file-list").find("li:visible").find(".isFile:visible").find(".icover").show().find(".cbox").prop("checked",true);
                $(this).parent().parent().siblings(".file-list").find("li:visible").find(".isFile,.isImg").css("border","5px solid #ffc4c4");
            }else{
                $(this).parent().parent().siblings(".file-list").find("li:visible").find(".isFile:visible").find(".icover").hide().find(".cbox").removeAttr("checked");
                $(this).parent().parent().siblings(".file-list").find("li:visible").find(".isFile,.isImg").css("border","5px solid #e5e5e5");
            }
        });
        fileupLoad.cboxEffect();
        //删除所有选中文件
        $(".del-file").on("click",function(){debugger
            if($(".cbox:checked").length!==0){
                var del_divList=$(".cbox:checked").parent().parent();
                var del_fileList=[];
                var del_docIDList=[];
                for(var i=0;i<del_divList.length;i++){
                    del_fileList.push(del_divList[i].data("item"));
                    del_docIDList.push(del_divList.eq(i).data("id"));
                    var cutnum = parseInt(fileupLoad.options.getCurrentFileCount());
                    cutnum--;
                    fileupLoad.options.setCurrentFileCount(cutnum);
                }
                console.log(del_docIDList);

                var delFile=function()
                {
                    $(".cbox:checked").parent().parent().remove();
                    $("#s-all ,.cbox").prop("checked",false);
                };
                var e={};
                e.del_fileList=del_fileList;
                e.del_docIDList=del_docIDList;
                e.delFile=delFile;
                if($.isFunction(fileupLoad.options.onBeforeDelFile))
                {
                    fileupLoad.options.onBeforeDelFile(e);
                }

            }else{
                $('#pic-fail').show();
                $('#pic-fail').html("请选择要删除的附件");
                setTimeout(function(){$('#pic-fail').hide();},2000);
            }
        });
        //删除单个文件
        $(".img_div").on("click",".removeBtn",function(){debugger
            var del_div=$(this).parent().parent();
            var ts=$(this);
            var del_fileList=[];
            var del_docIDList=[];
            del_fileList.push(del_div.data("item"));
            del_docIDList.push(del_div.attr("data-id"));
            console.log(del_docIDList);
            var delFile=function()
            {
                fileupLoad.removeImg(ts);
            };
            var e={};
            e.del_fileList=del_fileList;
            e.del_docIDList=del_docIDList;
            e.delFile=delFile;
            if($.isFunction(fileupLoad.options.onBeforeDelFile))
            {
                fileupLoad.options.onBeforeDelFile(e);
            }
            var cutnum = parseInt(fileupLoad.options.getCurrentFileCount());
            cutnum--;
            fileupLoad.options.setCurrentFileCount(cutnum);
        });
        //展开所有文档
        $(".navbox>span").click(function(){
            $(".tree2").toggle();
            $(".showmenu").find("i").toggleClass("tri-top");
        });
    };

    //如果选中 显示遮罩层
    if($(".cbox").is(':checked')){
        $(this).parents(".icover").show();
    }else{
        $(this).parents(".icover").hide();
    }
})(jQuery);





