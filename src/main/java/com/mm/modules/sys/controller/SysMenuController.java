package com.mm.modules.sys.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mm.common.annotation.SysLog;
import com.mm.common.exception.RRException;
import com.mm.common.utils.Constant;
import com.mm.common.utils.R;
import com.mm.modules.sys.entity.SysMenuEntity;
import com.mm.modules.sys.service.SysMenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统菜单
 *
 * @author lwl
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends AbstractController {
    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 导航菜单
     */
    @RequestMapping("/nav")
    public R nav() {
        List<SysMenuEntity> menuList = sysMenuService.getUserMenuList(getUserId());
        return R.ok().put("menuList", menuList);
    }

    /**
     * 所有菜单列表
     */
    @RequestMapping("/list")
    @PreAuthorize("hasAnyRole('sys:menu:list')")
    public List<SysMenuEntity> list() {
        List<SysMenuEntity> list = sysMenuService.list();
        for (SysMenuEntity menu : list) {
            if (menu.getPid() != null & 0 != menu.getPid()) {
                menu.setParentName(sysMenuService.getById(menu.getPid()).getName());
            }
        }
        return list;
    }

    /**
     * 选择菜单(添加、修改菜单)
     */
    @RequestMapping("/select")
    public R select() {
        // 查询目录
        List<Map<String, Object>> list = getMenu(0L);
        //添加顶级菜单
        Map<String, Object> map = new HashMap<>();
        map.put("id", 0);
        map.put("title", "一级菜单");
        map.put("children", list);
        List<Map<String, Object>> menuList = new ArrayList<>();
        menuList.add(map);
        return R.ok().put("menuList", menuList);
    }

    private List<Map<String, Object>> getMenu(Long pid) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<SysMenuEntity> ms = sysMenuService.list(Wrappers.<SysMenuEntity>lambdaQuery()
                .eq(SysMenuEntity::getPid, pid).ne(SysMenuEntity::getType, Constant.MenuType.BUTTON.getValue())
                .orderByAsc(SysMenuEntity::getOrderNum));
        for (SysMenuEntity m : ms) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", m.getId());
            map.put("title", m.getName());
            List<Map<String, Object>> menus = getMenu(m.getId());
            if (!menus.isEmpty()) {
                map.put("children", menus);
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 保存修改
     */
    @SysLog("保存修改菜单")
    @RequestMapping("/saveOrUpdate")
    @PreAuthorize("hasAnyRole('sys:menu:save', 'sys:menu:update')")
    public R saveOrUpdate(@RequestBody SysMenuEntity menu) {
        //数据校验
        verifyForm(menu);
        sysMenuService.saveOrUpdate(menu);
        return R.ok();
    }

    /**
     * 删除
     */
    @SysLog("删除菜单")
    @RequestMapping("/delete")
    @PreAuthorize("hasAnyRole('sys:menu:delete')")
    public R delete(@RequestBody Long[] ids) {
        for (Long id : ids) {
            //判断是否有子菜单或按钮
            List<SysMenuEntity> menuList = sysMenuService.list(Wrappers.<SysMenuEntity>lambdaQuery()
                    .eq(SysMenuEntity::getPid, id));
            if (menuList.size() > 0) {
                return R.error("请先删除子菜单或按钮");
            }
        }
        for (Long id : ids) {
            sysMenuService.delete(id);
        }
        return R.ok();
    }

    /**
     * 验证参数是否正确
     */
    private void verifyForm(SysMenuEntity menu) {
        if (StringUtils.isBlank(menu.getName())) {
            throw new RRException("菜单名称不能为空");
        }

        if (menu.getPid() == null) {
            throw new RRException("上级菜单不能为空");
        }

        //菜单
        if (menu.getType() == Constant.MenuType.MENU.getValue()) {
            if (StringUtils.isBlank(menu.getUrl())) {
                throw new RRException("菜单URL不能为空");
            }
        }

        //上级菜单类型
        int parentType = Constant.MenuType.CATALOG.getValue();
        if (menu.getPid() != 0) {
            SysMenuEntity parentMenu = sysMenuService.getById(menu.getPid());
            parentType = parentMenu.getType();
        }

        //目录、菜单
        if (menu.getType() == Constant.MenuType.CATALOG.getValue() ||
                menu.getType() == Constant.MenuType.MENU.getValue()) {
            if (parentType != Constant.MenuType.CATALOG.getValue()) {
                throw new RRException("上级菜单只能为目录类型");
            }
            return;
        }

        //按钮
        if (menu.getType() == Constant.MenuType.BUTTON.getValue()) {
            if (parentType != Constant.MenuType.MENU.getValue()) {
                throw new RRException("上级菜单只能为菜单类型");
            }
            return;
        }
    }
}
