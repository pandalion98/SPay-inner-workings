package com.samsung.android.spayfw.core.retry;

import com.samsung.android.spayfw.core.p005a.TokenReplenisher;
import com.samsung.android.spayfw.p002b.Log;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/* renamed from: com.samsung.android.spayfw.core.retry.c */
public class ReplenishRetryRequester {
    private static ReplenishRetryRequester mO;
    private static ArrayList<TokenReplenisher> mP;

    /* renamed from: com.samsung.android.spayfw.core.retry.c.a */
    public class ReplenishRetryRequester extends TimerTask {
        private TokenReplenisher mQ;
        final /* synthetic */ ReplenishRetryRequester mR;

        public ReplenishRetryRequester(ReplenishRetryRequester replenishRetryRequester, TokenReplenisher tokenReplenisher) {
            this.mR = replenishRetryRequester;
            this.mQ = tokenReplenisher;
        }

        public void run() {
            Log.m285d("ReplenishRetryRequester", "run : ReplenishRetryTimerTask - tokenId = " + this.mQ.getTokenId());
            try {
                this.mQ.process();
            } catch (Throwable e) {
                Log.m284c("ReplenishRetryRequester", e.getMessage(), e);
            }
        }
    }

    static {
        mP = new ArrayList();
    }

    private ReplenishRetryRequester() {
    }

    public static synchronized ReplenishRetryRequester bl() {
        ReplenishRetryRequester replenishRetryRequester;
        synchronized (ReplenishRetryRequester.class) {
            if (mO == null) {
                mO = new ReplenishRetryRequester();
            }
            replenishRetryRequester = mO;
        }
        return replenishRetryRequester;
    }

    public static synchronized void m665a(TokenReplenisher tokenReplenisher) {
        synchronized (ReplenishRetryRequester.class) {
            Log.m285d("ReplenishRetryRequester", "addInQueue: adding token in queue = " + tokenReplenisher.getTokenId());
            if (!(mP == null || mP.contains(tokenReplenisher))) {
                mP.add(tokenReplenisher);
            }
        }
    }

    public static synchronized void bm() {
        synchronized (ReplenishRetryRequester.class) {
            int size = mP.size();
            for (int i = 0; i < size; i++) {
                TokenReplenisher tokenReplenisher = (TokenReplenisher) mP.get(0);
                Log.m285d("ReplenishRetryRequester", "flushReplenishQueue: trTokenId = " + tokenReplenisher.getTokenId());
                try {
                    tokenReplenisher.process();
                } catch (Throwable e) {
                    Log.m284c("ReplenishRetryRequester", e.getMessage(), e);
                }
                mP.remove(tokenReplenisher);
                if (mP.size() < 1) {
                    break;
                }
            }
        }
    }

    public static synchronized void m666b(TokenReplenisher tokenReplenisher) {
        synchronized (ReplenishRetryRequester.class) {
            Log.m285d("ReplenishRetryRequester", "scheduleTimer: scheduling replenish timer for token = " + tokenReplenisher.getTokenId());
            Timer timer = new Timer();
            ReplenishRetryRequester bl = ReplenishRetryRequester.bl();
            bl.getClass();
            timer.schedule(new ReplenishRetryRequester(bl, tokenReplenisher), 120000);
        }
    }
}
