
package cm.android.apn;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A utility available to control APN settings The utility object requires a
 * {@link Context} object to access the
 * {@link ContentResolverandroid.content.ContentResolver} object which modifies
 * the APN settings The manifest file of the project which calls the utility
 * must have the following permissions: <uses-permission
 * android:name="android.permission.WRITE_APN_SETTINGS" /> As it is defined in
 * the manifest file for this project and must be able to pass it's context. If
 * it's an {@link android.test.ActivityInstrumentationTestCase2} Then it
 * requires the following code in the class mContext =
 * getInstrumentation().getContext(); If it's an
 * {@link android.test.AndroidTestCase} Then it requires the following code in
 * the class mContext = getContext();
 *
 * @author whatsthebeef
 */
public class NetworkManager {

    /**
     * Object used to interact with APN
     */
    private ApnDao mApnDao;

    /**
     * Context used to interface with APN settings
     */
    private Context mContext;

    private ConnectivityManager mConnectivityManager;

    private NetworkInfo mNetworkInfo;

    private SynchronizableBoolean mDataConnectionStatus;

    /**
     * Object class allows it to be locked without problem of JVM interning
     * the object
     *
     * @author wtb
     */
    private class SynchronizableBoolean {

        /**
         * The constructor gets the desired reference to pull the information
         */
        public SynchronizableBoolean() {
            mConnectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
        }

        /**
         * @return boolean, is there a data connection
         */
        public boolean isDataConnection() {
            mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            return (mNetworkInfo == null || !mNetworkInfo.isConnected()) ? false : true;
        }
    }

    /**
     * Constructor requires context belonging to an application with sufficient
     * privileges as described in class definition
     */
    public NetworkManager(Context aContext) {
        mContext = aContext;
        init();
    }

    /**
     * Initializes object used to interact with APN
     */
    private void init() {
        mApnDao = new ApnDao(mContext.getContentResolver());
        mDataConnectionStatus = new SynchronizableBoolean();

    }

    /**
     * Method used to switch the connectivity on
     *
     * @return true if operation was successful and network is enable
     */
    public void enableNetwork() {
        Timer connectivityChangeDetectonTimer = new Timer();
        connectivityChangeDetectonTimer.scheduleAtFixedRate(
                new ConnectivityChangeDetectionTimerTask(), 1000, 1000);
        mApnDao.setDataEnabled(true);
        while (!mDataConnectionStatus.isDataConnection()) {
            try {
                synchronized (mDataConnectionStatus) {
                    mDataConnectionStatus.wait();
                }
            } catch (InterruptedException e) {
                // If interrupt doesn't change connectivity carry on
            }
        }
        connectivityChangeDetectonTimer.cancel();
    }

    /**
     * Method used to switch the connectivity off
     *
     * @return true if operation was successful and network is disabled
     */
    public void disableNetwork() {
        Timer connectivityChangeDetectonTimer = new Timer();
        connectivityChangeDetectonTimer.scheduleAtFixedRate(
                new ConnectivityChangeDetectionTimerTask(), 1000, 1000);
        mApnDao.setDataEnabled(false);
        while (mDataConnectionStatus.isDataConnection()) {
            try {
                synchronized (mDataConnectionStatus) {
                    mDataConnectionStatus.wait();
                }
            } catch (InterruptedException e) {
                // If interrupt doesn't change connectivity carry on
            }
        }
        connectivityChangeDetectonTimer.cancel();
    }

    /**
     * Task is used by timer to wake up and check connectivity
     *
     * @author wtb
     */
    private class ConnectivityChangeDetectionTimerTask extends TimerTask {

        @Override
        public void run() {
            synchronized (mDataConnectionStatus) {
                mDataConnectionStatus.notifyAll();
            }
            cancel();
        }
    }
}
