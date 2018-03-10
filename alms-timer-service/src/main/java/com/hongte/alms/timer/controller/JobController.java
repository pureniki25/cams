package com.hongte.alms.timer.controller;

import com.baomidou.mybatisplus.plugins.Page;

import com.hongte.alms.timer.entity.JobAndTrigger;
import com.hongte.alms.timer.job.BaseJob;
import com.hongte.alms.timer.service.IJobAndTriggerService;
import com.hongte.alms.timer.service.IXDSyncDataService;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value= "/job")
public class JobController 
{
	@Autowired
	private IJobAndTriggerService iJobAndTriggerService;

	private static Logger log = LoggerFactory.getLogger(JobController.class);  
	

	@PostMapping(value="/addjob")
	public void addjob(@RequestParam(value="jobClassName")String jobClassName, 
			@RequestParam(value="jobGroupName")String jobGroupName, 
			@RequestParam(value="cronExpression")String cronExpression) throws Exception
	{			
		addJob(jobClassName, jobGroupName, cronExpression);
	}
	
	public void addJob(String jobClassName, String jobGroupName, String cronExpression)throws Exception{

		Scheduler scheduler = new StdSchedulerFactory().getScheduler();

		
		//构建job信息
		JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass()).withIdentity(jobClassName, jobGroupName).build();
		
		//表达式调度构建器(即任务执行的时间)
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

        //按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName)
            .withSchedule(scheduleBuilder).build();
        
        try {
        	scheduler.scheduleJob(jobDetail, trigger);
            
        } catch (SchedulerException e) {
            System.out.println("创建定时任务失败");
            throw new Exception("创建定时任务失败",e);
        }

		// 启动调度器
		scheduler.start();
	}


	@PostMapping(value="/pausejob")
	public void pausejob(@RequestParam(value="jobClassName")String jobClassName, @RequestParam(value="jobGroupName")String jobGroupName) throws Exception
	{			
		jobPause(jobClassName, jobGroupName);
	}
	
	public void jobPause(String jobClassName, String jobGroupName) throws Exception
	{

		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.pauseJob(JobKey.jobKey(jobClassName, jobGroupName));
	}
	

	@PostMapping(value="/resumejob")
	public void resumejob(@RequestParam(value="jobClassName")String jobClassName, @RequestParam(value="jobGroupName")String jobGroupName) throws Exception
	{			
		jobresume(jobClassName, jobGroupName);
	}
	
	public void jobresume(String jobClassName, String jobGroupName) throws Exception
	{

		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.resumeJob(JobKey.jobKey(jobClassName, jobGroupName));
	}
	
	
	@PostMapping(value="/reschedulejob")
	public void rescheduleJob(@RequestParam(value="jobClassName")String jobClassName, 
			@RequestParam(value="jobGroupName")String jobGroupName,
			@RequestParam(value="cronExpression")String cronExpression) throws Exception
	{			
		jobreschedule(jobClassName, jobGroupName, cronExpression);
	}
	
	public void jobreschedule(String jobClassName, String jobGroupName, String cronExpression) throws Exception
	{				
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);
			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);


			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
		} catch (SchedulerException e) {
			System.out.println("更新定时任务失败"+e);
			throw new Exception("更新定时任务失败");
		}
	}
	
	
	@PostMapping(value="/deletejob")
	public void deletejob(@RequestParam(value="jobClassName")String jobClassName, @RequestParam(value="jobGroupName")String jobGroupName) throws Exception
	{			
		jobdelete(jobClassName, jobGroupName);
	}
	
	public void jobdelete(String jobClassName, String jobGroupName) throws Exception
	{
		Scheduler scheduler;
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		scheduler = schedulerFactory.getScheduler();
		scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName));
		scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName));
		scheduler.deleteJob(JobKey.jobKey(jobClassName, jobGroupName));				
	}
	
	
	@GetMapping(value="/queryjob")
	public Map<String, Object> queryjob(@RequestParam(value="pageNum")Integer pageNum, @RequestParam(value="pageSize")Integer pageSize) 
	{			
		Page<JobAndTrigger> jobAndTrigger = iJobAndTriggerService.getJobAndTriggerDetails(pageNum, pageSize);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("JobAndTrigger", jobAndTrigger);
		map.put("number", jobAndTrigger.getTotal());

/*		try {
			List<JobExecutionContext> list = scheduler.getCurrentlyExecutingJobs();
			for(JobExecutionContext e:list){
				JobDetail detail = e.getJobDetail();
				System.out.println(detail.getDescription());
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}*/
		return map;
	}
	
	public static BaseJob getClass(String classname) throws Exception
	{
		Class<?> class1 = Class.forName(classname);
		return (BaseJob)class1.newInstance();
	}
	
	
}
