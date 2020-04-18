package com.mm.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mm.common.utils.PageUtil;
import com.mm.common.utils.Query;
import com.mm.modules.sys.dao.SysLogDao;
import com.mm.modules.sys.entity.SysLogEntity;
import com.mm.modules.sys.service.SysLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("sysLogService")
public class SysLogServiceImpl extends ServiceImpl<SysLogDao, SysLogEntity> implements SysLogService {

    @Override
    public PageUtil queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        LambdaQueryWrapper<SysLogEntity> qw = new LambdaQueryWrapper<>();
        qw.like(StringUtils.isNotBlank(key), SysLogEntity::getUsername, key);
        IPage<SysLogEntity> page = this.page(new Query<SysLogEntity>().getPage(params), qw);
        return new PageUtil(page);
    }
}
