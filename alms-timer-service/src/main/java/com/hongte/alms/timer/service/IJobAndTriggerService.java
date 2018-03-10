package com.hongte.alms.timer.service;


import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.timer.entity.JobAndTrigger;

public interface IJobAndTriggerService {
	public Page<JobAndTrigger> getJobAndTriggerDetails(int pageNum, int pageSize);
}
