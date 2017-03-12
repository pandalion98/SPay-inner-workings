package com.americanexpress.mobilepayments.hceclient.session;

import java.util.HashMap;
import java.util.Map;

public class Session {
    private Map<String, Object> map;

    public Session() {
        this.map = new HashMap();
    }

    public Object setValue(String str, Object obj) {
        Object obj2 = this.map.get(str);
        if (obj2 != null) {
            return obj2;
        }
        this.map.put(str, obj);
        return obj;
    }

    public Object getValue(String str, boolean z) {
        if (z) {
            return this.map.remove(str);
        }
        return this.map.get(str);
    }

    public Object removeValue(String str) {
        return this.map.remove(str);
    }

    public int clean() {
        int size = this.map.size();
        this.map.clear();
        return size;
    }

    public void cleanAPDU() {
        this.map.remove(SessionConstants.COMMAND_APDU_BYTES);
        this.map.remove(SessionConstants.COMMAND_APDU);
        this.map.remove(SessionConstants.RESPONSE_APDU);
        this.map.remove(SessionConstants.STATUS_WORD);
        this.map.remove(SessionConstants.VALIDATION_CONTEXT);
    }
}
