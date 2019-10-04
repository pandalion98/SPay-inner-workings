/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.sdkmodulelib.model.apdu;

public final class APDURequestCommand
extends Enum<APDURequestCommand> {
    private static final /* synthetic */ APDURequestCommand[] $VALUES;
    public static final /* enum */ APDURequestCommand GENAC;
    public static final /* enum */ APDURequestCommand GETDATA;
    public static final /* enum */ APDURequestCommand GPO;
    public static final /* enum */ APDURequestCommand READ;
    public static final /* enum */ APDURequestCommand SELECT_AID;
    public static final /* enum */ APDURequestCommand SELECT_PPSE;
    private int workflowStep;

    static {
        SELECT_PPSE = new APDURequestCommand(0);
        SELECT_AID = new APDURequestCommand(1);
        GPO = new APDURequestCommand(2);
        READ = new APDURequestCommand(3);
        GETDATA = new APDURequestCommand(4);
        GENAC = new APDURequestCommand(5);
        APDURequestCommand[] arraPDURequestCommand = new APDURequestCommand[]{SELECT_PPSE, SELECT_AID, GPO, READ, GETDATA, GENAC};
        $VALUES = arraPDURequestCommand;
    }

    private APDURequestCommand(int n3) {
        this.workflowStep = n3;
    }

    public static APDURequestCommand valueOf(String string) {
        return (APDURequestCommand)Enum.valueOf(APDURequestCommand.class, (String)string);
    }

    public static APDURequestCommand[] values() {
        return (APDURequestCommand[])$VALUES.clone();
    }

    public int getWorkflowStep() {
        return this.workflowStep;
    }
}

