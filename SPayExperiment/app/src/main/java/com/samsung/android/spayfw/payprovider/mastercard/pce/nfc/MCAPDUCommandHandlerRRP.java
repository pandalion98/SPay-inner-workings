/*
 * Decompiled with CFR 0.0.
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.nfc;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.samsung.android.spayfw.b.Log;

public class MCAPDUCommandHandlerRRP
extends MCCAPDUBaseCommandHandler {
    private ByteArray mResponseRRE = null;

    @Override
    public boolean checkCLA(byte by) {
        return by == -128;
    }

    @Override
    protected MCCommandResult checkP1P2Parameters(byte by, byte by2) {
        if (by != 0 || by2 != 0) {
            return this.completeCommand(27270);
        }
        return this.completeCommand();
    }

    @Override
    protected MCCommandResult generateResponseAPDU() {
        if (this.mResponseRRE == null) {
            Log.e("mcpce_MCCAPDUBaseCommandHandler", "processCommand RELAY_RESISTANCE, RRP response is null.");
            return this.completeCommand(27013);
        }
        return this.completeCommand(this.mResponseRRE.clone().append(ByteArrayFactory.getInstance().getFromWord(-28672)));
    }

    @Override
    protected MCCommandResult processCommand(ByteArray byteArray) {
        int n2 = 255 & byteArray.getByte(4);
        if (n2 != 4) {
            Log.e("mcpce_MCCAPDUBaseCommandHandler", "processCommand RELAY_RESISTANCE, wrong apdu length, lc = " + n2 + ", epected length = " + 4);
            return this.completeCommand(26368);
        }
        if (this.getTransactionContext().getRRPCounter() >= 3) {
            Log.e("mcpce_MCCAPDUBaseCommandHandler", "processCommand RELAY_RESISTANCE, RRP counter exceed max, RRP counter 3, max 3");
            return this.completeCommand(27013);
        }
        ByteArray byteArray2 = byteArray.copyOfRange(5, 1 + (n2 + 4));
        this.getTransactionContext().setTerminalRREntropy(byteArray2);
        this.getTransactionContext().incrementRRPCounter();
        ByteArray byteArray3 = this.getTransactionContext().getTransactionCredentials().getIDN().copyOfRange(-1 + (-3 + 4 * this.getTransactionContext().getRRPCounter()), 4 * this.getTransactionContext().getRRPCounter());
        this.getTransactionContext().setDeviceRREntropy(byteArray3);
        byte[] arrby = new byte[]{-128, 10};
        this.mResponseRRE = this.baf.getByteArray(arrby, arrby.length);
        this.mResponseRRE.append(byteArray3);
        this.mResponseRRE.append(this.getPaymentProfile().getContactlessPaymentData().getMinRRTime());
        this.mResponseRRE.append(this.getPaymentProfile().getContactlessPaymentData().getMaxRRTime());
        this.mResponseRRE.append(this.getPaymentProfile().getContactlessPaymentData().getTransmissionRRTime());
        return this.completeCommand();
    }
}

