package android.lsm;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.storage.sensitive.SDServiceAPI;
import android.os.storage.sensitive.SDServiceAPI.Stub;
import android.util.Log;
import com.android.internal.widget.LockPatternUtils;
import java.io.UnsupportedEncodingException;

public class LockedStatePasswordWrapper {
    public static final String SERVICE_NAME = "LSManager";
    private static final String TAG = "LSManager.LockedStatePasswordWrapper";
    private Context mContext = null;
    private SDServiceAPI m_API = null;

    public LockedStatePasswordWrapper(Context context) {
        this.mContext = context;
        IBinder m_obj = ServiceManager.getService(SERVICE_NAME);
        if (m_obj == null) {
            Log.e(TAG, "Unable to get LSManager service...");
        }
        this.m_API = Stub.asInterface(m_obj);
        if (this.m_API == null) {
            Log.e(TAG, "Unable to get SDServiceAPI instance.");
        }
    }

    public int getCurrentUserID() {
        return new LockPatternUtils(this.mContext).getCurrentUser();
    }

    public int setPassword(String password) {
        if (this.m_API == null) {
            return -1;
        }
        if (password != null) {
            byte[] bytes;
            try {
                bytes = password.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "Unsupported encoding exception.");
                return -1;
            }
            try {
                this.m_API.setPassword(bytes);
                for (int i = 0; i < bytes.length; i++) {
                    bytes[i] = (byte) 0;
                }
            } catch (RemoteException e2) {
                Log.e(TAG, "Unable to communicate with LSManager");
            }
        } else {
            this.m_API.setPassword(new byte[0]);
        }
        return 0;
    }

    public int setLocked() {
        if (this.m_API == null) {
            return -1;
        }
        try {
            this.m_API.setLocked();
        } catch (RemoteException e) {
            Log.e(TAG, "Unable to communicate with LSManager");
        }
        return 0;
    }
}
