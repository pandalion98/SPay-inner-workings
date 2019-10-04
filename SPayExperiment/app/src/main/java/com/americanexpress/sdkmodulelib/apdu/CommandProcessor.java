/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.americanexpress.sdkmodulelib.apdu;

import com.americanexpress.sdkmodulelib.model.APDUResponse;
import com.americanexpress.sdkmodulelib.model.Session;
import com.americanexpress.sdkmodulelib.model.apdu.CommandInfo;
import com.americanexpress.sdkmodulelib.model.token.ParsedTokenRecord;

public interface CommandProcessor {
    public APDUResponse getApduResponse(CommandInfo var1, ParsedTokenRecord var2, Session var3);
}

