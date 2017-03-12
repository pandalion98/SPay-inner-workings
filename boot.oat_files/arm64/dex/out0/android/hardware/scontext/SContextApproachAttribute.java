package android.hardware.scontext;

import android.os.Bundle;

public class SContextApproachAttribute extends SContextAttribute {
    private static final String TAG = "SContextApproachAttribute";
    private int mUserID = -1;

    SContextApproachAttribute() {
        setAttribute();
    }

    public SContextApproachAttribute(int userID) {
        this.mUserID = userID;
        setAttribute();
    }

    boolean checkAttribute() {
        return true;
    }

    private void setAttribute() {
        Bundle attribute = new Bundle();
        attribute.putInt("UserID", this.mUserID);
        super.setAttribute(1, attribute);
    }
}
