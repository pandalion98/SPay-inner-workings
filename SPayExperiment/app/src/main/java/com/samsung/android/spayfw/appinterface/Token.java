/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.ClassLoader
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import com.samsung.android.spayfw.appinterface.TokenMetaData;
import com.samsung.android.spayfw.appinterface.TokenStatus;

public class Token
implements Parcelable {
    public static final Parcelable.Creator<Token> CREATOR = new Parcelable.Creator<Token>(){

        public Token createFromParcel(Parcel parcel) {
            return new Token(parcel);
        }

        public Token[] newArray(int n2) {
            return new Token[n2];
        }
    };
    private TokenMetaData metadata;
    private String suffix;
    private String tokenExpiryMonth;
    private String tokenExpiryYear;
    private String tokenId;
    private TokenStatus tokenStatus;

    public Token() {
    }

    private Token(Parcel parcel) {
        this.readFromParcel(parcel);
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
        this.tokenStatus = (TokenStatus)parcel.readParcelable(TokenStatus.class.getClassLoader());
        this.metadata = (TokenMetaData)parcel.readParcelable(TokenMetaData.class.getClassLoader());
    }

    public void setMetadata(TokenMetaData tokenMetaData) {
        this.metadata = tokenMetaData;
    }

    public void setTokenExpiryMonth(String string) {
        this.tokenExpiryMonth = string;
    }

    public void setTokenExpiryYear(String string) {
        this.tokenExpiryYear = string;
    }

    public void setTokenId(String string) {
        this.tokenId = string;
    }

    public void setTokenStatus(TokenStatus tokenStatus) {
        this.tokenStatus = tokenStatus;
    }

    public void setTokenSuffix(String string) {
        this.suffix = string;
    }

    /*
     * Enabled aggressive block sorting
     */
    public String toString() {
        String string = "Token: tokenId: " + this.tokenId + " tokenExpiryMonth: " + this.tokenExpiryMonth + "tokenExpiryYear: " + this.tokenExpiryYear + "tokenSuffix: " + this.suffix;
        String string2 = this.tokenStatus != null ? string + this.tokenStatus.toString() : string + " tokenStatus: null ";
        if (this.metadata != null) {
            return string2 + this.metadata.toString();
        }
        return string2 + " metadata: null";
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.tokenId);
        parcel.writeString(this.tokenExpiryMonth);
        parcel.writeString(this.tokenExpiryYear);
        parcel.writeString(this.suffix);
        parcel.writeParcelable((Parcelable)this.tokenStatus, n2);
        parcel.writeParcelable((Parcelable)this.metadata, n2);
    }

}

