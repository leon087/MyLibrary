package cm.android.framework.ext.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Fragment;

import java.util.concurrent.atomic.AtomicBoolean;

public class BaseFragment extends Fragment {

    private static Logger logger = LoggerFactory.getLogger("BaseFragment");

    private final AtomicBoolean visible = new AtomicBoolean(false);

    protected void onVisible() {

    }

    protected void onInvisible() {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (getUserVisibleHint()) {
            onVisibleInternal();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        setUserVisibleHint(false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // 该方法先于onStart执行，在tab切换时会触发
        super.setUserVisibleHint(isVisibleToUser);
        if (logger.isDebugEnabled()) {
            logger.debug("isVisibleToUser = " + isVisibleToUser
                    + ",isVisible() = " + isVisible());
        }
        // onStart之后isVisible()会返回true
        if (isVisible()) {
            if (isVisibleToUser) {
                onVisibleInternal();
            } else {
                onInvisibleInternal();
            }
        }
    }

    private void onVisibleInternal() {
        if (visible.compareAndSet(false, true)) {
            onVisible();
        }
    }

    private void onInvisibleInternal() {
        if (visible.compareAndSet(true, false)) {
            onInvisible();
        }
    }
}
