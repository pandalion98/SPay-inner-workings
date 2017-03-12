package com.americanexpress.sdkmodulelib.model.token;

import java.util.HashMap;

public class ParsedTokenRecord {
    private HashMap<Class, DataGroup> dataGroups;
    private boolean isTokenDataContainsEMV;
    private String tokenDataBlob;
    private boolean tokenMalformed;
    private boolean tokenPartiallyMalformed;

    public ParsedTokenRecord() {
        this.tokenPartiallyMalformed = false;
        this.tokenMalformed = false;
        this.isTokenDataContainsEMV = false;
    }

    public HashMap<Class, DataGroup> getDataGroups() {
        return this.dataGroups;
    }

    public void setDataGroups(HashMap<Class, DataGroup> hashMap) {
        this.dataGroups = hashMap;
    }

    public boolean isTokenPartiallyMalformed() {
        return this.tokenPartiallyMalformed;
    }

    public void setTokenPartiallyMalformed(boolean z) {
        this.tokenPartiallyMalformed = z;
    }

    public boolean isTokenMalformed() {
        return this.tokenMalformed;
    }

    public void setTokenMalformed(boolean z) {
        this.tokenMalformed = z;
    }

    public String getTokenDataBlob() {
        return this.tokenDataBlob;
    }

    public void setTokenDataBlob(String str) {
        this.tokenDataBlob = str;
    }

    public boolean isTokenDataContainsEMV() {
        return this.isTokenDataContainsEMV;
    }

    public void setIsTokenDataContainsEMV(boolean z) {
        this.isTokenDataContainsEMV = z;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\norginalTokenDataBlob length=").append(getTokenDataBlob() != null ? getTokenDataBlob().length() : 0);
        stringBuilder.append("\nisTokenDataContainsEMV=").append(this.isTokenDataContainsEMV);
        stringBuilder.append("\ntokenMalformed=").append(isTokenMalformed());
        stringBuilder.append("\ntokenPartiallyMalformed=").append(isTokenPartiallyMalformed());
        return stringBuilder.toString();
    }
}
