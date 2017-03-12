package android.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.UserInfo;
import android.database.Cursor;
import android.media.AudioParameter;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.ContactsContract.CommonDataKinds.Callable;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.DataUsageFeedback;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import com.android.internal.telephony.CallerInfo;
import java.util.List;

public class CallLog {
    public static final String AUTHORITY = "call_log";
    public static final Uri CONTENT_URI = Uri.parse("content://call_log");
    private static final String LOG_TAG = "CallLog";

    public static class Calls implements BaseColumns {
        public static final String ALLOW_VOICEMAILS_PARAM_KEY = "allow_voicemails";
        public static final String CACHED_FORMATTED_NUMBER = "formatted_number";
        public static final String CACHED_LOOKUP_URI = "lookup_uri";
        public static final String CACHED_MATCHED_NUMBER = "matched_number";
        public static final String CACHED_NAME = "name";
        public static final String CACHED_NORMALIZED_NUMBER = "normalized_number";
        public static final String CACHED_NUMBER_LABEL = "numberlabel";
        public static final String CACHED_NUMBER_TYPE = "numbertype";
        public static final String CACHED_PHOTO_ID = "photo_id";
        public static final String CACHED_PHOTO_URI = "photo_uri";
        public static final Uri CONTENT_FILTER_URI = Uri.parse("content://call_log/calls/filter");
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/calls";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/calls";
        public static final Uri CONTENT_URI = Uri.parse("content://call_log/calls");
        public static final Uri CONTENT_URI_WITH_VOICEMAIL = CONTENT_URI.buildUpon().appendQueryParameter(ALLOW_VOICEMAILS_PARAM_KEY, AudioParameter.AUDIO_PARAMETER_VALUE_true).build();
        public static final String COUNTRY_ISO = "countryiso";
        public static final String DATA_USAGE = "data_usage";
        public static final String DATE = "date";
        public static final String DEFAULT_SORT_ORDER = "date DESC";
        public static final String DURATION = "duration";
        public static final String EXTRA_CALL_TYPE_FILTER = "android.provider.extra.CALL_TYPE_FILTER";
        public static final String FEATURES = "features";
        public static final int FEATURES_VIDEO = 1;
        public static final String GEOCODED_LOCATION = "geocoded_location";
        public static final int INCOMING_TYPE = 1;
        public static final String IS_READ = "is_read";
        public static final String LIMIT_PARAM_KEY = "limit";
        private static final int MIN_DURATION_FOR_NORMALIZED_NUMBER_UPDATE_MS = 10000;
        public static final int MISSED_TYPE = 3;
        public static final String NEW = "new";
        public static final String NUMBER = "number";
        public static final String NUMBER_PRESENTATION = "presentation";
        public static final String OFFSET_PARAM_KEY = "offset";
        public static final int OUTGOING_TYPE = 2;
        public static final String PHONE_ACCOUNT_ADDRESS = "phone_account_address";
        public static final String PHONE_ACCOUNT_COMPONENT_NAME = "subscription_component_name";
        public static final String PHONE_ACCOUNT_HIDDEN = "phone_account_hidden";
        public static final String PHONE_ACCOUNT_ID = "subscription_id";
        public static final int PRESENTATION_ALLOWED = 1;
        public static final int PRESENTATION_PAYPHONE = 4;
        public static final int PRESENTATION_RESTRICTED = 2;
        public static final int PRESENTATION_UNKNOWN = 3;
        public static final String SUB_ID = "sub_id";
        public static final String TRANSCRIPTION = "transcription";
        public static final String TYPE = "type";
        public static final int VOICEMAIL_TYPE = 4;
        public static final String VOICEMAIL_URI = "voicemail_uri";

        public static Uri addCall(CallerInfo ci, Context context, String number, int presentation, int callType, int features, PhoneAccountHandle accountHandle, long start, int duration, Long dataUsage) {
            return addCall(ci, context, number, presentation, callType, features, accountHandle, start, duration, dataUsage, false, false);
        }

        public static Uri addCall(CallerInfo ci, Context context, String number, int presentation, int callType, int features, PhoneAccountHandle accountHandle, long start, int duration, Long dataUsage, boolean addForAllUsers) {
            return addCall(ci, context, number, presentation, callType, features, accountHandle, start, duration, dataUsage, addForAllUsers, false);
        }

