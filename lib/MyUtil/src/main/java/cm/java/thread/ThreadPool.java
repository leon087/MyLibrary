package cm.java.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池 <br>
 * 在JDK帮助文档中，有如此一段话：<br>
 * “强烈建议程序员使用较为方便的 Executors 工厂方法 Executors.newCachedThreadPool
 * ()（无界线程池，可以进行自动线程回收）、Executors.newFixedThreadPool(int)（固定大小线程池）和
 * Executors.newSingleThreadExecutor()（单个后台线程），它们均为大多数使用场景预定义了设置。”
 */
public class ThreadPool {

    private final ThreadPoolExecutor executor;

    private final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();

    private static final int KEEP_ALIVE_TIME = 10 * 1000;

    public ThreadPool(int corePoolSize, int maximumPoolSize) {
        RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, workQueue, Executors.defaultThreadFactory(),
                rejectedExecutionHandler);
    }

    public ThreadPool(int corePoolSize, int maximumPoolSize, ThreadFactory threadFactory) {
        RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, workQueue, threadFactory,
                rejectedExecutionHandler);
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    /**
     * 执行一个task
     */
    public void execute(Runnable worker) {
        executor.execute(worker);
    }

    /**
     * 获取正在执行的task数量
     */
    public long getActiveCount() {
        return executor.getActiveCount();
        // return mExecutor.getTaskCount();
    }

    /**
     * 释放资源
     */
    public void release() {
        workQueue.clear();
        executor.shutdown();
    }

    /**
     * 移除task
     */
    public void remove(Runnable task) {
        executor.remove(task);
    }
}
