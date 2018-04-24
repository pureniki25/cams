package com.hongte.alms.base.repayPlan.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.baseException.CreatRepaymentExcepiton;
import com.hongte.alms.base.entity.RepaymentProjPlan;
import com.hongte.alms.base.entity.RepaymentProjPlanList;
import com.hongte.alms.base.entity.RepaymentProjPlanListDetail;
import com.hongte.alms.base.enums.RepayPlanStatus;
import com.hongte.alms.base.repayPlan.enums.RepayPlanBorrowLimitUnitEnum;
import com.hongte.alms.base.repayPlan.enums.RepayPlanBorrowRateUnitEnum;
import com.hongte.alms.base.repayPlan.enums.RepayPlanActiveEnum;
import com.hongte.alms.base.repayPlan.enums.RepayPlanIsDeferEnum;
import com.hongte.alms.base.repayPlan.req.BizOutPutPlanReq;
import com.hongte.alms.base.repayPlan.req.BusinessBasicInfoReq;
import com.hongte.alms.base.repayPlan.req.CreatRepayPlanReq;
import com.hongte.alms.base.repayPlan.req.TuandaiProjInfoReq;
import com.hongte.alms.base.repayPlan.service.CreatRepayPlanService;
import com.hongte.alms.base.service.RepaymentProjPlanService;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author zengkun
 * @since 2018/4/24
 */
@Service("CreatRepayPlanService")
public class CreatRepayPlanServiceImpl  implements CreatRepayPlanService {


    private  static Logger logger = LoggerFactory.getLogger(CreatRepayPlanServiceImpl.class);


    @Autowired
    @Qualifier("RepaymentProjPlanService")
    RepaymentProjPlanService  repaymentProjPlanService;


