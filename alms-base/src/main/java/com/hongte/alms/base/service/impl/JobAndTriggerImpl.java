package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.plugins.Page;

import com.hongte.alms.base.entity.CarBasic;
import com.hongte.alms.base.entity.JobAndTrigger;
import com.hongte.alms.base.mapper.CarBasicMapper;
import com.hongte.alms.base.mapper.JobAndTriggerMapper;
import com.hongte.alms.base.service.IJobAndTriggerService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service()
public class JobAndTriggerImpl extends BaseServiceImpl<JobAndTriggerMapper, JobAndTrigger> implements IJobAndTriggerService {

	@Autowired
	private JobAndTriggerMapper jobAndTriggerMapper;

	@Override
	public Page<JobAndTrigger> getJobAndTriggerDetails(int pageNum, int pageSize) {

		Page<JobAndTrigger> pages = new Page<>();
		pages.setCurrent(pageNum);
		pages.setSize(pageSize);

		List<JobAndTrigger> list = jobAndTriggerMapper.getJobAndTriggerDetails(pages);

		pages.setRecords(list);

		return pages;

/*		PageHelper.startPage(pageNum, pageSize);
		PageInfo<JobAndTrigger> page = new PageInfo<JobAndTrigger>(list);
		return page;*/
	}

}