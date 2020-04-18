package com.mm.modules.job.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mm.common.utils.PageUtil;
import com.mm.modules.job.entity.ScheduleJobLogEntity;

import java.util.Map;

/**
 * 定时任务日志
 *
 * @author lwl
 */
public interface ScheduleJobLogService extends IService<ScheduleJobLogEntity> {

	PageUtil queryPage(Map<String, Object> params);
	
}
