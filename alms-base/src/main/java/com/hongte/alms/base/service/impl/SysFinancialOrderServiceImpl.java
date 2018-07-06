package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.entity.SysFinancialOrder;
import com.hongte.alms.base.entity.SysFinancialOrderUser;
import com.hongte.alms.base.enums.AreaLevel;
import com.hongte.alms.base.mapper.SysFinancialOrderMapper;
import com.hongte.alms.base.service.BasicCompanyService;
import com.hongte.alms.base.service.SysFinancialOrderService;
import com.hongte.alms.base.service.SysFinancialOrderUserService;
import com.hongte.alms.base.service.SysUserService;
import com.hongte.alms.base.vo.finance.SysFinancialOrderReq;
import com.hongte.alms.base.vo.finance.SysFinancialOrderVO;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.ht.ussp.bean.LoginUserInfoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 财务跟单设置 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-29
 */
@Service("SysFinancialOrderService")
public class SysFinancialOrderServiceImpl extends BaseServiceImpl<SysFinancialOrderMapper, SysFinancialOrder> implements SysFinancialOrderService {
    @Autowired
    @Qualifier("SysFinancialOrderUserService")
    private SysFinancialOrderUserService sysFinancialOrderUserService;
    @Autowired
    private LoginUserInfoHelper loginUserInfoHelper;
    @Autowired
    @Qualifier("BasicCompanyService")
    private BasicCompanyService basicCompanyService;
    @Autowired
    @Qualifier("SysUserService")
    private SysUserService sysUserService;


    /**
     *  自定义分页查询
     *
     * @param [page, businessTypeId, areaId, companyId, userName]
     * @return com.baomidou.mybatisplus.plugins.Page<com.hongte.alms.base.vo.finance.SysFinancialOrderVO>
     * @author 张贵宏
     * @date 2018/6/17 15:25
     */
    @Override
    public Page<SysFinancialOrderVO> search(Page<SysFinancialOrderVO> page, Integer businessTypeId, String areaId, String companyId, String userName) {
        List<SysFinancialOrderVO>  voList = ((SysFinancialOrderMapper)super.baseMapper).search(page, businessTypeId, areaId, companyId, userName);
        return page.setRecords(voList);
    }

    /**
     *  保存数据到父子表
     *
     * @param
     * @return void
     * @author 张贵宏
     * @date 2018/6/19 9:42
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(SysFinancialOrderReq req) {
        for (String companyId : req.getCompanyId()) {
            BasicCompany basicCompany = basicCompanyService.selectOne(new EntityWrapper<BasicCompany>().eq("area_level", AreaLevel.COMPANY_LEVEL.getKey()).eq("area_id", companyId));
            //公司id对应的区域ID
            String areaId = basicCompany.getAreaPid();
            for (Integer businessTypeId : req.getBusinessType()) {
                SysFinancialOrder financialOrder = super.selectOne(
                        new EntityWrapper<SysFinancialOrder>().eq("area_id", areaId)
                                .eq("company_id", companyId)
                                .eq("business_type_id", businessTypeId)
                );
                if (financialOrder == null) {
                    //父表中没有记录则新增
                    financialOrder = new SysFinancialOrder();
                    financialOrder.setAreaId(areaId);
                    financialOrder.setCompanyId(companyId);
                    financialOrder.setBusinessTypeId(businessTypeId);
                    financialOrder.setCreateUser(loginUserInfoHelper.getUserId());
                    financialOrder.setCreateTime(new Date());
                    boolean result = super.insert(financialOrder);
                    if (!result) {
                        throw new RuntimeException("插入失败");
                    }
                }
                for (String userId : req.getCollectionGroup1Users()) {
                    //保证子表中同一父id只有一条记录
                    SysFinancialOrderUser financialOrderUser = sysFinancialOrderUserService.selectOne(
                            new EntityWrapper<SysFinancialOrderUser>().eq("finance_order_id", financialOrder.getId())
                                    .eq("user_id", userId)
                    );
                    if (financialOrderUser == null) {
                        String userName = sysUserService.selectById(userId).getUserName();
                        SysFinancialOrderUser newFinancialOrderUser = new SysFinancialOrderUser();
                        newFinancialOrderUser.setUserId(userId);
                        newFinancialOrderUser.setUserName(userName);
                        newFinancialOrderUser.setFinanceOrderId(financialOrder.getId());
                        newFinancialOrderUser.setCreateUser(loginUserInfoHelper.getUserId());
                        newFinancialOrderUser.setCreateTime(new Date());
                        boolean result = sysFinancialOrderUserService.insert(newFinancialOrderUser);
                        if (!result) {
                            throw new RuntimeException("插入跟单用户失败");
                        }
                    }
                }
            }
        }


    }

    /**
     *  按id删除
     *
     * @param [ids]
     * @return void
     * @author 张贵宏
     * @date 2018/6/19 9:44
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Integer id) {
        sysFinancialOrderUserService.delete(new EntityWrapper<SysFinancialOrderUser>().eq("finance_order_id", id));
        super.deleteById(id);
    }

    /*@Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Integer id, String userId) {
        SysFinancialOrder oldFinancialOrder = this.selectById(id);
        if(oldFinancialOrder != null){
            sysFinancialOrderUserService.delete(new EntityWrapper<SysFinancialOrderUser>().eq("finance_order_id", oldFinancialOrder.getId()).eq("user_id", userId));

            int childCount = sysFinancialOrderUserService.selectCount(new EntityWrapper<SysFinancialOrderUser>().eq("finance_order_id", oldFinancialOrder.getId()));
            if(childCount == 0){
                this.deleteById(id);
            }
        }
    }*/

}
