package com.samsung.android.hermes;

import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.HoverableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import com.samsung.android.hermes.IKerykeion.Stub;
import com.samsung.android.hermes.object.HermesObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HermesServiceManager {
    private static final String DIVIDER = ",";
    private static int EVENT_PERIOD = 1;
    public static final int GET_LINKS = 1;
    public static final int GET_TAGS = 4;
    private static final int HERMES_EVENT = 1;
    private static final String HTTP_SCHEME = "http";
    private static final String JSON_CONTENTS = "contents";
    private static final int JSON_EVENT = 2;
    public static final int RECOMMAND_APP = 16;
    public static final int RECOMMAND_CONTENTS = 8;
    private static String TAG = "HermesServiceManager";
    private static final Pattern emailPattern = Patterns.EMAIL_ADDRESS;
    private static Context mContext = null;
    private static IKerykeion mService = null;
    private static final Pattern urlPattern = Patterns.WEB_URL;
    public IHermesCallBack mIHermesCallBack = null;

    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$samsung$android$hermes$HermesServiceManager$AnalyzerResultType = new int[AnalyzerResultType.values().length];

        static {
            try {
                $SwitchMap$com$samsung$android$hermes$HermesServiceManager$AnalyzerResultType[AnalyzerResultType.Telnum.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$samsung$android$hermes$HermesServiceManager$AnalyzerResultType[AnalyzerResultType.Name.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$samsung$android$hermes$HermesServiceManager$AnalyzerResultType[AnalyzerResultType.Contact_id.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$samsung$android$hermes$HermesServiceManager$AnalyzerResultType[AnalyzerResultType.Date.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$samsung$android$hermes$HermesServiceManager$AnalyzerResultType[AnalyzerResultType.Time.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$samsung$android$hermes$HermesServiceManager$AnalyzerResultType[AnalyzerResultType.Event.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$samsung$android$hermes$HermesServiceManager$AnalyzerResultType[AnalyzerResultType.Event_id.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$samsung$android$hermes$HermesServiceManager$AnalyzerResultType[AnalyzerResultType.Email.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$samsung$android$hermes$HermesServiceManager$AnalyzerResultType[AnalyzerResultType.Url.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$samsung$android$hermes$HermesServiceManager$AnalyzerResultType[AnalyzerResultType.Place.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$samsung$android$hermes$HermesServiceManager$AnalyzerResultType[AnalyzerResultType.Poi.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$samsung$android$hermes$HermesServiceManager$AnalyzerResultType[AnalyzerResultType.Hotkeyword.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$samsung$android$hermes$HermesServiceManager$AnalyzerResultType[AnalyzerResultType.Original.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
        }
    }

    public enum AnalyzerResultType {
        Unknown,
        Event,
        Event_id,
        Name,
        Contact_id,
        Telnum,
        Place,
        Poi,
        Email,
        Url,
        Hotkeyword,
        NonLikableTypeDivider,
        Date,
        Time,
        Tag,
        Color,
        Uri,
        Document,
        ScreenShot,
        PeekEvent,
        Original,
        Date_Time
    }

    private enum AppType {
        contact,
        pim,
        email,
        browser,
        map,
        news,
        recommand_tag,
        recommand_text
    }

    public class HermesClickSpannable extends ClickableSpan {
        KerykeionResult mResult = null;

        public HermesClickSpannable(KerykeionResult kResult) {
            this.mResult = kResult;
        }

        public void updateDrawState(TextPaint ds) {
            ds.setColor(-16776961);
            ds.setUnderlineText(true);
            super.updateDrawState(ds);
        }

        public void onClick(View arg0) {
            Log.d(HermesServiceManager.TAG, "Spannable type : " + this.mResult.getResultType());
            Log.d(HermesServiceManager.TAG, "Spannable String : " + this.mResult.getAdaptableData());
            Rect rect = new Rect();
            arg0.getGlobalVisibleRect(rect);
            HermesServiceManager.this.startHermesTickerService(HermesServiceManager.this.makeJson(new ArrayList(Arrays.asList(new KerykeionResult[]{this.mResult}))), rect);
        }
    }

    public class HermesEvent {
        private String description = null;
        private long endTime = 0;
        private String location = null;
        private long startTime = 0;

        public HermesEvent(long startTime, long endTime, String location, String description) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.location = location;
            this.description = description;
        }

        public long getStartTime() {
            return this.startTime;
        }

        public long getEndTime() {
            return this.endTime;
        }

        public String getLocation() {
            return this.location;
        }

        public String getDescription() {
            return this.description;
        }
    }

    public class HermesHoverSpannable extends HoverableSpan {
        KerykeionResult mResult = null;

        public HermesHoverSpannable(KerykeionResult kResult) {
            this.mResult = kResult;
        }

        public void updateDrawState(TextPaint ds) {
            ds.setColor(-16776961);
            ds.setUnderlineText(true);
            super.updateDrawState(ds);
        }

        public void onHoverEnter(View widget) {
            Log.d(HermesServiceManager.TAG, "Hovered");
            Rect rect = new Rect();
            widget.getGlobalVisibleRect(rect);
            HermesServiceManager.this.startHermesTickerService(HermesServiceManager.this.makeJson(new ArrayList(Arrays.asList(new KerykeionResult[]{this.mResult}))), rect);
        }

        public void onHoverExit(View widget) {
        }
    }

    public class HermesResult {
        private Object data = null;
        private HermesEvent event = null;
        private int id = 0;

        public HermesResult(int id, Object data) {
            this.id = id;
            this.data = data;
            this.event = null;
        }

        public HermesResult(int id, Object data, HermesEvent event) {
            this.id = id;
            this.data = data;
            this.event = event;
        }

        public int getId() {
            return this.id;
        }

        private void setId(int id) {
            this.id = id;
        }

        public Object getData() {
            return this.data;
        }

        private void setData(Object obj) {
            this.data = obj;
        }

        public HermesEvent getEvent() {
            return this.event;
        }

        private void setEvent(HermesEvent event) {
            this.event = event;
        }
    }

    public interface IHermesCallBack {
        void onCompleted(Object obj);
    }

    public static class PatternType {
        public static final int ALL = 31;
        public static final int DATE_TIME = 1;
        public static final int DETAIL_ALL = 29;
        public static final int EMAIL = 2;
        public static final int LINKIFY_ALL = 30;
        public static final int MAP = 16;
        public static final int NOT_DETERMINE = 0;
        public static final int PHONE_NUMBER = 8;
        public static final int URL = 4;
    }

    public HermesServiceManager(Context context) throws IllegalStateException {
        Log.d(TAG, "HermesServiceManager");
        mContext = context;
        if (getService() == null) {
            throw new IllegalStateException("Could not get the hermes service.");
        }
    }

    public void setHermesCallBack(IHermesCallBack i) {
        this.mIHermesCallBack = i;
    }

    private synchronized IKerykeion getService() {
        IKerykeion iKerykeion;
        if (mService == null) {
            mService = Stub.asInterface(ServiceManager.getService("hermesservice"));
            if (mService == null) {
                Log.e(TAG, "getService : Could not get the service!");
                iKerykeion = null;
            }
            if (mService != null) {
                Log.d(TAG, "getService : " + mService.toString());
            }
            iKerykeion = mService;
        } else {
            if (!mService.asBinder().isBinderAlive()) {
                mService = Stub.asInterface(ServiceManager.getService("hermesservice"));
            }
            if (mService != null) {
                Log.d(TAG, "getService : " + mService.toString());
            }
            iKerykeion = mService;
        }
        return iKerykeion;
    }

    public void analysis(int type, Object obj) throws IllegalArgumentException, IllegalStateException {
        analysis(type, mContext.toString(), 29, obj);
    }

    public void analysis(int type, Object obj, int uniqueId) throws IllegalArgumentException, IllegalStateException {
        analysis(type, Integer.valueOf(uniqueId), 29, obj);
    }

    public void analysis(int type, String source, Uri uri, int uniqueId) throws IllegalArgumentException, IllegalStateException {
        List<Object> sources = new ArrayList();
        sources.add(source);
        sources.add(uri);
        analysis(type, uniqueId == 0 ? mContext.toString() : Integer.valueOf(uniqueId), 29, sources);
    }

    public void analysis(int type, Object obj, int uniqueId, int PatternType) throws IllegalArgumentException, IllegalStateException {
        analysis(type, Integer.valueOf(uniqueId), PatternType, obj);
    }

    private void analysis(int type, Object uniqueId, int pType, Object... source) throws IllegalArgumentException, IllegalStateException {
        checkParamValidation(type, pType, source);
        final IKerykeion svc = getService();
        if (svc == null) {
            throw new IllegalStateException("Could not get the hermes service.");
        }
        String tempKey;
        if (uniqueId instanceof Integer) {
            tempKey = String.valueOf((Integer) uniqueId);
        } else if (uniqueId instanceof String) {
            tempKey = (String) uniqueId;
        } else {
            tempKey = uniqueId.toString();
        }
        final String key = tempKey;
        if (svc != null) {
            try {
                Log.d(TAG, " key : " + key);
                final Object obj = uniqueId;
                final int i = type;
                svc.setIKerykeionCallBack(key, new IKerykeionCallBack.Stub() {
                    public void onCompleted(List<KerykeionResult> results) throws RemoteException {
                        if (HermesServiceManager.this.mIHermesCallBack == null || !(results == null || results.isEmpty())) {
                            HermesResult lResult = new HermesResult();
                            lResult.setId(obj instanceof Integer ? ((Integer) obj).intValue() : 0);
                            if (!((i & 1) == 0 && (i & 8) == 0)) {
                                List<KerykeionResult> destResults = new ArrayList();
                                String original = "";
                                for (KerykeionResult kR : results) {
                                    if (AnalyzerResultType.Original.ordinal() == kR.getResultType()) {
                                        original = kR.getSrc();
                                    }
                                    destResults.add(kR);
                                }
                                lResult.setData(destResults);
                                lResult.setEvent((HermesEvent) HermesServiceManager.this.extractEvent(results, original, 1));
                            }
                            if ((i & 4) != 0) {
                                StringBuilder sb = new StringBuilder();
                                for (KerykeionResult kR2 : results) {
                                    if (kR2.getResult() instanceof String) {
                                        sb.append((String) kR2.getResult());
                                        sb.append(",");
                                    } else if (kR2.getResult() instanceof Integer) {
                                        sb.append("Color:" + kR2.getResult());
                                        sb.append(",");
                                    }
                                }
                                lResult.setData(sb.toString());
                            }
                            if (HermesServiceManager.this.mIHermesCallBack != null) {
                                Log.secD(HermesServiceManager.TAG, "-----------------Complete callback----------------");
                                HermesServiceManager.this.mIHermesCallBack.onCompleted(lResult);
                            }
                            svc.stop(key);
                            return;
                        }
                        HermesServiceManager.this.mIHermesCallBack.onCompleted(null);
                        svc.stop(key);
                    }
                });
                svc.start(key, makeRequestList(type, pType, source), null);
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.secD(TAG, "RemoteException throw new IllegalStateException()");
                throw new IllegalStateException("Failed to create IKerykeionCallBack.");
            }
        }
    }

    public void getSpannableString(int type, String str, int viewId, Rect rect) throws IllegalArgumentException, IllegalStateException {
        getSpannableString(type, str, viewId, rect, 0);
    }

    public void getSpannableString(int type, String str, int viewId, Rect rect, int nAutoLinkMask) throws IllegalArgumentException, IllegalStateException {
        Log.d(TAG, "analysis");
        int nPatterType = getLinkifyToPatternType(nAutoLinkMask);
        checkParamValidation(type, nPatterType, str);
        final String key = String.valueOf(viewId);
        final IKerykeion svc = getService();
        if (svc == null) {
            throw new IllegalStateException("Could not get the hermes service.");
        } else if (svc != null) {
            try {
                Log.d(TAG, " View Id : " + viewId);
                final int i = type;
                final String str2 = str;
                final int i2 = nAutoLinkMask;
                final int i3 = viewId;
                svc.setIKerykeionCallBack(key, new IKerykeionCallBack.Stub() {
                    public void onCompleted(List<KerykeionResult> results) throws RemoteException {
                        if (HermesServiceManager.this.mIHermesCallBack == null || !(results == null || results.isEmpty())) {
                            if ((i & 1) != 0) {
                                SpannableString spanStr = new SpannableString(str2);
                                for (KerykeionResult kR : results) {
                                    int start = kR.getStartPos();
                                    int end = kR.getEndPos();
                                    int type = kR.getResultType();
                                    if (AnalyzerResultType.Url.ordinal() == type) {
                                        spanStr.setSpan(new HermesHoverSpannable(kR), start, end, 33);
                                    } else if (i2 == 0) {
                                        if (HermesServiceManager.this.hoverFilter(type, 0)) {
                                            spanStr.setSpan(new HermesHoverSpannable(kR), start, end, 33);
                                        }
                                    } else if (HermesServiceManager.this.clickFilter(type)) {
                                        spanStr.setSpan(new HermesClickSpannable(kR), start, end, 33);
                                    }
                                }
                                HermesResult lResult = new HermesResult(i3, spanStr);
                                if ((i & 8) != 0) {
                                    HermesEvent event = (HermesEvent) HermesServiceManager.this.extractEvent(results, str2, 1);
                                    if (event != null) {
                                        lResult.setEvent(event);
                                    }
                                }
                                HermesServiceManager.this.mIHermesCallBack.onCompleted(lResult);
                            }
                            svc.stop(key);
                            return;
                        }
                        HermesServiceManager.this.mIHermesCallBack.onCompleted(null);
                        svc.stop(key);
                    }
                });
                svc.start(key, makeRequestList(type, nPatterType, str), new KerykeionPosition(rect));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean hoverFilter(int analyzerType, int patternType) {
        AnalyzerResultType dataType = AnalyzerResultType.values()[analyzerType];
        if (AnalyzerResultType.Event_id.equals(dataType) || AnalyzerResultType.Contact_id.equals(dataType) || AnalyzerResultType.Place.equals(dataType) || AnalyzerResultType.Poi.equals(dataType) || AnalyzerResultType.Url.equals(dataType) || AnalyzerResultType.Hotkeyword.equals(dataType) || !AnalyzerResultType.Unknown.equals(getPatternTpAnalyzerResultType(patternType))) {
            return true;
        }
        return false;
    }

    private boolean clickFilter(int type) {
        AnalyzerResultType dataType = AnalyzerResultType.values()[type];
        if (AnalyzerResultType.Url.equals(dataType) || AnalyzerResultType.Telnum.equals(dataType) || AnalyzerResultType.Email.equals(dataType) || AnalyzerResultType.Place.equals(dataType)) {
            return true;
        }
        return false;
    }

    private Object extractEvent(List<KerykeionResult> results, String description, int returnType) {
        long startTime = 0;
        long endTime = 0;
        String date = null;
        String location = null;
        float curlocationAccuracy = 0.0f;
        boolean hasDate = false;
        boolean hasEvent = false;
        boolean hasLocation = false;
        for (KerykeionResult kR : results) {
            AnalyzerResultType dataType = AnalyzerResultType.values()[kR.getResultType()];
            if (!hasDate && AnalyzerResultType.Date.equals(dataType)) {
                if (kR.getAdaptableData() instanceof String) {
                    date = (String) kR.getAdaptableData();
                    String[] time = date.split(",");
                    if (time.length >= 2) {
                        startTime = Long.valueOf(time[0]).longValue();
                        endTime = Long.valueOf(time[1]).longValue();
                        if (startTime == endTime) {
                            Log.secD(TAG, "Set event period.");
                            endTime += TimeUnit.HOURS.toMillis((long) EVENT_PERIOD);
                        }
                    }
                }
                hasDate = true;
            }
            if ((!hasLocation && AnalyzerResultType.Place.equals(dataType)) || AnalyzerResultType.Poi.equals(dataType)) {
                if (curlocationAccuracy < kR.getAccuracy()) {
                    curlocationAccuracy = kR.getAccuracy();
                    location = (String) kR.getAdaptableData();
                }
                hasLocation = true;
            }
            if (AnalyzerResultType.Event.equals(dataType)) {
                hasEvent = true;
            }
        }
        if ((!hasDate || !hasLocation) && !hasDate && !hasEvent) {
            return null;
        }
        if (returnType == 1) {
            return new HermesEvent(startTime, endTime, location, description);
        }
        if (returnType != 2) {
            return null;
        }
        if (date == null) {
            return null;
        }
        JSONObject app = new JSONObject();
        JSONArray dataArray = new JSONArray();
        dataArray.put(createDataObject(AnalyzerResultType.Date.toString(), date));
        if (location != null) {
            dataArray.put(createDataObject(AnalyzerResultType.Place.toString(), location));
        }
        try {
            app.put(AppType.pim.toString(), dataArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return app;
    }

    private KerykeionRequest makeRequestList(int type, int nPatternType, Object... source) {
        KerykeionRequest kRequest = new KerykeionRequest();
        List<Object> objList = new ArrayList();
        HermesObject hObj = new HermesObject();
        for (Object obj : source) {
            if ((obj instanceof String) || (obj instanceof Uri)) {
                objList.add(obj);
            } else if (obj instanceof HermesObject) {
                hObj.setObject(obj);
            }
        }
        kRequest.setRequestData(type, objList, nPatternType, hObj);
        return kRequest;
    }

    public void showHermes(String source, Rect rect, int type) throws IllegalArgumentException {
        if (source == null) {
            throw new IllegalArgumentException("Argument is wrong.");
        }
        boolean hasHoverData = false;
        int nType = 0;
        Matcher emailMatcher = emailPattern.matcher(source);
        while (emailMatcher.find()) {
            String temp = emailMatcher.group(0);
            if (temp != null && temp.length() > 0) {
                return;
            }
        }
        Matcher urlMatcher = urlPattern.matcher(source);
        while (urlMatcher.find()) {
            source = urlMatcher.group(0);
            nType = AnalyzerResultType.Url.ordinal();
            hasHoverData = true;
        }
        if (hasHoverData) {
            if (new KerykeionResult(nType, source, source, source, 0, 0, 5.0f, 1, 1) != null) {
                startHermesTickerService(makeJson(new ArrayList(Arrays.asList(new KerykeionResult[]{new KerykeionResult(nType, source, source, source, 0, 0, 5.0f, 1, 1)}))), rect);
                return;
            }
            return;
        }
        Log.secD(TAG, "fail to find adaptable parsing data");
    }

    public void showHermes(KerykeionResult kResult, Rect rect) throws NullPointerException {
        if (kResult != null) {
            startHermesTickerService(makeJson(new ArrayList(Arrays.asList(new KerykeionResult[]{kResult}))), rect);
            return;
        }
        throw new NullPointerException("kResult is null.");
    }

    public void showHermes(ArrayList<KerykeionResult> kResultList, Rect rect) throws NullPointerException {
        if (kResultList == null || kResultList.isEmpty()) {
            throw new NullPointerException("kResultList is null or empty.");
        }
        startHermesTickerService(makeJson(kResultList), rect);
    }

    public void dismissHermes() throws IllegalStateException {
        IKerykeion svc = getService();
        if (svc == null) {
            throw new IllegalStateException("Could not get the hermes service.");
        } else if (svc != null) {
            try {
                svc.dismiss();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void training(String source) throws IllegalStateException, IllegalArgumentException {
        if (source != null) {
            IKerykeion svc = getService();
            if (svc == null) {
                throw new IllegalStateException("Could not get the hermes service.");
            } else if (svc != null) {
                try {
                    svc.training(source);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void startHermesTickerService(String json, Rect rect) throws IllegalStateException, IllegalArgumentException {
        if (json != null) {
            IKerykeion svc = getService();
            if (svc == null) {
                throw new IllegalStateException("Could not get the hermes service.");
            } else if (svc != null) {
                try {
                    svc.show(json, new KerykeionPosition(rect));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static int getLinkifyToPatternType(int linkify) {
        int pattern = 0;
        if (linkify == 0) {
            return 29;
        }
        if ((linkify & 15) == 15) {
            return 30;
        }
        if ((linkify & 2) != 0) {
            pattern = 0 | 2;
        }
        if ((linkify & 1) != 0) {
            pattern |= 4;
        }
        if ((linkify & 4) != 0) {
            pattern |= 8;
        }
        if ((linkify & 8) != 0) {
            pattern |= 2;
        }
        return pattern;
    }

    private static AnalyzerResultType getPatternTpAnalyzerResultType(int pType) {
        switch (pType) {
            case 1:
                return AnalyzerResultType.Date;
            case 2:
                return AnalyzerResultType.Email;
            case 4:
                return AnalyzerResultType.Url;
            case 8:
                return AnalyzerResultType.Telnum;
            case 16:
                return AnalyzerResultType.Place;
            default:
                return AnalyzerResultType.Unknown;
        }
    }

    private void checkParamValidation(int type, int pType, Object... source) throws IllegalArgumentException {
        if (1 == type || 8 == type || 9 == type || 4 == type || 16 == type) {
            Log.secD(TAG, "available request type");
            if ((pType & 31) != 0) {
                Log.secD(TAG, "available request pattern type");
                if (source == null || (source != null && source.length <= 0)) {
                    throw new IllegalArgumentException("Request source is empty.");
                }
                Object[] arr$ = source;
                int len$ = arr$.length;
                int i$ = 0;
                while (i$ < len$) {
                    Object obj = arr$[i$];
                    if ((obj instanceof String) || (obj instanceof Uri) || (obj instanceof HermesObject)) {
                        Log.secD(TAG, "available sources");
                        i$++;
                    } else {
                        throw new IllegalArgumentException("Request source is not available.");
                    }
                }
                return;
            }
            throw new IllegalArgumentException("Request Pattern Type is not available.");
        }
        throw new IllegalArgumentException("Request Type is not available.");
    }

    private JSONObject createAppObject(String appType, String dataType, Object value) {
        JSONObject app = new JSONObject();
        JSONArray dataArray = new JSONArray();
        dataArray.put(createDataObject(dataType, value));
        try {
            app.put(appType, dataArray);
            return app;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private JSONObject createDataObject(String key, Object value) {
        JSONObject data = new JSONObject();
        try {
            data.put(key, value);
            Log.secD(TAG, " JSON DATA : " + key + " / " + value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    private String makeJson(ArrayList<KerykeionResult> resultList) {
        JSONObject contents = new JSONObject();
        JSONArray appArray = new JSONArray();
        Iterator it = resultList.iterator();
        while (it.hasNext()) {
            KerykeionResult result = (KerykeionResult) it.next();
            AnalyzerResultType dataType = AnalyzerResultType.values()[result.getResultType()];
            AppType appType = getAppType(dataType);
            if (appType != null && result.isPossibleToShow()) {
                if (!dataType.equals(AnalyzerResultType.Event_id) && !dataType.equals(AnalyzerResultType.Contact_id)) {
                    appArray.put(createAppObject(appType.toString(), dataType.toString(), result.getAdaptableData()));
                } else if (result.getAdaptableData() instanceof List) {
                    for (Object data : (List) result.getAdaptableData()) {
                        appArray.put(createAppObject(appType.toString(), dataType.toString(), data));
                    }
                } else {
                    appArray.put(createAppObject(appType.toString(), dataType.toString(), result.getAdaptableData()));
                }
            }
        }
        JSONObject app = (JSONObject) extractEvent(resultList, null, 2);
        if (app != null) {
            appArray.put(app);
        }
        if (appArray.length() <= 0) {
            Log.secD(TAG, "App array length is zero");
            return null;
        }
        try {
            contents.put(JSON_CONTENTS, appArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.secD(TAG, " JSON DATA : " + contents.toString());
        return contents.toString();
    }

    private AppType getAppType(AnalyzerResultType rType) {
        switch (AnonymousClass3.$SwitchMap$com$samsung$android$hermes$HermesServiceManager$AnalyzerResultType[rType.ordinal()]) {
            case 1:
            case 2:
            case 3:
                return AppType.contact;
            case 4:
            case 5:
            case 6:
            case 7:
                return AppType.pim;
            case 8:
                return AppType.email;
            case 9:
                return AppType.browser;
            case 10:
            case 11:
                return AppType.map;
            case 12:
                return AppType.news;
            case 13:
                return AppType.recommand_text;
            default:
                return null;
        }
    }
}
