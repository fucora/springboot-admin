package com.mm.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mm.modules.sys.dao.SysRoleMenuDao;
import com.mm.modules.sys.entity.SysRoleMenuEntity;
import com.mm.modules.sys.service.SysRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色与菜单对应关系
 *
 * @author lwl
 */
@Service("sysRoleMenuService")
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuDao, SysRoleMenuEntity> implements SysRoleMenuService {

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveOrUpdate(Long roleId, List<Long> menuIdList) {
		//先删除角色与菜单关系
		remove(Wrappers.<SysRoleMenuEntity>lambdaQuery()
				.in(SysRoleMenuEntity::getRoleId, new Object[]{roleId}));

		if(menuIdList.size() == 0){
			return ;
		}

		//保存角色与菜单关系
		List<SysRoleMenuEntity> saveList = new ArrayList<>();
		for(Long menuId : menuIdList){
			SysRoleMenuEntity rm = new SysRoleMenuEntity();
			rm.setMenuId(menuId);
			rm.setRoleId(roleId);
			saveList.add(rm);
		}
		if (!saveList.isEmpty()) {
			this.saveBatch(saveList);
		}
	}
}
