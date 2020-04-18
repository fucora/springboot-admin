package com.mm.modules.job.utils;

import com.mm.common.utils.SpringContextUtil;
import com.mm.modules.job.entity.ScheduleJobEntity;
import com.mm.modules.job.entity.ScheduleJobLogEntity;
import com.mm.modules.job.service.ScheduleJobLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.lang.reflect.Method;
import java.util.Date;


/**
 * 定时任务
 *
 * @author lwl
 */
@Slf4j
public class ScheduleJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        ScheduleJobEntity scheduleJob = (ScheduleJobEntity) context.getMergedJobDataMap()
        		.get(ScheduleJobEntity.JOB_PARAM_KEY);
        
        //获取spring bean
        ScheduleJobLogService scheduleJobLogService = (ScheduleJobLogService) SpringContextUtil.getBean("scheduleJobLogService");
        
        //数据库保存执行记录
        ScheduleJobLogEntity jobLog = new ScheduleJobLogEntity();
		jobLog.setJobId(scheduleJob.getId());
		jobLog.setBeanName(scheduleJob.getBeanName());
		jobLog.setParams(scheduleJob.getParams());
		jobLog.setCreateTime(new Date());
        
        //任务开始时间
        long startTime = System.currentTimeMillis();
        
        try {
            //执行任务
        	log.debug("任务准备执行，任务ID：{}", scheduleJob.getId());

			Object target = SpringContextUtil.getBean(scheduleJob.getBeanName());
			Method method = target.getClass().getDeclaredMethod("run", String.class);
			method.invoke(target, scheduleJob.getParams());
			
			//任务执行总时长
			long times = System.currentTimeMillis() - startTime;
			jobLog.setTimes((int)times);
			//任务状态    0：失败    1：成功
			jobLog.setStatus(1);
			
			log.debug("任务执行完毕，任务ID：" + scheduleJob.getId() + "  总共耗时：" + times + "毫秒");
		} catch (Exception e) {
			log.error("任务执行失败，任务ID：" + scheduleJob.getId(), e);
			
			//任务执行总时长
			long times = System.currentTimeMillis() - startTime;
			jobLog.setTimes((int)times);
			
			//任务状态    0：失败    1：成功
			jobLog.setStatus(0);
			jobLog.setError(StringUtils.substring(e.toString(), 0, 2000));
		}finally {
			scheduleJobLogService.save(jobLog);
		}
    }
}
