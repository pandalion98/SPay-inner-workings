package com.android.internal.telephony;

import android.app.ActivityManager;
import android.content.AsyncQueryHandler;
import android.content.AsyncQueryHandler.WorkerArgs;
import android.content.AsyncQueryHandler.WorkerHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.ContactsContract.PhoneLookup;
import android.telecom.PhoneAccount;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import com.sec.android.app.CscFeature;

public class CallerInfoAsyncQuery {
    private static final boolean DBG = false;
    private static final boolean ENABLE_UNKNOWN_NUMBER_GEO_DESCRIPTION = true;
    private static final int EVENT_ADD_LISTENER = 2;
    private static final int EVENT_EMERGENCY_NUMBER = 4;
    private static final int EVENT_END_OF_QUEUE = 3;
    private static final int EVENT_NEW_QUERY = 1;
    private static final int EVENT_VOICEMAIL_NUMBER = 5;
    private static final String LOG_TAG = "CallerInfoAsyncQuery";
    private CallerInfoAsyncQueryHandler mHandler;

    private class CallerInfoAsyncQueryHandler extends AsyncQueryHandler {
        private CallerInfo mCallerInfo;
        private Context mContext;
        private Uri mQueryUri;

        protected class CallerInfoWorkerHandler extends WorkerHandler {
            public CallerInfoWorkerHandler(Looper looper) {
                super(CallerInfoAsyncQueryHandler.this, looper);
            }

            public void handleMessage(Message msg) {
                WorkerArgs args = msg.obj;
                CookieWrapper cw = args.cookie;
                if (cw == null) {
                    super.handleMessage(msg);
                    return;
                }
                switch (cw.event) {
                    case 1:
                        super.handleMessage(msg);
                        return;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                        Message reply = args.handler.obtainMessage(msg.what);
                        reply.obj = args;
                        reply.arg1 = msg.arg1;
                        reply.sendToTarget();
                        return;
                    default:
                        return;
                }
            }
        }

        private CallerInfoAsyncQueryHandler(Context context) {
            super(CallerInfoAsyncQuery.getCurrentProfileContentResolver(context));
            this.mContext = context;
        }

