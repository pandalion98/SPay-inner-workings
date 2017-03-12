package com.samsung.android.contextaware;

import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubParser;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.handler.builtin.ActivityTrackerHandler;

public class InterruptModeContextList {
    private static volatile InterruptModeContextList instance;

    public enum InterruptModeContextType implements IParserHandler {
        SENSORHUB_RUNNER_ACTIVITY_TRACKER_INTERRUPT(ContextType.SENSORHUB_RUNNER_ACTIVITY_TRACKER_INTERRUPT.getCode()) {
            public final ISensorHubParser getParserHandler() {
                return ActivityTrackerHandler.getInstance();
            }
        };
        
        private String code;

        private InterruptModeContextType(String code) {
            this.code = code;
        }

        public String getCode() {
            return this.code;
        }

        public ISensorHubParser getParserHandler() {
            return null;
        }
    }

    public static InterruptModeContextList getInstance() {
        if (instance == null) {
            synchronized (InterruptModeContextList.class) {
                if (instance == null) {
                    instance = new InterruptModeContextList();
                }
            }
        }
        return instance;
    }

    public final boolean isInterruptModeType(int key) {
        return isInterruptModeType(ContextList.getInstance().getServiceCode(key));
    }

    public final boolean isInterruptModeType(String key) {
        for (InterruptModeContextType i : InterruptModeContextType.values()) {
            if (i.code.equals(key)) {
                return true;
            }
        }
        return false;
    }
}
