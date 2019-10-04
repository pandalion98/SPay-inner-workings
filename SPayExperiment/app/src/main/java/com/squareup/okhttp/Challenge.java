/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.squareup.okhttp;

import com.squareup.okhttp.internal.Util;

public final class Challenge {
    private final String realm;
    private final String scheme;

    public Challenge(String string, String string2) {
        this.scheme = string;
        this.realm = string2;
    }

    public boolean equals(Object object) {
        return object instanceof Challenge && Util.equal(this.scheme, ((Challenge)object).scheme) && Util.equal(this.realm, ((Challenge)object).realm);
    }

    public String getRealm() {
        return this.realm;
    }

    public String getScheme() {
        return this.scheme;
    }

    /*
     * Enabled aggressive block sorting
     */
    public int hashCode() {
        int n2 = this.realm != null ? this.realm.hashCode() : 0;
        int n3 = 31 * (n2 + 899);
        String string = this.scheme;
        int n4 = 0;
        if (string != null) {
            n4 = this.scheme.hashCode();
        }
        return n3 + n4;
    }

    public String toString() {
        return this.scheme + " realm=\"" + this.realm + "\"";
    }
}

