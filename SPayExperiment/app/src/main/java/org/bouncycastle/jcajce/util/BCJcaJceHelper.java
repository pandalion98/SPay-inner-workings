/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 *  java.security.Provider
 *  java.security.Security
 */
package org.bouncycastle.jcajce.util;

import java.security.Provider;
import java.security.Security;
import org.bouncycastle.jcajce.util.ProviderJcaJceHelper;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class BCJcaJceHelper
extends ProviderJcaJceHelper {
    public BCJcaJceHelper() {
        super(BCJcaJceHelper.getBouncyCastleProvider());
    }

    private static Provider getBouncyCastleProvider() {
        if (Security.getProvider((String)"BC") != null) {
            return Security.getProvider((String)"BC");
        }
        return new BouncyCastleProvider();
    }
}

