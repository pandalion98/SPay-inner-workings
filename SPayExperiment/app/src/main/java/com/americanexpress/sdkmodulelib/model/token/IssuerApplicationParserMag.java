/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.americanexpress.sdkmodulelib.model.token;

import com.americanexpress.sdkmodulelib.model.token.IssuerApplicationParser;

public class IssuerApplicationParserMag
extends IssuerApplicationParser {
    @Override
    public void init(String string) {
        super.init(string, "#S9301#9301", "#E9301#");
    }
}

