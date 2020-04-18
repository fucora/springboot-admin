package com.mm.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mm.modules.sys.dao.SysUserRoleDao;
import com.mm.modules.sys.entity.SysUserRoleEntity;
import com.mm.modules.sys.service.SysUserRoleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 用户与角色对应关系
 *
 * @author lwl
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleDao, SysUserRoleEntity> implements SysUserRoleService {
    @Override
    public void saveOrUpdate(Long userId, List<Long> roleIdList) {
        //先删除用户与角色关系
        this.remove(Wrappers.<SysUserRoleEntity>lambdaQuery()
                .eq(SysUserRoleEntity::getUserId, userId));

        if (roleIdList == null || roleIdList.size() == 0) {
            return;
        }
        List<SysUserRoleEntity> saveList = new ArrayList<>();
        //保存用户与角色关系
        for (Long roleId : roleIdList) {
            SysUserRoleEntity ur = new SysUserRoleEntity();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            saveList.add(ur);
        }
        if (!saveList.isEmpty()) {
            this.saveBatch(saveList);
        }
    }
}
