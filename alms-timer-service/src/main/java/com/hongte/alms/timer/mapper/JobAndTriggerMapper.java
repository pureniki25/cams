package com.hongte.alms.timer.mapper;


import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hongte.alms.common.mapper.SuperMapper;
import com.hongte.alms.timer.entity.JobAndTrigger;

import java.util.List;

public interface JobAndTriggerMapper extends SuperMapper<JobAndTrigger> {
	 List<JobAndTrigger> getJobAndTriggerDetails(Pagination pages);
}
