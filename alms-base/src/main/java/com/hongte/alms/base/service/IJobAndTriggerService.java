package com.hongte.alms.base.service;


import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.JobAndTrigger;


public interface IJobAndTriggerService {
	public Page<JobAndTrigger> getJobAndTriggerDetails(int pageNum, int pageSize);
}
