package com.mm.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mm.modules.sys.entity.SysMenuEntity;
import com.mm.modules.sys.entity.SysRoleMenuEntity;
import com.mm.modules.sys.entity.SysUserEntity;
import com.mm.modules.sys.entity.SysUserRoleEntity;
import com.mm.modules.sys.service.SysMenuService;
import com.mm.modules.sys.service.SysRoleMenuService;
import com.mm.modules.sys.service.SysUserRoleService;
import com.mm.modules.sys.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("userService")
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        List<SysUserEntity> users = sysUserService.list(Wrappers.<SysUserEntity>lambdaQuery()
                .eq(SysUserEntity::getUsername, s));
        if (users.isEmpty()) {
            throw new UsernameNotFoundException("用户不存在");
        }
        SysUserEntity user = users.get(0);
        List<String> permsList = new ArrayList<>();
        if (user.getId() == 1) {
            permsList = sysMenuService.list().stream().filter(e -> StringUtils.isNotBlank(e.getPerms()))
                    .map(e -> e.getPerms()).collect(Collectors.toList());
        } else {
            List<SysUserRoleEntity> rus = sysUserRoleService.list(Wrappers.<SysUserRoleEntity>lambdaQuery()
                    .eq(SysUserRoleEntity::getUserId, user.getId()));
            if (!rus.isEmpty()) {
                List<SysRoleMenuEntity> rms = sysRoleMenuService.list(Wrappers.<SysRoleMenuEntity>lambdaQuery()
                        .in(SysRoleMenuEntity::getRoleId, rus.stream().map(e -> e.getRoleId()).collect(Collectors.toList())));
                if (!rms.isEmpty()) {
                    List<SysMenuEntity> ms = sysMenuService.list(Wrappers.<SysMenuEntity>lambdaQuery()
                            .in(SysMenuEntity::getId, rms.stream().map(e -> e.getMenuId()).collect(Collectors.toList())));
                    if (!ms.isEmpty()) {
                        permsList = ms.stream().filter(e -> StringUtils.isNotBlank(e.getPerms()))
                                .map(e -> e.getPerms()).collect(Collectors.toList());
                    }
                }
            }
        }
        Set<String> permsSet = new HashSet<>();
        for (String perms : permsList) {
            permsSet.addAll(Arrays.asList(perms.split(",")));
        }
        return new User(user.getUsername(), user.getPassword(), user.getStatus(), true,
                true, true,
                permsSet.stream().map(e -> new SimpleGrantedAuthority("ROLE_" + e)).collect(Collectors.toList()));
    }
}
