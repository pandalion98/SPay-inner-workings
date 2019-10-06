package com.synaptics.fingerprint;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class VcsTemplateIds {
    public Map<Integer, byte[]> fingerIndexTemplateIdMap = new HashMap();

    public boolean setTemplateId(int fingerIndex, byte[] templateId) {
        this.fingerIndexTemplateIdMap.put(Integer.valueOf(fingerIndex), templateId);
        return true;
    }

    public Map<Integer, byte[]> getTemplateIds() {
        return this.fingerIndexTemplateIdMap;
    }

    public byte[] getTemplateId(int fingerIndex) {
        for (Entry<Integer, byte[]> mEntry : this.fingerIndexTemplateIdMap.entrySet()) {
            if (((Integer) mEntry.getKey()).intValue() == fingerIndex) {
                return (byte[]) mEntry.getValue();
            }
        }
        return null;
    }
}
