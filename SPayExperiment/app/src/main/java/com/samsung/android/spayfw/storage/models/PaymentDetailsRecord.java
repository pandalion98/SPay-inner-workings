/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 */
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

    public String getBarcodeAttempted() {
        return this.barcodeAttempted;
    }

    public float getBatteryLevel() {
        return this.batteryLevel;
    }

    public String getCardId() {
        return this.cardId;
    }

    public String getCardName() {
        return this.cardName;
    }

    public long getElapsedTime() {
        return this.elapsedTime;
    }

    public InAppTransactionInfo getInAppTransactionInfo() {
        return this.inAppTransactionInfo;
    }

    public Location getLocation() {
        return this.location;
    }

    public String getMstAttempted() {
        return this.mstAttempted;
    }

    public String getMstCancelled() {
        return this.mstCancelled;
    }

    public int getMstLoopcount() {
        return this.mstLoopcount;
    }

    public String getMstRetryAttempted() {
        return this.mstRetryAttempted;
    }

    public String getMstRetryCancelled() {
        return this.mstRetryCancelled;
    }

    public int getMstRetryLoopcount() {
        return this.mstRetryLoopcount;
    }

    public String getMstSequenceId() {
        return this.mstSequenceId;
    }

    public String getNfcAttempted() {
        return this.nfcAttempted;
    }

    public String getNfcCompleted() {
        return this.nfcCompleted;
    }

    public String getNfcRetryAttempted() {
        return this.nfcRetryAttempted;
    }

    public String getNfcRetryCompleted() {
        return this.nfcRetryCompleted;
    }

    public String getOsName() {
        return this.osName;
    }

    public String getOsVersion() {
        return this.osVersion;
    }

    public String getPaymentType() {
        return this.paymentType;
    }

    public String getPfVersion() {
        return this.pfVersion;
    }

    public String getRscAttemptRequestId() {
        return this.rscAttemptRequestId;
    }

    public String getSamsungpayVersion() {
        return this.samsungpayVersion;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public String getTrTokenId() {
        return this.trTokenId;
    }

    public String getTxnId() {
        return this.txnId;
    }

    public ArrayList<Wifi> getWifi() {
        return this.wifi;
    }

    public void setAuthenticationMode(String string) {
        this.authenticationMode = string;
    }

    public void setBarcodeAttempted(String string) {
        this.barcodeAttempted = string;
    }

    public void setBatteryLevel(float f2) {
        this.batteryLevel = f2;
    }

    public void setCardId(String string) {
        this.cardId = string;
    }

    public void setCardName(String string) {
        this.cardName = string;
    }

    public void setElapsedTime(long l2) {
        this.elapsedTime = l2;
    }

    public void setInAppTransactionInfo(InAppTransactionInfo inAppTransactionInfo) {
        this.inAppTransactionInfo = inAppTransactionInfo;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setMstAttempted(String string) {
        this.mstAttempted = string;
    }

    public void setMstCancelled(String string) {
        this.mstCancelled = string;
    }

    public void setMstLoopcount(int n2) {
        this.mstLoopcount = n2;
    }

    public void setMstRetryAttempted(String string) {
        this.mstRetryAttempted = string;
    }

    public void setMstRetryCancelled(String string) {
        this.mstRetryCancelled = string;
    }

    public void setMstRetryLoopcount(int n2) {
        this.mstRetryLoopcount = n2;
    }

    public void setMstSequenceId(String string) {
        this.mstSequenceId = string;
    }

    public void setNfcAttempted(String string) {
        this.nfcAttempted = string;
    }

    public void setNfcCompleted(String string) {
        this.nfcCompleted = string;
    }

    public void setNfcRetryAttempted(String string) {
        this.nfcRetryAttempted = string;
    }

    public void setNfcRetryCompleted(String string) {
        this.nfcRetryCompleted = string;
    }

    public void setOsName(String string) {
        this.osName = string;
    }

    public void setOsVersion(String string) {
        this.osVersion = string;
    }

    public void setPaymentType(String string) {
        this.paymentType = string;
    }

    public void setPfVersion(String string) {
        this.pfVersion = string;
    }

    public void setRscAttemptRequestId(String string) {
        this.rscAttemptRequestId = string;
    }

    public void setSamsungpayVersion(String string) {
        this.samsungpayVersion = string;
    }

    public void setTimeStamp(long l2) {
        this.timeStamp = l2;
    }

    public void setTrTokenId(String string) {
        this.trTokenId = string;
    }

    public void setTxnId(String string) {
        this.txnId = string;
    }

    public void setWifi(ArrayList<Wifi> arrayList) {
        this.wifi = arrayList;
    }
}

