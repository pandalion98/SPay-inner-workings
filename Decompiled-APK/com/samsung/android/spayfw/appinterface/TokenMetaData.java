package com.samsung.android.spayfw.appinterface;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.List;

public class TokenMetaData implements Parcelable {
    public static final String BUNDLE_KEY_EXTRA_META_DATA = "extraMetaData";
    public static final String BUNDLE_KEY_EXTRA_META_DATA_FD = "extraMetaDataFd";
    public static final String BUNDLE_KEY_EXTRA_META_DATA_FILE_PATH = "extraMetaDataFilePath";
    public static final Creator<TokenMetaData> CREATOR;
    private List<CardArts> cardArts;
    private List<CardColors> cardColors;
    private String cardExpiryMonth;
    private String cardExpiryYear;
    private String cardHolderName;
    private CardIssuer cardIssuer;
    private String cardNetwork;
    private int cardPresentationMode;
    private String cardProductDesc;
    private String cardProductName;
    private String cardRefId;
    private String cardSuffix;
    private String cardType;
    private Bundle extraMetaData;
    private String securityCode;

    /* renamed from: com.samsung.android.spayfw.appinterface.TokenMetaData.1 */
    static class C03931 implements Creator<TokenMetaData> {
        C03931() {
        }

        public TokenMetaData createFromParcel(Parcel parcel) {
            return new TokenMetaData(null);
        }

        public TokenMetaData[] newArray(int i) {
            return new TokenMetaData[i];
        }
    }

    static {
        CREATOR = new C03931();
    }

    private TokenMetaData(Parcel parcel) {
        this();
        readFromParcel(parcel);
    }

    public TokenMetaData() {
        this.cardPresentationMode = 15;
        this.cardArts = new ArrayList();
        this.cardColors = new ArrayList();
    }

    public Bundle getExtraMetaData() {
        return this.extraMetaData;
    }

    public void setExtraMetaData(Bundle bundle) {
        this.extraMetaData = bundle;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
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
        parcel.writeParcelable(this.cardIssuer, i);
        parcel.writeString(this.cardHolderName);
        parcel.writeInt(this.cardPresentationMode);
        parcel.writeString(this.securityCode);
        parcel.writeBundle(this.extraMetaData);
    }

    public List<CardArts> getCardArts() {
        return this.cardArts;
    }

    public void setCardArts(List<CardArts> list) {
        this.cardArts = list;
    }

    public List<CardColors> getCardColors() {
        return this.cardColors;
    }

    public void setCardColors(List<CardColors> list) {
        this.cardColors = list;
    }

    public String getCardExpiryMonth() {
        return this.cardExpiryMonth;
    }

    public void setCardExpiryMonth(String str) {
        this.cardExpiryMonth = str;
    }

    public String getCardExpiryYear() {
        return this.cardExpiryYear;
    }

    public void setCardExpiryYear(String str) {
        this.cardExpiryYear = str;
    }

    public CardIssuer getCardIssuer() {
        return this.cardIssuer;
    }

    public void setCardIssuer(CardIssuer cardIssuer) {
        this.cardIssuer = cardIssuer;
    }

    public String getCardNetworkType() {
        return this.cardNetwork;
    }

    public void setCardNetworkType(String str) {
        this.cardNetwork = str;
    }

    public String getCardProductDesc() {
        return this.cardProductDesc;
    }

    public void setCardProductDesc(String str) {
        this.cardProductDesc = str;
    }

    public String getCardProductName() {
        return this.cardProductName;
    }

    public void setCardProductName(String str) {
        this.cardProductName = str;
    }

    public String getCardRefernceId() {
        return this.cardRefId;
    }

    public void setCardRefernceId(String str) {
        this.cardRefId = str;
    }

    public String getCardSuffix() {
        return this.cardSuffix;
    }

    public void setCardSuffix(String str) {
        this.cardSuffix = str;
    }

    public String getCardType() {
        return this.cardType;
    }

    public void setCardType(String str) {
        this.cardType = str;
    }

    public String getCardHolderName() {
        return this.cardHolderName;
    }

    public void setCardHolderName(String str) {
        this.cardHolderName = str;
    }

    public int getCardPresentationMode() {
        return this.cardPresentationMode;
    }

