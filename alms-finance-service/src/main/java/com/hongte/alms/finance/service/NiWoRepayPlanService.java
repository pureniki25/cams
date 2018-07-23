package com.hongte.alms.finance.service;

import java.util.HashMap;


/**
 * @author 陈泽圣2018年6月13日 
 */
public interface NiWoRepayPlanService {


	/**
	 * 根据请求编号获取标的信息
	 * 
	 * @author 陈泽圣 2018年6月13日
	 * @return
	 */
	public void sycNiWoRepayPlan(String orderNo,HashMap<String,Object> niwoMap);


	/**
	 * 定时查询你我金融的还款计划
	 * 
	 * @author 陈泽圣 2018年6月19日
	 * @return
	 */
	public void SearchNiWoRepayPlan();
	
	
	
	/**若你我金融还款日当日5pm没有返回更新的还款计划，则触发扣款失败提示；
	 * 
	 */
	
	public void calFailMsg();
	
	
	
	/**还款提醒，提前7天/1天提醒
	 * 
	 */
	
	public void sendRepayRemindMsg(Integer days);
	
	
	
	/**结清提醒，提前15天/1天提醒
	 * 
	 */
	
	public void sendSettleRemindMsg(Integer days);
	

	/**逾期提醒，逾期提醒
	 * 
	 */
	
	public void sendOverDueRemindMsg(Integer days);

	
}
