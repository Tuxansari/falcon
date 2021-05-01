package com.fameoflight.falcon.feignUtil;

import com.netflix.hystrix.*;
import feign.hystrix.SetterFactory;

public final class HystrixSetterFactoryBuilder {

    private HystrixSetterFactoryBuilder() {
    }

    public static SetterFactory buildSetterFactory(Integer socketTimeout,
                                                   Integer connectTimeout,
                                                   String groupName,
                                                   Integer maxHystrixThreads) {
        return (target, method) -> HystrixCommand.Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupName))
                .andCommandKey(HystrixCommandKey.Factory.asKey(method.getName()))
                .andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter()
                                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                                .withExecutionTimeoutInMilliseconds(socketTimeout + connectTimeout)
                                .withFallbackEnabled(true))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(groupName))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(maxHystrixThreads));
    }
}
