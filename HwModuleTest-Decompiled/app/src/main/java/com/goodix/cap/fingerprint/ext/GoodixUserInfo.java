package com.goodix.cap.fingerprint.ext;

public final class GoodixUserInfo {
    private int mGroupId;
    private CharSequence mPwd;
    private CharSequence mUserId;

    public GoodixUserInfo(CharSequence userId, int groupId) {
        this.mUserId = userId;
        this.mGroupId = groupId;
        this.mPwd = null;
    }

    public GoodixUserInfo(CharSequence userId, int groupId, CharSequence pwd) {
        this.mUserId = userId;
        this.mGroupId = groupId;
        this.mPwd = pwd;
    }

    public void setUserId(CharSequence userId) {
        this.mUserId = userId;
    }

    public CharSequence getUserId() {
        return this.mUserId;
    }

    public void setGroupId(int groupId) {
        this.mGroupId = groupId;
    }

    public int getGroupId() {
        return this.mGroupId;
    }

    public void setPwd(CharSequence pwd) {
        this.mPwd = pwd;
    }

    public CharSequence getPwd() {
        return this.mPwd;
    }
}
