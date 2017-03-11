package com.samsung.android.spayfw.payprovider.discover.db;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.discover.DiscoverPayProvider;
import com.samsung.android.spayfw.payprovider.discover.db.dao.CardDetailsDaoImpl;
import com.samsung.android.spayfw.payprovider.discover.db.dao.DcCardMasterDaoImpl;
import com.samsung.android.spayfw.payprovider.discover.db.dao.DcCommonDao.DetailDataId;
import com.samsung.android.spayfw.payprovider.discover.db.dao.DcPaymentProfileDaoImpl;
import com.samsung.android.spayfw.payprovider.discover.db.models.CardDetails;
import com.samsung.android.spayfw.payprovider.discover.db.models.DcCardMaster;
import com.samsung.android.spayfw.payprovider.discover.db.models.DcPaymentProfile;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverContactlessPaymentData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverInnAppPaymentData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentCard;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentProfile;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import com.samsung.android.spaytzsvc.api.TACommands.MoveServiceKey;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class DcStorageManager {
    private static Gson mGson;

    public enum ResultCode {
        ERR_MIN("Min Error Code"),
        ERR_NONE(MoveServiceKey.TZ_SPAY_MSK_NO_ERROR_STR),
        ERR_FAIL("Request Failed"),
        ERR_SERIALIZATION_FAILED("JSON Serialization Failed"),
        ERR_EXCEPTION("Exception Occurred"),
        ERR_SAVE_DATA_FAILED("Failed to Save Data"),
        ERR_UPDATE_DATA_FAILED("Failed to Update Data"),
        ERR_DELETE_DATA_FAILED("Failed to Delete Data"),
        ERR_INPUT_NULL("Invalid Input (Null)"),
        ERR_NO_DATA("No Data Found in Storage"),
        ERR_MAX("Max Error Code");
        
        private String mMessage;

        public String getErrorMessage() {
            return this.mMessage;
        }

        private ResultCode(String str) {
            this.mMessage = str;
        }
    }

    static {
        mGson = new GsonBuilder().create();
    }

    public static synchronized DiscoverContactlessPaymentData m866e(long j) {
        DiscoverContactlessPaymentData discoverContactlessPaymentData;
        synchronized (DcStorageManager.class) {
            Log.m285d("DCSDK_DcStorageManager", "getContactlessPaymentData");
            try {
                CardDetails a = m863a(j, DetailDataId.DC_CONTACTLESS_PAYMENT_DATA.cJ());
                if (a == null) {
                    Log.m286e("DCSDK_DcStorageManager", "getContactlessPaymentData, loadCardDetailsRecord returns null, tokenId " + j);
                    discoverContactlessPaymentData = null;
                } else {
                    discoverContactlessPaymentData = (DiscoverContactlessPaymentData) mGson.fromJson(new String(a.getData(), "UTF8"), DiscoverContactlessPaymentData.class);
                    discoverContactlessPaymentData.setPaymentProfiles(m874m(j));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                discoverContactlessPaymentData = null;
            }
        }
        return discoverContactlessPaymentData;
    }

    public static synchronized ResultCode m857a(long j, DiscoverContactlessPaymentData discoverContactlessPaymentData) {
        ResultCode resultCode;
        synchronized (DcStorageManager.class) {
            Log.m285d("DCSDK_DcStorageManager", "saveContactlessPaymentData");
            try {
                Set<Entry> entrySet = discoverContactlessPaymentData.getPaymentProfiles().entrySet();
                resultCode = ResultCode.ERR_NONE;
                for (Entry value : entrySet) {
                    resultCode = m859a(j, (DiscoverPaymentProfile) value.getValue());
                    if (resultCode != ResultCode.ERR_NONE) {
                        break;
                    }
                }
                if (resultCode != ResultCode.ERR_NONE) {
                    Log.m285d("DCSDK_DcStorageManager", "saveContactlessPaymentData: Failed to save Payment Profiles");
                } else {
                    discoverContactlessPaymentData.setPaymentProfiles(null);
                    Object toJson = mGson.toJson((Object) discoverContactlessPaymentData);
                    if (TextUtils.isEmpty(toJson)) {
                        Log.m286e("DCSDK_DcStorageManager", "saveContactlessPaymentData: jsonData failed");
                        resultCode = ResultCode.ERR_SERIALIZATION_FAILED;
                    } else {
                        resultCode = m854a(j, DetailDataId.DC_CONTACTLESS_PAYMENT_DATA.cJ(), toJson.getBytes("UTF8"));
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                resultCode = ResultCode.ERR_EXCEPTION;
            } catch (NullPointerException e2) {
                e2.printStackTrace();
                resultCode = ResultCode.ERR_EXCEPTION;
            }
        }
        return resultCode;
    }

    public static synchronized DiscoverInnAppPaymentData m867f(long j) {
        DiscoverInnAppPaymentData discoverInnAppPaymentData;
        synchronized (DcStorageManager.class) {
            Log.m285d("DCSDK_DcStorageManager", "getInAppPaymentData");
            try {
                CardDetails a = m863a(j, DetailDataId.DC_INAPP_PAYMENT_DATA.cJ());
                if (a == null) {
                    Log.m286e("DCSDK_DcStorageManager", "getInAppPaymentData, loadCardDetailsRecord returns null, tokenId " + j);
                    discoverInnAppPaymentData = null;
                } else {
                    discoverInnAppPaymentData = (DiscoverInnAppPaymentData) mGson.fromJson(new String(a.getData(), "UTF8"), DiscoverInnAppPaymentData.class);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                discoverInnAppPaymentData = null;
                return discoverInnAppPaymentData;
            } catch (NullPointerException e2) {
                e2.printStackTrace();
                discoverInnAppPaymentData = null;
                return discoverInnAppPaymentData;
            }
        }
        return discoverInnAppPaymentData;
    }

    public static synchronized ResultCode m858a(long j, DiscoverInnAppPaymentData discoverInnAppPaymentData) {
        ResultCode resultCode;
        synchronized (DcStorageManager.class) {
            Log.m285d("DCSDK_DcStorageManager", "saveInAppPaymentData");
            try {
                Object toJson = mGson.toJson((Object) discoverInnAppPaymentData);
                if (TextUtils.isEmpty(toJson)) {
                    Log.m286e("DCSDK_DcStorageManager", "saveInAppPaymentData: jsonData failed");
                    resultCode = ResultCode.ERR_SERIALIZATION_FAILED;
                } else {
                    resultCode = m854a(j, DetailDataId.DC_INAPP_PAYMENT_DATA.cJ(), toJson.getBytes("UTF8"));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                resultCode = ResultCode.ERR_EXCEPTION;
            }
        }
        return resultCode;
    }

    public static synchronized byte[] m868g(long j) {
        byte[] bArr = null;
        synchronized (DcStorageManager.class) {
            Log.m285d("DCSDK_DcStorageManager", "getPaymentSecureObj");
            try {
                CardDetails a = m863a(j, DetailDataId.DC_PAYMENT_SECUREOBJ.cJ());
                if (a == null) {
                    Log.m286e("DCSDK_DcStorageManager", "getPaymentSecureObj, loadCardDetailsRecord returns null, tokenId " + j);
                } else {
                    bArr = a.getData();
                }
            } catch (NullPointerException e) {
                Log.m286e("DCSDK_DcStorageManager", e.getMessage());
                e.printStackTrace();
            }
        }
        return bArr;
    }

    public static synchronized ResultCode m861a(long j, byte[] bArr) {
        ResultCode a;
        synchronized (DcStorageManager.class) {
            a = m854a(j, DetailDataId.DC_PAYMENT_SECUREOBJ.cJ(), bArr);
        }
        return a;
    }

    public static synchronized ResultCode m859a(long j, DiscoverPaymentProfile discoverPaymentProfile) {
        ResultCode resultCode;
        synchronized (DcStorageManager.class) {
            DcPaymentProfileDaoImpl dcPaymentProfileDaoImpl = new DcPaymentProfileDaoImpl(DiscoverPayProvider.cC());
            ResultCode resultCode2 = ResultCode.ERR_NONE;
            Log.m285d("DCSDK_DcStorageManager", "saveOrUpdatePaymentProfile");
            try {
                DcPaymentProfile c = dcPaymentProfileDaoImpl.m889c(j, (long) discoverPaymentProfile.getProfileId());
                if (c == null) {
                    Log.m287i("DCSDK_DcStorageManager", "No existing record");
                    c = new DcPaymentProfile();
                    c.init(j, discoverPaymentProfile);
                    dcPaymentProfileDaoImpl.saveData(c);
                } else {
                    Log.m287i("DCSDK_DcStorageManager", "Found existing record.");
                    c.init(j, discoverPaymentProfile);
                    if (c.getRowId() != -1) {
                        dcPaymentProfileDaoImpl.updateData(c, c.getRowId());
                    } else {
                        Log.m286e("DCSDK_DcStorageManager", "Invalid Row Id for Payment Profile Record");
                        resultCode = ResultCode.ERR_UPDATE_DATA_FAILED;
                    }
                }
                resultCode = ResultCode.ERR_NONE;
            } catch (DcDbException e) {
                Log.m286e("DCSDK_DcStorageManager", e.getMessage());
                if (e.getErrorCode() == 7) {
                    resultCode = ResultCode.ERR_UPDATE_DATA_FAILED;
                } else {
                    resultCode = ResultCode.ERR_SAVE_DATA_FAILED;
                }
            }
        }
        return resultCode;
    }

    public static synchronized ResultCode m864b(long j, byte[] bArr) {
        ResultCode a;
        synchronized (DcStorageManager.class) {
            a = m854a(j, DetailDataId.DC_PAYMENT_PTHDATA.cJ(), bArr);
        }
        return a;
    }

    public static synchronized byte[] m869h(long j) {
        byte[] bArr = null;
        synchronized (DcStorageManager.class) {
            CardDetailsDaoImpl cardDetailsDaoImpl = new CardDetailsDaoImpl(DiscoverPayProvider.cC());
            Log.m285d("DCSDK_DcStorageManager", "getOTPKData");
            try {
                CardDetails b = cardDetailsDaoImpl.m880b(j, (long) DetailDataId.DC_PAYMENT_OTPKDATA.cJ());
                if (b != null) {
                    bArr = b.getData();
                }
            } catch (DcDbException e) {
                Log.m286e("DCSDK_DcStorageManager", e.getMessage());
                e.printStackTrace();
            }
        }
        return bArr;
    }

    public static synchronized ResultCode m865c(long j, byte[] bArr) {
        ResultCode a;
        synchronized (DcStorageManager.class) {
            a = m854a(j, DetailDataId.DC_PAYMENT_OTPKDATA.cJ(), bArr);
        }
        return a;
    }

    public static synchronized ResultCode m862a(DiscoverPaymentCard discoverPaymentCard) {
        ResultCode resultCode;
        synchronized (DcStorageManager.class) {
            resultCode = ResultCode.ERR_NONE;
            long tokenId = discoverPaymentCard.getTokenId();
            resultCode = m857a(tokenId, discoverPaymentCard.getDiscoverContactlessPaymentData());
            if (resultCode != ResultCode.ERR_NONE) {
                Log.m286e("DCSDK_DcStorageManager", "saveDiscoverPaymentCard: Failed to save ContactlessPaymentData. Error: " + resultCode.getErrorMessage());
            } else {
                resultCode = m858a(tokenId, discoverPaymentCard.getDiscoverInnAppPaymentData());
                if (resultCode != ResultCode.ERR_NONE) {
                    Log.m286e("DCSDK_DcStorageManager", "saveDiscoverPaymentCard: Failed to save InnAppPaymentData. Error: " + resultCode.getErrorMessage());
                } else {
                    resultCode = m865c(tokenId, discoverPaymentCard.getOTPK());
                    if (resultCode != ResultCode.ERR_NONE) {
                        Log.m286e("DCSDK_DcStorageManager", "saveDiscoverPaymentCard: Failed to save OTPK Data. Error: " + resultCode.getErrorMessage());
                    } else {
                        resultCode = m861a(tokenId, discoverPaymentCard.getSecureObject());
                        if (resultCode != ResultCode.ERR_NONE) {
                            Log.m286e("DCSDK_DcStorageManager", "saveDiscoverPaymentCard: Failed to save SecureObject. Error: " + resultCode.getErrorMessage());
                        } else {
                            resultCode = m864b(tokenId, discoverPaymentCard.getDiscoverContactlessPaymentData().getPth().getBytes());
                            if (resultCode != ResultCode.ERR_NONE) {
                                Log.m286e("DCSDK_DcStorageManager", "saveDiscoverPaymentCard: Failed to save PTH Data. Error: " + resultCode.getErrorMessage());
                            }
                        }
                    }
                }
            }
        }
        return resultCode;
    }

    public static synchronized DiscoverPaymentCard m870i(long j) {
        DiscoverPaymentCard discoverPaymentCard;
        synchronized (DcStorageManager.class) {
            discoverPaymentCard = new DiscoverPaymentCard(j, null, null, null);
            DiscoverContactlessPaymentData e = m866e(j);
            if (e == null) {
                Log.m286e("DCSDK_DcStorageManager", "getDiscoverPaymentCard: Failed to get DiscoverContactlessPaymentData.");
                discoverPaymentCard = null;
            } else {
                discoverPaymentCard.setDiscoverContactlessPaymentData(e);
                DiscoverInnAppPaymentData f = m867f(j);
                if (f == null) {
                    Log.m286e("DCSDK_DcStorageManager", "getDiscoverPaymentCard: Failed to get DiscoverInnAppPaymentData.");
                    discoverPaymentCard = null;
                } else {
                    discoverPaymentCard.setDiscoverInnAppPaymentData(f);
                    byte[] h = m869h(j);
                    if (h == null) {
                        Log.m286e("DCSDK_DcStorageManager", "getDiscoverPaymentCard: Failed to get otpkData.");
                        discoverPaymentCard = null;
                    } else {
                        discoverPaymentCard.setOTPK(h);
                        h = m868g(j);
                        if (h == null) {
                            Log.m286e("DCSDK_DcStorageManager", "getDiscoverPaymentCard: Failed to get secureObjData.");
                            discoverPaymentCard = null;
                        } else {
                            discoverPaymentCard.setSecureObject(h);
                            CardDetails a = m863a(j, DetailDataId.DC_PAYMENT_PTHDATA.cJ());
                            if (a == null) {
                                Log.m286e("DCSDK_DcStorageManager", "getDiscoverPaymentCard: Failed to get pthData.");
                                discoverPaymentCard = null;
                            } else {
                                discoverPaymentCard.getDiscoverContactlessPaymentData().setPth(new ByteBuffer(a.getData()));
                            }
                        }
                    }
                }
            }
        }
        return discoverPaymentCard;
    }

    public static synchronized long m871j(long j) {
        long remainingOtpkCount;
        synchronized (DcStorageManager.class) {
            DcCardMaster l = m873l(j);
            if (l != null) {
                remainingOtpkCount = l.getRemainingOtpkCount();
            } else {
                remainingOtpkCount = 0;
            }
        }
        return remainingOtpkCount;
    }

    public static synchronized ResultCode m855a(long j, long j2) {
        ResultCode a;
        synchronized (DcStorageManager.class) {
            DcCardMaster l = m873l(j);
            if (l != null) {
                l.setRemainingOtpkCount(j2);
                a = m856a(j, l);
            } else {
                a = ResultCode.ERR_NO_DATA;
            }
        }
        return a;
    }

    public static synchronized ResultCode m860a(long j, String str) {
        ResultCode a;
        synchronized (DcStorageManager.class) {
            DcCardMaster l = m873l(j);
            if (l != null) {
                l.setStatus(str);
                a = m856a(j, l);
            } else {
                a = ResultCode.ERR_NO_DATA;
            }
        }
        return a;
    }

    public static synchronized ResultCode m872k(long j) {
        ResultCode resultCode;
        synchronized (DcStorageManager.class) {
            DcCardMasterDaoImpl dcCardMasterDaoImpl = new DcCardMasterDaoImpl(DiscoverPayProvider.cC());
            resultCode = ResultCode.ERR_NONE;
            Log.m287i("DCSDK_DcStorageManager", "deleteToken");
            try {
                dcCardMasterDaoImpl.deleteData(j);
            } catch (DcDbException e) {
                Log.m286e("DCSDK_DcStorageManager", "delete: DB Exception while deleting data");
                e.printStackTrace();
                resultCode = ResultCode.ERR_DELETE_DATA_FAILED;
            }
        }
        return resultCode;
    }

    public static ResultCode m856a(long j, DcCardMaster dcCardMaster) {
        DcCardMasterDaoImpl dcCardMasterDaoImpl = new DcCardMasterDaoImpl(DiscoverPayProvider.cC());
        ResultCode resultCode = ResultCode.ERR_NONE;
        try {
            dcCardMasterDaoImpl.updateData(dcCardMaster, j);
            return resultCode;
        } catch (DcDbException e) {
            e.printStackTrace();
            return ResultCode.ERR_SAVE_DATA_FAILED;
        }
    }

    public static void cH() {
        try {
            new DcCardMasterDaoImpl(DiscoverPayProvider.cC()).cI();
        } catch (DcDbException e) {
            e.printStackTrace();
        }
    }

    private static ResultCode m854a(long j, int i, byte[] bArr) {
        ResultCode resultCode = ResultCode.ERR_NONE;
        CardDetailsDaoImpl cardDetailsDaoImpl = new CardDetailsDaoImpl(DiscoverPayProvider.cC());
        try {
            CardDetails b = cardDetailsDaoImpl.m880b(j, (long) i);
            if (b == null) {
                Log.m287i("DCSDK_DcStorageManager", "No existing record");
                cardDetailsDaoImpl.saveData(new CardDetails(j, (long) i, bArr));
            } else {
                Log.m287i("DCSDK_DcStorageManager", "Found existing record.");
                b.setData(bArr);
                cardDetailsDaoImpl.m879a(b, j);
            }
            return ResultCode.ERR_NONE;
        } catch (DcDbException e) {
            Log.m286e("DCSDK_DcStorageManager", e.getMessage());
            e.printStackTrace();
            if (e.getErrorCode() == 7) {
                return ResultCode.ERR_UPDATE_DATA_FAILED;
            }
            return ResultCode.ERR_SAVE_DATA_FAILED;
        }
    }

    private static DcCardMaster m873l(long j) {
        DcCardMaster dcCardMaster;
        DcCardMasterDaoImpl dcCardMasterDaoImpl = new DcCardMasterDaoImpl(DiscoverPayProvider.cC());
        Log.m285d("DCSDK_DcStorageManager", "loadCardMaster: tokenId - " + j);
        try {
            dcCardMaster = (DcCardMaster) dcCardMasterDaoImpl.getData(j);
        } catch (DcDbException e) {
            e.printStackTrace();
            dcCardMaster = null;
        }
        if (dcCardMaster == null) {
            Log.m286e("DCSDK_DcStorageManager", "loadCardMaster: Failed to load CardMaster Record");
        }
        return dcCardMaster;
    }

    private static CardDetails m863a(long j, int i) {
        CardDetailsDaoImpl cardDetailsDaoImpl = new CardDetailsDaoImpl(DiscoverPayProvider.cC());
        Log.m285d("DCSDK_DcStorageManager", "loadCardDetailsRecord: tokenId - " + j + ", detailDataId - " + i);
        try {
            return cardDetailsDaoImpl.m880b(j, (long) i);
        } catch (DcDbException e) {
            Log.m286e("DCSDK_DcStorageManager", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static HashMap<Integer, DiscoverPaymentProfile> m874m(long j) {
        Log.m285d("DCSDK_DcStorageManager", "loadDiscoverPaymentProfiles: tokenId - " + j);
        DcPaymentProfileDaoImpl dcPaymentProfileDaoImpl = new DcPaymentProfileDaoImpl(DiscoverPayProvider.cC());
        HashMap<Integer, DiscoverPaymentProfile> hashMap = new HashMap();
        try {
            List<DcPaymentProfile> o = dcPaymentProfileDaoImpl.m891o(j);
            if (o != null) {
                for (DcPaymentProfile dcPaymentProfile : o) {
                    hashMap.put(Integer.valueOf((int) dcPaymentProfile.getProfileId()), DcPaymentProfile.toDiscoverPaymentProfile(dcPaymentProfile));
                }
            } else {
                Log.m286e("DCSDK_DcStorageManager", "loadDiscoverPaymentProfiles: payment profiles list not found.");
            }
            return hashMap;
        } catch (DcDbException e) {
            Log.m286e("DCSDK_DcStorageManager", "loadDiscoverPaymentProfiles: Exception - " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
