package com.americanexpress.mobilepayments.hceclient.payments.nfc;

import com.americanexpress.mobilepayments.hceclient.model.TokenAPDUResponse;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.process.ApplicationSelectionProcess;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.process.FirstCardActionAnalysisProcess;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.process.InitialApplicationProcessingProcess;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.process.ReadApplicationDataProcess;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.process.TerminalRiskManagmentProcess;
import com.mastercard.mcbp.core.mpplite.MPPLiteInstruction;
import com.mastercard.mobile_api.utils.apdu.ISO7816;
import com.mastercard.mobile_api.utils.apdu.emv.GetDataApdu;
import com.mastercard.mobile_api.utils.apdu.emv.GetProcessingOptions;
import com.mastercard.mobile_api.utils.apdu.emv.ReadRecordApdu;
import com.mastercard.mobile_api.utils.apdu.emv.VerifyPINApdu;

public enum CommandSet {
    APPLICATION_SELECTION((byte) 0, ISO7816.INS_SELECT, new ApplicationSelectionProcess()),
    GPO(VerifyPINApdu.P2_PLAINTEXT, GetProcessingOptions.INS, new InitialApplicationProcessingProcess()),
    READ_RECORD((byte) 0, ReadRecordApdu.INS, new ReadApplicationDataProcess()),
    GET_DATA(VerifyPINApdu.P2_PLAINTEXT, GetDataApdu.INS, new TerminalRiskManagmentProcess()),
    GEN_AC(VerifyPINApdu.P2_PLAINTEXT, MPPLiteInstruction.INS_GENERATE_AC, new FirstCardActionAnalysisProcess());
    
    private byte bCLA;
    private byte bINS;
    private CommandProcess commandProcess;

    public byte getbCLA() {
        return this.bCLA;
    }

    public byte getbINS() {
        return this.bINS;
    }

    private CommandSet(byte b, byte b2, CommandProcess commandProcess) {
        this.bCLA = b;
        this.bINS = b2;
        this.commandProcess = commandProcess;
    }

    public TokenAPDUResponse process(CommandAPDU commandAPDU) {
        new TokenAPDUResponse().setsSW(com.samsung.android.spayfw.appinterface.ISO7816.SW_UNKNOWN);
        try {
            this.commandProcess.set();
            TokenAPDUResponse process = this.commandProcess.process(commandAPDU);
            return process;
        } finally {
            this.commandProcess.reset();
        }
    }
}
