package com.samsung.android.spayfw.payprovider.discover.db.models;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.discover.db.DcDbException;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentProfile;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentProfile.CL;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentProfile.CRM;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentProfile.CVM;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import java.io.UnsupportedEncodingException;

public class DcPaymentProfile {
    private static final String TAG = "DCSDK_DcPaymentProfile";
    protected static final String TEXT_ENCODING = "UTF8";
    private static Gson mGson;
    private byte[] mAfl;
    private byte[] mAip;
    private byte[] mAuc;
    private byte[] mBlob1;
    private byte[] mBlob2;
    private long mCardMasterId;
    private byte[] mCl;
    private byte[] mCpr;
    private byte[] mCrm;
    private byte[] mCtq;
    private byte[] mCvm;
    private String mData1;
    private String mData2;
    private long mProfileId;
    private byte[] mPru;
    private long mRowId;

    public DcPaymentProfile() {
        this.mRowId = -1;
        this.mCardMasterId = -1;
    }

    static {
        mGson = new GsonBuilder().create();
    }

    public boolean init(long j, DiscoverPaymentProfile discoverPaymentProfile) {
        if (discoverPaymentProfile == null) {
            Log.m286e(TAG, "init: data Null");
            throw new DcDbException("Invalid Input", 3);
        }
        this.mCardMasterId = j;
        this.mProfileId = (long) discoverPaymentProfile.getProfileId();
        ByteBuffer ctq = discoverPaymentProfile.getCtq();
        if (ctq != null) {
            this.mCtq = ctq.getBytes();
        }
        ctq = discoverPaymentProfile.getApplicationUsageControl();
        if (ctq != null) {
            this.mAuc = ctq.getBytes();
        }
        ctq = discoverPaymentProfile.getPru();
        if (ctq != null) {
            this.mPru = ctq.getBytes();
        }
        ctq = discoverPaymentProfile.getAip();
        if (ctq != null) {
            this.mAip = ctq.getBytes();
        }
        ctq = discoverPaymentProfile.getAfl();
        if (ctq != null) {
            this.mAfl = ctq.getBytes();
        }
        ctq = discoverPaymentProfile.getCpr();
        if (ctq != null) {
            this.mCpr = ctq.getBytes();
        }
        try {
            Object toJson = mGson.toJson(discoverPaymentProfile.getCRM());
            if (TextUtils.isEmpty(toJson)) {
                Log.m286e(TAG, "init: jsonData failed");
                throw new DcDbException("Invalid Input", 3);
            }
            this.mCrm = toJson.getBytes(TEXT_ENCODING);
            toJson = mGson.toJson(discoverPaymentProfile.getCVM());
            if (TextUtils.isEmpty(toJson)) {
                Log.m286e(TAG, "init: jsonData failed");
                throw new DcDbException("Invalid Input", 3);
            }
            this.mCvm = toJson.getBytes(TEXT_ENCODING);
            toJson = mGson.toJson(discoverPaymentProfile.getCl());
            if (TextUtils.isEmpty(toJson)) {
                Log.m286e(TAG, "init: jsonData failed");
                throw new DcDbException("Invalid Input", 3);
            }
            this.mCl = toJson.getBytes(TEXT_ENCODING);
            return true;
        } catch (UnsupportedEncodingException e) {
            Log.m286e(TAG, "init: UnsupportedEncodingException: " + e.getMessage());
            throw new DcDbException("Invalid Input", 3);
        }
    }

    public long getCardMasterId() {
        return this.mCardMasterId;
    }

    public void setCardMasterId(long j) {
        this.mCardMasterId = j;
    }

    public long getProfileId() {
        return this.mProfileId;
    }

    public void setProfileId(long j) {
        this.mProfileId = j;
    }

    public byte[] getCtq() {
        return this.mCtq;
    }

    public void setCtq(byte[] bArr) {
        this.mCtq = bArr;
    }

