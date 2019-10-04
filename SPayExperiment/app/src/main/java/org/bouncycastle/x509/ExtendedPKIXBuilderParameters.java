/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.security.InvalidParameterException
 *  java.security.cert.CertSelector
 *  java.security.cert.PKIXBuilderParameters
 *  java.security.cert.PKIXParameters
 *  java.security.cert.X509CertSelector
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.HashSet
 *  java.util.Set
 */
package org.bouncycastle.x509;

import java.security.InvalidParameterException;
import java.security.cert.CertSelector;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXParameters;
import java.security.cert.X509CertSelector;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.bouncycastle.util.Selector;
import org.bouncycastle.x509.ExtendedPKIXParameters;
import org.bouncycastle.x509.X509CertStoreSelector;

public class ExtendedPKIXBuilderParameters
extends ExtendedPKIXParameters {
    private Set excludedCerts = Collections.EMPTY_SET;
    private int maxPathLength = 5;

    public ExtendedPKIXBuilderParameters(Set set, Selector selector) {
        super(set);
        this.setTargetConstraints(selector);
    }

    public static ExtendedPKIXParameters getInstance(PKIXParameters pKIXParameters) {
        ExtendedPKIXBuilderParameters extendedPKIXBuilderParameters;
        try {
            extendedPKIXBuilderParameters = new ExtendedPKIXBuilderParameters(pKIXParameters.getTrustAnchors(), X509CertStoreSelector.getInstance((X509CertSelector)pKIXParameters.getTargetCertConstraints()));
        }
        catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
        extendedPKIXBuilderParameters.setParams(pKIXParameters);
        return extendedPKIXBuilderParameters;
    }

    @Override
    public Object clone() {
        ExtendedPKIXBuilderParameters extendedPKIXBuilderParameters;
        try {
            extendedPKIXBuilderParameters = new ExtendedPKIXBuilderParameters(this.getTrustAnchors(), this.getTargetConstraints());
        }
        catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
        extendedPKIXBuilderParameters.setParams(this);
        return extendedPKIXBuilderParameters;
    }

    public Set getExcludedCerts() {
        return Collections.unmodifiableSet((Set)this.excludedCerts);
    }

    public int getMaxPathLength() {
        return this.maxPathLength;
    }

    public void setExcludedCerts(Set set) {
        if (set == null) {
            return;
        }
        this.excludedCerts = new HashSet((Collection)set);
    }

    public void setMaxPathLength(int n) {
        if (n < -1) {
            throw new InvalidParameterException("The maximum path length parameter can not be less than -1.");
        }
        this.maxPathLength = n;
    }

    @Override
    protected void setParams(PKIXParameters pKIXParameters) {
        super.setParams(pKIXParameters);
        if (pKIXParameters instanceof ExtendedPKIXBuilderParameters) {
            ExtendedPKIXBuilderParameters extendedPKIXBuilderParameters = (ExtendedPKIXBuilderParameters)pKIXParameters;
            this.maxPathLength = extendedPKIXBuilderParameters.maxPathLength;
            this.excludedCerts = new HashSet((Collection)extendedPKIXBuilderParameters.excludedCerts);
        }
        if (pKIXParameters instanceof PKIXBuilderParameters) {
            this.maxPathLength = ((PKIXBuilderParameters)pKIXParameters).getMaxPathLength();
        }
    }
}

