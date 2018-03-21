package com.hongte.alms.base.collection.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.collection.mapper.PhoneUrgeMapper;
import com.hongte.alms.base.collection.service.PhoneUrgeService;
import com.hongte.alms.base.collection.vo.AfterLoanStandingBookReq;
import com.hongte.alms.base.collection.vo.AfterLoanStandingBookVo;
import com.hongte.alms.base.collection.vo.StaffBusinessReq;
import com.hongte.alms.base.collection.vo.StaffBusinessVo;
import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.base.entity.SysUser;
import com.hongte.alms.base.enums.RepayPlanStatus;
import com.hongte.alms.base.enums.SysParameterTypeEnums;
import com.hongte.alms.base.service.BasicCompanyService;
import com.hongte.alms.base.service.SysParameterService;
import com.hongte.alms.base.service.SysUserService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.DateUtil;
import com.ht.ussp.bean.LoginUserInfoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 电催业务 服务实现类
 * </p>
 *
 * @author zengkun
 * @since 2018-01-24
 * */
@Service("PhoneUrgeService")
public class PhoneUrgeServiceImpl extends BaseServiceImpl<PhoneUrgeMapper, StaffBusinessVo> implements PhoneUrgeService {
//
    @Autowired
    PhoneUrgeMapper phoneUrgeMapper;


    @Autowired
    LoginUserInfoHelper loginUserInfoHelper;

    @Autowired
    @Qualifier("SysParameterService")
    SysParameterService sysParameterService;

    @Autowired
    @Qualifier("SysUserService")
    SysUserService sysUserService;

    @Autowired
    @Qualifier("BasicCompanyService")
    BasicCompanyService basicCompanyService;


//    @Override
//    public Page<StaffBusinessVo> selectPhoneUrgePage(Page<StaffBusinessVo> pages, StaffBusinessReq key) {
//        List<StaffBusinessVo> list = new LinkedList<StaffBusinessVo>();
//        StaffBusinessVo staffBusinessVo =  StaffBusinessVo.getDefaultVo();
//        list.add(staffBusinessVo);
//        StaffBusinessVo staffBusinessVo1 =  StaffBusinessVo.getDefaultVo();
//        staffBusinessVo1.setBusinessId("2");
//        staffBusinessVo1.setCrpId("crp2");
//        list.add(staffBusinessVo1);
//        for(StaffBusinessVo vo:list){
//            //计算逾期天数
//            vo.setOverDueDays(DateUtil.getDiffDays(new Date(),vo.getBorrowDate()));
//        }
//
//
//        pages.setRecords(list);
//        return pages;
//    }

    @Override
    public Page<AfterLoanStandingBookVo> selectAfterLoanStandingBookPage(AfterLoanStandingBookReq req){
        setAfterLoanStandingBookReqInfo(req);
        Page<AfterLoanStandingBookVo> pages = new Page<>();
        pages.setCurrent(req.getPage());
        pages.setSize(req.getLimit());

        if(req.getLiquidationOne()!=null&&!req.getLiquidationOne().equals("")){
            req.setLiquidationOneUIds(sysUserService.selectUseIdsByName(req.getLiquidationOne()));
        }
        if(req.getLiquidationTow()!=null&&!req.getLiquidationTow().equals("")){
            req.setLiquidationTowUIds(sysUserService.selectUseIdsByName(req.getLiquidationTow()));
        }

        //区域转换为公司列表
        List<String> areas = new LinkedList<String>();
        if(req.getAreaId()!= null && req.getAreaId()!=""){
            areas.add(req.getAreaId());
        }
        List<String> coms = new LinkedList<>();
        if(req.getCompanyId()!=null && req.getCompanyId()!=""){
            coms.add(req.getCompanyId());
        }
//        List<String> cIds = basicCompanyService.selectUserSearchComIds(loginUserInfoHelper.getUserId(),areas,coms);
        List<String> cIds = basicCompanyService.selectSearchComids(areas,coms);

        if(cIds.size()>0){
            req.setCommIds(cIds);
        }



        req.setUserId(loginUserInfoHelper.getUserId());


        List<AfterLoanStandingBookVo> list = phoneUrgeMapper.selectAfterLoadStanding(pages,req);

        for(AfterLoanStandingBookVo vo: list){
            SysUser user = sysUserService.selectById(vo.getPhoneStaffId());
            if(user!=null){
                vo.setPhoneStaffName(user.getUserName());
            }
            user = sysUserService.selectById(vo.getVisitStaffId());
            if(user!=null){
                vo.setVisitStaffName(user.getUserName());
            }
            List<SysParameter> pList = sysParameterService.selectList(new EntityWrapper<SysParameter>().
                    eq("param_type",SysParameterTypeEnums.COLLECTION_STATUS.getKey())
            .eq("param_value",vo.getColStatus()));
            if(pList.size()>0){
                vo.setAfterColStatusName(pList.get(0).getParamName());
            }
        }

//        pages.setRecords(setInfoForAfterLoanStandingBookVo(list));
        pages.setRecords(list);
        return pages;

    }


