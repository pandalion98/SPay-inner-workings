package com.samsung.android.spayfw.core.retry;

import android.os.Message;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.PaymentFrameworkHandler;
import com.samsung.android.spayfw.p002b.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/* renamed from: com.samsung.android.spayfw.core.retry.a */
public class JwtRetryRequester {
    private static JwtRetryRequester mL;
    private static List<Message> mM;
    private PaymentFrameworkHandler jB;
    private Timer lH;

    /* renamed from: com.samsung.android.spayfw.core.retry.a.a */
    public class JwtRetryRequester extends TimerTask {
        final /* synthetic */ JwtRetryRequester mN;

        public JwtRetryRequester(JwtRetryRequester jwtRetryRequester) {
            this.mN = jwtRetryRequester;
        }

        public void run() {
            Log.m285d("JwtRetryRequester", "run : JwtRetryTimerTask ");
            this.mN.bk();
        }
    }

    static {
        mM = new ArrayList();
    }

    private JwtRetryRequester() {
        this.jB = PaymentFrameworkApp.az();
    }

    public static synchronized JwtRetryRequester bj() {
        JwtRetryRequester jwtRetryRequester;
        synchronized (JwtRetryRequester.class) {
            if (mL == null) {
                mL = new JwtRetryRequester();
            }
            jwtRetryRequester = mL;
        }
        return jwtRetryRequester;
    }

    public synchronized void m664b(Message message) {
        Log.m285d("JwtRetryRequester", "addInQueue: adding msg in queue = " + message.toString());
        if (!(mM == null || mM.contains(message))) {
            mM.add(message);
            if (this.lH == null) {
                this.lH = new Timer();
                this.lH.schedule(new JwtRetryRequester(this), 60000);
            }
        }
    }

    public synchronized void bk() {
        Log.m285d("JwtRetryRequester", "flushJwtQueue");
        if (mM != null) {
            int size = mM.size();
            for (int i = 0; i < size; i++) {
                Message message = (Message) mM.get(0);
                Log.m285d("JwtRetryRequester", "flushJwtQueue: msg number = " + i);
                this.jB.m618a(message);
                mM.remove(message);
                if (mM.size() < 1) {
                    break;
                }
            }
        }
        if (this.lH != null) {
            this.lH.cancel();
            this.lH = null;
        }
    }
}