        protected Handler createHandler(Looper looper) {
            return new CallerInfoWorkerHandler(looper);
        }

        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            CookieWrapper cw = (CookieWrapper) cookie;
            if (cw == null) {
                if (cursor != null) {
                    cursor.close();
                }
            } else if (cw.event == 3) {
                CallerInfoAsyncQuery.this.release();
                if (cursor != null) {
                    cursor.close();
                }
            } else {
                if (this.mCallerInfo == null) {
                    if (this.mContext == null || this.mQueryUri == null) {
                        throw new QueryPoolException("Bad context or query uri, or CallerInfoAsyncQuery already released.");
                    }
                    if (cw.event == 4) {
                        this.mCallerInfo = new CallerInfo().markAsEmergency(this.mContext);
                    } else if (cw.event == 5) {
                        this.mCallerInfo = new CallerInfo().markAsVoiceMail(cw.subId);
                    } else {
                        this.mCallerInfo = CallerInfo.getCallerInfo(this.mContext, this.mQueryUri, cursor);
                        CallerInfo newCallerInfo = CallerInfo.doSecondaryLookupIfNecessary(this.mContext, cw.number, this.mCallerInfo);
                        if (newCallerInfo != this.mCallerInfo) {
                            this.mCallerInfo = newCallerInfo;
                        }
                        if (TextUtils.isEmpty(this.mCallerInfo.name) || CscFeature.getInstance().getEnableStatus("CscFeature_RIL_AlwaysDisplayGeodescription")) {
                            this.mCallerInfo.updateGeoDescription(this.mContext, cw.number);
                        }
                        if (!TextUtils.isEmpty(cw.number)) {
                            if (CscFeature.getInstance().getEnableStatus("CscFeature_Common_DisablePhoneNumberFormatting") || SystemProperties.get("ro.csc.sales_code").equals("CTC")) {
                                this.mCallerInfo.phoneNumber = cw.number;
                            } else {
                                this.mCallerInfo.phoneNumber = PhoneNumberUtils.formatNumber(cw.number, this.mCallerInfo.normalizedNumber, CallerInfo.getCurrentCountryIso(this.mContext));
                            }
                        }
                    }
                    CookieWrapper endMarker = new CookieWrapper();
                    endMarker.event = 3;
                    startQuery(token, endMarker, null, null, null, null, null);
                }
                if (cw.listener != null) {
                    cw.listener.onQueryComplete(token, cw.cookie, this.mCallerInfo);
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }

    private static final class CookieWrapper {
        public Object cookie;
        public int event;
        public OnQueryCompleteListener listener;
        public String number;
        public int subId;

        private CookieWrapper() {
        }
    }

    public interface OnQueryCompleteListener {
        void onQueryComplete(int i, Object obj, CallerInfo callerInfo);
    }

    public static class QueryPoolException extends SQLException {
        public QueryPoolException(String error) {
            super(error);
        }
    }

    static ContentResolver getCurrentProfileContentResolver(Context context) {
        int currentUser = ActivityManager.getCurrentUser();
        if (UserManager.get(context).getUserHandle() != currentUser) {
            try {
                return context.createPackageContextAsUser(context.getPackageName(), 0, new UserHandle(currentUser)).getContentResolver();
            } catch (NameNotFoundException e) {
                Rlog.e(LOG_TAG, "Can't find self package", e);
            }
        }
        return context.getContentResolver();
    }

    private CallerInfoAsyncQuery() {
    }

    public static CallerInfoAsyncQuery startQuery(int token, Context context, Uri contactRef, OnQueryCompleteListener listener, Object cookie) {
        CallerInfoAsyncQuery c = new CallerInfoAsyncQuery();
        c.allocate(context, contactRef);
        CookieWrapper cw = new CookieWrapper();
        cw.listener = listener;
        cw.cookie = cookie;
        cw.event = 1;
        c.mHandler.startQuery(token, cw, contactRef, null, null, null, null);
        return c;
    }

    public static CallerInfoAsyncQuery startQuery(int token, Context context, String number, OnQueryCompleteListener listener, Object cookie) {
        return startQuery(token, context, number, listener, cookie, SubscriptionManager.getDefaultSubId());
    }

    public static CallerInfoAsyncQuery startQuery(int token, Context context, String number, OnQueryCompleteListener listener, Object cookie, int subId) {
        Uri contactRef = PhoneLookup.ENTERPRISE_CONTENT_FILTER_URI.buildUpon().appendPath(number).appendQueryParameter(PhoneAccount.SCHEME_SIP, String.valueOf(PhoneNumberUtils.isUriNumber(number))).build();
        CallerInfoAsyncQuery c = new CallerInfoAsyncQuery();
        c.allocate(context, contactRef);
        CookieWrapper cw = new CookieWrapper();
        cw.listener = listener;
        cw.cookie = cookie;
        cw.number = number;
        cw.subId = subId;
        if (PhoneNumberUtils.isLocalEmergencyNumber(context, number)) {
            cw.event = 4;
        } else if (PhoneNumberUtils.isVoiceMailNumber(subId, number)) {
            cw.event = 5;
        } else {
            cw.event = 1;
        }
        c.mHandler.startQuery(token, cw, contactRef, null, null, null, null);
        return c;
    }

    public void addQueryListener(int token, OnQueryCompleteListener listener, Object cookie) {
        CookieWrapper cw = new CookieWrapper();
        cw.listener = listener;
        cw.cookie = cookie;
        cw.event = 2;
        this.mHandler.startQuery(token, cw, null, null, null, null, null);
    }

    private void allocate(Context context, Uri contactRef) {
        if (context == null || contactRef == null) {
            throw new QueryPoolException("Bad context or query uri.");
        }
        this.mHandler = new CallerInfoAsyncQueryHandler(context);
        this.mHandler.mQueryUri = contactRef;
    }

    private void release() {
        this.mHandler.mContext = null;
        this.mHandler.mQueryUri = null;
        this.mHandler.mCallerInfo = null;
        this.mHandler = null;
    }

    private static String sanitizeUriToString(Uri uri) {
        if (uri == null) {
            return "";
        }
        String uriString = uri.toString();
        int indexOfLastSlash = uriString.lastIndexOf(47);
        if (indexOfLastSlash > 0) {
            return uriString.substring(0, indexOfLastSlash) + "/xxxxxxx";
        }
        return uriString;
    }
}
