/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.PublicKey
 *  java.security.cert.X509Extension
 *  java.util.Date
 */
package org.bouncycastle.x509;

import java.math.BigInteger;
import java.security.PublicKey;
import java.security.cert.X509Extension;
import java.util.Date;
import org.bouncycastle.x509.AttributeCertificateHolder;
import org.bouncycastle.x509.AttributeCertificateIssuer;
import org.bouncycastle.x509.X509Attribute;

public interface X509AttributeCertificate
extends X509Extension {
    public void checkValidity();

    public void checkValidity(Date var1);

    public X509Attribute[] getAttributes();

    public X509Attribute[] getAttributes(String var1);

    public byte[] getEncoded();

    public AttributeCertificateHolder getHolder();

    public AttributeCertificateIssuer getIssuer();

    public boolean[] getIssuerUniqueID();

    public Date getNotAfter();

    public Date getNotBefore();

    public BigInteger getSerialNumber();

    public byte[] getSignature();

    public int getVersion();

    public void verify(PublicKey var1, String var2);
}

