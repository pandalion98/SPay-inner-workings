package com.android.services.SecurityBridge.api;

import android.content.ClipData;

public class ClipboardManagerMonitor {
    public boolean approvePasteRequest(int appID, ClipData clipData) {
        return true;
    }

    public void notifyCopy(int appID, ClipData clipData) {
    }
}
