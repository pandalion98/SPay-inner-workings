package com.samsung.android.contextaware.manager;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.utilbundle.CaPowerManager;
import com.samsung.android.contextaware.utilbundle.CaTimeChangeManager;
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;
import com.samsung.android.contextaware.utilbundle.ITimeOutCheckObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.fingerprint.FingerprintManager;
import java.util.Iterator;

public abstract class ContextProvider extends ContextComponent implements ISensorHubResetObserver, IApPowerObserver, ITimeChangeObserver {
    private int mApStatus;
    private final Context mContext;
    private final Looper mLooper;
    private int mPreparedCollection;
    private final ISensorHubResetObservable mSensorHubResetObservable;
    private final IContextTimeOutCheck mTimeOutCheck = new ContextTimeOutCheck(getTimeOutCheckObserver());
    private long mTimeStampForApStatus;
    private int mVersion;

    public abstract void disable();

    public abstract void enable();

    public abstract String[] getContextValueNames();

    public abstract Bundle getFaultDetectionResult();

    protected abstract IApPowerObserver getPowerObserver();

    protected abstract ISensorHubResetObserver getPowerResetObserver();

    protected ContextProvider(int version, Context context, Looper looper, ISensorHubResetObservable observable) {
        this.mVersion = version;
        this.mContext = context;
        this.mLooper = looper;
        this.mSensorHubResetObservable = observable;
    }

    protected void enableForRestore() {
        enable();
    }

    protected void disableForRestore() {
        disable();
    }

    public void start(Listener listener, int operation) {
        CaLogger.trace();
        if (isEnable()) {
            initialize();
            clear();
            enableForStart(operation);
            registerApPowerObserver();
            if (getTimeChangeObserver() != null) {
                CaTimeChangeManager.getInstance().registerObserver(getTimeChangeObserver());
            }
        }
        if (operation == 1) {
            notifyFaultDetectionResult();
        }
    }

    public void stop(Listener listener, int operation) {
        CaLogger.trace();
        if (isDisable()) {
            clear();
            unregisterApPowerObserver();
            disableForStop(operation);
            terminate();
            if (getTimeChangeObserver() != null) {
                CaTimeChangeManager.getInstance().unregisterObserver(getTimeChangeObserver());
            }
        }
        if (operation == 1) {
            notifyFaultDetectionResult();
        }
    }

    protected void enableForStart(int operation) {
        if (operation == 2) {
            enableForRestore();
        } else {
            enable();
        }
    }

    protected void disableForStop(int operation) {
        if (operation == 2) {
            disableForRestore();
        } else {
            disable();
        }
    }

    protected void getContextInfo(Listener listener) {
        String dependentService = getDependentService();
        this.mTimeOutCheck.setTimeOutOccurence(false);
        if (dependentService == null || dependentService.isEmpty()) {
            CaLogger.error(ContextAwareServiceErrors.ERROR_DEPENDENT_SERVICE_NULL_EXCEPTION.getMessage());
            notifyCmdProcessResultObserver(getContextTypeOfFaultDetection(), getFaultDetectionResult(1, ContextAwareServiceErrors.ERROR_DEPENDENT_SERVICE_NULL_EXCEPTION.getMessage()));
            return;
        }
        boolean isRegisterDependentService = false;
        Iterator<Integer> iter = listener.getServices().keySet().iterator();
        Iterator<?> j = iter;
        while (iter.hasNext()) {
            if (ContextList.getInstance().getServiceCode(((Integer) j.next()).intValue()).equals(dependentService)) {
                isRegisterDependentService = true;
                break;
            }
        }
        if (isRegisterDependentService) {
            enable();
            doTimeOutChecking(listener, getFaultDetectionResult());
            return;
        }
        CaLogger.error(ContextAwareServiceErrors.ERROR_DEPENDENT_SERVICE_NOT_REGISTERED.getMessage());
        notifyCmdProcessResultObserver(getContextTypeOfFaultDetection(), getFaultDetectionResult(1, ContextAwareServiceErrors.ERROR_DEPENDENT_SERVICE_NOT_REGISTERED.getMessage()));
    }

    protected void doTimeOutChecking(Listener listener, Bundle bundle) {
        if (bundle == null) {
            CaLogger.error(ContextAwareServiceErrors.getMessage(ContextAwareServiceErrors.ERROR_BUNDLE_NULL_EXCEPTION.getCode()));
        } else if (bundle.getInt("CheckResult") == 0) {
            clear();
            this.mTimeOutCheck.run();
        } else {
            CaLogger.error("FAULT_DETECTION result is not success");
            notifyCmdProcessResultObserver(getContextTypeOfFaultDetection(), getFaultDetectionResult(1, bundle.getString("Cause")));
        }
    }

    protected String getDependentService() {
        return null;
    }

    protected boolean checkFaultDetectionResult() {
        return true;
    }

    protected ITimeOutCheckObserver getTimeOutCheckObserver() {
        return null;
    }

    protected final IContextTimeOutCheck getTimeOutCheckManager() {
        return this.mTimeOutCheck;
    }

    protected final void notifyFaultDetectionResult() {
        Bundle bundle = getFaultDetectionResult();
        if (bundle == null) {
            CaLogger.error("Fault Detection Result is null.");
        } else {
            notifyCmdProcessResultObserver(getContextTypeOfFaultDetection(), bundle);
        }
    }

    protected final boolean isEnable() {
        return getUsedTotalCount() <= 1;
    }

