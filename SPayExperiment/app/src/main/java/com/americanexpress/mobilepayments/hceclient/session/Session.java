/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.Map
 */
package com.americanexpress.mobilepayments.hceclient.session;

import java.util.HashMap;
import java.util.Map;

public class Session {
    private Map<String, Object> map = new HashMap();

    public int clean() {
        int n2 = this.map.size();
        this.map.clear();
        return n2;
    }

    public void cleanAPDU() {
        this.map.remove((Object)"COMMAND_APDU_BYTES");
        this.map.remove((Object)"COMMAND_APDU");
        this.map.remove((Object)"RESPONSE_APDU");
        this.map.remove((Object)"STATUS_WORD");
        this.map.remove((Object)"VALIDATION_CONTEXT");
    }

    public Object getValue(String string, boolean bl) {
        if (bl) {
            return this.map.remove((Object)string);
        }
        return this.map.get((Object)string);
    }

    public Object removeValue(String string) {
        return this.map.remove((Object)string);
    }

    public Object setValue(String string, Object object) {
        Object object2 = this.map.get((Object)string);
        if (object2 == null) {
            this.map.put((Object)string, object);
            return object;
        }
        return object2;
    }
}

