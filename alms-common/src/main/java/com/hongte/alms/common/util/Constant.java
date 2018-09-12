package com.hongte.alms.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zengkun
 * @since 2018/2/2
 */
public class Constant {

	// Excel存储文件夹名字
	// public static final String EXCEL_FILE_NAME = "excelTemp//";

//	public static final String DEV_DEFAULT_USER = "devDefaultUser";
//	public static final String DEFAULT_SYS_USER = "sysAutoSet";
//	public static final String SYS_DEFAULT_USER = "sysDefaultUser";
	public static final String ADMIN_ID = "0111130000";

	// 申请减免的流程类型ID
	public static final String APPLY_DERATE_PROCEEE_TYPE_ID = "b138a92f-f796-11e7-94ed-94c69109b34a";
	// 申请减免的最后一个节点值
	public static final Integer APPLY_DERATE_PROCEEE_LAST_STEP = 400;
	// 申请减免的第一个节点值
	public static final Integer APPLY_DERATE_PROCEEE_BEGIN_STEP = 100;

	/**
	 * 新增参数时,用于占位的参数值
	 */
	public static final Integer SYS_PARAMETER_PLACEHOLDER = -Integer.MAX_VALUE;

	/**
	 * 五级分类设置业务类别-条件操作类型：1、增加
	 */
	public static final String FIVE_LEVEL_CLASSIFY_CONDITION_ADD = "1";
	/**
	 * 五级分类设置业务类别-条件操作类型：2、修改
	 */
	public static final String FIVE_LEVEL_CLASSIFY_CONDITION_UPDATE = "2";
	/**
	 * 五级分类设置业务类别-条件操作类型：3、删除
	 */
	public static final String FIVE_LEVEL_CLASSIFY_CONDITION_DELETE = "3";
	/**
	 * 五级分类参数类型，对应tb_sys_parameter的param_type
	 */
	public static final String FIVE_LEVEL_CLASSIFY = "fiveLevelClassify";
	/**
	 * 五级分类操作源：贷后定时任务
	 */
	public static final String FIVE_LEVEL_CLASSIFY_OP_SOUSE_TYPE_ALMS_JOB = "1";
	/**
	 * 五级分类操作源：贷后跟踪记录
	 */
	public static final String FIVE_LEVEL_CLASSIFY_OP_SOUSE_TYPE_ALMS_LOG = "2";
	/**
	 * 五级分类操作源：风控
	 */
	public static final String FIVE_LEVEL_CLASSIFY_OP_SOUSE_TYPE_ALMS_RISK_CONTROL = "3";
	/**
	 * 五级分类：首期逾期
	 */
	public static final String FIVE_LEVEL_CLASSIFY_FIRST_PERIOD_OVERDUE = "首月逾期";
	/**
	 * 五级分类：本金逾期
	 */
	public static final String FIVE_LEVEL_CLASSIFY_PRINCIPAL_OVERDUE = "本金逾期";
	/**
	 * 五级分类：利息逾期
	 */
	public static final String FIVE_LEVEL_CLASSIFY_INTEREST_OVERDUE = "利息逾期";
	/**
	 * 五级分类：案件情况
	 */
	public static final String FIVE_LEVEL_CLASSIFY_CASE = "案件情况";
	/**
	 * 五级分类：档案资料情况
	 */
	public static final String FIVE_LEVEL_CLASSIFY_PROFILES = "档案资料情况";
	/**
	 * 五级分类：抵押物情况
	 */
	public static final String FIVE_LEVEL_CLASSIFY_GUARANTEE = "抵押物情况";
	/**
	 * 五级分类：主借款人情况
	 */
	public static final String FIVE_LEVEL_CLASSIFY_MAIN_BORROWER = "主借款人情况";
	/**
	 * 五级分类：主借款人情况
	 */
	public static final String FIVE_LEVEL_CLASSIFY_SPLIT = "#--SPLIT--#";
	/**
	 * 发送数据平台贷款余额（撮合业务余额）kafka主题
	 */
	public static final String TOTAL_BUSINESS_BALANCE_TOPIC = "storm_demo";
	/**
	 * 请求EIP资金分发接口 接口代码
	 */
	public static final String INTERFACE_CODE_SEND_DISTRIBUTE_FUND = "TdrepayRechargeService.sendDistributeFund";
	/**
	 * 请求EIP资金分发接口 接口名称
	 */
	public static final String INTERFACE_NAME_SEND_DISTRIBUTE_FUND = "请求EIP资金分发接口";
	/**
	 * 调用第三方日志 系统标识
	 */
	public static final String SYSTEM_CODE_EIP = "EIP";
	/**
	 * 偿还垫付接口 接口代码
	 */
	public static final String INTERFACE_CODE_ADVANCE_SHARE_PROFIT = "/eip/td/repayment/advanceShareProfit";
	/**
	 * 偿还垫付接口 接口名称
	 */
	public static final String INTERFACE_NAME_ADVANCE_SHARE_PROFIT = "请求EIP偿还垫付接口";
	/**
	 * 标的还款信息查询接口（/repayment/queryProjectPayment） 接口代码
	 */
	public static final String INTERFACE_CODE_QUERY_PROJECT_PAYMENT = "/eip/td/repayment/queryProjectPayment";
	/**
	 * 标的还款信息查询接口（/repayment/queryProjectPayment） 接口名称
	 */
	public static final String INTERFACE_NAME_QUERY_PROJECT_PAYMENT = "请求EIP标的还款信息查询接口";
	/**
	 * 还垫付信息查询接口 接口代码
	 */
	public static final String INTERFACE_CODE_RETURN_ADVANCE_SHARE_PROFIT = "/eip/td/repayment/returnAdvanceShareProfit";
	/**
	 * 还垫付信息查询接口 接口名称
	 */
	public static final String INTERFACE_NAME_RETURN_ADVANCE_SHARE_PROFIT = "请求EIP还垫付信息查询接口";
	/**
	 * 标的还款信息查询接口（/assetside/getProjectPayment） 接口代码
	 */
	public static final String INTERFACE_CODE_GET_PROJECT_PAYMENT = "/eip/td/assetside/getProjectPayment";
	/**
	 * 标的还款信息查询接口（/assetside/getProjectPayment） 接口代码
	 */
	public static final String INTERFACE_NAME_GET_PROJECT_PAYMENT = "请求EIP标的还款信息查询接口";
	/**
	 * 提前结清接口
	 */
	public static final String INTERFACE_CODE_REPAYMENT_EARLIER = "/eip/td/repayment/repaymentEarlier";
	/**
	 * 提前结清接口
	 */
	public static final String INTERFACE_NAME_REPAYMENT_EARLIER = "请求EIP提前结清接口";
	/**
	 * 资金分发订单查询
	 */
	public static final String INTERFACE_CODE_QUERY_DISTRIBUTE_FUND = "/eip/td/assetside/queryDistributeFund";
	/**
	 * 资金分发订单查询
	 */
	public static final String INTERFACE_NAME_QUERY_DISTRIBUTE_FUND = "请求EIP资金分发订单查询接口";
	/**
	 * 调用platrepay 服务的平台还款接口
	 */
	public static final String INTERFACE_CODE_PLATREPAY_REPAYMENT = "/platformRepayment/repayment";
	/**
	 * 调用platrepay 服务的平台还款接口
	 */
	public static final String INTERFACE_NAME_PLATREPAY_REPAYMENT = "平台合规化还款接口";
	/**
	 * 提前结清类型（坏账结清）BAD
	 */
	public static final String REPAYMENT_EARLIER_BAD = "BAD";
	/**
	 * 提前结清类型（正常结清）NORMAL
	 */
	public static final String REPAYMENT_EARLIER_NORMAL = "NORMAL";
	/**
	 * 调用EIP接口成功状态码
	 */
	public static final String REMOTE_EIP_SUCCESS_CODE = "0000";

