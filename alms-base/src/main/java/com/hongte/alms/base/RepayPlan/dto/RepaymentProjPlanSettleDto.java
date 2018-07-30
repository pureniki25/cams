package com.hongte.alms.base.RepayPlan.dto;

import com.hongte.alms.base.entity.RepaymentProjPlanListDetail;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zengkun
 * @since 2018/7/26
 * 标的还款计划主表结清Dto
 */
@Data
public class RepaymentProjPlanSettleDto extends  RepaymentProjPlanDto {

    // =========   结清使用的列表  开始  ==============
    /**
     * 当前期业务还款计划
     */
    private RepaymentProjPlanListDto  currProjPlanListDto;

    /**
     * 当前期应还费用项Map  Map<feeId,PlanListDetailShowPayDto>
     */
    private Map<String,RepaymentProjPlanListDetail>  curProjListDetailMap;

    /**
     * 当前期结清应还费用项Map  Map<feeId,PlanListDetailShowPayDto>
     */
    private Map<String,PlanListDetailShowPayDto> curShowPayFeels;

    /**
     * 当前期之前的业务还款计划列表
     */
    private List<RepaymentProjPlanListDto> beforeProjPlanListDtos;

    /**
     * 当前期之前的费用统计
     */
    private Map<String,PlanListDetailShowPayDto> beforeFeels;


    /**
     * 当前期之后的业务还款计划列表
     */
    private List<RepaymentProjPlanListDto>  afterProjPlanListDtos;


    /**
     * 当前期之后的费用统计
     */
    private Map<String,PlanListDetailShowPayDto> afterFeels;

    // =========   结清使用的列表  结束  ==============


    public void setProjPlanListDtos(RepaymentProjPlanListDto projPlanListDto) {
        if (CollectionUtils.isEmpty(projPlanListDtos)) {
            projPlanListDtos = new ArrayList<>() ;
        }
        this.projPlanListDtos.add(projPlanListDto);
    }

}
