package com.fengwenyi.session1

import org.springframework.context.annotation.Configuration
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession

/**
 *
 * @author Wenyi Feng
 */
@Configuration
@EnableRedisHttpSession
class RedisSessionConfig {
}