    public  List<String> getUserSearchComIds(AfterLoanStandingBookReq req){

        //取用户可访问的公司列表
        //1.用户拥有全局数据范围的权限 则取所有公司

        //2.用户拥有区域范围的权限
        // 则1）取用户区域表的配置
        // 如果没有 则从用户信息表中取原信贷的组织机构 查出公司ID  再没有就取现在UC上的组织机构ID查公司ID




        return  null;

    }


    @Override
    public List<AfterLoanStandingBookVo> selectAfterLoanStandingBookList(AfterLoanStandingBookReq req){
        setAfterLoanStandingBookReqInfo(req);
//        return  setInfoForAfterLoanStandingBookVo(phoneUrgeMapper.selectAfterLoadStanding(req));
        return  phoneUrgeMapper.selectAfterLoadStanding(req);
    }

    /**
     * 设置台账查询信息的额外信息
     * @param req
     * @return
     */
    private AfterLoanStandingBookReq setAfterLoanStandingBookReqInfo(AfterLoanStandingBookReq req){

//        new EntityWrapper<SysParameter>().eq("param_type", SysParameterTypeEnums.COLLECTION_LEVERS.getKey()).eq("param_value",req.getCollectLevel())

        if(req.getCollectLevel()!=null){

            List<SysParameter> lp = sysParameterService.selectList(new EntityWrapper<SysParameter>().eq("param_type", SysParameterTypeEnums.COLLECTION_LEVERS.getKey()).eq("param_value",req.getCollectLevel()));

            if(lp.size()>0){
                String remark =  lp.get(0).getRemark();
                String[]  delayDays  = remark.split("-");
                try{
                    if(delayDays.length == 1&&req.getDelayDaysEnd()==null){
                        req.setDelayDaysEnd(Integer.parseInt(delayDays[0]));
                    }else if(delayDays.length == 2){
                        if(req.getDelayDaysEnd()==null){
                            req.setDelayDaysEnd(Integer.parseInt(delayDays[1]));
                        }
                        if(req.getDelayDaysBegin()==null) {
                            req.setDelayDaysBegin(Integer.parseInt(delayDays[0]));
                        }
                    }
                }catch (NumberFormatException  e){
                    e.printStackTrace();
                }

            }
        }


        return req;
    }


    /**
     * 设置贷后台账的额外信息
     * @param list
     * @return
     */
//    private List<AfterLoanStandingBookVo> setInfoForAfterLoanStandingBookVo(List<AfterLoanStandingBookVo> list){
//        for(AfterLoanStandingBookVo vo:list){
//            vo.setStatusName(RepayPlanStatus.nameOf(vo.getStatus()));
//
//        }
//        return list;
//    }

}
