/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.payments.nfc;

import com.americanexpress.mobilepayments.hceclient.model.TokenAPDUResponse;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.CommandAPDU;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.CommandProcess;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.process.ApplicationSelectionProcess;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.process.FirstCardActionAnalysisProcess;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.process.InitialApplicationProcessingProcess;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.process.ReadApplicationDataProcess;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.process.TerminalRiskManagmentProcess;

public final class CommandSet
extends Enum<CommandSet> {
    private static final /* synthetic */ CommandSet[] $VALUES;
    public static final /* enum */ CommandSet APPLICATION_SELECTION = new CommandSet(0, -92, new ApplicationSelectionProcess());
    public static final /* enum */ CommandSet GEN_AC;
    public static final /* enum */ CommandSet GET_DATA;
    public static final /* enum */ CommandSet GPO;
    public static final /* enum */ CommandSet READ_RECORD;
    private byte bCLA;
    private byte bINS;
    private CommandProcess commandProcess;

    static {
        GPO = new CommandSet(-128, -88, new InitialApplicationProcessingProcess());
        READ_RECORD = new CommandSet(0, -78, new ReadApplicationDataProcess());
        GET_DATA = new CommandSet(-128, -54, new TerminalRiskManagmentProcess());
        GEN_AC = new CommandSet(-128, -82, new FirstCardActionAnalysisProcess());
        CommandSet[] arrcommandSet = new CommandSet[]{APPLICATION_SELECTION, GPO, READ_RECORD, GET_DATA, GEN_AC};
        $VALUES = arrcommandSet;
    }

    private CommandSet(byte by, byte by2, CommandProcess commandProcess) {
        this.bCLA = by;
        this.bINS = by2;
        this.commandProcess = commandProcess;
    }

    public static CommandSet valueOf(String string) {
        return (CommandSet)Enum.valueOf(CommandSet.class, (String)string);
    }

    public static CommandSet[] values() {
        return (CommandSet[])$VALUES.clone();
    }

    public byte getbCLA() {
        return this.bCLA;
    }

    public byte getbINS() {
        return this.bINS;
    }

    public TokenAPDUResponse process(CommandAPDU commandAPDU) {
        new TokenAPDUResponse().setsSW((short)28416);
        try {
            this.commandProcess.set();
            TokenAPDUResponse tokenAPDUResponse = this.commandProcess.process(commandAPDU);
            return tokenAPDUResponse;
        }
        finally {
            this.commandProcess.reset();
        }
    }
}

