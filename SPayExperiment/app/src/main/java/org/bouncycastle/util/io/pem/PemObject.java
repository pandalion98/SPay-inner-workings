/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Collections
 *  java.util.List
 */
package org.bouncycastle.util.io.pem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bouncycastle.util.io.pem.PemObjectGenerator;

public class PemObject
implements PemObjectGenerator {
    private static final List EMPTY_LIST = Collections.unmodifiableList((List)new ArrayList());
    private byte[] content;
    private List headers;
    private String type;

    public PemObject(String string, List list, byte[] arrby) {
        this.type = string;
        this.headers = Collections.unmodifiableList((List)list);
        this.content = arrby;
    }

    public PemObject(String string, byte[] arrby) {
        this(string, EMPTY_LIST, arrby);
    }

    @Override
    public PemObject generate() {
        return this;
    }

    public byte[] getContent() {
        return this.content;
    }

    public List getHeaders() {
        return this.headers;
    }

    public String getType() {
        return this.type;
    }
}

