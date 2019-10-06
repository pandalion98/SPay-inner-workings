package com.sec.android.app.hwmoduletest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.XMLDataStorage;

public class HwModuleTestBroadcastReceiver extends BroadcastReceiver {
    public static final String SECRET_CODE_ACTION = "android.provider.Telephony.SECRET_CODE";
    private static final String TAG = "TestingBroadcastReceiver";

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SECRET_CODE_ACTION)) {
            Intent i = new Intent();
            String host = intent.getData().getHost();
            StringBuilder sb = new StringBuilder();
            sb.append("Get the Secret code action. Host=");
            sb.append(host);
            LtUtil.log_i(TAG, "onReceive", sb.toString());
            if (!XMLDataStorage.instance().wasCompletedParsing()) {
                XMLDataStorage.instance().parseXML(context);
            }
            if (host == null) {
                LtUtil.log_e(TAG, "onReceive", "host is null.");
                return;
            }
            if (host.equals("0*")) {
                if (XMLDataStorage.instance().wasCompletedParsing()) {
                    i.setClass(context, HwModuleTest.class);
                } else {
                    LtUtil.log_e(TAG, "onReceive", "Lcdtest XML data parsing was not completed.");
                }
                i.addFlags(276824064);
            }
            context.startActivity(i);
        }
    }
}
