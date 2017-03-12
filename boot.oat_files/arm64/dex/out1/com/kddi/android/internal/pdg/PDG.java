package com.kddi.android.internal.pdg;

import android.net.Uri;

public final class PDG {
    public static final String ACCESS_KEY = "permission";
    public static final String AUTH = "com.kddi.android.pdg";
    public static final String AUTH_READ_ONLY = "com.kddi.android.pdg.read_only";

    public static final class PdgStatus {
        public static final String ACCESS_TIME = "access_time";
        public static final String AUTH_STATE = "auth_state";
        public static final int AUTH_STATE_ALLOW = 1;
        public static final int AUTH_STATE_DENY = 2;
        public static final int AUTH_STATE_NONE = 0;
        public static final Uri CONTENT_URI = Uri.parse("content://com.kddi.android.pdg/pdg_status");
        public static final Uri CONTENT_URI_READ_ONLY = Uri.parse("content://com.kddi.android.pdg.read_only/pdg_status");
        public static final String DATA_TYPE = "data_type";
        public static final int DATA_TYPE_CONTACTS = 0;
        public static final int DATA_TYPE_LOCATION = 1;
        public static final int DATA_TYPE_UIM = 2;
        public static final String PACKAGE_NAME = "packagename";
        public static final String PDG = "pdg_status";
        public static final String PDG_CONTACTS = "pdg_status/contacts";
        public static final String PDG_LOCATION = "pdg_status/location";
        public static final String PDG_UIM = "pdg_status/uim";
        public static final String SETTING_STATE = "setting_state";
        public static final int SETTING_STATE_CONFIGURED = 2;
        public static final int SETTING_STATE_NOT_SET = 1;
        public static final int SETTING_STATE_NO_ACCESS = 0;
    }

    public static final class Settings {
        public static final int ACTION_ALLOW = 0;
        public static final int ACTION_DENY = 1;
        public static final String ACTION_OF_NON_SELECTED = "action_of_non_selected";
        public static final int AGREE = 2;
        public static final Uri CONTENT_URI = Uri.parse("content://com.kddi.android.pdg/settings");
        public static final Uri CONTENT_URI_READ_ONLY = Uri.parse("content://com.kddi.android.pdg.read_only/settings");
        public static final int DISABLE = 0;
        public static final int ENABLE = 1;
        public static final int FIRST = 0;
        public static final String FIRST_LAUNCH = "first_launch";
        public static final int NOT_AGREE = 1;
        public static final String PDG_ENABLE = "pdg_enable";
        public static final String SETTING = "settings";
    }

    public static final class WhiteList {
        public static final Uri CONTENT_URI = Uri.parse("content://com.kddi.android.pdg/white_list");
        public static final Uri CONTENT_URI_READ_ONLY = Uri.parse("content://com.kddi.android.pdg.read_only/white_list");
        public static final String PACKAGE_NAME = "packagename";
        public static final String WHITE_LIST = "white_list";
    }

    private PDG() {
    }
}
