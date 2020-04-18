package com.mm.modules.job.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mm.common.utils.Constant;
import com.mm.common.utils.PageUtil;
import com.mm.common.utils.Query;
import com.mm.modules.job.dao.ScheduleJobDao;
import com.mm.modules.job.entity.ScheduleJobEntity;
import com.mm.modules.job.service.ScheduleJobService;
import com.mm.modules.job.utils.ScheduleUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

@Service("scheduleJobService")
public class ScheduleJobServiceImpl extends ServiceImpl<ScheduleJobDao, ScheduleJobEntity> implements ScheduleJobService {

    @Resource
    private Scheduler scheduler;

    /**
     * 项目启动时，初始化定时器
     */
    @PostConstruct
    public void init() {
        List<ScheduleJobEntity> scheduleJobList = this.list();
        for (ScheduleJobEntity scheduleJob : scheduleJobList) {
            CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJob.getId());
            //如果不存在，则创建
            if (cronTrigger == null) {
                ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
            } else {
                ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
            }
        }
    }

    @Override
    public PageUtil queryPage(Map<String, Object> params) {
        String beanName = (String) params.get("beanName");
        LambdaQueryWrapper<ScheduleJobEntity> qw = new LambdaQueryWrapper<>();
        qw.like(StringUtils.isNotBlank(beanName), ScheduleJobEntity::getBeanName, beanName);
        IPage<ScheduleJobEntity> page = this.page(new Query<ScheduleJobEntity>().getPage(params), qw);
        return new PageUtil(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdate(ScheduleJobEntity entity) {
        if (entity.getId() == null) {
            entity.setCreateTime(new Date());
            entity.setStatus(Constant.ScheduleStatus.NORMAL.getValue());
            this.save(entity);
            ScheduleUtils.createScheduleJob(scheduler, entity);
        } else {
            ScheduleUtils.updateScheduleJob(scheduler, entity);
            this.updateById(entity);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] jobIds) {
        for (Long jobId : jobIds) {
            ScheduleUtils.deleteScheduleJob(scheduler, jobId);
        }
        //删除数据
        this.removeByIds(Arrays.asList(jobIds));
    }

    private void updateBatch(Long[] jobIds, int status) {
        List<ScheduleJobEntity> list = new ArrayList<>();
        for (Long jobId : jobIds) {
            ScheduleJobEntity job = new ScheduleJobEntity();
            job.setId(jobId);
            job.setStatus(status);
            list.add(job);
        }
        if (!list.isEmpty()) {
            this.updateBatchById(list);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(Long[] jobIds) {
        for (Long jobId : jobIds) {
            ScheduleUtils.run(scheduler, this.getById(jobId));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pause(Long[] jobIds) {
        for (Long jobId : jobIds) {
            ScheduleUtils.pauseJob(scheduler, jobId);
        }

        updateBatch(jobIds, Constant.ScheduleStatus.PAUSE.getValue());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resume(Long[] jobIds) {
        for (Long jobId : jobIds) {
            ScheduleUtils.resumeJob(scheduler, jobId);
        }

        updateBatch(jobIds, Constant.ScheduleStatus.NORMAL.getValue());
    }

}
