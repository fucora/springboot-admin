package com.mm.modules.job.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mm.common.utils.PageUtil;
import com.mm.common.utils.Query;
import com.mm.modules.job.dao.ScheduleJobLogDao;
import com.mm.modules.job.entity.ScheduleJobLogEntity;
import com.mm.modules.job.service.ScheduleJobLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("scheduleJobLogService")
public class ScheduleJobLogServiceImpl extends ServiceImpl<ScheduleJobLogDao, ScheduleJobLogEntity> implements ScheduleJobLogService {

    @Override
    public PageUtil queryPage(Map<String, Object> params) {
        String jobId = (String) params.get("jobId");
        LambdaQueryWrapper<ScheduleJobLogEntity> qw = new LambdaQueryWrapper<>();
        qw.like(StringUtils.isNotBlank(jobId), ScheduleJobLogEntity::getJobId, jobId);
        qw.orderByDesc(ScheduleJobLogEntity::getCreateTime);
        IPage<ScheduleJobLogEntity> page = this.page(new Query<ScheduleJobLogEntity>().getPage(params), qw);
        return new PageUtil(page);
    }

}
