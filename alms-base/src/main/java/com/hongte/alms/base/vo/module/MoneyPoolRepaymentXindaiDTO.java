package com.hongte.alms.base.vo.module;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import com.hongte.alms.base.entity.MoneyPool;
import com.hongte.alms.base.entity.MoneyPoolRepayment;

public class MoneyPoolRepaymentXindaiDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4835127117436737348L;
	private Integer	id	;//	[编号]
	private Integer	moneyPoolId	;//	[款项池ID]
	private String	business_id	;//	业务ID
	private String	afterbusiness_id	;//	贷后业务id
	private String	operate_id	;//	领取人用户名
	private String	operate_name	;//	领取人真实姓名
	private String	claimDate	;//	领取时间
	private String	state	;//	状态
	private String	reserve_1	;//	还款金额
	private Integer	income_type	;//	收入类型（1：收入；2：支出）
	private String	reserve_2	;//	转入账号
	private String	reserve_3	;//	交易类型
	private String	reserve_4	;//	交易场所
	private String	reserve_5	;//	实际转款人
	private BigDecimal	late_cost	;//	逾期费用
	private BigDecimal	other_cost	;//	其它费用
	private String	remark	;//	备注
	private String	pool_list	;//	银行流水列表
	private String	certificate_picture_url	;//	凭证图片地址
	private String	create_user	;//	创建人
	private String	create_time	;//	创建时间
	private String	update_user	;//	更新人
	private String	update_time	;//	更新时间
	private String	alms_pool_id	;//	贷后系统贷款项池ID
	public MoneyPoolRepaymentXindaiDTO() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMoneyPoolId() {
		return moneyPoolId;
	}
	public void setMoneyPoolId(Integer moneyPoolId) {
		this.moneyPoolId = moneyPoolId;
	}
	public String getBusiness_id() {
		return business_id;
	}
	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}
	public String getAfterbusiness_id() {
		return afterbusiness_id;
	}
	public void setAfterbusiness_id(String afterbusiness_id) {
		this.afterbusiness_id = afterbusiness_id;
	}
	public String getOperate_id() {
		return operate_id;
	}
	public void setOperate_id(String operate_id) {
		this.operate_id = operate_id;
	}
	public String getOperate_name() {
		return operate_name;
	}
	public void setOperate_name(String operate_name) {
		this.operate_name = operate_name;
	}
	public String getClaimDate() {
		return claimDate;
	}
	public void setClaimDate(String claimDate) {
		this.claimDate = claimDate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getReserve_1() {
		return reserve_1;
	}
	public void setReserve_1(String reserve_1) {
		this.reserve_1 = reserve_1;
	}
	public Integer getIncome_type() {
		return income_type;
	}
	public void setIncome_type(Integer income_type) {
		this.income_type = income_type;
	}
	public String getReserve_2() {
		return reserve_2;
	}
	public void setReserve_2(String reserve_2) {
		this.reserve_2 = reserve_2;
	}
	public String getReserve_3() {
		return reserve_3;
	}
	public void setReserve_3(String reserve_3) {
		this.reserve_3 = reserve_3;
	}
	public String getReserve_4() {
		return reserve_4;
	}
	public void setReserve_4(String reserve_4) {
		this.reserve_4 = reserve_4;
	}
	public String getReserve_5() {
		return reserve_5;
	}
	public void setReserve_5(String reserve_5) {
		this.reserve_5 = reserve_5;
	}
	public BigDecimal getLate_cost() {
		return late_cost;
	}
	public void setLate_cost(BigDecimal late_cost) {
		this.late_cost = late_cost;
	}
	public BigDecimal getOther_cost() {
		return other_cost;
	}
	public void setOther_cost(BigDecimal other_cost) {
		this.other_cost = other_cost;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPool_list() {
		return pool_list;
	}
	public void setPool_list(String pool_list) {
		this.pool_list = pool_list;
	}
	public String getCertificate_picture_url() {
		return certificate_picture_url;
	}
	public void setCertificate_picture_url(String certificate_picture_url) {
		this.certificate_picture_url = certificate_picture_url;
	}
	public String getCreate_user() {
		return create_user;
	}
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getUpdate_user() {
		return update_user;
	}
	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getAlms_pool_id() {
		return alms_pool_id;
	}
	public void setAlms_pool_id(String alms_pool_id) {
		this.alms_pool_id = alms_pool_id;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public MoneyPoolRepaymentXindaiDTO(MoneyPoolRepayment repayment,String businessId,String afterId) {
		super();
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.id = repayment.getXdMatchingId()==null?null:repayment.getXdMatchingId();
		//update by liuzq for 修改moneyPoolId默认值为0
		this.moneyPoolId = repayment.getXdPoolId()==null?0:repayment.getXdPoolId();
		this.business_id = businessId;
		this.afterbusiness_id = afterId;
		this.operate_id = repayment.getOperateId();
		this.operate_name = repayment.getOperateName();
		this.claimDate = dateTimeFormat.format(repayment.getTradeDate());
		this.state = repayment.getState();
		this.reserve_1 = repayment.getAccountMoney() == null ? null : repayment.getAccountMoney().toString();
		this.income_type = repayment.getIncomeType();
		this.reserve_2 = repayment.getBankAccount();
		this.reserve_3 = repayment.getTradeType();
		this.reserve_4 = repayment.getTradePlace();
		this.reserve_5 = repayment.getFactTransferName();
		this.late_cost = null;
		this.other_cost = null;
		this.remark = repayment.getRemark();
		this.pool_list = repayment.getPoolList();
		this.certificate_picture_url = repayment.getCertificatePictureUrl();
		this.create_user = repayment.getCreateUser();
		this.create_time = repayment.getCreateTime() == null ? null : dateTimeFormat.format(repayment.getCreateTime());
		this.update_user = repayment.getUpdateUser();
		this.update_time = repayment.getUpdateTime() == null ? null : dateTimeFormat.format(repayment.getUpdateTime());
		this.alms_pool_id = repayment.getMoneyPoolId();
	}
	

}
