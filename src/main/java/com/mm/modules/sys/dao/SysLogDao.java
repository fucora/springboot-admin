package com.mm.modules.sys.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mm.modules.sys.entity.SysLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统日志
 *
 * @author lwl
 */
@Mapper
public interface SysLogDao extends BaseMapper<SysLogEntity> {
	
}
