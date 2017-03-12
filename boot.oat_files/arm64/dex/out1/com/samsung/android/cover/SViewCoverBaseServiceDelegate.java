package com.samsung.android.cover;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.UserHandle;
import android.util.Log;
import android.util.Pair;
import com.samsung.android.cover.ISViewCoverBaseService.Stub;
import java.util.ArrayList;

public class SViewCoverBaseServiceDelegate {
    private static final boolean DEBUG = true;
    private static final boolean SAFE_DEBUG = true;
    private static final String SVIEWCOVERBASE_CLASS = "com.android.systemui.cover.SViewCoverService";
    private static final String SVIEWCOVERBASE_PACKAGE = "com.android.systemui";
    private static final int SVIEWCOVER_UPDATE_COVERSTATE = 1;
    private static final String TAG = "SViewCoverBaseServiceDelegate";
    private Context mContext;
    private boolean mIsBound = false;
    private ArrayList<Pair<Integer, Object>> mPendingCommand = new ArrayList();
    protected SViewCoverBaseServiceWrapper mSViewCoverBaseService;
    private SViewCoverBaseState mSViewCoverBaseState = new SViewCoverBaseState();
    private final ServiceConnection mSViewCoverConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            SViewCoverBaseServiceDelegate.this.mSViewCoverBaseService = new SViewCoverBaseServiceWrapper(Stub.asInterface(service));
            if (SViewCoverBaseServiceDelegate.this.mSViewCoverBaseState.systemIsReady) {
                SViewCoverBaseServiceDelegate.this.mSViewCoverBaseService.onSystemReady();
            }
            int N = SViewCoverBaseServiceDelegate.this.mPendingCommand.size();
            for (int i = 0; i < N; i++) {
                Pair<Integer, Object> command = (Pair) SViewCoverBaseServiceDelegate.this.mPendingCommand.get(i);
                switch (((Integer) command.first).intValue()) {
                    case 1:
                        SViewCoverBaseServiceDelegate.this.mSViewCoverBaseService.updateCoverState((CoverState) command.second);
                        break;
                    default:
                        break;
                }
            }
            SViewCoverBaseServiceDelegate.this.mPendingCommand.clear();
        }

        public void onServiceDisconnected(ComponentName name) {
            SViewCoverBaseServiceDelegate.this.mSViewCoverBaseService = null;
        }
    };
    private Intent mSViewCoverIntent;

    static final class SViewCoverBaseState {
        boolean systemIsReady;

        SViewCoverBaseState() {
        }
    }

    public SViewCoverBaseServiceDelegate(Context context) {
        this.mContext = context;
        this.mSViewCoverIntent = new Intent();
        this.mSViewCoverIntent.setClassName(SVIEWCOVERBASE_PACKAGE, SVIEWCOVERBASE_CLASS);
    }

    public void onBindSViewCoverService() {
        if (this.mIsBound) {
            Log.d(TAG, "*** SViewCoverBase : already started");
        } else if (this.mContext.bindServiceAsUser(this.mSViewCoverIntent, this.mSViewCoverConnection, 1, UserHandle.OWNER)) {
            this.mIsBound = true;
            Log.d(TAG, "*** SViewCoverBase : started");
        } else {
            this.mIsBound = false;
            Log.d(TAG, "*** SViewCoverBase : can't bind to com.android.systemui.cover.SViewCoverService");
        }
    }

    public void onUnbindSViewCoverService() {
        if (this.mIsBound) {
            this.mContext.unbindService(this.mSViewCoverConnection);
            this.mSViewCoverBaseService = null;
            this.mIsBound = false;
            Log.d(TAG, "*** SViewCoverBase : unbind");
            return;
        }
        Log.d(TAG, "*** SViewCoverBase : can't unbind. It already unbound");
    }

    public void onSystemReady() {
        if (this.mSViewCoverBaseService != null) {
            this.mSViewCoverBaseService.onSystemReady();
        } else {
            this.mSViewCoverBaseState.systemIsReady = true;
        }
    }

    public void onSViewCoverShow() {
        if (this.mSViewCoverBaseService != null) {
            this.mSViewCoverBaseService.onSViewCoverShow();
        }
    }

    public void onSViewCoverHide() {
        if (this.mSViewCoverBaseService != null) {
            this.mSViewCoverBaseService.onSViewCoverHide();
        }
    }

    public void updateCoverState(CoverState state) {
        if (this.mSViewCoverBaseService != null) {
            this.mSViewCoverBaseService.updateCoverState(state);
        } else if (this.mIsBound) {
            this.mPendingCommand.add(new Pair(Integer.valueOf(1), state));
        }
    }

    public boolean isCoverViewShowing() {
        if (this.mSViewCoverBaseService != null) {
            return this.mSViewCoverBaseService.isCoverViewShowing();
        }
        return false;
    }

    public int onCoverAppCovered(boolean covered) {
        if (this.mSViewCoverBaseService != null) {
            return this.mSViewCoverBaseService.onCoverAppCovered(covered);
        }
        return 0;
    }
}
