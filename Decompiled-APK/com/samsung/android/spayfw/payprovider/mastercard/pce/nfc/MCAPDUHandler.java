package com.samsung.android.spayfw.payprovider.mastercard.pce.nfc;

import com.mastercard.mcbp.core.mpplite.MPPLiteInstruction;
import com.mastercard.mobile_api.utils.apdu.ISO7816;
import com.mastercard.mobile_api.utils.apdu.emv.GenerateACApdu;
import com.mastercard.mobile_api.utils.apdu.emv.GetProcessingOptions;
import com.mastercard.mobile_api.utils.apdu.emv.ReadRecordApdu;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MTBPLite.MTBPState;
import java.util.EnumSet;
import java.util.HashMap;

public class MCAPDUHandler {
    private static HashMap<Byte, APDUCommand> mAPDUCommands;
    private static HashMap<Byte, String> sApduInsNames;

    public static class APDUCommand {
        private MCCAPDUBaseCommandHandler mAPDUHandler;
        private MTBPState mNextState;
        private EnumSet<MTBPState> mValidStates;

        public APDUCommand(MCCAPDUBaseCommandHandler mCCAPDUBaseCommandHandler, EnumSet<MTBPState> enumSet, MTBPState mTBPState) {
            this.mValidStates = enumSet;
            this.mNextState = mTBPState;
            this.mAPDUHandler = mCCAPDUBaseCommandHandler;
        }

        public MCCAPDUBaseCommandHandler getAPDUHandler() {
            return this.mAPDUHandler;
        }

        public boolean validateState(MTBPState mTBPState) {
            return this.mValidStates.contains(mTBPState);
        }

        public MTBPState getNextState() {
            return this.mNextState;
        }
    }

    static {
        mAPDUCommands = new HashMap();
        sApduInsNames = new HashMap();
        sApduInsNames.put(Byte.valueOf(ISO7816.INS_SELECT), "Select");
        sApduInsNames.put(Byte.valueOf(ReadRecordApdu.INS), "ReadRecord");
        sApduInsNames.put(Byte.valueOf(MPPLiteInstruction.INS_RELAY_RESISTANCE), "RRP");
        sApduInsNames.put(Byte.valueOf(GetProcessingOptions.INS), "GPO");
        sApduInsNames.put(Byte.valueOf(GenerateACApdu.INS), "CCC");
        sApduInsNames.put(Byte.valueOf(MPPLiteInstruction.INS_GENERATE_AC), "GAC");
    }

    public static String getApduName(byte b) {
        return (String) sApduInsNames.get(Byte.valueOf(b));
    }

    public MCAPDUHandler() {
        mAPDUCommands.put(Byte.valueOf(ISO7816.INS_SELECT), new APDUCommand(new MCAPDUCommandHandlerSELECT(), EnumSet.of(MTBPState.READY, MTBPState.NFC_SELECTED, MTBPState.NFC_INITIATED), MTBPState.NFC_SELECTED));
        mAPDUCommands.put(Byte.valueOf(ReadRecordApdu.INS), new APDUCommand(new MCAPDUCommandHandlerRR(), EnumSet.of(MTBPState.NFC_SELECTED, MTBPState.NFC_INITIATED), null));
        mAPDUCommands.put(Byte.valueOf(MPPLiteInstruction.INS_RELAY_RESISTANCE), new APDUCommand(new MCAPDUCommandHandlerRRP(), EnumSet.of(MTBPState.NFC_INITIATED), null));
        mAPDUCommands.put(Byte.valueOf(GetProcessingOptions.INS), new APDUCommand(new MCAPDUCommandHandlerGPO(), EnumSet.of(MTBPState.NFC_SELECTED), MTBPState.NFC_INITIATED));
        mAPDUCommands.put(Byte.valueOf(GenerateACApdu.INS), new APDUCommand(new MCAPDUCommandHandlerCCC(), EnumSet.of(MTBPState.NFC_INITIATED), MTBPState.READY));
        mAPDUCommands.put(Byte.valueOf(MPPLiteInstruction.INS_GENERATE_AC), new APDUCommand(new MCAPDUCommandHandlerGAC(), EnumSet.of(MTBPState.NFC_INITIATED), MTBPState.READY));
    }

    public APDUCommand getCommandHandlerByInstruction(byte b) {
        return (APDUCommand) mAPDUCommands.get(Byte.valueOf(b));
    }
}
