package com.samsung.android.spayfw.payprovider.discover.payment.data.profile;

import com.samsung.android.spayfw.payprovider.discover.payment.data.PDOLCheckEntry;
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
    private HashMap<Integer, DiscoverPaymentProfile> mPaymentProfiles;
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

    public DiscoverContactlessPaymentData() {
        this.mPaymentProfiles = new HashMap();
    }

    public DiscoverApplicationData getDiscoverApplicationData() {
        return this.mDiscoverApplicationData;
    }

    public void setDiscoverApplicationData(DiscoverApplicationData discoverApplicationData) {
        this.mDiscoverApplicationData = discoverApplicationData;
    }

    public void setIssuerApplicationData(DiscoverIssuerOptions discoverIssuerOptions) {
        this.mIssuerApplicationData = discoverIssuerOptions;
    }

    public DiscoverIssuerOptions getIssuerApplicationData() {
        return this.mIssuerApplicationData;
    }

    public List<DiscoverRecord> getRecords() {
        return this.mRecords;
    }

    public void setRecords(List<DiscoverRecord> list) {
        this.mRecords = list;
    }

    public ByteBuffer getAid() {
        return this.mAid;
    }

    public void setAid(ByteBuffer byteBuffer) {
        this.mAid = byteBuffer;
    }

    public ByteBuffer getFciPpse() {
        return this.mFciPpse;
    }

    public void setFciPpse(ByteBuffer byteBuffer) {
        this.mFciPpse = byteBuffer;
    }

    public ByteBuffer getFciMainAid() {
        return this.mFciMainAid;
    }

    public void setFciMainAid(ByteBuffer byteBuffer) {
        this.mFciMainAid = byteBuffer;
    }

    public ByteBuffer getFciZipAid() {
        return this.mFciZipAid;
    }

    public void setFciZipAid(ByteBuffer byteBuffer) {
        this.mFciZipAid = byteBuffer;
    }

    public ByteBuffer getFciDebitAid() {
        return this.mFciDebitAid;
    }

    public void setFciDebitAid(ByteBuffer byteBuffer) {
        this.mFciDebitAid = byteBuffer;
    }

    public HashMap<String, ByteBuffer> getFciAltAid() {
        return this.mFciAltAid;
    }

    public void setFciAltAid(HashMap<String, ByteBuffer> hashMap) {
        this.mFciAltAid = hashMap;
    }

    public ByteBuffer getPasscodeRetryCounter() {
        return this.mPasscodeRetryCounter;
    }

    public void setPasscodeRetryCounter(ByteBuffer byteBuffer) {
        this.mPasscodeRetryCounter = byteBuffer;
    }

    public ByteBuffer getSecondaryCurrency1() {
        return this.mSecondaryCurrency1;
    }

    public void setSecondaryCurrency1(ByteBuffer byteBuffer) {
        this.mSecondaryCurrency1 = byteBuffer;
    }

    public ByteBuffer getSecondaryCurrency2() {
        return this.mSecondaryCurrency2;
    }

    public void setSecondaryCurrency2(ByteBuffer byteBuffer) {
        this.mSecondaryCurrency2 = byteBuffer;
    }

    public String getServiceCode() {
        return this.mServiceCode;
    }

    public void setServiceCode(String str) {
        this.mServiceCode = str;
    }

    public ByteBuffer getTrack1DataZipMsMode() {
        return this.mTrack1DataZipMsMode;
    }

    public void setTrack1DataZipMsMode(ByteBuffer byteBuffer) {
        this.mTrack1DataZipMsMode = byteBuffer;
    }

    public ByteBuffer getTrack2DataZipMsMode() {
        return this.mTrack2DataZipMsMode;
    }

    public void setTrack2DataZipMsMode(ByteBuffer byteBuffer) {
        this.mTrack2DataZipMsMode = byteBuffer;
    }

    public ByteBuffer getTrack2EquivalentData() {
        return this.mTrack2EquivalentData;
    }

    public void setTrack2EquivalentData(ByteBuffer byteBuffer) {
        this.mTrack2EquivalentData = byteBuffer;
    }

    public ByteBuffer getTrack1DataMstMode() {
        return this.mTrack1DataMstMode;
    }

    public void setTrack1DataMstMode(ByteBuffer byteBuffer) {
        this.mTrack1DataMstMode = byteBuffer;
    }

    public ByteBuffer getTrack2DataMstMode() {
        return this.mTrack2DataMstMode;
    }

    public void setTrack2DataMstMode(ByteBuffer byteBuffer) {
        this.mTrack2DataMstMode = byteBuffer;
    }

    public void setZipAip(ByteBuffer byteBuffer) {
        this.mZipAip = byteBuffer;
    }

    public ByteBuffer getZipAip() {
        return this.mZipAip;
    }

    public void setZipAfl(ByteBuffer byteBuffer) {
        this.mZipAfl = byteBuffer;
    }

    public ByteBuffer getZipAfl() {
        return this.mZipAfl;
    }

    public void setPdolProfileEntries(PDOLCheckEntry[] pDOLCheckEntryArr) {
        this.mPdolProfileEntries = pDOLCheckEntryArr;
    }

    public void setPdolOnlineEntries(PDOLCheckEntry[] pDOLCheckEntryArr) {
        this.mPdolOnlineEntries = pDOLCheckEntryArr;
    }

    public void setPdolDeclineEntries(PDOLCheckEntry[] pDOLCheckEntryArr) {
        this.mPdolDeclineEntries = pDOLCheckEntryArr;
    }

    public PDOLCheckEntry[] getPdolProfileEntries() {
        return this.mPdolProfileEntries;
    }

    public PDOLCheckEntry[] getPdolOnlineEntries() {
        return this.mPdolOnlineEntries;
    }

    public PDOLCheckEntry[] getPdolDeclineEntries() {
        return this.mPdolDeclineEntries;
    }

    public ByteBuffer getCountryCode() {
        return this.mCountryCode;
    }

    public void setCountryCode(ByteBuffer byteBuffer) {
        this.mCountryCode = byteBuffer;
    }

    public ByteBuffer getCurrencyCodeCode() {
        return this.mCurrencyCode;
    }

    public void setCurrencyCode(ByteBuffer byteBuffer) {
        this.mCurrencyCode = byteBuffer;
    }

    public HashMap<Integer, DiscoverPaymentProfile> getPaymentProfiles() {
        return this.mPaymentProfiles;
    }

    public void setPaymentProfiles(HashMap<Integer, DiscoverPaymentProfile> hashMap) {
        this.mPaymentProfiles = hashMap;
    }

    public ByteBuffer getCaco() {
        return this.mCaco;
    }

    public void setCaco(ByteBuffer byteBuffer) {
        this.mCaco = byteBuffer;
    }

    public void setPth(ByteBuffer byteBuffer) {
        this.mPth = byteBuffer;
    }

    public ByteBuffer getPth() {
        return this.mPth;
    }

    public ByteBuffer getPDOLProfileCheckTable() {
        return this.mPDOLProfileCheckTable;
    }

    public void setPDOLProfileCheckTable(ByteBuffer byteBuffer) {
        this.mPDOLProfileCheckTable = byteBuffer;
    }

    public void setAliasList(List<ByteBuffer> list) {
        this.mAliasList = list;
    }

    public List<ByteBuffer> getAliasList() {
        return this.mAliasList;
    }
}
