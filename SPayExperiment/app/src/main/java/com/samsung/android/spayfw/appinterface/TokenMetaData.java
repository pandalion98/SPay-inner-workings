/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.Class
 *  java.lang.ClassLoader
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.samsung.android.spayfw.appinterface.CardArts;
import com.samsung.android.spayfw.appinterface.CardColors;
import com.samsung.android.spayfw.appinterface.CardIssuer;
import java.util.ArrayList;
import java.util.List;

public class TokenMetaData
implements Parcelable {
    public static final String BUNDLE_KEY_EXTRA_META_DATA = "extraMetaData";
    public static final String BUNDLE_KEY_EXTRA_META_DATA_FD = "extraMetaDataFd";
    public static final String BUNDLE_KEY_EXTRA_META_DATA_FILE_PATH = "extraMetaDataFilePath";
    public static final Parcelable.Creator<TokenMetaData> CREATOR = new Parcelable.Creator<TokenMetaData>(){

        public TokenMetaData createFromParcel(Parcel parcel) {
            return new TokenMetaData(parcel);
        }

        public TokenMetaData[] newArray(int n2) {
            return new TokenMetaData[n2];
        }
    };
    private List<CardArts> cardArts = new ArrayList();
    private List<CardColors> cardColors = new ArrayList();
    private String cardExpiryMonth;
    private String cardExpiryYear;
    private String cardHolderName;
    private CardIssuer cardIssuer;
    private String cardNetwork;
    private int cardPresentationMode = 15;
    private String cardProductDesc;
    private String cardProductName;
    private String cardRefId;
    private String cardSuffix;
    private String cardType;
    private Bundle extraMetaData;
    private String securityCode;

    public TokenMetaData() {
    }

    private TokenMetaData(Parcel parcel) {
        this();
        this.readFromParcel(parcel);
    }

    /*
     * Exception decompiling
     */
    private void copyContentFromFd() {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [19[TRYBLOCK]], but top level block is 4[TRYBLOCK]
        // org.benf.cfr.reader.b.a.a.j.a(Op04StructuredStatement.java:432)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:484)
        // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    public int describeContents() {
        return 0;
    }

    public List<CardArts> getCardArts() {
        return this.cardArts;
    }

    public List<CardColors> getCardColors() {
        return this.cardColors;
    }

    public String getCardExpiryMonth() {
        return this.cardExpiryMonth;
    }

    public String getCardExpiryYear() {
        return this.cardExpiryYear;
    }

    public String getCardHolderName() {
        return this.cardHolderName;
    }

    public CardIssuer getCardIssuer() {
        return this.cardIssuer;
    }

    public String getCardNetworkType() {
        return this.cardNetwork;
    }

    public int getCardPresentationMode() {
        return this.cardPresentationMode;
    }

    public String getCardProductDesc() {
        return this.cardProductDesc;
    }

    public String getCardProductName() {
        return this.cardProductName;
    }

    public String getCardRefernceId() {
        return this.cardRefId;
    }

    public String getCardSuffix() {
        return this.cardSuffix;
    }

    public String getCardType() {
        return this.cardType;
    }

    public Bundle getExtraMetaData() {
        return this.extraMetaData;
    }

    public String getSecurityCode() {
        return this.securityCode;
    }

    public void readFromParcel(Parcel parcel) {
        this.cardNetwork = parcel.readString();
        this.cardType = parcel.readString();
        this.cardProductName = parcel.readString();
        this.cardProductDesc = parcel.readString();
        this.cardSuffix = parcel.readString();
        this.cardRefId = parcel.readString();
        this.cardExpiryMonth = parcel.readString();
        this.cardExpiryYear = parcel.readString();
        parcel.readList(this.cardArts, this.getClass().getClassLoader());
        parcel.readList(this.cardColors, this.getClass().getClassLoader());
        this.cardIssuer = (CardIssuer)parcel.readParcelable(this.getClass().getClassLoader());
        this.cardHolderName = parcel.readString();
        this.cardPresentationMode = parcel.readInt();
        this.securityCode = parcel.readString();
        this.extraMetaData = parcel.readBundle();
        this.copyContentFromFd();
    }

    public void setCardArts(List<CardArts> list) {
        this.cardArts = list;
    }

    public void setCardColors(List<CardColors> list) {
        this.cardColors = list;
    }

    public void setCardExpiryMonth(String string) {
        this.cardExpiryMonth = string;
    }

    public void setCardExpiryYear(String string) {
        this.cardExpiryYear = string;
    }

    public void setCardHolderName(String string) {
        this.cardHolderName = string;
    }

    public void setCardIssuer(CardIssuer cardIssuer) {
        this.cardIssuer = cardIssuer;
    }

    public void setCardNetworkType(String string) {
        this.cardNetwork = string;
    }

    public void setCardPresentationMode(int n2) {
        this.cardPresentationMode = n2;
    }

    public void setCardProductDesc(String string) {
        this.cardProductDesc = string;
    }

    public void setCardProductName(String string) {
        this.cardProductName = string;
    }

    public void setCardRefernceId(String string) {
        this.cardRefId = string;
    }

    public void setCardSuffix(String string) {
        this.cardSuffix = string;
    }

    public void setCardType(String string) {
        this.cardType = string;
    }

    public void setExtraMetaData(Bundle bundle) {
        this.extraMetaData = bundle;
    }

    public void setSecurityCode(String string) {
        this.securityCode = string;
    }

    /*
     * Enabled aggressive block sorting
     */
    public String toString() {
        String string = "TokenMetaData: cardNetwork: " + this.cardNetwork + " cardType: " + this.cardType + " cardProductName: " + this.cardProductName + " cardProductDesc: " + this.cardProductDesc + " cardSuffix: " + this.cardSuffix + " cardRefId: " + this.cardRefId + " cardExpiryMonth: " + this.cardExpiryMonth + " cardExpiryYear: " + this.cardExpiryYear + " cardHolderName: " + this.cardHolderName + " cardPresentationMode: " + this.cardPresentationMode;
        String string2 = this.cardArts != null ? string + this.cardArts.toString() : string + " cardArts: null";
        String string3 = this.cardColors != null ? string2 + this.cardColors.toString() : string2 + " cardColors: null";
        if (this.cardIssuer != null) {
            return string3 + this.cardIssuer.toString();
        }
        return string3 + " cardIssuer: null";
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.cardNetwork);
        parcel.writeString(this.cardType);
        parcel.writeString(this.cardProductName);
        parcel.writeString(this.cardProductDesc);
        parcel.writeString(this.cardSuffix);
        parcel.writeString(this.cardRefId);
        parcel.writeString(this.cardExpiryMonth);
        parcel.writeString(this.cardExpiryYear);
        parcel.writeList(this.cardArts);
        parcel.writeList(this.cardColors);
        parcel.writeParcelable((Parcelable)this.cardIssuer, n2);
        parcel.writeString(this.cardHolderName);
        parcel.writeInt(this.cardPresentationMode);
        parcel.writeString(this.securityCode);
        parcel.writeBundle(this.extraMetaData);
    }

}

