package com.mm.modules.sys.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mm.common.annotation.SysLog;
import com.mm.common.utils.Constant;
import com.mm.common.utils.PageUtil;
import com.mm.common.utils.R;
import com.mm.common.validator.ValidatorUtils;
import com.mm.modules.sys.entity.SysMenuEntity;
import com.mm.modules.sys.entity.SysRoleEntity;
import com.mm.modules.sys.entity.SysRoleMenuEntity;
import com.mm.modules.sys.service.SysMenuService;
import com.mm.modules.sys.service.SysRoleMenuService;
import com.mm.modules.sys.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 角色管理
 *
 * @author lwl
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {
    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    /**
     * 全部角色列表
     */
    @RequestMapping("/all")
    public R all() {
        return R.ok().put("data", sysRoleService.list());
    }

    /**
     * 角色列表
     */
    @RequestMapping("/list")
    @PreAuthorize("hasAnyRole('sys:role:list')")
    public PageUtil list(@RequestParam Map<String, Object> params) {
        return sysRoleService.queryPage(params);
    }

    /**
     * 获取角色菜单
     */
    @RequestMapping("/select")
    public R select(Long roleId) {
        List<SysRoleMenuEntity> rms = new ArrayList<>();
        if (roleId != null) {
            rms = sysRoleMenuService.list(Wrappers.<SysRoleMenuEntity>lambdaQuery()
                    .select(SysRoleMenuEntity::getMenuId)
                    .eq(SysRoleMenuEntity::getRoleId, roleId));
        }
        // 查询目录
        List<Map<String, Object>> list = getMenu(0L, rms);
        return R.ok().put("menuList", list);
    }

    private List<Map<String, Object>> getMenu(Long pid, List<SysRoleMenuEntity> rms) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<SysMenuEntity> ms = sysMenuService.list(Wrappers.<SysMenuEntity>lambdaQuery()
                .eq(SysMenuEntity::getPid, pid).ne(SysMenuEntity::getType, Constant.MenuType.BUTTON.getValue())
                .orderByAsc(SysMenuEntity::getOrderNum));
        for (SysMenuEntity m : ms) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", m.getId());
            map.put("title", m.getName());
            List<Map<String, Object>> menus = getMenu(m.getId(), rms);
            if (!menus.isEmpty()) {
                map.put("children", menus);
                // 是否初始展开
                map.put("spread", true);
            }
            // 是否选中
            if (map.get("children") == null && !rms.isEmpty()) {
                for (SysRoleMenuEntity rm : rms) {
                    if (m.getId().equals(rm.getMenuId())) {
                        map.put("checked", true);
                    }
                }
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 保存角色
     */
    @SysLog("保存角色")
    @RequestMapping("/saveOrUpdate")
    @PreAuthorize("hasAnyRole('sys:role:save', 'sys:role:update')")
    public R saveOrUpdate(@RequestBody SysRoleEntity role) {
        ValidatorUtils.validateEntity(role);
        sysRoleService.saveOrUpdate(role);
        return R.ok();
    }

    /**
     * 删除角色
     */
    @SysLog("删除角色")
    @RequestMapping("/delete")
    @PreAuthorize("hasAnyRole('sys:role:delete')")
    public R delete(@RequestBody Long[] roleIds) {
        sysRoleService.removeByIds(Arrays.asList(roleIds));
        return R.ok();
    }
}
