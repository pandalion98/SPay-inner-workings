package org.bouncycastle.asn1.x509;

import java.util.Vector;

public class GeneralNamesBuilder {
    private Vector names;

    public GeneralNamesBuilder() {
        this.names = new Vector();
    }

    public GeneralNamesBuilder addName(GeneralName generalName) {
        this.names.addElement(generalName);
        return this;
    }

    public GeneralNamesBuilder addNames(GeneralNames generalNames) {
        GeneralName[] names = generalNames.getNames();
        for (int i = 0; i != names.length; i++) {
            this.names.addElement(names[i]);
        }
        return this;
    }

    public GeneralNames build() {
        GeneralName[] generalNameArr = new GeneralName[this.names.size()];
        for (int i = 0; i != generalNameArr.length; i++) {
            generalNameArr[i] = (GeneralName) this.names.elementAt(i);
        }
        return new GeneralNames(generalNameArr);
    }
}
