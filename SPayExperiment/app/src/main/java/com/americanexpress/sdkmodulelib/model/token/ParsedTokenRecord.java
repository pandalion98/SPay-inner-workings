/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.HashMap
 */
package com.americanexpress.sdkmodulelib.model.token;

import com.americanexpress.sdkmodulelib.model.token.DataGroup;
import java.util.HashMap;

public class ParsedTokenRecord {
    private HashMap<Class, DataGroup> dataGroups;
    private boolean isTokenDataContainsEMV = false;
    private String tokenDataBlob;
    private boolean tokenMalformed = false;
    private boolean tokenPartiallyMalformed = false;

    public HashMap<Class, DataGroup> getDataGroups() {
        return this.dataGroups;
    }

    public String getTokenDataBlob() {
        return this.tokenDataBlob;
    }

    public boolean isTokenDataContainsEMV() {
        return this.isTokenDataContainsEMV;
    }

    public boolean isTokenMalformed() {
        return this.tokenMalformed;
    }

    public boolean isTokenPartiallyMalformed() {
        return this.tokenPartiallyMalformed;
    }

    public void setDataGroups(HashMap<Class, DataGroup> hashMap) {
        this.dataGroups = hashMap;
    }

    public void setIsTokenDataContainsEMV(boolean bl) {
        this.isTokenDataContainsEMV = bl;
    }

    public void setTokenDataBlob(String string) {
        this.tokenDataBlob = string;
    }

    public void setTokenMalformed(boolean bl) {
        this.tokenMalformed = bl;
    }

    public void setTokenPartiallyMalformed(boolean bl) {
        this.tokenPartiallyMalformed = bl;
    }

    /*
     * Enabled aggressive block sorting
     */
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = stringBuilder.append("\norginalTokenDataBlob length=");
        int n2 = this.getTokenDataBlob() != null ? this.getTokenDataBlob().length() : 0;
        stringBuilder2.append(n2);
        stringBuilder.append("\nisTokenDataContainsEMV=").append(this.isTokenDataContainsEMV);
        stringBuilder.append("\ntokenMalformed=").append(this.isTokenMalformed());
        stringBuilder.append("\ntokenPartiallyMalformed=").append(this.isTokenPartiallyMalformed());
        return stringBuilder.toString();
    }
}

