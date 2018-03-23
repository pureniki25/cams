/**
 * 关闭上层layer
 */
var closePareantLayer = function(){
    if (typeof (parent.layer) != 'undefined') {
        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        parent.layer.close(index);
    }

}


//为表单添加输入
/**
 * 导出Excel时传参使用
 * @param form
 * @param type
 * @param name
 * @param value
 */
var addInput = function(form, type,name,value){
    var input=document.createElement("input");
    input.type=type;
    input.name=name;
    input.value = value;
    form.appendChild(input);
}

var expoertExcel = function (url,data) {

    axios.post(url,data)
        .then(function (res) {
            if (res.data.code == "1") {
                var fileName = res.data.data;
                console.log(fileName);
                var ExportForm = document.createElement("FORM");
                document.body.appendChild(ExportForm);
                ExportForm.method = "GET";
                ExportForm.action = basePath + "downLoadController/excelFiles";
                addInput(ExportForm, "text", "filename", fileName);//区域ID
                // http://localhost:30111/alms/core/collection/saveExcel?files=64eda1f0-8f76-4833-94f2-3407380ab37b.xls
                ExportForm.submit();


                document.body.removeChild(ExportForm);
                vm.$Modal.success({
                    title: '',
                    content: '操作成功'
                });
            } else {
                vm.$Modal.error({content: '操作失败，消息：' + res.data.msg});
            }
        })
        .catch(function (error) {
            vm.$Modal.error({content: '接口调用异常!'});
        });

}



/**
 * js获得html界面传参
 * @type {string}
 */

var LocString = String(window.document.location.href);
function getQueryStr(str) {
    var rs = new RegExp("(^|)" + str + "=([^&]*)(&|$)", "gi").exec(LocString), tmp;
    if (tmp = rs) {
        return tmp[2];
    }
// parameter cannot be found
    return "";
}


//流程状态  新建
var PROCESS_STATUS_NEW = "-1"
//流程状态  开始 草稿
var PROCESS_STATUS_START = "1"
//流程状态  进行中
var PROCESS_STATUS_RUNNING = "0"

//申请减免的最后一个状态
var APPLY_DERATE_LAST_STATUS = "400"

// axios.defaults.baseURL="http://localhost:30677";

/**
 * 阿拉伯数字转中文大写
 * 
 */
function ArabicToChinese(n) {

    if(n==''){
        return n
    }
    if (!/^(0|[1-9]\d*)(\.\d+)?$/.test(n)){
        return "数据非法";  //判断数据是否大于0
    }

    var unit = "千百拾亿千百拾万千百拾元角分", str = "";
    n += "00";  

    var indexpoint = n.indexOf('.');  // 如果是小数，截取小数点前面的位数

    if (indexpoint >= 0){

        n = n.substring(0, indexpoint) + n.substr(indexpoint+1, 2);   // 若为小数，截取需要使用的unit单位
    }

    unit = unit.substr(unit.length - n.length);  // 若为整数，截取需要使用的unit单位
    for (var i=0; i < n.length; i++){
        str += "零壹贰叁肆伍陆柒捌玖".charAt(n.charAt(i)) + unit.charAt(i);  //遍历转化为大写的数字
    }

    return str.replace(/零(千|百|拾|角)/g, "零").replace(/(零)+/g, "零").replace(/零(万|亿|元)/g, "$1").replace(/(亿)万|壹(拾)/g, "$1$2").replace(/^元零?|零分/g, "").replace(/元$/g, "元整"); // 替换掉数字里面的零字符，得到结果

}

/**
 * 子页面内新增tab,与clickNavByUrl一起使用
 * @param text 新增tab的标题
 */
var clickNavByText = function (text) {
    var list = parent.document.querySelectorAll('[data-options]')
    list.forEach(function (e, i) {
        var _text = e.querySelector("span").textContent;
        
        if (text.trim() === _text.trim()) e.click();
    })
}
/**
 * 子页面内新增tab,与clickNavByText一起使用
 * @param url 新增tab的url
 */
var clickNavByUrl = function (url) {
    var list = parent.document.querySelectorAll('[data-options]')
    list.forEach(function (e, i) {
       var opt = e.getAttribute('data-options');
       try {
           var _url = eval("("+opt+")").url;
           if (url.trim() === _url.trim()) e.click()
       } catch (e) {

       }
    })
}

let formatDate = function (DATE) {
    var year = DATE.getFullYear();
    var month = DATE.getMonth() + 1;
    var day = DATE.getDate();
    return year + "-" + month + "-" + day
}

let parseDate = function(str){
    
}