	/**
	 * 调用LMS信贷系统接口成功状态码
	 */
	public static final String LMS_SUCCESS_CODE = "1";
	
	/**
	 * 空字符串
	 */
	public static final String BLANK_STR = "";

	//*********调用api接口失败记录相关 OPEN模块  zgh *********//
	public static final String INTERFACE_CODE_OPEN_REPAYPLAN_UPDATEREPAYPLANTOLMS = "/RepayPlan/updateRepayPlanToLMS";
	public static final String INTERFACE_NAME_OPEN_REPAYPLAN_UPDATEREPAYPLANTOLMS = "将指定业务的还款计划的变动通过信贷接口推送给信贷系统";

	//*********调用api接口失败记录相关 Finance模块 zgh *********//
	public static final String INTERFACE_CODE_FINANCE_FINANCE_PREVIEWCONFIRMREPAYMENT = "/finance/previewConfirmRepayment";
	public static final String INTERFACE_NAME_FINANCE_FINANCE_PREVIEWCONFIRMREPAYMENT = "确认还款拆标情况并存储";
	
	public static final String INTERFACE_CODE_FINANCE_NIWOCONTROLLER_SYCREPAYPLAN = "/niwoController/sycRepayPlan";
	public static final String INTERFACE_NAME_FINANCE_NIWOCONTROLLER_SYCREPAYPLAN = "同步你我金融还款计划";



