/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.SecurityManager
 *  java.lang.String
 *  java.lang.System
 *  java.lang.ThreadLocal
 *  java.math.BigInteger
 *  java.security.Permission
 *  java.security.spec.ECParameterSpec
 *  javax.crypto.spec.DHParameterSpec
 */
package org.bouncycastle.jce.provider;

import java.math.BigInteger;
import java.security.Permission;
import java.security.spec.ECParameterSpec;
import javax.crypto.spec.DHParameterSpec;
import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.bouncycastle.jcajce.provider.config.ProviderConfiguration;
import org.bouncycastle.jcajce.provider.config.ProviderConfigurationPermission;

class BouncyCastleProviderConfiguration
implements ProviderConfiguration {
    private static Permission BC_DH_LOCAL_PERMISSION;
    private static Permission BC_DH_PERMISSION;
    private static Permission BC_EC_LOCAL_PERMISSION;
    private static Permission BC_EC_PERMISSION;
    private volatile Object dhDefaultParams;
    private ThreadLocal dhThreadSpec = new ThreadLocal();
    private volatile org.bouncycastle.jce.spec.ECParameterSpec ecImplicitCaParams;
    private ThreadLocal ecThreadSpec = new ThreadLocal();

    static {
        BC_EC_LOCAL_PERMISSION = new ProviderConfigurationPermission("BC", "threadLocalEcImplicitlyCa");
        BC_EC_PERMISSION = new ProviderConfigurationPermission("BC", "ecImplicitlyCa");
        BC_DH_LOCAL_PERMISSION = new ProviderConfigurationPermission("BC", "threadLocalDhDefaultParams");
        BC_DH_PERMISSION = new ProviderConfigurationPermission("BC", "DhDefaultParams");
    }

    BouncyCastleProviderConfiguration() {
    }

    @Override
    public DHParameterSpec getDHDefaultParameters(int n) {
        Object object = this.dhThreadSpec.get();
        if (object == null) {
            object = this.dhDefaultParams;
        }
        if (object instanceof DHParameterSpec) {
            DHParameterSpec dHParameterSpec = (DHParameterSpec)object;
            if (dHParameterSpec.getP().bitLength() == n) {
                return dHParameterSpec;
            }
        } else if (object instanceof DHParameterSpec[]) {
            DHParameterSpec[] arrdHParameterSpec = (DHParameterSpec[])object;
            for (int i = 0; i != arrdHParameterSpec.length; ++i) {
                if (arrdHParameterSpec[i].getP().bitLength() != n) continue;
                return arrdHParameterSpec[i];
            }
        }
        return null;
    }

    @Override
    public org.bouncycastle.jce.spec.ECParameterSpec getEcImplicitlyCa() {
        org.bouncycastle.jce.spec.ECParameterSpec eCParameterSpec = (org.bouncycastle.jce.spec.ECParameterSpec)this.ecThreadSpec.get();
        if (eCParameterSpec != null) {
            return eCParameterSpec;
        }
        return this.ecImplicitCaParams;
    }

    /*
     * Enabled aggressive block sorting
     */
    void setParameter(String string, Object object) {
        SecurityManager securityManager = System.getSecurityManager();
        if (string.equals((Object)"threadLocalEcImplicitlyCa")) {
            org.bouncycastle.jce.spec.ECParameterSpec eCParameterSpec;
            if (securityManager != null) {
                securityManager.checkPermission(BC_EC_LOCAL_PERMISSION);
            }
            if ((eCParameterSpec = object instanceof org.bouncycastle.jce.spec.ECParameterSpec || object == null ? (org.bouncycastle.jce.spec.ECParameterSpec)object : EC5Util.convertSpec((ECParameterSpec)object, false)) != null) {
                this.ecThreadSpec.set((Object)eCParameterSpec);
                return;
            }
            this.ecThreadSpec.remove();
            return;
        } else {
            if (string.equals((Object)"ecImplicitlyCa")) {
                if (securityManager != null) {
                    securityManager.checkPermission(BC_EC_PERMISSION);
                }
                if (!(object instanceof org.bouncycastle.jce.spec.ECParameterSpec) && object != null) {
                    this.ecImplicitCaParams = EC5Util.convertSpec((ECParameterSpec)object, false);
                    return;
                }
                this.ecImplicitCaParams = (org.bouncycastle.jce.spec.ECParameterSpec)object;
                return;
            }
            if (string.equals((Object)"threadLocalDhDefaultParams")) {
                if (securityManager != null) {
                    securityManager.checkPermission(BC_DH_LOCAL_PERMISSION);
                }
                if (!(object instanceof DHParameterSpec) && !(object instanceof DHParameterSpec[]) && object != null) {
                    throw new IllegalArgumentException("not a valid DHParameterSpec");
                }
                if (object == null) {
                    this.dhThreadSpec.remove();
                    return;
                }
                this.dhThreadSpec.set(object);
                return;
            }
            if (!string.equals((Object)"DhDefaultParams")) return;
            {
                if (securityManager != null) {
                    securityManager.checkPermission(BC_DH_PERMISSION);
                }
                if (!(object instanceof DHParameterSpec) && !(object instanceof DHParameterSpec[]) && object != null) {
                    throw new IllegalArgumentException("not a valid DHParameterSpec or DHParameterSpec[]");
                }
                this.dhDefaultParams = object;
                return;
            }
        }
    }
}

