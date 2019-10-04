/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.discover.payment.data.profile;

import com.samsung.android.spayfw.payprovider.discover.payment.data.PDOLCheckEntry;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverApplicationData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverIssuerOptions;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentProfile;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverRecord;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import java.util.HashMap;
import java.util.List;

public class DiscoverContactlessPaymentData {
    private ByteBuffer mAid;
    private List<ByteBuffer> mAliasList;
    private ByteBuffer mCaco;
    private ByteBuffer mCountryCode;
    private ByteBuffer mCurrencyCode;
    private DiscoverApplicationData mDiscoverApplicationData;
    private HashMap<String, ByteBuffer> mFciAltAid;
    private ByteBuffer mFciDebitAid;
    private ByteBuffer mFciMainAid;
    private ByteBuffer mFciPpse;
    private ByteBuffer mFciZipAid;
    private DiscoverIssuerOptions mIssuerApplicationData;
    private ByteBuffer mPDOLProfileCheckTable;
    private ByteBuffer mPasscodeRetryCounter;
    private HashMap<Integer, DiscoverPaymentProfile> mPaymentProfiles = new HashMap();
    private PDOLCheckEntry[] mPdolDeclineEntries;
    private PDOLCheckEntry[] mPdolOnlineEntries;
    private PDOLCheckEntry[] mPdolProfileEntries;
    private ByteBuffer mPth;
    private List<DiscoverRecord> mRecords;
    private ByteBuffer mSecondaryCurrency1;
    private ByteBuffer mSecondaryCurrency2;
    private String mServiceCode;
    private ByteBuffer mTrack1DataMstMode;
    private ByteBuffer mTrack1DataZipMsMode;
    private ByteBuffer mTrack2DataMstMode;
    private ByteBuffer mTrack2DataZipMsMode;
    private ByteBuffer mTrack2EquivalentData;
    private ByteBuffer mZipAfl;
    private ByteBuffer mZipAip;

    public ByteBuffer getAid() {
        return this.mAid;
    }

    public List<ByteBuffer> getAliasList() {
        return this.mAliasList;
    }

    public ByteBuffer getCaco() {
        return this.mCaco;
    }

    public ByteBuffer getCountryCode() {
        return this.mCountryCode;
    }

    public ByteBuffer getCurrencyCodeCode() {
        return this.mCurrencyCode;
    }

    public DiscoverApplicationData getDiscoverApplicationData() {
        return this.mDiscoverApplicationData;
    }

    public HashMap<String, ByteBuffer> getFciAltAid() {
        return this.mFciAltAid;
    }

    public ByteBuffer getFciDebitAid() {
        return this.mFciDebitAid;
    }

    public ByteBuffer getFciMainAid() {
        return this.mFciMainAid;
    }

    public ByteBuffer getFciPpse() {
        return this.mFciPpse;
    }

    public ByteBuffer getFciZipAid() {
        return this.mFciZipAid;
    }

    public DiscoverIssuerOptions getIssuerApplicationData() {
        return this.mIssuerApplicationData;
    }

    public ByteBuffer getPDOLProfileCheckTable() {
        return this.mPDOLProfileCheckTable;
    }

    public ByteBuffer getPasscodeRetryCounter() {
        return this.mPasscodeRetryCounter;
    }

    public HashMap<Integer, DiscoverPaymentProfile> getPaymentProfiles() {
        return this.mPaymentProfiles;
    }

    public PDOLCheckEntry[] getPdolDeclineEntries() {
        return this.mPdolDeclineEntries;
    }

    public PDOLCheckEntry[] getPdolOnlineEntries() {
        return this.mPdolOnlineEntries;
    }

    public PDOLCheckEntry[] getPdolProfileEntries() {
        return this.mPdolProfileEntries;
    }

    public ByteBuffer getPth() {
        return this.mPth;
    }

    public List<DiscoverRecord> getRecords() {
        return this.mRecords;
    }

    public ByteBuffer getSecondaryCurrency1() {
        return this.mSecondaryCurrency1;
    }

    public ByteBuffer getSecondaryCurrency2() {
        return this.mSecondaryCurrency2;
    }

    public String getServiceCode() {
        return this.mServiceCode;
    }

    public ByteBuffer getTrack1DataMstMode() {
        return this.mTrack1DataMstMode;
    }

    public ByteBuffer getTrack1DataZipMsMode() {
        return this.mTrack1DataZipMsMode;
    }

    public ByteBuffer getTrack2DataMstMode() {
        return this.mTrack2DataMstMode;
    }

