package org.bouncycastle.asn1.eac;

import java.util.Enumeration;
import java.util.Hashtable;

public class Flags {
    int value;

    private class StringJoiner {
        boolean First;
        StringBuffer f38b;
        String mSeparator;

        public StringJoiner(String str) {
            this.First = true;
            this.f38b = new StringBuffer();
            this.mSeparator = str;
        }

        public void add(String str) {
            if (this.First) {
                this.First = false;
            } else {
                this.f38b.append(this.mSeparator);
            }
            this.f38b.append(str);
        }

        public String toString() {
            return this.f38b.toString();
        }
    }

    public Flags() {
        this.value = 0;
    }

    public Flags(int i) {
        this.value = 0;
        this.value = i;
    }

    String decode(Hashtable hashtable) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        Enumeration keys = hashtable.keys();
        while (keys.hasMoreElements()) {
            Integer num = (Integer) keys.nextElement();
            if (isSet(num.intValue())) {
                stringJoiner.add((String) hashtable.get(num));
            }
        }
        return stringJoiner.toString();
    }

    public int getFlags() {
        return this.value;
    }

    public boolean isSet(int i) {
        return (this.value & i) != 0;
    }

    public void set(int i) {
        this.value |= i;
    }
}
