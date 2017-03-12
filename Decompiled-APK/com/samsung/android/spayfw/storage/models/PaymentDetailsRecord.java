package com.samsung.android.spayfw.storage.models;

import com.samsung.android.spayfw.appinterface.InAppTransactionInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Location;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Wifi;
import java.util.ArrayList;

public class PaymentDetailsRecord {
    private String authenticationMode;
    private String barcodeAttempted;
    private float batteryLevel;
    private String cardId;
    private String cardName;
    private long elapsedTime;
    private InAppTransactionInfo inAppTransactionInfo;
    private Location location;
    private String mstAttempted;
    private String mstCancelled;
    private int mstLoopcount;
    private String mstRetryAttempted;
    private String mstRetryCancelled;
    private int mstRetryLoopcount;
    private String mstSequenceId;
    private String nfcAttempted;
    private String nfcCompleted;
    private String nfcRetryAttempted;
    private String nfcRetryCompleted;
    private String osName;
    private String osVersion;
    private String paymentType;
    private String pfVersion;
    private String rscAttemptRequestId;
    private String samsungpayVersion;
    private long timeStamp;
    private String trTokenId;
    private String txnId;
    private ArrayList<Wifi> wifi;

    public String getAuthenticationMode() {
        return this.authenticationMode;
    }

    public void setAuthenticationMode(String str) {
        this.authenticationMode = str;
    }

    public String getBarcodeAttempted() {
        return this.barcodeAttempted;
    }

    public void setBarcodeAttempted(String str) {
        this.barcodeAttempted = str;
    }

    public float getBatteryLevel() {
        return this.batteryLevel;
    }

    public void setBatteryLevel(float f) {
        this.batteryLevel = f;
    }

    public String getCardId() {
        return this.cardId;
    }

    public void setCardId(String str) {
        this.cardId = str;
    }

    public String getCardName() {
        return this.cardName;
    }

    public void setCardName(String str) {
        this.cardName = str;
    }

    public long getElapsedTime() {
        return this.elapsedTime;
    }

    public void setElapsedTime(long j) {
        this.elapsedTime = j;
    }

    public InAppTransactionInfo getInAppTransactionInfo() {
        return this.inAppTransactionInfo;
    }

    public void setInAppTransactionInfo(InAppTransactionInfo inAppTransactionInfo) {
        this.inAppTransactionInfo = inAppTransactionInfo;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getMstAttempted() {
        return this.mstAttempted;
    }

    public void setMstAttempted(String str) {
        this.mstAttempted = str;
    }

    public String getMstCancelled() {
        return this.mstCancelled;
    }

    public void setMstCancelled(String str) {
        this.mstCancelled = str;
    }

    public int getMstLoopcount() {
        return this.mstLoopcount;
    }

    public void setMstLoopcount(int i) {
        this.mstLoopcount = i;
    }

    public String getMstRetryAttempted() {
        return this.mstRetryAttempted;
    }

    public void setMstRetryAttempted(String str) {
        this.mstRetryAttempted = str;
    }

    public String getMstRetryCancelled() {
        return this.mstRetryCancelled;
    }

    public void setMstRetryCancelled(String str) {
        this.mstRetryCancelled = str;
    }

    public int getMstRetryLoopcount() {
        return this.mstRetryLoopcount;
    }

    public void setMstRetryLoopcount(int i) {
        this.mstRetryLoopcount = i;
    }

    public String getMstSequenceId() {
        return this.mstSequenceId;
    }

    public void setMstSequenceId(String str) {
        this.mstSequenceId = str;
    }

    public String getNfcAttempted() {
        return this.nfcAttempted;
    }

    public void setNfcAttempted(String str) {
        this.nfcAttempted = str;
    }

    public String getNfcCompleted() {
        return this.nfcCompleted;
    }

    public void setNfcCompleted(String str) {
        this.nfcCompleted = str;
    }

    public String getNfcRetryAttempted() {
        return this.nfcRetryAttempted;
    }

    public void setNfcRetryAttempted(String str) {
        this.nfcRetryAttempted = str;
    }

    public String getNfcRetryCompleted() {
        return this.nfcRetryCompleted;
    }

    public void setNfcRetryCompleted(String str) {
        this.nfcRetryCompleted = str;
    }

    public String getOsName() {
        return this.osName;
    }

    public void setOsName(String str) {
        this.osName = str;
    }

    public String getOsVersion() {
        return this.osVersion;
    }

    public void setOsVersion(String str) {
        this.osVersion = str;
    }

    public String getPaymentType() {
        return this.paymentType;
    }

    public void setPaymentType(String str) {
        this.paymentType = str;
    }

    public String getPfVersion() {
        return this.pfVersion;
    }

    public void setPfVersion(String str) {
        this.pfVersion = str;
    }

    public String getSamsungpayVersion() {
        return this.samsungpayVersion;
    }

    public void setSamsungpayVersion(String str) {
        this.samsungpayVersion = str;
    }

    public String getRscAttemptRequestId() {
        return this.rscAttemptRequestId;
    }

    public void setRscAttemptRequestId(String str) {
        this.rscAttemptRequestId = str;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long j) {
        this.timeStamp = j;
    }

    public String getTrTokenId() {
        return this.trTokenId;
    }

    public void setTrTokenId(String str) {
        this.trTokenId = str;
    }

    public String getTxnId() {
        return this.txnId;
    }

    public void setTxnId(String str) {
        this.txnId = str;
    }

    public ArrayList<Wifi> getWifi() {
        return this.wifi;
    }

    public void setWifi(ArrayList<Wifi> arrayList) {
        this.wifi = arrayList;
    }
}
