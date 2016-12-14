package cm.android.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Looper;
import android.os.Message;

import java.util.concurrent.atomic.AtomicBoolean;

import cm.android.sdk.WeakHandler;
import cm.java.thread.SimpleLock;

public class AsyncHandler extends WeakHandler {
    private static final Logger logger = LoggerFactory.getLogger("thread");

    public AsyncHandler(Looper looper) {
        super(looper);
        if (looper == Looper.getMainLooper()) {
            logger.error("looper == Looper.getMainLooper()");
        }
    }

    @Override
    public final void handleMessage(Message message) {
        logger.info("{}:handleMessage:begin", this);
        handleFlag.set(true);
        try {
            //
            handleMessageImpl(message);
        } finally {
            handleFlag.set(false);
            lock.signalAll();
            logger.info("{}:handleMessage:end", this);
        }
    }

    protected void handleMessageImpl(Message message) {
    }

    private volatile AtomicBoolean handleFlag = new AtomicBoolean(false);
    private final SimpleLock lock = new SimpleLock();

    public void exit() {
        this.get().removeCallbacksAndMessages(null);
        logger.info("{}:exit:begin", this);
        while (handleFlag.get()) {
            lock.await();
        }
        logger.info("{}:exit:end", this);
    }
}