    public ByteBuffer getTrack2DataZipMsMode() {
        return this.mTrack2DataZipMsMode;
    }

    public ByteBuffer getTrack2EquivalentData() {
        return this.mTrack2EquivalentData;
    }

    public ByteBuffer getZipAfl() {
        return this.mZipAfl;
    }

    public ByteBuffer getZipAip() {
        return this.mZipAip;
    }

    public void setAid(ByteBuffer byteBuffer) {
        this.mAid = byteBuffer;
    }

    public void setAliasList(List<ByteBuffer> list) {
        this.mAliasList = list;
    }

    public void setCaco(ByteBuffer byteBuffer) {
        this.mCaco = byteBuffer;
    }

    public void setCountryCode(ByteBuffer byteBuffer) {
        this.mCountryCode = byteBuffer;
    }

    public void setCurrencyCode(ByteBuffer byteBuffer) {
        this.mCurrencyCode = byteBuffer;
    }

    public void setDiscoverApplicationData(DiscoverApplicationData discoverApplicationData) {
        this.mDiscoverApplicationData = discoverApplicationData;
    }

    public void setFciAltAid(HashMap<String, ByteBuffer> hashMap) {
        this.mFciAltAid = hashMap;
    }

    public void setFciDebitAid(ByteBuffer byteBuffer) {
        this.mFciDebitAid = byteBuffer;
    }

    public void setFciMainAid(ByteBuffer byteBuffer) {
        this.mFciMainAid = byteBuffer;
    }

    public void setFciPpse(ByteBuffer byteBuffer) {
        this.mFciPpse = byteBuffer;
    }

    public void setFciZipAid(ByteBuffer byteBuffer) {
        this.mFciZipAid = byteBuffer;
    }

    public void setIssuerApplicationData(DiscoverIssuerOptions discoverIssuerOptions) {
        this.mIssuerApplicationData = discoverIssuerOptions;
    }

    public void setPDOLProfileCheckTable(ByteBuffer byteBuffer) {
        this.mPDOLProfileCheckTable = byteBuffer;
    }

    public void setPasscodeRetryCounter(ByteBuffer byteBuffer) {
        this.mPasscodeRetryCounter = byteBuffer;
    }

    public void setPaymentProfiles(HashMap<Integer, DiscoverPaymentProfile> hashMap) {
        this.mPaymentProfiles = hashMap;
    }

    public void setPdolDeclineEntries(PDOLCheckEntry[] arrpDOLCheckEntry) {
        this.mPdolDeclineEntries = arrpDOLCheckEntry;
    }

    public void setPdolOnlineEntries(PDOLCheckEntry[] arrpDOLCheckEntry) {
        this.mPdolOnlineEntries = arrpDOLCheckEntry;
    }

    public void setPdolProfileEntries(PDOLCheckEntry[] arrpDOLCheckEntry) {
        this.mPdolProfileEntries = arrpDOLCheckEntry;
    }

    public void setPth(ByteBuffer byteBuffer) {
        this.mPth = byteBuffer;
    }

    public void setRecords(List<DiscoverRecord> list) {
        this.mRecords = list;
    }

    public void setSecondaryCurrency1(ByteBuffer byteBuffer) {
        this.mSecondaryCurrency1 = byteBuffer;
    }

    public void setSecondaryCurrency2(ByteBuffer byteBuffer) {
        this.mSecondaryCurrency2 = byteBuffer;
    }

    public void setServiceCode(String string) {
        this.mServiceCode = string;
    }

    public void setTrack1DataMstMode(ByteBuffer byteBuffer) {
        this.mTrack1DataMstMode = byteBuffer;
    }

    public void setTrack1DataZipMsMode(ByteBuffer byteBuffer) {
        this.mTrack1DataZipMsMode = byteBuffer;
    }

    public void setTrack2DataMstMode(ByteBuffer byteBuffer) {
        this.mTrack2DataMstMode = byteBuffer;
    }

    public void setTrack2DataZipMsMode(ByteBuffer byteBuffer) {
        this.mTrack2DataZipMsMode = byteBuffer;
    }

    public void setTrack2EquivalentData(ByteBuffer byteBuffer) {
        this.mTrack2EquivalentData = byteBuffer;
    }

    public void setZipAfl(ByteBuffer byteBuffer) {
        this.mZipAfl = byteBuffer;
    }

    public void setZipAip(ByteBuffer byteBuffer) {
        this.mZipAip = byteBuffer;
    }
}