	public static final String CANWITHHOLD_NO="该卡当前不支持代扣，请及时更换";
	public static final String CANWITHHOLD_YES_1="限额足够，可一次代扣";
	public static final String CANWITHHOLD_YES_2="单次限额不够，本次还款需要代扣%s次，可一次代扣";
	public static final String CANWITHHOLD_YES_3="代扣限额不足，请换卡";
	
	public static final String CAR_LOAN = "车贷";
	public static final String HOUSE_LOAN = "房贷";
	public static final String POVERTY_ALLEVIATION_LOAN = "扶贫贷";
	public static final String CHE_QUAN_LOAN = "车全业务";
	public static final String ER_SHOU_CHE_LOAN = "二手车业务";
	public static final String YI_DIAN_LOAN = "一点车贷";
	public static final String CREDIT_LOAN = "信用贷";
	public static final String COMMERCE_LOAN = "商贸贷";
	public static final String PROPRIETOR_LOAN = "业主贷";
	public static final String INSTALMENT_CREDIT_LOAN = "家装分期";
	public static final String COMMERCE_COMPANY_LOAN = "商贸贷共借";
	public static final String PROPRIETOR_COMPANY_LOAN = "业主贷共借";
	public static final String CAR_JOINTLY_TYPE = "车贷共借";
	public static final String HOUSE_JOINTLY_TYPE = "房贷共借";
	public static final String YD_CAR_JOINTLY_TYPE = "一点车贷共借";
	
	/**
	 * 线上线下部分分界线,参考tb_repayment_biz_plan_list_detail.share_profit_index和tb_repayment_proj_plan_list_detail.share_profit_index <br/>
	 * 1200以下是线上部分费用,超过1200是线下部分费用
	 */
	public static final Integer ONLINE_OFFLINE_FEE_BOUNDARY = 1200 ;


