/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 *  java.util.Map
 */
package com.samsung.android.spayfw.payprovider.mastercard.tds.network.callback;

import android.content.Context;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.McProvider;
import com.samsung.android.spayfw.payprovider.mastercard.dao.McTdsMetaDataDaoImpl;
import com.samsung.android.spayfw.utils.a;
import java.util.List;
import java.util.Map;

public class McTdsUnRegisterCallbck
implements a.a {
    private static final String TAG = "McTdsUnRegisterCallbck";
    private static final String TDS_TAG_ERROR = "e_McTdsUnRegisterCallbck";
    private static final String TDS_TAG_INFO = "i_McTdsUnRegisterCallbck";
    private final long mCardMasterId;

    public McTdsUnRegisterCallbck(long l2) {
        this.mCardMasterId = l2;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void onComplete(int n2, Map<String, List<String>> map, byte[] arrby) {
        Context context = McProvider.getContext();
        if (context == null) {
            Log.e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + " McTdsUnRegisterCallbck: Err. Cannot delete TDS entries without context");
            return;
        }
        McTdsMetaDataDaoImpl mcTdsMetaDataDaoImpl = new McTdsMetaDataDaoImpl(context);
        Log.i(TDS_TAG_INFO, "tokenId: " + this.mCardMasterId + " McTdsUnRegisterCallbck: statusCode : " + n2);
        int n3 = -36;
        switch (n2) {
            case 200: {
                n3 = 0;
            }
        }
        if (n3 != 0) {
            Log.e(TAG, "tokenId: " + this.mCardMasterId + " McTdsUnRegisterCallbck: failed : " + n2);
            return;
        }
        if (mcTdsMetaDataDaoImpl.getData(this.mCardMasterId) == null) return;
        {
            if (mcTdsMetaDataDaoImpl.deleteData(this.mCardMasterId)) {
                Log.i(TDS_TAG_INFO, "tokenId: " + this.mCardMasterId + " McTdsUnRegisterCallbck Delete operation Success");
                return;
            }
        }
        Log.i(TDS_TAG_INFO, "tokenId: " + this.mCardMasterId + " McTdsUnRegisterCallbck Delete operation Falied");
    }
}

