package com.goodix.cap.fingerprint.ext;

public final class GoodixFpInfo {
    private int mFingerId;
    private int mGroupId;
    private int mIndex;

    public GoodixFpInfo() {
    }

    public GoodixFpInfo(int index, int groupId, int fingerId) {
        this.mIndex = index;
        this.mGroupId = groupId;
        this.mFingerId = fingerId;
    }

    public void setFingerIndex(int index) {
        this.mIndex = index;
    }

    public int getFingerIndex() {
        return this.mIndex;
    }

    public void setFingerId(int fingerId) {
        this.mFingerId = fingerId;
    }

    public int getFingerId() {
        return this.mFingerId;
    }

    public void setGroupId(int groupId) {
        this.mGroupId = groupId;
    }

    public int getGroupId() {
        return this.mGroupId;
    }
}
