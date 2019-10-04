/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.provider.BaseColumns
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.db;

import android.provider.BaseColumns;

public final class McDbContract {
    private static final String BLOB_TYPE = " BLOB";
    private static final String BLOB_TYPE_NOT_NULL = " BLOB NOT NULL";
    public static final String CARD_MASTER_ID = "cardMasterId";
    private static final String COMMA_SEP = ",";
    public static final String DB_NAME = "mc_enc.db";
    private static final String DELETE_CASCADE = " ON DELETE CASCADE";
    private static final String INT_TYPE = " INTEGER";
    private static final String INT_TYPE_NOT_NULL = " INTEGER NOT NULL";
    private static final String TEXT_TYPE = " TEXT";
    private static final String TEXT_TYPE_NOT_NULL = " TEXT NOT NULL";
    public static final int VERSION = 1;

    public static abstract class CardMaster
    implements BaseColumns {
        public static final String COL_ACCOUNT_PAN_SUFFIX = "accountPanSuffix";
        public static final String COL_MPA_INSTANCE_ID = "mpaInstanceId";
        public static final String COL_RGK_KEYS = "rgkDerivedKeys";
        public static final String COL_STATUS = "status";
        public static final String COL_SUSPENDEDBY = "suspendedBy";
        public static final String COL_TOKEN_EXPIRY = "tokenExpiry";
        public static final String COL_TOKEN_PAN_SUFFIX = "tokenPanSuffix";
        public static final String COL_TOKEN_PROVISIONED = "isProvisioned";
        public static final String COL_TOKEN_UNIQUE_REFERENCE = "tokenUniqueReference";
        public static final String CREATE_TABLE = "CREATE TABLE CARD_MASTER (_id INTEGER PRIMARY KEY AUTOINCREMENT,tokenUniqueReference TEXT,mpaInstanceId TEXT,status TEXT,suspendedBy TEXT,tokenPanSuffix TEXT,accountPanSuffix TEXT,isProvisioned INTEGER NOT NULL,rgkDerivedKeys TEXT,tokenExpiry TEXT )";
        public static final String TABLE_NAME = "CARD_MASTER";
    }

    public static abstract class CardProvisionData
    implements BaseColumns {
        public static final String COL_CARD_MASTER_ID = "cardMasterId";
        public static final String COL_DATA = "data";
        public static final String COL_DATA_ID = "dataId";
        public static final String CREATE_TABLE = "CREATE TABLE CARD_PROVISION_DATA (_id INTEGER PRIMARY KEY AUTOINCREMENT,cardMasterId INTEGER NOT NULL,dataId INTEGER NOT NULL,data BLOB NOT NULL, FOREIGN KEY(cardMasterId) REFERENCES CARD_MASTER(_id) ON DELETE CASCADE )";
        public static final String TABLE_NAME = "CARD_PROVISION_DATA";
    }

    public static abstract class Certificates
    implements BaseColumns {
        public static final String COL_CARD_INFO_CERT_ALIAS = "cardInfoCertAlias";
        public static final String COL_CARD_INFO_CERT_PEM = "cardInfoCertPem";
        public static final String COL_PUBLIC_CERT_ALIAS = "publicCertAlias";
        public static final String COL_PUBLIC_CERT_PEM = "publicCertPem";
        public static final String CREATE_TABLE = "CREATE TABLE CERTIFICATES (_id INTEGER PRIMARY KEY,publicCertPem TEXT NOT NULL,publicCertAlias TEXT NOT NULL,cardInfoCertPem TEXT NOT NULL,cardInfoCertAlias TEXT NOT NULL )";
        public static final String TABLE_NAME = "CERTIFICATES";
    }

    public static abstract class TdsMetaData
    implements BaseColumns {
        public static final String COL_AUTH_CODE = "authCode";
        public static final String COL_CARD_MASTER_ID = "cardMasterId";
        public static final String COL_HASH = "hash";
        public static final String COL_LAST_UPDATE_TAG = "lastUpdateTag";
        public static final String COL_PAYMENT_APP_INSTANCE_ID = "paymentAppInstanceId";
        public static final String COL_TDS_REGISTRATION_URL = "tdsRegisterUrl";
        public static final String COL_TDS_URL = "tdsUrl";
        public static final String CREATE_TABLE = "CREATE TABLE TDS_METADATA (_id INTEGER PRIMARY KEY AUTOINCREMENT,cardMasterId INTEGER NOT NULL,tdsUrl TEXT,tdsRegisterUrl TEXT,paymentAppInstanceId TEXT,hash TEXT,authCode TEXT,lastUpdateTag, FOREIGN KEY(cardMasterId) REFERENCES CARD_MASTER(_id) ON DELETE CASCADE )";
        public static final String TABLE_NAME = "TDS_METADATA";
    }

}

