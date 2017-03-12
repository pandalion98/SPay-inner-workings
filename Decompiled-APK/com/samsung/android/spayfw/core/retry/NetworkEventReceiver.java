package com.samsung.android.spayfw.core.retry;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import com.samsung.android.spayfw.core.p005a.TokenChangeChecker;
import com.samsung.android.spayfw.p002b.Log;

/* renamed from: com.samsung.android.spayfw.core.retry.b */
public class NetworkEventReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            Log.m286e("NetworkEventReceiver", "intent is null");
            return;
        }
        Log.m285d("NetworkEventReceiver", "intent.action = " + intent.getAction());
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
            if (networkInfo != null && networkInfo.getState() != null) {
                State state = networkInfo.getState();
                Log.m285d("NetworkEventReceiver", "state = " + state.toString());
                if (state.equals(State.CONNECTED)) {
                    RetryRequester.m673e(false);
                    ReplenishRetryRequester.bm();
                    TokenChangeChecker.restart();
                }
            }
        }
    }
}
