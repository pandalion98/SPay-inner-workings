package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import android.content.Context;
import com.samsung.android.contextaware.MultiModeContextList.MultiModeContextType;
import com.samsung.android.contextaware.utilbundle.IUtilManager;
import java.util.concurrent.ConcurrentHashMap;

public class SensorHubMultiModeParser implements IUtilManager {
    private static volatile SensorHubMultiModeParser instance;
    private final ConcurrentHashMap<String, ISensorHubParser> mParserMap = new ConcurrentHashMap();

    public static SensorHubMultiModeParser getInstance() {
        if (instance == null) {
            synchronized (SensorHubMultiModeParser.class) {
                if (instance == null) {
                    instance = new SensorHubMultiModeParser();
                }
            }
        }
        return instance;
    }

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

    public final boolean containsParser(String key) {
        return (this.mParserMap == null || this.mParserMap.isEmpty() || getParser(key) == null) ? false : true;
    }

    public final ISensorHubParser getParser(String key) {
        if (this.mParserMap == null || !this.mParserMap.containsKey(key)) {
            return null;
        }
        return (ISensorHubParser) this.mParserMap.get(key);
    }

    public final void initializeManager(Context context) {
        for (MultiModeContextType i : MultiModeContextType.values()) {
            ISensorHubParser parser = i.getParserHandler();
            if (parser != null) {
                registerParser(i.getCode(), parser);
            }
        }
    }

    public final void terminateManager() {
        if (this.mParserMap != null) {
            this.mParserMap.clear();
        }
    }
}
