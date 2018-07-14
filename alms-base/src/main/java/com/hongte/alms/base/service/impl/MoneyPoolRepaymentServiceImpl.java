package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.collection.vo.CollectionTrackLogVo;
import com.hongte.alms.base.customer.vo.CustomerRepayFlowDto;
import com.hongte.alms.base.customer.vo.CustomerRepayFlowExel;
import com.hongte.alms.base.customer.vo.CustomerRepayFlowListReq;
import com.hongte.alms.base.entity.MoneyPool;
import com.hongte.alms.base.entity.MoneyPoolRepayment;
import com.hongte.alms.base.enums.RepayRegisterFinanceStatus;
import com.hongte.alms.base.enums.RepayRegisterState;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.mapper.MoneyPoolMapper;
import com.hongte.alms.base.mapper.MoneyPoolRepaymentMapper;
import com.hongte.alms.base.service.MoneyPoolRepaymentService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 款项池业务关联表 服务实现类
 * </p>
 *
 * @author 王继光
 * @since 2018-03-23
 */
@Service("MoneyPoolRepaymentService")
public class MoneyPoolRepaymentServiceImpl extends BaseServiceImpl<MoneyPoolRepaymentMapper, MoneyPoolRepayment> implements MoneyPoolRepaymentService {

    @Autowired
    private MoneyPoolMapper moneyPoolMapper;
    @Autowired
    private MoneyPoolRepaymentMapper moneyPoolRepaymentMapper;

    @Value("${oss.readUrl}")
    private String ossUrl;

    @Override
    @Transactional(rollbackFor = ServiceRuntimeException.class)
    public boolean deleteFinanceAddStatement(String mprId) {
        if (mprId == null) {
            throw new ServiceRuntimeException("mprid 不能为空");
        }
        MoneyPoolRepayment moneyPoolRepayment = moneyPoolRepaymentMapper.selectById(mprId);
        if (moneyPoolRepayment == null) {
            throw new ServiceRuntimeException("找不到此还款登记");
        }
        if (StringUtil.isEmpty(moneyPoolRepayment.getMoneyPoolId())) {
            throw new ServiceRuntimeException("此还款登记没关联银行流水");
        }
        MoneyPool moneyPool = moneyPoolMapper.selectById(moneyPoolRepayment.getMoneyPoolId());
        if (moneyPool.getStatus().equals(RepayRegisterState.完成.toString())) {
            throw new ServiceRuntimeException("已完成的银行流水不能被删除");
        }
        boolean updateMP = moneyPool.deleteById();
        boolean updateMPR = moneyPoolRepayment.deleteById();
        if (updateMP && updateMPR) {
            return true;
        }
        throw new ServiceRuntimeException("更新数据库失败");
    }

    @Override
    public List<CustomerRepayFlowExel> getCustomerRepayFlowList(CustomerRepayFlowListReq customerRepayFlowListReq) {
        String businessId = customerRepayFlowListReq.getBusinessId();
        if (!StringUtil.isEmpty(businessId)) {
            customerRepayFlowListReq.setBusinessId("%" + businessId + "%");
        }

        String customerName = customerRepayFlowListReq.getCustomerName();
        if (!StringUtil.isEmpty(customerName)) {
            customerRepayFlowListReq.setCustomerName("%" + customerName + "%");
        }
        // 1 未审核 2 已审核 3 拒绝
        //未审核_未关联银行流水", "审核_财务确认已还款", "拒绝_还款登记被财务拒绝
        String state=customerRepayFlowListReq.getState();
        if(!StringUtil.isEmpty(state)){
            if("1".equals(state)){
                customerRepayFlowListReq.setState(RepayRegisterFinanceStatus.未关联银行流水.toString());
            }else if("2".equals(state)){
                customerRepayFlowListReq.setState(RepayRegisterFinanceStatus.财务确认已还款.toString());
            }else if("3".equals(state)){
                customerRepayFlowListReq.setState(RepayRegisterFinanceStatus.还款登记被财务拒绝.toString());
            }
        }
        return moneyPoolRepaymentMapper.getCustomerRepayFlowList(customerRepayFlowListReq);

    }

    @Override
    public Page<CustomerRepayFlowDto> getCustomerRepayFlowPageList(CustomerRepayFlowListReq customerRepayFlowListReq) {
        Page<CustomerRepayFlowDto> page = new Page<>();
        String businessId = customerRepayFlowListReq.getBusinessId();
        if (!StringUtil.isEmpty(businessId)) {
            customerRepayFlowListReq.setBusinessId("%" + businessId + "%");
        }

        String customerName = customerRepayFlowListReq.getCustomerName();
        if (!StringUtil.isEmpty(customerName)) {
            customerRepayFlowListReq.setCustomerName("%" + customerName + "%");
        }


         // 1 未审核 2 已审核 3 拒绝
        //未审核_未关联银行流水", "审核_财务确认已还款", "拒绝_还款登记被财务拒绝
        String state=customerRepayFlowListReq.getState();
        if(!StringUtil.isEmpty(state)){
             if("1".equals(state)){
                 customerRepayFlowListReq.setState(RepayRegisterFinanceStatus.未关联银行流水.toString());
             }else if("2".equals(state)){
                 customerRepayFlowListReq.setState(RepayRegisterFinanceStatus.财务确认已还款.toString());
             }else if("3".equals(state)){
                 customerRepayFlowListReq.setState(RepayRegisterFinanceStatus.还款登记被财务拒绝.toString());
             }
        }
        List<CustomerRepayFlowDto> repayFlowList = moneyPoolRepaymentMapper.getCustomerRepayFlowPageList(customerRepayFlowListReq);

        if(!CollectionUtils.isEmpty(repayFlowList)){
            for(CustomerRepayFlowDto customerRepayFlowDto : repayFlowList){
                if(!StringUtil.isEmpty(customerRepayFlowDto.getCertificatePictureUrl())){
                    String url=customerRepayFlowDto.getCertificatePictureUrl();
                    customerRepayFlowDto.setCertificatePictureUrl(ossUrl+url);
                }
            }
        }
        int count = moneyPoolRepaymentMapper.countCustomerRepayFlowList(customerRepayFlowListReq);
        page.setTotal(count);
        page.setRecords(repayFlowList);
        return page;
    }

}
