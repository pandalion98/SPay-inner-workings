/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.ClassCastException
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.security.cert.CertSelector
 *  java.security.cert.CertStore
 *  java.security.cert.PKIXParameters
 *  java.security.cert.TrustAnchor
 *  java.security.cert.X509CertSelector
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.Date
 *  java.util.HashSet
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Set
 */
package org.bouncycastle.x509;

import java.security.cert.CertSelector;
import java.security.cert.CertStore;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CertSelector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bouncycastle.util.Selector;
import org.bouncycastle.util.Store;
import org.bouncycastle.x509.PKIXAttrCertChecker;
import org.bouncycastle.x509.X509CertStoreSelector;

public class ExtendedPKIXParameters
extends PKIXParameters {
    public static final int CHAIN_VALIDITY_MODEL = 1;
    public static final int PKIX_VALIDITY_MODEL;
    private boolean additionalLocationsEnabled;
    private List additionalStores = new ArrayList();
    private Set attrCertCheckers = new HashSet();
    private Set necessaryACAttributes = new HashSet();
    private Set prohibitedACAttributes = new HashSet();
    private Selector selector;
    private List stores = new ArrayList();
    private Set trustedACIssuers = new HashSet();
    private boolean useDeltas = false;
    private int validityModel = 0;

    public ExtendedPKIXParameters(Set set) {
        super(set);
    }

    public static ExtendedPKIXParameters getInstance(PKIXParameters pKIXParameters) {
        ExtendedPKIXParameters extendedPKIXParameters;
        try {
            extendedPKIXParameters = new ExtendedPKIXParameters(pKIXParameters.getTrustAnchors());
        }
        catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
        extendedPKIXParameters.setParams(pKIXParameters);
        return extendedPKIXParameters;
    }

    public void addAddionalStore(Store store) {
        this.addAdditionalStore(store);
    }

    public void addAdditionalStore(Store store) {
        if (store != null) {
            this.additionalStores.add((Object)store);
        }
    }

    public void addStore(Store store) {
        if (store != null) {
            this.stores.add((Object)store);
        }
    }

    public Object clone() {
        ExtendedPKIXParameters extendedPKIXParameters;
        try {
            extendedPKIXParameters = new ExtendedPKIXParameters(this.getTrustAnchors());
        }
        catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
        extendedPKIXParameters.setParams(this);
        return extendedPKIXParameters;
    }

    public List getAdditionalStores() {
        return Collections.unmodifiableList((List)this.additionalStores);
    }

    public Set getAttrCertCheckers() {
        return Collections.unmodifiableSet((Set)this.attrCertCheckers);
    }

    public Set getNecessaryACAttributes() {
        return Collections.unmodifiableSet((Set)this.necessaryACAttributes);
    }

    public Set getProhibitedACAttributes() {
        return Collections.unmodifiableSet((Set)this.prohibitedACAttributes);
    }

    public List getStores() {
        return Collections.unmodifiableList((List)new ArrayList((Collection)this.stores));
    }

    public Selector getTargetConstraints() {
        if (this.selector != null) {
            return (Selector)this.selector.clone();
        }
        return null;
    }

    public Set getTrustedACIssuers() {
        return Collections.unmodifiableSet((Set)this.trustedACIssuers);
    }

    public int getValidityModel() {
        return this.validityModel;
    }

    public boolean isAdditionalLocationsEnabled() {
        return this.additionalLocationsEnabled;
    }

    public boolean isUseDeltasEnabled() {
        return this.useDeltas;
    }

    public void setAdditionalLocationsEnabled(boolean bl) {
        this.additionalLocationsEnabled = bl;
    }

    public void setAttrCertCheckers(Set set) {
        if (set == null) {
            this.attrCertCheckers.clear();
            return;
        }
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof PKIXAttrCertChecker) continue;
            throw new ClassCastException("All elements of set must be of type " + PKIXAttrCertChecker.class.getName() + ".");
        }
        this.attrCertCheckers.clear();
        this.attrCertCheckers.addAll((Collection)set);
    }

    public void setCertStores(List list) {
        if (list != null) {
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                this.addCertStore((CertStore)iterator.next());
            }
        }
    }

    public void setNecessaryACAttributes(Set set) {
        if (set == null) {
            this.necessaryACAttributes.clear();
            return;
        }
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof String) continue;
            throw new ClassCastException("All elements of set must be of type String.");
        }
        this.necessaryACAttributes.clear();
        this.necessaryACAttributes.addAll((Collection)set);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void setParams(PKIXParameters pKIXParameters) {
        this.setDate(pKIXParameters.getDate());
        this.setCertPathCheckers(pKIXParameters.getCertPathCheckers());
        this.setCertStores(pKIXParameters.getCertStores());
        this.setAnyPolicyInhibited(pKIXParameters.isAnyPolicyInhibited());
        this.setExplicitPolicyRequired(pKIXParameters.isExplicitPolicyRequired());
        this.setPolicyMappingInhibited(pKIXParameters.isPolicyMappingInhibited());
        this.setRevocationEnabled(pKIXParameters.isRevocationEnabled());
        this.setInitialPolicies(pKIXParameters.getInitialPolicies());
        this.setPolicyQualifiersRejected(pKIXParameters.getPolicyQualifiersRejected());
        this.setSigProvider(pKIXParameters.getSigProvider());
        this.setTargetCertConstraints(pKIXParameters.getTargetCertConstraints());
        try {
            this.setTrustAnchors(pKIXParameters.getTrustAnchors());
        }
        catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
        if (pKIXParameters instanceof ExtendedPKIXParameters) {
            ExtendedPKIXParameters extendedPKIXParameters = (ExtendedPKIXParameters)pKIXParameters;
            this.validityModel = extendedPKIXParameters.validityModel;
            this.useDeltas = extendedPKIXParameters.useDeltas;
            this.additionalLocationsEnabled = extendedPKIXParameters.additionalLocationsEnabled;
            Selector selector = extendedPKIXParameters.selector == null ? null : (Selector)extendedPKIXParameters.selector.clone();
            this.selector = selector;
            this.stores = new ArrayList((Collection)extendedPKIXParameters.stores);
            this.additionalStores = new ArrayList((Collection)extendedPKIXParameters.additionalStores);
            this.trustedACIssuers = new HashSet((Collection)extendedPKIXParameters.trustedACIssuers);
            this.prohibitedACAttributes = new HashSet((Collection)extendedPKIXParameters.prohibitedACAttributes);
            this.necessaryACAttributes = new HashSet((Collection)extendedPKIXParameters.necessaryACAttributes);
            this.attrCertCheckers = new HashSet((Collection)extendedPKIXParameters.attrCertCheckers);
        }
    }

    public void setProhibitedACAttributes(Set set) {
        if (set == null) {
            this.prohibitedACAttributes.clear();
            return;
        }
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof String) continue;
            throw new ClassCastException("All elements of set must be of type String.");
        }
        this.prohibitedACAttributes.clear();
        this.prohibitedACAttributes.addAll((Collection)set);
    }

    public void setStores(List list) {
        if (list == null) {
            this.stores = new ArrayList();
            return;
        }
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof Store) continue;
            throw new ClassCastException("All elements of list must be of type org.bouncycastle.util.Store.");
        }
        this.stores = new ArrayList((Collection)list);
    }

    public void setTargetCertConstraints(CertSelector certSelector) {
        super.setTargetCertConstraints(certSelector);
        if (certSelector != null) {
            this.selector = X509CertStoreSelector.getInstance((X509CertSelector)certSelector);
            return;
        }
        this.selector = null;
    }

    public void setTargetConstraints(Selector selector) {
        if (selector != null) {
            this.selector = (Selector)selector.clone();
            return;
        }
        this.selector = null;
    }

    public void setTrustedACIssuers(Set set) {
        if (set == null) {
            this.trustedACIssuers.clear();
            return;
        }
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof TrustAnchor) continue;
            throw new ClassCastException("All elements of set must be of type " + TrustAnchor.class.getName() + ".");
        }
        this.trustedACIssuers.clear();
        this.trustedACIssuers.addAll((Collection)set);
    }

    public void setUseDeltasEnabled(boolean bl) {
        this.useDeltas = bl;
    }

    public void setValidityModel(int n) {
        this.validityModel = n;
    }
}

