package com.mm.modules.sys.controller;


import cn.hutool.core.date.DateUtil;
import com.mm.common.annotation.SysLog;
import com.mm.common.utils.PageUtil;
import com.mm.common.utils.R;
import com.mm.common.utils.SecurityUtil;
import com.mm.common.validator.Assert;
import com.mm.common.validator.ValidatorUtils;
import com.mm.modules.sys.entity.SysUserEntity;
import com.mm.modules.sys.service.SysUserRoleService;
import com.mm.modules.sys.service.SysUserService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

/**
 * 系统用户
 *
 * @author lwl
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 所有用户列表
     */
    @RequestMapping("/list")
    @PreAuthorize("hasAnyRole('sys:user:list')")
    public PageUtil list(@RequestParam Map<String, Object> params) {
        return sysUserService.queryPage(params);
    }

    /**
     * 获取登录的用户信息
     */
    @RequestMapping("/info")
    public R info() {
        return R.ok().put("user", getUser());
    }

    /**
     * 修改登录用户密码
     */
    @SysLog("修改密码")
    @RequestMapping("/password")
    public R password(String password, String newPassword) {
        Assert.isBlank(password, "原密码不为能空");
        Assert.isBlank(newPassword, "新密码不为能空");
        final SysUserEntity user = SecurityUtil.getUserEntity();
        if (!SecurityUtil.matchesPwd(password, user.getPassword())) {
            return R.error("原密码不正确");
        }
        SysUserEntity uu = new SysUserEntity();
        uu.setId(user.getId());
        uu.setPassword(SecurityUtil.encodePwd(newPassword));
        if (sysUserService.updateById(uu)) {
            return R.ok();
        }
        return R.error("修改失败");
    }

    /**
     * 保存修改用户
     */
    @SysLog("保存修改用户")
    @RequestMapping("/saveOrUpdate")
    @PreAuthorize("hasAnyRole('sys:user:save','sys:user:update')")
    public R save(@RequestBody SysUserEntity entity) {
        ValidatorUtils.validateEntity(entity);
        if (entity.getId() == null) {
            Assert.isBlank(entity.getPassword(), "密码不能为空");
            entity.setPassword(SecurityUtil.encodePwd(entity.getPassword()));
            entity.setCreateTime(DateUtil.date());
        } else {
            if (StringUtils.isBlank(entity.getPassword())) {
                entity.setPassword(null);
            } else {
                entity.setPassword(SecurityUtil.encodePwd(entity.getPassword()));
            }
        }
        if (sysUserService.saveOrUpdate(entity)) {
            //保存用户与角色关系
            sysUserRoleService.saveOrUpdate(entity.getId(), entity.getRoleIdList());
        }
        return R.ok();
    }

    /**
     * 删除用户
     */
    @SysLog("删除用户")
    @RequestMapping("/delete")
    @PreAuthorize("hasAnyRole('sys:user:delete')")
    public R delete(@RequestBody Long[] ids) {
        if (ArrayUtils.contains(ids, 1L)) {
            return R.error("系统管理员不能删除");
        }

        if (ArrayUtils.contains(ids, getUserId())) {
            return R.error("当前用户不能删除");
        }

        sysUserService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
}
