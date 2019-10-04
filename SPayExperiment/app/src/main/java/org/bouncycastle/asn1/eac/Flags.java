/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.util.Enumeration
 *  java.util.Hashtable
 */
package org.bouncycastle.asn1.eac;

import java.util.Enumeration;
import java.util.Hashtable;

public class Flags {
    int value = 0;

    public Flags() {
    }

    public Flags(int n2) {
        this.value = n2;
    }

    String decode(Hashtable hashtable) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        Enumeration enumeration = hashtable.keys();
        while (enumeration.hasMoreElements()) {
            Integer n2 = (Integer)enumeration.nextElement();
            if (!this.isSet(n2)) continue;
            stringJoiner.add((String)hashtable.get((Object)n2));
        }
        return stringJoiner.toString();
    }

    public int getFlags() {
        return this.value;
    }

    public boolean isSet(int n2) {
        return (n2 & this.value) != 0;
    }

    public void set(int n2) {
        this.value = n2 | this.value;
    }

    private class StringJoiner {
        boolean First = true;
        StringBuffer b = new StringBuffer();
        String mSeparator;

        public StringJoiner(String string) {
            this.mSeparator = string;
        }

        /*
         * Enabled aggressive block sorting
         */
        public void add(String string) {
            if (this.First) {
                this.First = false;
            } else {
                this.b.append(this.mSeparator);
            }
            this.b.append(string);
        }

        public String toString() {
            return this.b.toString();
        }
    }

}

