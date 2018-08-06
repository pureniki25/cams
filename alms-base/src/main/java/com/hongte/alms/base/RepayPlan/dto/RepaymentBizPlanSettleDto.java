package com.hongte.alms.base.RepayPlan.dto;

import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zengkun
 * @since 2018/7/26
 * 业务还款计划结清对象Dto
 */
@Data
public class RepaymentBizPlanSettleDto extends  RepaymentBizPlanDto {

    // =========   结清使用的列表  开始  ==============
    /**
     * 当前期业务还款计划
     */
    private RepaymentBizPlanListDto  currBizPlanListDto;

    /**
     * 当前期之前的业务还款计划列表
     */
    private List<RepaymentBizPlanListDto> beforeBizPlanListDtos;


    /**
     * 当前期之后的业务还款计划列表
     */
    private List<RepaymentBizPlanListDto>  afterBizPlanListDtos;

    /**
     * 标的还款计划结清Dto列表
     */
    private  List<RepaymentProjPlanSettleDto> projPlanStteleDtos;

    // =========   结清使用的列表  结束  ==============

    public void setProjPlanStteleDtos(RepaymentProjPlanSettleDto projPlanDto) {
        if (CollectionUtils.isEmpty(projPlanStteleDtos)) {
            projPlanStteleDtos = new ArrayList<>() ;
        }
        this.projPlanStteleDtos.add(projPlanDto);
    }
}