    public byte[] getAuc() {
        return this.mAuc;
    }

    public void setAuc(byte[] bArr) {
        this.mAuc = bArr;
    }

    public byte[] getPru() {
        return this.mPru;
    }

    public void setPru(byte[] bArr) {
        this.mPru = bArr;
    }

    public byte[] getAip() {
        return this.mAip;
    }

    public void setAip(byte[] bArr) {
        this.mAip = bArr;
    }

    public byte[] getAfl() {
        return this.mAfl;
    }

    public void setAfl(byte[] bArr) {
        this.mAfl = bArr;
    }

    public byte[] getCpr() {
        return this.mCpr;
    }

    public void setCpr(byte[] bArr) {
        this.mCpr = bArr;
    }

    public byte[] getCrm() {
        return this.mCrm;
    }

    public void setCrm(byte[] bArr) {
        this.mCrm = bArr;
    }

    public byte[] getCvm() {
        return this.mCvm;
    }

    public void setCvm(byte[] bArr) {
        this.mCvm = bArr;
    }

    public byte[] getCl() {
        return this.mCl;
    }

    public void setCl(byte[] bArr) {
        this.mCl = bArr;
    }

    public String getData1() {
        return this.mData1;
    }

    public void setData1(String str) {
        this.mData1 = str;
    }

    public String getData2() {
        return this.mData2;
    }

    public void setData2(String str) {
        this.mData2 = str;
    }

    public byte[] getBlob1() {
        return this.mBlob1;
    }

    public void setBlob1(byte[] bArr) {
        this.mBlob1 = bArr;
    }

    public byte[] getBlob2() {
        return this.mBlob2;
    }

    public void setBlob2(byte[] bArr) {
        this.mBlob2 = bArr;
    }

    public static DiscoverPaymentProfile toDiscoverPaymentProfile(DcPaymentProfile dcPaymentProfile) {
        if (dcPaymentProfile == null) {
            Log.m286e(TAG, "toDiscoverPaymentProfile: input null");
            return null;
        }
        DiscoverPaymentProfile discoverPaymentProfile = new DiscoverPaymentProfile();
        discoverPaymentProfile.setProfileId((int) dcPaymentProfile.getProfileId());
        byte[] ctq = dcPaymentProfile.getCtq();
        if (ctq != null) {
            discoverPaymentProfile.setCtq(new ByteBuffer(ctq));
        }
        ctq = dcPaymentProfile.getAuc();
        if (ctq != null) {
            discoverPaymentProfile.setApplicationUsageControl(new ByteBuffer(ctq));
        }
        ctq = dcPaymentProfile.getPru();
        if (ctq != null) {
            discoverPaymentProfile.setPru(new ByteBuffer(ctq));
        }
        ctq = dcPaymentProfile.getAip();
        if (ctq != null) {
            discoverPaymentProfile.setAip(new ByteBuffer(ctq));
        }
        ctq = dcPaymentProfile.getAfl();
        if (ctq != null) {
            discoverPaymentProfile.setAfl(new ByteBuffer(ctq));
        }
        ctq = dcPaymentProfile.getCpr();
        if (ctq != null) {
            discoverPaymentProfile.setCpr(new ByteBuffer(ctq));
        }
        try {
            discoverPaymentProfile.setCRM((CRM) mGson.fromJson(new String(dcPaymentProfile.getCrm(), TEXT_ENCODING), CRM.class));
            discoverPaymentProfile.setCVM((CVM) mGson.fromJson(new String(dcPaymentProfile.getCvm(), TEXT_ENCODING), CVM.class));
            discoverPaymentProfile.setCl((CL) mGson.fromJson(new String(dcPaymentProfile.getCl(), TEXT_ENCODING), CL.class));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return discoverPaymentProfile;
    }

    public void setRowId(long j) {
        this.mRowId = j;
    }

    public long getRowId() {
        return this.mRowId;
    }
}
