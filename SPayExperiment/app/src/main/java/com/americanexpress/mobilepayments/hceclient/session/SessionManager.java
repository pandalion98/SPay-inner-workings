/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.americanexpress.mobilepayments.hceclient.session;

import com.americanexpress.mobilepayments.hceclient.session.Session;

public class SessionManager {
    private static Session session = new Session();

    public static Session getSession() {
        return session;
    }
}

