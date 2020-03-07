/**
 * 
 */
package com.hongte.alms.base.vo.finance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @author 陈泽圣 2018年11月19日 下午8:42:42
 */
public class ApplyDerateDetailVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean master;
	private String userName;
	private String feeId;
	private String projectId;
	private BigDecimal underOverdueAmount=new BigDecimal(0);
	private BigDecimal onOverdueAmount=new BigDecimal(0);
	//上标金额
	private BigDecimal projAmount = new BigDecimal("0");
	//费用项名称
	private String itemName;
	//费用项应还金额
	private BigDecimal itemAmount=new BigDecimal("0");

	//费用项减免金额
	private BigDecimal itemDerateAmount=new BigDecimal("0");
	
	//费用项减免后应还金额
     private BigDecimal itemRepayAmount=new BigDecimal("0");
     private Date queryFullSuccessDate ;
     public ApplyDerateDetailVO(boolean master,String userName,BigDecimal projAmount,String itemName,BigDecimal itemAmount,BigDecimal itemDerateAmount,BigDecimal itemRepayAmount) {
    	 this.master=master;
    	 this.userName=userName;
    	 this.projAmount=projAmount;
    	 this.itemName=itemName;
    	 this.itemAmount=itemAmount;
    	 this.itemDerateAmount=itemDerateAmount;
    	 this.itemRepayAmount=itemRepayAmount;
     }

	public ApplyDerateDetailVO(){

	}

	public BigDecimal getUnderOverdueAmount() {
		return underOverdueAmount;
	}





	public void setUnderOverdueAmount(BigDecimal underOverdueAmount) {
		this.underOverdueAmount = underOverdueAmount;
	}





	public BigDecimal getOnOverdueAmount() {
		return onOverdueAmount;
	}





	public void setOnOverdueAmount(BigDecimal onOverdueAmount) {
		this.onOverdueAmount = onOverdueAmount;
	}





	public String getProjectId() {
		return projectId;
	}


	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public BigDecimal getProjAmount() {
		return projAmount;
	}

	public void setProjAmount(BigDecimal projAmount) {
		this.projAmount = projAmount;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public BigDecimal getItemAmount() {
		return itemAmount;
	}

	public void setItemAmount(BigDecimal itemAmount) {
		this.itemAmount = itemAmount;
	}

	public BigDecimal getItemDerateAmount() {
		return itemDerateAmount;
	}

	public void setItemDerateAmount(BigDecimal itemDerateAmount) {
		this.itemDerateAmount = itemDerateAmount;
	}
	/**
	 * @return the queryFullSuccessDate
	 */
	public Date getQueryFullSuccessDate() {
		return queryFullSuccessDate;
	}

	/**
	 * @param queryFullSuccessDate the queryFullSuccessDate to set
	 */
	public void setQueryFullSuccessDate(Date queryFullSuccessDate) {
		this.queryFullSuccessDate = queryFullSuccessDate;
	}
	
	
	public String getFeeId() {
		return feeId;
	}
	public void setFeeId(String feeId) {
		this.feeId = feeId;
	}
	public BigDecimal getItemRepayAmount() {
		itemRepayAmount = new BigDecimal("0");
		itemRepayAmount = itemRepayAmount.add(getItemAmount() == null ? new BigDecimal("0") : getItemAmount())
				.subtract(getItemDerateAmount() == null ? new BigDecimal("0") : getItemDerateAmount());
			
		return itemRepayAmount;
	}

	public void setItemRepayAmount(BigDecimal itemRepayAmount) {
		this.itemRepayAmount = itemRepayAmount;
	}



	public boolean isMaster() {
		return master;
	}

	public void setMaster(boolean master) {
		this.master = master;
	}


	
	/**
	 * 将核销完的标的实还信息排序
	 * @author 王继光
	 * 2018年7月26日 下午3:54:25
	 * @param detailVOs
	 */
	public static void sort(List<ApplyDerateDetailVO> detailVOs) {
		Collections.sort(detailVOs, new Comparator<ApplyDerateDetailVO>() {
            // 排序规则说明 需补充 从小标到大标，再到主借标
            //同等
            @Override
            public int compare(ApplyDerateDetailVO arg0, ApplyDerateDetailVO arg1) {
                if (arg0.isMaster()) {
                    return 1;
                }else if (arg1.isMaster()) {
                    return -1;
				}
                if (arg0.getProjAmount()
                        .compareTo(arg1.getProjAmount()) < 0) {
                    return -1;
                }else if (arg0.getProjAmount().compareTo(arg1.getProjAmount())>0) {
					return 1;
				}
                if (arg0.getQueryFullSuccessDate()
                        .before(arg1.getQueryFullSuccessDate())) {
                    return -1;
                }else if (arg0.getQueryFullSuccessDate()
                        .after(arg1.getQueryFullSuccessDate())) {
					return 1;
				}
                return 0;
            }

        });
	}

	
}
