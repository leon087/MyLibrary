package cm.android.framework.ext.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

import cm.android.framework.ext.util.MyIntent;
import cm.android.util.ExitHolder;

public class BaseActivity extends Activity {

    private static final Logger logger = LoggerFactory.getLogger(BaseActivity.class);

    protected final Bundle bundleBak = new Bundle();

    private final ExitHolder exitHolder = new ExitHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyIntent.restore(savedInstanceState, bundleBak);
        exitHolder.init(this, new ExitHolder.IExitActivity() {
            @Override
            public void exitActivity() {
                BaseActivity.this.finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        exitHolder.deInit(this);
        super.onDestroy();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        MyIntent.restore(savedInstanceState, bundleBak);
        logger.info("bundleBak = " + bundleBak);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        MyIntent.backup(outState, bundleBak);
        super.onSaveInstanceState(outState);
        logger.info("bundleBak = " + bundleBak);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        logger.info("newConfig = " + newConfig);
    }
}
