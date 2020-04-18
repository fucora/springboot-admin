package com.mm.modules.job.controller;

import com.mm.common.utils.PageUtil;
import com.mm.modules.job.service.ScheduleJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 定时任务日志
 *
 * @author lwl
 */
@RestController
@RequestMapping("/sys/scheduleLog")
public class ScheduleJobLogController {
    @Autowired
    private ScheduleJobLogService scheduleJobLogService;

    /**
     * 定时任务日志列表
     */
    @RequestMapping("/list")
    @PreAuthorize("hasAnyRole('sys:schedule:log')")
    public PageUtil list(@RequestParam Map<String, Object> params) {
        return scheduleJobLogService.queryPage(params);
    }
}
