package com.americanexpress.sdkmodulelib.model.apdu;

public enum APDURequestCommand {
    SELECT_PPSE(0),
    SELECT_AID(1),
    GPO(2),
    READ(3),
    GETDATA(4),
    GENAC(5);
    
    private int workflowStep;

    private APDURequestCommand(int i) {
        this.workflowStep = i;
    }

    public int getWorkflowStep() {
        return this.workflowStep;
    }
}
