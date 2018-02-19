package com.fynn.smsforwarder.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程处理
 *
 * @author Fynn
 * @date 18/2/9
 */
public final class ThreadPool {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));

    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;

    /**
     * An object that executes submitted {@link Runnable} tasks.
     */
    private ExecutorService executor;

    public ThreadPool() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("sms-pool-%d").build();
        executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1024),
                namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

    public static ThreadPool getInstance() {
        return ThreadPoolHolder.INSTANCE;
    }

    /**
     * 执行一个任务
     *
     * @param r
     */
    public void execute(Runnable r) {
        executor.execute(r);
    }

    public void shutdown() {
        if (executor.isShutdown()) {
            return;
        }

        executor.shutdown();
    }

    private static class ThreadPoolHolder {
        private static final ThreadPool INSTANCE = new ThreadPool();
    }
}
