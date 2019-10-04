/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.sdkmodulelib.apdu;

import com.americanexpress.sdkmodulelib.apdu.ApplicationSelection;
import com.americanexpress.sdkmodulelib.apdu.CommandProcessor;
import com.americanexpress.sdkmodulelib.apdu.FirstCardActionAnalysisGenAc;
import com.americanexpress.sdkmodulelib.apdu.InitialApplicationProcessing;
import com.americanexpress.sdkmodulelib.apdu.ReadApplicationData;
import com.americanexpress.sdkmodulelib.apdu.TerminalRiskManagementGetData;
import com.americanexpress.sdkmodulelib.exception.APDUCommandProcessorException;
import com.americanexpress.sdkmodulelib.model.apdu.APDU;
import com.americanexpress.sdkmodulelib.model.apdu.APDURequestCommand;
import com.americanexpress.sdkmodulelib.model.apdu.CommandInfo;

public class APDUCommandProcessorFactory {
    public static CommandProcessor getCommandProcessor(CommandInfo commandInfo) {
        if (commandInfo == null) {
            throw new APDUCommandProcessorException("CommandInfo is null or cannot be recognized");
        }
        switch (1.$SwitchMap$com$americanexpress$sdkmodulelib$model$apdu$APDURequestCommand[commandInfo.getApdu().getCommand().ordinal()]) {
            default: {
                throw new APDUCommandProcessorException("Command not recognized: " + (Object)((Object)commandInfo.getApdu().getCommand()));
            }
            case 1: {
                return new ApplicationSelection();
            }
            case 2: {
                return new ApplicationSelection();
            }
            case 3: {
                return new InitialApplicationProcessing();
            }
            case 4: {
                return new ReadApplicationData();
            }
            case 5: {
                return new TerminalRiskManagementGetData();
            }
            case 6: 
        }
        return new FirstCardActionAnalysisGenAc();
    }

}

