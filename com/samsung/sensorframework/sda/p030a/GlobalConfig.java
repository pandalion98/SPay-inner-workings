package com.samsung.sensorframework.sda.p030a;

/* renamed from: com.samsung.sensorframework.sda.a.b */
public class GlobalConfig extends AbstractConfig {
    private static final String HW;
    private static GlobalConfig HX;
    private static final Object lock;

    static {
        HW = null;
        lock = new Object();
    }

    public static GlobalConfig gO() {
        if (HX == null) {
            synchronized (lock) {
                if (HX == null) {
                    HX = GlobalConfig.gP();
                }
            }
        }
        return HX;
    }

    private static GlobalConfig gP() {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setParameter("ENABLE_HASHING", Boolean.valueOf(true));
        globalConfig.setParameter("INTENT_BROADCASTER_PERMISSION", HW);
        return globalConfig;
    }

    public boolean gQ() {
        try {
            return ((Boolean) getParameter("ENABLE_HASHING")).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public String gR() {
        try {
            return (String) getParameter("INTENT_BROADCASTER_PERMISSION");
        } catch (Exception e) {
            e.printStackTrace();
            return HW;
        }
    }
}
