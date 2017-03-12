package com.americanexpress.sdkmodulelib.payment;

import com.americanexpress.sdkmodulelib.model.Session;
import com.americanexpress.sdkmodulelib.model.TokenDataRecord;
import com.americanexpress.sdkmodulelib.model.token.ParsedTokenRecord;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;

public class SessionManager {
    private static Session session;

    static {
        session = null;
    }

    private SessionManager() {
    }

    public static Session createSession() {
        if (session == null) {
            synchronized (Session.class) {
                session = new Session();
            }
        }
        return session;
    }

    public static Session getSession() {
        return session;
    }

    public static void cleanSession() {
        if (session != null) {
            session.setTokenDataBlob(BuildConfig.FLAVOR);
            session.setParsedTokenRecord(new ParsedTokenRecord());
            session.setTokenDataRecord(new TokenDataRecord());
        }
        session = null;
    }
}
