/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.Class
 *  java.lang.ClassLoader
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import com.samsung.android.spayfw.appinterface.Instruction;
import java.util.ArrayList;
import java.util.List;

public class UpdateLoyaltyCardInfo
implements Parcelable {
    public static final Parcelable.Creator<UpdateLoyaltyCardInfo> CREATOR = new Parcelable.Creator<UpdateLoyaltyCardInfo>(){

        public UpdateLoyaltyCardInfo createFromParcel(Parcel parcel) {
            return new UpdateLoyaltyCardInfo(parcel);
        }

        public UpdateLoyaltyCardInfo[] newArray(int n2) {
            return new UpdateLoyaltyCardInfo[n2];
        }
    };
    private String cardId = null;
    private List<Instruction> instructions = new ArrayList();
    private String tokenId = null;

    public UpdateLoyaltyCardInfo() {
    }

    private UpdateLoyaltyCardInfo(Parcel parcel) {
        this();
        this.readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public String getCardId() {
        return this.cardId;
    }

    public List<Instruction> getInstructions() {
        return this.instructions;
    }

    public String getTokenId() {
        return this.tokenId;
    }

    public void readFromParcel(Parcel parcel) {
        this.tokenId = parcel.readString();
        this.cardId = parcel.readString();
        parcel.readList(this.instructions, this.getClass().getClassLoader());
    }

    public void setCardId(String string) {
        this.cardId = string;
    }

    public void setInstructions(List<Instruction> list) {
        this.instructions = list;
    }

    public void setTokenId(String string) {
        this.tokenId = string;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("UpdateLoyaltyCardInfo: tokenId = " + this.tokenId + " cardId = " + this.cardId + " instructions: ");
        if (this.instructions != null) {
            for (int i2 = 0; i2 < this.instructions.size(); ++i2) {
                stringBuffer.append(((Instruction)this.instructions.get(i2)).toString());
            }
        } else {
            stringBuffer.append(" instructions: null ");
        }
        return stringBuffer.toString();
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.tokenId);
        parcel.writeString(this.cardId);
        parcel.writeList(this.instructions);
    }

}

