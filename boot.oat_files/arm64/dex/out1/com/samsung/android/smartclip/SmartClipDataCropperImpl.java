package com.samsung.android.smartclip;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipCroppedArea;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipDataElement;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipMetaTag;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipMetaTagArray;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SmartClipDataCropperImpl extends SmartClipDataCropper {
    private static boolean DEBUG = false;
    public static final int EXTRACTION_LEVEL_0 = 0;
    public static final int EXTRACTION_LEVEL_1 = 1;
    private static final int EXTRACTION_RESULT_MAIN_MASKING = 255;
    private static final int MAX_META_VALUE_SIZE = 102400;
    private static final String TAG = "SmartClipDataCropperImpl";
    private static final String YOUTUBE_PACKAGE_NAME = "com.google.android.youtube";
    private static final String YOUTUBE_URL_PREFIX = "http://www.youtube.com/watch?v=";
    private String mChromeBrowserContentViewName;
    protected Context mContext;
    protected int mExtractionLevel;
    protected SmartClipDataExtractionEvent mExtractionRequest;
    private long mExtractionStartTime;
    protected boolean mIsExtractingData;
    private int mLastMetaFileId;
    protected String mPackageName;
    private int mPenWindowBorderWidth;
    protected ArrayList<SmartClipDataElementImpl> mPendingElements;
    private RectF mScaleRect;
    protected SmartClipDataRepositoryImpl mSmartClipDataRepository;
    private boolean mUseViewPositionCache;
    private HashMap<View, Point> mViewPositionCache;
    private Rect mWinFrameRect;

    public SmartClipDataCropperImpl(Context context, SmartClipDataExtractionEvent extractionRequest) {
        this(context, extractionRequest, new Rect(0, 0, 0, 0), new RectF(0.0f, 0.0f, 1.0f, 1.0f), 0);
    }

    public SmartClipDataCropperImpl(Context context, SmartClipDataExtractionEvent extractionRequest, Rect winFrameRect, RectF scaleRect, int penWindowBorderWidth) {
        this.mWinFrameRect = null;
        this.mScaleRect = null;
        this.mPenWindowBorderWidth = 0;
        this.mSmartClipDataRepository = null;
        this.mPendingElements = new ArrayList();
        this.mExtractionRequest = null;
        this.mIsExtractingData = false;
        this.mExtractionLevel = 0;
        this.mPackageName = null;
        this.mChromeBrowserContentViewName = null;
        this.mExtractionStartTime = 0;
        this.mLastMetaFileId = 0;
        this.mUseViewPositionCache = false;
        this.mViewPositionCache = new HashMap();
        this.mContext = context;
        this.mExtractionRequest = extractionRequest;
        this.mWinFrameRect = new Rect(winFrameRect);
        this.mScaleRect = new RectF(scaleRect);
        this.mPenWindowBorderWidth = penWindowBorderWidth;
        this.mPackageName = context.getPackageName();
        if (this.mPackageName == null) {
            this.mPackageName = "";
        }
        this.mChromeBrowserContentViewName = SmartClipMetaUtils.getChromeViewClassNameFromManifest(context, this.mPackageName);
        PackageManager pm = context.getPackageManager();
        if (pm != null) {
            this.mExtractionLevel = 0;
            if (pm.getSystemFeatureLevel("com.sec.feature.spen_usp") >= 3) {
                this.mExtractionLevel = 1;
            }
            if (pm.hasSystemFeature("com.samsung.android.smartclip.DEBUG")) {
                DEBUG = true;
            }
        }
    }

    public SmartClipDataRepository getSmartClipDataRepository() {
        return this.mSmartClipDataRepository;
    }

    public boolean doExtractSmartClipData(View rootView) {
        if (this.mExtractionRequest == null) {
            Log.e(TAG, "doExtractSmartClipData : extractionRequest is null!");
            return false;
        }
        this.mExtractionStartTime = System.currentTimeMillis();
        SmartClipCroppedAreaImpl croppedArea = new SmartClipCroppedAreaImpl(this.mExtractionRequest.mCropRect);
        Rect cropRect = croppedArea.getRect();
        Log.d(TAG, "doExtractSmartClipData : Extraction start! reqId = " + this.mExtractionRequest.mRequestId + "  Cropped area = " + (cropRect == null ? "null" : cropRect.toString()) + "  Package = " + this.mPackageName);
        this.mIsExtractingData = true;
        this.mSmartClipDataRepository = new SmartClipDataRepositoryImpl(this, this.mWinFrameRect, this.mScaleRect, this.mPenWindowBorderWidth);
        SmartClipDataElementImpl rootElement = this.mSmartClipDataRepository.getRootElement();
        this.mViewPositionCache.clear();
        if (this.mExtractionRequest.mExtractionMode == 2) {
            traverseViewForDragAndDrop(rootView, croppedArea, this.mSmartClipDataRepository, rootElement);
        } else {
            traverseView(rootView, croppedArea, this.mSmartClipDataRepository, rootElement);
        }
        this.mViewPositionCache.clear();
        addAppMetaTag(rootElement);
        this.mSmartClipDataRepository.setAppPackageName(this.mPackageName);
        this.mIsExtractingData = false;
        if (this.mPendingElements.size() == 0) {
            this.mSmartClipDataRepository.determineContentType();
            sendExtractionResultToSmartClipService();
        }
        return true;
    }

    protected void addAppMetaTag(SlookSmartClipDataElement element) {
        if (this.mContext == null) {
            Log.e(TAG, "addAppMetaTag : mContext is null!");
            return;
        }
        Log.d(TAG, "addAppMetaTag : package name is " + this.mPackageName);
        element.addTag(new SmartClipMetaTagImpl(SmartClipMetaTagType.APP_LAUNCH_INFO, this.mPackageName));
    }

    public boolean setPendingExtractionResult(View view, SlookSmartClipDataElement resultElement) {
        int elementIndex = findElementIndexFromPendingList((SmartClipDataElementImpl) resultElement);
        if (elementIndex < 0) {
            return false;
        }
        this.mPendingElements.remove(elementIndex);
        SmartClipDataElementImpl elementImpl = (SmartClipDataElementImpl) resultElement;
        if (!elementImpl.isEmptyTag(false)) {
            if (DEBUG) {
                Log.d(TAG, "setPendingExtractionResult : Contains meta data : " + elementImpl.getDumpString(false, true));
            } else {
                Log.d(TAG, "setPendingExtractionResult : Contains meta data : " + elementImpl.getDumpString(false, false));
            }
        }
        if (this.mPendingElements.size() == 0 && !this.mIsExtractingData) {
            this.mSmartClipDataRepository.determineContentType();
            sendExtractionResultToSmartClipService();
        }
        return true;
    }

    protected ArrayList<View> getParentList(View view) {
        ViewParent cur;
        ArrayList<View> parents = new ArrayList();
        if (view instanceof ViewGroup) {
            cur = (ViewParent) view;
        } else {
            parents.add(view);
            cur = view.getParent();
        }
        while (cur != null) {
            if (cur instanceof ViewGroup) {
                parents.add((View) cur);
            }
            cur = cur.getParent();
        }
        return parents;
    }

    protected int findElementIndexFromPendingList(SmartClipDataElementImpl element) {
        int pendingCount = this.mPendingElements.size();
        for (int i = 0; i < pendingCount; i++) {
            if (this.mPendingElements.get(i) == element) {
                return i;
            }
        }
        return -1;
    }

    protected boolean sendExtractionResultToSmartClipService() {
        if (this.mPendingElements.size() > 0) {
            Log.e(TAG, "Cannot send the extraction result due to it still have pending element!");
            return false;
        } else if (this.mSmartClipDataRepository != null) {
            return sendExtractionResultToSmartClipService(this.mSmartClipDataRepository);
        } else {
            Log.e(TAG, "Cannot send the extraction result due to it is NULL!");
            return false;
        }
    }

    public int getExtractionMode() {
        if (this.mExtractionRequest != null) {
            return this.mExtractionRequest.mExtractionMode;
        }
        return 0;
    }

    public int getExtractionLevel() {
        return this.mExtractionLevel;
    }

    public boolean sendExtractionResultToSmartClipService(SmartClipDataRepositoryImpl result) {
        if (this.mExtractionRequest == null) {
            Log.e(TAG, "sendExtractionResultToSmartClipService : extractionRequest is null!");
            return false;
        }
        if (result != null && this.mExtractionRequest.mExtractionMode == 0) {
            filterMetaTagForBrowserViews(result.getRootElement());
        }
        if (result != null) {
            Log.d(TAG, "sendExtractionResultToSmartClipService : -- Extracted SmartClip data information --");
            Log.d(TAG, "sendExtractionResultToSmartClipService : Request Id : " + this.mExtractionRequest.mRequestId);
            Log.d(TAG, "sendExtractionResultToSmartClipService : Extraction mode : " + this.mExtractionRequest.mExtractionMode);
            result.dump(DEBUG);
        } else {
            Log.e(TAG, "sendExtractionResultToSmartClipService : The repository is null");
        }
        SpenGestureManager spenGestureManager = (SpenGestureManager) this.mContext.getSystemService("spengestureservice");
        SmartClipDataExtractionResponse response = new SmartClipDataExtractionResponse(this.mExtractionRequest.mRequestId, this.mExtractionRequest.mExtractionMode, result);
        if (result != null && this.mExtractionRequest.mTargetWindowLayer >= 0) {
            result.setWindowLayer(this.mExtractionRequest.mTargetWindowLayer);
        }
        try {
            spenGestureManager.sendSmartClipRemoteRequestResult(new SmartClipRemoteRequestResult(this.mExtractionRequest.mRequestId, 1, response));
        } catch (RuntimeException e) {
            Log.e(TAG, "sendExtractionResultToSmartClipService : Failed to send the result! e=" + e);
            Log.e(TAG, "sendExtractionResultToSmartClipService : Send empty response...");
            spenGestureManager.sendSmartClipRemoteRequestResult(new SmartClipRemoteRequestResult(this.mExtractionRequest.mRequestId, 1, null));
        }
        Log.d(TAG, "sendExtractionResultToSmartClipService : Elapsed = " + (System.currentTimeMillis() - this.mExtractionStartTime));
        return true;
    }

    private Rect getScreenRectOfView(View view) {
        Rect screenRectOfView = new Rect();
        Point screenPointOfView = getScreenPointOfView(view);
        screenRectOfView.left = screenPointOfView.x;
        screenRectOfView.top = screenPointOfView.y;
        screenRectOfView.right = screenRectOfView.left + view.getWidth();
        screenRectOfView.bottom = screenRectOfView.top + view.getHeight();
        return screenRectOfView;
    }

    private Point getScreenPointOfView(View view) {
        Point point = null;
        if (this.mUseViewPositionCache) {
            point = (Point) this.mViewPositionCache.get(view);
        }
        if (point == null) {
            point = new Point();
            int[] screenOffsetOfView = new int[2];
            view.getLocationOnScreen(screenOffsetOfView);
            point.x = screenOffsetOfView[0];
            point.y = screenOffsetOfView[1];
            if (this.mUseViewPositionCache) {
                this.mViewPositionCache.put(view, point);
            }
        }
        return point;
    }

    public int extractDefaultSmartClipData(View view, SmartClipDataElementImpl resultElement, SlookSmartClipCroppedArea croppedArea) {
        if (resultElement == null) {
            Log.e(TAG, "extractDefaultSmartClipData : The result element is null!");
            return 0;
        } else if (croppedArea == null) {
            Log.e(TAG, "extractDefaultSmartClipData : The cropped area is null!");
            return 0;
        } else {
            try {
                String viewClassName = view.getClass().getName();
                if (this.mPackageName.equals(YOUTUBE_PACKAGE_NAME) && viewClassName.endsWith("PlayerView")) {
                    return extractDefaultSmartClipData_YoutubePlayerView(view, resultElement, croppedArea);
                }
                if (this.mChromeBrowserContentViewName != null && SmartClipMetaUtils.isInstanceOf(view, this.mChromeBrowserContentViewName)) {
                    Log.d(TAG, "extractDefaultSmartClipData : Has chrome view");
                    return extractDefaultSmartClipData_GoogleChromeView(view, resultElement, croppedArea);
                } else if (viewClassName.equals("org.chromium.content.browser.JellyBeanContentView")) {
                    return extractDefaultSmartClipData_GoogleChromeView(view, resultElement, croppedArea);
                } else {
                    if (view instanceof TextView) {
                        return extractDefaultSmartClipData_TextView(view, resultElement, croppedArea);
                    }
                    if (view instanceof ImageView) {
                        return extractDefaultSmartClipData_ImageView(view, resultElement, croppedArea);
                    }
                    if (view instanceof TextureView) {
                        return extractDefaultSmartClipData_TextureView(view, resultElement, croppedArea);
                    }
                    return 1;
                }
            } catch (ClassCastException e) {
                Toast.makeText(view.getContext(), "ClassCastException in traverseView : target class is " + view.toString(), 1).show();
                e.printStackTrace();
            }
        }
    }

    public int extractDefaultSmartClipData_YoutubePlayerView(View view, SmartClipDataElementImpl resultElement, SlookSmartClipCroppedArea croppedArea) {
        return 1;
    }

    public int extractDefaultSmartClipData_TextView(View view, SmartClipDataElementImpl resultElement, SlookSmartClipCroppedArea croppedArea) {
        if (resultElement.getTag("plain_text").size() == 0) {
            TextView textView = (TextView) view;
            TransformationMethod transformationMethod = textView.getTransformationMethod();
            if (transformationMethod == null || !(transformationMethod instanceof PasswordTransformationMethod)) {
                CharSequence charSequence = textView.getText();
                if (charSequence == null) {
                    charSequence = "";
                }
                if (this.mExtractionRequest != null && this.mExtractionRequest.mExtractionMode == 2) {
                    Rect spanRect = textView.getSpannedTextRect(croppedArea.getRect());
                    if (spanRect != null) {
                        resultElement.setMetaAreaRect(spanRect);
                        charSequence = "";
                    }
                    if (textView.hasSelection()) {
                        int selStart = textView.getSelectionStart();
                        int selEnd = textView.getSelectionEnd();
                        CharSequence selectedText = charSequence.subSequence(Math.max(0, Math.min(selStart, selEnd)), Math.max(0, Math.max(selStart, selEnd)));
                        if (selectedText != null) {
                            resultElement.addTag(new SlookSmartClipMetaTag(SmartClipMetaTagType.TEXT_SELECTION, selectedText.toString()));
                        }
                    }
                }
                resultElement.addTag(new SlookSmartClipMetaTag("plain_text", charSequence.toString()));
            }
        }
        return 1;
    }

    public int extractDefaultSmartClipData_ImageView(View view, SmartClipDataElementImpl resultElement, SlookSmartClipCroppedArea croppedArea) {
        if (resultElement.getTag("plain_text").size() == 0) {
            ImageView imageView = (ImageView) view;
            if (!(imageView.getDrawable() == null && imageView.getBackground() == null)) {
                resultElement.addTag(new SlookSmartClipMetaTag("plain_text", ""));
            }
        }
        return 1;
    }

    public int extractDefaultSmartClipData_TextureView(View view, SmartClipDataElementImpl resultElement, SlookSmartClipCroppedArea croppedArea) {
        if (resultElement.getTag("plain_text").size() == 0) {
            resultElement.addTag(new SlookSmartClipMetaTag("plain_text", ""));
        }
        return 1;
    }

    public int extractDefaultSmartClipData_GoogleChromeView(View view, SmartClipDataElementImpl resultElement, SlookSmartClipCroppedArea croppedArea) {
        try {
            Method extractSmartClipDataMethod = view.getClass().getMethod("extractSmartClipData", new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE});
            final Method setSmartClipResultHandlerMethod = view.getClass().getMethod("setSmartClipResultHandler", new Class[]{Handler.class});
            if (!(extractSmartClipDataMethod == null || setSmartClipResultHandlerMethod == null)) {
                Log.d(TAG, "Extracting meta data from Chrome view...");
                final SmartClipDataElementImpl smartClipDataElementImpl = resultElement;
                final View view2 = view;
                Handler handler = new Handler() {
                    public SlookSmartClipDataElement mResult = smartClipDataElementImpl;

                    public void handleMessage(Message msg) {
                        Log.d(SmartClipDataCropperImpl.TAG, "Meta data arrived from chrome");
                        Bundle bundle = msg.getData();
                        if (bundle == null) {
                            Log.e(SmartClipDataCropperImpl.TAG, "The bundle is null!");
                            SmartClipDataCropperImpl.this.setPendingExtractionResult(view2, this.mResult);
                            return;
                        }
                        String title = bundle.getString("title");
                        String url = bundle.getString("url");
                        String html = bundle.getString(SmartClipMetaTagType.HTML);
                        String text = bundle.getString("text");
                        Rect area = (Rect) bundle.getParcelable("rect");
                        if (SmartClipDataCropperImpl.DEBUG) {
                            Log.d(SmartClipDataCropperImpl.TAG, String.format("Title:%s\nURL:%s\nArea:%s\nText:%s\nHTML:%s", new Object[]{title, url, area, text, html}));
                        }
                        if (!title.isEmpty()) {
                            smartClipDataElementImpl.setTag(new SlookSmartClipMetaTag("title", title));
                        }
                        if (!url.isEmpty()) {
                            smartClipDataElementImpl.setTag(new SlookSmartClipMetaTag("url", url));
                        }
                        if (!html.isEmpty()) {
                            smartClipDataElementImpl.setTag(new SlookSmartClipMetaTag(SmartClipMetaTagType.HTML, html));
                        }
                        if (!text.isEmpty()) {
                            smartClipDataElementImpl.setTag(new SlookSmartClipMetaTag("plain_text", text));
                        }
                        if (area != null) {
                            DisplayMetrics metrics = SmartClipDataCropperImpl.this.mContext.getResources().getDisplayMetrics();
                            area.left = (int) TypedValue.applyDimension(1, (float) area.left, metrics);
                            area.top = (int) TypedValue.applyDimension(1, (float) area.top, metrics);
                            area.right = (int) TypedValue.applyDimension(1, (float) area.right, metrics);
                            area.bottom = (int) TypedValue.applyDimension(1, (float) area.bottom, metrics);
                            Rect screenRectOfView = SmartClipDataCropperImpl.this.getScreenRectOfView(view2);
                            area.offset(screenRectOfView.left, screenRectOfView.top);
                            area.intersect(screenRectOfView);
                            smartClipDataElementImpl.setMetaAreaRect(area);
                        }
                        try {
                            setSmartClipResultHandlerMethod.invoke(view2, new Object[]{(Handler) null});
                        } catch (Exception e) {
                            Log.e(SmartClipDataCropperImpl.TAG, "Could not invoke set smartclip handler API");
                            e.printStackTrace();
                        }
                        SmartClipDataCropperImpl.this.setPendingExtractionResult(view2, this.mResult);
                    }
                };
                Rect cropRect = new Rect(croppedArea.getRect());
                int[] screenPosOfView = new int[2];
                view.getLocationOnScreen(screenPosOfView);
                cropRect.offset(-screenPosOfView[0], -screenPosOfView[1]);
                setSmartClipResultHandlerMethod.invoke(view, new Object[]{handler});
                if (DEBUG) {
                    Log.d(TAG, "Converting coordinate : " + croppedArea.getRect().toString() + " -> " + cropRect.toString());
                }
                extractSmartClipDataMethod.invoke(view, new Object[]{Integer.valueOf(cropRect.left), Integer.valueOf(cropRect.top), Integer.valueOf(cropRect.width()), Integer.valueOf(cropRect.height())});
                return 2;
            }
        } catch (Exception e) {
            Log.e(TAG, "Current chrome view does not support smartclip");
        }
        try {
            String str = "url";
            SmartClipDataElementImpl smartClipDataElementImpl2 = resultElement;
            smartClipDataElementImpl2.setTag(new SlookSmartClipMetaTag(str, (String) view.getClass().getMethod("getUrl", new Class[0]).invoke(view, new Object[0])));
            str = "title";
            smartClipDataElementImpl2 = resultElement;
            smartClipDataElementImpl2.setTag(new SlookSmartClipMetaTag(str, (String) view.getClass().getMethod("getTitle", new Class[0]).invoke(view, new Object[0])));
            if (this.mExtractionRequest != null) {
                if (this.mExtractionRequest.mExtractionMode == 0) {
                    resultElement.setMetaAreaRect(croppedArea.getRect());
                } else if (this.mExtractionRequest.mExtractionMode == 2) {
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return 1;
    }

    private int getMainResultFromExtractionResult(int extractionResult) {
        return extractionResult & 255;
    }

    private Rect adjustMetaAreaRect(View view, Rect rect) {
        Rect screenRectOfView = getScreenRectOfView(view);
        Rect intersection = new Rect();
        if (rect == null) {
            Log.e(TAG, "adjustMetaAreaRect : rect is null");
            return null;
        }
        for (ViewParent curView = view.getParent(); curView != null; curView = curView.getParent()) {
            if (curView instanceof ViewGroup) {
                Rect parentViewRect = getScreenRectOfView((View) curView);
                Rect intersectionWithParentView = new Rect();
                if (intersectionWithParentView.setIntersect(screenRectOfView, parentViewRect)) {
                    screenRectOfView = intersectionWithParentView;
                }
            }
        }
        if (intersection.setIntersect(rect, screenRectOfView)) {
            return intersection;
        }
        Log.e(TAG, "adjustMetaAreaRect : there is no intersection " + rect + " and " + screenRectOfView);
        return null;
    }

    private Rect getOpaqueBackgroundRect(SmartClipDataElementImpl element) {
        SmartClipDataElementImpl curElement = element;
        Rect opaqueRect = null;
        while (curElement != null) {
            View view = curElement.getView();
            if (view != null) {
                Drawable background = view.getBackground();
                if (!(background == null || !background.isVisible() || background.getOpacity() == -2)) {
                    Rect metaRect = curElement.getMetaAreaRect();
                    if (metaRect != null) {
                        Rect adjustedRect = adjustMetaAreaRect(view, metaRect);
                        if (adjustedRect != null) {
                            if (opaqueRect == null) {
                                opaqueRect = new Rect(adjustedRect);
                            } else {
                                opaqueRect.union(adjustedRect);
                            }
                        }
                    }
                }
            }
            curElement = curElement.traverseNextElement(element);
        }
        Log.d(TAG, "getOpaqueBackgroundRect : opaqueRect=" + opaqueRect + "  element=" + element);
        return opaqueRect;
    }

    private boolean removeSmartClipDataElementByRect(SmartClipDataElementImpl element, Rect rectToDelete) {
        SmartClipDataElementImpl child = element.getLastChild();
        while (child != null) {
            SmartClipDataElementImpl cur = child;
            child = child.getPrevSibling();
            removeSmartClipDataElementByRect(cur, rectToDelete);
        }
        if (element.getFirstChild() == null) {
            Rect metaAreaRect = element.getMetaAreaRect();
            if (element.isEmptyTag(false)) {
                element.getParent().removeChild(element);
                return true;
            } else if (metaAreaRect != null && Rect.intersects(rectToDelete, metaAreaRect)) {
                Log.d(TAG, "removeSmartClipDataElementByRect : Removing element due to RECT intersection. element = " + element.getDumpString(false, true));
                element.getParent().removeChild(element);
                return true;
            }
        }
        return false;
    }

    private void filterMetaTagForBrowserViews(SmartClipDataElementImpl element) {
        if (element == null) {
            Log.e(TAG, "filterMetaTagForBrowserViews : element is null!");
            return;
        }
        SmartClipDataElementImpl curElement = element;
        while (curElement != null) {
            SlookSmartClipMetaTagArray tags = curElement.getTagTable();
            if (tags != null) {
                Iterator i$;
                SlookSmartClipMetaTag curTag;
                View view = curElement.getView();
                String viewName = view != null ? view.getClass().getSimpleName() : "null";
                int htmlTagCount = tags.getTag(SmartClipMetaTagType.HTML).size();
                int textTagCount = tags.getTag("plain_text").size();
                if (htmlTagCount > 0 && textTagCount > 0) {
                    switch (this.mExtractionLevel) {
                        case 0:
                            tags.removeTag(SmartClipMetaTagType.HTML);
                            Log.d(TAG, "filterMetaTagForBrowserViews : Discarding HTML tag from " + viewName);
                            break;
                        default:
                            i$ = tags.iterator();
                            while (i$.hasNext()) {
                                curTag = (SlookSmartClipMetaTag) i$.next();
                                if ("plain_text".equals(curTag.getType())) {
                                    curTag.setType(SmartClipMetaTagType.HTML_TEXT);
                                }
                            }
                            Log.d(TAG, "filterMetaTagForBrowserViews : The TEXT tag changed to HTML_TEXT. View=" + viewName);
                            break;
                    }
                }
                i$ = tags.iterator();
                while (i$.hasNext()) {
                    curTag = (SlookSmartClipMetaTag) i$.next();
                    if (SmartClipMetaTagType.HTML.equals(curTag.getType())) {
                        String value = curTag.getValue();
                        if (value.length() > MAX_META_VALUE_SIZE) {
                            Log.e(TAG, "filterMetaTagForBrowserViews : Have large HTML data(" + value.length() + " bytes). Converting tag..");
                            String filePathName = allocateMetaTagFilePath();
                            if (writeStringToFile(filePathName, value)) {
                                Log.d(TAG, "filterMetaTagForBrowserViews : Saved the meta tag to " + filePathName);
                            } else {
                                Log.e(TAG, "filterMetaTagForBrowserViews : Failed to save meta tag! - " + filePathName);
                            }
                            curTag.setType(SmartClipMetaTagType.FILE_PATH_HTML);
                            curTag.setValue(filePathName);
                        }
                    }
                }
            }
            curElement = curElement.traverseNextElement(element);
        }
    }

    private boolean writeStringToFile(String filePathName, String value) {
        Exception e;
        Throwable th;
        boolean ret = true;
        Log.d(TAG, "writeStringToFile : " + filePathName);
        File file = new File(filePathName);
        FileOutputStream os = null;
        try {
            FileOutputStream os2 = new FileOutputStream(file);
            try {
                os2.write(value.getBytes());
                if (os2 != null) {
                    try {
                        os2.close();
                        os = os2;
                    } catch (Exception e2) {
                        Log.e(TAG, "writeStringToFile : File close failed! " + e2);
                        ret = false;
                        os = os2;
                    }
                }
            } catch (Exception e3) {
                e2 = e3;
                os = os2;
                try {
                    Log.e(TAG, "writeStringToFile : File write failed! " + e2);
                    ret = false;
                    if (os != null) {
                        try {
                            os.close();
                        } catch (Exception e22) {
                            Log.e(TAG, "writeStringToFile : File close failed! " + e22);
                            ret = false;
                        }
                    }
                    file.setReadable(true, false);
                    file.setWritable(true, false);
                    return ret;
                } catch (Throwable th2) {
                    th = th2;
                    if (os != null) {
                        try {
                            os.close();
                        } catch (Exception e222) {
                            Log.e(TAG, "writeStringToFile : File close failed! " + e222);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                os = os2;
                if (os != null) {
                    os.close();
                }
                throw th;
            }
        } catch (Exception e4) {
            e222 = e4;
            Log.e(TAG, "writeStringToFile : File write failed! " + e222);
            ret = false;
            if (os != null) {
                os.close();
            }
            file.setReadable(true, false);
            file.setWritable(true, false);
            return ret;
        }
        file.setReadable(true, false);
        file.setWritable(true, false);
        return ret;
    }

    private String allocateMetaTagFilePath() {
        File baseDir = new File(this.mContext.getFilesDir().getAbsolutePath() + "/smartclip");
        if (!baseDir.exists()) {
            baseDir.mkdir();
            baseDir.setWritable(true, false);
            baseDir.setReadable(true, false);
            baseDir.setExecutable(true, false);
        }
        this.mLastMetaFileId++;
        return String.format("%s/SC%02d", new Object[]{baseDirPath, Integer.valueOf(this.mLastMetaFileId)});
    }

    private boolean traverseView(View view, SlookSmartClipCroppedArea croppedArea, SmartClipDataRepositoryImpl smartClipDataRepository, SmartClipDataElementImpl parentSlookSmartClipDataElement) {
        boolean haveCroppedView = false;
        if (view != null && view.getVisibility() == 0 && view.getWidth() > 0 && view.getHeight() > 0) {
            Rect screenRectOfView = getScreenRectOfView(view);
            if (Rect.intersects(croppedArea.getRect(), screenRectOfView)) {
                int extractionResult;
                SmartClipDataElementImpl element = new SmartClipDataElementImpl(smartClipDataRepository, view, screenRectOfView);
                SmartClipMetaTagArrayImpl defaultViewTags = view.getSmartClipTags();
                if (defaultViewTags != null) {
                    element.setTagTable(defaultViewTags.getCopy());
                }
                SmartClipDataExtractionListener listener = view.getSmartClipDataExtractionListener();
                if (listener != null) {
                    extractionResult = listener.onExtractSmartClipData(view, element, croppedArea);
                } else {
                    extractionResult = view.extractSmartClipData(element, croppedArea);
                }
                for (SmartClipDataElementImpl elementTraveler = element; elementTraveler != null; elementTraveler = elementTraveler.traverseNextElement(element)) {
                    elementTraveler.setMetaAreaRect(adjustMetaAreaRect(view, elementTraveler.getMetaAreaRect()));
                }
                int mainResult = getMainResultFromExtractionResult(extractionResult);
                switch (mainResult) {
                    case 0:
                        element.clearMetaData();
                        break;
                    case 1:
                        break;
                    case 2:
                        this.mPendingElements.add(element);
                        haveCroppedView = true;
                        break;
                    default:
                        Log.e(TAG, "Unknown main extraction result value : " + mainResult + " / View = " + view.toString());
                        element.clearMetaData();
                        break;
                }
                boolean skipExtractionFromChildView = (extractionResult & 256) != 0;
                if ((view instanceof ViewGroup) && !skipExtractionFromChildView) {
                    ViewGroup vg = (ViewGroup) view;
                    int childCount = vg.getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        if (traverseView(vg.getChildAt(i), croppedArea, smartClipDataRepository, element)) {
                            haveCroppedView = true;
                        }
                    }
                }
                if (!element.isEmptyTag(true)) {
                    haveCroppedView = true;
                }
                if (!element.isEmptyTag(false)) {
                    if (DEBUG) {
                        Log.d(TAG, "traverseView : Contains meta data : " + element.getDumpString(false, true));
                    } else {
                        Log.d(TAG, "traverseView : Contains meta data : " + element.getDumpString(false, false));
                    }
                }
                if (haveCroppedView) {
                    if ((view instanceof FrameLayout) || (view instanceof RelativeLayout)) {
                        SmartClipDataElementImpl childElement = element.getLastChild();
                        Rect opaqueRect = null;
                        while (childElement != null) {
                            boolean isCurElementRemoved = false;
                            SmartClipDataElementImpl curElement = childElement;
                            childElement = childElement.getPrevSibling();
                            if (opaqueRect != null) {
                                isCurElementRemoved = removeSmartClipDataElementByRect(curElement, opaqueRect);
                            }
                            if (!isCurElementRemoved) {
                                Rect curOpaqueRect = getOpaqueBackgroundRect(curElement);
                                if (curOpaqueRect != null) {
                                    if (opaqueRect == null) {
                                        opaqueRect = curOpaqueRect;
                                    } else {
                                        opaqueRect.union(curOpaqueRect);
                                    }
                                }
                            }
                        }
                    }
                    parentSlookSmartClipDataElement.addChild(element);
                }
            }
        }
        return haveCroppedView;
    }

    private boolean traverseViewForDragAndDrop(View view, SlookSmartClipCroppedArea croppedArea, SmartClipDataRepositoryImpl smartClipDataRepository, SmartClipDataElementImpl parentSlookSmartClipDataElement) {
        boolean haveCroppedView = false;
        if (view != null && view.getVisibility() == 0 && view.getWidth() > 0 && view.getHeight() > 0) {
            Rect screenRectOfView = getScreenRectOfView(view);
            if (Rect.intersects(croppedArea.getRect(), screenRectOfView)) {
                int extractionResult;
                SmartClipDataElementImpl element = new SmartClipDataElementImpl(smartClipDataRepository, view, screenRectOfView);
                boolean needToCallListener = false;
                SmartClipDataExtractionListener listener = view.getSmartClipDataExtractionListener();
                if (listener != null && (view instanceof SurfaceView)) {
                    needToCallListener = true;
                }
                if (needToCallListener) {
                    extractionResult = listener.onExtractSmartClipData(view, element, croppedArea);
                } else {
                    extractionResult = view.extractSmartClipData(element, croppedArea);
                }
                for (SmartClipDataElementImpl elementTraveler = element; elementTraveler != null; elementTraveler = elementTraveler.traverseNextElement(element)) {
                    elementTraveler.setMetaAreaRect(adjustMetaAreaRect(view, elementTraveler.getMetaAreaRect()));
                }
                int mainResult = getMainResultFromExtractionResult(extractionResult);
                switch (mainResult) {
                    case 0:
                        element.clearMetaData();
                        break;
                    case 1:
                        break;
                    case 2:
                        this.mPendingElements.add(element);
                        haveCroppedView = true;
                        break;
                    default:
                        Log.e(TAG, "Unknown main extraction result value : " + mainResult + " / View = " + view.toString());
                        element.clearMetaData();
                        break;
                }
                boolean skipExtractionFromChildView = (extractionResult & 256) != 0;
                if ((view instanceof ViewGroup) && !skipExtractionFromChildView) {
                    ViewGroup vg = (ViewGroup) view;
                    int i = vg.getChildCount() - 1;
                    while (i >= 0) {
                        if (traverseViewForDragAndDrop(vg.getChildAt(i), croppedArea, smartClipDataRepository, element)) {
                            haveCroppedView = true;
                        } else {
                            i--;
                        }
                    }
                }
                if (!element.isEmptyTag(true)) {
                    haveCroppedView = true;
                }
                if (haveCroppedView) {
                    parentSlookSmartClipDataElement.addChild(element);
                }
            }
        }
        return haveCroppedView;
    }
}
