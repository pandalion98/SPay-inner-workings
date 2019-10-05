/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.text.TextUtils
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  java.io.UnsupportedEncodingException
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Integer
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.Set
 */
package com.samsung.android.spayfw.payprovider.discover.db;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.discover.db.dao.DcCommonDao;
import com.samsung.android.spayfw.payprovider.discover.db.dao.a;
import com.samsung.android.spayfw.payprovider.discover.db.dao.c;
import com.samsung.android.spayfw.payprovider.discover.db.dao.e;
import com.samsung.android.spayfw.payprovider.discover.db.models.CardDetails;
import com.samsung.android.spayfw.payprovider.discover.db.models.DcCardMaster;
import com.samsung.android.spayfw.payprovider.discover.db.models.DcPaymentProfile;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverContactlessPaymentData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverInnAppPaymentData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentCard;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentProfile;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DcStorageManager {
    private static Gson mGson = new GsonBuilder().create();

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static ResultCode a(long l2, int n2, byte[] arrby) {
        a a2 = new a(com.samsung.android.spayfw.payprovider.discover.a.cC());
        long l3 = n2;
        try {
            CardDetails cardDetails = a2.b(l2, l3);
            if (cardDetails == null) {
                Log.i("DCSDK_DcStorageManager", "No existing record");
                a2.saveData(new CardDetails(l2, n2, arrby));
                return ResultCode.sw;
            }
            Log.i("DCSDK_DcStorageManager", "Found existing record.");
            cardDetails.setData(arrby);
            a2.a(cardDetails, l2);
            return ResultCode.sw;
        }
        catch (DcDbException dcDbException) {
            Log.e("DCSDK_DcStorageManager", dcDbException.getMessage());
            dcDbException.printStackTrace();
            if (dcDbException.getErrorCode() != 7) return ResultCode.sA;
            return ResultCode.sB;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static ResultCode a(long l2, long l3) {
        Class<DcStorageManager> class_ = DcStorageManager.class;
        synchronized (DcStorageManager.class) {
            block7 : {
                DcCardMaster dcCardMaster = DcStorageManager.l(l2);
                if (dcCardMaster == null) break block7;
                dcCardMaster.setRemainingOtpkCount(l3);
                ResultCode resultCode = DcStorageManager.a(l2, dcCardMaster);
                return resultCode;
            }
            ResultCode resultCode = ResultCode.sE;
            return resultCode;
        }
    }

    public static ResultCode a(long l2, DcCardMaster dcCardMaster) {
        c c2 = new c(com.samsung.android.spayfw.payprovider.discover.a.cC());
        ResultCode resultCode = ResultCode.sw;
        try {
            c2.updateData(dcCardMaster, l2);
            return resultCode;
        }
        catch (DcDbException dcDbException) {
            dcDbException.printStackTrace();
            return ResultCode.sA;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static ResultCode a(long l2, DiscoverContactlessPaymentData discoverContactlessPaymentData) {
        Class<DcStorageManager> class_ = DcStorageManager.class;
        synchronized (DcStorageManager.class) {
            ResultCode resultCode;
            block8 : {
                Log.d("DCSDK_DcStorageManager", "saveContactlessPaymentData");
                try {
                    Set set = discoverContactlessPaymentData.getPaymentProfiles().entrySet();
                    resultCode = ResultCode.sw;
                    Iterator iterator = set.iterator();
                    while (iterator.hasNext() && (resultCode = DcStorageManager.a(l2, (DiscoverPaymentProfile)((Map.Entry)iterator.next()).getValue())) == ResultCode.sw) {
                    }
                    if (resultCode != ResultCode.sw) {
                        Log.d("DCSDK_DcStorageManager", "saveContactlessPaymentData: Failed to save Payment Profiles");
                        break block8;
                    }
                    discoverContactlessPaymentData.setPaymentProfiles(null);
                    String string = mGson.toJson((Object)discoverContactlessPaymentData);
                    if (TextUtils.isEmpty((CharSequence)string)) {
                        Log.e("DCSDK_DcStorageManager", "saveContactlessPaymentData: jsonData failed");
                        return ResultCode.sy;
                    }
                    byte[] arrby = string.getBytes("UTF8");
                    ResultCode resultCode2 = DcStorageManager.a(l2, DcCommonDao.DetailDataId.sK.cJ(), arrby);
                    return resultCode2;
                }
                catch (UnsupportedEncodingException unsupportedEncodingException) {
                    unsupportedEncodingException.printStackTrace();
                    return ResultCode.sz;
                }
                catch (NullPointerException nullPointerException) {
                    nullPointerException.printStackTrace();
                    return ResultCode.sz;
                }
            }
            // ** MonitorExit[class_] (shouldn't be in output)
            return resultCode;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static ResultCode a(long l2, DiscoverInnAppPaymentData discoverInnAppPaymentData) {
        Class<DcStorageManager> class_ = DcStorageManager.class;
        synchronized (DcStorageManager.class) {
            Log.d("DCSDK_DcStorageManager", "saveInAppPaymentData");
            try {
                String string = mGson.toJson((Object)discoverInnAppPaymentData);
                if (TextUtils.isEmpty((CharSequence)string)) {
                    Log.e("DCSDK_DcStorageManager", "saveInAppPaymentData: jsonData failed");
                    return ResultCode.sy;
                }
                byte[] arrby = string.getBytes("UTF8");
                ResultCode resultCode = DcStorageManager.a(l2, DcCommonDao.DetailDataId.sL.cJ(), arrby);
                return resultCode;
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                unsupportedEncodingException.printStackTrace();
                return ResultCode.sz;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static ResultCode a(long l2, DiscoverPaymentProfile discoverPaymentProfile) {
        Class<DcStorageManager> class_ = DcStorageManager.class;
        synchronized (DcStorageManager.class) {
            e e2 = new e(com.samsung.android.spayfw.payprovider.discover.a.cC());
            Log.d("DCSDK_DcStorageManager", "saveOrUpdatePaymentProfile");
            try {
                DcPaymentProfile dcPaymentProfile = e2.c(l2, discoverPaymentProfile.getProfileId());
                if (dcPaymentProfile == null) {
                    Log.i("DCSDK_DcStorageManager", "No existing record");
                    DcPaymentProfile dcPaymentProfile2 = new DcPaymentProfile();
                    dcPaymentProfile2.init(l2, discoverPaymentProfile);
                    e2.saveData(dcPaymentProfile2);
                    return ResultCode.sw;
                }
                Log.i("DCSDK_DcStorageManager", "Found existing record.");
                dcPaymentProfile.init(l2, discoverPaymentProfile);
                if (dcPaymentProfile.getRowId() != -1L) {
                    e2.updateData(dcPaymentProfile, dcPaymentProfile.getRowId());
                    return ResultCode.sw;
                }
                Log.e("DCSDK_DcStorageManager", "Invalid Row Id for Payment Profile Record");
                return ResultCode.sB;
            }
            catch (DcDbException dcDbException) {
                Log.e("DCSDK_DcStorageManager", dcDbException.getMessage());
                if (dcDbException.getErrorCode() != 7) return ResultCode.sA;
                return ResultCode.sB;
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static ResultCode a(long l2, String string) {
        Class<DcStorageManager> class_ = DcStorageManager.class;
        synchronized (DcStorageManager.class) {
            block7 : {
                DcCardMaster dcCardMaster = DcStorageManager.l(l2);
                if (dcCardMaster == null) break block7;
                dcCardMaster.setStatus(string);
                ResultCode resultCode = DcStorageManager.a(l2, dcCardMaster);
                return resultCode;
            }
            ResultCode resultCode = ResultCode.sE;
            return resultCode;
        }
    }

    public static ResultCode a(long l2, byte[] arrby) {
        Class<DcStorageManager> class_ = DcStorageManager.class;
        synchronized (DcStorageManager.class) {
            ResultCode resultCode = DcStorageManager.a(l2, DcCommonDao.DetailDataId.sM.cJ(), arrby);
            // ** MonitorExit[var5_2] (shouldn't be in output)
            return resultCode;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static ResultCode a(DiscoverPaymentCard discoverPaymentCard) {
        Class<DcStorageManager> class_ = DcStorageManager.class;
        synchronized (DcStorageManager.class) {
            long l2 = discoverPaymentCard.getTokenId();
            ResultCode resultCode = DcStorageManager.a(l2, discoverPaymentCard.getDiscoverContactlessPaymentData());
            if (resultCode != ResultCode.sw) {
                Log.e("DCSDK_DcStorageManager", "saveDiscoverPaymentCard: Failed to save ContactlessPaymentData. Error: " + resultCode.getErrorMessage());
            } else {
                resultCode = DcStorageManager.a(l2, discoverPaymentCard.getDiscoverInnAppPaymentData());
                if (resultCode != ResultCode.sw) {
                    Log.e("DCSDK_DcStorageManager", "saveDiscoverPaymentCard: Failed to save InnAppPaymentData. Error: " + resultCode.getErrorMessage());
                } else {
                    resultCode = DcStorageManager.c(l2, discoverPaymentCard.getOTPK());
                    if (resultCode != ResultCode.sw) {
                        Log.e("DCSDK_DcStorageManager", "saveDiscoverPaymentCard: Failed to save OTPK Data. Error: " + resultCode.getErrorMessage());
                    } else {
                        resultCode = DcStorageManager.a(l2, discoverPaymentCard.getSecureObject());
                        if (resultCode != ResultCode.sw) {
                            Log.e("DCSDK_DcStorageManager", "saveDiscoverPaymentCard: Failed to save SecureObject. Error: " + resultCode.getErrorMessage());
                        } else {
                            resultCode = DcStorageManager.b(l2, discoverPaymentCard.getDiscoverContactlessPaymentData().getPth().getBytes());
                            if (resultCode != ResultCode.sw) {
                                Log.e("DCSDK_DcStorageManager", "saveDiscoverPaymentCard: Failed to save PTH Data. Error: " + resultCode.getErrorMessage());
                            }
                        }
                    }
                }
            }
            // ** MonitorExit[var6_1] (shouldn't be in output)
            return resultCode;
        }
    }

    private static CardDetails a(long l2, int n2) {
        a a2 = new a(com.samsung.android.spayfw.payprovider.discover.a.cC());
        Log.d("DCSDK_DcStorageManager", "loadCardDetailsRecord: tokenId - " + l2 + ", detailDataId - " + n2);
        long l3 = n2;
        try {
            CardDetails cardDetails = a2.b(l2, l3);
            return cardDetails;
        }
        catch (DcDbException dcDbException) {
            Log.e("DCSDK_DcStorageManager", dcDbException.getMessage());
            dcDbException.printStackTrace();
            return null;
        }
    }

    public static ResultCode b(long l2, byte[] arrby) {
        Class<DcStorageManager> class_ = DcStorageManager.class;
        synchronized (DcStorageManager.class) {
            ResultCode resultCode = DcStorageManager.a(l2, DcCommonDao.DetailDataId.sO.cJ(), arrby);
            // ** MonitorExit[var5_2] (shouldn't be in output)
            return resultCode;
        }
    }

    public static ResultCode c(long l2, byte[] arrby) {
        Class<DcStorageManager> class_ = DcStorageManager.class;
        synchronized (DcStorageManager.class) {
            ResultCode resultCode = DcStorageManager.a(l2, DcCommonDao.DetailDataId.sN.cJ(), arrby);
            // ** MonitorExit[var5_2] (shouldn't be in output)
            return resultCode;
        }
    }

    public static void cH() {
        c c2 = new c(com.samsung.android.spayfw.payprovider.discover.a.cC());
        try {
            c2.cI();
            return;
        }
        catch (DcDbException dcDbException) {
            dcDbException.printStackTrace();
            return;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static DiscoverContactlessPaymentData e(long l2) {
        Class<DcStorageManager> class_ = DcStorageManager.class;
        synchronized (DcStorageManager.class) {
            DiscoverContactlessPaymentData discoverContactlessPaymentData;
            CardDetails cardDetails;
            block10 : {
                Log.d("DCSDK_DcStorageManager", "getContactlessPaymentData");
                try {
                    cardDetails = DcStorageManager.a(l2, DcCommonDao.DetailDataId.sK.cJ());
                    if (cardDetails != null) break block10;
                }
                catch (UnsupportedEncodingException unsupportedEncodingException) {
                    unsupportedEncodingException.printStackTrace();
                    discoverContactlessPaymentData = null;
                    return discoverContactlessPaymentData;
                }
                Log.e("DCSDK_DcStorageManager", "getContactlessPaymentData, loadCardDetailsRecord returns null, tokenId " + l2);
                discoverContactlessPaymentData = null;
                do {
                    return discoverContactlessPaymentData;
                    break;
                } while (true);
            }
            String string = new String(cardDetails.getData(), "UTF8");
            discoverContactlessPaymentData = (DiscoverContactlessPaymentData)mGson.fromJson(string, DiscoverContactlessPaymentData.class);
            discoverContactlessPaymentData.setPaymentProfiles(DcStorageManager.m(l2));
            return discoverContactlessPaymentData;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static DiscoverInnAppPaymentData f(long l2) {
        Class<DcStorageManager> class_ = DcStorageManager.class;
        synchronized (DcStorageManager.class) {
            Log.d("DCSDK_DcStorageManager", "getInAppPaymentData");
            try {
                CardDetails cardDetails = DcStorageManager.a(l2, DcCommonDao.DetailDataId.sL.cJ());
                if (cardDetails == null) {
                    Log.e("DCSDK_DcStorageManager", "getInAppPaymentData, loadCardDetailsRecord returns null, tokenId " + l2);
                    return null;
                }
                String string = new String(cardDetails.getData(), "UTF8");
                return (DiscoverInnAppPaymentData)mGson.fromJson(string, DiscoverInnAppPaymentData.class);
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                unsupportedEncodingException.printStackTrace();
                return null;
            }
            catch (NullPointerException nullPointerException) {
                nullPointerException.printStackTrace();
            }
            return null;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static byte[] g(long l2) {
        byte[] arrby = null;
        Class<DcStorageManager> class_ = DcStorageManager.class;
        synchronized (DcStorageManager.class) {
            Log.d("DCSDK_DcStorageManager", "getPaymentSecureObj");
            try {
                CardDetails cardDetails = DcStorageManager.a(l2, DcCommonDao.DetailDataId.sM.cJ());
                byte[] arrby2 = cardDetails.getData();
                if (cardDetails != null) return arrby2;
                Log.e("DCSDK_DcStorageManager", "getPaymentSecureObj, loadCardDetailsRecord returns null, tokenId " + l2);
            }
            catch (NullPointerException nullPointerException) {
                Log.e("DCSDK_DcStorageManager", nullPointerException.getMessage());
                nullPointerException.printStackTrace();
                return null;
            }
            return arrby;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static byte[] h(long l2) {
        Class<DcStorageManager> class_ = DcStorageManager.class;
        synchronized (DcStorageManager.class) {
            a a2 = new a(com.samsung.android.spayfw.payprovider.discover.a.cC());
            Log.d("DCSDK_DcStorageManager", "getOTPKData");
            try {
                CardDetails cardDetails = a2.b(l2, DcCommonDao.DetailDataId.sN.cJ());
                byte[] arrby = null;
                if (cardDetails == null) return arrby;
                byte[] arrby2 = cardDetails.getData();
                return arrby2;
            }
            catch (DcDbException dcDbException) {
                Log.e("DCSDK_DcStorageManager", dcDbException.getMessage());
                dcDbException.printStackTrace();
                return null;
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static DiscoverPaymentCard i(long l2) {
        Class<DcStorageManager> class_ = DcStorageManager.class;
        synchronized (DcStorageManager.class) {
            CardDetails cardDetails;
            DiscoverPaymentCard discoverPaymentCard;
            block19 : {
                byte[] arrby;
                block18 : {
                    byte[] arrby2;
                    block17 : {
                        DiscoverInnAppPaymentData discoverInnAppPaymentData;
                        block16 : {
                            DiscoverContactlessPaymentData discoverContactlessPaymentData;
                            block15 : {
                                discoverPaymentCard = new DiscoverPaymentCard(l2, null, null, null);
                                discoverContactlessPaymentData = DcStorageManager.e(l2);
                                if (discoverContactlessPaymentData != null) break block15;
                                Log.e("DCSDK_DcStorageManager", "getDiscoverPaymentCard: Failed to get DiscoverContactlessPaymentData.");
                                return null;
                            }
                            discoverPaymentCard.setDiscoverContactlessPaymentData(discoverContactlessPaymentData);
                            discoverInnAppPaymentData = DcStorageManager.f(l2);
                            if (discoverInnAppPaymentData != null) break block16;
                            Log.e("DCSDK_DcStorageManager", "getDiscoverPaymentCard: Failed to get DiscoverInnAppPaymentData.");
                            return null;
                        }
                        discoverPaymentCard.setDiscoverInnAppPaymentData(discoverInnAppPaymentData);
                        arrby2 = DcStorageManager.h(l2);
                        if (arrby2 != null) break block17;
                        Log.e("DCSDK_DcStorageManager", "getDiscoverPaymentCard: Failed to get otpkData.");
                        return null;
                    }
                    discoverPaymentCard.setOTPK(arrby2);
                    arrby = DcStorageManager.g(l2);
                    if (arrby != null) break block18;
                    Log.e("DCSDK_DcStorageManager", "getDiscoverPaymentCard: Failed to get secureObjData.");
                    return null;
                }
                discoverPaymentCard.setSecureObject(arrby);
                cardDetails = DcStorageManager.a(l2, DcCommonDao.DetailDataId.sO.cJ());
                if (cardDetails != null) break block19;
                Log.e("DCSDK_DcStorageManager", "getDiscoverPaymentCard: Failed to get pthData.");
                return null;
            }
            discoverPaymentCard.getDiscoverContactlessPaymentData().setPth(new ByteBuffer(cardDetails.getData()));
            return discoverPaymentCard;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static long j(long l2) {
        Class<DcStorageManager> class_ = DcStorageManager.class;
        synchronized (DcStorageManager.class) {
            DcCardMaster dcCardMaster = DcStorageManager.l(l2);
            if (dcCardMaster == null) return 0L;
            long l3 = dcCardMaster.getRemainingOtpkCount();
            return l3;
        }
    }

    public static ResultCode k(long l2) {
        Class<DcStorageManager> class_ = DcStorageManager.class;
        synchronized (DcStorageManager.class) {
            c c2 = new c(com.samsung.android.spayfw.payprovider.discover.a.cC());
            ResultCode resultCode = ResultCode.sw;
            Log.i("DCSDK_DcStorageManager", "deleteToken");
            try {
                c2.deleteData(l2);
            }
            catch (DcDbException dcDbException) {
                try {
                    Log.e("DCSDK_DcStorageManager", "delete: DB Exception while deleting data");
                    dcDbException.printStackTrace();
                    resultCode = ResultCode.sC;
                }
                catch (Throwable throwable) {
                    throw throwable;
                }
                finally {
                    // ** MonitorExit[var7_1] (shouldn't be in output)
                }
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static DcCardMaster l(long l2) {
        DcCardMaster dcCardMaster;
        c c2 = new c(com.samsung.android.spayfw.payprovider.discover.a.cC());
        Log.d("DCSDK_DcStorageManager", "loadCardMaster: tokenId - " + l2);
        try {
            dcCardMaster = (DcCardMaster)c2.getData(l2);
        }
        catch (DcDbException dcDbException) {
            dcDbException.printStackTrace();
            dcCardMaster = null;
        }
        if (dcCardMaster == null) {
            Log.e("DCSDK_DcStorageManager", "loadCardMaster: Failed to load CardMaster Record");
        }
        return dcCardMaster;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static HashMap<Integer, DiscoverPaymentProfile> m(long l2) {
        Log.d("DCSDK_DcStorageManager", "loadDiscoverPaymentProfiles: tokenId - " + l2);
        e e2 = new e(com.samsung.android.spayfw.payprovider.discover.a.cC());
        HashMap hashMap = new HashMap();
        try {
            List<DcPaymentProfile> list = e2.o(l2);
            if (list != null) {
                for (DcPaymentProfile dcPaymentProfile : list) {
                    hashMap.put((Object)((int)dcPaymentProfile.getProfileId()), (Object)DcPaymentProfile.toDiscoverPaymentProfile(dcPaymentProfile));
                }
                return hashMap;
            } else {
                Log.e("DCSDK_DcStorageManager", "loadDiscoverPaymentProfiles: payment profiles list not found.");
            }
            return hashMap;
        }
        catch (DcDbException dcDbException) {
            Log.e("DCSDK_DcStorageManager", "loadDiscoverPaymentProfiles: Exception - " + dcDbException.getMessage());
            dcDbException.printStackTrace();
            return null;
        }
    }

    public static final class ResultCode
    extends Enum<ResultCode> {
        public static final /* enum */ ResultCode sA;
        public static final /* enum */ ResultCode sB;
        public static final /* enum */ ResultCode sC;
        public static final /* enum */ ResultCode sD;
        public static final /* enum */ ResultCode sE;
        public static final /* enum */ ResultCode sF;
        private static final /* synthetic */ ResultCode[] sG;
        public static final /* enum */ ResultCode sv;
        public static final /* enum */ ResultCode sw;
        public static final /* enum */ ResultCode sx;
        public static final /* enum */ ResultCode sy;
        public static final /* enum */ ResultCode sz;
        private String mMessage;

        static {
            sv = new ResultCode("Min Error Code");
            sw = new ResultCode("Success");
            sx = new ResultCode("Request Failed");
            sy = new ResultCode("JSON Serialization Failed");
            sz = new ResultCode("Exception Occurred");
            sA = new ResultCode("Failed to Save Data");
            sB = new ResultCode("Failed to Update Data");
            sC = new ResultCode("Failed to Delete Data");
            sD = new ResultCode("Invalid Input (Null)");
            sE = new ResultCode("No Data Found in Storage");
            sF = new ResultCode("Max Error Code");
            ResultCode[] arrresultCode = new ResultCode[]{sv, sw, sx, sy, sz, sA, sB, sC, sD, sE, sF};
            sG = arrresultCode;
        }

        private ResultCode(String string2) {
            this.mMessage = string2;
        }

        public static ResultCode valueOf(String string) {
            return (ResultCode)Enum.valueOf(ResultCode.class, (String)string);
        }

        public static ResultCode[] values() {
            return (ResultCode[])sG.clone();
        }

        public String getErrorMessage() {
            return this.mMessage;
        }
    }

}

