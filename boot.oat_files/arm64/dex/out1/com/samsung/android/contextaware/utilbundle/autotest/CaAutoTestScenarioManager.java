package com.samsung.android.contextaware.utilbundle.autotest;

import android.content.Context;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.ArrayList;
import java.util.Iterator;

public class CaAutoTestScenarioManager {
    public static final int AP_POWER_AUTO_TEST = 3;
    public static final int BYPASS_AUTO_TEST = 1;
    public static final int CA_OPERATION_AUTO_TEST = 4;
    public static final int CA_OPERATION_DEBUGGING = 5;
    public static final int LIBRARY_AUTO_TEST = 2;
    public static final int SENSOR_HUB_OPERATION_DEBUGGING = 6;
    private final CaOperationDebugging mCaOperationDebugging = new CaOperationDebugging(0);
    private final Context mContext;
    private final ArrayList<ICaAutoTest> mScenarioListForTest = new ArrayList();
    private final SensorHubOperationDebugging mSensorHubOperationDebugging;

    public CaAutoTestScenarioManager(Context context) {
        this.mContext = context;
        this.mSensorHubOperationDebugging = new SensorHubOperationDebugging(context, 0);
    }

    public final void startAutoTest() {
        if (this.mScenarioListForTest == null || this.mScenarioListForTest.isEmpty()) {
            CaLogger.error("Scenario list is empty.");
            return;
        }
        Iterator i$ = this.mScenarioListForTest.iterator();
        while (i$.hasNext()) {
            ICaAutoTest i = (ICaAutoTest) i$.next();
            i.setStopFlag(false);
            new Thread(i).start();
        }
    }

    public final void stopAutoTest() {
        if (this.mScenarioListForTest != null && !this.mScenarioListForTest.isEmpty()) {
            Iterator i$ = this.mScenarioListForTest.iterator();
            while (i$.hasNext()) {
                ((ICaAutoTest) i$.next()).stopAutoTest();
            }
            initilizeAutoTest();
        }
    }

    public final void initilizeAutoTest() {
        if (this.mScenarioListForTest != null) {
            this.mScenarioListForTest.clear();
        }
        this.mCaOperationDebugging.clearPacket();
        this.mSensorHubOperationDebugging.clearPacket();
    }

    public final boolean setScenarioForTest(int testType, int delayTime) {
        switch (testType) {
            case 1:
                this.mScenarioListForTest.add(new BypassStressTest(this.mContext, delayTime));
                break;
            case 2:
                this.mScenarioListForTest.add(new LibraryStressTest(this.mContext, delayTime));
                break;
            case 3:
                this.mScenarioListForTest.add(new ApPowerStressTest(delayTime));
                break;
            case 4:
                this.mScenarioListForTest.add(new CaOperationStressTest(delayTime));
                break;
        }
        return true;
    }

    public final boolean setScenarioForDebugging(int testType, int delayTime, byte[] packet) {
        if (packet == null) {
            return false;
        }
        switch (testType) {
            case 5:
                this.mCaOperationDebugging.setDelayTime(delayTime);
                this.mCaOperationDebugging.addPacket(packet);
                if (!this.mScenarioListForTest.contains(this.mCaOperationDebugging)) {
                    this.mScenarioListForTest.add(this.mCaOperationDebugging);
                    break;
                }
                break;
            case 6:
                this.mSensorHubOperationDebugging.setDelayTime(delayTime);
                this.mSensorHubOperationDebugging.addPacket(packet);
                if (!this.mScenarioListForTest.contains(this.mSensorHubOperationDebugging)) {
                    this.mScenarioListForTest.add(this.mSensorHubOperationDebugging);
                    break;
                }
                break;
        }
        return true;
    }
}
