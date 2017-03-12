package android.service.notification;

import android.app.ActivityManager;
import android.app.NotificationManager.Policy;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.provider.Settings.Global;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import com.android.internal.R;
import com.android.internal.content.NativeLibraryHelper;
import com.android.internal.telephony.IccCardConstants;
import com.samsung.android.fingerprint.FingerprintEvent;
import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.service.DeviceRootKeyService.DeviceRootKeyServiceManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class ZenModeConfig implements Parcelable {
    public static final String ACTION_DND_CHANGED = "android.intent.action.DND_CHANGED";
    private static final String ALLOW_ATT_ALARMS = "alarms";
    private static final String ALLOW_ATT_CALLS = "calls";
    private static final String ALLOW_ATT_CALLS_FROM = "callsFrom";
    private static final String ALLOW_ATT_EVENTS = "events";
    private static final String ALLOW_ATT_FROM = "from";
    private static final String ALLOW_ATT_MESSAGES = "messages";
    private static final String ALLOW_ATT_MESSAGES_FROM = "messagesFrom";
    private static final String ALLOW_ATT_REMINDERS = "reminders";
    private static final String ALLOW_ATT_REPEAT_CALLERS = "repeatCallers";
    private static final String ALLOW_TAG = "allow";
    public static final int[] ALL_DAYS = new int[]{1, 2, 3, 4, 5, 6, 7};
    private static final String AUTOMATIC_TAG = "automatic";
    private static final String CONDITION_ATT_COMPONENT = "component";
    private static final String CONDITION_ATT_FLAGS = "flags";
    private static final String CONDITION_ATT_ICON = "icon";
    private static final String CONDITION_ATT_ID = "id";
    private static final String CONDITION_ATT_LINE1 = "line1";
    private static final String CONDITION_ATT_LINE2 = "line2";
    private static final String CONDITION_ATT_STATE = "state";
    private static final String CONDITION_ATT_SUMMARY = "summary";
    private static final String CONDITION_TAG = "condition";
    public static final String COUNTDOWN_PATH = "countdown";
    public static final Creator<ZenModeConfig> CREATOR = new Creator<ZenModeConfig>() {
        public ZenModeConfig createFromParcel(Parcel source) {
            return new ZenModeConfig(source);
        }

        public ZenModeConfig[] newArray(int size) {
            return new ZenModeConfig[size];
        }
    };
    private static final int DAY_MINUTES = 1440;
    private static final boolean DEFAULT_ALLOW_ALARMS = true;
    private static final boolean DEFAULT_ALLOW_CALLS = false;
    private static final boolean DEFAULT_ALLOW_EVENTS = false;
    private static final boolean DEFAULT_ALLOW_MESSAGES = false;
    private static final boolean DEFAULT_ALLOW_REMINDERS = false;
    private static final boolean DEFAULT_ALLOW_REPEAT_CALLERS = false;
    private static final int DEFAULT_SOURCE = 1;
    public static final int DND_CHECK_BY_ALARM = 5;
    public static final int DND_CHECK_BY_ALLOW = 7;
    public static final int DND_CHECK_BY_INIT = 6;
    public static final int DND_CHECK_BY_NONE = 0;
    public static final int DND_CHECK_BY_SCHEDULE_SWITCH = 1;
    public static final int DND_CHECK_BY_SCHEDULE_TIME = 2;
    public static final int DND_CHECK_BY_TIME_SET = 3;
    public static final int DND_CHECK_BY_TIME_ZONE = 4;
    public static final String EVENT_PATH = "event";
    private static final String MANUAL_TAG = "manual";
    public static final int MAX_SOURCE = 2;
    private static final int MINUTES_MS = 60000;
    public static final int[] MINUTE_BUCKETS = generateMinuteBuckets();
    public static final String REASON_DND_CHANGED = "reason";
    private static final String RULE_ATT_COMPONENT = "component";
    private static final String RULE_ATT_CONDITION_ID = "conditionId";
    private static final String RULE_ATT_ENABLED = "enabled";
    private static final String RULE_ATT_ID = "ruleId";
    private static final String RULE_ATT_NAME = "name";
    private static final String RULE_ATT_SNOOZING = "snoozing";
    private static final String RULE_ATT_ZEN = "zen";
    public static final String SCHEDULE_PATH = "schedule";
    private static final int SECONDS_MS = 1000;
    public static final int SOURCE_ANYONE = 0;
    public static final int SOURCE_CONTACT = 1;
    public static final int SOURCE_STAR = 2;
    public static final String SYSTEM_AUTHORITY = "android";
    private static String TAG = "ZenModeConfig";
    public static final String TW_SCHEDULE_PATH = "twschedule";
    public static final int[] WEEKEND_DAYS = new int[]{6, 7};
    public static final int[] WEEKNIGHT_DAYS = new int[]{1, 2, 3, 4, 5};
    private static final int XML_VERSION = 2;
    private static final String ZEN_ATT_USER = "user";
    private static final String ZEN_ATT_VERSION = "version";
    private static final String ZEN_TAG = "zen";
    private static final int ZERO_VALUE_MS = 10000;
    public boolean allowAlarms = true;
    public boolean allowCalls = false;
    public int allowCallsFrom = 1;
    public boolean allowEvents = false;
    public boolean allowMessages = false;
    public int allowMessagesFrom = 1;
    public boolean allowReminders = false;
    public boolean allowRepeatCallers = false;
    public ArrayMap<String, ZenRule> automaticRules = new ArrayMap();
    public int mChangeReason = 0;
    public ZenRule manualRule;
    public int user = 0;

    public static class Diff {
        private final ArrayList<String> lines = new ArrayList();

        public String toString() {
            StringBuilder sb = new StringBuilder("Diff[");
            int N = this.lines.size();
            for (int i = 0; i < N; i++) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append((String) this.lines.get(i));
            }
            return sb.append(']').toString();
        }

        private Diff addLine(String item, String action) {
            this.lines.add(item + ":" + action);
            return this;
        }

        public Diff addLine(String item, String subitem, Object from, Object to) {
            return addLine(item + "." + subitem, from, to);
        }

        public Diff addLine(String item, Object from, Object to) {
            return addLine(item, from + "->" + to);
        }
    }

    public static class EventInfo {
        public static final int REPLY_ANY_EXCEPT_NO = 0;
        public static final int REPLY_YES = 2;
        public static final int REPLY_YES_OR_MAYBE = 1;
        public String calendar;
        public int reply;
        public int userId = DeviceRootKeyServiceManager.ERR_SERVICE_ERROR;

        public int hashCode() {
            return 0;
        }

        public boolean equals(Object o) {
            if (!(o instanceof EventInfo)) {
                return false;
            }
            EventInfo other = (EventInfo) o;
            if (this.userId == other.userId && Objects.equals(this.calendar, other.calendar) && this.reply == other.reply) {
                return true;
            }
            return false;
        }

        public EventInfo copy() {
            EventInfo rt = new EventInfo();
            rt.userId = this.userId;
            rt.calendar = this.calendar;
            rt.reply = this.reply;
            return rt;
        }

        public static int resolveUserId(int userId) {
            return userId == DeviceRootKeyServiceManager.ERR_SERVICE_ERROR ? ActivityManager.getCurrentUser() : userId;
        }
    }

    public interface Migration {
        ZenModeConfig migrate(XmlV1 xmlV1);
    }

    public static class ScheduleInfo {
        public int[] days;
        public int endHour;
        public int endMinute;
        public int startHour;
        public int startMinute;

        public int hashCode() {
            return 0;
        }

        public boolean equals(Object o) {
            if (!(o instanceof ScheduleInfo)) {
                return false;
            }
            ScheduleInfo other = (ScheduleInfo) o;
            if (ZenModeConfig.toDayList(this.days).equals(ZenModeConfig.toDayList(other.days)) && this.startHour == other.startHour && this.startMinute == other.startMinute && this.endHour == other.endHour && this.endMinute == other.endMinute) {
                return true;
            }
            return false;
        }

        public ScheduleInfo copy() {
            ScheduleInfo rt = new ScheduleInfo();
            if (this.days != null) {
                rt.days = new int[this.days.length];
                System.arraycopy(this.days, 0, rt.days, 0, this.days.length);
            }
            rt.startHour = this.startHour;
            rt.startMinute = this.startMinute;
            rt.endHour = this.endHour;
            rt.endMinute = this.endMinute;
            return rt;
        }
    }

    public static final class XmlV1 {
        private static final String EXIT_CONDITION_ATT_COMPONENT = "component";
        private static final String EXIT_CONDITION_TAG = "exitCondition";
        private static final String SLEEP_ATT_END_HR = "endHour";
        private static final String SLEEP_ATT_END_MIN = "endMin";
        private static final String SLEEP_ATT_MODE = "mode";
        private static final String SLEEP_ATT_NONE = "none";
        private static final String SLEEP_ATT_START_HR = "startHour";
        private static final String SLEEP_ATT_START_MIN = "startMin";
        public static final String SLEEP_MODE_DAYS_PREFIX = "days:";
        public static final String SLEEP_MODE_NIGHTS = "nights";
        public static final String SLEEP_MODE_WEEKNIGHTS = "weeknights";
        private static final String SLEEP_TAG = "sleep";
        public boolean allowAlarms = true;
        public boolean allowCalls;
        public boolean allowEvents = false;
        public int allowFrom = 0;
        public boolean allowMessages;
        public boolean allowReminders = false;
        public boolean applyDowntime = false;
        public ComponentName[] conditionComponents;
        public Uri[] conditionIds;
        public Condition exitCondition;
        public ComponentName exitConditionComponent;
        public int sleepEndHour;
        public int sleepEndMinute;
        public String sleepMode;
        public boolean sleepNone;
        public int sleepStartHour;
        public int sleepStartMinute;

        private static boolean isValidSleepMode(String sleepMode) {
            return sleepMode == null || sleepMode.equals(SLEEP_MODE_NIGHTS) || sleepMode.equals(SLEEP_MODE_WEEKNIGHTS) || tryParseDays(sleepMode) != null;
        }

        public static int[] tryParseDays(String sleepMode) {
            if (sleepMode == null) {
                return null;
            }
            sleepMode = sleepMode.trim();
            if (SLEEP_MODE_NIGHTS.equals(sleepMode)) {
                return ZenModeConfig.ALL_DAYS;
            }
            if (SLEEP_MODE_WEEKNIGHTS.equals(sleepMode)) {
                return ZenModeConfig.WEEKNIGHT_DAYS;
            }
            if (!sleepMode.startsWith(SLEEP_MODE_DAYS_PREFIX) || sleepMode.equals(SLEEP_MODE_DAYS_PREFIX)) {
                return null;
            }
            return ZenModeConfig.tryParseDayList(sleepMode.substring(SLEEP_MODE_DAYS_PREFIX.length()), FingerprintManager.FINGER_PERMISSION_DELIMITER);
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static android.service.notification.ZenModeConfig.XmlV1 readXml(org.xmlpull.v1.XmlPullParser r15) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            /*
            r7 = new android.service.notification.ZenModeConfig$XmlV1;
            r7.<init>();
            r1 = new java.util.ArrayList;
            r1.<init>();
            r3 = new java.util.ArrayList;
            r3.<init>();
        L_0x000f:
            r11 = r15.next();
            r12 = 1;
            if (r11 == r12) goto L_0x0162;
        L_0x0016:
            r10 = r15.getName();
            r12 = 3;
            if (r11 != r12) goto L_0x0049;
        L_0x001d:
            r12 = "zen";
            r12 = r12.equals(r10);
            if (r12 == 0) goto L_0x0049;
        L_0x0026:
            r12 = r1.isEmpty();
            if (r12 != 0) goto L_0x0048;
        L_0x002c:
            r12 = r1.size();
            r12 = new android.content.ComponentName[r12];
            r12 = r1.toArray(r12);
            r12 = (android.content.ComponentName[]) r12;
            r7.conditionComponents = r12;
            r12 = r3.size();
            r12 = new android.net.Uri[r12];
            r12 = r3.toArray(r12);
            r12 = (android.net.Uri[]) r12;
            r7.conditionIds = r12;
        L_0x0048:
            return r7;
        L_0x0049:
            r12 = 2;
            if (r11 != r12) goto L_0x000f;
        L_0x004c:
            r12 = "allow";
            r12 = r12.equals(r10);
            if (r12 == 0) goto L_0x00b9;
        L_0x0054:
            r12 = "calls";
            r13 = 0;
            r12 = android.service.notification.ZenModeConfig.safeBoolean(r15, r12, r13);
            r7.allowCalls = r12;
            r12 = "messages";
            r13 = 0;
            r12 = android.service.notification.ZenModeConfig.safeBoolean(r15, r12, r13);
            r7.allowMessages = r12;
            r12 = "reminders";
            r13 = 0;
            r12 = android.service.notification.ZenModeConfig.safeBoolean(r15, r12, r13);
            r7.allowReminders = r12;
            r12 = "events";
            r13 = 0;
            r12 = android.service.notification.ZenModeConfig.safeBoolean(r15, r12, r13);
            r7.allowEvents = r12;
            r12 = "alarms";
            r13 = 1;
            r12 = android.service.notification.ZenModeConfig.safeBoolean(r15, r12, r13);
            r7.allowAlarms = r12;
            r12 = "applydowntime";
            r13 = 0;
            r12 = android.service.notification.ZenModeConfig.safeBoolean(r15, r12, r13);
            r7.applyDowntime = r12;
            r12 = "from";
            r13 = 0;
            r12 = android.service.notification.ZenModeConfig.safeInt(r15, r12, r13);
            r7.allowFrom = r12;
            r12 = r7.allowFrom;
            if (r12 < 0) goto L_0x009e;
        L_0x0099:
            r12 = r7.allowFrom;
            r13 = 2;
            if (r12 <= r13) goto L_0x000f;
        L_0x009e:
            r12 = new java.lang.IndexOutOfBoundsException;
            r13 = new java.lang.StringBuilder;
            r13.<init>();
            r14 = "bad source in config:";
            r13 = r13.append(r14);
            r14 = r7.allowFrom;
            r13 = r13.append(r14);
            r13 = r13.toString();
            r12.<init>(r13);
            throw r12;
        L_0x00b9:
            r12 = "sleep";
            r12 = r12.equals(r10);
            if (r12 == 0) goto L_0x0126;
        L_0x00c2:
            r12 = 0;
            r13 = "mode";
            r6 = r15.getAttributeValue(r12, r13);
            r12 = isValidSleepMode(r6);
            if (r12 == 0) goto L_0x011c;
        L_0x00d0:
            r7.sleepMode = r6;
            r12 = "none";
            r13 = 0;
            r12 = android.service.notification.ZenModeConfig.safeBoolean(r15, r12, r13);
            r7.sleepNone = r12;
            r12 = "startHour";
            r13 = 0;
            r8 = android.service.notification.ZenModeConfig.safeInt(r15, r12, r13);
            r12 = "startMin";
            r13 = 0;
            r9 = android.service.notification.ZenModeConfig.safeInt(r15, r12, r13);
            r12 = "endHour";
            r13 = 0;
            r4 = android.service.notification.ZenModeConfig.safeInt(r15, r12, r13);
            r12 = "endMin";
            r13 = 0;
            r5 = android.service.notification.ZenModeConfig.safeInt(r15, r12, r13);
            r12 = android.service.notification.ZenModeConfig.isValidHour(r8);
            if (r12 == 0) goto L_0x011e;
        L_0x0100:
            r7.sleepStartHour = r8;
            r12 = android.service.notification.ZenModeConfig.isValidMinute(r9);
            if (r12 == 0) goto L_0x0120;
        L_0x0108:
            r7.sleepStartMinute = r9;
            r12 = android.service.notification.ZenModeConfig.isValidHour(r4);
            if (r12 == 0) goto L_0x0122;
        L_0x0110:
            r7.sleepEndHour = r4;
            r12 = android.service.notification.ZenModeConfig.isValidMinute(r5);
            if (r12 == 0) goto L_0x0124;
        L_0x0118:
            r7.sleepEndMinute = r5;
            goto L_0x000f;
        L_0x011c:
            r6 = 0;
            goto L_0x00d0;
        L_0x011e:
            r8 = 0;
            goto L_0x0100;
        L_0x0120:
            r9 = 0;
            goto L_0x0108;
        L_0x0122:
            r4 = 0;
            goto L_0x0110;
        L_0x0124:
            r5 = 0;
            goto L_0x0118;
        L_0x0126:
            r12 = "condition";
            r12 = r12.equals(r10);
            if (r12 == 0) goto L_0x0146;
        L_0x012e:
            r12 = "component";
            r0 = android.service.notification.ZenModeConfig.safeComponentName(r15, r12);
            r12 = "id";
            r2 = android.service.notification.ZenModeConfig.safeUri(r15, r12);
            if (r0 == 0) goto L_0x000f;
        L_0x013c:
            if (r2 == 0) goto L_0x000f;
        L_0x013e:
            r1.add(r0);
            r3.add(r2);
            goto L_0x000f;
        L_0x0146:
            r12 = "exitCondition";
            r12 = r12.equals(r10);
            if (r12 == 0) goto L_0x000f;
        L_0x014e:
            r12 = android.service.notification.ZenModeConfig.readConditionXml(r15);
            r7.exitCondition = r12;
            r12 = r7.exitCondition;
            if (r12 == 0) goto L_0x000f;
        L_0x0158:
            r12 = "component";
            r12 = android.service.notification.ZenModeConfig.safeComponentName(r15, r12);
            r7.exitConditionComponent = r12;
            goto L_0x000f;
        L_0x0162:
            r12 = new java.lang.IllegalStateException;
            r13 = "Failed to reach END_DOCUMENT";
            r12.<init>(r13);
            throw r12;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.service.notification.ZenModeConfig.XmlV1.readXml(org.xmlpull.v1.XmlPullParser):android.service.notification.ZenModeConfig$XmlV1");
        }
    }

    public static class ZenRule implements Parcelable {
        public static final Creator<ZenRule> CREATOR = new Creator<ZenRule>() {
            public ZenRule createFromParcel(Parcel source) {
                return new ZenRule(source);
            }

            public ZenRule[] newArray(int size) {
                return new ZenRule[size];
            }
        };
        public ComponentName component;
        public Condition condition;
        public Uri conditionId;
        public boolean enabled;
        public String name;
        public boolean snoozing;
        public int zenMode;

        public ZenRule(Parcel source) {
            boolean z = false;
            this.enabled = source.readInt() == 1;
            if (source.readInt() == 1) {
                z = true;
            }
            this.snoozing = z;
            if (source.readInt() == 1) {
                this.name = source.readString();
            }
            this.zenMode = source.readInt();
            this.conditionId = (Uri) source.readParcelable(null);
            this.condition = (Condition) source.readParcelable(null);
            this.component = (ComponentName) source.readParcelable(null);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            int i;
            dest.writeInt(this.enabled ? 1 : 0);
            if (this.snoozing) {
                i = 1;
            } else {
                i = 0;
            }
            dest.writeInt(i);
            if (this.name != null) {
                dest.writeInt(1);
                dest.writeString(this.name);
            } else {
                dest.writeInt(0);
            }
            dest.writeInt(this.zenMode);
            dest.writeParcelable(this.conditionId, 0);
            dest.writeParcelable(this.condition, 0);
            dest.writeParcelable(this.component, 0);
        }

        public String toString() {
            return new StringBuilder(ZenRule.class.getSimpleName()).append('[').append("enabled=").append(this.enabled).append(",snoozing=").append(this.snoozing).append(",name=").append(this.name).append(",zenMode=").append(Global.zenModeToString(this.zenMode)).append(",conditionId=").append(this.conditionId).append(",condition=").append(this.condition).append(",component=").append(this.component).append(']').toString();
        }

        private static void appendDiff(Diff d, String item, ZenRule from, ZenRule to) {
            if (d != null) {
                if (from != null) {
                    from.appendDiff(d, item, to);
                } else if (to != null) {
                    d.addLine(item, "insert");
                }
            }
        }

        private void appendDiff(Diff d, String item, ZenRule to) {
            if (to == null) {
                d.addLine(item, "delete");
                return;
            }
            if (this.enabled != to.enabled) {
                d.addLine(item, ZenModeConfig.RULE_ATT_ENABLED, Boolean.valueOf(this.enabled), Boolean.valueOf(to.enabled));
            }
            if (this.snoozing != to.snoozing) {
                d.addLine(item, ZenModeConfig.RULE_ATT_SNOOZING, Boolean.valueOf(this.snoozing), Boolean.valueOf(to.snoozing));
            }
            if (!Objects.equals(this.name, to.name)) {
                d.addLine(item, "name", this.name, to.name);
            }
            if (this.zenMode != to.zenMode) {
                d.addLine(item, "zenMode", Integer.valueOf(this.zenMode), Integer.valueOf(to.zenMode));
            }
            if (!Objects.equals(this.conditionId, to.conditionId)) {
                d.addLine(item, ZenModeConfig.RULE_ATT_CONDITION_ID, this.conditionId, to.conditionId);
            }
            if (!Objects.equals(this.condition, to.condition)) {
                d.addLine(item, "condition", this.condition, to.condition);
            }
            if (!Objects.equals(this.component, to.component)) {
                d.addLine(item, "component", this.component, to.component);
            }
        }

        public boolean equals(Object o) {
            if (!(o instanceof ZenRule)) {
                return false;
            }
            if (o == this) {
                return true;
            }
            ZenRule other = (ZenRule) o;
            if (other.enabled == this.enabled && other.snoozing == this.snoozing && Objects.equals(other.name, this.name) && other.zenMode == this.zenMode && Objects.equals(other.conditionId, this.conditionId) && Objects.equals(other.condition, this.condition) && Objects.equals(other.component, this.component)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{Boolean.valueOf(this.enabled), Boolean.valueOf(this.snoozing), this.name, Integer.valueOf(this.zenMode), this.conditionId, this.condition, this.component});
        }

        public boolean isAutomaticActive() {
            return this.enabled && !this.snoozing && this.component != null && isTrueOrUnknown();
        }

        public boolean isTrueOrUnknown() {
            return this.condition != null && (this.condition.state == 1 || this.condition.state == 2);
        }
    }

    public ZenModeConfig(Parcel source) {
        boolean z;
        boolean z2 = true;
        if (source.readInt() == 1) {
            z = true;
        } else {
            z = false;
        }
        this.allowCalls = z;
        if (source.readInt() == 1) {
            z = true;
        } else {
            z = false;
        }
        this.allowRepeatCallers = z;
        if (source.readInt() == 1) {
            z = true;
        } else {
            z = false;
        }
        this.allowMessages = z;
        if (source.readInt() == 1) {
            z = true;
        } else {
            z = false;
        }
        this.allowReminders = z;
        if (source.readInt() == 1) {
            z = true;
        } else {
            z = false;
        }
        this.allowEvents = z;
        if (source.readInt() != 1) {
            z2 = false;
        }
        this.allowAlarms = z2;
        this.allowCallsFrom = source.readInt();
        this.allowMessagesFrom = source.readInt();
        this.user = source.readInt();
        this.manualRule = (ZenRule) source.readParcelable(null);
        int len = source.readInt();
        if (len > 0) {
            String[] ids = new String[len];
            ZenRule[] rules = new ZenRule[len];
            source.readStringArray(ids);
            source.readTypedArray(rules, ZenRule.CREATOR);
            for (int i = 0; i < len; i++) {
                this.automaticRules.put(ids[i], rules[i]);
            }
        }
        this.mChangeReason = source.readInt();
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i;
        int i2 = 1;
        if (this.allowCalls) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.allowRepeatCallers) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.allowMessages) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.allowReminders) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.allowEvents) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (!this.allowAlarms) {
            i2 = 0;
        }
        dest.writeInt(i2);
        dest.writeInt(this.allowCallsFrom);
        dest.writeInt(this.allowMessagesFrom);
        dest.writeInt(this.user);
        dest.writeParcelable(this.manualRule, 0);
        if (this.automaticRules.isEmpty()) {
            dest.writeInt(0);
        } else {
            int len = this.automaticRules.size();
            String[] ids = new String[len];
            ZenRule[] rules = new ZenRule[len];
            for (int i3 = 0; i3 < len; i3++) {
                ids[i3] = (String) this.automaticRules.keyAt(i3);
                rules[i3] = (ZenRule) this.automaticRules.valueAt(i3);
            }
            dest.writeInt(len);
            dest.writeStringArray(ids);
            dest.writeTypedArray(rules, 0);
        }
        dest.writeInt(this.mChangeReason);
    }

    public String toString() {
        return new StringBuilder(ZenModeConfig.class.getSimpleName()).append('[').append("user=").append(this.user).append(",allowCalls=").append(this.allowCalls).append(",allowRepeatCallers=").append(this.allowRepeatCallers).append(",allowMessages=").append(this.allowMessages).append(",allowCallsFrom=").append(sourceToString(this.allowCallsFrom)).append(",allowMessagesFrom=").append(sourceToString(this.allowMessagesFrom)).append(",allowReminders=").append(this.allowReminders).append(",allowEvents=").append(this.allowEvents).append(",allowAlarms=").append(this.allowAlarms).append(",automaticRules=").append(this.automaticRules).append(",manualRule=").append(this.manualRule).append(']').toString();
    }

    private Diff diff(ZenModeConfig to) {
        Diff d = new Diff();
        if (to == null) {
            return d.addLine("config", "delete");
        }
        if (this.user != to.user) {
            d.addLine("user", Integer.valueOf(this.user), Integer.valueOf(to.user));
        }
        if (this.allowCalls != to.allowCalls) {
            d.addLine("allowCalls", Boolean.valueOf(this.allowCalls), Boolean.valueOf(to.allowCalls));
        }
        if (this.allowRepeatCallers != to.allowRepeatCallers) {
            d.addLine("allowRepeatCallers", Boolean.valueOf(this.allowRepeatCallers), Boolean.valueOf(to.allowRepeatCallers));
        }
        if (this.allowMessages != to.allowMessages) {
            d.addLine("allowMessages", Boolean.valueOf(this.allowMessages), Boolean.valueOf(to.allowMessages));
        }
        if (this.allowCallsFrom != to.allowCallsFrom) {
            d.addLine("allowCallsFrom", Integer.valueOf(this.allowCallsFrom), Integer.valueOf(to.allowCallsFrom));
        }
        if (this.allowMessagesFrom != to.allowMessagesFrom) {
            d.addLine("allowMessagesFrom", Integer.valueOf(this.allowMessagesFrom), Integer.valueOf(to.allowMessagesFrom));
        }
        if (this.allowReminders != to.allowReminders) {
            d.addLine("allowReminders", Boolean.valueOf(this.allowReminders), Boolean.valueOf(to.allowReminders));
        }
        if (this.allowEvents != to.allowEvents) {
            d.addLine("allowEvents", Boolean.valueOf(this.allowEvents), Boolean.valueOf(to.allowEvents));
        }
        if (this.allowAlarms != to.allowAlarms) {
            d.addLine("allowAlarms", Boolean.valueOf(this.allowAlarms), Boolean.valueOf(to.allowAlarms));
        }
        ArraySet<String> allRules = new ArraySet();
        addKeys(allRules, this.automaticRules);
        addKeys(allRules, to.automaticRules);
        int N = allRules.size();
        for (int i = 0; i < N; i++) {
            ZenRule fromRule;
            ZenRule toRule;
            String rule = (String) allRules.valueAt(i);
            if (this.automaticRules != null) {
                fromRule = (ZenRule) this.automaticRules.get(rule);
            } else {
                fromRule = null;
            }
            if (to.automaticRules != null) {
                toRule = (ZenRule) to.automaticRules.get(rule);
            } else {
                toRule = null;
            }
            ZenRule.appendDiff(d, "automaticRule[" + rule + "]", fromRule, toRule);
        }
        ZenRule.appendDiff(d, "manualRule", this.manualRule, to.manualRule);
        return d;
    }

    public static Diff diff(ZenModeConfig from, ZenModeConfig to) {
        if (from != null) {
            return from.diff(to);
        }
        Diff d = new Diff();
        if (to == null) {
            return d;
        }
        d.addLine("config", "insert");
        return d;
    }

    private static <T> void addKeys(ArraySet<T> set, ArrayMap<T, ?> map) {
        if (map != null) {
            for (int i = 0; i < map.size(); i++) {
                set.add(map.keyAt(i));
            }
        }
    }

    public boolean isValid() {
        if (!isValidManualRule(this.manualRule)) {
            return false;
        }
        int N = this.automaticRules.size();
        for (int i = 0; i < N; i++) {
            if (!isValidAutomaticRule((ZenRule) this.automaticRules.valueAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isValidManualRule(ZenRule rule) {
        return rule == null || (Global.isValidZenMode(rule.zenMode) && sameCondition(rule));
    }

    private static boolean isValidAutomaticRule(ZenRule rule) {
        return (rule == null || TextUtils.isEmpty(rule.name) || !Global.isValidZenMode(rule.zenMode) || rule.conditionId == null || !sameCondition(rule)) ? false : true;
    }

    private static boolean sameCondition(ZenRule rule) {
        boolean z = true;
        if (rule == null) {
            return false;
        }
        if (rule.conditionId == null) {
            if (rule.condition != null) {
                z = false;
            }
            return z;
        } else if (rule.condition == null || rule.conditionId.equals(rule.condition.id)) {
            return true;
        } else {
            return false;
        }
    }

    private static int[] generateMinuteBuckets() {
        int[] buckets = new int[15];
        buckets[0] = 15;
        buckets[1] = 30;
        buckets[2] = 45;
        for (int i = 1; i <= 12; i++) {
            buckets[i + 2] = i * 60;
        }
        return buckets;
    }

    public static String sourceToString(int source) {
        switch (source) {
            case 0:
                return "anyone";
            case 1:
                return "contacts";
            case 2:
                return "stars";
            default:
                return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
        }
    }

    public boolean equals(Object o) {
        if (!(o instanceof ZenModeConfig)) {
            return false;
        }
        if (o == this) {
            return true;
        }
        ZenModeConfig other = (ZenModeConfig) o;
        if (other.allowCalls == this.allowCalls && other.allowRepeatCallers == this.allowRepeatCallers && other.allowMessages == this.allowMessages && other.allowCallsFrom == this.allowCallsFrom && other.allowMessagesFrom == this.allowMessagesFrom && other.allowReminders == this.allowReminders && other.allowEvents == this.allowEvents && other.allowAlarms == this.allowAlarms && other.user == this.user && Objects.equals(other.automaticRules, this.automaticRules) && Objects.equals(other.manualRule, this.manualRule)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Boolean.valueOf(this.allowCalls), Boolean.valueOf(this.allowRepeatCallers), Boolean.valueOf(this.allowMessages), Integer.valueOf(this.allowCallsFrom), Integer.valueOf(this.allowMessagesFrom), Boolean.valueOf(this.allowReminders), Boolean.valueOf(this.allowEvents), Integer.valueOf(this.user), Boolean.valueOf(this.allowAlarms), this.automaticRules, this.manualRule});
    }

    private static String toDayList(int[] days) {
        if (days == null || days.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < days.length; i++) {
            if (i > 0) {
                sb.append('.');
            }
            sb.append(days[i]);
        }
        return sb.toString();
    }

    private static int[] tryParseDayList(String dayList, String sep) {
        if (dayList == null) {
            return null;
        }
        String[] tokens = dayList.split(sep);
        if (tokens.length == 0) {
            return null;
        }
        int[] rt = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            int day = tryParseInt(tokens[i], -1);
            if (day == -1) {
                return null;
            }
            rt[i] = day;
        }
        return rt;
    }

    private static int tryParseInt(String value, int defValue) {
        if (!TextUtils.isEmpty(value)) {
            try {
                defValue = Integer.valueOf(value).intValue();
            } catch (NumberFormatException e) {
            }
        }
        return defValue;
    }

    private static long tryParseLong(String value, long defValue) {
        if (!TextUtils.isEmpty(value)) {
            try {
                defValue = Long.valueOf(value).longValue();
            } catch (NumberFormatException e) {
            }
        }
        return defValue;
    }

    public static ZenModeConfig readXml(XmlPullParser parser, Migration migration) throws XmlPullParserException, IOException {
        if (parser.getEventType() != 2) {
            return null;
        }
        if (!"zen".equals(parser.getName())) {
            return null;
        }
        ZenModeConfig rt = new ZenModeConfig();
        if (safeInt(parser, ZEN_ATT_VERSION, 2) == 1) {
            return migration.migrate(XmlV1.readXml(parser));
        }
        rt.user = safeInt(parser, "user", rt.user);
        boolean scheduleRuleFound = false;
        while (true) {
            int type = parser.next();
            if (type != 1) {
                String tag = parser.getName();
                if (type == 3 && "zen".equals(tag)) {
                    return rt;
                }
                if (type == 2) {
                    if (ALLOW_TAG.equals(tag)) {
                        rt.allowCalls = safeBoolean(parser, ALLOW_ATT_CALLS, false);
                        rt.allowRepeatCallers = safeBoolean(parser, ALLOW_ATT_REPEAT_CALLERS, false);
                        rt.allowMessages = safeBoolean(parser, ALLOW_ATT_MESSAGES, false);
                        rt.allowReminders = safeBoolean(parser, ALLOW_ATT_REMINDERS, false);
                        rt.allowEvents = safeBoolean(parser, ALLOW_ATT_EVENTS, false);
                        rt.allowAlarms = safeBoolean(parser, ALLOW_ATT_ALARMS, true);
                        int from = safeInt(parser, ALLOW_ATT_FROM, -1);
                        int callsFrom = safeInt(parser, ALLOW_ATT_CALLS_FROM, -1);
                        int messagesFrom = safeInt(parser, ALLOW_ATT_MESSAGES_FROM, -1);
                        if (isValidSource(callsFrom) && isValidSource(messagesFrom)) {
                            rt.allowCallsFrom = callsFrom;
                            rt.allowMessagesFrom = messagesFrom;
                        } else if (isValidSource(from)) {
                            Slog.i(TAG, "Migrating existing shared 'from': " + sourceToString(from));
                            rt.allowCallsFrom = from;
                            rt.allowMessagesFrom = from;
                        } else {
                            rt.allowCallsFrom = 1;
                            rt.allowMessagesFrom = 1;
                        }
                    } else if (MANUAL_TAG.equals(tag)) {
                        rt.manualRule = readRuleXml(parser);
                    } else if (AUTOMATIC_TAG.equals(tag)) {
                        String id = parser.getAttributeValue(null, RULE_ATT_ID);
                        ZenRule automaticRule = readRuleXml(parser);
                        if (!(id == null || automaticRule == null || !isValidScheduleConditionId(automaticRule.conditionId) || scheduleRuleFound)) {
                            scheduleRuleFound = true;
                            automaticRule.name = TW_SCHEDULE_PATH;
                            rt.automaticRules.put(id, automaticRule);
                        }
                    }
                }
            } else {
                throw new IllegalStateException("Failed to reach END_DOCUMENT");
            }
        }
    }

    public void writeXml(XmlSerializer out) throws IOException {
        out.startTag(null, "zen");
        out.attribute(null, ZEN_ATT_VERSION, Integer.toString(2));
        out.attribute(null, "user", Integer.toString(this.user));
        out.startTag(null, ALLOW_TAG);
        out.attribute(null, ALLOW_ATT_CALLS, Boolean.toString(this.allowCalls));
        out.attribute(null, ALLOW_ATT_REPEAT_CALLERS, Boolean.toString(this.allowRepeatCallers));
        out.attribute(null, ALLOW_ATT_MESSAGES, Boolean.toString(this.allowMessages));
        out.attribute(null, ALLOW_ATT_REMINDERS, Boolean.toString(this.allowReminders));
        out.attribute(null, ALLOW_ATT_EVENTS, Boolean.toString(this.allowEvents));
        out.attribute(null, ALLOW_ATT_ALARMS, Boolean.toString(this.allowAlarms));
        out.attribute(null, ALLOW_ATT_CALLS_FROM, Integer.toString(this.allowCallsFrom));
        out.attribute(null, ALLOW_ATT_MESSAGES_FROM, Integer.toString(this.allowMessagesFrom));
        out.endTag(null, ALLOW_TAG);
        if (this.manualRule != null) {
            out.startTag(null, MANUAL_TAG);
            writeRuleXml(this.manualRule, out);
            out.endTag(null, MANUAL_TAG);
        }
        int N = this.automaticRules.size();
        for (int i = 0; i < N; i++) {
            String id = (String) this.automaticRules.keyAt(i);
            ZenRule automaticRule = (ZenRule) this.automaticRules.valueAt(i);
            out.startTag(null, AUTOMATIC_TAG);
            out.attribute(null, RULE_ATT_ID, id);
            writeRuleXml(automaticRule, out);
            out.endTag(null, AUTOMATIC_TAG);
        }
        out.endTag(null, "zen");
    }

    public static ZenRule readRuleXml(XmlPullParser parser) {
        ZenRule rt = new ZenRule();
        rt.enabled = safeBoolean(parser, RULE_ATT_ENABLED, true);
        rt.snoozing = safeBoolean(parser, RULE_ATT_SNOOZING, false);
        rt.name = parser.getAttributeValue(null, "name");
        String zen = parser.getAttributeValue(null, "zen");
        rt.zenMode = tryParseZenMode(zen, -1);
        if (rt.zenMode == -1) {
            Slog.w(TAG, "Bad zen mode in rule xml:" + zen);
            return null;
        }
        rt.conditionId = safeUri(parser, RULE_ATT_CONDITION_ID);
        rt.component = safeComponentName(parser, "component");
        rt.condition = readConditionXml(parser);
        return rt;
    }

    public static void writeRuleXml(ZenRule rule, XmlSerializer out) throws IOException {
        out.attribute(null, RULE_ATT_ENABLED, Boolean.toString(rule.enabled));
        out.attribute(null, RULE_ATT_SNOOZING, Boolean.toString(rule.snoozing));
        if (rule.name != null) {
            out.attribute(null, "name", rule.name);
        }
        out.attribute(null, "zen", Integer.toString(rule.zenMode));
        if (rule.component != null) {
            out.attribute(null, "component", rule.component.flattenToString());
        }
        if (rule.conditionId != null) {
            out.attribute(null, RULE_ATT_CONDITION_ID, rule.conditionId.toString());
        }
        if (rule.condition != null) {
            writeConditionXml(rule.condition, out);
        }
    }

    public static Condition readConditionXml(XmlPullParser parser) {
        Uri id = safeUri(parser, CONDITION_ATT_ID);
        if (id == null) {
            return null;
        }
        try {
            return new Condition(id, parser.getAttributeValue(null, CONDITION_ATT_SUMMARY), parser.getAttributeValue(null, CONDITION_ATT_LINE1), parser.getAttributeValue(null, CONDITION_ATT_LINE2), safeInt(parser, CONDITION_ATT_ICON, -1), safeInt(parser, "state", -1), safeInt(parser, CONDITION_ATT_FLAGS, -1));
        } catch (IllegalArgumentException e) {
            Slog.w(TAG, "Unable to read condition xml", e);
            return null;
        }
    }

    public static void writeConditionXml(Condition c, XmlSerializer out) throws IOException {
        out.attribute(null, CONDITION_ATT_ID, c.id.toString());
        out.attribute(null, CONDITION_ATT_SUMMARY, c.summary);
        out.attribute(null, CONDITION_ATT_LINE1, c.line1);
        out.attribute(null, CONDITION_ATT_LINE2, c.line2);
        out.attribute(null, CONDITION_ATT_ICON, Integer.toString(c.icon));
        out.attribute(null, "state", Integer.toString(c.state));
        out.attribute(null, CONDITION_ATT_FLAGS, Integer.toString(c.flags));
    }

    public static boolean isValidHour(int val) {
        return val >= 0 && val < 24;
    }

    public static boolean isValidMinute(int val) {
        return val >= 0 && val < 60;
    }

    private static boolean isValidSource(int source) {
        return source >= 0 && source <= 2;
    }

    private static boolean safeBoolean(XmlPullParser parser, String att, boolean defValue) {
        String val = parser.getAttributeValue(null, att);
        return TextUtils.isEmpty(val) ? defValue : Boolean.valueOf(val).booleanValue();
    }

    private static int safeInt(XmlPullParser parser, String att, int defValue) {
        return tryParseInt(parser.getAttributeValue(null, att), defValue);
    }

    private static ComponentName safeComponentName(XmlPullParser parser, String att) {
        String val = parser.getAttributeValue(null, att);
        if (TextUtils.isEmpty(val)) {
            return null;
        }
        return ComponentName.unflattenFromString(val);
    }

    private static Uri safeUri(XmlPullParser parser, String att) {
        String val = parser.getAttributeValue(null, att);
        if (TextUtils.isEmpty(val)) {
            return null;
        }
        return Uri.parse(val);
    }

    public ArraySet<String> getAutomaticRuleNames() {
        ArraySet<String> rt = new ArraySet();
        for (int i = 0; i < this.automaticRules.size(); i++) {
            rt.add(((ZenRule) this.automaticRules.valueAt(i)).name);
        }
        return rt;
    }

    public int describeContents() {
        return 0;
    }

    public ZenModeConfig copy() {
        Parcel parcel = Parcel.obtain();
        try {
            writeToParcel(parcel, 0);
            parcel.setDataPosition(0);
            ZenModeConfig zenModeConfig = new ZenModeConfig(parcel);
            return zenModeConfig;
        } finally {
            parcel.recycle();
        }
    }

    public Policy toNotificationPolicy() {
        int priorityCategories = 0;
        if (this.allowCalls) {
            priorityCategories = 0 | 8;
        }
        if (this.allowMessages) {
            priorityCategories |= 4;
        }
        if (this.allowEvents) {
            priorityCategories |= 2;
        }
        if (this.allowReminders) {
            priorityCategories |= 1;
        }
        if (this.allowRepeatCallers) {
            priorityCategories |= 16;
        }
        return new Policy(priorityCategories, sourceToPrioritySenders(this.allowCallsFrom, 1), sourceToPrioritySenders(this.allowMessagesFrom, 1));
    }

    private static int sourceToPrioritySenders(int source, int def) {
        switch (source) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return def;
        }
    }

    private static int prioritySendersToSource(int prioritySenders, int def) {
        switch (prioritySenders) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return def;
        }
    }

    public void applyNotificationPolicy(Policy policy) {
        boolean z = true;
        if (policy != null) {
            boolean z2;
            this.allowCalls = (policy.priorityCategories & 8) != 0;
            if ((policy.priorityCategories & 4) != 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            this.allowMessages = z2;
            if ((policy.priorityCategories & 2) != 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            this.allowEvents = z2;
            if ((policy.priorityCategories & 1) != 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            this.allowReminders = z2;
            if ((policy.priorityCategories & 16) == 0) {
                z = false;
            }
            this.allowRepeatCallers = z;
            this.allowCallsFrom = prioritySendersToSource(policy.priorityCallSenders, this.allowCallsFrom);
            this.allowMessagesFrom = prioritySendersToSource(policy.priorityMessageSenders, this.allowMessagesFrom);
        }
    }

    public static Condition toTimeCondition(Context context, int minutesFromNow, int userHandle) {
        return toTimeCondition(context, minutesFromNow, userHandle, false);
    }

    public static Condition toTimeCondition(Context context, int minutesFromNow, int userHandle, boolean shortVersion) {
        return toTimeCondition(context, System.currentTimeMillis() + (minutesFromNow == 0 ? 10000 : (long) (MINUTES_MS * minutesFromNow)), minutesFromNow, userHandle, shortVersion);
    }

    public static Condition toTimeCondition(Context context, long time, int minutes, int userHandle, boolean shortVersion) {
        String summary;
        String line1;
        String line2;
        CharSequence formattedTime = getFormattedTime(context, time, userHandle);
        Resources res = context.getResources();
        int num;
        if (minutes < 60) {
            num = minutes;
            summary = res.getQuantityString(shortVersion ? R.plurals.zen_mode_duration_minutes_summary_short : R.plurals.zen_mode_duration_minutes_summary, num, new Object[]{Integer.valueOf(num), formattedTime});
            line1 = res.getQuantityString(shortVersion ? R.plurals.zen_mode_duration_minutes_short : R.plurals.zen_mode_duration_minutes, num, new Object[]{Integer.valueOf(num), formattedTime});
            line2 = res.getString(R.string.zen_mode_until, new Object[]{formattedTime});
        } else if (minutes < DAY_MINUTES) {
            num = Math.round(((float) minutes) / 60.0f);
            summary = res.getQuantityString(shortVersion ? R.plurals.zen_mode_duration_hours_summary_short : R.plurals.zen_mode_duration_hours_summary, num, new Object[]{Integer.valueOf(num), formattedTime});
            line1 = res.getQuantityString(shortVersion ? R.plurals.zen_mode_duration_hours_short : R.plurals.zen_mode_duration_hours, num, new Object[]{Integer.valueOf(num), formattedTime});
            line2 = res.getString(R.string.zen_mode_until, new Object[]{formattedTime});
        } else {
            line2 = res.getString(R.string.zen_mode_until, new Object[]{formattedTime});
            line1 = line2;
            summary = line2;
        }
        return new Condition(toCountdownConditionId(time), summary, line1, line2, 0, 1, 1);
    }

    public static Condition toNextAlarmCondition(Context context, long now, long alarm, int userHandle) {
        CharSequence formattedTime = getFormattedTime(context, alarm, userHandle);
        return new Condition(toCountdownConditionId(alarm), "", context.getResources().getString(R.string.zen_mode_alarm, new Object[]{formattedTime}), "", 0, 1, 1);
    }

    private static CharSequence getFormattedTime(Context context, long time, int userHandle) {
        String skeleton = "EEE " + (DateFormat.is24HourFormat(context, userHandle) ? "Hm" : "hma");
        GregorianCalendar now = new GregorianCalendar();
        GregorianCalendar endTime = new GregorianCalendar();
        endTime.setTimeInMillis(time);
        if (now.get(1) == endTime.get(1) && now.get(2) == endTime.get(2) && now.get(5) == endTime.get(5)) {
            skeleton = DateFormat.is24HourFormat(context, userHandle) ? "Hm" : "hma";
        }
        return DateFormat.format(DateFormat.getBestDateTimePattern(Locale.getDefault(), skeleton), time);
    }

    public static Uri toCountdownConditionId(long time) {
        return new Builder().scheme("condition").authority(SYSTEM_AUTHORITY).appendPath(COUNTDOWN_PATH).appendPath(Long.toString(time)).build();
    }

    public static long tryParseCountdownConditionId(Uri conditionId) {
        long j = 0;
        if (Condition.isValidId(conditionId, SYSTEM_AUTHORITY) && conditionId.getPathSegments().size() == 2 && COUNTDOWN_PATH.equals(conditionId.getPathSegments().get(0))) {
            try {
                j = Long.parseLong((String) conditionId.getPathSegments().get(1));
            } catch (RuntimeException e) {
                Slog.w(TAG, "Error parsing countdown condition: " + conditionId, e);
            }
        }
        return j;
    }

    public static boolean isValidCountdownConditionId(Uri conditionId) {
        return tryParseCountdownConditionId(conditionId) != 0;
    }

    public static Uri toScheduleConditionId(ScheduleInfo schedule) {
        return new Builder().scheme("condition").authority(SYSTEM_AUTHORITY).appendPath(SCHEDULE_PATH).appendQueryParameter("days", toDayList(schedule.days)).appendQueryParameter("start", schedule.startHour + "." + schedule.startMinute).appendQueryParameter("end", schedule.endHour + "." + schedule.endMinute).build();
    }

    public static boolean isValidScheduleConditionId(Uri conditionId) {
        return tryParseScheduleConditionId(conditionId) != null;
    }

    public static ScheduleInfo tryParseScheduleConditionId(Uri conditionId) {
        boolean isSchedule;
        if (conditionId != null && conditionId.getScheme().equals("condition") && conditionId.getAuthority().equals(SYSTEM_AUTHORITY) && conditionId.getPathSegments().size() == 1 && ((String) conditionId.getPathSegments().get(0)).equals(SCHEDULE_PATH)) {
            isSchedule = true;
        } else {
            isSchedule = false;
        }
        if (!isSchedule) {
            return null;
        }
        int[] start = tryParseHourAndMinute(conditionId.getQueryParameter("start"));
        int[] end = tryParseHourAndMinute(conditionId.getQueryParameter("end"));
        if (start == null || end == null) {
            return null;
        }
        ScheduleInfo rt = new ScheduleInfo();
        rt.days = tryParseDayList(conditionId.getQueryParameter("days"), "\\.");
        rt.startHour = start[0];
        rt.startMinute = start[1];
        rt.endHour = end[0];
        rt.endMinute = end[1];
        return rt;
    }

    public static Uri toEventConditionId(EventInfo event) {
        return new Builder().scheme("condition").authority(SYSTEM_AUTHORITY).appendPath(EVENT_PATH).appendQueryParameter(FingerprintEvent.IDENTIFY_INFO_KEY_USER_ID, Long.toString((long) event.userId)).appendQueryParameter("calendar", event.calendar != null ? event.calendar : "").appendQueryParameter("reply", Integer.toString(event.reply)).build();
    }

    public static boolean isValidEventConditionId(Uri conditionId) {
        return tryParseEventConditionId(conditionId) != null;
    }

    public static EventInfo tryParseEventConditionId(Uri conditionId) {
        boolean isEvent = true;
        if (!(conditionId != null && conditionId.getScheme().equals("condition") && conditionId.getAuthority().equals(SYSTEM_AUTHORITY) && conditionId.getPathSegments().size() == 1 && ((String) conditionId.getPathSegments().get(0)).equals(EVENT_PATH))) {
            isEvent = false;
        }
        if (!isEvent) {
            return null;
        }
        EventInfo rt = new EventInfo();
        rt.userId = tryParseInt(conditionId.getQueryParameter(FingerprintEvent.IDENTIFY_INFO_KEY_USER_ID), DeviceRootKeyServiceManager.ERR_SERVICE_ERROR);
        rt.calendar = conditionId.getQueryParameter("calendar");
        if (TextUtils.isEmpty(rt.calendar) || tryParseLong(rt.calendar, -1) != -1) {
            rt.calendar = null;
        }
        rt.reply = tryParseInt(conditionId.getQueryParameter("reply"), 0);
        return rt;
    }

    private static int[] tryParseHourAndMinute(String value) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }
        int i = value.indexOf(46);
        if (i < 1 || i >= value.length() - 1) {
            return null;
        }
        int hour = tryParseInt(value.substring(0, i), -1);
        int minute = tryParseInt(value.substring(i + 1), -1);
        if (!isValidHour(hour) || !isValidMinute(minute)) {
            return null;
        }
        return new int[]{hour, minute};
    }

    private static int tryParseZenMode(String value, int defValue) {
        int rt = tryParseInt(value, defValue);
        return Global.isValidZenMode(rt) ? rt : defValue;
    }

    public String newRuleId() {
        return UUID.randomUUID().toString().replace(NativeLibraryHelper.CLEAR_ABI_OVERRIDE, "");
    }

    public static String getConditionSummary(Context context, ZenModeConfig config, int userHandle, boolean shortVersion) {
        return getConditionLine(context, config, userHandle, false, shortVersion);
    }

    private static String getConditionLine(Context context, ZenModeConfig config, int userHandle, boolean useLine1, boolean shortVersion) {
        if (config == null) {
            return "";
        }
        if (config.manualRule != null) {
            Uri id = config.manualRule.conditionId;
            if (id == null) {
                return context.getString(R.string.zen_mode_forever);
            }
            long time = tryParseCountdownConditionId(id);
            Condition c = config.manualRule.condition;
            if (time > 0) {
                c = toTimeCondition(context, time, Math.round(((float) (time - System.currentTimeMillis())) / 60000.0f), userHandle, shortVersion);
            }
            String rt = c == null ? "" : useLine1 ? c.line1 : c.summary;
            if (TextUtils.isEmpty(rt)) {
                return "";
            }
            return rt;
        }
        String summary = "";
        for (ZenRule automaticRule : config.automaticRules.values()) {
            if (automaticRule.isAutomaticActive()) {
                if (summary.isEmpty()) {
                    summary = automaticRule.name;
                } else {
                    summary = context.getResources().getString(R.string.zen_mode_rule_name_combination, new Object[]{summary, automaticRule.name});
                }
            }
        }
        return summary;
    }
}
