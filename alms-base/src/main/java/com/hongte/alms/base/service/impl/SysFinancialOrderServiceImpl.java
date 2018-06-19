package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.SysFinancialOrder;
import com.hongte.alms.base.entity.SysFinancialOrderUser;
import com.hongte.alms.base.mapper.SysFinancialOrderMapper;
import com.hongte.alms.base.service.SysFinancialOrderService;
import com.hongte.alms.base.service.SysFinancialOrderUserService;
import com.hongte.alms.base.vo.finance.SysFinancialOrderVO;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.util.BeanUtils;
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
    public void save(SysFinancialOrderVO sysFinancialOrderVO) {
        SysFinancialOrder oldFinancialORder = super.selectOne(
                new EntityWrapper<SysFinancialOrder>().eq("area_id",sysFinancialOrderVO.getAreaId())
                .eq("company_id", sysFinancialOrderVO.getCompanyId())
                .eq("business_type_id", sysFinancialOrderVO.getBusinessTypeId())
        );
        //父表中保证只有一条记录
        if(oldFinancialORder == null ){
            SysFinancialOrder newFinancialOrder = new SysFinancialOrder();
            BeanUtils.copyProperties(sysFinancialOrderVO, newFinancialOrder);
            newFinancialOrder.setCreateUser(loginUserInfoHelper.getUserId());
            newFinancialOrder.setCreateTime(new Date());
            boolean result = super.insert(newFinancialOrder);
            if(!result){
                throw new RuntimeException("插入失败");
            }

            SysFinancialOrderUser newFinancialOrderUser = new SysFinancialOrderUser();
            newFinancialOrderUser.setUserId(sysFinancialOrderVO.getUserId());
            newFinancialOrderUser.setUserName(sysFinancialOrderVO.getUserName());
            newFinancialOrderUser.setFinanceOrderId(newFinancialOrder.getId());
            newFinancialOrderUser.setCreateUser(loginUserInfoHelper.getUserId());
            newFinancialOrderUser.setCreateTime(new Date());
            result = sysFinancialOrderUserService.insert(newFinancialOrderUser);
            if(!result){
                throw new RuntimeException("插入跟单用户失败");
            }
        }else {
            //保证子表中同一父id只有一条记录
            SysFinancialOrderUser oldFinancialOrderUser = sysFinancialOrderUserService.selectOne(
                    new EntityWrapper<SysFinancialOrderUser>().eq("finance_order_id", oldFinancialORder.getId())
                    .eq("user_id", sysFinancialOrderVO.getUserId())
            );
            if(oldFinancialOrderUser == null){
                SysFinancialOrderUser newFinancialOrderUser = new SysFinancialOrderUser();
                newFinancialOrderUser.setUserId(sysFinancialOrderVO.getUserId());
                newFinancialOrderUser.setUserName(sysFinancialOrderVO.getUserName());
                newFinancialOrderUser.setFinanceOrderId(oldFinancialORder.getId());
                newFinancialOrderUser.setCreateUser(loginUserInfoHelper.getUserId());
                newFinancialOrderUser.setCreateTime(new Date());
                boolean result = sysFinancialOrderUserService.insert(newFinancialOrderUser);
                if(!result){
                    throw new RuntimeException("插入跟单用户失败");
                }
            }else{
                throw new RuntimeException("重复的跟单用户");
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
    public void delete(Integer id, String userId) {
        SysFinancialOrder oldFinancialOrder = this.selectById(id);
        if(oldFinancialOrder != null){
            sysFinancialOrderUserService.delete(new EntityWrapper<SysFinancialOrderUser>().eq("finance_order_id", oldFinancialOrder.getId()).eq("user_id", userId));

            int childCount = sysFinancialOrderUserService.selectCount(new EntityWrapper<SysFinancialOrderUser>().eq("finance_order_id", oldFinancialOrder.getId()));
            if(childCount == 0){
                this.deleteById(id);
            }
        }
    }

}
