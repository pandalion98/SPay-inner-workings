package com.synaptics.bpd.fingerprint;

public class DeviceInfo {
    public int capabilities;
    public int flexId;
    public String fwExtVersion;
    public String fwVersion;
    public int productId;
    public int projectId;
    public byte[] serialNumber = new byte[6];
    public int siliconVersion;
}
