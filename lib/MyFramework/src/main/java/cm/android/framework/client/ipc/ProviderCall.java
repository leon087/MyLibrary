package cm.android.framework.client.ipc;

import android.annotation.TargetApi;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Parcelable;
import android.os.RemoteException;

import java.io.Serializable;

import cm.android.framework.client.core.LogUtil;

public final class ProviderCall {

//    public static Bundle call(String authority, String methodName, String arg, Bundle bundle) {
//        return call(authority, Framework.get().getBaseContext(), methodName, arg, bundle);
//    }

    @Deprecated
    public static Bundle call2(String authority, Context context, String methodName, String arg, Bundle bundle) {
        Uri uri = Uri.parse("content://" + authority);
        ContentResolver contentResolver = context.getContentResolver();
        return contentResolver.call(uri, methodName, arg, bundle);
    }

    public static void closeQuietly(ContentProviderClient client) {
        if (client == null) {
            return;
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                client.close();
            } else {
                client.release();
            }
        } catch (Exception ignored) {
        }
    }

    public static Bundle call(String authority, Context context, String methodName, String arg, Bundle bundle) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Uri uri = Uri.parse("content://" + authority);
            return context.getContentResolver().call(uri, methodName, arg, bundle);
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                return callUnstable(authority, context, methodName, arg, bundle);
            }
        } catch (DeadObjectException e) {
            LogUtil.getLogger().error(e.getMessage(), e);
        }

        return callStable(authority, context, methodName, arg, bundle);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static Bundle callUnstable(String authority, Context context, String methodName, String arg, Bundle bundle) throws DeadObjectException {
        ContentProviderClient unstableClient = context.getContentResolver().acquireUnstableContentProviderClient(authority);
        if (unstableClient == null) {
            return null;
        }

        try {
            return unstableClient.call(methodName, arg, bundle);
        } catch (DeadObjectException e) {
            throw e;
        } catch (RemoteException e) {
            return null;
        } finally {
            closeQuietly(unstableClient);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static Bundle callStable(String authority, Context context, String methodName, String arg, Bundle bundle) {
        ContentProviderClient stableClient = context.getContentResolver().acquireContentProviderClient(authority);
        if (stableClient == null) {
            return null;
        }

        try {
            return stableClient.call(methodName, arg, bundle);
        } catch (RemoteException e) {
            return null;
        } finally {
            closeQuietly(stableClient);
        }
    }

    public static final class Builder {

        private Context context;

        private Bundle bundle = new Bundle();

        private String methodName;
        private String auth;
        private String arg;

        public Builder(Context context, String auth) {
            this.context = context;
            this.auth = auth;
        }

        public Builder methodName(String name) {
            this.methodName = name;
            return this;
        }

        public Builder arg(String arg) {
            this.arg = arg;
            return this;
        }

        public Builder addArg(Bundle bundle) {
            if (bundle != null) {
                this.bundle.putAll(bundle);
            }
            return this;
        }

        public Builder addArg(String key, Object value) {
            if (value != null) {
                if (value instanceof Boolean) {
                    bundle.putBoolean(key, (Boolean) value);
                } else if (value instanceof Integer) {
                    bundle.putInt(key, (Integer) value);
                } else if (value instanceof String) {
                    bundle.putString(key, (String) value);
                } else if (value instanceof Serializable) {
                    bundle.putSerializable(key, (Serializable) value);
                } else if (value instanceof Bundle) {
                    bundle.putBundle(key, (Bundle) value);
                } else if (value instanceof Parcelable) {
                    bundle.putParcelable(key, (Parcelable) value);
                } else {
                    throw new IllegalArgumentException("Unknown type " + value.getClass() + " in Bundle.");
                }
            }
            return this;
        }

        public Bundle call() {
            return ProviderCall.call(auth, context, methodName, arg, bundle);
        }

    }

}
