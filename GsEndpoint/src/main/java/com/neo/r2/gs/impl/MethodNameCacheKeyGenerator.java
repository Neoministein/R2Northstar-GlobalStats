package com.neo.r2.gs.impl;

import com.neo.util.framework.api.cache.CacheKeyGenerator;
import com.neo.util.framework.api.cache.spi.CompositeCacheKey;

import java.lang.reflect.Method;

public class MethodNameCacheKeyGenerator implements CacheKeyGenerator {

    @Override
    public Object generate(Method method, Object... methodParams) {
        return new CompositeCacheKey(method.getName(), methodParams);
    }
}
