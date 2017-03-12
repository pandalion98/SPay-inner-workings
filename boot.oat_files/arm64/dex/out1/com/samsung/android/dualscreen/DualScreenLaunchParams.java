package com.samsung.android.dualscreen;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class DualScreenLaunchParams implements Parcelable {
    public static final Creator<DualScreenLaunchParams> CREATOR = new Creator<DualScreenLaunchParams>() {
        public DualScreenLaunchParams createFromParcel(Parcel in) {
            return new DualScreenLaunchParams(in);
        }

        public DualScreenLaunchParams[] newArray(int size) {
            return new DualScreenLaunchParams[size];
        }
    };
    public static final int FLAG_COUPLED_TASK = 1;
    public static final int FLAG_COUPLED_TASK_CONTEXTUAL_MODE = 2;
    public static final int FLAG_COUPLED_TASK_EXPAND_MODE = 1;
    public static final int FLAG_COUPLED_TASK_LEAF_MODE = 4;
    private boolean fromDisplayChooser;
    private boolean fromOppositeLaunchApp;
    private int mFlags;
    private DualScreen mScreen;

    public DualScreenLaunchParams() {
        this.fromDisplayChooser = false;
        this.fromOppositeLaunchApp = false;
        this.mScreen = DualScreen.UNKNOWN;
        this.mFlags = 0;
    }

    public DualScreenLaunchParams(DualScreen screen) {
        this.fromDisplayChooser = false;
        this.fromOppositeLaunchApp = false;
        if (screen == null) {
            throw new NullPointerException("screen is null");
        }
        this.mScreen = screen;
        this.mFlags = 0;
    }

    public DualScreenLaunchParams(Parcel in) {
        this.fromDisplayChooser = false;
        this.fromOppositeLaunchApp = false;
        readFromParcel(in);
    }

    public DualScreen getScreen() {
        return this.mScreen;
    }

    public void setScreen(DualScreen screen) {
        this.mScreen = screen;
    }

    public int getFlags() {
        return this.mFlags;
    }

    public void addFlags(int flags) {
        this.mFlags |= flags;
    }

    public void setFlags(int flags) {
        this.mFlags = flags;
    }

    public void clearFlags(int flags) {
        this.mFlags &= flags ^ -1;
    }

    public void setFromDisplayChooser(boolean set) {
        this.fromDisplayChooser = set;
    }

    public boolean fromDisplayChooser() {
        return this.fromDisplayChooser;
    }

    public void setFromOppositeLaunchApp(boolean set) {
        this.fromOppositeLaunchApp = set;
    }

    public boolean fromOppositeLaunchApp() {
        return this.fromOppositeLaunchApp;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        if (this.mScreen != null) {
            out.writeInt(1);
            this.mScreen.writeToParcel(out, flags);
        } else {
            out.writeInt(0);
        }
        out.writeInt(this.mFlags);
        if (this.fromDisplayChooser) {
            out.writeInt(1);
        } else {
            out.writeInt(0);
        }
        if (this.fromOppositeLaunchApp) {
            out.writeInt(1);
        } else {
            out.writeInt(0);
        }
    }

    public void readFromParcel(Parcel in) {
        if (in.readInt() != 0) {
            this.mScreen = (DualScreen) DualScreen.CREATOR.createFromParcel(in);
        }
        this.mFlags = in.readInt();
        if (in.readInt() != 0) {
            this.fromDisplayChooser = true;
        }
        if (in.readInt() != 0) {
            this.fromOppositeLaunchApp = true;
        }
    }

    public String toString() {
        StringBuilder b = new StringBuilder(128);
        b.append("DualScreenLaunchParams { ");
        b.append("mScreen=").append(this.mScreen).append(" ");
        b.append("mFlags=").append(this.mFlags);
        b.append(" }");
        return b.toString();
    }
}
