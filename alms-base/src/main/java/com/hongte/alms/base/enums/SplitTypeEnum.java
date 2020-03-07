package com.hongte.alms.base.enums;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SplitTypeEnum implements IEnum {

	EXCEPTION(-1,"异常"),
	Split_Type_0(0, "初始状态"),
	Split_Type_1(1, "上标金额<>借款金额"),
	Split_Type_2(2, "检查数据完整性-还款计划有明细失败"),
	Split_Type_3(3, "检查数据完整性-还款计划明细有详情失败"),
	Split_Type_4(4, "不是信贷系统生成的还款计划"),
	Split_Type_51(51, "还款计划所属业务没有一个标(线下出款)"),
	Split_Type_52(52, "还款计划所属业务只有一个标(单标)"),
	Split_Type_6(6, "已经结清或者已经展期"),
	Split_Type_7(7, "已经存在标的还款计划"),
	Split_Type_10(10, "还款计划所属业务存在多个标的(多标)"),
	
	NO_BIZPLANLIST(11,"无还款计划list"),
	NO_AFTERID(12,"还款计划无afterid"),
	NO_BIZPLAN(13,"无还款计划plan"),
	NO_BIZPLANLISTDETAIL(14,"detail应还==null"),
	FACTAMOUNT_LT_PLANAMOUNT(15,"实还大于应还"),
	NO_SHAREPROFIT(16,"分润顺序==null"),
	
	Split_Type_17(17, "整个还款计划没还完，实还大于应还"),
	Split_Type_18(18, "整个还款计划没还完，实还小于应还"),
	Split_Type_19(19, "找不到主借款人"),
	Split_Type_20(20, "业务类型(`tb_basic_business`>business_ctype)为空"),
	
	Split_Type_21(21,"detail应还==null"),
	Split_Type_22(22,"实还大于应还"),
	Split_Type_23(23,"分润顺序==null"),
	Split_Type_24(24,"feeid==null"),
	Split_Type_25(25,"本金feeid异常"),
	Split_Type_26(26,"利息feeid异常"),
	Split_Type_27(27,"资产端分公司服务费feeid异常"),
	Split_Type_28(28,"资金端平台服务费feeid异常"),
	Split_Type_29(29,"滞纳金feeid异常"),
	Split_Type_30(30,"非线上费用分润顺序小于1200"),
	Split_Type_31(31,"只有1种分润顺序"),
	Split_Type_32(32,"分润顺序少于4种"),
	
	Split_Type_33(33,"还款状态异常"),
	;

	private SplitTypeEnum(final int value, final String name) {
		this.value = value;
		this.name = name;
	}

	private final int value;
	private final String name;

	@Override
	public Serializable getValue() {
		return this.value;
	}

	public int value() {
		return this.value;
	}
	
	@JsonValue
    public String getName(){
        return this.name;
    }

	public static String getName(int value) {
		SplitTypeEnum[] repaySourceEnums = values();
		for (SplitTypeEnum repaySourceEnum : repaySourceEnums) {
			if (repaySourceEnum.value() == value) {
				return repaySourceEnum.getName();
			}
		}
		return null;
	}

}
