package com.zxw.springbootinit.manager;

import com.zxw.springbootinit.common.ErrorCode;
import com.zxw.springbootinit.exception.BusinessException;
import org.redisson.Redisson;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 专门提供 RedisLimiter 限流基础服务的**(提供了通用的能力，可以放到任何一个项目模板里)
 */
@Service
public class RedisLimiterManager {
    @Resource
    private RedissonClient redissonClient;

    public void doRateLimit(String key) {
        // 创建 RateLimiter 对象
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);

        // 初始化限流器：每秒最多5个令牌
        rateLimiter.trySetRate(RateType.OVERALL, 5, 1, RateIntervalUnit.SECONDS);

        //每当一个用户请求的时候，通过 tryAcquire() 方法获取一个令牌，如果获取不到，告诉用户请求过于频繁
        if (!rateLimiter.tryAcquire(1)) {
            throw new BusinessException(ErrorCode.TO_MANY_REQUEST,"请求过于频繁！");
        }
    }
}
