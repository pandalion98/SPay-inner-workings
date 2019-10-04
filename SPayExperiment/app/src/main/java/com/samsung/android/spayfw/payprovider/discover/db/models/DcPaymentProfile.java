/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  java.io.UnsupportedEncodingException
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.discover.db.models;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.discover.db.DcDbException;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentProfile;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import java.io.UnsupportedEncodingException;

public class DcPaymentProfile {
    private static final String TAG = "DCSDK_DcPaymentProfile";
    protected static final String TEXT_ENCODING = "UTF8";
    private static Gson mGson = new GsonBuilder().create();
    private byte[] mAfl;
    private byte[] mAip;
    private byte[] mAuc;
    private byte[] mBlob1;
    private byte[] mBlob2;
    private long mCardMasterId = -1L;
    private byte[] mCl;
    private byte[] mCpr;
    private byte[] mCrm;
    private byte[] mCtq;
    private byte[] mCvm;
    private String mData1;
    private String mData2;
    private long mProfileId;
    private byte[] mPru;
    private long mRowId = -1L;

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static DiscoverPaymentProfile toDiscoverPaymentProfile(DcPaymentProfile dcPaymentProfile) {
        byte[] arrby;
        byte[] arrby2;
        byte[] arrby3;
        byte[] arrby4;
        byte[] arrby5;
        if (dcPaymentProfile == null) {
            c.e(TAG, "toDiscoverPaymentProfile: input null");
            return null;
        }
        DiscoverPaymentProfile discoverPaymentProfile = new DiscoverPaymentProfile();
        discoverPaymentProfile.setProfileId((int)dcPaymentProfile.getProfileId());
        byte[] arrby6 = dcPaymentProfile.getCtq();
        if (arrby6 != null) {
            discoverPaymentProfile.setCtq(new ByteBuffer(arrby6));
        }
        if ((arrby3 = dcPaymentProfile.getAuc()) != null) {
            discoverPaymentProfile.setApplicationUsageControl(new ByteBuffer(arrby3));
        }
        if ((arrby = dcPaymentProfile.getPru()) != null) {
            discoverPaymentProfile.setPru(new ByteBuffer(arrby));
        }
        if ((arrby2 = dcPaymentProfile.getAip()) != null) {
            discoverPaymentProfile.setAip(new ByteBuffer(arrby2));
        }
        if ((arrby4 = dcPaymentProfile.getAfl()) != null) {
            discoverPaymentProfile.setAfl(new ByteBuffer(arrby4));
        }
        if ((arrby5 = dcPaymentProfile.getCpr()) != null) {
            discoverPaymentProfile.setCpr(new ByteBuffer(arrby5));
        }
        try {
            String string = new String(dcPaymentProfile.getCrm(), TEXT_ENCODING);
            discoverPaymentProfile.setCRM((DiscoverPaymentProfile.CRM)mGson.fromJson(string, DiscoverPaymentProfile.CRM.class));
            String string2 = new String(dcPaymentProfile.getCvm(), TEXT_ENCODING);
            discoverPaymentProfile.setCVM((DiscoverPaymentProfile.CVM)mGson.fromJson(string2, DiscoverPaymentProfile.CVM.class));
            String string3 = new String(dcPaymentProfile.getCl(), TEXT_ENCODING);
            discoverPaymentProfile.setCl((DiscoverPaymentProfile.CL)mGson.fromJson(string3, DiscoverPaymentProfile.CL.class));
            do {
                return discoverPaymentProfile;
                break;
            } while (true);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
            return discoverPaymentProfile;
        }
    }

    public byte[] getAfl() {
        return this.mAfl;
    }

    public byte[] getAip() {
        return this.mAip;
    }

    public byte[] getAuc() {
        return this.mAuc;
    }

    public byte[] getBlob1() {
        return this.mBlob1;
    }

    public byte[] getBlob2() {
        return this.mBlob2;
    }

    public long getCardMasterId() {
        return this.mCardMasterId;
    }

    public byte[] getCl() {
        return this.mCl;
    }

    public byte[] getCpr() {
        return this.mCpr;
    }

    public byte[] getCrm() {
        return this.mCrm;
    }

    public byte[] getCtq() {
        return this.mCtq;
    }

    public byte[] getCvm() {
        return this.mCvm;
    }

