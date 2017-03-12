package com.samsung.android.magazinecard;

import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class MagazineCardRecord implements Parcelable {
    public static final Creator<MagazineCardRecord> CREATOR = new Creator<MagazineCardRecord>() {
        public MagazineCardRecord createFromParcel(Parcel in) {
            MagazineCardRecord data = new MagazineCardRecord();
            data.readFromParcel(in);
            return data;
        }

        public MagazineCardRecord[] newArray(int size) {
            return new MagazineCardRecord[size];
        }
    };
    public MagazineCardInfo mCardInfo;
    public ComponentName mComponentName;
    public int mId;

    public MagazineCardRecord() {
        this.mId = 0;
        this.mCardInfo = null;
        this.mComponentName = null;
    }

    public MagazineCardRecord(int id, MagazineCardInfo card) {
        this.mId = 0;
        this.mCardInfo = null;
        this.mComponentName = null;
        this.mId = id;
        this.mCardInfo = card;
    }

    public MagazineCardRecord(int id, MagazineCardInfo card, ComponentName componentName) {
        this(id, card);
        this.mComponentName = componentName;
    }

    public String getAppName() {
        if (this.mComponentName == null) {
            return null;
        }
        String packageName = this.mComponentName.getPackageName();
        if (packageName == null) {
            return null;
        }
        String[] split = packageName.split("\\.");
        if (split.length > 0) {
            return split[split.length - 1];
        }
        return null;
    }

    public String dump() {
        String dumpResult = "RID:" + this.mId;
        if (this.mCardInfo != null) {
            dumpResult = dumpResult + " " + this.mCardInfo.dump();
        }
        return dumpResult + " / " + getAppName();
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mId);
        out.writeParcelable(this.mCardInfo, flags);
        out.writeParcelable(this.mComponentName, flags);
    }

    public void readFromParcel(Parcel in) {
        this.mId = in.readInt();
        this.mCardInfo = (MagazineCardInfo) in.readParcelable(MagazineCardInfo.class.getClassLoader());
        this.mComponentName = (ComponentName) in.readParcelable(ComponentName.class.getClassLoader());
    }

    public int describeContents() {
        return 0;
    }
}
