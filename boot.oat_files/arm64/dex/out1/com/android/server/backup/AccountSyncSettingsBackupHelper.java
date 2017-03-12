package com.android.server.backup;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.backup.BackupDataInputStream;
import android.app.backup.BackupDataOutput;
import android.app.backup.BackupHelper;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncAdapterType;
import android.os.ParcelFileDescriptor;
import android.security.KeyChain;
import android.security.keystore.KeyProperties;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AccountSyncSettingsBackupHelper implements BackupHelper {
    private static final boolean DEBUG = false;
    private static final String JSON_FORMAT_ENCODING = "UTF-8";
    private static final String JSON_FORMAT_HEADER_KEY = "account_data";
    private static final int JSON_FORMAT_VERSION = 1;
    private static final String KEY_ACCOUNTS = "accounts";
    private static final String KEY_ACCOUNT_AUTHORITIES = "authorities";
    private static final String KEY_ACCOUNT_NAME = "name";
    private static final String KEY_ACCOUNT_TYPE = "type";
    private static final String KEY_AUTHORITY_NAME = "name";
    private static final String KEY_AUTHORITY_SYNC_ENABLED = "syncEnabled";
    private static final String KEY_AUTHORITY_SYNC_STATE = "syncState";
    private static final String KEY_MASTER_SYNC_ENABLED = "masterSyncEnabled";
    private static final String KEY_VERSION = "version";
    private static final int MD5_BYTE_SIZE = 16;
    private static final int STATE_VERSION = 1;
    private static final int SYNC_REQUEST_LATCH_TIMEOUT_SECONDS = 1;
    private static final String TAG = "AccountSyncSettingsBackupHelper";
    private AccountManager mAccountManager = AccountManager.get(this.mContext);
    private Context mContext;

    public AccountSyncSettingsBackupHelper(Context context) {
        this.mContext = context;
    }

    public void performBackup(ParcelFileDescriptor oldState, BackupDataOutput output, ParcelFileDescriptor newState) {
        Exception e;
        try {
            byte[] dataBytes = serializeAccountSyncSettingsToJSON().toString().getBytes(JSON_FORMAT_ENCODING);
            byte[] oldMd5Checksum = readOldMd5Checksum(oldState);
            byte[] newMd5Checksum = generateMd5Checksum(dataBytes);
            if (Arrays.equals(oldMd5Checksum, newMd5Checksum)) {
                Log.i(TAG, "Old and new MD5 checksums match. Skipping backup.");
            } else {
                int dataSize = dataBytes.length;
                output.writeEntityHeader(JSON_FORMAT_HEADER_KEY, dataSize);
                output.writeEntityData(dataBytes, dataSize);
                Log.i(TAG, "Backup successful.");
            }
            writeNewMd5Checksum(newState, newMd5Checksum);
        } catch (JSONException e2) {
            e = e2;
            Log.e(TAG, "Couldn't backup account sync settings\n" + e);
        } catch (IOException e3) {
            e = e3;
            Log.e(TAG, "Couldn't backup account sync settings\n" + e);
        } catch (NoSuchAlgorithmException e4) {
            e = e4;
            Log.e(TAG, "Couldn't backup account sync settings\n" + e);
        }
    }

    private JSONObject serializeAccountSyncSettingsToJSON() throws JSONException {
        Account[] accounts = this.mAccountManager.getAccounts();
        SyncAdapterType[] syncAdapters = ContentResolver.getSyncAdapterTypesAsUser(this.mContext.getUserId());
        HashMap<String, List<String>> accountTypeToAuthorities = new HashMap();
        for (SyncAdapterType syncAdapter : syncAdapters) {
            if (syncAdapter.isUserVisible()) {
                if (!accountTypeToAuthorities.containsKey(syncAdapter.accountType)) {
                    accountTypeToAuthorities.put(syncAdapter.accountType, new ArrayList());
                }
                ((List) accountTypeToAuthorities.get(syncAdapter.accountType)).add(syncAdapter.authority);
            }
        }
        JSONObject backupJSON = new JSONObject();
        backupJSON.put(KEY_VERSION, 1);
        backupJSON.put(KEY_MASTER_SYNC_ENABLED, ContentResolver.getMasterSyncAutomatically());
        JSONArray accountJSONArray = new JSONArray();
        for (Account account : accounts) {
            List<String> authorities = (List) accountTypeToAuthorities.get(account.type);
            if (!(authorities == null || authorities.isEmpty())) {
                JSONObject accountJSON = new JSONObject();
                accountJSON.put(KeyChain.EXTRA_NAME, account.name);
                accountJSON.put(KEY_ACCOUNT_TYPE, account.type);
                JSONArray authoritiesJSONArray = new JSONArray();
                for (String authority : authorities) {
                    int syncState = ContentResolver.getIsSyncable(account, authority);
                    boolean syncEnabled = ContentResolver.getSyncAutomatically(account, authority);
                    JSONObject authorityJSON = new JSONObject();
                    authorityJSON.put(KeyChain.EXTRA_NAME, authority);
                    authorityJSON.put(KEY_AUTHORITY_SYNC_STATE, syncState);
                    authorityJSON.put(KEY_AUTHORITY_SYNC_ENABLED, syncEnabled);
                    authoritiesJSONArray.put(authorityJSON);
                }
                accountJSON.put(KEY_ACCOUNT_AUTHORITIES, authoritiesJSONArray);
                accountJSONArray.put(accountJSON);
            }
        }
        backupJSON.put(KEY_ACCOUNTS, accountJSONArray);
        return backupJSON;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private byte[] readOldMd5Checksum(android.os.ParcelFileDescriptor r9) throws java.io.IOException {
        /*
        r8 = this;
        r7 = 16;
        r6 = 1;
        r0 = new java.io.DataInputStream;
        r4 = new java.io.FileInputStream;
        r5 = r9.getFileDescriptor();
        r4.<init>(r5);
        r0.<init>(r4);
        r2 = new byte[r7];
        r3 = r0.readInt();	 Catch:{ EOFException -> 0x0052, all -> 0x0057 }
        if (r3 > r6) goto L_0x0025;
    L_0x0019:
        r1 = 0;
    L_0x001a:
        if (r1 >= r7) goto L_0x004e;
    L_0x001c:
        r4 = r0.readByte();	 Catch:{ EOFException -> 0x0052, all -> 0x0057 }
        r2[r1] = r4;	 Catch:{ EOFException -> 0x0052, all -> 0x0057 }
        r1 = r1 + 1;
        goto L_0x001a;
    L_0x0025:
        r4 = "AccountSyncSettingsBackupHelper";
        r5 = new java.lang.StringBuilder;	 Catch:{ EOFException -> 0x0052, all -> 0x0057 }
        r5.<init>();	 Catch:{ EOFException -> 0x0052, all -> 0x0057 }
        r6 = "Backup state version is: ";
        r5 = r5.append(r6);	 Catch:{ EOFException -> 0x0052, all -> 0x0057 }
        r5 = r5.append(r3);	 Catch:{ EOFException -> 0x0052, all -> 0x0057 }
        r6 = " (support only up to version ";
        r5 = r5.append(r6);	 Catch:{ EOFException -> 0x0052, all -> 0x0057 }
        r6 = 1;
        r5 = r5.append(r6);	 Catch:{ EOFException -> 0x0052, all -> 0x0057 }
        r6 = ")";
        r5 = r5.append(r6);	 Catch:{ EOFException -> 0x0052, all -> 0x0057 }
        r5 = r5.toString();	 Catch:{ EOFException -> 0x0052, all -> 0x0057 }
        android.util.Log.i(r4, r5);	 Catch:{ EOFException -> 0x0052, all -> 0x0057 }
    L_0x004e:
        r0.close();
    L_0x0051:
        return r2;
    L_0x0052:
        r4 = move-exception;
        r0.close();
        goto L_0x0051;
    L_0x0057:
        r4 = move-exception;
        r0.close();
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.AccountSyncSettingsBackupHelper.readOldMd5Checksum(android.os.ParcelFileDescriptor):byte[]");
    }

    private void writeNewMd5Checksum(ParcelFileDescriptor newState, byte[] md5Checksum) throws IOException {
        DataOutputStream dataOutput = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(newState.getFileDescriptor())));
        dataOutput.writeInt(1);
        dataOutput.write(md5Checksum);
        dataOutput.close();
    }

    private byte[] generateMd5Checksum(byte[] data) throws NoSuchAlgorithmException {
        if (data == null) {
            return null;
        }
        return MessageDigest.getInstance(KeyProperties.DIGEST_MD5).digest(data);
    }

    public void restoreEntity(BackupDataInputStream data) {
        Exception e;
        byte[] dataBytes = new byte[data.size()];
        boolean masterSyncEnabled;
        try {
            data.read(dataBytes);
            JSONObject dataJSON = new JSONObject(new String(dataBytes, JSON_FORMAT_ENCODING));
            masterSyncEnabled = dataJSON.getBoolean(KEY_MASTER_SYNC_ENABLED);
            JSONArray accountJSONArray = dataJSON.getJSONArray(KEY_ACCOUNTS);
            if (ContentResolver.getMasterSyncAutomatically()) {
                ContentResolver.setMasterSyncAutomatically(false);
            }
            HashSet<Account> currentAccounts = getAccountsHashSet();
            for (int i = 0; i < accountJSONArray.length(); i++) {
                JSONObject accountJSON = (JSONObject) accountJSONArray.get(i);
                if (currentAccounts.contains(new Account(accountJSON.getString(KeyChain.EXTRA_NAME), accountJSON.getString(KEY_ACCOUNT_TYPE)))) {
                    restoreExistingAccountSyncSettingsFromJSON(accountJSON);
                }
            }
            ContentResolver.setMasterSyncAutomatically(masterSyncEnabled);
            Log.i(TAG, "Restore successful.");
        } catch (IOException e2) {
            e = e2;
            Log.e(TAG, "Couldn't restore account sync settings\n" + e);
        } catch (JSONException e3) {
            e = e3;
            Log.e(TAG, "Couldn't restore account sync settings\n" + e);
        } catch (Throwable th) {
            ContentResolver.setMasterSyncAutomatically(masterSyncEnabled);
        }
    }

    private HashSet<Account> getAccountsHashSet() {
        Account[] accounts = this.mAccountManager.getAccounts();
        HashSet<Account> accountHashSet = new HashSet();
        for (Account account : accounts) {
            accountHashSet.add(account);
        }
        return accountHashSet;
    }

    private void restoreExistingAccountSyncSettingsFromJSON(JSONObject accountJSON) throws JSONException {
        JSONArray authorities = accountJSON.getJSONArray(KEY_ACCOUNT_AUTHORITIES);
        Account account = new Account(accountJSON.getString(KeyChain.EXTRA_NAME), accountJSON.getString(KEY_ACCOUNT_TYPE));
        for (int i = 0; i < authorities.length(); i++) {
            JSONObject authority = (JSONObject) authorities.get(i);
            String authorityName = authority.getString(KeyChain.EXTRA_NAME);
            boolean wasSyncEnabled = authority.getBoolean(KEY_AUTHORITY_SYNC_ENABLED);
            int wasSyncable = authority.getInt(KEY_AUTHORITY_SYNC_STATE);
            ContentResolver.setSyncAutomaticallyAsUser(account, authorityName, wasSyncEnabled, 0);
            if (!wasSyncEnabled) {
                int i2;
                if (wasSyncable == 0) {
                    i2 = 0;
                } else {
                    i2 = 2;
                }
                ContentResolver.setIsSyncable(account, authorityName, i2);
            }
        }
    }

    public void writeNewStateDescription(ParcelFileDescriptor newState) {
    }
}