    //需要判断是否重复传入
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean creatRepayPlan(CreatRepayPlanReq creatRepayPlanReq) {

        //1、根据业务和标的对应关系，判断出需要生成几个还款计划，每个还款计划相对于业务总金额占的比例
        // 1))业务基本信息
        BusinessBasicInfoReq  businessBasicInfo = creatRepayPlanReq.getBusinessBasicInfoReq();

        List<BizOutPutPlanReq> outPutPlanList  = creatRepayPlanReq.getOutPutPlanList();

        List<TuandaiProjInfoReq> tuandaiProjReqInfos = creatRepayPlanReq.getTuandaiProjReqInfos();

        //计算每个标 在占整个业务的比例
        Map<String,BigDecimal>  projPersent = new HashMap<>();
        for(TuandaiProjInfoReq projInfoReq:tuandaiProjReqInfos){
            BigDecimal persent = projInfoReq.getFullBorrowMoney().divide(businessBasicInfo.getBorrowMoney());
            projPersent.put(projInfoReq.getProjectId(),persent);
        }

        String bizPlanId = UUID.randomUUID().toString();
        List<RepaymentProjPlan> repaymentProjPlanList = new LinkedList<>();

        //标的planId对应的List列表
        Map<String,List<RepaymentProjPlanList>>  repaymentPlanListMap = new HashMap<>();

        //期数对应的标List列表
        //Map<期数，标还款计划费用详情列表>
        Map<Integer,List<RepaymentProjPlanList>>  repaymentPlanListPeriorMap = new HashMap<>();

        //期数 费用类型对应的标ListDetail列表
        //Map<期数，Map<费用类型fee_id,标还款计划费用详情列表>>
        Map<Integer,Map<String,List<RepaymentProjPlanListDetail>>>  RepaymentProjPlanListDetailPeriorMap = new HashMap<>();


        //标的还款计划List对应的detail列表
        Map<String,List<RepaymentProjPlanListDetail>> repaymentPlanListDetailMap = new HashMap<>();


        //计算每个标的还款计划列表
        for(TuandaiProjInfoReq projInfoReq:tuandaiProjReqInfos){
            List<RepaymentProjPlan> projList =  repaymentProjPlanService.selectList(new EntityWrapper<RepaymentProjPlan>().eq("project_id",projInfoReq.getProjectId()));

            if(projList.size()>0){
                for(RepaymentProjPlan projPlan:projList){
                    if(projPlan.getActive().equals(RepayPlanActiveEnum.ACTIVE.getValue())){
                        Integer diffDays = DateUtil.getDiffDays(projPlan.getCreateTime(),projInfoReq.getQueryFullsuccessDate());
                        //如果同一个标的满标时间与还款计划生成的时间相差一天以内
                        if(diffDays <= 1){
                            throw  new CreatRepaymentExcepiton("已存在时间相近的还款计划");
                        }
                    }

                }
            }

            ///////  标还款计划表   一次出款 生成一条记录
            RepaymentProjPlan repaymentProjPlan = new RepaymentProjPlan();
            repaymentProjPlan.setProjPlanId(UUID.randomUUID().toString());
            repaymentProjPlan.setProjectId(projInfoReq.getProjectId());
            repaymentProjPlan.setBusinessId(projInfoReq.getBusinessId());
            repaymentProjPlan.setOriginalBusinessId(projInfoReq.getBusinessId());
            repaymentProjPlan.setRepaymentBatchId("");  //还款计划批次号  需要与咏康核对 怎么写
            repaymentProjPlan.setPlanId(bizPlanId);   //业务还款计划ID
            repaymentProjPlan.setBorrowMoney(projInfoReq.getAmount());  //生成还款计划对应的借款总额
            repaymentProjPlan.setBorrowRate(projInfoReq.getInterestRate());  //生成还款计划对应的借款利率
            repaymentProjPlan.setBorrowRateUnit(RepayPlanBorrowRateUnitEnum.YEAR_RATE.getValue());//利率类型
            repaymentProjPlan.setBorrowLimit(projInfoReq.getBorrowLimit());//借款期限
            repaymentProjPlan.setBorrowLimitUnit(RepayPlanBorrowLimitUnitEnum.MONTH.getValue());//借款期限单位
            repaymentProjPlan.setPlanStatus(RepayPlanStatus.REPAYING.getKey());//还款计划状态
            repaymentProjPlan.setIsDefer(RepayPlanIsDeferEnum.NO.getValue());//是否展期还款计划
            repaymentProjPlan.setActive(RepayPlanActiveEnum.ACTIVE.getValue());//是否有效标志位
            repaymentProjPlan.setCreateTime(new Date());
            repaymentProjPlan.setCreateUser(Constant.SYS_DEFAULT_USER);

            repaymentProjPlanList.add(repaymentProjPlan);


            ///////  标的还款计划00期   一次性收取的费用信息
            RepaymentProjPlanList  zeroList = new RepaymentProjPlanList();
            zeroList.setProjPlanListId(UUID.randomUUID().toString());
            zeroList.setProjPlanId(repaymentProjPlan.getProjPlanId());
            zeroList.setPlanId(""); //所属还款计划编号(外键，对应tb_repayment_biz_plan.plan_id)
            zeroList.setBusinessId(repaymentProjPlan.getBusinessId());  //还款计划所属业务编号(若当前业务为展期，则存展期业务编号)
            zeroList.setOrigBusinessId(repaymentProjPlan.getOriginalBusinessId());  //还款计划所属原业务编号
            zeroList.setPeriod(0);  //还款计划期数
            zeroList.setAfterId(""); // 总批次期数，  核对原来信贷还款计划是怎么写的
            zeroList.setDueDate(new Date()); //应还日期 怎么设置， 需要核对
            zeroList.setTotalBorrowAmount(new BigDecimal(0));// 总计划应还金额   需要按照每一项计算
            zeroList.setOverdueAmount(new BigDecimal(0)); //总应还滞纳金
            zeroList.setOverdueDays(new BigDecimal(0)); //逾期天数
            zeroList.setCurrentStatus(RepayPlanStatus.REPAYED.getName()); //当前还款状态  00期的直接置位为已还款





;
//            /**
//             * 当前还款状态，目前只有三种，分别为"还款中"，"逾期"，"已还款"
//             */
//            @TableField("current_status")
//            @ApiModelProperty(required= true,value = "当前还款状态，目前只有三种，分别为‘还款中’，‘逾期’，‘已还款’")
//            private String currentStatus;
//            /**
//             * 已还款类型标记，null或0：还款中，6：申请展期已还款，10：线下确认已还款，20：自动线下代扣已还款，21，人工线下代扣已还款，30：自动银行代扣已还款，31：人工银行代扣已还款，40：用户APP主动还款，50：线下财务确认全部结清，60：线下代扣全部结清，70：银行代扣全部结清
//             */
//            @TableField("repay_flag")
//            @ApiModelProperty(required= true,value = "已还款类型标记，null或0：还款中，6：申请展期已还款，10：线下确认已还款，20：自动线下代扣已还款，21，人工线下代扣已还款，30：自动银行代扣已还款，31：人工银行代扣已还款，40：用户APP主动还款，50：线下财务确认全部结清，60：线下代扣全部结清，70：银行代扣全部结清")
//            private Integer repayFlag;
//            /**
//             * 客户实还日期
//             */
//            @TableField("fact_repay_date")
//            @ApiModelProperty(required= true,value = "客户实还日期")
//            private Date factRepayDate;
//            /**
//             * 财务确认还款操作日期
//             */
//            @TableField("finance_comfirm_date")
//            @ApiModelProperty(required= true,value = "财务确认还款操作日期")
//            private Date financeComfirmDate;
//            /**
//             * 财务还款确认人ID
//             */
//            @TableField("finance_confirm_user")
//            @ApiModelProperty(required= true,value = "财务还款确认人ID")
//            private String financeConfirmUser;
//            /**
//             * 财务还款确认人名称
//             */
//            @TableField("finance_confirm_user_name")
//            @ApiModelProperty(required= true,value = "财务还款确认人名称")
//            private String financeConfirmUserName;
//            /**
//             * 财务还款金额确认(1:已确认,0:未确认)
//             */
//            @TableField("confirm_flag")
//            @ApiModelProperty(required= true,value = "财务还款金额确认(1:已确认,0:未确认)")
//            private Integer confirmFlag;
//            /**
//             * 财务确认自动代扣日期
//             */
//            @TableField("auto_withholding_confirmed_date")
//            @ApiModelProperty(required= true,value = "财务确认自动代扣日期")
//            private Date autoWithholdingConfirmedDate;
//            /**
//             * 确认自动代扣的确认者ID
//             */
//            @TableField("auto_withholding_confirmed_user")
//            @ApiModelProperty(required= true,value = "确认自动代扣的确认者ID")
//            private String autoWithholdingConfirmedUser;
//            /**
//             * 确认自动代扣的确认者姓名
//             */
//            @TableField("auto_withholding_confirmed_user_name")
//            @ApiModelProperty(required= true,value = "确认自动代扣的确认者姓名")
//            private String autoWithholdingConfirmedUserName;
//            /**
//             * 会计确认状态，0或null:待审核;1:已审核;2:已退回;3:已返审核;4:导入;
//             */
//            @TableField("accountant_confirm_status")
//            @ApiModelProperty(required= true,value = "会计确认状态，0或null:待审核;1:已审核;2:已退回;3:已返审核;4:导入;")
//            private Integer accountantConfirmStatus;
//            /**
//             * 会计确认人ID
//             */
//            @TableField("accountant_confirm_user")
//            @ApiModelProperty(required= true,value = "会计确认人ID")
//            private String accountantConfirmUser;
//            /**
//             * 会计确认人姓名
//             */
//            @TableField("accountant_confirm_user_name")
//            @ApiModelProperty(required= true,value = "会计确认人姓名")
//            private String accountantConfirmUserName;
//            /**
//             * 会计确认日期
//             */
//            @TableField("accountant_confirm_date")
//            @ApiModelProperty(required= true,value = "会计确认日期")
//            private Date accountantConfirmDate;
//            /**
//             * 标的资金端(平台)应还日期
//             */
//            @TableField("proj_due_date")
//            @ApiModelProperty(required= true,value = "标的资金端(平台)应还日期")
//            private Date projDueDate;
//            /**
//             * 标的当前期资产端总应还金额(元)，不含滞纳金
//             */
//            @TableField("biz_amount")
//            @ApiModelProperty(required= true,value = "标的当前期资产端总应还金额(元)，不含滞纳金")
//            private BigDecimal bizAmount;
//            /**
//             * 标的当前期资产端总应还滞纳金(元)，每天零点由系统自动计算
//             */
//            @TableField("biz_overdue_amount")
//            @ApiModelProperty(required= true,value = "标的当前期资产端总应还滞纳金(元)，每天零点由系统自动计算")
//            private BigDecimal bizOverdueAmount;
//            /**
//             * 标的当前期资金端总应还金额(元)，不含滞纳金
//             */
//            @TableField("proj_amount")
//            @ApiModelProperty(required= true,value = "标的当前期资金端总应还金额(元)，不含滞纳金")
//            private BigDecimal projAmount;
//            /**
//             * 标的当前期资金端总应还滞纳金(元)
//             */
//            @TableField("proj_overdue_amount")
//            @ApiModelProperty(required= true,value = "标的当前期资金端总应还滞纳金(元)")
//            private BigDecimal projOverdueAmount;
//            /**
//             * 还款备注
//             */
//            @ApiModelProperty(required= true,value = "还款备注")
//            private String remark;
//
//            /**
//             * 是否有效状态：1 有效 ，0 无效
//             */
//            @ApiModelProperty(required= true,value = "是否有效状态：1 有效 ，0 无效")
//            private Integer active;
//
//            /**
//             * 创建日期
//             */
//            @TableField("create_time")
//            @ApiModelProperty(required= true,value = "创建日期")
//            private Date createTime;
//            /**
//             * 创建用户
//             */
//            @TableField("create_user")
//            @ApiModelProperty(required= true,value = "创建用户")
//            private String createUser;
//            /**
//             * 更新日期
//             */
//            @TableField("update_time")
//            @ApiModelProperty(required= true,value = "更新日期")
//            private Date updateTime;
//            /**
//             * 更新用户
//             */
//            @TableField("update_user")
//            @ApiModelProperty(required= true,value = "更新用户")
//            private String updateUser;




        }







        return null;
    }

    public static void main(String[] args) {

        BigDecimal big1 = new BigDecimal(100);
        BigDecimal big2 = new BigDecimal(50);
        System.out.println(big2.divide(big1));
    }
}
