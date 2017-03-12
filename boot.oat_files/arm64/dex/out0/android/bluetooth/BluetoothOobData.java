package android.bluetooth;

import android.net.ProxyInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;
import java.util.Arrays;

public final class BluetoothOobData implements Parcelable, Serializable {
    public static final Creator<BluetoothOobData> CREATOR = new Creator<BluetoothOobData>() {
        public BluetoothOobData createFromParcel(Parcel source) {
            return new BluetoothOobData(source);
        }

        public BluetoothOobData[] newArray(int size) {
            return new BluetoothOobData[size];
        }
    };
    public static final int HASH_P192_LEN = 16;
    public static final int HASH_P256_LEN = 16;
    public static final int RANDOMIZER_P192_LEN = 16;
    public static final int RANDOMIZER_P256_LEN = 16;
    public static final String TAG = "BluetoothOobData";
    private String mAddress;
    private byte[] mHashP192;
    private byte[] mHashP256;
    private byte[] mRandomizerP192;
    private byte[] mRandomizerP256;
    private int mValid_P192_P256;

    public BluetoothOobData() {
        this.mHashP192 = new byte[16];
        this.mRandomizerP192 = new byte[16];
        this.mHashP256 = new byte[16];
        this.mRandomizerP256 = new byte[16];
        Arrays.fill(this.mHashP192, (byte) 0);
        Arrays.fill(this.mRandomizerP192, (byte) 0);
        Arrays.fill(this.mRandomizerP256, (byte) 0);
        Arrays.fill(this.mHashP256, (byte) 0);
        this.mValid_P192_P256 = 0;
        this.mAddress = ProxyInfo.LOCAL_EXCL_LIST;
    }

    public BluetoothOobData(byte[] hashP192, byte[] randomizerP192, byte[] hashP256, byte[] randomizerP256, int valid_P192_P256, String address) {
        this.mHashP192 = new byte[16];
        this.mRandomizerP192 = new byte[16];
        this.mHashP256 = new byte[16];
        this.mRandomizerP256 = new byte[16];
        this.mValid_P192_P256 = 0;
        if (hashP192 != null) {
            this.mHashP192 = Arrays.copyOf(hashP192, 16);
        } else {
            Arrays.fill(this.mHashP192, (byte) 0);
        }
        if (randomizerP192 != null) {
            this.mRandomizerP192 = Arrays.copyOf(randomizerP192, 16);
        } else {
            Arrays.fill(this.mRandomizerP192, (byte) 0);
        }
        if (hashP256 != null) {
            this.mHashP256 = Arrays.copyOf(hashP256, 16);
        } else {
            Arrays.fill(this.mHashP256, (byte) 0);
        }
        if (randomizerP256 != null) {
            this.mRandomizerP256 = Arrays.copyOf(randomizerP256, 16);
        } else {
            Arrays.fill(this.mRandomizerP256, (byte) 0);
        }
        this.mValid_P192_P256 = valid_P192_P256;
        this.mAddress = address;
    }

    public BluetoothOobData(BluetoothOobData obbdata) {
        this.mHashP192 = new byte[16];
        this.mRandomizerP192 = new byte[16];
        this.mHashP256 = new byte[16];
        this.mRandomizerP256 = new byte[16];
        if (obbdata.mHashP192 != null) {
            this.mHashP192 = Arrays.copyOf(obbdata.mHashP192, 16);
        } else {
            Arrays.fill(this.mHashP192, (byte) 0);
        }
        if (obbdata.mRandomizerP192 != null) {
            this.mRandomizerP192 = Arrays.copyOf(obbdata.mRandomizerP192, 16);
        } else {
            Arrays.fill(this.mRandomizerP192, (byte) 0);
        }
        if (obbdata.mHashP256 != null) {
            this.mHashP256 = Arrays.copyOf(obbdata.mHashP256, 16);
        } else {
            Arrays.fill(this.mHashP256, (byte) 0);
        }
        if (obbdata.mRandomizerP256 != null) {
            this.mRandomizerP256 = Arrays.copyOf(obbdata.mRandomizerP256, 16);
        } else {
            Arrays.fill(this.mRandomizerP256, (byte) 0);
        }
        this.mValid_P192_P256 = obbdata.mValid_P192_P256;
        this.mAddress = obbdata.mAddress;
    }

