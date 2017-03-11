package com.samsung.android.spayfw.payprovider.mastercard.tds.network.callback;

import android.content.Context;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.McProvider;
import com.samsung.android.spayfw.payprovider.mastercard.dao.McTdsMetaDataDaoImpl;
import com.samsung.android.spayfw.utils.AsyncNetworkHttpClient.AsyncNetworkHttpClient;
import java.util.List;
import java.util.Map;
import org.bouncycastle.asn1.x509.DisplayText;

public class McTdsUnRegisterCallbck implements AsyncNetworkHttpClient {
    private static final String TAG = "McTdsUnRegisterCallbck";
    private static final String TDS_TAG_ERROR = "e_McTdsUnRegisterCallbck";
    private static final String TDS_TAG_INFO = "i_McTdsUnRegisterCallbck";
    private final long mCardMasterId;

    public McTdsUnRegisterCallbck(long j) {
        this.mCardMasterId = j;
    }

    public void onComplete(int i, Map<String, List<String>> map, byte[] bArr) {
        Context context = McProvider.getContext();
        if (context == null) {
            Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + " McTdsUnRegisterCallbck: Err. Cannot delete TDS entries without context");
            return;
        }
        McTdsMetaDataDaoImpl mcTdsMetaDataDaoImpl = new McTdsMetaDataDaoImpl(context);
        Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mCardMasterId + " McTdsUnRegisterCallbck: statusCode : " + i);
        Object obj = -36;
        switch (i) {
            case DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE /*200*/:
                obj = null;
                break;
        }
        if (obj != null) {
            Log.m286e(TAG, "tokenId: " + this.mCardMasterId + " McTdsUnRegisterCallbck: failed : " + i);
        } else if (mcTdsMetaDataDaoImpl.getData(this.mCardMasterId) == null) {
        } else {
            if (mcTdsMetaDataDaoImpl.deleteData(this.mCardMasterId)) {
                Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mCardMasterId + " McTdsUnRegisterCallbck Delete operation Success");
            } else {
                Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mCardMasterId + " McTdsUnRegisterCallbck Delete operation Falied");
            }
        }
    }
}
