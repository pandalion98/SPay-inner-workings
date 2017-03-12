package com.android.internal.statusbar;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.PrintWriter;

public class StatusBarIconList implements Parcelable {
    public static final Creator<StatusBarIconList> CREATOR = new Creator<StatusBarIconList>() {
        public StatusBarIconList createFromParcel(Parcel parcel) {
            return new StatusBarIconList(parcel);
        }

        public StatusBarIconList[] newArray(int size) {
            return new StatusBarIconList[size];
        }
    };
    private StatusBarIcon[] mIcons;
    private String[] mSlots;

    public StatusBarIconList(Parcel in) {
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        this.mSlots = in.readStringArray();
        int N = in.readInt();
        if (N < 0) {
            this.mIcons = null;
            return;
        }
        this.mIcons = new StatusBarIcon[N];
        for (int i = 0; i < N; i++) {
            if (in.readInt() != 0) {
                this.mIcons[i] = new StatusBarIcon(in);
            }
        }
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeStringArray(this.mSlots);
        if (this.mIcons == null) {
            out.writeInt(-1);
            return;
        }
        out.writeInt(N);
        for (StatusBarIcon ic : this.mIcons) {
            if (ic == null) {
                out.writeInt(0);
            } else {
                out.writeInt(1);
                ic.writeToParcel(out, flags);
            }
        }
    }

    public int describeContents() {
        return 0;
    }

    public void defineSlots(String[] slots) {
        int N = slots.length;
        String[] s = new String[N];
        this.mSlots = s;
        for (int i = 0; i < N; i++) {
            s[i] = slots[i];
        }
        this.mIcons = new StatusBarIcon[N];
    }

    public int getSlotIndex(String slot) {
        int N = this.mSlots.length;
        for (int i = 0; i < N; i++) {
            if (slot.equals(this.mSlots[i])) {
                return i;
            }
        }
        return -1;
    }

    public int size() {
        return this.mSlots.length;
    }

    public void setIcon(int index, StatusBarIcon icon) {
        this.mIcons[index] = icon.clone();
    }

    public void removeIcon(int index) {
        this.mIcons[index] = null;
    }

    public String getSlot(int index) {
        return this.mSlots[index];
    }

    public StatusBarIcon getIcon(int index) {
        return this.mIcons[index];
    }

    public int getViewIndex(int index) {
        int count = 0;
        for (int i = 0; i < index; i++) {
            if (this.mIcons[i] != null) {
                count++;
            }
        }
        return count;
    }

    public void copyFrom(StatusBarIconList that) {
        if (that.mSlots == null) {
            this.mSlots = null;
            this.mIcons = null;
            return;
        }
        int N = that.mSlots.length;
        this.mSlots = new String[N];
        this.mIcons = new StatusBarIcon[N];
        for (int i = 0; i < N; i++) {
            StatusBarIcon clone;
            this.mSlots[i] = that.mSlots[i];
            StatusBarIcon[] statusBarIconArr = this.mIcons;
            if (that.mIcons[i] != null) {
                clone = that.mIcons[i].clone();
            } else {
                clone = null;
            }
            statusBarIconArr[i] = clone;
        }
    }

    public void dump(PrintWriter pw) {
        int N = this.mSlots.length;
        pw.println("Icon list:");
        for (int i = 0; i < N; i++) {
            pw.printf("  %2d: (%s) %s\n", new Object[]{Integer.valueOf(i), this.mSlots[i], this.mIcons[i]});
        }
    }
}
