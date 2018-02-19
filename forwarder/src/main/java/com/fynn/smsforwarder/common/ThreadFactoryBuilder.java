package com.fynn.smsforwarder.common;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author lifs
 * @date 18/2/9
 */
public class ThreadFactoryBuilder {

    /**
     * 线程命名格式
     */
    private String nameFormat;

    /**
     * 是否后台线程
     */
    private Boolean daemon;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 未知异常捕获
     */
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    /**
     * 创建一个新的 {@link ThreadFactory} 构造器.
     */
    public ThreadFactoryBuilder() {
    }

    /**
     * check if object is null
     *
     * @param object
     * @param <T>
     * @return
     */
    static <T> T checkNotNull(T object) {
        if (object == null) {
            throw new NullPointerException();
        }
        return object;
    }

    /**
     * 构造 ThreadFactory
     *
     * @param builder
     * @return
     */
    private static ThreadFactory build(ThreadFactoryBuilder builder) {
        final String nameFormat = builder.nameFormat;
        final Boolean daemon = builder.daemon;
        final Integer priority = builder.priority;
        final Thread.UncaughtExceptionHandler uncaughtExceptionHandler =
                builder.uncaughtExceptionHandler;

        final ThreadFactory factory = Executors.defaultThreadFactory();
        final AtomicLong count = nameFormat != null ? new AtomicLong(0) : null;

        return new ThreadFactory() {

            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = factory.newThread(runnable);

                if (nameFormat != null) {
                    thread.setName(String.format(nameFormat, count.getAndIncrement()));
                }

                if (daemon != null) {
                    thread.setDaemon(daemon);
                }

                if (priority != null) {
                    thread.setPriority(priority);
                }

                if (uncaughtExceptionHandler != null) {
                    thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
                }

                return thread;
            }
        };
    }

    /**
     * 设置命名格式，用于 ({@link Thread#setName})
     *
     * @param nameFormat
     * @return
     */
    public ThreadFactoryBuilder setNameFormat(String nameFormat) {
        // 仅作格式检验
        String.format(nameFormat, 0);
        this.nameFormat = nameFormat;
        return this;
    }

    /**
     * 为新线程设置是否为守护线程
     */
    public ThreadFactoryBuilder setDaemon(boolean daemon) {
        this.daemon = daemon;
        return this;
    }

    /**
     * 设置线程优先级
     *
     * @param priority
     * @return
     */
    public ThreadFactoryBuilder setPriority(int priority) {
        checkArgument(priority >= Thread.MIN_PRIORITY,
                String.format("Thread priority (%s) must be >= %s", priority, Thread.MIN_PRIORITY));
        checkArgument(priority <= Thread.MAX_PRIORITY,
                String.format("Thread priority (%s) must be <= %s", priority, Thread.MAX_PRIORITY));
        this.priority = priority;
        return this;
    }

    /**
     * 预检查参数合法性
     *
     * @param within
     * @param message
     */
    private void checkArgument(boolean within, String message) {
        if (!within) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 设置未知异常处理
     *
     * @param uncaughtExceptionHandler
     * @return
     */
    public ThreadFactoryBuilder setUncaughtExceptionHandler(
            Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.uncaughtExceptionHandler = checkNotNull(uncaughtExceptionHandler);
        return this;
    }

    /**
     * build a ThreadFactory
     *
     * @return
     */
    public ThreadFactory build() {
        return build(this);
    }
}
