package com.mm.common.utils;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mm.modules.sys.entity.SysUserEntity;
import com.mm.modules.sys.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Security工具类
 *
 * @author lwl
 */
@Component
public class SecurityUtil {

    private static SysUserService sysUserService;
    private static PasswordEncoder passwordEncoder;

    public SecurityUtil(SysUserService sysUserService, PasswordEncoder passwordEncoder) {
        SecurityUtil.sysUserService = sysUserService;
        SecurityUtil.passwordEncoder = passwordEncoder;
    }

    public static String getUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) {
            return null;
        }
        return userDetails.getUsername();
    }

    public static SysUserEntity getUserEntity() {
        if (StringUtils.isBlank(getUsername())) {
            return null;
        }
        List<SysUserEntity> users = sysUserService.list(Wrappers.<SysUserEntity>lambdaQuery()
                .eq(SysUserEntity::getUsername, getUsername()));
        if (users.isEmpty()) {
            return null;
        }
        return users.get(0);
    }

    public static Long getUserId() {
        if (getUserEntity() == null) {
            return null;
        }
        return getUserEntity().getId();
    }

    public static String encodePwd(String pwd) {
        return passwordEncoder.encode(pwd);
    }

    public static Boolean matchesPwd(String pwd, String encodePwd) {
        return passwordEncoder.matches(pwd, encodePwd);
    }

}
