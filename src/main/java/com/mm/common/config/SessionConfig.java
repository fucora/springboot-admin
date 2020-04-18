package com.mm.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * session 配置
 * Session失效时间 默认1800 改为 一天（86400）
 *
 * @author: lwl
 **/
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400)
public class SessionConfig {
}
