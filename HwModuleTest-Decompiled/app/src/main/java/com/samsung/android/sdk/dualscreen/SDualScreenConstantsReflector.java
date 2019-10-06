package com.samsung.android.sdk.dualscreen;

import java.lang.reflect.Field;

class SDualScreenConstantsReflector extends SDualScreenReflector {
    private static final boolean DEBUG = true;
    private static final String TAG = SDualScreenConstantsReflector.class.getSimpleName();

    public static class DualScreenLaunchParams {
        static String[] FIELD_NAMES = {"FLAG_COUPLED_TASK_CONTEXTUAL_MODE", "FLAG_COUPLED_TASK_EXPAND_MODE"};
        public static int FLAG_COUPLED_TASK_CONTEXTUAL_MODE;
        public static int FLAG_COUPLED_TASK_EXPAND_MODE;

        static {
            int N = FIELD_NAMES.length;
            for (int i = 0; i < N; i++) {
                try {
                    Field src = com.samsung.android.dualscreen.DualScreenLaunchParams.class.getDeclaredField(FIELD_NAMES[i]);
                    Field dst = DualScreenLaunchParams.class.getField(FIELD_NAMES[i]);
                    dst.set(dst, src.get(src));
                } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
                }
            }
        }
    }

    public static class PackageManager {
        public static String FEATURE_DUALSCREEN;
        static String[] FIELD_NAMES = {"FEATURE_DUALSCREEN"};

        static {
            int N = FIELD_NAMES.length;
            for (int i = 0; i < N; i++) {
                try {
                    Field src = android.content.pm.PackageManager.class.getDeclaredField(FIELD_NAMES[i]);
                    Field dst = PackageManager.class.getField(FIELD_NAMES[i]);
                    dst.set(dst, src.get(src));
                } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
                }
            }
        }
    }

    private SDualScreenConstantsReflector() {
    }
}
