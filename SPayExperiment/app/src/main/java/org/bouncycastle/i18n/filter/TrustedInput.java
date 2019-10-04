/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.i18n.filter;

public class TrustedInput {
    protected Object input;

    public TrustedInput(Object object) {
        this.input = object;
    }

    public Object getInput() {
        return this.input;
    }

    public String toString() {
        return this.input.toString();
    }
}