    public void setHashP192(byte[] hashP192) {
        this.mHashP192 = Arrays.copyOf(hashP192, 16);
    }

    public void setRandomizerP192(byte[] randomizerP192) {
        this.mRandomizerP192 = Arrays.copyOf(randomizerP192, 16);
    }

    public void setHashP256(byte[] randomizerP256) {
        this.mHashP256 = Arrays.copyOf(randomizerP256, 16);
    }

    public void setRandomizerP256(byte[] randomizerP256) {
        this.mRandomizerP256 = Arrays.copyOf(randomizerP256, 16);
    }

    public void setValid_P192_P256(int valid_P192_P256) {
        this.mValid_P192_P256 = valid_P192_P256;
    }

    public void setBtAddress(String address) {
        this.mAddress = address;
    }

    public String getAddress() {
        return this.mAddress;
    }

    public byte[] getRandomizerP192() {
        return this.mRandomizerP192;
    }

    public byte[] getHashP192() {
        return this.mHashP192;
    }

    public byte[] getHashP256() {
        return this.mHashP256;
    }

    public byte[] getRandomizerP256() {
        return this.mRandomizerP256;
    }

    public int getValid_P192_P256() {
        return this.mValid_P192_P256;
    }

    public String getBtAddress() {
        return this.mAddress;
    }

    public int describeContents() {
        return 0;
    }

    public String getAddressStringFromBytearray(byte[] address) {
        return String.format("%02X:%02X:%02X:%02X:%02X:%02X:%02X:%02X:%02X:%02X:%02X:%02X:%02X:%02X:%02X:%02X", new Object[]{Byte.valueOf(address[0]), Byte.valueOf(address[1]), Byte.valueOf(address[2]), Byte.valueOf(address[3]), Byte.valueOf(address[4]), Byte.valueOf(address[5]), Byte.valueOf(address[6]), Byte.valueOf(address[7]), Byte.valueOf(address[8]), Byte.valueOf(address[9]), Byte.valueOf(address[10]), Byte.valueOf(address[11]), Byte.valueOf(address[12]), Byte.valueOf(address[13]), Byte.valueOf(address[14]), Byte.valueOf(address[15])});
    }

    public String getSummary() {
        return "mValid_P192_P256 = " + this.mValid_P192_P256 + ", mHashP192 = " + getAddressStringFromBytearray(this.mHashP192) + ", mRandomizerP192 = " + getAddressStringFromBytearray(this.mRandomizerP192) + ", mHashP256 = " + getAddressStringFromBytearray(this.mHashP256) + ", mRandomizerP256 = " + getAddressStringFromBytearray(this.mRandomizerP256) + ", mAddress = " + this.mAddress;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mValid_P192_P256);
        dest.writeByteArray(this.mHashP192);
        dest.writeByteArray(this.mRandomizerP192);
        dest.writeByteArray(this.mHashP256);
        dest.writeByteArray(this.mRandomizerP256);
        dest.writeString(this.mAddress);
    }

    private BluetoothOobData(Parcel source) {
        this.mHashP192 = new byte[16];
        this.mRandomizerP192 = new byte[16];
        this.mHashP256 = new byte[16];
        this.mRandomizerP256 = new byte[16];
        this.mValid_P192_P256 = source.readInt();
        source.readByteArray(this.mHashP192);
        source.readByteArray(this.mRandomizerP192);
        source.readByteArray(this.mHashP256);
        source.readByteArray(this.mRandomizerP256);
        this.mAddress = source.readString();
    }
}
