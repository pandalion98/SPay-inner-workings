package com.samsung.android.smartclip;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipCroppedArea;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipDataElement;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipMetaTag;

public class SmartClipMetaUtils {
    private static final String TAG = "SmartClipMetaUtils";

    public static boolean setSmartClipTag(View view, SlookSmartClipMetaTag metaTag) {
        if (metaTag == null || metaTag.getType() == null) {
            return false;
        }
        view.removeSmartClipTag(metaTag.getType());
        return view.addSmartClipTag(metaTag);
    }

    public static boolean addMetaTag(View view, SlookSmartClipMetaTag metaTag) {
        if (metaTag == null || metaTag.getType() == null || !isValidMetaTag(metaTag)) {
            return false;
        }
        return view.addSmartClipTag(metaTag);
    }

    public static boolean removeMetaTag(View view, String tagType) {
        if (view == null || tagType == null) {
            return false;
        }
        return view.removeSmartClipTag(tagType);
    }

    public static boolean clearAllMetaTag(View view) {
        if (view == null) {
            return false;
        }
        return view.clearAllSmartClipTag();
    }

    public static boolean isValidMetaTag(SlookSmartClipMetaTag metaTag) {
        if (metaTag == null || metaTag.getType() == null) {
            return false;
        }
        String metaValue = metaTag.getValue();
        if (metaTag.getType().equals("url") && (metaValue == null || metaValue.startsWith("about:") || metaValue.startsWith("email://"))) {
            return false;
        }
        return true;
    }

    public static boolean setIntentMetaTag(View view, Intent intent) {
        if (view == null) {
            return false;
        }
        View rootView = view;
        while (rootView instanceof ViewGroup) {
            View parent = (View) ((ViewGroup) view).getParent();
            if (parent == null) {
                break;
            }
            rootView = parent;
        }
        if (!SmartClipIntentUtils.isValidIntent(view.getContext(), intent)) {
            return false;
        }
        byte[] intentData = SmartClipIntentUtils.marshall(intent);
        if (intentData == null) {
            return false;
        }
        removeMetaTag(rootView, SmartClipMetaTagType.APP_LAUNCH_INFO);
        addMetaTag(rootView, new SmartClipMetaTagImpl(SmartClipMetaTagType.APP_LAUNCH_INFO, "", intentData));
        return true;
    }

    public static boolean setDataExtractionListener(View view, SmartClipDataExtractionListener listener) {
        if (view == null) {
            return false;
        }
        view.setSmartClipDataExtractionListener(listener);
        return true;
    }

    public static int extractDefaultSmartClipData(View view, SlookSmartClipDataElement resultElement, SlookSmartClipCroppedArea croppedArea) {
        if (view == null) {
            Log.e(TAG, "extractDefaultSmartClipData : The view is null!");
            return 0;
        } else if (resultElement == null) {
            Log.e(TAG, "extractDefaultSmartClipData : The result element is null!");
            return 0;
        } else if (croppedArea != null) {
            return view.extractSmartClipData(resultElement, croppedArea);
        } else {
            Log.e(TAG, "extractDefaultSmartClipData : The cropped area is null!");
            return 0;
        }
    }

    public static boolean setPendingExtractionResult(View view, SlookSmartClipDataElement resultElement) {
        if (resultElement == null) {
            return false;
        }
        SmartClipDataRepositoryImpl repository = ((SmartClipDataElementImpl) resultElement).getDataRepository();
        SmartClipDataCropper cropper = repository != null ? repository.getSmartClipDataCropper() : null;
        if (cropper != null) {
            return cropper.setPendingExtractionResult(view, resultElement);
        }
        return false;
    }

    public static Rect getScreenRectOfView(View view) {
        Rect screenRectOfView = new Rect();
        Point screenPointOfView = getScreenPointOfView(view);
        screenRectOfView.left = screenPointOfView.x;
        screenRectOfView.top = screenPointOfView.y;
        screenRectOfView.right = screenRectOfView.left + view.getWidth();
        screenRectOfView.bottom = screenRectOfView.top + view.getHeight();
        return screenRectOfView;
    }

    public static Point getScreenPointOfView(View view) {
        Point screenPointOfView = new Point();
        int[] screenOffsetOfView = new int[2];
        view.getLocationOnScreen(screenOffsetOfView);
        screenPointOfView.x = screenOffsetOfView[0];
        screenPointOfView.y = screenOffsetOfView[1];
        return screenPointOfView;
    }

    public static boolean isInstanceOf(Object o, String className) {
        boolean z = false;
        if (!(o == null || className == null)) {
            try {
                z = Class.forName(className, true, o.getClass().getClassLoader()).isInstance(o);
            } catch (ClassNotFoundException e) {
            }
        }
        return z;
    }

    public static String getChromeViewClassNameFromManifest(Context context, String packageName) {
        String chromeViewName = null;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(packageName, 128);
            if (ai == null) {
                Log.e(TAG, "getChromeViewClassNameFromManifest : Could not get appInfo! - " + packageName);
                return null;
            }
            Bundle bundle = ai.metaData;
            if (bundle != null) {
                chromeViewName = bundle.getString("org.chromium.content.browser.SMART_CLIP_PROVIDER");
                if (chromeViewName != null) {
                    Log.d(TAG, "Target chrome view = " + chromeViewName);
                }
            }
            return chromeViewName;
        } catch (NameNotFoundException e) {
        }
    }
}
