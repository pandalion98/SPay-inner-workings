/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.session;

public final class StateMode
extends Enum<StateMode> {
    private static final /* synthetic */ StateMode[] $VALUES;
    public static final /* enum */ StateMode ACTIVE = new StateMode(1, false);
    public static final /* enum */ StateMode BLOCKED;
    public static final /* enum */ StateMode DELETE;
    public static final /* enum */ StateMode SUSPEND;
    public static final /* enum */ StateMode TERMINATE;
    private boolean canHCEClientSet;
    private int lcmState;

    static {
        SUSPEND = new StateMode(2, true);
        DELETE = new StateMode(3, true);
        BLOCKED = new StateMode(4, false);
        TERMINATE = new StateMode(5, false);
        StateMode[] arrstateMode = new StateMode[]{ACTIVE, SUSPEND, DELETE, BLOCKED, TERMINATE};
        $VALUES = arrstateMode;
    }

    private StateMode(int n3, boolean bl) {
        this.lcmState = n3;
        this.canHCEClientSet = bl;
    }

    public static StateMode getStateMode(int n2) {
        for (StateMode stateMode : StateMode.values()) {
            if (n2 != stateMode.getLcmState()) continue;
            return stateMode;
        }
        return null;
    }

    public static StateMode valueOf(String string) {
        return (StateMode)Enum.valueOf(StateMode.class, (String)string);
    }

    public static StateMode[] values() {
        return (StateMode[])$VALUES.clone();
    }

    public int getLcmState() {
        return this.lcmState;
    }

    public boolean isCanHCEClientSet() {
        return this.canHCEClientSet;
    }
}

