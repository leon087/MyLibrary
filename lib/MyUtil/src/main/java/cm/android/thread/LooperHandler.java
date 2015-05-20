package cm.android.thread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

/**
 * Looper线程
 */
public class LooperHandler {

    private Handler mTaskExecutor;

    private MyHandlerThread mHandlerThread;

    // private android.os.Handler.Callback callback;

    /**
     * 创建一个Loop线程对象
     */
    public LooperHandler() {
    }

    private static String generateTag(StackTraceElement caller) {
        String tag = "%s:%s:%d";
        tag = String.format(tag, caller.getFileName(), caller.getMethodName(),
                caller.getLineNumber());
        return tag;
    }

    public void initialize(android.os.Handler.Callback callback) {
        // this.callback = callback;
        StackTraceElement stackTraceElement = Thread.currentThread()
                .getStackTrace()[3];
        mHandlerThread = new MyHandlerThread(generateTag(stackTraceElement));
        mHandlerThread.start();
        mTaskExecutor = new MyHandler(mHandlerThread.getLooper(), callback);
    }

    /**
     * 释放资源
     */
    public void release() {
        mTaskExecutor.removeCallbacks(mHandlerThread);
        mTaskExecutor.getLooper().quit();
        mTaskExecutor = null;
        mHandlerThread = null;
        // callback = null;
    }

    /**
     * 在Loop线程中执行task
     */
    public void post(Runnable task) {
        mTaskExecutor.post(task);
    }

    /**
     * 在Loop线程中延迟执行task
     */
    public void postDelayed(Runnable task, long delayMillis) {
        mTaskExecutor.postDelayed(task, delayMillis);
    }

    /**
     * 向loop线程中发送消息
     */
    public void sendMessage(int what, Object object) {
        Message msg = mTaskExecutor.obtainMessage(what, object);
        mTaskExecutor.sendMessage(msg);
    }

    /**
     * 向loop线程发送空消息
     */
    public void sendEmptyMessage(int what) {
        mTaskExecutor.removeMessages(what);
        mTaskExecutor.sendEmptyMessage(what);
    }

    /**
     * 延迟发送空消息
     */
    public void sendEmptyMessageDelayed(int what, long delayMillis) {
        mTaskExecutor.sendEmptyMessageDelayed(what, delayMillis);
    }

    /**
     * 延迟发送消息
     */
    public void sendMessageDelayed(int what, Object obj, long delayMillis) {
        Message msg = mTaskExecutor.obtainMessage(what, obj);
        mTaskExecutor.sendMessageDelayed(msg, delayMillis);
    }

    /**
     * 移除消息
     */
    public void removeMessages(int what) {
        mTaskExecutor.removeMessages(what);
    }

    private class MyHandlerThread extends HandlerThread {

        public MyHandlerThread(String name) {
            super(name);
        }

        @Override
        public void start() {
            super.start();
        }

        @Override
        public void run() {
            super.run();
        }
    }

    private class MyHandler extends Handler {

        private Callback callback;

        private MyHandler(Looper looper, Callback callback) {
            super(looper);
            this.callback = callback;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null != callback) {
                callback.handleMessage(msg);
            }
        }
    }
}
