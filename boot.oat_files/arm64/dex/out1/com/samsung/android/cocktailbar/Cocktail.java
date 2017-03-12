package com.samsung.android.cocktailbar;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.widget.RemoteViews;

public class Cocktail implements Parcelable {
    public static final Creator<Cocktail> CREATOR = new Creator<Cocktail>() {
        public Cocktail createFromParcel(Parcel in) {
            Cocktail data = new Cocktail();
            data.readFromParcel(in);
            return data;
        }

        public Cocktail[] newArray(int size) {
            return new Cocktail[size];
        }
    };
    private PendingIntent mBroadcast;
    private int mCocktailId;
    private CocktailInfo mCocktailInfo;
    private boolean mEnable;
    private CocktailProviderInfo mProviderInfo;
    private int mUid;
    private int mVersion;

    public Cocktail() {
        this.mUid = 0;
        this.mCocktailInfo = new CocktailInfo();
        this.mVersion = 1;
        this.mEnable = true;
    }

    public Cocktail(int cocktailId) {
        this.mUid = 0;
        this.mCocktailInfo = new CocktailInfo();
        this.mVersion = 1;
        this.mEnable = true;
        this.mCocktailId = cocktailId;
    }

    public Cocktail(int cocktailId, CocktailInfo cocktailInfo) {
        this(cocktailId);
        this.mCocktailInfo = cocktailInfo;
    }

    public void setProviderInfo(CocktailProviderInfo providerInfo) {
        this.mProviderInfo = providerInfo;
    }

    public void setUid(int uid) {
        this.mUid = uid;
    }

    public void setVersion(int version) {
        this.mVersion = version;
    }

    public int getCocktailId() {
        return this.mCocktailId;
    }

    public CocktailInfo getCocktailInfo() {
        return this.mCocktailInfo;
    }

    public int getUid() {
        return this.mUid;
    }

    public int getVersion() {
        return this.mVersion;
    }

    public CocktailProviderInfo getProviderInfo() {
        return this.mProviderInfo;
    }

    public PendingIntent getBroadcast() {
        return this.mBroadcast;
    }

    public void setBroadcast(PendingIntent broadcast) {
        this.mBroadcast = broadcast;
    }

    public ComponentName getProvider() {
        if (this.mProviderInfo != null) {
            return this.mProviderInfo.provider;
        }
        return null;
    }

    public boolean isEnable() {
        return this.mEnable;
    }

    public void setEnable(boolean enable) {
        this.mEnable = enable;
    }

    @Deprecated
    public void addCocktailInfo(CocktailInfo cocktailInfo) {
        this.mCocktailInfo = cocktailInfo;
    }

    public void updateCocktailInfo(CocktailInfo cocktailInfo) {
        if (this.mCocktailInfo == null || cocktailInfo == null) {
            this.mCocktailInfo = cocktailInfo;
        } else {
            this.mCocktailInfo.mergeInfo(cocktailInfo);
        }
    }

    public void updateCocktailContentView(RemoteViews contentView, boolean isPartialUpdate) {
        if (this.mCocktailInfo != null) {
            this.mCocktailInfo.updateContentView(contentView, isPartialUpdate);
        }
    }

    public void updateCocktailHelpView(RemoteViews helpView, boolean isPartialUpdate) {
        if (this.mCocktailInfo != null) {
            this.mCocktailInfo.updateHelpView(helpView, isPartialUpdate);
        }
    }

    public String getUpdateIntentName() {
        return getUpdateIntentName(this.mVersion);
    }

    public static String getUpdateIntentName(int version) {
        switch (version) {
            case 1:
                return CocktailBarManager.ACTION_COCKTAIL_UPDATE;
            case 2:
                return CocktailBarManager.ACTION_COCKTAIL_UPDATE_V2;
            default:
                return CocktailBarManager.ACTION_COCKTAIL_UPDATE;
        }
    }

    public String dump() {
        String dumpResult = "[CocktailId:" + this.mCocktailId + " uid:" + this.mUid + " version:" + this.mVersion + " enable:" + this.mEnable;
        if (this.mBroadcast != null) {
            dumpResult = dumpResult + " has broadcast";
        }
        if (this.mCocktailInfo != null) {
            dumpResult = dumpResult + " " + this.mCocktailInfo.dump();
        }
        return dumpResult + " ]";
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mCocktailId);
        out.writeInt(this.mUid);
        out.writeInt(this.mVersion);
        if (this.mEnable) {
            out.writeByte((byte) 1);
        } else {
            out.writeByte((byte) 0);
        }
        out.writeParcelable(this.mBroadcast, flags);
        out.writeParcelable(this.mProviderInfo, flags);
        out.writeParcelable(this.mCocktailInfo, flags);
    }

    public void readFromParcel(Parcel in) {
        boolean z = true;
        this.mCocktailId = in.readInt();
        this.mUid = in.readInt();
        this.mVersion = in.readInt();
        if (in.readByte() != (byte) 1) {
            z = false;
        }
        this.mEnable = z;
        this.mBroadcast = (PendingIntent) in.readParcelable(PendingIntent.class.getClassLoader());
        this.mProviderInfo = (CocktailProviderInfo) in.readParcelable(ComponentName.class.getClassLoader());
        this.mCocktailInfo = (CocktailInfo) in.readParcelable(CocktailInfo.class.getClassLoader());
    }

    public int describeContents() {
        return 0;
    }
}
