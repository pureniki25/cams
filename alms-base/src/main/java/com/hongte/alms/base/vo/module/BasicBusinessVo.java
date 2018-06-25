package com.hongte.alms.base.vo.module;

import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.common.util.ClassCopyUtil;

import java.util.List;

/**
 * @author zengkun
 * @since 2018/2/1
 */
public class BasicBusinessVo extends BasicBusiness{

    //还款类型名称
    private  String repaymentTypeName;

    //借款期限字符串
    private String borrowLimitStr;

    //贷后跟踪记录的状态
    private List<SysParameter> collectionTackLogStatu;

    //还款状态：还款中，已结清
    private String status;





    public static BasicBusinessVo creatByClone(BasicBusiness business){

        BasicBusinessVo vo=new BasicBusinessVo();
        ClassCopyUtil.fatherToChild(business, vo);
        return vo;

    }



    public String getRepaymentTypeName() {
        return repaymentTypeName;
    }

    public void setRepaymentTypeName(String repaymentTypeName) {
        this.repaymentTypeName = repaymentTypeName;
    }

    public String getBorrowLimitStr() {
        return borrowLimitStr;
    }

    public void setBorrowLimitStr(String borrowLimitStr) {
        this.borrowLimitStr = borrowLimitStr;
    }

    public List<SysParameter> getCollectionTackLogStatu() {
        return collectionTackLogStatu;
    }

    public void setCollectionTackLogStatu(List<SysParameter> collectionTackLogStatu) {
        this.collectionTackLogStatu = collectionTackLogStatu;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
