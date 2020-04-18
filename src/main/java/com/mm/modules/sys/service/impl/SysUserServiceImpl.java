package com.mm.modules.sys.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mm.common.utils.Constant;
import com.mm.common.utils.PageUtil;
import com.mm.common.utils.Query;
import com.mm.modules.sys.dao.SysUserDao;
import com.mm.modules.sys.entity.SysUserEntity;
import com.mm.modules.sys.entity.SysUserRoleEntity;
import com.mm.modules.sys.service.SysUserRoleService;
import com.mm.modules.sys.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 系统用户
 *
 * @author lwl
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public List<Long> queryAllMenuId(Long userId) {
        return baseMapper.queryAllMenuId(userId);
    }

    @Override
    public PageUtil queryPage(Map<String, Object> params) {
        String username = (String) params.get("username");

        IPage<SysUserEntity> page = this.page(
                new Query<SysUserEntity>().getPage(params),
                new QueryWrapper<SysUserEntity>()
                        .like(StringUtils.isNotBlank(username), "username", username)
                        .apply(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER))
        );
        for (SysUserEntity su : page.getRecords()) {
            su.setPassword(null);
            //获取用户所属的角色列表
            List<SysUserRoleEntity> urs = sysUserRoleService.list(Wrappers.<SysUserRoleEntity>lambdaQuery()
                    .select(SysUserRoleEntity::getRoleId)
                    .eq(SysUserRoleEntity::getUserId, su.getId()));
            if (!urs.isEmpty()) {
                su.setRoleIdList(urs.stream().map(e -> e.getRoleId()).collect(Collectors.toList()));
            }
        }
        return new PageUtil(page);
    }

}
