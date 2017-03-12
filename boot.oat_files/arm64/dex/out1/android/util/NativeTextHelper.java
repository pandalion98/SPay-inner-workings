package android.util;

import android.content.Context;
import android.service.notification.ZenModeConfig;

public class NativeTextHelper {
    private static final String getLocalString(Context context, String originalString, String defPackage, int originNamesId, int localNamesId) {
        String[] origNames = context.getResources().getStringArray(originNamesId);
        String[] localNames = context.getResources().getStringArray(localNamesId);
        for (int i = 0; i < origNames.length; i++) {
            if (origNames[i].equalsIgnoreCase(originalString)) {
                return context.getString(context.getResources().getIdentifier(localNames[i], "string", defPackage));
            }
        }
        return originalString;
    }

    public static final String getLocalString(Context context, String originalString, int originNamesId, int localNamesId) {
        return getLocalString(context, originalString, ZenModeConfig.SYSTEM_AUTHORITY, originNamesId, localNamesId);
    }

    public static final String getInternalLocalString(Context context, String originalString, int originNamesId, int localNamesId) {
        return getLocalString(context, originalString, context.getPackageName(), originNamesId, localNamesId);
    }
}
