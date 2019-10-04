/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.text.TextUtils
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Exception
 *  java.lang.Long
 *  java.lang.Math
 *  java.lang.NoSuchFieldError
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.concurrent.atomic.AtomicLong
 */
package com.samsung.android.spayfw.payprovider.mastercard.tds;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.i;
import com.samsung.android.spayfw.payprovider.mastercard.McProvider;
import com.samsung.android.spayfw.payprovider.mastercard.card.McCardMaster;
import com.samsung.android.spayfw.payprovider.mastercard.dao.McCardMasterDaoImpl;
import com.samsung.android.spayfw.payprovider.mastercard.dao.McTdsMetaDataDaoImpl;
import com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsRequestBuilder;
import com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsRequestor;
import com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsTimerUtil;
import com.samsung.android.spayfw.payprovider.mastercard.tds.network.models.McTdsError;
import com.samsung.android.spayfw.payprovider.mastercard.tds.network.models.McTdsMetaData;
import com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement.McTokenManager;
import com.samsung.android.spayfw.payprovider.mastercard.utils.CryptoUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class McTdsManager {
    private static final String DEFAULT_TOKEN_REF = "ErrTokenRef";
    private static final String TAG = "McTdsManager";
    private static final String TDS_TAG_ERROR = "e__McTdsManager";
    private static final String TDS_TAG_INFO = "i__McTdsManager";
    private static final int TOKEN_SUFFIX_DISP_LEN = 8;
    private static final Map<Long, McTdsManager> mInstanceMap = new HashMap();
    private static final List<Long> sRegistrationPendingList;
    private static final Map<Long, RegistrationState> sRegistrationStatusMap;
    private String mRegCode1;
    private String mRegCode2;
    private String mResponseHostTds;
    private String mResponseHostTdsReg;
    private final AtomicLong mTokenId;
    private final String mTokenRefSuffix;

    static {
        sRegistrationStatusMap = new HashMap();
        sRegistrationPendingList = new ArrayList();
    }

    private McTdsManager(long l2) {
        this.mTokenId = new AtomicLong(l2);
        this.mTokenRefSuffix = this.getTokenSuffix();
        RegistrationState registrationState = McTdsManager.getCurrentRegistrationState(l2, true);
        if (registrationState == null) {
            registrationState = RegistrationState.DEFAULT;
        }
        c.e(TDS_TAG_INFO, "tokenId: " + l2 + " init: " + registrationState.getValue());
        McTdsManager.setRegistrationState(l2, registrationState);
        if (registrationState == RegistrationState.TDS_NOT_REGISTERED) {
            c.e(TDS_TAG_ERROR, "tokenId: " + l2 + " add to List" + registrationState.getValue());
            McTdsManager.addTokenToRegPendingList(l2);
            McTdsTimerUtil.startTdsTimer();
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static void addTokenToRegPendingList(Long l2) {
        Class<McTdsManager> class_ = McTdsManager.class;
        synchronized (McTdsManager.class) {
            block7 : {
                if (l2 != null) {
                    boolean bl = sRegistrationPendingList.contains((Object)l2);
                    if (!bl) break block7;
                }
                do {
                    return;
                    break;
                } while (true);
            }
            try {
                sRegistrationPendingList.add((Object)l2);
                return;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
            finally {
                // ** MonitorExit[var4_1] (shouldn't be in output)
            }
        }
    }

    private void cleanupRegCodes() {
        McTdsManager mcTdsManager = this;
        synchronized (mcTdsManager) {
            this.mRegCode1 = null;
            this.mRegCode2 = null;
            return;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static void deleteTokenFromRegPendingList(Long l2) {
        Class<McTdsManager> class_ = McTdsManager.class;
        synchronized (McTdsManager.class) {
            if (l2 == null) {
                do {
                    return;
                    break;
                } while (true);
            }
            sRegistrationPendingList.remove((Object)l2);
            return;
        }
    }

    private String generateHash(String string, String string2) {
        if (TextUtils.isEmpty((CharSequence)string) || TextUtils.isEmpty((CharSequence)string2)) {
            c.e(TDS_TAG_ERROR, "generateHash: Invalid params passed");
            return null;
        }
        try {
            String string3 = string + string2;
            c.d(TAG, "generateHash: Combined code : " + string3);
            String string4 = CryptoUtils.convertbyteToHexString(CryptoUtils.getShaDigest(string3, CryptoUtils.ShaConstants.SHA256));
            c.d(TAG, "generateHash: Hash generated : " + string4);
            return string4;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            c.e(TDS_TAG_ERROR, "generateHash: Exception during hashing " + exception.getMessage());
            return null;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static RegistrationState getCurrentRegistrationState(long l2, boolean bl) {
        Class<McTdsManager> class_ = McTdsManager.class;
        synchronized (McTdsManager.class) {
            if (!bl) {
                if (sRegistrationStatusMap == null) return null;
                return (RegistrationState)((Object)sRegistrationStatusMap.get((Object)l2));
            }
            try {
                RegistrationState registrationState = McTdsManager.getTdsState((McTdsMetaData)new McTdsMetaDataDaoImpl(McProvider.getContext()).getData(l2));
                return registrationState;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                c.e(TDS_TAG_ERROR, "checkForRegistrationReTry: Err : " + exception.getMessage());
                return null;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static McTdsManager getInstance(long l2) {
        Class<McTdsManager> class_ = McTdsManager.class;
        synchronized (McTdsManager.class) {
            McTdsManager mcTdsManager;
            if (mInstanceMap.containsKey((Object)l2)) {
                mcTdsManager = (McTdsManager)mInstanceMap.get((Object)l2);
            } else {
                mcTdsManager = new McTdsManager(l2);
                mInstanceMap.put((Object)l2, (Object)mcTdsManager);
            }
            c.i(TDS_TAG_INFO, "tokenId: " + l2 + " Ref: " + mcTdsManager.mTokenRefSuffix);
            // ** MonitorExit[var5_1] (shouldn't be in output)
            return mcTdsManager;
        }
    }

    private String getRegCode1() {
        McTdsManager mcTdsManager = this;
        synchronized (mcTdsManager) {
            String string = this.mRegCode1;
            return string;
        }
    }

    private String getRegCode2() {
        McTdsManager mcTdsManager = this;
        synchronized (mcTdsManager) {
            String string = this.mRegCode2;
            return string;
        }
    }

    public static List<Long> getRegList() {
        Class<McTdsManager> class_ = McTdsManager.class;
        synchronized (McTdsManager.class) {
            List<Long> list = sRegistrationPendingList;
            // ** MonitorExit[var2] (shouldn't be in output)
            return list;
        }
    }

    private static RegistrationState getTdsState(McTdsMetaData mcTdsMetaData) {
        if (mcTdsMetaData == null || TextUtils.isEmpty((CharSequence)mcTdsMetaData.getTdsRegisterUrl())) {
            return RegistrationState.NO_TDS;
        }
        if (TextUtils.isEmpty((CharSequence)mcTdsMetaData.getAuthCode())) {
            return RegistrationState.TDS_NOT_REGISTERED;
        }
        return RegistrationState.TDS_REGISTERED;
    }

    private String getTokenSuffix() {
        String string = ((McCardMaster)new McCardMasterDaoImpl(McProvider.getContext()).getData(this.mTokenId.get())).getTokenUniqueReference();
        if (TextUtils.isEmpty((CharSequence)string)) {
            c.e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + " register: Err...TokenUniqueReference missing in DB. Cannot complete TDS registration");
            return DEFAULT_TOKEN_REF;
        }
        return string.substring(Math.max((int)(-8 + string.length()), (int)0));
    }

    private int getTransactions(i i2, String string) {
        if (this.mTokenId == null) {
            c.e(TDS_TAG_ERROR, "getTransactions: Empty tokenId..Cant register tds");
            return -2;
        }
        try {
            String string2 = ((McCardMaster)new McCardMasterDaoImpl(McProvider.getContext()).getData(this.mTokenId.get())).getTokenUniqueReference();
            if (TextUtils.isEmpty((CharSequence)string2)) {
                c.e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + " getTransactions: Err...TokenUniqueReference missing in DB. Cannot complete TDS fetch");
                return -2;
            }
            McTdsMetaData mcTdsMetaData = (McTdsMetaData)new McTdsMetaDataDaoImpl(McProvider.getContext()).getData(this.mTokenId.get());
            if (TextUtils.isEmpty((CharSequence)mcTdsMetaData.getAuthCode())) {
                c.e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + " getTransactions: Auth Code missing in db. Cannot fetch transactions");
                return -2;
            }
            c.d(TAG, "tokenId: " + this.mTokenId.get() + " getTransactions: Using tdsUrl: " + string);
            McTdsRequestBuilder.TdsRequest tdsRequest = McTdsRequestBuilder.build(McTdsRequestBuilder.RequestType.GET_TRANSACTIONS, mcTdsMetaData, string2, i2, string);
            return 0;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            c.e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + " getTransactions: Err retriving TDS data from DB. Cannot register for TDS: " + exception.getMessage());
            return -2;
        }
        finally {
            if (false) {
                if (!McTdsRequestor.sendRequest(null)) {
                    c.e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get());
                    return -2;
                }
                c.i(TAG, "tokenId: " + this.mTokenId.get() + " getTransactions: making fetchTransactionRequest");
            }
        }
    }

    private int onRegistrationCode2(String string) {
        if (this.mTokenId == null) {
            c.e(TDS_TAG_ERROR, "onRegistrationCode2: Empty tokenId..Cant process onRegistrationCode2");
            return -2;
        }
        if (TextUtils.isEmpty((CharSequence)string)) {
            c.e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + " onAuthorizationCode2: Err...Input validation failed");
            return -4;
        }
        c.i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " onRegistrationCode2: " + System.currentTimeMillis());
        this.setRegCode2(string);
        this.register();
        return 0;
    }

    private int revokeCheck(String string) {
        if (!TextUtils.isEmpty((CharSequence)string)) {
            return 0;
        }
        if (this.mTokenId == null) {
            c.e(TDS_TAG_ERROR, "revokeCheck: tokenId cannot be null");
            return -2;
        }
        RegistrationState registrationState = McTdsManager.getCurrentRegistrationState(this.mTokenId.get(), true);
        if (registrationState == null) {
            c.e(TDS_TAG_ERROR, "revokeCheck: Failed to get current state from DB");
            return -2;
        }
        switch (1.$SwitchMap$com$samsung$android$spayfw$payprovider$mastercard$tds$McTdsManager$RegistrationState[registrationState.ordinal()]) {
            default: {
                c.e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + " revokeRevokeCheck: invalid tdsState: " + registrationState.name());
                return 0;
            }
            case 1: 
            case 2: {
                c.e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + " revokeRevokeCheck: No tds support. Nothing to do here" + registrationState.name());
                return 0;
            }
            case 3: {
                c.i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " revokeRevokeCheck: revoking tdsState: " + registrationState.name());
                this.unRegister();
                return 0;
            }
            case 4: 
        }
        c.i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " revokeRevokeCheck: tds not registered before. so delete entry");
        new McTdsMetaDataDaoImpl(McTokenManager.getContext()).deleteData(this.mTokenId.get());
        return 0;
    }

    private void setRegCode1(String string) {
        McTdsManager mcTdsManager = this;
        synchronized (mcTdsManager) {
            this.mRegCode1 = string;
            return;
        }
    }

    private void setRegCode2(String string) {
        McTdsManager mcTdsManager = this;
        synchronized (mcTdsManager) {
            this.mRegCode2 = string;
            return;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static void setRegistrationState(long l2, RegistrationState registrationState) {
        Class<McTdsManager> class_ = McTdsManager.class;
        synchronized (McTdsManager.class) {
            block7 : {
                if (registrationState != null) {
                    Map<Long, RegistrationState> map = sRegistrationStatusMap;
                    if (map != null) break block7;
                }
                do {
                    return;
                    break;
                } while (true);
            }
            try {
                sRegistrationStatusMap.put((Object)l2, (Object)registrationState);
                return;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
            finally {
                // ** MonitorExit[var6_2] (shouldn't be in output)
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private int storeOrUpdateTdsMetaData(String string, String string2, boolean bl) {
        int n2 = -2;
        if (this.mTokenId == null || this.mTokenId.get() < 1L || TextUtils.isEmpty((CharSequence)string)) {
            c.d(TAG, "Invalid TDS data received in token call");
            if (this.mTokenId != null) {
                c.d(TAG, "Invalid TDS data received in token call: cardId : " + this.mTokenId.get());
            }
            c.d(TAG, "Invalid TDS data received in token call: tdsRegisterUrl : " + string);
            return n2;
        } else {
            RegistrationState registrationState;
            McTdsMetaDataDaoImpl mcTdsMetaDataDaoImpl = new McTdsMetaDataDaoImpl(McTokenManager.getContext());
            McTdsMetaData mcTdsMetaData = (McTdsMetaData)mcTdsMetaDataDaoImpl.getData(this.mTokenId.get());
            if (mcTdsMetaData == null) {
                if (TextUtils.isEmpty((CharSequence)string2)) {
                    c.e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + " Invalid TDS data received in token call: paymentAppInstanceId : " + string2);
                    return n2;
                }
                McTdsMetaData mcTdsMetaData2 = new McTdsMetaData();
                mcTdsMetaData2.setCardMasterId(this.mTokenId.get());
                mcTdsMetaData2.setTdsRegisterUrl(string);
                mcTdsMetaData2.setPaymentAppInstanceId(string2);
                int n3 = mcTdsMetaDataDaoImpl.saveData(mcTdsMetaData2) != -1L ? 0 : n2;
                RegistrationState registrationState2 = RegistrationState.TDS_NOT_REGISTERED;
                n2 = n3;
                registrationState = registrationState2;
            } else {
                registrationState = TextUtils.isEmpty((CharSequence)mcTdsMetaData.getAuthCode()) ? RegistrationState.TDS_NOT_REGISTERED : RegistrationState.TDS_REGISTERED;
                boolean bl2 = mcTdsMetaDataDaoImpl.updateUrl(string, this.mTokenId.get(), true);
                int n4 = 0;
                if (!bl2) {
                    n4 = n2;
                }
                n2 = n4;
            }
            if (!bl) return n2;
            {
                McTdsManager.setRegistrationState(this.mTokenId.get(), registrationState);
                return n2;
            }
        }
    }

    public String getResponseHostTds() {
        McTdsManager mcTdsManager = this;
        synchronized (mcTdsManager) {
            String string = this.mResponseHostTds;
            return string;
        }
    }

    public String getResponseHostTdsReg() {
        McTdsManager mcTdsManager = this;
        synchronized (mcTdsManager) {
            String string = this.mResponseHostTdsReg;
            return string;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public int getTransactionData(Bundle bundle, i i2) {
        String string;
        if (this.mTokenId == null) {
            c.e(TDS_TAG_ERROR, "getTransactionData: Empty tokenId..Cant fetch tds");
            return -9;
        }
        c.i(TDS_TAG_INFO, "getTransactionData: entryTime:" + System.currentTimeMillis());
        if (bundle != null && bundle.containsKey("pushtransactionUrl") && !TextUtils.isEmpty((CharSequence)bundle.getString("pushtransactionUrl"))) {
            string = bundle.getString("pushtransactionUrl");
            c.i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " getTransactionData: new tds URL obtained : ");
            c.d(TAG, "url: " + string);
        } else {
            string = this.getResponseHostTds();
            if (!TextUtils.isEmpty((CharSequence)string)) {
                this.setResponseHostTds(null);
                c.i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " getTransactionData: responseHost from before ignored: ");
                c.d(TAG, "getResponseHostTds: " + this.getResponseHostTds());
            }
            c.i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " getTransactionData: using responseHost : ");
            c.d(TAG, "url: " + string);
        }
        if (bundle != null && bundle.containsKey("authorizationCode") && !TextUtils.isEmpty((CharSequence)bundle.getString("authorizationCode"))) {
            String string2 = bundle.getString("authorizationCode");
            c.i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " getTransactionData: RegistrationCode2 received: ");
            c.d(TDS_TAG_INFO, "RegistrationCode2: " + string2);
            return this.onRegistrationCode2(string2);
        }
        if (i2 == null) {
            c.e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + " getTransactionData: callBack missing");
            return -4;
        }
        return this.getTransactions(i2, string);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void onRegisterCode1(String string, String string2) {
        if (this.mTokenId == null) {
            c.e(TDS_TAG_ERROR, "onRegisterCode1: Empty tokenId..Cant process onRegisterCode1");
            return;
        }
        try {
            if (TextUtils.isEmpty((CharSequence)string2)) {
                c.e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + " onRegisterCode1: Invalid RegistrationCode1 received");
                return;
            }
            if (!TextUtils.isEmpty((CharSequence)string)) {
                this.setResponseHostTdsReg(string);
            }
            this.setRegCode1(string2);
            this.register();
            return;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            c.e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + " onRegisterCode1: Exception occured: " + exception.getMessage());
            return;
        }
    }

    public int onTokenStateUpdate(String string, String string2) {
        int n2 = this.revokeCheck(string);
        if (n2 != 0) {
            return n2;
        }
        int n3 = this.storeOrUpdateTdsMetaData(string, string2, true);
        if (n3 != 0 || this.mTokenId == null) {
            c.e(TDS_TAG_ERROR, "onTokenStateUpdate: dbOperation failed: ");
            return n3;
        }
        RegistrationState registrationState = McTdsManager.getCurrentRegistrationState(this.mTokenId.get(), false);
        if (registrationState == null) {
            return -2;
        }
        switch (1.$SwitchMap$com$samsung$android$spayfw$payprovider$mastercard$tds$McTdsManager$RegistrationState[registrationState.ordinal()]) {
            default: {
                c.e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + " onTokenStateUpdate: invalid tdsState: " + registrationState.name());
                return n3;
            }
            case 1: 
            case 2: 
            case 3: {
                c.i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " onTokenStateUpdate: tdsState: " + registrationState.name());
                return n3;
            }
            case 4: 
        }
        c.i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " onTokenStateUpdate: Triggering tds flow first time");
        this.reRegisterIfNeeded(null);
        return n3;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void reRegisterIfNeeded(String string) {
        if (this.mTokenId == null) {
            c.e(TDS_TAG_ERROR, "reRegisterIfNeeded: Empty tokenId..Cant process reRegisterIfNeeded");
            return;
        } else {
            boolean bl = TextUtils.isEmpty((CharSequence)string);
            boolean bl2 = false;
            if (!bl) {
                McTdsError mcTdsError = McTdsError.getTdsError(string);
                bl2 = false;
                if (mcTdsError != null) {
                    boolean bl3 = mcTdsError.getErrorCode().equalsIgnoreCase(McTdsError.INVALID_AUTHENTICATION_CODE.getErrorCode());
                    bl2 = false;
                    if (bl3) {
                        c.i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " reRegisterIfNeeded: Auth expired");
                        bl2 = true;
                    }
                }
            }
            if (!bl2 && !this.registerReTry()) return;
            {
                this.cleanupRegCodes();
                c.i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " reRegisterIfNeeded: triggering re-register flow :");
                this.register();
                return;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void register() {
        McTdsManager mcTdsManager = this;
        synchronized (mcTdsManager) {
            block33 : {
                if (this.mTokenId == null) {
                    c.e(TDS_TAG_ERROR, "register: Empty tokenId..Cant register tds");
                } else {
                    try {
                        McTdsMetaData mcTdsMetaData = (McTdsMetaData)new McTdsMetaDataDaoImpl(McProvider.getContext()).getData(this.mTokenId.get());
                        String string = ((McCardMaster)new McCardMasterDaoImpl(McProvider.getContext()).getData(this.mTokenId.get())).getTokenUniqueReference();
                        if (TextUtils.isEmpty((CharSequence)string)) {
                            c.e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + " register: Err...TokenUniqueReference missing in DB. Cannot complete TDS registration");
                        } else if (TextUtils.isEmpty((CharSequence)this.getRegCode1()) && TextUtils.isEmpty((CharSequence)this.getRegCode2())) {
                            c.i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " register: Triggering getRegistrationCode");
                            McTdsRequestBuilder.TdsRequest tdsRequest = McTdsRequestBuilder.build(McTdsRequestBuilder.RequestType.REGISTRATION_CODE, mcTdsMetaData, string, null, null);
                        } else if (TextUtils.isEmpty((CharSequence)this.getRegCode1())) {
                            c.i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " register: Missing regCode1");
                        } else if (TextUtils.isEmpty((CharSequence)this.getRegCode2())) {
                            c.i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " register: Missing regCode2..");
                        } else {
                            String string2 = this.generateHash(this.getRegCode1(), this.getRegCode2());
                            if (TextUtils.isEmpty((CharSequence)string2)) {
                                c.e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + "register: Err...Could not generate Hash! Cannot complete TDS registration");
                            } else {
                                mcTdsMetaData.setHash(string2);
                                this.cleanupRegCodes();
                                McTdsRequestBuilder.TdsRequest tdsRequest = McTdsRequestBuilder.build(McTdsRequestBuilder.RequestType.REGISTER, mcTdsMetaData, string, null, this.getResponseHostTdsReg());
                            }
                        }
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                        c.e(TDS_TAG_ERROR, "register: Err retriving TDS data from DB. Cannot register for TDS: " + exception.getMessage());
                    }
                    finally {
                        if (!false) break block33;
                        if (!McTdsRequestor.sendRequest(null)) {
                            c.e(TDS_TAG_ERROR, "registration Err tokenId: " + this.mTokenId.get());
                            break block33;
                        }
                        c.i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " register: making registration request");
                    }
                }
            }
            return;
        }
    }

    public boolean registerReTry() {
        try {
            RegistrationState registrationState = McTdsManager.getCurrentRegistrationState(this.mTokenId.get(), false);
            c.d(TAG, "registerReTry:" + registrationState.name());
            RegistrationState registrationState2 = RegistrationState.TDS_NOT_REGISTERED;
            boolean bl = false;
            if (registrationState == registrationState2) {
                bl = true;
            }
            return bl;
        }
        catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            c.e(TDS_TAG_ERROR, "registerReTry: NPE" + nullPointerException.getMessage());
            return false;
        }
    }

    public void setResponseHostTds(String string) {
        McTdsManager mcTdsManager = this;
        synchronized (mcTdsManager) {
            this.mResponseHostTds = string;
            return;
        }
    }

    public void setResponseHostTdsReg(String string) {
        McTdsManager mcTdsManager = this;
        synchronized (mcTdsManager) {
            this.mResponseHostTdsReg = string;
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void unRegister() {
        String string;
        McTdsMetaDataDaoImpl mcTdsMetaDataDaoImpl;
        McTdsMetaData mcTdsMetaData;
        McTdsRequestBuilder.TdsRequest tdsRequest;
        block38 : {
            block37 : {
                mcTdsMetaDataDaoImpl = new McTdsMetaDataDaoImpl(McProvider.getContext());
                if (mcTdsMetaDataDaoImpl == null || this.mTokenId == null) {
                    c.e(TDS_TAG_ERROR, "unRegister: Empty tokenId..Cant unRegister tds : " + mcTdsMetaDataDaoImpl);
                    return;
                }
                string = ((McCardMaster)new McCardMasterDaoImpl(McProvider.getContext()).getData(this.mTokenId.get())).getTokenUniqueReference();
                if (!TextUtils.isEmpty((CharSequence)string)) break block37;
                c.e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + "unRegister: Err...TokenUniqueReference missing in DB. Cannot complete TDS unRegistration");
                mcTdsMetaDataDaoImpl.deleteData(this.mTokenId.get());
                if (mInstanceMap != null) {
                    mInstanceMap.remove((Object)this.mTokenId.get());
                }
                Class<McTdsManager> class_ = McTdsManager.class;
                synchronized (McTdsManager.class) {
                    if (sRegistrationStatusMap != null) {
                        sRegistrationStatusMap.remove((Object)this.mTokenId.get());
                    }
                    // ** MonitorExit[var27_3] (shouldn't be in output)
                    if (!false) return;
                    if (!McTdsRequestor.sendRequest(null)) {
                        c.e(TDS_TAG_ERROR, "unregistration Err tokenId: " + this.mTokenId.get());
                        return;
                    }
                    c.i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " unRegister: making Unregistration request");
                    return;
                }
            }
            mcTdsMetaData = (McTdsMetaData)mcTdsMetaDataDaoImpl.getData(this.mTokenId.get());
            if (mcTdsMetaData != null && !TextUtils.isEmpty((CharSequence)string) && !TextUtils.isEmpty((CharSequence)mcTdsMetaData.getAuthCode())) break block38;
            c.i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + "unRegister: TDS not registered..Nothing to do");
            mcTdsMetaDataDaoImpl.deleteData(this.mTokenId.get());
            if (mInstanceMap != null) {
                mInstanceMap.remove((Object)this.mTokenId.get());
            }
            Class<McTdsManager> class_ = McTdsManager.class;
            synchronized (McTdsManager.class) {
                if (sRegistrationStatusMap != null) {
                    sRegistrationStatusMap.remove((Object)this.mTokenId.get());
                }
                // ** MonitorExit[var27_4] (shouldn't be in output)
                if (!false) return;
                if (!McTdsRequestor.sendRequest(null)) {
                    c.e(TDS_TAG_ERROR, "unregistration Err tokenId: " + this.mTokenId.get());
                    return;
                }
                c.i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " unRegister: making Unregistration request");
                return;
            }
        }
        try {
            tdsRequest = McTdsRequestBuilder.build(McTdsRequestBuilder.RequestType.UNREGISTER, mcTdsMetaData, string, null, this.getResponseHostTds());
        }
        catch (Exception exception) {
            try {
                exception.printStackTrace();
                c.e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + "unRegister: Err retriving TDS data from DB. Cannot register for TDS: " + exception.getMessage());
            }
            catch (Throwable throwable) {
                mcTdsMetaDataDaoImpl.deleteData(this.mTokenId.get());
                if (mInstanceMap != null) {
                    mInstanceMap.remove((Object)this.mTokenId.get());
                }
                Class<McTdsManager> class_ = McTdsManager.class;
                synchronized (McTdsManager.class) {
                    if (sRegistrationStatusMap != null) {
                        sRegistrationStatusMap.remove((Object)this.mTokenId.get());
                    }
                    // ** MonitorExit[var27_7] (shouldn't be in output)
                    if (!false) throw throwable;
                    if (!McTdsRequestor.sendRequest(null)) {
                        c.e(TDS_TAG_ERROR, "unregistration Err tokenId: " + this.mTokenId.get());
                        throw throwable;
                    }
                    c.i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " unRegister: making Unregistration request");
                    throw throwable;
                }
            }
            mcTdsMetaDataDaoImpl.deleteData(this.mTokenId.get());
            if (mInstanceMap != null) {
                mInstanceMap.remove((Object)this.mTokenId.get());
            }
            Class<McTdsManager> class_ = McTdsManager.class;
            synchronized (McTdsManager.class) {
                if (sRegistrationStatusMap != null) {
                    sRegistrationStatusMap.remove((Object)this.mTokenId.get());
                }
                // ** MonitorExit[var27_6] (shouldn't be in output)
                if (!false) return;
                if (!McTdsRequestor.sendRequest(null)) {
                    c.e(TDS_TAG_ERROR, "unregistration Err tokenId: " + this.mTokenId.get());
                    return;
                }
                c.i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " unRegister: making Unregistration request");
                return;
            }
        }
        mcTdsMetaDataDaoImpl.deleteData(this.mTokenId.get());
        if (mInstanceMap != null) {
            mInstanceMap.remove((Object)this.mTokenId.get());
        }
        Class<McTdsManager> class_ = McTdsManager.class;
        synchronized (McTdsManager.class) {
            if (sRegistrationStatusMap != null) {
                sRegistrationStatusMap.remove((Object)this.mTokenId.get());
            }
            // ** MonitorExit[var27_5] (shouldn't be in output)
            if (tdsRequest == null) return;
            if (!McTdsRequestor.sendRequest(tdsRequest)) {
                c.e(TDS_TAG_ERROR, "unregistration Err tokenId: " + this.mTokenId.get());
                return;
            }
            c.i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " unRegister: making Unregistration request");
            return;
        }
    }

    public static final class RegistrationState
    extends Enum<RegistrationState> {
        private static final /* synthetic */ RegistrationState[] $VALUES;
        public static final /* enum */ RegistrationState DEFAULT = new RegistrationState(1);
        public static final /* enum */ RegistrationState NO_TDS = new RegistrationState(2);
        public static final /* enum */ RegistrationState TDS_NOT_REGISTERED = new RegistrationState(3);
        public static final /* enum */ RegistrationState TDS_REGISTERED = new RegistrationState(4);
        private final int mValue;

        static {
            RegistrationState[] arrregistrationState = new RegistrationState[]{DEFAULT, NO_TDS, TDS_NOT_REGISTERED, TDS_REGISTERED};
            $VALUES = arrregistrationState;
        }

        private RegistrationState(int n3) {
            this.mValue = n3;
        }

        public static RegistrationState valueOf(String string) {
            return (RegistrationState)Enum.valueOf(RegistrationState.class, (String)string);
        }

        public static RegistrationState[] values() {
            return (RegistrationState[])$VALUES.clone();
        }

        public int getValue() {
            return this.mValue;
        }
    }

}

