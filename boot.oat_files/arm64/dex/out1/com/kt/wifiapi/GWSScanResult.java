package com.kt.wifiapi;

import android.os.Debug;

public class GWSScanResult {
    private static final boolean DBG;
    public String BSSID;
    public String BSSLoadElement;
    public String SSID;
    public String capabilities;
    public int frequency;
    public int level;
    public String vendorSpecificContents;
    public String vendorSpecificOUI;

    static {
        boolean z = true;
        if (Debug.isProductShip() == 1) {
            z = false;
        }
        DBG = z;
    }

    public GWSScanResult(String SSID, String BSSID, String caps, int level, int frequency, String BSSLoadElement, String vendorSpecificOUI, String vendorSpecificContents) {
        this.SSID = SSID;
        this.BSSID = BSSID;
        this.capabilities = caps;
        this.level = level;
        this.frequency = frequency;
        this.BSSLoadElement = BSSLoadElement;
        this.vendorSpecificOUI = vendorSpecificOUI;
        this.vendorSpecificContents = vendorSpecificContents;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        String none = "<none>";
        if (DBG) {
            sb.append("SSID: ").append(this.SSID == null ? none : this.SSID).append(", BSSID: ").append(this.BSSID == null ? none : this.BSSID);
        }
        StringBuffer append = sb.append(", capabilities: ");
        if (this.capabilities != null) {
            none = this.capabilities;
        }
        append.append(none).append(", level: ").append(this.level).append(", frequency: ").append(this.frequency).append(", BSSLoadElement: ").append(this.BSSLoadElement).append(", vendorSpecificOUI: ").append(this.vendorSpecificOUI).append(", vendorSpecificContents: ").append(this.vendorSpecificContents);
        return sb.toString();
    }
}
