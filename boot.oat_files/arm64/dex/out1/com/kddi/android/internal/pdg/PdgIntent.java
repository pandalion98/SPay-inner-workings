package com.kddi.android.internal.pdg;

public final class PdgIntent {
    public static final String ACTION_FIRST_ACCESS = "com.kddi.android.pdg.intent.action.FIRST_ACCESS";
    public static final String ACTION_SHOW_PDG_DIALOG = "com.kddi.android.pdg.intent.action.SHOW_PDG_DIALOG";
    public static final String ACTION_SHOW_PDG_NOTIFICATION = "com.kddi.android.pdg.intent.action.SHOW_PDG_NOTIFICATION";
    public static final String ACTION_START_PDG_SETTING = "com.kddi.android.pdg.intent.action.START_PDG_SETTING";
    public static final String ACTION_UPDATE_PDG_STATUS = "com.kddi.android.pdg.intent.action.UPDATE_PDG_STATUS";
    public static final String EXTRA_ACCESS_TIME = "accesstime";
    public static final String EXTRA_APP_NAME = "packagename";
    public static final String EXTRA_CURRENT_PRIVACY = "currentprivacy";
    public static final String EXTRA_DATA_TYPE = "datatype";

    private PdgIntent() {
    }
}
