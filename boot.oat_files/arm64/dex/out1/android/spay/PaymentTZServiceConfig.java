package android.spay;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.spay.IPaymentClient.Stub;
import java.util.HashMap;
import java.util.Map;

public class PaymentTZServiceConfig implements Parcelable {
    public static final Creator<PaymentTZServiceConfig> CREATOR = new Creator<PaymentTZServiceConfig>() {
        public PaymentTZServiceConfig createFromParcel(Parcel in) {
            return new PaymentTZServiceConfig(in);
        }

        public PaymentTZServiceConfig[] newArray(int size) {
            return new PaymentTZServiceConfig[size];
        }
    };
    public IBinder mClient;
    public Map<Integer, TAConfig> mTAConfigs;

    public static class TAConfig {
        public int maxRecvRespSize;
        public int maxSendCmdSize;

        public TAConfig(int sendsize, int recvsize) {
            this.maxSendCmdSize = sendsize;
            this.maxRecvRespSize = recvsize;
        }
    }

    public void addTAConfig(int taId, TAConfig config) {
        this.mTAConfigs.put(Integer.valueOf(taId), config);
    }

    public void removeTAConfig(int taId, TAConfig config) {
        this.mTAConfigs.remove(Integer.valueOf(taId));
    }

    public TAConfig getTAConfig(int taId) {
        return (TAConfig) this.mTAConfigs.get(Integer.valueOf(taId));
    }

    public PaymentTZServiceConfig() {
        this.mClient = new Stub() {
        };
        this.mTAConfigs = new HashMap();
    }

    private PaymentTZServiceConfig(Parcel in) {
        this.mClient = /* anonymous class already generated */;
        this.mTAConfigs = new HashMap();
        readFromParcel(in);
    }

    public void writeToParcel(Parcel out, int flag) {
        out.writeStrongBinder(this.mClient);
        out.writeInt(this.mTAConfigs.size());
        for (Integer s : this.mTAConfigs.keySet()) {
            out.writeInt(s.intValue());
            out.writeInt(((TAConfig) this.mTAConfigs.get(s)).maxSendCmdSize);
            out.writeInt(((TAConfig) this.mTAConfigs.get(s)).maxRecvRespSize);
        }
    }

    public void readFromParcel(Parcel in) {
        this.mClient = in.readStrongBinder();
        int count = in.readInt();
        for (int i = 0; i < count; i++) {
            this.mTAConfigs.put(Integer.valueOf(in.readInt()), new TAConfig(in.readInt(), in.readInt()));
        }
    }

    public int describeContents() {
        return 0;
    }
}
