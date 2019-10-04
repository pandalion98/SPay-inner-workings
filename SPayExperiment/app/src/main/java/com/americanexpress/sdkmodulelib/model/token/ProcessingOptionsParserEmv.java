/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.americanexpress.sdkmodulelib.model.token;

import com.americanexpress.sdkmodulelib.model.token.ProcessingOptionsParser;

public class ProcessingOptionsParserEmv
extends ProcessingOptionsParser {
    @Override
    public void init(String string) {
        super.init(string, "#S9104#9104", "#E9104#");
    }
}

