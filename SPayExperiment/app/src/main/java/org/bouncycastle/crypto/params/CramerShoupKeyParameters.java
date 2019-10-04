/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.CramerShoupParameters;

public class CramerShoupKeyParameters
extends AsymmetricKeyParameter {
    private CramerShoupParameters params;

    protected CramerShoupKeyParameters(boolean bl, CramerShoupParameters cramerShoupParameters) {
        super(bl);
        this.params = cramerShoupParameters;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        block5 : {
            block4 : {
                if (!(object instanceof CramerShoupKeyParameters)) break block4;
                CramerShoupKeyParameters cramerShoupKeyParameters = (CramerShoupKeyParameters)object;
                if (this.params != null) {
                    return this.params.equals(cramerShoupKeyParameters.getParameters());
                }
                if (cramerShoupKeyParameters.getParameters() == null) break block5;
            }
            return false;
        }
        return true;
    }

    public CramerShoupParameters getParameters() {
        return this.params;
    }

    /*
     * Enabled aggressive block sorting
     */
    public int hashCode() {
        int n2 = this.isPrivate() ? 0 : 1;
        if (this.params != null) {
            n2 ^= this.params.hashCode();
        }
        return n2;
    }
}

