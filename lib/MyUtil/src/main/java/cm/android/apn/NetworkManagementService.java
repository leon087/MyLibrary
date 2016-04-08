/**
 *
 */

package cm.android.apn;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import java.util.ArrayList;

public class NetworkManagementService extends Service {

    /**
     * Command to the service to register a client, receiving callbacks from the
     * service. The Message's replyTo field must be a Messenger of the client
     * where callbacks should be sent.
     */
    private static final int MSG_REGISTER_CLIENT = 1;

    /**
     * Command to the service to unregister a client, to stop receiving
     * callbacks from the service. The Message's replyTo field must be a
     * Messenger of the client as previously given with MSG_REGISTER_CLIENT.
     */
    private static final int MSG_UNREGISTER_CLIENT = 2;

    /**
     * Command to the service to issue a instruction to the network manager
     * corresponding to the argument sent with the message (explained below).
     */
    private static final int MSG_SET_VALUE = 3;

    /**
     * Command to the network manager to turn connectivity on
     */
    public static final int NM_CONNECTIVITY_ON_VALUE = 4;

    /**
     * Command to the network manager to turn connectivity off
     */
    public static final int NM_CONNECTIVITY_OFF_VALUE = 5;

    /** Keeps track of all current registered clients. */
    ArrayList<Messenger> mClients = new ArrayList<Messenger>();

    /** Work horse of service turning network on and off */
    private NetworkManager mNetworkManager;

    /** Holds last value set by a client. */
    private int mValue = 0;

    /**
     * Handler of incoming messages from clients.
     */
    private class IncomingHandler extends Handler {

        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_REGISTER_CLIENT:
                    mClients.add(message.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(message.replyTo);
                    break;
                case MSG_SET_VALUE:
                    mValue = message.arg1;
                    for (int i = mClients.size() - 1; i >= 0; i--) {
                        switch (mValue) {
                            case NM_CONNECTIVITY_ON_VALUE:
                                mNetworkManager.enableNetwork();
                                break;
                            case NM_CONNECTIVITY_OFF_VALUE:
                                mNetworkManager.disableNetwork();
                                break;
                            default:
                                break;
                        }
                        try {
                            mClients.get(i).send(Message.obtain(null, MSG_SET_VALUE, mValue, 0));
                        } catch (RemoteException e) {
                            // The client is dead. Remove it from the list;
                            // we are going through the list from back to front
                            // so this is safe to do inside the loop.
                            mClients.remove(i);
                        }
                    }
                    break;
                default:
                    super.handleMessage(message);
            }
        }
    }

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    @Override
    public void onCreate() {
        mNetworkManager = new NetworkManager(this);
    }

    /**
     * When binding to the service we return an instance of the messanger
     */
    @Override
    public IBinder onBind(Intent arg0) {
        return mMessenger.getBinder();
    }

}
