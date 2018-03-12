package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.WithholdingRepaymentLog;
import com.hongte.alms.base.mapper.InfoSmsMapper;
import com.hongte.alms.base.mapper.WithholdingRepaymentLogMapper;
import com.hongte.alms.base.service.WithholdingRepaymentLogService;
import com.hongte.alms.base.vo.module.RepaymentLogReq;
import com.hongte.alms.base.vo.module.RepaymentLogVO;
import com.hongte.alms.common.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 还款计划代扣日志流水表 服务实现类
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-03-08
 */
@Service("WithholdingRepaymentLogService")
@Transactional
public class WithholdingRepaymentLogServiceImpl extends BaseServiceImpl<WithholdingRepaymentLogMapper, WithholdingRepaymentLog> implements WithholdingRepaymentLogService {
    @Autowired
    WithholdingRepaymentLogMapper withholdingRepaymentLogmapper;
	@Override
	public Page<RepaymentLogVO> selectRepaymentLogPage(RepaymentLogReq key) {
	      Page<RepaymentLogVO> pages = new Page<>();
	      pages.setSize(key.getLimit());
	      pages.setCurrent(key.getPage());

	      List<RepaymentLogVO> list = withholdingRepaymentLogmapper.selectRepaymentLogList(pages,key);
	      

	      pages.setRecords(list);
		return pages;
	}
	
	//不分页，作为导出EXCEL数据
	@Override
	public List<RepaymentLogVO> selectRepaymentLogExcel(RepaymentLogReq key) {

	      List<RepaymentLogVO> list = withholdingRepaymentLogmapper.selectRepaymentLogList(key);
	      

		return list;
	}

	@Override
	public RepaymentLogVO selectSumByBusinessId(String repayStatus, String userId) {
		return withholdingRepaymentLogmapper.selectSumByBusinessId(repayStatus, userId);
	}

	@Override
	public RepaymentLogVO selectSumByLogId(String userId) {
		return withholdingRepaymentLogmapper.selectSumByLogId(userId);
	}

}
