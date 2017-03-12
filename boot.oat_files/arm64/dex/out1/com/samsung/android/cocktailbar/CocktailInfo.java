package com.samsung.android.cocktailbar;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.widget.RemoteViews;

public class CocktailInfo implements Parcelable {
    public static final Creator<CocktailInfo> CREATOR = new Creator<CocktailInfo>() {
        public CocktailInfo createFromParcel(Parcel in) {
            CocktailInfo data = new CocktailInfo();
            data.readFromParcel(in);
            return data;
        }

        public CocktailInfo[] newArray(int size) {
            return new CocktailInfo[size];
        }
    };
    private int mCategory = 1;
    private ComponentName mClassInfo = null;
    private Bundle mContentInfo = null;
    private RemoteViews mContentView = null;
    private int mDisplayPolicy = 1;
    private RemoteViews mHelpView = null;
    private int mOrientation = 1;
    private int mUserId = 0;

    public static class Builder {
        private int mCategory = 1;
        private ComponentName mClassInfo = null;
        private Bundle mContentInfo = null;
        private RemoteViews mContentView = null;
        private Context mContext = null;
        private int mDisplayPolicy = 1;
        private RemoteViews mHelpView = null;
        private int mOrientation = 1;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setOrientation(int orientation) {
            this.mOrientation = orientation;
            return this;
        }

        public Builder setDiplayPolicy(int displayPolicy) {
            this.mDisplayPolicy = displayPolicy;
            return this;
        }

        public Builder setCategory(int category) {
            this.mCategory = category;
            return this;
        }

        public Builder setContentView(RemoteViews views) {
            this.mContentView = views;
            return this;
        }

        public Builder setContentInfo(Bundle bundle) {
            this.mContentInfo = bundle;
            return this;
        }

        public Builder setHelpView(RemoteViews views) {
            this.mHelpView = views;
            return this;
        }

        public Builder setClassloader(ComponentName classInfo) {
            this.mClassInfo = classInfo;
            return this;
        }

        public CocktailInfo build() {
            CocktailInfo cocktailInfo = new CocktailInfo();
            cocktailInfo.mOrientation = this.mOrientation;
            cocktailInfo.mDisplayPolicy = this.mDisplayPolicy;
            cocktailInfo.mCategory = this.mCategory;
            cocktailInfo.mContentView = this.mContentView;
            cocktailInfo.mHelpView = this.mHelpView;
            cocktailInfo.mUserId = this.mContext.getUserId();
            cocktailInfo.mContentInfo = this.mContentInfo;
            cocktailInfo.mClassInfo = this.mClassInfo;
            return cocktailInfo;
        }
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public int getDisplayPolicy() {
        return this.mDisplayPolicy;
    }

    public int getCategory() {
        return this.mCategory;
    }

    public void setCategory(int category) {
        this.mCategory = category;
    }

    public RemoteViews getContentView() {
        return this.mContentView;
    }

    public RemoteViews getHelpView() {
        return this.mHelpView;
    }

    public Bundle getContentInfo() {
        return this.mContentInfo;
    }

    public ComponentName getClassInfo() {
        return this.mClassInfo;
    }

    public void updateContentView(RemoteViews contentView, boolean isPartialUpdate) {
        if (!isPartialUpdate || this.mContentView == null) {
            this.mContentView = contentView;
        } else {
            this.mContentView.mergeRemoteViews(contentView);
        }
    }

    public void updateHelpView(RemoteViews helpView, boolean isPartialUpdate) {
        if (!isPartialUpdate || this.mHelpView == null) {
            this.mHelpView = helpView;
        } else {
            this.mHelpView.mergeRemoteViews(helpView);
        }
    }

    public void mergeInfo(CocktailInfo info) {
        this.mOrientation = info.mOrientation;
        this.mCategory = info.mCategory;
        this.mDisplayPolicy = info.mDisplayPolicy;
        if (info.mContentInfo != null) {
            this.mContentInfo = info.mContentInfo;
            this.mContentView = null;
        }
        if (info.mContentView != null) {
            this.mContentView = info.mContentView;
            this.mContentInfo = null;
        }
        if (info.mHelpView != null) {
            this.mHelpView = info.mHelpView;
        }
        if (info.mClassInfo != null) {
            this.mClassInfo = info.mClassInfo;
        }
    }

    public String dump() {
        String dumpResult = "U:" + this.mUserId + " ORI:" + this.mOrientation + " DP:" + this.mDisplayPolicy + " CAT:" + this.mCategory;
        if (this.mContentView != null) {
            dumpResult = dumpResult + " has RemoteViews";
        }
        if (this.mContentInfo != null) {
            dumpResult = dumpResult + " has ContentInfo";
        }
        if (this.mClassInfo != null) {
            dumpResult = dumpResult + " ClassInfo : " + this.mClassInfo.flattenToShortString();
        }
        if (this.mHelpView != null) {
            return dumpResult + " has HelpView";
        }
        return dumpResult;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(this.mContentView, flags);
        out.writeParcelable(this.mHelpView, flags);
        out.writeInt(this.mUserId);
        out.writeInt(this.mOrientation);
        out.writeInt(this.mDisplayPolicy);
        out.writeInt(this.mCategory);
        out.writeBundle(this.mContentInfo);
        if (this.mClassInfo != null) {
            out.writeInt(1);
            this.mClassInfo.writeToParcel(out, flags);
            return;
        }
        out.writeInt(0);
    }

    public void readFromParcel(Parcel in) {
        this.mContentView = (RemoteViews) in.readParcelable(RemoteViews.class.getClassLoader());
        this.mHelpView = (RemoteViews) in.readParcelable(RemoteViews.class.getClassLoader());
        this.mUserId = in.readInt();
        this.mOrientation = in.readInt();
        this.mDisplayPolicy = in.readInt();
        this.mCategory = in.readInt();
        this.mContentInfo = in.readBundle();
        this.mClassInfo = in.readInt() != 0 ? new ComponentName(in) : null;
    }

    public int describeContents() {
        return 0;
    }
}
