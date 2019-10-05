/*
 * Decompiled with CFR 0.0.
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.nfc;

import com.mastercard.mcbp.core.mcbpcards.profile.Records;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.apdu.emv.ReadRecordApdu;
import com.samsung.android.spayfw.b.Log;

public class MCAPDUCommandHandlerRR
extends MCCAPDUBaseCommandHandler {
    private ReadRecordApdu mReadRecordApdu;
    private byte mRecord = (byte)-1;
    private ByteArray mResponseData = null;
    private byte mSFI = (byte)-1;

    @Override
    public boolean checkCLA(byte by) {
        return by == 0;
    }

    @Override
    public MCCommandResult checkP1P2Parameters(byte by, byte by2) {
        if (by == 0 || (by2 & 7) != 4) {
            return this.completeCommand(27270);
        }
        return this.completeCommand();
    }

    @Override
    public MCCommandResult generateResponseAPDU() {
        if (this.mResponseData == null || this.mRecord == -1 || this.mSFI == -1) {
            Log.e("mcpce_MCCAPDUBaseCommandHandler", "Read Record: record not found. RespData is null.");
            return this.completeCommand(27267);
        }
        ByteArray byteArray = this.getPaymentProfile().getContactlessPaymentData().getGPO_Response();
        if (this.getTransactionContext().isAlternateAID()) {
            byteArray = this.getPaymentProfile().getContactlessPaymentData().getAlternateContactlessPaymentData().getGPO_Response();
        }
        if (byteArray != null && byteArray.getLength() > 8) {
            ByteArray byteArray2 = byteArray.copyOfRange(8, byteArray.getLength());
            boolean bl = false;
            int n2 = 1;
            while (byteArray2.getLength() >= n2 * 4) {
                ByteArray byteArray3 = byteArray2.copyOfRange(-4 + n2 * 4, n2 * 4);
                if (byteArray3.getByte(0) >> 3 == this.mSFI && this.mRecord >= byteArray3.getByte(1) && this.mRecord <= byteArray3.getByte(2)) {
                    bl = true;
                }
                ++n2;
            }
            if (!bl) {
                Log.e("mcpce_MCCAPDUBaseCommandHandler", "Read Record: record not found in AFL: SFI = " + this.mSFI + ", record = " + this.mRecord);
                return this.completeCommand(27013);
            }
        } else {
            Log.e("mcpce_MCCAPDUBaseCommandHandler", "Read Record: cannot get AFL from gpo_response.");
            return this.completeCommand(27013);
        }
        return this.completeCommand(this.mResponseData.clone().append(ByteArrayFactory.getInstance().getFromWord(-28672)));
    }

    @Override
    public MCCommandResult processCommand(ByteArray byteArray) {
        block5 : {
            this.mReadRecordApdu = new ReadRecordApdu(byteArray);
            this.mRecord = this.mReadRecordApdu.getRecordNumber();
            this.mSFI = this.mReadRecordApdu.getSfiNumber();
            if (this.mRecord == -1) break block5;
            Records[] arrrecords = this.getPaymentProfile().getContactlessPaymentData().getRecords();
            if (arrrecords == null || arrrecords.length == 0) {
                return this.completeCommand(27267);
            }
            int n2 = arrrecords.length;
            int n3 = 0;
            do {
                block7 : {
                    block6 : {
                        if (n3 >= n2) break block6;
                        Records records = arrrecords[n3];
                        if (records.getRecordNumber() != this.mRecord || records.getSFI() != this.mSFI) break block7;
                        this.mResponseData = records.getRecordValue();
                    }
                    return this.completeCommand();
                }
                ++n3;
            } while (true);
        }
        Log.e("mcpce_MCCAPDUBaseCommandHandler", "Read Record: record not found : SFI = " + this.mSFI + ", record = " + this.mRecord);
        return this.completeCommand(27267);
    }
}

