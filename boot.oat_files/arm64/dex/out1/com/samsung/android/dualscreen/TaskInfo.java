package com.samsung.android.dualscreen;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class TaskInfo implements Parcelable {
    public static final int APPLICATION_TYPE = 0;
    public static final Creator<TaskInfo> CREATOR = new Creator<TaskInfo>() {
        public TaskInfo createFromParcel(Parcel in) {
            return new TaskInfo(in);
        }

        public TaskInfo[] newArray(int size) {
            return new TaskInfo[size];
        }
    };
    public static final int HOME_TYPE = 1;
    public static final int RECENTS_TYPE = 2;
    private static final String TAG = TaskInfo.class.getSimpleName();
    public static final int UNSPECIFIED_TASK_ID = -1;
    public boolean canMoveTaskToScreen = false;
    private int mChildTaskId = -1;
    private boolean mFixed = false;
    private int mParentTaskId = -1;
    private DualScreen mScreen = DualScreen.UNKNOWN;
    private int mTaskId = -1;
    private int mTaskType = 0;

    private TaskInfo() {
    }

    public TaskInfo(int taskId) {
        this.mTaskId = taskId;
    }

    public TaskInfo(Parcel in) {
        readFromParcel(in);
    }

    public int getTaskId() {
        return this.mTaskId;
    }

    public int getTaskType() {
        return this.mTaskType;
    }

    public boolean isHomeTask() {
        if (this.mTaskType == 1) {
            return true;
        }
        return false;
    }

    public boolean isFixed() {
        return this.mFixed;
    }

    public DualScreen getScreen() {
        return this.mScreen;
    }

    public int getChildCoupledTaskId() {
        return this.mChildTaskId;
    }

    public int getParentCoupledTaskId() {
        return this.mParentTaskId;
    }

    public boolean isCoupled() {
        if (this.mParentTaskId >= 0 || this.mChildTaskId >= 0) {
            return true;
        }
        return false;
    }

    public void setTaskType(int taskType) {
        this.mTaskType = taskType;
    }

    public void setScreen(DualScreen screen) {
        this.mScreen = screen;
    }

    public void setFixed(boolean fixed) {
        this.mFixed = fixed;
    }

    public void setChildCoupledTaskId(int taskId) {
        this.mChildTaskId = taskId;
    }

    public void setParentCoupledTaskId(int taskId) {
        this.mParentTaskId = taskId;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mTaskId);
        out.writeInt(this.mTaskType);
        if (this.mScreen != null) {
            out.writeInt(1);
            this.mScreen.writeToParcel(out, flags);
        } else {
            out.writeInt(0);
        }
        if (this.mFixed) {
            out.writeInt(1);
        } else {
            out.writeInt(0);
        }
        out.writeInt(this.mChildTaskId);
        out.writeInt(this.mParentTaskId);
        if (this.canMoveTaskToScreen) {
            out.writeInt(1);
        } else {
            out.writeInt(0);
        }
    }

    public void readFromParcel(Parcel in) {
        this.mTaskId = in.readInt();
        this.mTaskType = in.readInt();
        if (in.readInt() != 0) {
            this.mScreen = (DualScreen) DualScreen.CREATOR.createFromParcel(in);
        }
        if (in.readInt() != 0) {
            this.mFixed = true;
        }
        this.mChildTaskId = in.readInt();
        this.mParentTaskId = in.readInt();
        if (in.readInt() != 0) {
            this.canMoveTaskToScreen = true;
        }
    }

    public String toString() {
        StringBuilder b = new StringBuilder(128);
        b.append(TaskInfo.class.getSimpleName() + "{");
        b.append("mTaskId=").append(this.mTaskId).append(" ");
        b.append("mTaskType=").append(this.mTaskType).append(" ");
        b.append("mScreen=").append(this.mScreen).append(" ");
        b.append("mFixed=").append(this.mFixed).append(" ");
        b.append("mChildTaskId=").append(this.mChildTaskId).append(" ");
        b.append("mParentTaskId=").append(this.mParentTaskId).append(" ");
        b.append("canMoveTaskToScreen=").append(this.canMoveTaskToScreen).append(" ");
        b.append("}");
        return b.toString();
    }
}
