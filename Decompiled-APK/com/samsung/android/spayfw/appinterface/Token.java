package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Token implements Parcelable {
    public static final Creator<Token> CREATOR;
    private TokenMetaData metadata;
    private String suffix;
    private String tokenExpiryMonth;
    private String tokenExpiryYear;
    private String tokenId;
    private TokenStatus tokenStatus;

    /* renamed from: com.samsung.android.spayfw.appinterface.Token.1 */
    static class C03921 implements Creator<Token> {
        C03921() {
        }

        public Token createFromParcel(Parcel parcel) {
            return new Token(null);
        }

        public Token[] newArray(int i) {
            return new Token[i];
        }
    }

    static {
        CREATOR = new C03921();
    }

    private Token(Parcel parcel) {
        readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public TokenMetaData getMetadata() {
        return this.metadata;
    }

    public String getTokenExpiryMonth() {
        return this.tokenExpiryMonth;
    }

    public String getTokenExpiryYear() {
        return this.tokenExpiryYear;
    }

    public String getTokenId() {
        return this.tokenId;
    }

    public TokenStatus getTokenStatus() {
        return this.tokenStatus;
    }

    public String getTokenSuffix() {
        return this.suffix;
    }

    public void readFromParcel(Parcel parcel) {
        this.tokenId = parcel.readString();
        this.tokenExpiryMonth = parcel.readString();
        this.tokenExpiryYear = parcel.readString();
        this.suffix = parcel.readString();
        this.tokenStatus = (TokenStatus) parcel.readParcelable(TokenStatus.class.getClassLoader());
        this.metadata = (TokenMetaData) parcel.readParcelable(TokenMetaData.class.getClassLoader());
    }

    public void setMetadata(TokenMetaData tokenMetaData) {
        this.metadata = tokenMetaData;
    }

    public void setTokenExpiryMonth(String str) {
        this.tokenExpiryMonth = str;
    }

    public void setTokenExpiryYear(String str) {
        this.tokenExpiryYear = str;
    }

    public void setTokenId(String str) {
        this.tokenId = str;
    }

    public void setTokenStatus(TokenStatus tokenStatus) {
        this.tokenStatus = tokenStatus;
    }

    public void setTokenSuffix(String str) {
        this.suffix = str;
    }

    public String toString() {
        String str = "Token: tokenId: " + this.tokenId + " tokenExpiryMonth: " + this.tokenExpiryMonth + "tokenExpiryYear: " + this.tokenExpiryYear + "tokenSuffix: " + this.suffix;
        if (this.tokenStatus != null) {
            str = str + this.tokenStatus.toString();
        } else {
            str = str + " tokenStatus: null ";
        }
        if (this.metadata != null) {
            return str + this.metadata.toString();
        }
        return str + " metadata: null";
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.tokenId);
        parcel.writeString(this.tokenExpiryMonth);
        parcel.writeString(this.tokenExpiryYear);
        parcel.writeString(this.suffix);
        parcel.writeParcelable(this.tokenStatus, i);
        parcel.writeParcelable(this.metadata, i);
    }
}
