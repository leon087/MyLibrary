package cm.android.thread;

import java.util.concurrent.*;

/**
 * 线程池 <br>
 * 在JDK帮助文档中，有如此一段话：<br>
 * “强烈建议程序员使用较为方便的 Executors 工厂方法 Executors.newCachedThreadPool
 * ()（无界线程池，可以进行自动线程回收）、Executors.newFixedThreadPool(int)（固定大小线程池）和
 * Executors.newSingleThreadExecutor()（单个后台线程），它们均为大多数使用场景预定义了设置。”
 */
public class ThreadPool {
	private int CORE_POOL_SIZE = 5;
	private int MAX_POOL_SIZE = 5;
	private int KEEP_ALIVE_TIME = 2; // 10 seconds

	private final ThreadPoolExecutor executor;
	private final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();

	public ThreadPool(int corePoolSize, int maximumPoolSize) {
		StackTraceElement stackTraceElement = Thread.currentThread()
				.getStackTrace()[3];
		StringBuilder sb = new StringBuilder();
		sb.append(stackTraceElement.getFileName());
		sb.append(":");
		sb.append(stackTraceElement.getMethodName());
		sb.append(":");
		sb.append(stackTraceElement.getLineNumber());

		RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
		ThreadFactory threadFactory = new PriorityThreadFactory(sb.toString(),
				android.os.Process.THREAD_PRIORITY_LOWEST);
		executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
				KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue, threadFactory,
				rejectedExecutionHandler);
	}

	public Executor getExecutor() {
		return executor;
	}

	/**
	 * 执行一个task
	 * 
	 * @param worker
	 */
	public void execute(Runnable worker) {
		executor.execute(worker);
	}

	/**
	 * 获取正在执行的task数量
	 * 
	 * @return
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
	 * 
	 * @param task
	 */
	public void remove(Runnable task) {
		executor.remove(task);
	}
}
