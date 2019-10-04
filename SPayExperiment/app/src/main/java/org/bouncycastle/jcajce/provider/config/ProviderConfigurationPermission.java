/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.security.BasicPermission
 *  java.security.Permission
 *  java.util.StringTokenizer
 */
package org.bouncycastle.jcajce.provider.config;

import java.security.BasicPermission;
import java.security.Permission;
import java.util.StringTokenizer;
import org.bouncycastle.util.Strings;

public class ProviderConfigurationPermission
extends BasicPermission {
    private static final int ALL = 15;
    private static final String ALL_STR = "all";
    private static final int DH_DEFAULT_PARAMS = 8;
    private static final String DH_DEFAULT_PARAMS_STR = "dhdefaultparams";
    private static final int EC_IMPLICITLY_CA = 2;
    private static final String EC_IMPLICITLY_CA_STR = "ecimplicitlyca";
    private static final int THREAD_LOCAL_DH_DEFAULT_PARAMS = 4;
    private static final String THREAD_LOCAL_DH_DEFAULT_PARAMS_STR = "threadlocaldhdefaultparams";
    private static final int THREAD_LOCAL_EC_IMPLICITLY_CA = 1;
    private static final String THREAD_LOCAL_EC_IMPLICITLY_CA_STR = "threadlocalecimplicitlyca";
    private final String actions;
    private final int permissionMask;

    public ProviderConfigurationPermission(String string) {
        super(string);
        this.actions = ALL_STR;
        this.permissionMask = 15;
    }

    public ProviderConfigurationPermission(String string, String string2) {
        super(string, string2);
        this.actions = string2;
        this.permissionMask = this.calculateMask(string2);
    }

    private int calculateMask(String string) {
        StringTokenizer stringTokenizer = new StringTokenizer(Strings.toLowerCase(string), " ,");
        int n = 0;
        while (stringTokenizer.hasMoreTokens()) {
            String string2 = stringTokenizer.nextToken();
            if (string2.equals((Object)THREAD_LOCAL_EC_IMPLICITLY_CA_STR)) {
                n |= 1;
                continue;
            }
            if (string2.equals((Object)EC_IMPLICITLY_CA_STR)) {
                n |= 2;
                continue;
            }
            if (string2.equals((Object)THREAD_LOCAL_DH_DEFAULT_PARAMS_STR)) {
                n |= 4;
                continue;
            }
            if (string2.equals((Object)DH_DEFAULT_PARAMS_STR)) {
                n |= 8;
                continue;
            }
            if (!string2.equals((Object)ALL_STR)) continue;
            n |= 15;
        }
        if (n == 0) {
            throw new IllegalArgumentException("unknown permissions passed to mask");
        }
        return n;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        block5 : {
            block4 : {
                if (object == this) break block4;
                if (!(object instanceof ProviderConfigurationPermission)) {
                    return false;
                }
                ProviderConfigurationPermission providerConfigurationPermission = (ProviderConfigurationPermission)((Object)object);
                if (this.permissionMask != providerConfigurationPermission.permissionMask || !this.getName().equals((Object)providerConfigurationPermission.getName())) break block5;
            }
            return true;
        }
        return false;
    }

    public String getActions() {
        return this.actions;
    }

    public int hashCode() {
        return this.getName().hashCode() + this.permissionMask;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean implies(Permission permission) {
        block3 : {
            block2 : {
                if (!(permission instanceof ProviderConfigurationPermission) || !this.getName().equals((Object)permission.getName())) break block2;
                ProviderConfigurationPermission providerConfigurationPermission = (ProviderConfigurationPermission)permission;
                if ((this.permissionMask & providerConfigurationPermission.permissionMask) == providerConfigurationPermission.permissionMask) break block3;
            }
            return false;
        }
        return true;
    }
}