        public static Uri addCall(CallerInfo ci, Context context, String number, int presentation, int callType, int features, PhoneAccountHandle accountHandle, long start, int duration, Long dataUsage, boolean addForAllUsers, boolean is_read) {
            Cursor cursor;
            ContentResolver resolver = context.getContentResolver();
            int numberPresentation = 1;
            TelecomManager tm = null;
            try {
                tm = TelecomManager.from(context);
            } catch (UnsupportedOperationException e) {
            }
            String accountAddress = null;
            if (!(tm == null || accountHandle == null)) {
                PhoneAccount account = tm.getPhoneAccount(accountHandle);
                if (account != null) {
                    Uri address = account.getSubscriptionAddress();
                    if (address != null) {
                        accountAddress = address.getSchemeSpecificPart();
                    }
                }
            }
            if (presentation == 2) {
                numberPresentation = 2;
            } else if (presentation == 4) {
                numberPresentation = 4;
            } else if (TextUtils.isEmpty(number) || presentation == 3) {
                numberPresentation = 3;
            }
            if (numberPresentation != 1) {
                number = ProxyInfo.LOCAL_EXCL_LIST;
                if (ci != null) {
                    ci.name = ProxyInfo.LOCAL_EXCL_LIST;
                }
            }
            String accountComponentString = null;
            String accountId = null;
            if (accountHandle != null) {
                accountComponentString = accountHandle.getComponentName().flattenToString();
                accountId = accountHandle.getId();
            }
            ContentValues contentValues = new ContentValues(6);
            contentValues.put("number", number);
            contentValues.put(NUMBER_PRESENTATION, Integer.valueOf(numberPresentation));
            contentValues.put("type", Integer.valueOf(callType));
            contentValues.put(FEATURES, Integer.valueOf(features));
            contentValues.put("date", Long.valueOf(start));
            contentValues.put("duration", Long.valueOf((long) duration));
            if (dataUsage != null) {
                contentValues.put(DATA_USAGE, dataUsage);
            }
            contentValues.put(PHONE_ACCOUNT_COMPONENT_NAME, accountComponentString);
            contentValues.put("subscription_id", accountId);
            contentValues.put(PHONE_ACCOUNT_ADDRESS, accountAddress);
            contentValues.put(NEW, Integer.valueOf(1));
            if (callType == 3) {
                contentValues.put(IS_READ, Integer.valueOf(is_read ? 1 : 0));
            }
            if (ci != null && ci.contactIdOrZero > 0) {
                if (ci.normalizedNumber != null) {
                    String normalizedPhoneNumber = ci.normalizedNumber;
                    cursor = resolver.query(Phone.CONTENT_URI, new String[]{"_id"}, "contact_id =? AND data4 =?", new String[]{String.valueOf(ci.contactIdOrZero), normalizedPhoneNumber}, null);
                } else {
                    String phoneNumber;
                    if (ci.phoneNumber != null) {
                        phoneNumber = ci.phoneNumber;
                    } else {
                        phoneNumber = number;
                    }
                    cursor = resolver.query(Uri.withAppendedPath(Callable.CONTENT_FILTER_URI, Uri.encode(phoneNumber)), new String[]{"_id"}, "contact_id =?", new String[]{String.valueOf(ci.contactIdOrZero)}, null);
                }
                if (cursor != null) {
                    try {
                        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                            resolver.update(DataUsageFeedback.FEEDBACK_URI.buildUpon().appendPath(cursor.getString(0)).appendQueryParameter("type", "call").build(), new ContentValues(), null, null);
                        }
                        cursor.close();
                    } catch (Throwable th) {
                        cursor.close();
                    }
                }
            }
            Uri result = null;
            if (addForAllUsers) {
                UserManager userManager = (UserManager) context.getSystemService(Context.USER_SERVICE);
                List<UserInfo> users = userManager.getUsers(true);
                int currentUserId = userManager.getUserHandle();
                int count = users.size();
                for (int i = 0; i < count; i++) {
                    UserInfo user = (UserInfo) users.get(i);
                    UserHandle userHandle = user.getUserHandle();
                    if (userManager.isUserRunning(userHandle)) {
                        if (!(userManager.hasUserRestriction(UserManager.DISALLOW_OUTGOING_CALLS, userHandle) || user.isManagedProfile())) {
                            Uri uri = addEntryAndRemoveExpiredEntries(context, ContentProvider.maybeAddUserId(CONTENT_URI, user.id), contentValues);
                            if (user.id == currentUserId) {
                                result = uri;
                            }
                        }
                    }
                }
                return result;
            }
            return addEntryAndRemoveExpiredEntries(context, CONTENT_URI, contentValues);
        }

        public static String getLastOutgoingCall(Context context) {
            ContentResolver resolver = context.getContentResolver();
            Cursor c = null;
            try {
                c = resolver.query(CONTENT_URI, new String[]{"number"}, "type = 2", null, "date DESC LIMIT 1");
                String str;
                if (c == null || !c.moveToFirst()) {
                    str = ProxyInfo.LOCAL_EXCL_LIST;
                    return str;
                }
                str = c.getString(0);
                if (c != null) {
                    c.close();
                }
                return str;
            } finally {
                if (c != null) {
                    c.close();
                }
            }
        }

        private static Uri addEntryAndRemoveExpiredEntries(Context context, Uri uri, ContentValues values) {
            ContentResolver resolver = context.getContentResolver();
            Uri result = resolver.insert(uri, values);
            resolver.delete(uri, "_id IN (SELECT _id FROM calls ORDER BY date DESC LIMIT -1 OFFSET 500)", null);
            return result;
        }
    }
}
