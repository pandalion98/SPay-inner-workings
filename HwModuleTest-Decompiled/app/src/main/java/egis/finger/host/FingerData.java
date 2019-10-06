package egis.finger.host;

import java.io.Serializable;

/* compiled from: FileDB */
class FingerData implements Serializable {
    private static final long serialVersionUID = 1;
    byte[] mFeature;
    String mID;

    FingerData(String id, byte[] feature) {
        this.mID = id;
        if (feature != null) {
            this.mFeature = (byte[]) feature.clone();
        }
    }

    public String getid() {
        return this.mID;
    }

    public byte[] getfeature() {
        return this.mFeature;
    }

    public void setfeature(byte[] feature) {
        this.mFeature = (byte[]) feature.clone();
    }

    public boolean equals(Object obj) {
        return ((FingerData) obj).mID.equals(this.mID);
    }
}
