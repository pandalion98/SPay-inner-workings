package android.mtp;

public class MtpDeviceInfo {
    private String mManufacturer;
    private String mModel;
    private String mSerialNumber;
    private String mVersion;

    private MtpDeviceInfo() {
    }

    public final String getManufacturer() {
        return this.mManufacturer;
    }

    public final String getModel() {
        return this.mModel;
    }

    public final String getVersion() {
        return this.mVersion;
    }

    public final String getSerialNumber() {
        return this.mSerialNumber;
    }
}
