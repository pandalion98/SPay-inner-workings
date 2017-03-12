package com.samsung.android.cocktailbar;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CocktailBarStateInfo implements Parcelable, Cloneable {
    public static final Creator<CocktailBarStateInfo> CREATOR = new Creator<CocktailBarStateInfo>() {
        public CocktailBarStateInfo createFromParcel(Parcel in) {
            return new CocktailBarStateInfo(in);
        }

        public CocktailBarStateInfo[] newArray(int size) {
            return new CocktailBarStateInfo[size];
        }
    };
    public static final int FLAG_CHANGE_ACTIVATE = 64;
    public static final int FLAG_CHANGE_BACKGROUND_TYPE = 2;
    public static final int FLAG_CHANGE_LOCK_STATE = 8;
    public static final int FLAG_CHANGE_MODE = 16;
    public static final int FLAG_CHANGE_POSITION = 4;
    public static final int FLAG_CHANGE_SHOW_TIMEOUT = 32;
    public static final int FLAG_CHANGE_VISIBILITY = 1;
    public static final int FLAG_CHANGE_WINDOW_TYPE = 128;
    public boolean activate = true;
    public int backgroundType = 0;
    public int changeFlag = 0;
    public int lockState = 0;
    public int mode = 0;
    public int position = 0;
    public int showTimeout = -1;
    public int visibility;
    public int windowType = 0;

    public CocktailBarStateInfo(int visibility) {
        this.visibility = visibility;
    }

    public CocktailBarStateInfo(CocktailBarStateInfo stateInfo) {
        this.visibility = stateInfo.visibility;
        this.backgroundType = stateInfo.backgroundType;
        this.position = stateInfo.position;
        this.lockState = stateInfo.lockState;
        this.showTimeout = stateInfo.showTimeout;
        this.activate = stateInfo.activate;
        this.windowType = stateInfo.windowType;
    }

    CocktailBarStateInfo(Parcel in) {
        boolean z = true;
        this.visibility = in.readInt();
        this.backgroundType = in.readInt();
        this.position = in.readInt();
        this.lockState = in.readInt();
        this.mode = in.readInt();
        this.showTimeout = in.readInt();
        if (in.readInt() != 1) {
            z = false;
        }
        this.activate = z;
        this.windowType = in.readInt();
        this.changeFlag = in.readInt();
    }

    public CocktailBarStateInfo clone() {
        Parcel p = Parcel.obtain();
        writeToParcel(p, 0);
        p.setDataPosition(0);
        CocktailBarStateInfo stateInfo = new CocktailBarStateInfo(p);
        p.recycle();
        return stateInfo;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.visibility);
        out.writeInt(this.backgroundType);
        out.writeInt(this.position);
        out.writeInt(this.lockState);
        out.writeInt(this.mode);
        out.writeInt(this.showTimeout);
        out.writeInt(this.activate ? 1 : 0);
        out.writeInt(this.windowType);
        out.writeInt(this.changeFlag);
    }
}
