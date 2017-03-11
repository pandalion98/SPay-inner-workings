package com.americanexpress.mobilepayments.hceclient.session;

public class SessionManager {
    private static Session session;

    static {
        session = new Session();
    }

    public static Session getSession() {
        return session;
    }
}
