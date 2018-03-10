package com.hongte.alms.timer.service.impl;

import com.baomidou.mybatisplus.plugins.Page;

import com.hongte.alms.timer.entity.JobAndTrigger;
import com.hongte.alms.timer.mapper.JobAndTriggerMapper;
import com.hongte.alms.timer.service.IJobAndTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class JobAndTriggerImpl implements IJobAndTriggerService {

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