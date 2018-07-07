/**
 * 
 */
package com.hongte.alms.base.enums.repayPlan;

/**
 * @author 王继光
 * 2018年5月25日 下午9:18:38
 */
public enum SectionRepayStatusEnum {
	SECTION_REPAID(1,"部分已还款"),
	ONLINE_REPAID(2,"线上已还款"),
	ALL_REPAID(3,"全部已还款");
	
	private Integer key ;
	private String name ;
	private SectionRepayStatusEnum(int key,String name) {
		this.key = key ;
		this.name= name;
	}
	public Integer getKey() {
		return key;
	}
	public String getName() {
		return name;
	}
	
	public static String getName(int value) {  
		SectionRepayStatusEnum[] sectionRepayStatusEnums = values();  
        for (SectionRepayStatusEnum sectionRepayStatusEnum : sectionRepayStatusEnums) {  
            if (sectionRepayStatusEnum.getKey().intValue() == value) {  
                return sectionRepayStatusEnum.getName();  
            }  
        }  
        return null;  
    } 
}
