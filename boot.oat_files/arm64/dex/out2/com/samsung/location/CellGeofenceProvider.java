package com.samsung.location;

import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.samsung.location.ISLocationCellInterface.Stub;

public class CellGeofenceProvider {
    private static final String TAG = "CellGeofenceProvider";
    private static boolean mEnabled;
    private ISLocationCellInterface mSGeofenceCellInterface = new Stub() {
        public void initCellGeofence(int state) {
            CellGeofenceProvider.this.native_init_cell_geofence(state);
        }

        public void addCellGeofence(int geofenceId) {
            CellGeofenceProvider.this.native_add_cell_geofence(geofenceId);
        }

        public void enableCellGeofence(int geofenceId, int geofenceState) {
            CellGeofenceProvider.this.native_enable_cell_geofence(geofenceId, geofenceState);
        }

        public void removeCellGeofence(int geofenceId) {
            CellGeofenceProvider.this.native_remove_cell_geofence(geofenceId);
        }

        public void startCollectCell(int geofenceId) {
            CellGeofenceProvider.this.native_start_collect_cell(geofenceId);
        }

        public void stopCollectCell(int geofenceId) {
            CellGeofenceProvider.this.native_stop_collect_cell(geofenceId);
        }

        public void syncCellGeofence(int[] geofenceIdArray, int geofenceIdArraySize, int[] enabledIdArray, int enabledIdSize) {
            CellGeofenceProvider.this.native_sync_cell_geofence(geofenceIdArray, geofenceIdArraySize, enabledIdArray, enabledIdSize);
        }
    };

    private static native boolean class_init_native();

    private native void native_add_cell_geofence(int i);

    private native void native_cleanup_cell_geofence();

    private native void native_enable_cell_geofence(int i, int i2);

    private native boolean native_init();

    private native void native_init_cell_geofence(int i);

    private native void native_remove_cell_geofence(int i);

    private native void native_start_collect_cell(int i);

    private native void native_stop_collect_cell(int i);

    private native void native_sync_cell_geofence(int[] iArr, int i, int[] iArr2, int i2);

    static {
        mEnabled = false;
        mEnabled = class_init_native();
    }

    public void enable() {
        Log.d(TAG, "CellGeofenceProvider is enabled");
        if (native_init()) {
            ISLocationManager mService = ISLocationManager.Stub.asInterface(ServiceManager.getService("sec_location"));
            if (mService != null) {
                try {
                    mService.setGeofenceCellInterface(getSGeofenceCellInterface());
                    return;
                } catch (RemoteException e) {
                    Log.e(TAG, e.toString());
                    return;
                }
            }
            return;
        }
        Log.d(TAG, "CellGeofenceProvider enable is failed....");
    }

    public void disable() {
        Log.d(TAG, "CellGeofenceProvider is disabled");
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public ISLocationCellInterface getSGeofenceCellInterface() {
        return this.mSGeofenceCellInterface;
    }

    private void reportCellGeofenceDetected(int area_inout, int geofenceId) {
        ISLocationManager mService = ISLocationManager.Stub.asInterface(ServiceManager.getService("sec_location"));
        if (mService != null) {
            try {
                mService.reportCellGeofenceDetected(geofenceId, area_inout);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    private void reportCellGeofenceRequestFail(int geofenceId) {
        ISLocationManager mService = ISLocationManager.Stub.asInterface(ServiceManager.getService("sec_location"));
        if (mService != null) {
            try {
                mService.reportCellGeofenceRequestFail(geofenceId);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        }
    }
}
