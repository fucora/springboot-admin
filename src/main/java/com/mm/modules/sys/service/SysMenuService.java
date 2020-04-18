package com.mm.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mm.modules.sys.entity.SysMenuEntity;

import java.util.List;


/**
 * 菜单管理
 *
 * @author lwl
 */
public interface SysMenuService extends IService<SysMenuEntity> {

	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 * @param menuIdList  用户菜单ID
	 */
	List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList);
	
	/**
	 * 获取用户菜单列表
	 */
	List<SysMenuEntity> getUserMenuList(Long userId);

	/**
	 * 删除
	 */
	void delete(Long menuId);
}
