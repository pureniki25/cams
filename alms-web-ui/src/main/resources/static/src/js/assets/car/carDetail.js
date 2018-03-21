var businessId = document.getElementById("businessId").getAttribute("value");
window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    basePath = _htConfig.coreBasePath;
    
var vm = new Vue({
    el: '#app',
    data: {
    	carBasic:{
    		vin:''//车架号
    		,engineModel:''//发动机号
    		,displacement:''	//排量
    		,invoiceCost:''//发票价
    		,trafficViolationSituation:''//违章情况
    		,trafficViolationFee:''//违章费用
    		,vehicleVesselTax:''	//车船税费用
    		,annualTicketFee:''//统缴费用
	    	,drivingLicenseConsistent:''//核对行驶资料
	    	,renewal:''//借款期内是否续保
	    	,fuelType:''//燃油形式
	    	,vehicleLicenseRegistrationDate:''//汽车注册日期
	    	,annualVerificationExpirationDate:''//年审到期日
	    	,mortgage:''//车辆抵押状态
	    	,odometer:''//里程表读数
	    	,fuelLeft:''//燃油量（约余）
    	},
    	carDetection:{
    		centerPanelNormal:''//中控台
    		,ventilatorNormal:''//空调
    		,interiorNormal:''//车厢内饰
    		,windowGlassNormal:''//玻璃
    		,radiatorNormal:''//水箱
    		,engineNormal:''//发动机
    		,frameNormal:''//车大梁
    		,tireNormal:''//轮胎
    		,spareTireNormal:''//备用胎
    		,doorNormal:''//车门
    			//,  :''其他情况说明
    		,evaluationAmount:''//评估金额
    		,accelerationPerformanceNormal:''//发动机性能
    		,brakingPerformanceNormal:''//刹车性能
    		,brakingBalancePerformanceNormal:''//刹车平衡性能
    		,steerPerformanceNormal:''//方向性能
    		,gearPerformanceNormal:''//挂档性能
    		,otherDriveDescription:''//其他情况说明
    		,vinConsistent:''//车架号是否一致
    		,engineModelConsistent:''//发动机号是否一致
    		,accident:''//是否有事故痕迹
    		,otherTrouble:''//是否有其他问题
    		,otherTroubleDescription:''//其他情况说明
    	}
    	,carAuction:{
    		createUser:''//申请人
    		,startingPrice:''//起拍价
    		,deposit:''//保证金
    		,fareRange:''//加价幅度
    		,reservePrice:''//保留价
    		,auctionStartTime:''//拍卖开始时间
    		,auctionEndTime:''//拍卖截止时间
    		,handleUnit:''//处置单位
    	}
    	,carAuctionReg:{
    		transPrice:''//成交价格
    		,regTel:''//联系方式
    	}
    	,carAuctionBidder:{
    		bidderName:''//最终获取人姓名
    		,bidderCertId:''//身份证号码
    	}
    },
    methods: {
    	viewData: function(){
    		var that = this;
    	   layui.use(['element', 'ht_config', 'ht_auth'], function () {
    		var element = layui.element;
    		var config = layui.ht_config;
            $.ajax({
                type: "POST",
                url: basePath+'car/carDetail',
                data: {"businessId":businessId},
                //contentType: "application/json; charset=utf-8",
                success: function (data) {
                	that.carBasic = data.data.carBasic;
                	that.carDetection=data.data.carDetection || {};
                	that.carAuction=data.data.carAuction || {};
                	that.carAuctionReg=data.data.carAuctionReg || {};
            		that.carAuctionBidder=data.data.carAuctionBidder || {};
                },
                error: function (message) {
                    layer.msg("查询车辆信息发生异常，请联系管理员。");
                    console.error(message);
                }
            });
    	});
    	}
    },
    beforeMount: function () {
    	this.viewData();
    }

});
});







