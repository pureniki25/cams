package com.hongte.alms.base.vo.withhold;


/**
 * APP代扣返回对象<br>
 * 
 * @author czs
 * @date 2018年7月5日 下午3:40:50
 */
public class Data {
  private Integer type;//1:成功 2:处理中 3:失败
  private String msg;
public Integer getType() {
	return type;
}
public void setType(Integer type) {
	this.type = type;
}
public String getMsg() {
	return msg;
}
public void setMsg(String msg) {
	this.msg = msg;
}
  
  

}
