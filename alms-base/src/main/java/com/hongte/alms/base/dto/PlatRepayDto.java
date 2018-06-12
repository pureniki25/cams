package com.hongte.alms.base.dto;

import com.hongte.alms.base.entity.TdrepayRechargeDetail;
import com.hongte.alms.base.entity.TdrepayRechargeLog;

import java.util.List;

/**
 * @author zengkun
 * @since 2018/6/12
 * 合规化还款Dto
 */
public class PlatRepayDto extends TdrepayRechargeLog{

        private static final long serialVersionUID = 1L;

        /**
         * 团贷网合规化还款标的充值明细表
         */
        private List<TdrepayRechargeDetail> detailList;

        public List<TdrepayRechargeDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<TdrepayRechargeDetail> detailList) {
        this.detailList = detailList;
    }

    @Override
    public String toString() {
        return "TdrepayRechargeInfoVO [detailList=" + detailList + "]";
    }

}

