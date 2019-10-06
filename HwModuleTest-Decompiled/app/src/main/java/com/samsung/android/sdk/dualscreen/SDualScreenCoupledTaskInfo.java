package com.samsung.android.sdk.dualscreen;

import com.samsung.android.sdk.dualscreen.SDualScreenConstantsReflector.DualScreenLaunchParams;

public class SDualScreenCoupledTaskInfo {
    public static int RUNNING_MODE_CONTEXTUAL = DualScreenLaunchParams.FLAG_COUPLED_TASK_CONTEXTUAL_MODE;
    public static int RUNNING_MODE_EXPAND = DualScreenLaunchParams.FLAG_COUPLED_TASK_EXPAND_MODE;
    public static int RUNNING_MODE_UNDEFINED = 0;
    public static final int UNSPECIFIED_TASK_ID = -1;
    private int mChildTaskId = -1;
    private int mCoupledMode;
    private boolean mIsCoupled = false;
    private int mParentTaskId = -1;
    private int mTaskId;

    private SDualScreenCoupledTaskInfo() {
    }

    public SDualScreenCoupledTaskInfo(int taskId) {
        this.mTaskId = taskId;
    }

    public int getTaskId() {
        return this.mTaskId;
    }

    public int getCoupledTaskMode() {
        return this.mCoupledMode;
    }

    public int getParentCoupledTaskId() {
        return this.mParentTaskId;
    }

    public int getChildCoupledTaskId() {
        return this.mChildTaskId;
    }

    public boolean isCoupled() {
        return this.mIsCoupled;
    }

    /* access modifiers changed from: 0000 */
    public void setCoupledTaskMode(int coupledTaskMode) {
        this.mCoupledMode = coupledTaskMode;
    }

    /* access modifiers changed from: 0000 */
    public void setParentCoupledTaskId(int taskId) {
        this.mParentTaskId = taskId;
    }

    /* access modifiers changed from: 0000 */
    public void setChildCoupledTaskId(int taskId) {
        this.mChildTaskId = taskId;
    }

    /* access modifiers changed from: 0000 */
    public void setCoupledState(boolean isCoupled) {
        this.mIsCoupled = isCoupled;
    }

    public String toString() {
        StringBuilder b = new StringBuilder(128);
        StringBuilder sb = new StringBuilder();
        sb.append(SDualScreenCoupledTaskInfo.class.getSimpleName());
        sb.append(" { ");
        b.append(sb.toString());
        b.append("mTaskId=");
        b.append(this.mTaskId);
        b.append(" ");
        b.append("mCoupledMode=");
        b.append(this.mCoupledMode);
        b.append(" ");
        if (this.mParentTaskId != -1) {
            b.append("mParentTaskId=");
            b.append(this.mParentTaskId);
            b.append(" ");
        }
        if (this.mChildTaskId != -1) {
            b.append("mChildTaskId=");
            b.append(this.mChildTaskId);
            b.append(" ");
        }
        b.append("}");
        return b.toString();
    }
}
