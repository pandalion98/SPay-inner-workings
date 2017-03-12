package com.samsung.android.magazinecard;

public interface Constants {

    public static class MagazineCommand {
        public static final int CARD_ADDED = 1;
        public static final int CARD_REMOVED = 3;
        public static final int CARD_UPDATED = 2;
    }

    public static class MagazineIntents {
        public static final String CARD = "card";
        public static final String COMMAND = "command";
        public static final String INTENT_NAME = "com.system.action.MAGAZINE_CARD";
    }
}
