package com.americanexpress.sdkmodulelib.storage;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.americanexpress.sdkmodulelib.model.TokenDataRecord;
import com.americanexpress.sdkmodulelib.util.Constants;
import com.americanexpress.sdkmodulelib.util.TrustedAppFactory;
import java.util.HashMap;

public class StorageManager {
    private static final String TOKEN_DATA_BLOB_STORAGE = "TOKEN_DATA_BLOB_STORAGE_SharedPref";
    private static HashMap<String, String> TOKEN_DATA_STORE;
    private SharedPreferences sharedPref;

    public StorageManager() {
        this.sharedPref = null;
    }

    static {
        TOKEN_DATA_STORE = new HashMap();
    }

    public void save(TokenDataRecord tokenDataRecord) {
        String tokenRefId = tokenDataRecord.getTokenRefId();
        String apduBlob = tokenDataRecord.getApduBlob();
        String metaDataBlob = tokenDataRecord.getMetaDataBlob();
        if (apduBlob == null || metaDataBlob == null) {
            TokenDataRecord fetch = fetch(tokenDataRecord.getTokenRefId());
            if (apduBlob == null) {
                apduBlob = fetch.getApduBlob();
            }
            if (metaDataBlob == null) {
                metaDataBlob = fetch.getMetaDataBlob();
            }
        }
        String nfcLUPCBlob = tokenDataRecord.getNfcLUPCBlob();
        String otherLUPCBlob = tokenDataRecord.getOtherLUPCBlob();
        String lupcMetadataBlob = tokenDataRecord.getLupcMetadataBlob();
        if (apduBlob == null || metaDataBlob == null || nfcLUPCBlob == null || otherLUPCBlob == null || lupcMetadataBlob == null) {
            throw new Exception("Storage failure, missing blob");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Constants.APDU_ENCRYPTED_DGI_START_TAG).append(apduBlob).append(Constants.APDU_ENCRYPTED_DGI_END_TAG);
        stringBuilder.append(Constants.NFC_LUPC_START_TAG).append(nfcLUPCBlob).append(Constants.NFC_LUPC_END_TAG);
        stringBuilder.append(Constants.OTHER_LUPC_START_TAG).append(otherLUPCBlob).append(Constants.OTHER_LUPC_END_TAG);
        stringBuilder.append(Constants.METADATA_START_TAG).append(metaDataBlob).append(Constants.METADATA_END_TAG);
        stringBuilder.append(Constants.LUPC_METADATA_START_TAG).append(lupcMetadataBlob).append(Constants.LUPC_METADATA_END_TAG);
        put(tokenRefId, stringBuilder.toString());
    }

    public TokenDataRecord fetch(String str) {
        String str2 = get(str);
        TokenDataRecord tokenDataRecord = new TokenDataRecord();
        tokenDataRecord.setTokenRefId(str);
        tokenDataRecord.setApduBlob(str2.substring(str2.indexOf(Constants.APDU_ENCRYPTED_DGI_START_TAG) + Constants.APDU_ENCRYPTED_DGI_START_TAG.length(), str2.indexOf(Constants.APDU_ENCRYPTED_DGI_END_TAG)));
        tokenDataRecord.setNfcLUPCBlob(str2.substring(str2.indexOf(Constants.NFC_LUPC_START_TAG) + Constants.NFC_LUPC_START_TAG.length(), str2.indexOf(Constants.NFC_LUPC_END_TAG)));
        tokenDataRecord.setOtherLUPCBlob(str2.substring(str2.indexOf(Constants.OTHER_LUPC_START_TAG) + Constants.OTHER_LUPC_START_TAG.length(), str2.indexOf(Constants.OTHER_LUPC_END_TAG)));
        tokenDataRecord.setMetaDataBlob(str2.substring(str2.indexOf(Constants.METADATA_START_TAG) + Constants.METADATA_START_TAG.length(), str2.indexOf(Constants.METADATA_END_TAG)));
        tokenDataRecord.setLupcMetadataBlob(str2.substring(str2.indexOf(Constants.LUPC_METADATA_START_TAG) + Constants.LUPC_METADATA_START_TAG.length(), str2.indexOf(Constants.LUPC_METADATA_END_TAG)));
        return tokenDataRecord;
    }

    public void delete(String str) {
        if (TrustedAppFactory.isMockTrustedApp()) {
            TOKEN_DATA_STORE.remove(str);
            return;
        }
        if (this.sharedPref == null) {
            this.sharedPref = TrustedAppFactory.getTrustedApp().getApplicationContext().getSharedPreferences(TOKEN_DATA_BLOB_STORAGE, 0);
        }
        Editor edit = this.sharedPref.edit();
        edit.remove(str);
        edit.commit();
    }

    private void put(String str, String str2) {
        if (TrustedAppFactory.isMockTrustedApp()) {
            TOKEN_DATA_STORE.put(str, str2);
            return;
        }
        if (this.sharedPref == null) {
            this.sharedPref = TrustedAppFactory.getTrustedApp().getApplicationContext().getSharedPreferences(TOKEN_DATA_BLOB_STORAGE, 0);
        }
        Editor edit = this.sharedPref.edit();
        edit.putString(str, str2);
        edit.commit();
    }

    private String get(String str) {
        if (TrustedAppFactory.isMockTrustedApp()) {
            return (String) TOKEN_DATA_STORE.get(str);
        }
        if (this.sharedPref == null) {
            this.sharedPref = TrustedAppFactory.getTrustedApp().getApplicationContext().getSharedPreferences(TOKEN_DATA_BLOB_STORAGE, 0);
        }
        return this.sharedPref.getString(str, null);
    }
}
