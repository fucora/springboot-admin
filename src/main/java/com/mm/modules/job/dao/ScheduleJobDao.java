package com.mm.modules.job.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mm.modules.job.entity.ScheduleJobEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务
 *
 * @author lwl
 */
@Mapper
public interface ScheduleJobDao extends BaseMapper<ScheduleJobEntity> {
}
