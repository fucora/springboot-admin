package com.mm.modules.sys.controller;

import com.mm.modules.sys.entity.SysUserEntity;
import com.mm.common.utils.SecurityUtil;

/**
 * Controller公共组件
 *
 * @author lwl
 */
public abstract class AbstractController {

    protected SysUserEntity getUser() {
        return (SysUserEntity) SecurityUtil.getUserEntity();
    }

    protected Long getUserId() {
        return getUser().getId();
    }

}
