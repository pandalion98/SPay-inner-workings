/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.tls.TlsCredentials;

public interface TlsAgreementCredentials
extends TlsCredentials {
    public byte[] generateAgreement(AsymmetricKeyParameter var1);
}

