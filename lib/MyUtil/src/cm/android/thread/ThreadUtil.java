package cm.android.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtil {
	/**
	 * 休眠一段时间
	 * 
	 * @param time
	 */
	public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static ExecutorService newFixedThreadPool(int nThreads) {
		return Executors.newFixedThreadPool(nThreads);
	}

	public static ExecutorService newCachedThreadPool() {
		return Executors.newCachedThreadPool();
	}

	public static ExecutorService newSingleThreadExecutor() {
		return Executors.newSingleThreadExecutor();
	}
}
