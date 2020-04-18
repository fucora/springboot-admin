package com.mm.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mm.modules.sys.entity.SysUserRoleEntity;

import java.util.List;


/**
 * 用户与角色对应关系
 *
 * @author lwl
 */
public interface SysUserRoleService extends IService<SysUserRoleEntity> {
	
	void saveOrUpdate(Long userId, List<Long> roleIdList);
}
