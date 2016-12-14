package cm.android.sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class CloseableHandler {
    private static final Logger logger = LoggerFactory.getLogger("CloseableHandler");

    public final void open() {
        getReal().open();
    }

    public final void close() {
        getReal().close();
    }

    protected void handleMessage(Message message) {
    }

    private Handler handler = null;

    public CloseableHandler() {
        handler = new CloseableHandler.InnerHandler(this);
    }

    public CloseableHandler(Looper looper) {
        handler = new CloseableHandler.InnerHandler(this, looper);
    }

    public Handler get() {
        return handler;
    }

    private InnerHandler getReal() {
        return ((InnerHandler) get());
    }

    private static class InnerHandler extends Handler {

        private WeakReference<CloseableHandler> mOuter;
        private volatile AtomicBoolean close = new AtomicBoolean(false);

        public InnerHandler(CloseableHandler callback) {
            super();
            mOuter = new WeakReference<>(callback);
        }

        public InnerHandler(CloseableHandler callback, Looper looper) {
            super(looper);
            mOuter = new WeakReference<>(callback);
        }

        @Override
        public final void handleMessage(Message msg) {
            synchronized (this) {
                if (close.get()) {
                    logger.info("handleMessage:close = {}", close.get());
                    return;
                }
                CloseableHandler outer = mOuter.get();
                if (outer != null) {
                    outer.handleMessage(msg);
                }
            }
        }

        private final void open() {
            synchronized (this) {
                close.set(false);
            }
        }

        private final void close() {
            synchronized (this) {
                close.set(true);
            }
            this.removeCallbacksAndMessages(null);
        }

        @Override
        public final void dispatchMessage(Message msg) {
            if (close.get()) {
                logger.info("dispatchMessage:close = {}", close.get());
                return;
            }
            super.dispatchMessage(msg);
        }

        @Override
        public final boolean sendMessageAtTime(Message msg, long uptimeMillis) {
            synchronized (this) {
                if (close.get()) {
                    logger.info("sendMessageAtTime:close = {}", close.get());
                    return false;
                }
            }
            return super.sendMessageAtTime(msg, uptimeMillis);
        }
    }
}
