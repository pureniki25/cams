package com.hongte.alms.base.collection.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.collection.entity.CollectionTrackLog;
import com.hongte.alms.base.collection.mapper.CollectionTrackLogMapper;
import com.hongte.alms.base.collection.service.CollectionTrackLogService;
import com.hongte.alms.base.collection.vo.CollectionTrackLogVo;
import com.hongte.alms.base.collection.vo.CollectionTrckLogReq;
import com.hongte.alms.base.service.FiveLevelClassifyBusinessChangeLogService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.ht.ussp.bean.LoginUserInfoHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 贷后跟踪记录表 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-02-01
 */
@Service("CollectionTrackLogService")
public class CollectionTrackLogServiceImpl extends BaseServiceImpl<CollectionTrackLogMapper, CollectionTrackLog>
		implements CollectionTrackLogService {

	@Autowired
	CollectionTrackLogMapper collectionTrackLogMapper;

	@Autowired
	@Qualifier("RepaymentBizPlanListService")
	private RepaymentBizPlanListService repaymentBizPlanListService;

	@Autowired
	@Qualifier("FiveLevelClassifyBusinessChangeLogService")
	private FiveLevelClassifyBusinessChangeLogService fiveLevelClassifyBusinessChangeLogService;

	@Autowired
	private LoginUserInfoHelper loginUserInfoHelper;

	@Override
	public Page<CollectionTrackLogVo> selectCollectionTrackLogByRbpId(CollectionTrckLogReq req) {
		Page<CollectionTrackLogVo> pages = new Page<>();
		pages.setCurrent(req.getPage());
		pages.setSize(req.getLimit());

		pages.setRecords(
				collectionTrackLogMapper.selectCollectionTrackLogByRbpId(pages, req.getRbpId(), req.getBusinessId()));

		return pages;

	}

	@Override
	public List<String> selectProjectIdByRbpId(String rbpId) {
		return collectionTrackLogMapper.selectProjectIdByRbpId(rbpId);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void addOrUpdateLog(CollectionTrackLog log) {

		if (log == null) {
			return;
		}

		log.setUniqueId(UUID.randomUUID().toString());

		if (log.getTrackLogId() != null) {
			CollectionTrackLog logOld = this.selectById(log.getTrackLogId());
			logOld.setContent(log.getContent());
			logOld.setIsSend(log.getIsSend());
			logOld.setTrackStatusId(log.getTrackStatusId());
			logOld.setTrackStatusName(log.getTrackStatusName());
			logOld.setBorrowerConditionDescList(log.getBorrowerConditionDescList());
			logOld.setGuaranteeConditionDescList(log.getGuaranteeConditionDescList());
			logOld.setClassName(log.getClassName());
			log = logOld;
		}
		CollectionTrackLogVo.setDefaultVal(log, loginUserInfoHelper.getUserId());
		this.insertOrUpdate(log);

		int count = this.selectCount(new EntityWrapper<CollectionTrackLog>().eq("unique_id", log.getUniqueId()));
		if (count > 0) {

			String origBusinessId = repaymentBizPlanListService.selectById(log.getRbpId()).getOrigBusinessId();

			fiveLevelClassifyBusinessChangeLogService.businessChangeLog(log.getClassName(),
					log.getBorrowerConditionDescList(), log.getGuaranteeConditionDescList(), log.getUniqueId(),
					origBusinessId);
		}
	}

}