    public String getData1() {
        return this.mData1;
    }

    public String getData2() {
        return this.mData2;
    }

    public long getProfileId() {
        return this.mProfileId;
    }

    public byte[] getPru() {
        return this.mPru;
    }

    public long getRowId() {
        return this.mRowId;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean init(long l2, DiscoverPaymentProfile discoverPaymentProfile) {
        ByteBuffer byteBuffer;
        ByteBuffer byteBuffer2;
        String string;
        ByteBuffer byteBuffer3;
        ByteBuffer byteBuffer4;
        ByteBuffer byteBuffer5;
        if (discoverPaymentProfile == null) {
            c.e(TAG, "init: data Null");
            throw new DcDbException("Invalid Input", 3);
        }
        this.mCardMasterId = l2;
        this.mProfileId = discoverPaymentProfile.getProfileId();
        ByteBuffer byteBuffer6 = discoverPaymentProfile.getCtq();
        if (byteBuffer6 != null) {
            this.mCtq = byteBuffer6.getBytes();
        }
        if ((byteBuffer2 = discoverPaymentProfile.getApplicationUsageControl()) != null) {
            this.mAuc = byteBuffer2.getBytes();
        }
        if ((byteBuffer3 = discoverPaymentProfile.getPru()) != null) {
            this.mPru = byteBuffer3.getBytes();
        }
        if ((byteBuffer5 = discoverPaymentProfile.getAip()) != null) {
            this.mAip = byteBuffer5.getBytes();
        }
        if ((byteBuffer = discoverPaymentProfile.getAfl()) != null) {
            this.mAfl = byteBuffer.getBytes();
        }
        if ((byteBuffer4 = discoverPaymentProfile.getCpr()) != null) {
            this.mCpr = byteBuffer4.getBytes();
        }
        try {
            String string2 = mGson.toJson((Object)discoverPaymentProfile.getCRM());
            if (TextUtils.isEmpty((CharSequence)string2)) {
                c.e(TAG, "init: jsonData failed");
                throw new DcDbException("Invalid Input", 3);
            }
            this.mCrm = string2.getBytes(TEXT_ENCODING);
            string = mGson.toJson((Object)discoverPaymentProfile.getCVM());
            if (TextUtils.isEmpty((CharSequence)string)) {
                c.e(TAG, "init: jsonData failed");
                throw new DcDbException("Invalid Input", 3);
            }
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            c.e(TAG, "init: UnsupportedEncodingException: " + unsupportedEncodingException.getMessage());
            throw new DcDbException("Invalid Input", 3);
        }
        this.mCvm = string.getBytes(TEXT_ENCODING);
        String string3 = mGson.toJson((Object)discoverPaymentProfile.getCl());
        if (TextUtils.isEmpty((CharSequence)string3)) {
            c.e(TAG, "init: jsonData failed");
            throw new DcDbException("Invalid Input", 3);
        }
        this.mCl = string3.getBytes(TEXT_ENCODING);
        return true;
    }

    public void setAfl(byte[] arrby) {
        this.mAfl = arrby;
    }

    public void setAip(byte[] arrby) {
        this.mAip = arrby;
    }

    public void setAuc(byte[] arrby) {
        this.mAuc = arrby;
    }

    public void setBlob1(byte[] arrby) {
        this.mBlob1 = arrby;
    }

    public void setBlob2(byte[] arrby) {
        this.mBlob2 = arrby;
    }

    public void setCardMasterId(long l2) {
        this.mCardMasterId = l2;
    }

    public void setCl(byte[] arrby) {
        this.mCl = arrby;
    }

    public void setCpr(byte[] arrby) {
        this.mCpr = arrby;
    }

    public void setCrm(byte[] arrby) {
        this.mCrm = arrby;
    }

    public void setCtq(byte[] arrby) {
        this.mCtq = arrby;
    }

    public void setCvm(byte[] arrby) {
        this.mCvm = arrby;
    }

    public void setData1(String string) {
        this.mData1 = string;
    }

    public void setData2(String string) {
        this.mData2 = string;
    }

    public void setProfileId(long l2) {
        this.mProfileId = l2;
    }

    public void setPru(byte[] arrby) {
        this.mPru = arrby;
    }

    public void setRowId(long l2) {
        this.mRowId = l2;
    }
}

