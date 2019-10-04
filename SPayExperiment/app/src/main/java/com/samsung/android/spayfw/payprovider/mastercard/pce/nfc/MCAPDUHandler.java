/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Byte
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 *  java.util.EnumSet
 *  java.util.HashMap
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.nfc;

import com.samsung.android.spayfw.payprovider.mastercard.pce.MTBPLite;
import com.samsung.android.spayfw.payprovider.mastercard.pce.nfc.MCAPDUCommandHandlerCCC;
import com.samsung.android.spayfw.payprovider.mastercard.pce.nfc.MCAPDUCommandHandlerGAC;
import com.samsung.android.spayfw.payprovider.mastercard.pce.nfc.MCAPDUCommandHandlerGPO;
import com.samsung.android.spayfw.payprovider.mastercard.pce.nfc.MCAPDUCommandHandlerRR;
import com.samsung.android.spayfw.payprovider.mastercard.pce.nfc.MCAPDUCommandHandlerRRP;
import com.samsung.android.spayfw.payprovider.mastercard.pce.nfc.MCAPDUCommandHandlerSELECT;
import com.samsung.android.spayfw.payprovider.mastercard.pce.nfc.MCCAPDUBaseCommandHandler;
import java.util.EnumSet;
import java.util.HashMap;

public class MCAPDUHandler {
    private static HashMap<Byte, APDUCommand> mAPDUCommands = new HashMap();
    private static HashMap<Byte, String> sApduInsNames = new HashMap();

    static {
        sApduInsNames.put((Object)-92, (Object)"Select");
        sApduInsNames.put((Object)-78, (Object)"ReadRecord");
        sApduInsNames.put((Object)-22, (Object)"RRP");
        sApduInsNames.put((Object)-88, (Object)"GPO");
        sApduInsNames.put((Object)42, (Object)"CCC");
        sApduInsNames.put((Object)-82, (Object)"GAC");
    }

    public MCAPDUHandler() {
        mAPDUCommands.put((Object)-92, (Object)new APDUCommand(new MCAPDUCommandHandlerSELECT(), (EnumSet<MTBPLite.MTBPState>)EnumSet.of((Enum)MTBPLite.MTBPState.READY, (Enum)MTBPLite.MTBPState.NFC_SELECTED, (Enum)MTBPLite.MTBPState.NFC_INITIATED), MTBPLite.MTBPState.NFC_SELECTED));
        mAPDUCommands.put((Object)-78, (Object)new APDUCommand(new MCAPDUCommandHandlerRR(), (EnumSet<MTBPLite.MTBPState>)EnumSet.of((Enum)MTBPLite.MTBPState.NFC_SELECTED, (Enum)MTBPLite.MTBPState.NFC_INITIATED), null));
        mAPDUCommands.put((Object)-22, (Object)new APDUCommand(new MCAPDUCommandHandlerRRP(), (EnumSet<MTBPLite.MTBPState>)EnumSet.of((Enum)MTBPLite.MTBPState.NFC_INITIATED), null));
        mAPDUCommands.put((Object)-88, (Object)new APDUCommand(new MCAPDUCommandHandlerGPO(), (EnumSet<MTBPLite.MTBPState>)EnumSet.of((Enum)MTBPLite.MTBPState.NFC_SELECTED), MTBPLite.MTBPState.NFC_INITIATED));
        mAPDUCommands.put((Object)42, (Object)new APDUCommand(new MCAPDUCommandHandlerCCC(), (EnumSet<MTBPLite.MTBPState>)EnumSet.of((Enum)MTBPLite.MTBPState.NFC_INITIATED), MTBPLite.MTBPState.READY));
        mAPDUCommands.put((Object)-82, (Object)new APDUCommand(new MCAPDUCommandHandlerGAC(), (EnumSet<MTBPLite.MTBPState>)EnumSet.of((Enum)MTBPLite.MTBPState.NFC_INITIATED), MTBPLite.MTBPState.READY));
    }

    public static String getApduName(byte by) {
        return (String)sApduInsNames.get((Object)by);
    }

    public APDUCommand getCommandHandlerByInstruction(byte by) {
        return (APDUCommand)mAPDUCommands.get((Object)by);
    }

    public static class APDUCommand {
        private MCCAPDUBaseCommandHandler mAPDUHandler;
        private MTBPLite.MTBPState mNextState;
        private EnumSet<MTBPLite.MTBPState> mValidStates;

        public APDUCommand(MCCAPDUBaseCommandHandler mCCAPDUBaseCommandHandler, EnumSet<MTBPLite.MTBPState> enumSet, MTBPLite.MTBPState mTBPState) {
            this.mValidStates = enumSet;
            this.mNextState = mTBPState;
            this.mAPDUHandler = mCCAPDUBaseCommandHandler;
        }

        public MCCAPDUBaseCommandHandler getAPDUHandler() {
            return this.mAPDUHandler;
        }

        public MTBPLite.MTBPState getNextState() {
            return this.mNextState;
        }

        public boolean validateState(MTBPLite.MTBPState mTBPState) {
            return this.mValidStates.contains((Object)mTBPState);
        }
    }

}

