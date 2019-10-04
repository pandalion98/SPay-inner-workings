/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.sdkmodulelib.payment;

import com.americanexpress.sdkmodulelib.model.Session;
import com.americanexpress.sdkmodulelib.model.TokenDataRecord;
import com.americanexpress.sdkmodulelib.model.token.ParsedTokenRecord;

public class SessionManager {
    private static Session session = null;

    private SessionManager() {
    }

    public static void cleanSession() {
        if (session != null) {
            session.setTokenDataBlob("");
            session.setParsedTokenRecord(new ParsedTokenRecord());
            session.setTokenDataRecord(new TokenDataRecord());
        }
        session = null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public static Session createSession() {
        if (session != null) return session;
        Class<Session> class_ = Session.class;
        // MONITORENTER : com.americanexpress.sdkmodulelib.model.Session.class
        session = new Session();
        // MONITOREXIT : class_
        return session;
    }

    public static Session getSession() {
        return session;
    }
}

