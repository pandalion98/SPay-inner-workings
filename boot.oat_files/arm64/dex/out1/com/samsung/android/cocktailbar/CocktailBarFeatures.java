package com.samsung.android.cocktailbar;

import android.content.Context;
import android.content.pm.PackageManager;
import java.io.File;
import java.util.ArrayList;

public class CocktailBarFeatures {
    public static final String CATEGORY_NORMAL = "normal";
    public static final boolean COCKTAIL_ENABLED = false;
    private static final int FEATURE_COCKTAIL_BAR = 1;
    private static final int FEATURE_COCKTAIL_PANEL = 2;
    private static final int FEATURE_NONE = 0;
    private static ArrayList<String> mCategoryFilter;
    private static int sCocktailFeature = 0;
    private static boolean sQueriedTypeCocktail = false;

    private static void ensureCocktailFeature(Context context) {
        if (!sQueriedTypeCocktail) {
            sQueriedTypeCocktail = true;
            PackageManager pm = null;
            if (context != null) {
                pm = context.getPackageManager();
            }
            try {
                sCocktailFeature = verifyCocktailFeature(pm, 1, "com.sec.feature.cocktailbar");
                if (sCocktailFeature == 0) {
                    sCocktailFeature = verifyCocktailFeature(pm, 2, "com.sec.feature.cocktailpanel");
                }
            } catch (Exception e) {
            }
        }
    }

    private static int verifyCocktailFeature(PackageManager pm, int feature, String systemFeature) {
        if (pm != null) {
            return pm.hasSystemFeature(systemFeature) ? feature : 0;
        } else {
            return new File(new StringBuilder().append("system/etc/permissions/").append(systemFeature).append(".xml").toString()).exists() ? feature : 0;
        }
    }

    public static boolean isSupportCocktailBar(Context context) {
        return sCocktailFeature == 1;
    }

    public static boolean isSupportCocktailPanel(Context context) {
        return sCocktailFeature == 1 || sCocktailFeature == 2;
    }

    @Deprecated
    public static boolean isSystemBarType(Context context) {
        return isSupportCocktailBar(context);
    }

    public static boolean isSupportCategory(Context context, String category) {
        return false;
    }

    public static synchronized ArrayList<String> getCategroyFilters(Context context) {
        ArrayList<String> arrayList;
        synchronized (CocktailBarFeatures.class) {
            arrayList = mCategoryFilter;
        }
        return arrayList;
    }
}
