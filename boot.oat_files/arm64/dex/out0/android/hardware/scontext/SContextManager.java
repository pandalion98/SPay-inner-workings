package android.hardware.scontext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.scontext.ISContextService.Stub;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class SContextManager {
    private static final String TAG = "SContextManager";
    @SuppressLint({"UseSparseArrays"})
    private HashMap<Integer, Integer> mAvailableServiceMap = null;
    private SContextListener mIsHistoryDataListener = null;
    private final CopyOnWriteArrayList<SContextListenerDelegate> mListenerDelegates = new CopyOnWriteArrayList();
    Looper mMainLooper;
    private ISContextService mSContextService = Stub.asInterface(ServiceManager.getService(Context.SCONTEXT_SERVICE));

    @SuppressLint({"HandlerLeak"})
    private class SContextListenerDelegate extends ISContextCallback.Stub {
        private final Handler mHandler;
        private final boolean mIsHistoryData;
        private final SContextListener mListener;

        SContextListenerDelegate(SContextListener listener, Looper looper, boolean isHistoryData) {
            this.mListener = listener;
            Looper mLooper = looper != null ? looper : SContextManager.this.mMainLooper;
            this.mIsHistoryData = isHistoryData;
            this.mHandler = new Handler(mLooper, SContextManager.this) {
                public void handleMessage(Message msg) {
                    if (SContextListenerDelegate.this.mListener != null) {
                        SContextEvent scontextEvent = msg.obj;
                        int type = scontextEvent.scontext.getType();
                        if (SContextListenerDelegate.this.mIsHistoryData) {
                            Log.d(SContextManager.TAG, "Data is received so remove listener related HistoryData");
                            SContextListenerDelegate.this.mListener.onSContextChanged(scontextEvent);
                            SContextManager.this.unregisterListener(SContextListenerDelegate.this.mListener, type);
                        } else if (!SContextManager.this.checkHistoryMode(scontextEvent)) {
                            SContextListenerDelegate.this.mListener.onSContextChanged(scontextEvent);
                        } else if (SContextManager.this.mIsHistoryDataListener != null && SContextManager.this.mIsHistoryDataListener.equals(SContextListenerDelegate.this.mListener)) {
                            Log.d(SContextManager.TAG, "Listener is already registered and history data is sent to Application");
                            SContextManager.this.mIsHistoryDataListener.onSContextChanged(scontextEvent);
                        }
                    }
                }
            };
        }

        public SContextListener getListener() {
            return this.mListener;
        }

        public synchronized void scontextCallback(SContextEvent scontextEvent) throws RemoteException {
            Message msg = Message.obtain();
            msg.what = 0;
            msg.obj = scontextEvent;
            this.mHandler.sendMessage(msg);
            notifyAll();
        }

        public String getListenerInfo() throws RemoteException, DeadObjectException {
            return this.mListener.toString();
        }
    }

    public SContextManager(Looper mainLooper) {
        this.mMainLooper = mainLooper;
    }

    public boolean registerListener(SContextListener listener, int service) {
        return registerListener(listener, service, addListenerAttribute(service));
    }

    public boolean registerListener(SContextListener listener, int service, Looper looper) {
        return registerListener(listener, service, addListenerAttribute(service), looper);
    }

    public boolean registerListener(SContextListener listener, int service, SContextAttribute attribute) {
        if (service == 48) {
            return setReferenceData(service, attribute);
        }
        if (attribute == null || !attribute.checkAttribute() || !checkListenerAndService(listener, service)) {
            return false;
        }
        SContextListenerDelegate scontextListener = getListenerDelegate(listener);
        if (scontextListener == null) {
            scontextListener = new SContextListenerDelegate(listener, null, false);
            this.mListenerDelegates.add(scontextListener);
        }
        try {
            this.mSContextService.registerCallback(scontextListener, service, attribute);
            Log.d(TAG, "  .registerListener : listener = " + listener + ", service=" + SContext.getServiceName(service));
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in registerListener: ", e);
        }
        return true;
    }

    public boolean registerListener(SContextListener listener, int service, SContextAttribute attribute, Looper looper) {
        if (attribute == null || !attribute.checkAttribute() || !checkListenerAndService(listener, service)) {
            return false;
        }
        SContextListenerDelegate scontextListener = getListenerDelegate(listener);
        if (scontextListener == null) {
            scontextListener = new SContextListenerDelegate(listener, looper, false);
            this.mListenerDelegates.add(scontextListener);
        }
        try {
            this.mSContextService.registerCallback(scontextListener, service, attribute);
            Log.d(TAG, "  .registerListener : listener = " + listener + ", service=" + SContext.getServiceName(service));
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in registerListener: ", e);
        }
        return true;
    }

    @Deprecated
    public boolean registerListener(SContextListener listener, int service, int arg) {
        SContextAttribute attribute = null;
        if (service == 3) {
            attribute = new SContextStepCountAlertAttribute(arg);
        } else if (service == 6) {
            attribute = new SContextAutoRotationAttribute(arg);
        } else if (service == 16) {
            attribute = new SContextWakeUpVoiceAttribute(arg);
        } else if (service == 33) {
            attribute = new SContextStepLevelMonitorAttribute(arg);
        } else if (service == 36) {
            attribute = new SContextFlatMotionForTableModeAttribute(arg);
        }
        return registerListener(listener, service, attribute);
    }

    @Deprecated
    public boolean registerListener(SContextListener listener, int service, int[] arg) {
        SContextAttribute attribute = null;
        if (service == 27) {
            attribute = new SContextActivityNotificationAttribute(arg);
        }
        return registerListener(listener, service, attribute);
    }

    @Deprecated
    public boolean registerListener(SContextListener listener, int service, int[] arg1, int arg2) {
        SContextAttribute attribute = null;
        if (service == 30) {
            attribute = new SContextActivityNotificationExAttribute(arg1, arg2);
        }
        return registerListener(listener, service, attribute);
    }

    @Deprecated
    public boolean registerListener(SContextListener listener, int service, int arg1, int arg2) {
        SContextAttribute attribute = null;
        if (service == 8) {
            attribute = new SContextEnvironmentAttribute(arg1, arg2);
        } else if (service == 12) {
            attribute = new SContextShakeMotionAttribute(arg1, arg2);
        } else if (service == 29) {
            attribute = new SContextSleepMonitorAttribute(arg1, arg2);
        }
        return registerListener(listener, service, attribute);
    }

    @Deprecated
    public boolean registerListener(SContextListener listener, int service, int arg1, int arg2, int arg3) {
        SContextAttribute attribute = null;
        if (service == 35) {
            attribute = new SContextInactiveTimerAttribute(arg1, arg2, arg3, 1500, 1500);
        }
        return registerListener(listener, service, attribute);
    }

    @Deprecated
    public boolean registerListener(SContextListener listener, int service, int arg1, double arg2, double arg3) {
        SContextAttribute attribute = null;
        if (service == 2) {
            attribute = new SContextPedometerAttribute(arg1, arg2, arg3);
        }
        return registerListener(listener, service, attribute);
    }

    @Deprecated
    public boolean registerListener(SContextListener listener, int service, int arg1, int arg2, boolean arg3) {
        SContextAttribute attribute = null;
        if (service == 23) {
            attribute = new SContextTemperatureAlertAttribute(arg1, arg2, arg3);
        }
        return registerListener(listener, service, attribute);
    }

    @Deprecated
    public boolean registerListener(SContextListener listener, int service, int arg1, int arg2, double arg3, int arg4) {
        SContextAttribute attribute = null;
        if (service == 9) {
            attribute = new SContextMovementForPositioningAttribute(arg1, arg2, arg3, arg4);
        }
        return registerListener(listener, service, attribute);
    }

    @Deprecated
    public boolean registerListener(SContextListener listener, int service, int arg1, int arg2, int arg3, int arg4) {
        SContextAttribute attribute = null;
        if (service == 28) {
            attribute = new SContextSpecificPoseAlertAttribute(arg1, arg2, arg3, arg4);
        }
        return registerListener(listener, service, attribute);
    }

    @Deprecated
    public boolean registerListener(SContextListener listener, int service, int arg1, int arg2, int arg3, int arg4, int arg5) {
        SContextAttribute attribute = null;
        if (service == 24) {
            attribute = new SContextActivityLocationLoggingAttribute(arg1, arg2, arg3, arg4, arg5);
        } else if (service == 35) {
            attribute = new SContextInactiveTimerAttribute(arg1, arg2, arg3, arg4, arg5);
        }
        return registerListener(listener, service, attribute);
    }

    public void unregisterListener(SContextListener listener) {
        if (listener != null && this.mAvailableServiceMap != null) {
            for (Integer intValue : this.mAvailableServiceMap.keySet()) {
                int service = intValue.intValue();
                SContextListenerDelegate scontextListener = getListenerDelegate(listener);
                if (scontextListener != null) {
                    try {
                        if (this.mSContextService.unregisterCallback(scontextListener, service)) {
                            this.mListenerDelegates.remove(scontextListener);
                            Log.d(TAG, "  .unregisterListener : listener = " + listener);
                            return;
                        }
                    } catch (RemoteException e) {
                        Log.e(TAG, "RemoteException in unregisterListener: ", e);
                    }
                } else {
                    return;
                }
            }
        }
    }

    public void unregisterListener(SContextListener listener, int service) {
        if (checkListenerAndService(listener, service)) {
            SContextListenerDelegate scontextListener = getListenerDelegate(listener);
            if (scontextListener == null) {
                Log.e(TAG, "  .unregisterListener : SContextListener is null!");
                return;
            }
            try {
                if (this.mSContextService.unregisterCallback(scontextListener, service)) {
                    this.mListenerDelegates.remove(scontextListener);
                }
                Log.d(TAG, "  .unregisterListener : listener = " + listener + ", service=" + SContext.getServiceName(service));
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in unregisterListener: ", e);
            }
        }
    }

    public void initializeSContextService(SContextListener listener, int service) {
        if (isAvailableService(service) && service == 3) {
            SContextListenerDelegate scontextListener = getListenerDelegate(listener);
            if (scontextListener == null) {
                Log.e(TAG, "  .initializeSContextService : SContextListener is null!");
                return;
            }
            try {
                this.mSContextService.initializeService(scontextListener, service);
                Log.d(TAG, "  .initializeSContextService : listener = " + listener + ", service=" + SContext.getServiceName(service));
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in initializeSContextService: ", e);
            }
        }
    }

    public boolean changeParameters(SContextListener listener, int service, SContextAttribute attribute) {
        if (attribute == null || !attribute.checkAttribute() || !checkListenerAndService(listener, service)) {
            return false;
        }
        if (service != 1 && service != 2 && service != 33 && service != 35 && service != 39 && service != 47 && service != 51) {
            return false;
        }
        SContextListenerDelegate scontextListener = getListenerDelegate(listener);
        if (scontextListener == null) {
            Log.e(TAG, "  .changeParameters : SContextListener is null!");
            return false;
        }
        try {
            if (this.mSContextService.changeParameters(scontextListener, service, attribute)) {
                Log.d(TAG, "  .changeParameters : listener = " + listener + ", service=" + SContext.getServiceName(service));
            }
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in changeParameters: ", e);
        }
        return true;
    }

    @Deprecated
    public boolean changeParameters(SContextListener listener, int service, int arg1, int arg2, int arg3, int arg4) {
        SContextAttribute attribute = null;
        if (service == 35) {
            attribute = new SContextInactiveTimerAttribute(1, arg1, arg2, arg3, arg4);
        }
        return changeParameters(listener, service, attribute);
    }

    @Deprecated
    public boolean changeParameters(SContextListener listener, int service, int arg1, double arg2, double arg3) {
        SContextAttribute attribute = null;
        if (service == 2) {
            attribute = new SContextPedometerAttribute(arg1, arg2, arg3);
        }
        return changeParameters(listener, service, attribute);
    }

    @Deprecated
    public boolean changeParameters(SContextListener listener, int service, int arg1) {
        SContextAttribute attribute = null;
        if (service == 2) {
            attribute = new SContextPedometerAttribute(arg1);
        } else if (service == 33) {
            attribute = new SContextStepLevelMonitorAttribute(arg1);
        }
        return changeParameters(listener, service, attribute);
    }

    public void requestToUpdate(SContextListener listener, int service) {
        if (!isAvailableService(service)) {
            return;
        }
        if (service == 2 || service == 25 || service == 26 || service == 29 || service == 40 || service == 50 || service == 51 || service == 52) {
            SContextListenerDelegate scontextListener = getListenerDelegate(listener);
            if (scontextListener == null) {
                Log.e(TAG, "  .requestToUpdate : SContextListener is null!");
                return;
            }
            try {
                this.mSContextService.requestToUpdate(scontextListener, service);
                Log.d(TAG, "  .requestToUpdate : listener = " + listener + ", service=" + SContext.getServiceName(service));
                return;
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in requestToUpdate: ", e);
                return;
            }
        }
        Log.e(TAG, "  .requestToUpdate : This service is not supported!");
    }

    public void requestHistoryData(SContextListener listener, int service) {
        if (!isAvailableService(service)) {
            return;
        }
        if (service == 2 || service == 33 || service == 26) {
            SContextAttribute attribute = addListenerAttribute(service);
            if (attribute != null && attribute.checkAttribute() && checkListenerAndService(listener, service)) {
                SContextListenerDelegate scontextListener = getListenerDelegate(listener);
                this.mIsHistoryDataListener = listener;
                if (scontextListener == null) {
                    scontextListener = new SContextListenerDelegate(listener, null, true);
                    this.mListenerDelegates.add(scontextListener);
                    try {
                        this.mSContextService.registerCallback(scontextListener, service, attribute);
                        Log.d(TAG, "  .registerListener : listener = " + listener + ", service=" + SContext.getServiceName(service));
                    } catch (RemoteException e) {
                        Log.e(TAG, "RemoteException in registerListener: ", e);
                    }
                }
                try {
                    this.mSContextService.requestHistoryData(scontextListener, service);
                    Log.d(TAG, "  .requestHistoryData : listener = " + listener + ", service=" + SContext.getServiceName(service));
                    return;
                } catch (RemoteException e2) {
                    Log.e(TAG, "RemoteException in requestHistoryData: ", e2);
                    return;
                }
            }
            return;
        }
        Log.e(TAG, "  .requestHistoryData : This service is not supported!");
    }

    public boolean isAvailableService(int service) {
        if (this.mAvailableServiceMap == null) {
            this.mAvailableServiceMap = getAvailableServiceMap();
        }
        if (this.mAvailableServiceMap == null) {
            return false;
        }
        boolean res = this.mAvailableServiceMap.containsKey(Integer.valueOf(service));
        if (service == 47 && "BCM4773_SLOCATION_CORE".equals(SystemProperties.get("ro.gps.chip.vendor.slocation"))) {
            return false;
        }
        return res;
    }

    public int getFeatureLevel(int service) {
        if (isAvailableService(service)) {
            return ((Integer) this.mAvailableServiceMap.get(Integer.valueOf(service))).intValue();
        }
        return 0;
    }

    @Deprecated
    public boolean setReferenceData(int service, byte[] data1, byte[] data2) {
        SContextAttribute attribute = null;
        if (data1 == null || data2 == null) {
            return false;
        }
        if (service == 16) {
            attribute = new SContextWakeUpVoiceAttribute(data1, data2);
        }
        return setReferenceData(service, attribute);
    }

    public boolean setReferenceData(int service, SContextAttribute attribute) {
        boolean res = false;
        if (this.mSContextService == null || attribute == null) {
            return 0;
        }
        Bundle bundle;
        if (service == 48) {
            bundle = attribute.getAttribute(3);
        } else {
            bundle = attribute.getAttribute(service);
        }
        if (bundle == null) {
            return 0;
        }
        switch (service) {
            case 16:
                try {
                    if (bundle.containsKey("net_data") && bundle.containsKey("gram_data")) {
                        byte[] netData = bundle.getByteArray("net_data");
                        byte[] gramData = bundle.getByteArray("gram_data");
                        if (netData != null && gramData != null) {
                            if (this.mSContextService.setReferenceData(1, netData) && this.mSContextService.setReferenceData(2, gramData)) {
                                res = true;
                                break;
                            }
                        }
                        return 0;
                    }
                    return 0;
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in initializeSContextService: ", e);
                    break;
                }
                break;
            case 39:
                if (bundle.containsKey("luminance_config_data")) {
                    byte[] luminanceConfigData = bundle.getByteArray("luminance_config_data");
                    if (luminanceConfigData != null) {
                        if (this.mSContextService.setReferenceData(0, luminanceConfigData)) {
                            res = true;
                            break;
                        }
                    }
                    return 0;
                }
                return 0;
                break;
            case 43:
                if (bundle.containsKey("display_status")) {
                    byte[] hallSensorStatus = new byte[]{(byte) bundle.getInt("display_status")};
                    Log.d(TAG, "Hall Sensor Data : " + String.valueOf(hallSensorStatus[0]));
                    if (this.mSContextService.setReferenceData(43, hallSensorStatus)) {
                        res = true;
                        break;
                    }
                }
                Log.d(TAG, "Bundle is not contained key data");
                return 0;
                break;
            case 48:
                if (bundle.containsKey("step_count")) {
                    byte[] sysfsMode = new byte[]{(byte) bundle.getInt("step_count")};
                    Log.d(TAG, "sysfs data : " + String.valueOf(sysfsMode[0]));
                    if (this.mSContextService.setReferenceData(48, sysfsMode)) {
                        res = true;
                        break;
                    }
                }
                Log.d(TAG, "Bundle is not contained key data");
                return 0;
                break;
        }
        return res;
    }

    @SuppressLint({"UseSparseArrays"})
    private HashMap<Integer, Integer> getAvailableServiceMap() {
        HashMap<Integer, Integer> serviceMap = null;
        try {
            return (HashMap) this.mSContextService.getAvailableServiceMap();
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in getAvailableServiceMap: ", e);
            return serviceMap;
        }
    }

    private SContextListenerDelegate getListenerDelegate(SContextListener listener) {
        if (listener == null || this.mListenerDelegates.isEmpty()) {
            return null;
        }
        Iterator<SContextListenerDelegate> i = this.mListenerDelegates.iterator();
        while (i.hasNext()) {
            SContextListenerDelegate delegate = (SContextListenerDelegate) i.next();
            if (delegate.getListener().equals(listener)) {
                return delegate;
            }
        }
        return null;
    }

    private boolean checkListenerAndService(SContextListener listener, int service) {
        if (listener != null) {
            return isAvailableService(service);
        }
        Log.d(TAG, "Listener is null!");
        return false;
    }

    private SContextAttribute addListenerAttribute(int service) {
        switch (service) {
            case 1:
                return new SContextApproachAttribute();
            case 2:
                return new SContextPedometerAttribute();
            case 3:
                return new SContextStepCountAlertAttribute();
            case 6:
                return new SContextAutoRotationAttribute();
            case 8:
                return new SContextEnvironmentAttribute();
            case 9:
                return new SContextMovementForPositioningAttribute();
            case 12:
                return new SContextShakeMotionAttribute();
            case 23:
                return new SContextTemperatureAlertAttribute();
            case 24:
                return new SContextActivityLocationLoggingAttribute();
            case 27:
                return new SContextActivityNotificationAttribute();
            case 28:
                return new SContextSpecificPoseAlertAttribute();
            case 29:
                return new SContextSleepMonitorAttribute();
            case 30:
                return new SContextActivityNotificationExAttribute();
            case 33:
                return new SContextStepLevelMonitorAttribute();
            case 35:
                return new SContextInactiveTimerAttribute();
            case 36:
                return new SContextFlatMotionForTableModeAttribute();
            case 39:
                return new SContextAutoBrightnessAttribute();
            case 40:
                return new SContextExerciseAttribute();
            case 47:
                return new SContextSLocationCoreAttribute();
            case 51:
                return new SContextDevicePhysicalContextMonitorAttribute();
            default:
                return new SContextAttribute();
        }
    }

    private boolean checkHistoryMode(SContextEvent scontextevent) {
        switch (scontextevent.scontext.getType()) {
            case 2:
                if (scontextevent.getPedometerContext().getMode() == 2) {
                    return true;
                }
                return false;
            case 26:
                if (scontextevent.getActivityBatchContext().getMode() == 1) {
                    return true;
                }
                return false;
            case 33:
                if (scontextevent.getStepLevelMonitorContext().getMode() == 1) {
                    return true;
                }
                return false;
            default:
                return false;
        }
    }
}
