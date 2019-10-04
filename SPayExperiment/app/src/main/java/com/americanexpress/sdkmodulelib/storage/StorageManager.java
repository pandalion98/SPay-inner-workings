/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.HashMap
 */
package com.americanexpress.sdkmodulelib.storage;

import android.content.Context;
import android.content.SharedPreferences;
import com.americanexpress.sdkmodulelib.model.TokenDataRecord;
import com.americanexpress.sdkmodulelib.util.TrustedAppFactory;
import java.util.HashMap;

public class StorageManager {
    private static final String TOKEN_DATA_BLOB_STORAGE = "TOKEN_DATA_BLOB_STORAGE_SharedPref";
    private static HashMap<String, String> TOKEN_DATA_STORE = new HashMap();
    private SharedPreferences sharedPref = null;

    private String get(String string) {
        if (TrustedAppFactory.isMockTrustedApp()) {
            return (String)TOKEN_DATA_STORE.get((Object)string);
        }
        if (this.sharedPref == null) {
            this.sharedPref = TrustedAppFactory.getTrustedApp().getApplicationContext().getSharedPreferences(TOKEN_DATA_BLOB_STORAGE, 0);
        }
        return this.sharedPref.getString(string, null);
    }

    private void put(String string, String string2) {
        if (TrustedAppFactory.isMockTrustedApp()) {
            TOKEN_DATA_STORE.put((Object)string, (Object)string2);
            return;
        }
        if (this.sharedPref == null) {
            this.sharedPref = TrustedAppFactory.getTrustedApp().getApplicationContext().getSharedPreferences(TOKEN_DATA_BLOB_STORAGE, 0);
        }
        SharedPreferences.Editor editor = this.sharedPref.edit();
        editor.putString(string, string2);
        editor.commit();
    }

    public void delete(String string) {
        if (TrustedAppFactory.isMockTrustedApp()) {
            TOKEN_DATA_STORE.remove((Object)string);
            return;
        }
        if (this.sharedPref == null) {
            this.sharedPref = TrustedAppFactory.getTrustedApp().getApplicationContext().getSharedPreferences(TOKEN_DATA_BLOB_STORAGE, 0);
        }
        SharedPreferences.Editor editor = this.sharedPref.edit();
        editor.remove(string);
        editor.commit();
    }

    public TokenDataRecord fetch(String string) {
        String string2 = this.get(string);
        TokenDataRecord tokenDataRecord = new TokenDataRecord();
        tokenDataRecord.setTokenRefId(string);
        tokenDataRecord.setApduBlob(string2.substring(string2.indexOf("#APDU_ENCRYPTED_START#") + "#APDU_ENCRYPTED_START#".length(), string2.indexOf("#APDU_ENCRYPTED_END#")));
        tokenDataRecord.setNfcLUPCBlob(string2.substring(string2.indexOf("#NFC_LUPC_S9400#9400") + "#NFC_LUPC_S9400#9400".length(), string2.indexOf("#NFC_LUPC_E9400#")));
        tokenDataRecord.setOtherLUPCBlob(string2.substring(string2.indexOf("#OTHER_LUPC_S9600#9600") + "#OTHER_LUPC_S9600#9600".length(), string2.indexOf("#OTHER_LUPC_E9600#")));
        tokenDataRecord.setMetaDataBlob(string2.substring(string2.indexOf("#METADATA_S9500#9500") + "#METADATA_S9500#9500".length(), string2.indexOf("#METADATA_E9500#")));
        tokenDataRecord.setLupcMetadataBlob(string2.substring(string2.indexOf("#LUPC_METADATA#") + "#LUPC_METADATA#".length(), string2.indexOf("#LUPC_METADATA_END#")));
        return tokenDataRecord;
    }

    public void save(TokenDataRecord tokenDataRecord) {
        String string = tokenDataRecord.getTokenRefId();
        String string2 = tokenDataRecord.getApduBlob();
        String string3 = tokenDataRecord.getMetaDataBlob();
        if (string2 == null || string3 == null) {
            TokenDataRecord tokenDataRecord2 = this.fetch(tokenDataRecord.getTokenRefId());
            if (string2 == null) {
                string2 = tokenDataRecord2.getApduBlob();
            }
            if (string3 == null) {
                string3 = tokenDataRecord2.getMetaDataBlob();
            }
        }
        String string4 = tokenDataRecord.getNfcLUPCBlob();
        String string5 = tokenDataRecord.getOtherLUPCBlob();
        String string6 = tokenDataRecord.getLupcMetadataBlob();
        if (string2 == null || string3 == null || string4 == null || string5 == null || string6 == null) {
            throw new Exception("Storage failure, missing blob");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("#APDU_ENCRYPTED_START#").append(string2).append("#APDU_ENCRYPTED_END#");
        stringBuilder.append("#NFC_LUPC_S9400#9400").append(string4).append("#NFC_LUPC_E9400#");
        stringBuilder.append("#OTHER_LUPC_S9600#9600").append(string5).append("#OTHER_LUPC_E9600#");
        stringBuilder.append("#METADATA_S9500#9500").append(string3).append("#METADATA_E9500#");
        stringBuilder.append("#LUPC_METADATA#").append(string6).append("#LUPC_METADATA_END#");
        this.put(string, stringBuilder.toString());
    }
}

