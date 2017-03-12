package com.samsung.android.service.DeviceRootKeyService;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.HashMap;
import java.util.Set;

public final class Tlv implements Parcelable {
    public static final Creator<Tlv> CREATOR = new Creator<Tlv>() {
        public Tlv createFromParcel(Parcel in) {
            Tlv tlv = new Tlv();
            int elementNum = in.readInt();
            for (int i = 0; i < elementNum; i++) {
                int key = in.readInt();
                byte[] value = new byte[in.readInt()];
                in.readByteArray(value);
                tlv.setTlv(key, value);
            }
            return tlv;
        }

        public Tlv[] newArray(int size) {
            return new Tlv[size];
        }
    };
    public static final int TLV_ATTRS = 16;
    public static final int TLV_TAG_CERT_SD = 9;
    public static final int TLV_TAG_CERT_SM = 8;
    public static final int TLV_TAG_EXPONENT = 1;
    public static final int TLV_TAG_EXTEND_PCR_DATA = 12;
    public static final int TLV_TAG_EXT_KEYUSAGE = 6;
    public static final int TLV_TAG_HASH_ALGO = 3;
    public static final int TLV_TAG_ISSUER = 2;
    public static final int TLV_TAG_KEYUSAGE = 5;
    private static final int TLV_TAG_MAX = 17;
    public static final int TLV_TAG_SIGN_DATA_BLOB = 7;
    public static final int TLV_TAG_SUBJECT = 4;
    public static final int TLV_TAG_TID = 13;
    public static final int TLV_TAG_TIMESTAMP = 10;
    public static final int TLV_TAG_TLV_KEY_INFO = 15;
    public static final int TLV_TAG_WRAPPED_KEY = 14;
    public static final int TLV_TAG_WRAPPED_PCR = 11;
    private HashMap<Integer, byte[]> mTlvList = new HashMap();

    public int getTotalSize() {
        return this.mTlvList.size();
    }

    public boolean setTlv(int tlvTag, byte[] tlvValue) {
        if (tlvTag < 1 || tlvTag >= 17) {
            return false;
        }
        this.mTlvList.put(Integer.valueOf(tlvTag), tlvValue);
        return true;
    }

    public byte[] getTlvValue(int tlvTag) {
        if (tlvTag < 1 || tlvTag >= 17) {
            return null;
        }
        return (byte[]) this.mTlvList.get(Integer.valueOf(tlvTag));
    }

    public Set<Integer> getValidKeyList() {
        return this.mTlvList.keySet();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mTlvList.size());
        for (Integer intValue : this.mTlvList.keySet()) {
            int key = intValue.intValue();
            dest.writeInt(key);
            dest.writeInt(((byte[]) this.mTlvList.get(Integer.valueOf(key))).length);
            dest.writeByteArray((byte[]) this.mTlvList.get(Integer.valueOf(key)));
        }
    }
}
