package com.samsung.android.magazinecard;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.widget.RemoteViews;

public class MagazineCardInfo implements Parcelable {
    public static int CATEGORY_EMAIL = 8;
    public static int CATEGORY_FAVORITE_APP = 512;
    public static int CATEGORY_FLIGHT_MODE = 2048;
    public static int CATEGORY_HERE_N_NOW = 256;
    public static int CATEGORY_MISSED_CALL = 32;
    public static int CATEGORY_MUSIC_PLAYER = 4;
    public static int CATEGORY_NEWS = 64;
    public static int CATEGORY_NEW_MESSAGE = 16;
    public static int CATEGORY_NONE = 0;
    public static int CATEGORY_ROAMING = 1024;
    public static int CATEGORY_SOCIAL = 128;
    public static int CATEGORY_TODAYS_SCHEDULE = 2;
    public static int CATEGORY_TODAY_BIRTHDAY = 4096;
    public static int CATEGORY_WEATHER = 1;
    public static final Creator<MagazineCardInfo> CREATOR = new Creator<MagazineCardInfo>() {
        public MagazineCardInfo createFromParcel(Parcel in) {
            MagazineCardInfo data = new MagazineCardInfo();
            data.readFromParcel(in);
            return data;
        }

        public MagazineCardInfo[] newArray(int size) {
            return new MagazineCardInfo[size];
        }
    };
    public static int SECURITY_LEVEL_NON_SECURE_MODE_ONLY = 2;
    public static int SECURITY_LEVEL_NORMAL = (SECURITY_LEVEL_SECURE_MODE_ONLY | SECURITY_LEVEL_NON_SECURE_MODE_ONLY);
    public static int SECURITY_LEVEL_SECURE_MODE_ONLY = 1;
    public int mCardId = 0;
    public int mCategory = CATEGORY_NONE;
    public RemoteViews mContentView = null;
    public RemoteViews mExpandedContentView = null;
    public PendingIntent mLaunchIntent = null;
    public int mSecurityLevel = SECURITY_LEVEL_NORMAL;
    public long mTimeStamp = 0;
    public int mUserId = 0;

    public static class Builder {
        private int mCardId = 0;
        private int mCategory = MagazineCardInfo.CATEGORY_NONE;
        private RemoteViews mContentView = null;
        private Context mContext = null;
        private RemoteViews mExpandedContentView = null;
        private PendingIntent mLaunchIntent = null;
        private int mSecurityLevel = MagazineCardInfo.SECURITY_LEVEL_NORMAL;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setCardId(int id) {
            this.mCardId = id;
            return this;
        }

        public Builder setSecurityLevel(int level) {
            this.mSecurityLevel = level;
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

        public Builder setExpandedContentView(RemoteViews views) {
            this.mExpandedContentView = views;
            return this;
        }

        public Builder setLaunchIntent(PendingIntent intent) {
            this.mLaunchIntent = intent;
            return this;
        }

        public MagazineCardInfo build() {
            MagazineCardInfo card = new MagazineCardInfo();
            card.mCardId = this.mCardId;
            card.mSecurityLevel = this.mSecurityLevel;
            card.mCategory = this.mCategory;
            card.mContentView = this.mContentView;
            card.mExpandedContentView = this.mExpandedContentView;
            card.mLaunchIntent = this.mLaunchIntent;
            card.mUserId = this.mContext.getUserId();
            return card;
        }
    }

    public String dump() {
        String dumpResult = "U:" + this.mUserId + " CID" + this.mCardId + " CAT:" + this.mCategory + " SL:" + this.mSecurityLevel;
        if (this.mExpandedContentView != null) {
            dumpResult = dumpResult + " expandable";
        }
        if (this.mLaunchIntent != null) {
            return dumpResult + " haveLauncher";
        }
        return dumpResult;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mCardId);
        out.writeParcelable(this.mContentView, flags);
        out.writeParcelable(this.mExpandedContentView, flags);
        out.writeInt(this.mSecurityLevel);
        out.writeInt(this.mUserId);
        out.writeInt(this.mCategory);
        out.writeLong(this.mTimeStamp);
        out.writeParcelable(this.mLaunchIntent, flags);
    }

    public void readFromParcel(Parcel in) {
        this.mCardId = in.readInt();
        this.mContentView = (RemoteViews) in.readParcelable(RemoteViews.class.getClassLoader());
        this.mExpandedContentView = (RemoteViews) in.readParcelable(RemoteViews.class.getClassLoader());
        this.mSecurityLevel = in.readInt();
        this.mUserId = in.readInt();
        this.mCategory = in.readInt();
        this.mTimeStamp = in.readLong();
        this.mLaunchIntent = (PendingIntent) in.readParcelable(PendingIntent.class.getClassLoader());
    }

    public int describeContents() {
        return 0;
    }
}
