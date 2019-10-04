/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.Vector
 */
package org.bouncycastle.asn1.x509;

import java.util.Vector;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;

public class GeneralNamesBuilder {
    private Vector names = new Vector();

    public GeneralNamesBuilder addName(GeneralName generalName) {
        this.names.addElement((Object)generalName);
        return this;
    }

    public GeneralNamesBuilder addNames(GeneralNames generalNames) {
        GeneralName[] arrgeneralName = generalNames.getNames();
        for (int i2 = 0; i2 != arrgeneralName.length; ++i2) {
            this.names.addElement((Object)arrgeneralName[i2]);
        }
        return this;
    }

    public GeneralNames build() {
        GeneralName[] arrgeneralName = new GeneralName[this.names.size()];
        for (int i2 = 0; i2 != arrgeneralName.length; ++i2) {
            arrgeneralName[i2] = (GeneralName)this.names.elementAt(i2);
        }
        return new GeneralNames(arrgeneralName);
    }
}

