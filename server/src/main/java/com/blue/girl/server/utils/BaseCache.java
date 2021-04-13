package com.blue.girl.server.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author msli
 * @Date 2021/04/13
 */

@Component
public class BaseCache {

    private Cache<String, Object> tenHoursCache = CacheBuilder.newBuilder()
            // 设置缓存初始容量大小，后续会自动扩容
            .initialCapacity(20)
            // 最大值
            .maximumSize(200)
            // 并发数
            .concurrencyLevel(5)
            // 过期时间
            .expireAfterWrite(10, TimeUnit.HOURS)
            // 统计命中率
            .recordStats()
            .build();

    public Cache<String, Object> getTenHoursCache() {
        return tenHoursCache;
    }

    public void setTenHoursCache(Cache<String, Object> tenHoursCache) {
        this.tenHoursCache = tenHoursCache;
    }

}
