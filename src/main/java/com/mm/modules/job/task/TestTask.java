package com.mm.modules.job.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 测试定时任务(演示Demo，可删除)
 * <p>
 * testTask为spring bean的名称
 *
 * @author lwl
 */
@Slf4j
@Component("testTask")
public class TestTask implements ITask {

    @Override
    public void run(String params) {
        log.debug("TestTask定时任务正在执行，参数为：{}", params);
    }
}