    protected final boolean isDisable() {
        return getUsedTotalCount() < 1;
    }

    protected void display() {
        Bundle context = getContextBean().getContextBundleForDisplay();
        if (context != null && !context.isEmpty()) {
            CaLogger.debug("================= " + getContextType() + " =================");
            StringBuffer str = new StringBuffer();
            for (String i : context.keySet()) {
                if (i == null || i.isEmpty()) {
                    break;
                }
                str.append(i + "=[" + getDisplayContents(context, i) + "], ");
            }
            if (str.lastIndexOf(FingerprintManager.FINGER_PERMISSION_DELIMITER) > 0) {
                str.delete(str.lastIndexOf(FingerprintManager.FINGER_PERMISSION_DELIMITER), str.length());
            }
            CaLogger.info(str.toString());
        }
    }

    protected String getDisplayContents(Bundle bundle, String key) {
        if (key == null || key.isEmpty()) {
            CaLogger.error("key is null");
            return null;
        } else if (bundle == null || !bundle.containsKey(key)) {
            CaLogger.error("bundle is null");
            return null;
        } else {
            StringBuffer str = new StringBuffer();
            if (bundle.get(key) instanceof String[]) {
                for (String j : bundle.getStringArray(key)) {
                    str.append(j + ", ");
                }
                if (str.lastIndexOf(FingerprintManager.FINGER_PERMISSION_DELIMITER) > 0) {
                    str.delete(str.lastIndexOf(FingerprintManager.FINGER_PERMISSION_DELIMITER), str.length());
                }
                return str.toString();
            }
            String value = bundle.getString(key);
            if (value != null && !value.isEmpty()) {
                return value;
            }
            CaLogger.error("bundle.getStringArray(key) is null");
            return null;
        }
    }

    protected final void registerApPowerObserver() {
        if (this.mSensorHubResetObservable != null) {
            this.mSensorHubResetObservable.registerSensorHubResetObserver(getPowerResetObserver());
        }
        CaPowerManager.getInstance().registerApPowerObserver(getPowerObserver());
    }

    protected final void unregisterApPowerObserver() {
        if (this.mSensorHubResetObservable != null) {
            this.mSensorHubResetObservable.unregisterSensorHubResetObserver(getPowerResetObserver());
        }
        CaPowerManager.getInstance().unregisterApPowerObserver(getPowerObserver());
    }

    public final void updateApPowerStatusForPreparedCollection() {
        this.mPreparedCollection++;
        if (getUsedSubCollectionCount() <= this.mPreparedCollection) {
            processApPowerStatus();
        }
    }

    public void updateApPowerStatus(int status, long timeStamp) {
        this.mApStatus = status;
        this.mTimeStampForApStatus = timeStamp;
        if (getUsedSubCollectionCount() <= 0 || getUsedSubCollectionCount() <= this.mPreparedCollection) {
            processApPowerStatus();
        }
    }

    protected void processApPowerStatus() {
        if (this.mApStatus == -46) {
            updateApSleep();
        } else if (this.mApStatus == -47) {
            updateApWakeup();
        }
    }

    public void updateSensorHubResetStatus(int status) {
        if (status == -43) {
            reset();
        }
    }

    protected void updateApSleep() {
        pause();
        this.mApStatus = 0;
    }

    protected void updateApWakeup() {
        resume();
        this.mApStatus = 0;
    }

    protected void updateApReset() {
        reset();
        this.mApStatus = 0;
    }

    public final void initializePreparedSubCollection() {
        this.mPreparedCollection = 0;
    }

    public final String getContextTypeOfFaultDetection() {
        return ContextType.CMD_PROCESS_FAULT_DETECTION.getCode();
    }

    public final String[] getFaultDetectionResultValueNames() {
        return new String[]{"Service", "CheckResult", "Cause"};
    }

    public final ContextProvider getContextProvider() {
        return this;
    }

    public final void notifyCmdProcessResultObserver(String type, Bundle context) {
        if (context == null) {
            CaLogger.error(ContextAwareServiceErrors.getMessage(ContextAwareServiceErrors.ERROR_CONTEXT_NULL_EXCEPTION.getCode()));
            return;
        }
        CaLogger.info("CheckResult = " + Integer.toString(context.getInt("CheckResult")) + ", Cause = " + context.getString("Cause"));
        super.notifyCmdProcessResultObserver(type, context);
    }

    protected final Bundle getFaultDetectionResult(int result, String cause) {
        String[] names = getFaultDetectionResultValueNames();
        Bundle bundle = new Bundle();
        bundle.putInt(names[0], ContextList.getInstance().getServiceOrdinal(getContextType()));
        bundle.putInt(names[1], result);
        bundle.putString(names[2], cause);
        return bundle;
    }

    public final int getAPStatus() {
        return this.mApStatus;
    }

    public final long getTimeStampForApStatus() {
        return this.mTimeStampForApStatus;
    }

    public final void setAPStatus(int status) {
        this.mApStatus = status;
    }

    protected final Context getContext() {
        return this.mContext;
    }

    protected final Looper getLooper() {
        return this.mLooper;
    }

    protected final void setVersion(int version) {
        this.mVersion = version;
    }

    protected final int getVersion() {
        return this.mVersion;
    }

    protected ITimeChangeObserver getTimeChangeObserver() {
        return null;
    }

    public void onTimeChanged() {
        disable();
        enable();
    }
}
