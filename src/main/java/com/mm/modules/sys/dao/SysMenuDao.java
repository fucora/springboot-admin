package com.mm.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mm.modules.sys.entity.SysMenuEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜单管理
 *
 * @author lwl
 */
@Mapper
public interface SysMenuDao extends BaseMapper<SysMenuEntity> {

}
