package com.hongte.alms.base.mapper;


import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hongte.alms.base.entity.JobAndTrigger;
import com.hongte.alms.common.mapper.SuperMapper;


import java.util.List;

public interface JobAndTriggerMapper extends SuperMapper<JobAndTrigger> {
	 List<JobAndTrigger> getJobAndTriggerDetails(Pagination pages);
}