    /**
     * 宝付交易类型
     */
    public static Map<String, String> TRADE_TYPE_MAP_BAOFU = null;
    static {
        TRADE_TYPE_MAP_BAOFU = new ConcurrentHashMap<>();
        //支付
        TRADE_TYPE_MAP_BAOFU.put("00313", "卡类");
        TRADE_TYPE_MAP_BAOFU.put("01311", "B2C");
        TRADE_TYPE_MAP_BAOFU.put("01311", "B2B");
        TRADE_TYPE_MAP_BAOFU.put("01311", "Wap");
        TRADE_TYPE_MAP_BAOFU.put("06311", "银联手机支付（插件支付）");
        TRADE_TYPE_MAP_BAOFU.put("04311", "认证支付API");
        TRADE_TYPE_MAP_BAOFU.put("03311", "认证支付PC");
        TRADE_TYPE_MAP_BAOFU.put("05311", " 认证支付WAP");
        TRADE_TYPE_MAP_BAOFU.put("08311", "认证支付SDK");
        TRADE_TYPE_MAP_BAOFU.put("09311", "委托还款");
        TRADE_TYPE_MAP_BAOFU.put("10311", "代扣");
        TRADE_TYPE_MAP_BAOFU.put("14311", "分账产品-API类");
        TRADE_TYPE_MAP_BAOFU.put("15311", "分账产品-代扣");
        TRADE_TYPE_MAP_BAOFU.put("16311", "分账产品-认证SDK");
        TRADE_TYPE_MAP_BAOFU.put("17311", "分账产品-认证WAP");
        TRADE_TYPE_MAP_BAOFU.put("18311", "分账产品-认证PC");
        TRADE_TYPE_MAP_BAOFU.put("19311", "分账产品-快捷");
        TRADE_TYPE_MAP_BAOFU.put("31311", "分账产品-协议支付");
        TRADE_TYPE_MAP_BAOFU.put("20311", "非实时代扣");
        TRADE_TYPE_MAP_BAOFU.put("21311", " 分账产品-非实时代扣");
        TRADE_TYPE_MAP_BAOFU.put("04317", "分账产品-分账不过账");
        TRADE_TYPE_MAP_BAOFU.put("06317", "协议支付分账-不过账");
        TRADE_TYPE_MAP_BAOFU.put("13311", "支付宝支付");
        TRADE_TYPE_MAP_BAOFU.put("12311", "微信支付");
        TRADE_TYPE_MAP_BAOFU.put("11311", "快捷支付");
        TRADE_TYPE_MAP_BAOFU.put("30311", "协议支付");
        TRADE_TYPE_MAP_BAOFU.put("07311", "账户");
        TRADE_TYPE_MAP_BAOFU.put("22311", "转账支付");
        //充值
        TRADE_TYPE_MAP_BAOFU.put("01301", "B2C网银");
        TRADE_TYPE_MAP_BAOFU.put("01301", "B2B网银");
        TRADE_TYPE_MAP_BAOFU.put("01301", "线下打款");
        TRADE_TYPE_MAP_BAOFU.put("01301", "快捷");
        TRADE_TYPE_MAP_BAOFU.put("30511", "手续费账户");
        //转账
        TRADE_TYPE_MAP_BAOFU.put("00105", "转账到银行");
        TRADE_TYPE_MAP_BAOFU.put("00104", "转账到账户");
        //提现
        TRADE_TYPE_MAP_BAOFU.put("00101", "普通提现");
        //服务认证
        TRADE_TYPE_MAP_BAOFU.put("01341", "银行卡验证");
        TRADE_TYPE_MAP_BAOFU.put("01351", "身份证验证");
    }
	/**
	 * 宝付交易子类型
	 */
    public static Map<String, String> TRADE_SUB_TYPE_MAP_BAOFU = null;
    static {
        TRADE_SUB_TYPE_MAP_BAOFU = new ConcurrentHashMap<>();
        TRADE_SUB_TYPE_MAP_BAOFU.put("00", "成功");
        TRADE_SUB_TYPE_MAP_BAOFU.put("01", "退款");
        TRADE_SUB_TYPE_MAP_BAOFU.put("02", "撤销");
    }

	/**
	 * 快钱交易状态
	 */
	public static Map<String,String> TRADE_TYPE_MAP_KUAIQIAN=null;
	static{
		TRADE_TYPE_MAP_KUAIQIAN = new HashMap<String,String>();
		TRADE_TYPE_MAP_KUAIQIAN.put("S", "成功");
		TRADE_TYPE_MAP_KUAIQIAN.put("F", "失败");
		TRADE_TYPE_MAP_KUAIQIAN.put("P", "挂起");
	}
}
