package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.List;

public class UpdateLoyaltyCardInfo implements Parcelable {
    public static final Creator<UpdateLoyaltyCardInfo> CREATOR;
    private String cardId;
    private List<Instruction> instructions;
    private String tokenId;

    /* renamed from: com.samsung.android.spayfw.appinterface.UpdateLoyaltyCardInfo.1 */
    static class C03991 implements Creator<UpdateLoyaltyCardInfo> {
        C03991() {
        }

        public UpdateLoyaltyCardInfo createFromParcel(Parcel parcel) {
            return new UpdateLoyaltyCardInfo(null);
        }

        public UpdateLoyaltyCardInfo[] newArray(int i) {
            return new UpdateLoyaltyCardInfo[i];
        }
    }

    static {
        CREATOR = new C03991();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.tokenId);
        parcel.writeString(this.cardId);
        parcel.writeList(this.instructions);
    }

    private UpdateLoyaltyCardInfo(Parcel parcel) {
        this();
        readFromParcel(parcel);
    }

    public UpdateLoyaltyCardInfo() {
        this.tokenId = null;
        this.cardId = null;
        this.instructions = null;
        this.instructions = new ArrayList();
    }

    public void readFromParcel(Parcel parcel) {
        this.tokenId = parcel.readString();
        this.cardId = parcel.readString();
        parcel.readList(this.instructions, getClass().getClassLoader());
    }

    public String getTokenId() {
        return this.tokenId;
    }

    public void setTokenId(String str) {
        this.tokenId = str;
    }

    public String getCardId() {
        return this.cardId;
    }

    public void setCardId(String str) {
        this.cardId = str;
    }

    public List<Instruction> getInstructions() {
        return this.instructions;
    }

    public void setInstructions(List<Instruction> list) {
        this.instructions = list;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("UpdateLoyaltyCardInfo: tokenId = " + this.tokenId + " cardId = " + this.cardId + " instructions: ");
        if (this.instructions != null) {
            for (int i = 0; i < this.instructions.size(); i++) {
                stringBuffer.append(((Instruction) this.instructions.get(i)).toString());
            }
        } else {
            stringBuffer.append(" instructions: null ");
        }
        return stringBuffer.toString();
    }
}
