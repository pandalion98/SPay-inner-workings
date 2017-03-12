package com.samsung.android.contextaware.manager;

public class CaUserInfo {
    public static final int DEFAULT_GENDER = 1;
    public static final double DEFAULT_HEIGHT = 170.0d;
    public static final double DEFAULT_WEIGHT = 60.0d;
    private static volatile CaUserInfo instance;
    private int mUserGender = 1;
    private double mUserHeight = DEFAULT_HEIGHT;
    private double mUserWeight = DEFAULT_WEIGHT;

    public static CaUserInfo getInstance() {
        if (instance == null) {
            synchronized (CaUserInfo.class) {
                if (instance == null) {
                    instance = new CaUserInfo();
                }
            }
        }
        return instance;
    }

    public final double getUserHeight() {
        return this.mUserHeight;
    }

    public final void setUserHeight(double height) {
        this.mUserHeight = height;
    }

    public final double getUserWeight() {
        return this.mUserWeight;
    }

    public final void setUserWeight(double weight) {
        this.mUserWeight = weight;
    }

    public final int getUserGender() {
        return this.mUserGender;
    }

    public final void setUserGender(int gender) {
        this.mUserGender = gender;
    }
}
