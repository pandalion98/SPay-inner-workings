package com.samsung.android.spayfw.payprovider.mastercard.pce.nfc;

import com.mastercard.mcbp.core.mcbpcards.profile.Records;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.apdu.emv.ReadRecordApdu;
import com.samsung.android.spayfw.p002b.Log;

public class MCAPDUCommandHandlerRR extends MCCAPDUBaseCommandHandler {
    private ReadRecordApdu mReadRecordApdu;
    private byte mRecord;
    private ByteArray mResponseData;
    private byte mSFI;

    public MCAPDUCommandHandlerRR() {
        this.mResponseData = null;
        this.mRecord = (byte) -1;
        this.mSFI = (byte) -1;
    }

    public MCCommandResult checkP1P2Parameters(byte b, byte b2) {
        if (b == null || (b2 & 7) != 4) {
            return completeCommand(27270);
        }
        return completeCommand();
    }

    public MCCommandResult processCommand(ByteArray byteArray) {
        this.mReadRecordApdu = new ReadRecordApdu(byteArray);
        this.mRecord = this.mReadRecordApdu.getRecordNumber();
        this.mSFI = this.mReadRecordApdu.getSfiNumber();
        if (this.mRecord != -1) {
            Records[] records = getPaymentProfile().getContactlessPaymentData().getRecords();
            if (records == null || records.length == 0) {
                return completeCommand(27267);
            }
            for (Records records2 : records) {
                if (records2.getRecordNumber() == this.mRecord && records2.getSFI() == this.mSFI) {
                    this.mResponseData = records2.getRecordValue();
                    break;
                }
            }
            return completeCommand();
        }
        Log.m286e("mcpce_MCCAPDUBaseCommandHandler", "Read Record: record not found : SFI = " + this.mSFI + ", record = " + this.mRecord);
        return completeCommand(27267);
    }

    public MCCommandResult generateResponseAPDU() {
        if (this.mResponseData == null || this.mRecord == (byte) -1 || this.mSFI == (byte) -1) {
            Log.m286e("mcpce_MCCAPDUBaseCommandHandler", "Read Record: record not found. RespData is null.");
            return completeCommand(27267);
        }
        ByteArray gPO_Response = getPaymentProfile().getContactlessPaymentData().getGPO_Response();
        if (getTransactionContext().isAlternateAID()) {
            gPO_Response = getPaymentProfile().getContactlessPaymentData().getAlternateContactlessPaymentData().getGPO_Response();
        }
        if (gPO_Response == null || gPO_Response.getLength() <= 8) {
            Log.m286e("mcpce_MCCAPDUBaseCommandHandler", "Read Record: cannot get AFL from gpo_response.");
            return completeCommand(27013);
        }
        ByteArray copyOfRange = gPO_Response.copyOfRange(8, gPO_Response.getLength());
        int i = 0;
        for (int i2 = 1; copyOfRange.getLength() >= i2 * 4; i2++) {
            ByteArray copyOfRange2 = copyOfRange.copyOfRange((i2 * 4) - 4, i2 * 4);
            if ((copyOfRange2.getByte(0) >> 3) == this.mSFI && this.mRecord >= copyOfRange2.getByte(1) && this.mRecord <= copyOfRange2.getByte(2)) {
                i = 1;
            }
        }
        if (i != 0) {
            return completeCommand(this.mResponseData.clone().append(ByteArrayFactory.getInstance().getFromWord(-28672)));
        }
        Log.m286e("mcpce_MCCAPDUBaseCommandHandler", "Read Record: record not found in AFL: SFI = " + this.mSFI + ", record = " + this.mRecord);
        return completeCommand(27013);
    }

    public boolean checkCLA(byte b) {
        return b == null;
    }
}
