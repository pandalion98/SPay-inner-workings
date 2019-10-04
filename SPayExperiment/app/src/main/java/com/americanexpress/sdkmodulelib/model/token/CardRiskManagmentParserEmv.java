/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.americanexpress.sdkmodulelib.model.token;

import com.americanexpress.sdkmodulelib.model.token.CardRiskManagmentParser;

public class CardRiskManagmentParserEmv
extends CardRiskManagmentParser {
    @Override
    public void init(String string) {
        super.init(string, "#S0103#0103", "#E0103#");
    }
}

