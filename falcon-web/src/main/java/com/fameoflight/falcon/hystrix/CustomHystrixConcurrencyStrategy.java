package com.fameoflight.falcon.hystrix;

import com.fameoflight.falcon.utils.UserContextHolder;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableLifecycle;
import com.netflix.hystrix.strategy.properties.HystrixProperty;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CustomHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {

	// https://www.slideshare.net/ufried/resilience-with-hystrix
	private HystrixConcurrencyStrategy customStrategy;

	public CustomHystrixConcurrencyStrategy(HystrixConcurrencyStrategy customStrategy) {
		this.customStrategy = customStrategy;
	}
	
	@Override
	public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey, HystrixProperty<Integer> corePoolSize,
                                            HystrixProperty<Integer> maximumPoolSize, HystrixProperty<Integer> keepAliveTime, TimeUnit unit,
                                            BlockingQueue<Runnable> workQueue) {
		return (customStrategy != null) ?
				customStrategy.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue) :
				super.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override
	public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
                                            HystrixThreadPoolProperties threadPoolProperties) {
		return (customStrategy != null) ?
				customStrategy.getThreadPool(threadPoolKey, threadPoolProperties) :
				super.getThreadPool(threadPoolKey, threadPoolProperties);
	}

	@Override
	public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {
		return  (customStrategy != null) ?
				customStrategy.getBlockingQueue(maxQueueSize) :
				super.getBlockingQueue(maxQueueSize);
	}

	@Override
	public <T> Callable<T> wrapCallable(Callable<T> callable) {
		return (customStrategy != null) ?
				customStrategy.wrapCallable(new DelegatingUserContextCallable<T>(callable, UserContextHolder.getContext())) :
				super.wrapCallable(new DelegatingUserContextCallable<T>(callable, UserContextHolder.getContext()));
	}

	@Override
	public <T> HystrixRequestVariable<T> getRequestVariable(HystrixRequestVariableLifecycle<T> rv) {
		return ( customStrategy != null) ?
				customStrategy.getRequestVariable(rv) :
				super.getRequestVariable(rv);
	}

}
