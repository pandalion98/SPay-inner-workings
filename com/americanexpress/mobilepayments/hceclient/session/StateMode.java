package com.americanexpress.mobilepayments.hceclient.session;

public enum StateMode {
    ACTIVE(1, false),
    SUSPEND(2, true),
    DELETE(3, true),
    BLOCKED(4, false),
    TERMINATE(5, false);
    
    private boolean canHCEClientSet;
    private int lcmState;

    private StateMode(int i, boolean z) {
        this.lcmState = i;
        this.canHCEClientSet = z;
    }

    public int getLcmState() {
        return this.lcmState;
    }

    public boolean isCanHCEClientSet() {
        return this.canHCEClientSet;
    }

    public static StateMode getStateMode(int i) {
        for (StateMode stateMode : values()) {
            if (i == stateMode.getLcmState()) {
                return stateMode;
            }
        }
        return null;
    }
}
