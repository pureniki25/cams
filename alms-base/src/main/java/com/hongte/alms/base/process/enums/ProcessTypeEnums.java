package com.hongte.alms.base.process.enums;

/**
 * @author zengkun
 * @since 2018/3/1
 * 流程类型枚举
 */
public enum ProcessTypeEnums {
	Aply_CarAuction("carAuctionAply","车辆拍卖申请",200,400, "b138a92f-f796-11e7-94ed-94c69109b343"),
	Aply_ConvBus("convBusAply","转公车申请",200,600, null),
	Apply_Derate("derate","减免流程",100,400, "b138a92f-f796-11e7-94ed-94c69109b34a"),
	CAR_LOAN_LITIGATION("carLoanLitigation", "车贷移交诉讼审批流程", 0, 500, "f04424b9-5057-4a19-8d9c-dd93711f4c44"), 
	HOUSE_LOAN_LITIGATION("houseLoanLitigation", "房贷移交诉讼审批流程", 0, 500, "4e3adfc9-f6a3-4f9c-bd42-225cc23396b5");
	

//	(0:运行中,1:开始,2:结束,3:注销)

	private String key; // 数据保存的值
	private String name; // 名称
	private Integer beginStep;//起始节点步骤
	private Integer endStep;//结束节点步骤
	private String typeId; // 流程类别ID

	private ProcessTypeEnums(String key, String name,Integer beginStep, Integer endStep, String typeId) {
		this.name = name;
		this.key = key;
		this.beginStep =beginStep;
		this.endStep = endStep;
		this.typeId = typeId;
	}


	public static String nameOf(Integer key){
		for(ProcessTypeEnums d : ProcessTypeEnums.values()){
			if(d.key.equals(key)){
				return d.name;
			}
		}
		return null;

	}

	public static ProcessTypeEnums getByKey(String key){
		for(ProcessTypeEnums d : ProcessTypeEnums.values()){
			if(d.key.equals(key)){
				return d;
			}
		}
		return null;

	}

	public static String keyOf(String name){
		for(ProcessTypeEnums d : ProcessTypeEnums.values()){
			if(d.name.equals(name)){
				return d.key;
			}
		}
		return null;

	}


	public String getName() {
		return name;
	}



	public String getKey() {
		return key;
	}




	public Integer getBeginStep() {
		return beginStep;
	}



	public Integer getEndStep() {
		return endStep;
	}


	public String getTypeId() {
		return typeId;
	}

}
