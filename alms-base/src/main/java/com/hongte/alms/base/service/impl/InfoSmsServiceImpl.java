package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.ApplyDerateProcess;
import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.entity.InfoSms;
import com.hongte.alms.base.enums.ProcessEngineFlageEnums;
import com.hongte.alms.base.enums.SysParameterTypeEnums;
import com.hongte.alms.base.mapper.ApplyDerateProcessMapper;
import com.hongte.alms.base.mapper.InfoSmsMapper;
import com.hongte.alms.base.process.entity.*;
import com.hongte.alms.base.process.entity.Process;
import com.hongte.alms.base.process.enums.ProcessStatusEnums;
import com.hongte.alms.base.process.enums.ProcessApproveResult;
import com.hongte.alms.base.process.service.*;
import com.hongte.alms.base.vo.module.ApplyDerateProcessReq;
import com.hongte.alms.base.process.vo.ProcessLogReq;
import com.hongte.alms.base.service.ApplyDerateProcessService;
import com.hongte.alms.base.service.BasicCompanyService;
import com.hongte.alms.base.service.InfoSmsService;
import com.hongte.alms.base.service.SysParameterService;
import com.hongte.alms.base.vo.module.ApplyDerateListSearchReq;
import com.hongte.alms.base.vo.module.ApplyDerateVo;
import com.hongte.alms.base.vo.module.InfoSmsListSearchReq;
import com.hongte.alms.base.vo.module.InfoSmsListSearchVO;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.ClassCopyUtil;
import com.hongte.alms.common.util.Constant;
import com.ht.ussp.bean.LoginUserInfoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenzs
 * @since 2018-03-02
 */
@Service("InfoSmsService")
@Transactional
public class InfoSmsServiceImpl extends BaseServiceImpl<InfoSmsMapper, InfoSms> implements InfoSmsService {

	
    @Autowired
    InfoSmsMapper infoSmsMap;
    /**
     * 分页查询
     *
     * @param key
     * @return
     */

	@Override
	public Page<InfoSmsListSearchVO> selectInfoSmsPage(InfoSmsListSearchReq key) {
		//
      Page<InfoSmsListSearchVO> pages = new Page<>();
      pages.setSize(key.getLimit());
      pages.setCurrent(key.getPage());

      List<InfoSmsListSearchVO> list = infoSmsMap.selectInfoSmsList(pages,key);
      

      pages.setRecords(list);


    return pages;
	}
	@Override
	public InfoSmsListSearchVO selectLastInfoSmsDetail(String logId) {
		InfoSmsListSearchVO vo=infoSmsMap.selectLastInfoSmsDetail(logId);
		return vo;
	}
	@Override
	public InfoSmsListSearchVO selectNextInfoSmsDetail(String logId) {
		InfoSmsListSearchVO vo=infoSmsMap.selectNextInfoSmsDetail(logId);
		return vo;
	}

//    LoginUs

 

    /**
     * 分页查询
     *
     * @param key
     * @return
     */
//    @Override
//   public  Page<InfoSms> selectInfoSmsPage(InfoSmsListSearchReq key);
//
//        Page<ApplyDerateVo> pages = new Page<>();
//        pages.setSize(key.getLimit());
//        pages.setCurrent(key.getPage());
//
//        List<ApplyDerateVo> list = applyDerateProcessMap.selectApplyDerateList(pages,key);
//
//        pages.setRecords(setApplyDerateVoListInfo(list));
//
//
//        return pages;
//    }







}
