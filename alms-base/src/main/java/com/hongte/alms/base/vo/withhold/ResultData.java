package com.hongte.alms.base.vo.withhold;


/**
 * 代扣返回对象<br>
 * 
 * @author czs
 * @date 2018年7月31日 下午3:40:50
 */
public class ResultData {
  private String resultMsg;
  private String status;//1 充值失败；2 充值成功；3 待付款

public String getResultMsg() {
	return resultMsg;
}
public void setResultMsg(String resultMsg) {
	this.resultMsg = resultMsg;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}

  
  

}