    public void setCardPresentationMode(int i) {
        this.cardPresentationMode = i;
    }

    public String getSecurityCode() {
        return this.securityCode;
    }

    public void setSecurityCode(String str) {
        this.securityCode = str;
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
        parcel.readList(this.cardArts, getClass().getClassLoader());
        parcel.readList(this.cardColors, getClass().getClassLoader());
        this.cardIssuer = (CardIssuer) parcel.readParcelable(getClass().getClassLoader());
        this.cardHolderName = parcel.readString();
        this.cardPresentationMode = parcel.readInt();
        this.securityCode = parcel.readString();
        this.extraMetaData = parcel.readBundle();
        copyContentFromFd();
    }

    public String toString() {
        String str = "TokenMetaData: cardNetwork: " + this.cardNetwork + " cardType: " + this.cardType + " cardProductName: " + this.cardProductName + " cardProductDesc: " + this.cardProductDesc + " cardSuffix: " + this.cardSuffix + " cardRefId: " + this.cardRefId + " cardExpiryMonth: " + this.cardExpiryMonth + " cardExpiryYear: " + this.cardExpiryYear + " cardHolderName: " + this.cardHolderName + " cardPresentationMode: " + this.cardPresentationMode;
        if (this.cardArts != null) {
            str = str + this.cardArts.toString();
        } else {
            str = str + " cardArts: null";
        }
        if (this.cardColors != null) {
            str = str + this.cardColors.toString();
        } else {
            str = str + " cardColors: null";
        }
        if (this.cardIssuer != null) {
            return str + this.cardIssuer.toString();
        }
        return str + " cardIssuer: null";
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void copyContentFromFd() {
        /*
        r11 = this;
        r2 = 0;
        r0 = r11.extraMetaData;
        if (r0 == 0) goto L_0x00c1;
    L_0x0005:
        r0 = r11.extraMetaData;
        r1 = "extraMetaDataFd";
        r0 = r0.getParcelable(r1);
        r0 = (android.os.ParcelFileDescriptor) r0;
        r3 = r0;
    L_0x0010:
        if (r3 == 0) goto L_0x0060;
    L_0x0012:
        r0 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r0 = new byte[r0];
        r1 = 0;
        r4 = new java.io.FileInputStream;	 Catch:{ Throwable -> 0x004f, all -> 0x00a1 }
        r5 = r3.getFileDescriptor();	 Catch:{ Throwable -> 0x004f, all -> 0x00a1 }
        r4.<init>(r5);	 Catch:{ Throwable -> 0x004f, all -> 0x00a1 }
        r5 = 0;
        r6 = new java.io.ByteArrayOutputStream;	 Catch:{ Throwable -> 0x0041, all -> 0x008c }
        r6.<init>();	 Catch:{ Throwable -> 0x0041, all -> 0x008c }
        r7 = 0;
    L_0x0027:
        r8 = r4.read(r0);	 Catch:{ Throwable -> 0x0033, all -> 0x00bd }
        r9 = -1;
        if (r8 == r9) goto L_0x0061;
    L_0x002e:
        r9 = 0;
        r6.write(r0, r9, r8);	 Catch:{ Throwable -> 0x0033, all -> 0x00bd }
        goto L_0x0027;
    L_0x0033:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x0035 }
    L_0x0035:
        r1 = move-exception;
        r10 = r1;
        r1 = r0;
        r0 = r10;
    L_0x0039:
        if (r6 == 0) goto L_0x0040;
    L_0x003b:
        if (r1 == 0) goto L_0x0098;
    L_0x003d:
        r6.close();	 Catch:{ Throwable -> 0x0093, all -> 0x008c }
    L_0x0040:
        throw r0;	 Catch:{ Throwable -> 0x0041, all -> 0x008c }
    L_0x0041:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x0043 }
    L_0x0043:
        r1 = move-exception;
        r10 = r1;
        r1 = r0;
        r0 = r10;
    L_0x0047:
        if (r4 == 0) goto L_0x004e;
    L_0x0049:
        if (r1 == 0) goto L_0x00ac;
    L_0x004b:
        r4.close();	 Catch:{ Throwable -> 0x00a7, all -> 0x00a1 }
    L_0x004e:
        throw r0;	 Catch:{ Throwable -> 0x004f, all -> 0x00a1 }
    L_0x004f:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x0051 }
    L_0x0051:
        r1 = move-exception;
        r2 = r0;
        r0 = r1;
    L_0x0054:
        if (r3 == 0) goto L_0x005b;
    L_0x0056:
        if (r2 == 0) goto L_0x00b9;
    L_0x0058:
        r3.close();	 Catch:{ Throwable -> 0x00b4 }
    L_0x005b:
        throw r0;	 Catch:{ IOException -> 0x005c }
    L_0x005c:
        r0 = move-exception;
        r0.printStackTrace();
    L_0x0060:
        return;
    L_0x0061:
        r0 = r6.toString();	 Catch:{ Throwable -> 0x0033, all -> 0x00bd }
        r8 = r11.extraMetaData;	 Catch:{ Throwable -> 0x0033, all -> 0x00bd }
        r9 = "extraMetaData";
        r8.putString(r9, r0);	 Catch:{ Throwable -> 0x0033, all -> 0x00bd }
        if (r6 == 0) goto L_0x0073;
    L_0x006e:
        if (r2 == 0) goto L_0x008f;
    L_0x0070:
        r6.close();	 Catch:{ Throwable -> 0x0087, all -> 0x008c }
    L_0x0073:
        if (r4 == 0) goto L_0x007a;
    L_0x0075:
        if (r2 == 0) goto L_0x00a3;
    L_0x0077:
        r4.close();	 Catch:{ Throwable -> 0x009c, all -> 0x00a1 }
    L_0x007a:
        if (r3 == 0) goto L_0x0060;
    L_0x007c:
        if (r2 == 0) goto L_0x00b0;
    L_0x007e:
        r3.close();	 Catch:{ Throwable -> 0x0082 }
        goto L_0x0060;
    L_0x0082:
        r0 = move-exception;
        r1.addSuppressed(r0);	 Catch:{ IOException -> 0x005c }
        goto L_0x0060;
    L_0x0087:
        r0 = move-exception;
        r7.addSuppressed(r0);	 Catch:{ Throwable -> 0x0041, all -> 0x008c }
        goto L_0x0073;
    L_0x008c:
        r0 = move-exception;
        r1 = r2;
        goto L_0x0047;
    L_0x008f:
        r6.close();	 Catch:{ Throwable -> 0x0041, all -> 0x008c }
        goto L_0x0073;
    L_0x0093:
        r5 = move-exception;
        r1.addSuppressed(r5);	 Catch:{ Throwable -> 0x0041, all -> 0x008c }
        goto L_0x0040;
    L_0x0098:
        r6.close();	 Catch:{ Throwable -> 0x0041, all -> 0x008c }
        goto L_0x0040;
    L_0x009c:
        r0 = move-exception;
        r5.addSuppressed(r0);	 Catch:{ Throwable -> 0x004f, all -> 0x00a1 }
        goto L_0x007a;
    L_0x00a1:
        r0 = move-exception;
        goto L_0x0054;
    L_0x00a3:
        r4.close();	 Catch:{ Throwable -> 0x004f, all -> 0x00a1 }
        goto L_0x007a;
    L_0x00a7:
        r4 = move-exception;
        r1.addSuppressed(r4);	 Catch:{ Throwable -> 0x004f, all -> 0x00a1 }
        goto L_0x004e;
    L_0x00ac:
        r4.close();	 Catch:{ Throwable -> 0x004f, all -> 0x00a1 }
        goto L_0x004e;
    L_0x00b0:
        r3.close();	 Catch:{ IOException -> 0x005c }
        goto L_0x0060;
    L_0x00b4:
        r1 = move-exception;
        r2.addSuppressed(r1);	 Catch:{ IOException -> 0x005c }
        goto L_0x005b;
    L_0x00b9:
        r3.close();	 Catch:{ IOException -> 0x005c }
        goto L_0x005b;
    L_0x00bd:
        r0 = move-exception;
        r1 = r2;
        goto L_0x0039;
    L_0x00c1:
        r3 = r2;
        goto L_0x0010;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.appinterface.TokenMetaData.copyContentFromFd():void");
    }
}
