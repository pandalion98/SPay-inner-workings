/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.americanexpress.sdkmodulelib.model.token;

import com.americanexpress.sdkmodulelib.model.token.Track2Info;

public class Track2InfoMag
extends Track2Info {
    private boolean isUsingDeprecateBlob(String string) {
        int n2 = string.indexOf("#S0101#0101");
        int n3 = string.indexOf("#E0101#");
        return n2 != -1 && n3 != -1;
    }

    @Override
    public void init(String string) {
        if (this.isUsingDeprecateBlob(string)) {
            super.init(string, "#S0101#0101", "#E0101#");
            return;
        }
        super.init(string, "#S2101#2101", "#E2101#");
    }
}

