/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Cloneable
 *  java.lang.Object
 *  java.security.cert.CertPath
 *  java.util.Collection
 *  java.util.Set
 */
package org.bouncycastle.x509;

import java.security.cert.CertPath;
import java.util.Collection;
import java.util.Set;
import org.bouncycastle.x509.X509AttributeCertificate;

public abstract class PKIXAttrCertChecker
implements Cloneable {
    public abstract void check(X509AttributeCertificate var1, CertPath var2, CertPath var3, Collection var4);

    public abstract Object clone();

    public abstract Set getSupportedExtensions();
}

