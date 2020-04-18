package com.mm.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mm.common.utils.PageUtil;
import com.mm.modules.sys.entity.SysLogEntity;

import java.util.Map;


/**
 * 系统日志
 *
 * @author lwl
 */
public interface SysLogService extends IService<SysLogEntity> {

    PageUtil queryPage(Map<String, Object> params);

}
