package com.hongte.alms.base.enums;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IEnum;

/**
 * 合规划还款期数类型
 * @author huweiqian
 *
 */
public enum TdrepayRechargePeriodTypeEnums implements IEnum {
  
	NORMAL_REPAYMENT(0, "正常还款"),
	NORMAL_SETTLEMENT(10, "正常结清"),
	OVERDUE_REPAYMENT(11, "逾期结清"),
	EXTENSION_CONFIRM(20, "展期确认"),
	BAD_SETTLEMENT(30, "坏账结清"),
	;

    private int key;	// 键
    private String value;	// 值

    TdrepayRechargePeriodTypeEnums(final int key, final String value) {
        this.key = key;
        this.value = value;
    }

	@Override
	public Serializable getValue() {
		return this.value;
	}
	
	public int getKey() {
		return this.key;
	}
	
	/**
	 * 根据key获取value    
	 * @param key
	 * @return
	 */
	public static String getValueByKey(int key) {
		TdrepayRechargePeriodTypeEnums[] tdrepayRechargePeriodTypeEnums = values();
		for (TdrepayRechargePeriodTypeEnums typeEnums : tdrepayRechargePeriodTypeEnums) {
			if (key == typeEnums.key) {
				return typeEnums.value;
			}
		}
		return null;
	}

}
