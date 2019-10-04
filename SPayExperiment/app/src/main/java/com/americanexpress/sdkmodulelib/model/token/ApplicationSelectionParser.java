/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 *  java.lang.StringBuilder
 */
package com.americanexpress.sdkmodulelib.model.token;

import com.americanexpress.sdkmodulelib.model.token.DataGroup;

public class ApplicationSelectionParser
extends DataGroup {
    @Override
    public void init(String string) {
        super.init(string, "#S9102#9102", "#E9102#");
    }

    @Override
    public void parseDataGroup() {
        if (!this.isDataGroupMalformed()) {
            // empty if block
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString());
        return stringBuilder.toString();
    }
}

