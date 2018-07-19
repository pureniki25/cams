package com.hongte.alms.base.enums.repayPlan;

import com.baomidou.mybatisplus.enums.IEnum;

/**
 * @author czs
 * @since 2018/6/9
 * 宝付代扣返回代码枚举
 */
public enum RepayResultCodeEnum implements IEnum{


    //交易成功类：
	BF0000("0000","交易成功"),
	BF00114("EIP_BAOFU_BF00114","订单已支付成功，请勿重复支付"),
	//交易结果暂未知，需查询类
	BF00100("EIP_BAOFU_BF00100","系统异常，请联系宝付"),
	BF00112("EIP_BAOFU_BF00112","系统繁忙，请稍后再试"),
	BF00113("EIP_BAOFU_BF00113","交易结果未知，请稍后查询"),
	BF00115("EIP_BAOFU_BF00115","交易处理中，请稍后查询"),
	BF00144("EIP_BAOFU_BF00144","该交易有风险,订单处理中"),
	BF00202("EIP_BAOFU_BF00202","交易超时，请稍后查询"),
	//特别注意：失败错误编码为HANDLER_EXECEPTION需要通过查询接口来同步订单状态，其他的错误则直接表示订单处理失败，无需查询。
	YH_HANDLER_EXCEPTION("EIP_TD_HANDLER_EXECEPTION","订单处理中"),
	YH_HANDLER_TIMEOU("EIP_TD_SOCKET_TIMEOU","超时"),

	
	//易宝代扣
	YB600339("600339","商户可用余额不足"),
	YB600043("600043","单卡超过当日累计支付限额"),
	YB600045("600045","单卡超过单笔支付限额");
	private String value; // 数据保存的值
	private String name; // 名称

	private RepayResultCodeEnum(String value, String name) {
		this.name = name;
		this.value = value;
	}


	public String getValue() {
		return value;
	}



	public void setValue(String value) {
		this.value = value;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
