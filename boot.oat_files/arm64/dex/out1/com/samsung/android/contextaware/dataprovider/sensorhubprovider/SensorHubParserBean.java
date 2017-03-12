package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import java.util.concurrent.ConcurrentHashMap;

public abstract class SensorHubParserBean {
    private final ConcurrentHashMap<String, ISensorHubParser> mParserMap = new ConcurrentHashMap();

    public final void registerParser(String key, ISensorHubParser parser) {
        if (this.mParserMap != null && !this.mParserMap.containsKey(key)) {
            this.mParserMap.put(key, parser);
        }
    }

    public final void unregisterParser(String key) {
        if (this.mParserMap != null && this.mParserMap.containsKey(key)) {
            this.mParserMap.remove(key);
        }
    }

    public final boolean checkParserMap() {
        return (this.mParserMap == null || this.mParserMap.isEmpty()) ? false : true;
    }

    protected final boolean checkParserMap(String key) {
        return (this.mParserMap == null || this.mParserMap.isEmpty() || getParser(key) == null) ? false : true;
    }

    public final ISensorHubParser getParser(String key) {
        if (this.mParserMap == null || !this.mParserMap.containsKey(key)) {
            return null;
        }
        return (ISensorHubParser) this.mParserMap.get(key);
    }
}
