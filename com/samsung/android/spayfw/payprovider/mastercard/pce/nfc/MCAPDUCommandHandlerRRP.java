package com.samsung.android.spayfw.payprovider.mastercard.pce.nfc;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.apdu.emv.VerifyPINApdu;
import com.samsung.android.spayfw.p002b.Log;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class MCAPDUCommandHandlerRRP extends MCCAPDUBaseCommandHandler {
    private ByteArray mResponseRRE;

    public MCAPDUCommandHandlerRRP() {
        this.mResponseRRE = null;
    }

    protected MCCommandResult checkP1P2Parameters(byte b, byte b2) {
        if (b == null && b2 == null) {
            return completeCommand();
        }
        return completeCommand(27270);
    }

    protected MCCommandResult processCommand(ByteArray byteArray) {
        int i = byteArray.getByte(4) & GF2Field.MASK;
        if (i != 4) {
            Log.m286e("mcpce_MCCAPDUBaseCommandHandler", "processCommand RELAY_RESISTANCE, wrong apdu length, lc = " + i + ", epected length = " + 4);
            return completeCommand(26368);
        } else if (getTransactionContext().getRRPCounter() >= 3) {
            Log.m286e("mcpce_MCCAPDUBaseCommandHandler", "processCommand RELAY_RESISTANCE, RRP counter exceed max, RRP counter 3, max 3");
            return completeCommand(27013);
        } else {
            getTransactionContext().setTerminalRREntropy(byteArray.copyOfRange(5, (i + 4) + 1));
            getTransactionContext().incrementRRPCounter();
            ByteArray copyOfRange = getTransactionContext().getTransactionCredentials().getIDN().copyOfRange(((getTransactionContext().getRRPCounter() * 4) - 3) - 1, getTransactionContext().getRRPCounter() * 4);
            getTransactionContext().setDeviceRREntropy(copyOfRange);
            byte[] bArr = new byte[]{VerifyPINApdu.P2_PLAINTEXT, (byte) 10};
            this.mResponseRRE = this.baf.getByteArray(bArr, bArr.length);
            this.mResponseRRE.append(copyOfRange);
            this.mResponseRRE.append(getPaymentProfile().getContactlessPaymentData().getMinRRTime());
            this.mResponseRRE.append(getPaymentProfile().getContactlessPaymentData().getMaxRRTime());
            this.mResponseRRE.append(getPaymentProfile().getContactlessPaymentData().getTransmissionRRTime());
            return completeCommand();
        }
    }

    protected MCCommandResult generateResponseAPDU() {
        if (this.mResponseRRE != null) {
            return completeCommand(this.mResponseRRE.clone().append(ByteArrayFactory.getInstance().getFromWord(-28672)));
        }
        Log.m286e("mcpce_MCCAPDUBaseCommandHandler", "processCommand RELAY_RESISTANCE, RRP response is null.");
        return completeCommand(27013);
    }

    public boolean checkCLA(byte b) {
        return b == -128;
    }
}
