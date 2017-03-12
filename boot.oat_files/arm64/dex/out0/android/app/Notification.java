package android.app;

import android.R;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PersonaInfo;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.AudioAttributes;
import android.media.session.MediaSession.Token;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.BadParcelableException;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import com.android.internal.util.NotificationColorUtil;
import java.lang.reflect.Constructor;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Notification implements Parcelable {
    public static final AudioAttributes AUDIO_ATTRIBUTES_DEFAULT = new android.media.AudioAttributes.Builder().setContentType(4).setUsage(5).build();
    public static final String CATEGORY_ALARM = "alarm";
    public static final String CATEGORY_CALL = "call";
    public static final String CATEGORY_EMAIL = "email";
    public static final String CATEGORY_ERROR = "err";
    public static final String CATEGORY_EVENT = "event";
    public static final String CATEGORY_MESSAGE = "msg";
    public static final String CATEGORY_PROGRESS = "progress";
    public static final String CATEGORY_PROMO = "promo";
    public static final String CATEGORY_RECOMMENDATION = "recommendation";
    public static final String CATEGORY_REMINDER = "reminder";
    public static final String CATEGORY_SERVICE = "service";
    public static final String CATEGORY_SOCIAL = "social";
    public static final String CATEGORY_STATUS = "status";
    public static final String CATEGORY_SYSTEM = "sys";
    public static final String CATEGORY_TRANSPORT = "transport";
    public static final int COLOR_DEFAULT = 0;
    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        public Notification createFromParcel(Parcel parcel) {
            return new Notification(parcel);
        }

        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };
    private static final boolean DEBUG;
    public static final int DEFAULT_ALL = -1;
    public static final int DEFAULT_LIGHTS = 4;
    public static final int DEFAULT_SOUND = 1;
    public static final int DEFAULT_VIBRATE = 2;
    public static final String EXTRA_ALLOW_DURING_SETUP = "android.allowDuringSetup";
    public static final String EXTRA_AS_HEADS_UP = "headsup";
    public static final String EXTRA_BACKGROUND_IMAGE_URI = "android.backgroundImageUri";
    public static final String EXTRA_BIG_TEXT = "android.bigText";
    public static final String EXTRA_COMPACT_ACTIONS = "android.compactActions";
    public static final String EXTRA_INFO_TEXT = "android.infoText";
    public static final String EXTRA_LARGE_ICON = "android.largeIcon";
    public static final String EXTRA_LARGE_ICON_BIG = "android.largeIcon.big";
    public static final String EXTRA_MEDIA_SESSION = "android.mediaSession";
    public static final String EXTRA_ORIGINATING_USERID = "android.originatingUserId";
    public static final String EXTRA_PEOPLE = "android.people";
    public static final String EXTRA_PICTURE = "android.picture";
    public static final String EXTRA_PROGRESS = "android.progress";
    public static final String EXTRA_PROGRESS_INDETERMINATE = "android.progressIndeterminate";
    public static final String EXTRA_PROGRESS_MAX = "android.progressMax";
    public static final String EXTRA_SHOW_CHRONOMETER = "android.showChronometer";
    public static final String EXTRA_SHOW_WHEN = "android.showWhen";
    public static final String EXTRA_SMALL_ICON = "android.icon";
    public static final String EXTRA_SUB_TEXT = "android.subText";
    public static final String EXTRA_SUMMARY_TEXT = "android.summaryText";
    public static final String EXTRA_TEMPLATE = "android.template";
    public static final String EXTRA_TEXT = "android.text";
    public static final String EXTRA_TEXT_LINES = "android.textLines";
    public static final String EXTRA_TITLE = "android.title";
    public static final String EXTRA_TITLE_BIG = "android.title.big";
    public static final int FLAG_AUTO_CANCEL = 16;
    public static final int FLAG_DISABLE_STATUS_BAR_OPEN_FULLSCREEN = 4096;
    public static final int FLAG_FOREGROUND_SERVICE = 64;
    public static final int FLAG_GROUP_SUMMARY = 512;
    public static final int FLAG_HIGH_PRIORITY = 128;
    public static final int FLAG_INSISTENT = 4;
    public static final int FLAG_IS_NEGATIVE = 2;
    public static final int FLAG_IS_ZERO = 4;
    public static final int FLAG_LOCAL_ONLY = 256;
    public static final int FLAG_NO_CLEAR = 32;
    public static final int FLAG_ONGOING_EVENT = 2;
    public static final int FLAG_ONLY_ALERT_ONCE = 8;
    public static final int FLAG_SHOW_LIGHTS = 1;
    public static final int FLAG_SYNCED = 1;
    public static final int FORCE_LED_BLINKING = 1024;
    public static final int HEADS_UP_ALLOWED = 1;
    public static final int HEADS_UP_NEVER = 0;
    public static final int HEADS_UP_REQUESTED = 2;
    public static final String INTENT_CATEGORY_NOTIFICATION_PREFERENCES = "android.intent.category.NOTIFICATION_PREFERENCES";
    public static final int MAGIC_NUMBER_HIDE_ACTION_BUTTON_ICON = 538976288;
    private static final int MAX_CHARSEQUENCE_LENGTH = 5120;
    public static final int PACKAGE_VISIBILITY_HIDE_ON_INDICATOR = 4;
    public static final int PACKAGE_VISIBILITY_HIDE_ON_LOCKSCREEN = 2;
    public static final int PACKAGE_VISIBILITY_PRIVATE = 1;
    public static final int PRIORITY_DEFAULT = 0;
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_LOW = -1;
    public static final int PRIORITY_MAX = 2;
    public static final int PRIORITY_MIN = -2;
    public static final int SEC_FLAG_FORCE_NOTIFICATION_LED = 8;
    public static final int SEC_FLAG_HIDE_ACTION_BUTTON_ICON = 4;
    public static final int SEC_FLAG_HIDE_NOTIFICATION_ICON = 2;
    public static final int SEC_FLAG_HIDE_NOTIFICATION_ON_PANEL = 1;
    public static final int SEC_PRIORITY_DEFAULT = 0;
    public static final int SEC_PRIORITY_HIGH = 10;
    public static final int SEC_PRIORITY_MAX = 20;
    @Deprecated
    public static final int STREAM_DEFAULT = -1;
    private static final String TAG = "Notification";
    public static final int TWQUICKPANEL_NOTIFICATION_CALL = 1;
    public static final int TWQUICKPANEL_NOTIFICATION_CMAS = 512;
    public static final int TWQUICKPANEL_NOTIFICATION_CONTEXTAWARE = 16;
    public static final int TWQUICKPANEL_NOTIFICATION_INVISIBLE_ICON = 64;
    public static final int TWQUICKPANEL_NOTIFICATION_KIDSMODE = 256;
    public static final int TWQUICKPANEL_NOTIFICATION_KNOXMODE = 128;
    public static final int TWQUICKPANEL_NOTIFICATION_MUSIC = 2;
    public static final int TWQUICKPANEL_NOTIFICATION_MUSICHUB = 32;
    public static final int TWQUICKPANEL_NOTIFICATION_NETWORKBOOSTER = 264;
    public static final int TWQUICKPANEL_NOTIFICATION_RADIO = 8;
    public static final int TWQUICKPANEL_NOTIFICATION_SMARTREMOTE = 257;
    public static final int TWQUICKPANEL_NOTIFICATION_STALKIE = 258;
    public static final int TWQUICKPANEL_NOTIFICATION_TOOLBOX = 260;
    public static final int TWQUICKPANEL_NOTIFICATION_VOICERECORD = 4;
    public static final int VISIBILITY_HIDE_ON_LOCKSCREEN = -2;
    public static final int VISIBILITY_PRIVATE = 0;
    public static final int VISIBILITY_PUBLIC = 1;
    public static final int VISIBILITY_SECRET = -1;
    public Action[] actions;
    public AudioAttributes audioAttributes;
    @Deprecated
    public int audioStreamType;
    public RemoteViews bigContentView;
    public String category;
    public int color;
    public int commonValue;
    public CharSequence contactCharSeq;
    public PendingIntent contentIntent;
    public RemoteViews contentView;
    public int defaults;
    public PendingIntent deleteIntent;
    public Bundle extras;
    public int flags;
    public PendingIntent fullScreenIntent;
    public int haptic;
    public RemoteViews headsUpContentView;
    @Deprecated
    public int icon;
    public int iconLevel;
    public int knoxFlags;
    @Deprecated
    public Bitmap largeIcon;
    public int ledARGB;
    public int ledOffMS;
    public int ledOnMS;
    private String mGroupKey;
    private Icon mLargeIcon;
    private Icon mSmallIcon;
    private String mSortKey;
    public int missedCount;
    public int number;
    public CharSequence originalPackageName;
    public int originalUserId;
    public int priority;
    public Notification publicVersion;
    public int secFlags;
    public int secPriority;
    public Uri sound;
    private Map<CharSequence, CharSequence> stringNamesMap;
    public long threadId;
    public CharSequence tickerText;
    @Deprecated
    public RemoteViews tickerView;
    public int twQuickPanelEvent;
    public long[] vibrate;
    public int visibility;
    public long when;

    public static class Action implements Parcelable {
        public static final Creator<Action> CREATOR = new Creator<Action>() {
            public Action createFromParcel(Parcel in) {
                return new Action(in);
            }

            public Action[] newArray(int size) {
                return new Action[size];
            }
        };
        public PendingIntent actionIntent;
        @Deprecated
        public int icon;
        private final Bundle mExtras;
        private Icon mIcon;
        private final RemoteInput[] mRemoteInputs;
        public CharSequence title;

        public static final class Builder {
            private final Bundle mExtras;
            private final Icon mIcon;
            private final PendingIntent mIntent;
            private ArrayList<RemoteInput> mRemoteInputs;
            private final CharSequence mTitle;

            @Deprecated
            public Builder(int icon, CharSequence title, PendingIntent intent) {
                this(Icon.createWithResource(ProxyInfo.LOCAL_EXCL_LIST, icon), title, intent, new Bundle(), null);
            }

            public Builder(Icon icon, CharSequence title, PendingIntent intent) {
                this(icon, title, intent, new Bundle(), null);
            }

            public Builder(Action action) {
                this(action.getIcon(), action.title, action.actionIntent, new Bundle(action.mExtras), action.getRemoteInputs());
            }

            private Builder(Icon icon, CharSequence title, PendingIntent intent, Bundle extras, RemoteInput[] remoteInputs) {
                this.mIcon = icon;
                this.mTitle = title;
                this.mIntent = intent;
                this.mExtras = extras;
                if (remoteInputs != null) {
                    this.mRemoteInputs = new ArrayList(remoteInputs.length);
                    Collections.addAll(this.mRemoteInputs, remoteInputs);
                }
            }

            public Builder addExtras(Bundle extras) {
                if (extras != null) {
                    this.mExtras.putAll(extras);
                }
                return this;
            }

            public Bundle getExtras() {
                return this.mExtras;
            }

            public Builder addRemoteInput(RemoteInput remoteInput) {
                if (this.mRemoteInputs == null) {
                    this.mRemoteInputs = new ArrayList();
                }
                this.mRemoteInputs.add(remoteInput);
                return this;
            }

            public Builder extend(Extender extender) {
                extender.extend(this);
                return this;
            }

            public Action build() {
                RemoteInput[] remoteInputs;
                if (this.mRemoteInputs != null) {
                    remoteInputs = (RemoteInput[]) this.mRemoteInputs.toArray(new RemoteInput[this.mRemoteInputs.size()]);
                } else {
                    remoteInputs = null;
                }
                return new Action(this.mIcon, this.mTitle, this.mIntent, this.mExtras, remoteInputs);
            }
        }

        public interface Extender {
            Builder extend(Builder builder);
        }

        public static final class WearableExtender implements Extender {
            private static final int DEFAULT_FLAGS = 1;
            private static final String EXTRA_WEARABLE_EXTENSIONS = "android.wearable.EXTENSIONS";
            private static final int FLAG_AVAILABLE_OFFLINE = 1;
            private static final String KEY_CANCEL_LABEL = "cancelLabel";
            private static final String KEY_CONFIRM_LABEL = "confirmLabel";
            private static final String KEY_FLAGS = "flags";
            private static final String KEY_IN_PROGRESS_LABEL = "inProgressLabel";
            private CharSequence mCancelLabel;
            private CharSequence mConfirmLabel;
            private int mFlags = 1;
            private CharSequence mInProgressLabel;

            public WearableExtender(Action action) {
                Bundle wearableBundle = action.getExtras().getBundle(EXTRA_WEARABLE_EXTENSIONS);
                if (wearableBundle != null) {
                    this.mFlags = wearableBundle.getInt("flags", 1);
                    this.mInProgressLabel = wearableBundle.getCharSequence(KEY_IN_PROGRESS_LABEL);
                    this.mConfirmLabel = wearableBundle.getCharSequence(KEY_CONFIRM_LABEL);
                    this.mCancelLabel = wearableBundle.getCharSequence(KEY_CANCEL_LABEL);
                }
            }

            public Builder extend(Builder builder) {
                Bundle wearableBundle = new Bundle();
                if (this.mFlags != 1) {
                    wearableBundle.putInt("flags", this.mFlags);
                }
                if (this.mInProgressLabel != null) {
                    wearableBundle.putCharSequence(KEY_IN_PROGRESS_LABEL, this.mInProgressLabel);
                }
                if (this.mConfirmLabel != null) {
                    wearableBundle.putCharSequence(KEY_CONFIRM_LABEL, this.mConfirmLabel);
                }
                if (this.mCancelLabel != null) {
                    wearableBundle.putCharSequence(KEY_CANCEL_LABEL, this.mCancelLabel);
                }
                builder.getExtras().putBundle(EXTRA_WEARABLE_EXTENSIONS, wearableBundle);
                return builder;
            }

            public WearableExtender clone() {
                WearableExtender that = new WearableExtender();
                that.mFlags = this.mFlags;
                that.mInProgressLabel = this.mInProgressLabel;
                that.mConfirmLabel = this.mConfirmLabel;
                that.mCancelLabel = this.mCancelLabel;
                return that;
            }

            public WearableExtender setAvailableOffline(boolean availableOffline) {
                setFlag(1, availableOffline);
                return this;
            }

            public boolean isAvailableOffline() {
                return (this.mFlags & 1) != 0;
            }

            private void setFlag(int mask, boolean value) {
                if (value) {
                    this.mFlags |= mask;
                } else {
                    this.mFlags &= mask ^ -1;
                }
            }

            public WearableExtender setInProgressLabel(CharSequence label) {
                this.mInProgressLabel = label;
                return this;
            }

            public CharSequence getInProgressLabel() {
                return this.mInProgressLabel;
            }

            public WearableExtender setConfirmLabel(CharSequence label) {
                this.mConfirmLabel = label;
                return this;
            }

            public CharSequence getConfirmLabel() {
                return this.mConfirmLabel;
            }

            public WearableExtender setCancelLabel(CharSequence label) {
                this.mCancelLabel = label;
                return this;
            }

            public CharSequence getCancelLabel() {
                return this.mCancelLabel;
            }
        }

        private Action(Parcel in) {
            if (in.readInt() != 0) {
                this.mIcon = (Icon) Icon.CREATOR.createFromParcel(in);
                if (this.mIcon.getType() == 2) {
                    this.icon = this.mIcon.getResId();
                }
            }
            this.title = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            if (in.readInt() == 1) {
                this.actionIntent = (PendingIntent) PendingIntent.CREATOR.createFromParcel(in);
            }
            this.mExtras = in.readBundle();
            this.mRemoteInputs = (RemoteInput[]) in.createTypedArray(RemoteInput.CREATOR);
        }

        @Deprecated
        public Action(int icon, CharSequence title, PendingIntent intent) {
            this(Icon.createWithResource(ProxyInfo.LOCAL_EXCL_LIST, icon), title, intent, new Bundle(), null);
        }

        private Action(Icon icon, CharSequence title, PendingIntent intent, Bundle extras, RemoteInput[] remoteInputs) {
            this.mIcon = icon;
            this.title = title;
            this.actionIntent = intent;
            if (extras == null) {
                extras = new Bundle();
            }
            this.mExtras = extras;
            this.mRemoteInputs = remoteInputs;
        }

        public Icon getIcon() {
            if (this.mIcon == null && this.icon != 0) {
                this.mIcon = Icon.createWithResource(ProxyInfo.LOCAL_EXCL_LIST, this.icon);
            }
            return this.mIcon;
        }

        public Bundle getExtras() {
            return this.mExtras;
        }

        public RemoteInput[] getRemoteInputs() {
            return this.mRemoteInputs;
        }

        public Action clone() {
            return new Action(getIcon(), this.title, this.actionIntent, new Bundle(this.mExtras), getRemoteInputs());
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            Icon ic = getIcon();
            if (ic != null) {
                out.writeInt(1);
                ic.writeToParcel(out, 0);
            } else {
                out.writeInt(0);
            }
            TextUtils.writeToParcel(this.title, out, flags);
            if (this.actionIntent != null) {
                out.writeInt(1);
                this.actionIntent.writeToParcel(out, flags);
            } else {
                out.writeInt(0);
            }
            out.writeBundle(this.mExtras);
            out.writeTypedArray(this.mRemoteInputs, flags);
        }
    }

    public static abstract class Style {
        private CharSequence mBigContentTitle;
        protected Builder mBuilder;
        protected CharSequence mSummaryText = null;
        protected boolean mSummaryTextSet = false;

        protected void internalSetBigContentTitle(CharSequence title) {
            this.mBigContentTitle = title;
        }

        protected void internalSetSummaryText(CharSequence cs) {
            this.mSummaryText = cs;
            this.mSummaryTextSet = true;
        }

        public void setBuilder(Builder builder) {
            if (this.mBuilder != builder) {
                this.mBuilder = builder;
                if (this.mBuilder != null) {
                    this.mBuilder.setStyle(this);
                }
            }
        }

        protected void checkBuilder() {
            if (this.mBuilder == null) {
                throw new IllegalArgumentException("Style requires a valid Builder object");
            }
        }

        protected RemoteViews getStandardView(int layoutId) {
            checkBuilder();
            CharSequence oldBuilderContentTitle = this.mBuilder.mContentTitle;
            if (this.mBigContentTitle != null) {
                this.mBuilder.setContentTitle(this.mBigContentTitle);
            }
            RemoteViews contentView = this.mBuilder.applyStandardTemplateWithActions(layoutId);
            this.mBuilder.mContentTitle = oldBuilderContentTitle;
            if (this.mBigContentTitle == null || !this.mBigContentTitle.equals(ProxyInfo.LOCAL_EXCL_LIST)) {
                contentView.setViewVisibility(16909369, 0);
            } else {
                contentView.setViewVisibility(16909369, 8);
            }
            CharSequence overflowText = this.mSummaryTextSet ? this.mSummaryText : this.mBuilder.mSubText;
            if (overflowText != null) {
                contentView.setTextViewText(16908419, this.mBuilder.processLegacyText(overflowText));
                contentView.setViewVisibility(16909358, 0);
                contentView.setViewVisibility(16909371, 0);
            } else {
                contentView.setTextViewText(16908419, ProxyInfo.LOCAL_EXCL_LIST);
                contentView.setViewVisibility(16909358, 8);
                contentView.setViewVisibility(16909371, 8);
            }
            return contentView;
        }

        protected void applyTopPadding(RemoteViews contentView) {
            contentView.setViewPadding(16909369, 0, Builder.calculateTopPadding(this.mBuilder.mContext, this.mBuilder.mHasThreeLines, this.mBuilder.mContext.getResources().getConfiguration().fontScale), 0, 0);
        }

        public void addExtras(Bundle extras) {
            if (this.mSummaryTextSet) {
                extras.putCharSequence(Notification.EXTRA_SUMMARY_TEXT, this.mSummaryText);
            }
            if (this.mBigContentTitle != null) {
                extras.putCharSequence(Notification.EXTRA_TITLE_BIG, this.mBigContentTitle);
            }
            extras.putString(Notification.EXTRA_TEMPLATE, getClass().getName());
        }

        protected void restoreFromExtras(Bundle extras) {
            if (extras.containsKey(Notification.EXTRA_SUMMARY_TEXT)) {
                this.mSummaryText = extras.getCharSequence(Notification.EXTRA_SUMMARY_TEXT);
                this.mSummaryTextSet = true;
            }
            if (extras.containsKey(Notification.EXTRA_TITLE_BIG)) {
                this.mBigContentTitle = extras.getCharSequence(Notification.EXTRA_TITLE_BIG);
            }
        }

        public Notification buildStyled(Notification wip) {
            populateTickerView(wip);
            populateContentView(wip);
            populateBigContentView(wip);
            populateHeadsUpContentView(wip);
            return wip;
        }

        public void purgeResources() {
        }

        protected void populateTickerView(Notification wip) {
        }

        protected void populateContentView(Notification wip) {
        }

        protected void populateBigContentView(Notification wip) {
        }

        protected void populateHeadsUpContentView(Notification wip) {
        }

        public Notification build() {
            checkBuilder();
            return this.mBuilder.build();
        }

        protected boolean hasProgress() {
            return true;
        }
    }

    public static class BigPictureStyle extends Style {
        public static final int MIN_ASHMEM_BITMAP_SIZE = 131072;
        private Icon mBigLargeIcon;
        private boolean mBigLargeIconSet = false;
        private Bitmap mPicture;

        public BigPictureStyle(Builder builder) {
            setBuilder(builder);
        }

        public BigPictureStyle setBigContentTitle(CharSequence title) {
            internalSetBigContentTitle(Notification.safeCharSequence(title));
            return this;
        }

        public BigPictureStyle setSummaryText(CharSequence cs) {
            internalSetSummaryText(Notification.safeCharSequence(cs));
            return this;
        }

        public BigPictureStyle bigPicture(Bitmap b) {
            this.mPicture = b;
            return this;
        }

        public BigPictureStyle bigLargeIcon(Bitmap b) {
            return bigLargeIcon(b != null ? Icon.createWithBitmap(b) : null);
        }

        public BigPictureStyle bigLargeIcon(Icon icon) {
            this.mBigLargeIconSet = true;
            this.mBigLargeIcon = icon;
            return this;
        }

        public void purgeResources() {
            super.purgeResources();
            if (this.mPicture != null && this.mPicture.isMutable() && this.mPicture.getAllocationByteCount() >= 131072) {
                this.mPicture = this.mPicture.createAshmemBitmap();
            }
            if (this.mBigLargeIcon != null) {
                this.mBigLargeIcon.convertToAshmem();
            }
        }

        private RemoteViews makeBigContentView() {
            Icon oldLargeIcon = null;
            if (this.mBigLargeIconSet) {
                oldLargeIcon = this.mBuilder.mLargeIcon;
                this.mBuilder.mLargeIcon = this.mBigLargeIcon;
            }
            RemoteViews contentView = getStandardView(this.mBuilder.getBigPictureLayoutResource());
            if (this.mBigLargeIconSet) {
                this.mBuilder.mLargeIcon = oldLargeIcon;
            }
            contentView.setImageViewBitmap(16909357, this.mPicture);
            applyTopPadding(contentView);
            boolean twoTextLines = (this.mBuilder.mSubText == null || this.mBuilder.mContentText == null) ? false : true;
            this.mBuilder.addProfileBadge(contentView, twoTextLines ? 16909370 : 16909373);
            return contentView;
        }

        public void addExtras(Bundle extras) {
            super.addExtras(extras);
            if (this.mBigLargeIconSet) {
                extras.putParcelable(Notification.EXTRA_LARGE_ICON_BIG, this.mBigLargeIcon);
            }
            extras.putParcelable(Notification.EXTRA_PICTURE, this.mPicture);
        }

        protected void restoreFromExtras(Bundle extras) {
            super.restoreFromExtras(extras);
            if (extras.containsKey(Notification.EXTRA_LARGE_ICON_BIG)) {
                this.mBigLargeIconSet = true;
                this.mBigLargeIcon = (Icon) extras.getParcelable(Notification.EXTRA_LARGE_ICON_BIG);
            }
            this.mPicture = (Bitmap) extras.getParcelable(Notification.EXTRA_PICTURE);
        }

        public void populateBigContentView(Notification wip) {
            this.mBuilder.setBuilderBigContentView(wip, makeBigContentView());
        }
    }

    public static class BigTextStyle extends Style {
        private static final int LINES_CONSUMED_BY_ACTIONS = 3;
        private static final int LINES_CONSUMED_BY_SUMMARY = 2;
        private static final int MAX_LINES = 12;
        private CharSequence mBigText;

        public BigTextStyle(Builder builder) {
            setBuilder(builder);
        }

        public BigTextStyle setBigContentTitle(CharSequence title) {
            internalSetBigContentTitle(Notification.safeCharSequence(title));
            return this;
        }

        public BigTextStyle setSummaryText(CharSequence cs) {
            internalSetSummaryText(Notification.safeCharSequence(cs));
            return this;
        }

        public BigTextStyle bigText(CharSequence cs) {
            this.mBigText = Notification.safeCharSequence(cs);
            return this;
        }

        public void addExtras(Bundle extras) {
            super.addExtras(extras);
            extras.putCharSequence(Notification.EXTRA_BIG_TEXT, this.mBigText);
        }

        protected void restoreFromExtras(Bundle extras) {
            super.restoreFromExtras(extras);
            this.mBigText = extras.getCharSequence(Notification.EXTRA_BIG_TEXT);
        }

        private RemoteViews makeBigContentView() {
            CharSequence oldBuilderContentText = this.mBuilder.mContentText;
            this.mBuilder.mContentText = null;
            RemoteViews contentView = getStandardView(this.mBuilder.getBigTextLayoutResource());
            this.mBuilder.mContentText = oldBuilderContentText;
            contentView.setTextViewText(16909353, this.mBuilder.processLegacyText(this.mBigText));
            contentView.setViewVisibility(16909353, 0);
            contentView.setInt(16909353, "setMaxLines", calculateMaxLines());
            contentView.setViewVisibility(R.id.text2, 8);
            applyTopPadding(contentView);
            this.mBuilder.shrinkLine3Text(contentView);
            this.mBuilder.addProfileBadge(contentView, 16909354);
            return contentView;
        }

        private int calculateMaxLines() {
            boolean hasActions;
            int lineCount = 12;
            if (this.mBuilder.mActions.size() > 0) {
                hasActions = true;
            } else {
                hasActions = false;
            }
            boolean hasSummary = (this.mSummaryTextSet ? this.mSummaryText : this.mBuilder.mSubText) != null;
            if (hasActions) {
                lineCount = 12 - 3;
            }
            if (hasSummary) {
                lineCount -= 2;
            }
            if (this.mBuilder.mHasThreeLines) {
                return lineCount;
            }
            return lineCount - 1;
        }

        public void populateBigContentView(Notification wip) {
            this.mBuilder.setBuilderBigContentView(wip, makeBigContentView());
        }
    }

    public static class Builder {
        public static final String EXTRA_NEEDS_REBUILD = "android.rebuild";
        public static final String EXTRA_REBUILD_BIG_CONTENT_VIEW = "android.rebuild.bigView";
        public static final String EXTRA_REBUILD_BIG_CONTENT_VIEW_ACTION_COUNT = "android.rebuild.bigViewActionCount";
        public static final String EXTRA_REBUILD_CONTENT_VIEW = "android.rebuild.contentView";
        public static final String EXTRA_REBUILD_CONTENT_VIEW_ACTION_COUNT = "android.rebuild.contentViewActionCount";
        private static final String EXTRA_REBUILD_CONTEXT_APPLICATION_INFO = "android.rebuild.applicationInfo";
        public static final String EXTRA_REBUILD_HEADS_UP_CONTENT_VIEW = "android.rebuild.hudView";
        public static final String EXTRA_REBUILD_HEADS_UP_CONTENT_VIEW_ACTION_COUNT = "android.rebuild.hudViewActionCount";
        public static final String EXTRA_REBUILD_LARGE_ICON = "android.rebuild.largeIcon";
        private static final float LARGE_TEXT_SCALE = 1.3f;
        private static final int MAX_ACTION_BUTTONS = 3;
        private static final boolean STRIP_AND_REBUILD = true;
        private ArrayList<Action> mActions;
        private AudioAttributes mAudioAttributes;
        private int mAudioStreamType;
        private String mCategory;
        private int mColor;
        private final NotificationColorUtil mColorUtil;
        private CharSequence mContentInfo;
        private PendingIntent mContentIntent;
        private CharSequence mContentText;
        private CharSequence mContentTitle;
        private RemoteViews mContentView;
        private Context mContext;
        private int mDefaults;
        private PendingIntent mDeleteIntent;
        private Bundle mExtras;
        private int mFlags;
        private PendingIntent mFullScreenIntent;
        private String mGroupKey;
        private boolean mHasThreeLines;
        private Icon mLargeIcon;
        private int mLedArgb;
        private int mLedOffMs;
        private int mLedOnMs;
        private int mNumber;
        private int mOriginatingUserId;
        private ArrayList<String> mPeople;
        private int mPriority;
        private int mProgress;
        private boolean mProgressIndeterminate;
        private int mProgressMax;
        private Notification mPublicVersion;
        private Bundle mRebuildBundle;
        private Notification mRebuildNotification;
        private int mSecFlags;
        private boolean mShowWhen;
        private Icon mSmallIcon;
        private int mSmallIconLevel;
        private String mSortKey;
        private Uri mSound;
        private Map<CharSequence, CharSequence> mStringNamesMap;
        private Style mStyle;
        private CharSequence mSubText;
        private CharSequence mTickerText;
        private RemoteViews mTickerView;
        private boolean mUseChronometer;
        private long[] mVibrate;
        private int mVisibility;
        private long mWhen;

        public Builder(Context context) {
            Map hashMap;
            NotificationColorUtil notificationColorUtil = null;
            this.mActions = new ArrayList(3);
            this.mShowWhen = true;
            this.mVisibility = 0;
            this.mPublicVersion = null;
            this.mColor = 0;
            this.mRebuildBundle = new Bundle();
            this.mRebuildNotification = null;
            if (Build.IS_SYSTEM_SECURE) {
                hashMap = new HashMap();
            } else {
                hashMap = null;
            }
            this.mStringNamesMap = hashMap;
            this.mContext = context;
            this.mWhen = System.currentTimeMillis();
            this.mAudioStreamType = -1;
            this.mAudioAttributes = Notification.AUDIO_ATTRIBUTES_DEFAULT;
            this.mPriority = 0;
            this.mPeople = new ArrayList();
            if (context.getApplicationInfo().targetSdkVersion < 21) {
                notificationColorUtil = NotificationColorUtil.getInstance(this.mContext);
            }
            this.mColorUtil = notificationColorUtil;
        }

        private Builder(Context context, Notification n) {
            this(context);
            this.mRebuildNotification = n;
            restoreFromNotification(n);
            Style style = null;
            Bundle extras = n.extras;
            String templateClass = extras.getString(Notification.EXTRA_TEMPLATE);
            if (!TextUtils.isEmpty(templateClass)) {
                Class<? extends Style> styleClass = getNotificationStyleClass(templateClass);
                if (styleClass == null) {
                    Log.d(Notification.TAG, "Unknown style class: " + styleClass);
                    return;
                }
                try {
                    Constructor<? extends Style> constructor = styleClass.getConstructor(new Class[0]);
                    constructor.setAccessible(true);
                    style = (Style) constructor.newInstance(new Object[0]);
                    style.restoreFromExtras(extras);
                } catch (Throwable t) {
                    Log.e(Notification.TAG, "Could not create Style", t);
                    return;
                }
            }
            if (style != null) {
                setStyle(style);
            }
        }

        public Builder setWhen(long when) {
            this.mWhen = when;
            return this;
        }

        public Builder setShowWhen(boolean show) {
            this.mShowWhen = show;
            return this;
        }

        public Builder setUsesChronometer(boolean b) {
            this.mUseChronometer = b;
            return this;
        }

        public Builder setSmallIcon(int icon) {
            return setSmallIcon(icon != 0 ? Icon.createWithResource(this.mContext, icon) : null);
        }

        public Builder setSmallIcon(int icon, int level) {
            this.mSmallIconLevel = level;
            return setSmallIcon(icon);
        }

        public Builder setSmallIcon(Icon icon) {
            this.mSmallIcon = icon;
            return this;
        }

        public Builder setContentTitle(CharSequence title) {
            this.mContentTitle = Notification.safeCharSequence(title);
            if (Build.IS_SYSTEM_SECURE && this.mContext != null) {
                this.mStringNamesMap.put(this.mContentTitle, this.mContext.getResources().getStringNames(this.mContentTitle));
            }
            return this;
        }

        public Builder setContentText(CharSequence text) {
            this.mContentText = Notification.safeCharSequence(text);
            if (Build.IS_SYSTEM_SECURE && this.mContext != null) {
                this.mStringNamesMap.put(this.mContentText, this.mContext.getResources().getStringNames(this.mContentText));
            }
            return this;
        }

        public Builder setSubText(CharSequence text) {
            this.mSubText = Notification.safeCharSequence(text);
            if (Build.IS_SYSTEM_SECURE && this.mContext != null) {
                this.mStringNamesMap.put(text, this.mContext.getResources().getStringNames(text));
            }
            return this;
        }

        public Builder setNumber(int number) {
            this.mNumber = number;
            return this;
        }

        public Builder setContentInfo(CharSequence info) {
            this.mContentInfo = Notification.safeCharSequence(info);
            return this;
        }

        public Builder setProgress(int max, int progress, boolean indeterminate) {
            this.mProgressMax = max;
            this.mProgress = progress;
            this.mProgressIndeterminate = indeterminate;
            return this;
        }

        public Builder setContent(RemoteViews views) {
            this.mContentView = views;
            return this;
        }

        public Builder setContentIntent(PendingIntent intent) {
            this.mContentIntent = intent;
            return this;
        }

        public Builder setDeleteIntent(PendingIntent intent) {
            this.mDeleteIntent = intent;
            return this;
        }

        public Builder setFullScreenIntent(PendingIntent intent, boolean highPriority) {
            this.mFullScreenIntent = intent;
            setFlag(128, highPriority);
            return this;
        }

        public Builder setTicker(CharSequence tickerText) {
            this.mTickerText = Notification.safeCharSequence(tickerText);
            if (Build.IS_SYSTEM_SECURE && this.mContext != null) {
                this.mStringNamesMap.put(this.mTickerText, this.mContext.getResources().getStringNames(this.mTickerText));
            }
            return this;
        }

        @Deprecated
        public Builder setTicker(CharSequence tickerText, RemoteViews views) {
            this.mTickerText = Notification.safeCharSequence(tickerText);
            this.mTickerView = views;
            if (Build.IS_SYSTEM_SECURE && this.mContext != null) {
                this.mStringNamesMap.put(this.mTickerText, this.mContext.getResources().getStringNames(this.mTickerText));
            }
            return this;
        }

        public Builder setLargeIcon(Bitmap b) {
            return setLargeIcon(b != null ? Icon.createWithBitmap(b) : null);
        }

        public Builder setLargeIcon(Icon icon) {
            this.mLargeIcon = icon;
            return this;
        }

        public Builder setSound(Uri sound) {
            this.mSound = sound;
            this.mAudioAttributes = Notification.AUDIO_ATTRIBUTES_DEFAULT;
            return this;
        }

        @Deprecated
        public Builder setSound(Uri sound, int streamType) {
            this.mSound = sound;
            this.mAudioStreamType = streamType;
            return this;
        }

        public Builder setSound(Uri sound, AudioAttributes audioAttributes) {
            this.mSound = sound;
            this.mAudioAttributes = audioAttributes;
            return this;
        }

        public Builder setVibrate(long[] pattern) {
            this.mVibrate = pattern;
            return this;
        }

        public Builder setLights(int argb, int onMs, int offMs) {
            this.mLedArgb = argb;
            this.mLedOnMs = onMs;
            this.mLedOffMs = offMs;
            return this;
        }

        public Builder setOngoing(boolean ongoing) {
            setFlag(2, ongoing);
            return this;
        }

        public Builder setOnlyAlertOnce(boolean onlyAlertOnce) {
            setFlag(8, onlyAlertOnce);
            return this;
        }

        public Builder setAutoCancel(boolean autoCancel) {
            setFlag(16, autoCancel);
            return this;
        }

        public Builder setLocalOnly(boolean localOnly) {
            setFlag(256, localOnly);
            return this;
        }

        public Builder setDefaults(int defaults) {
            this.mDefaults = defaults;
            return this;
        }

        public Builder setPriority(int pri) {
            this.mPriority = pri;
            return this;
        }

        public Builder setCategory(String category) {
            this.mCategory = category;
            return this;
        }

        public Builder addPerson(String uri) {
            this.mPeople.add(uri);
            return this;
        }

        public Builder setGroup(String groupKey) {
            this.mGroupKey = groupKey;
            return this;
        }

        public Builder setGroupSummary(boolean isGroupSummary) {
            setFlag(512, isGroupSummary);
            return this;
        }

        public Builder setSortKey(String sortKey) {
            this.mSortKey = sortKey;
            return this;
        }

        public Builder addExtras(Bundle extras) {
            if (extras != null) {
                if (this.mExtras == null) {
                    this.mExtras = new Bundle(extras);
                } else {
                    this.mExtras.putAll(extras);
                }
            }
            return this;
        }

        public Builder setExtras(Bundle extras) {
            this.mExtras = extras;
            return this;
        }

        public Bundle getExtras() {
            if (this.mExtras == null) {
                this.mExtras = new Bundle();
            }
            return this.mExtras;
        }

        @Deprecated
        public Builder addAction(int icon, CharSequence title, PendingIntent intent) {
            this.mActions.add(new Action(icon, Notification.safeCharSequence(title), intent));
            return this;
        }

        public Builder addAction(Action action) {
            this.mActions.add(action);
            return this;
        }

        public Builder setStyle(Style style) {
            if (this.mStyle != style) {
                this.mStyle = style;
                if (this.mStyle != null) {
                    this.mStyle.setBuilder(this);
                }
            }
            return this;
        }

        public Builder setVisibility(int visibility) {
            this.mVisibility = visibility;
            return this;
        }

        public Builder setPublicVersion(Notification n) {
            this.mPublicVersion = n;
            return this;
        }

        public Builder extend(Extender extender) {
            extender.extend(this);
            return this;
        }

        public void setFlag(int mask, boolean value) {
            if (value) {
                this.mFlags |= mask;
            } else {
                this.mFlags &= mask ^ -1;
            }
        }

        public Builder setColor(int argb) {
            this.mColor = argb;
            return this;
        }

        private Drawable getProfileBadgeDrawable() {
            return this.mContext.getPackageManager().getUserBadgeForDensity(new UserHandle(this.mOriginatingUserId), 0);
        }

        private Bitmap getProfileBadge() {
            Drawable badge = getProfileBadgeDrawable();
            if (badge == null) {
                return null;
            }
            int size = this.mContext.getResources().getDimensionPixelSize(17104994);
            Bitmap bitmap = Bitmap.createBitmap(size, size, Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            badge.setBounds(0, 0, size, size);
            badge.draw(canvas);
            return bitmap;
        }

        private boolean addProfileBadge(RemoteViews contentView, int resId) {
            Bitmap profileBadge = getProfileBadge();
            contentView.setViewVisibility(16909354, 8);
            contentView.setViewVisibility(16909370, 8);
            contentView.setViewVisibility(16909373, 8);
            if (profileBadge == null) {
                return false;
            }
            contentView.setImageViewBitmap(resId, profileBadge);
            contentView.setViewVisibility(resId, 0);
            if (resId == 16909373) {
                contentView.setViewVisibility(16909371, 0);
            }
            return true;
        }

        private void shrinkLine3Text(RemoteViews contentView) {
            contentView.setTextViewTextSize(16908419, 0, (float) this.mContext.getResources().getDimensionPixelSize(17104988));
        }

        private void unshrinkLine3Text(RemoteViews contentView) {
            contentView.setTextViewTextSize(16908419, 0, (float) this.mContext.getResources().getDimensionPixelSize(17104986));
        }

        private void resetStandardTemplate(RemoteViews contentView) {
            removeLargeIconBackground(contentView);
            contentView.setViewPadding(R.id.icon, 0, 0, 0, 0);
            contentView.setImageViewResource(R.id.icon, 0);
            contentView.setInt(R.id.icon, "setBackgroundResource", 0);
            contentView.setViewVisibility(16908352, 8);
            contentView.setInt(16908352, "setBackgroundResource", 0);
            contentView.setImageViewResource(16908352, 0);
            contentView.setImageViewResource(R.id.icon, 0);
            contentView.setTextViewText(R.id.title, null);
            contentView.setTextViewText(16908419, null);
            unshrinkLine3Text(contentView);
            contentView.setTextViewText(R.id.text2, null);
            contentView.setViewVisibility(R.id.text2, 8);
            contentView.setViewVisibility(16909372, 8);
            contentView.setViewVisibility(16908442, 8);
            contentView.setViewVisibility(16909371, 8);
            contentView.setViewVisibility(16909358, 8);
            contentView.setViewVisibility(R.id.progress, 8);
            contentView.setViewVisibility(16909368, 8);
            contentView.setViewVisibility(16908442, 8);
        }

        private RemoteViews applyStandardTemplate(int resId) {
            return applyStandardTemplate(resId, true);
        }

        private RemoteViews applyStandardTemplate(int resId, boolean hasProgress) {
            RemoteViews contentView = new BuilderRemoteViews(this.mContext.getApplicationInfo(), resId);
            resetStandardTemplate(contentView);
            boolean showLine3 = false;
            boolean showLine2 = false;
            boolean contentTextInLine2 = false;
            if (Build.IS_SYSTEM_SECURE) {
                Resources res = this.mContext.getResources();
                this.mStringNamesMap.put(this.mContentTitle, res.getStringNames(this.mContentTitle));
                this.mStringNamesMap.put(this.mContentText, res.getStringNames(this.mContentText));
                this.mStringNamesMap.put(this.mTickerText, res.getStringNames(this.mTickerText));
                contentView.setStringMap(this.mStringNamesMap);
            }
            if (this.mLargeIcon == null || this.mSmallIcon == null) {
                contentView.setImageViewIcon(R.id.icon, this.mSmallIcon);
                contentView.setViewVisibility(R.id.icon, 0);
                processSmallIconAsLarge(this.mSmallIcon, contentView);
            } else {
                contentView.setImageViewIcon(R.id.icon, this.mLargeIcon);
                processLargeLegacyIcon(this.mLargeIcon, contentView);
                contentView.setImageViewIcon(16908352, this.mSmallIcon);
                contentView.setViewVisibility(16908352, 0);
                processSmallRightIcon(this.mSmallIcon, contentView);
            }
            if (this.mContentTitle != null) {
                contentView.setTextViewText(R.id.title, processLegacyText(this.mContentTitle));
            }
            if (this.mContentText != null) {
                contentView.setTextViewText(16908419, processLegacyText(this.mContentText));
                showLine3 = true;
            }
            if (this.mContentInfo != null) {
                contentView.setTextViewText(16909372, processLegacyText(this.mContentInfo));
                contentView.setViewVisibility(16909372, 0);
                showLine3 = true;
            } else if (this.mNumber > 0) {
                if (this.mNumber > this.mContext.getResources().getInteger(R.integer.status_bar_notification_info_maxnum)) {
                    contentView.setTextViewText(16909372, processLegacyText(this.mContext.getResources().getString(R.string.status_bar_notification_info_overflow)));
                } else {
                    contentView.setTextViewText(16909372, processLegacyText(NumberFormat.getIntegerInstance().format((long) this.mNumber)));
                }
                contentView.setViewVisibility(16909372, 0);
                showLine3 = true;
            } else {
                contentView.setViewVisibility(16909372, 8);
            }
            if (this.mSubText != null) {
                contentView.setTextViewText(16908419, processLegacyText(this.mSubText));
                if (this.mContentText != null) {
                    contentView.setTextViewText(R.id.text2, processLegacyText(this.mContentText));
                    contentView.setViewVisibility(R.id.text2, 0);
                    showLine2 = true;
                    contentTextInLine2 = true;
                } else {
                    contentView.setViewVisibility(R.id.text2, 8);
                }
            } else {
                contentView.setViewVisibility(R.id.text2, 8);
                if (!hasProgress || (this.mProgressMax == 0 && !this.mProgressIndeterminate)) {
                    contentView.setViewVisibility(R.id.progress, 8);
                } else {
                    contentView.setViewVisibility(R.id.progress, 0);
                    contentView.setProgressBar(R.id.progress, this.mProgressMax, this.mProgress, this.mProgressIndeterminate);
                    contentView.setProgressBackgroundTintList(R.id.progress, ColorStateList.valueOf(this.mContext.getColor(17170515)));
                    if (this.mColor != 0) {
                        ColorStateList colorStateList = ColorStateList.valueOf(this.mColor);
                        contentView.setProgressTintList(R.id.progress, colorStateList);
                        contentView.setProgressIndeterminateTintList(R.id.progress, colorStateList);
                    }
                    showLine2 = true;
                }
            }
            if (showLine2) {
                shrinkLine3Text(contentView);
            }
            if (showsTimeOrChronometer()) {
                if (this.mUseChronometer) {
                    contentView.setViewVisibility(16909368, 0);
                    contentView.setLong(16909368, "setBase", this.mWhen + (SystemClock.elapsedRealtime() - System.currentTimeMillis()));
                    contentView.setBoolean(16909368, "setStarted", true);
                } else {
                    contentView.setViewVisibility(16908442, 0);
                    contentView.setLong(16908442, "setTime", this.mWhen);
                }
            }
            contentView.setViewPadding(16909369, 0, calculateTopPadding(this.mContext, this.mHasThreeLines, this.mContext.getResources().getConfiguration().fontScale), 0, 0);
            if (addProfileBadge(contentView, contentTextInLine2 ? 16909370 : 16909373) && !contentTextInLine2) {
                showLine3 = true;
            }
            contentView.setViewVisibility(16909371, showLine3 ? 0 : 8);
            contentView.setViewVisibility(16909358, showLine3 ? 0 : 8);
            return contentView;
        }

        private boolean showsTimeOrChronometer() {
            return this.mWhen != 0 && this.mShowWhen;
        }

        private boolean hasThreeLines() {
            boolean hasLine2;
            boolean contentTextInLine2;
            if (this.mSubText == null || this.mContentText == null) {
                contentTextInLine2 = false;
            } else {
                contentTextInLine2 = true;
            }
            boolean hasProgress;
            if (this.mStyle == null || this.mStyle.hasProgress()) {
                hasProgress = true;
            } else {
                hasProgress = false;
            }
            boolean badgeInLine3;
            if (getProfileBadgeDrawable() == null || contentTextInLine2) {
                badgeInLine3 = false;
            } else {
                badgeInLine3 = true;
            }
            boolean hasLine3;
            if (this.mContentText != null || this.mContentInfo != null || this.mNumber > 0 || badgeInLine3) {
                hasLine3 = true;
            } else {
                hasLine3 = false;
            }
            if ((this.mSubText == null || this.mContentText == null) && !(hasProgress && this.mSubText == null && (this.mProgressMax != 0 || this.mProgressIndeterminate))) {
                hasLine2 = false;
            } else {
                hasLine2 = true;
            }
            if (hasLine2 && hasLine3) {
                return true;
            }
            return false;
        }

        public static int calculateTopPadding(Context ctx, boolean hasThreeLines, float fontScale) {
            return 0;
        }

        private void resetStandardTemplateWithActions(RemoteViews big) {
            big.setViewVisibility(16909334, 8);
            big.setViewVisibility(16909355, 8);
            big.removeAllViews(16909334);
        }

        private RemoteViews applyStandardTemplateWithActions(int layoutId) {
            RemoteViews big = applyStandardTemplate(layoutId);
            resetStandardTemplateWithActions(big);
            int N = this.mActions.size();
            big.setViewPadding(16908378, this.mContext.getResources().getDimensionPixelSize(R.dimen.notification_large_icon_width), 0, 0, 0);
            if (N > 0) {
                big.setViewVisibility(16909334, 0);
                big.setViewVisibility(16909355, 8);
                if (N > 3) {
                    N = 3;
                }
                for (int i = 0; i < N; i++) {
                    big.addView(16909334, generateActionButton((Action) this.mActions.get(i)));
                }
            }
            return big;
        }

        private RemoteViews makeContentView() {
            if (this.mContentView != null) {
                return this.mContentView;
            }
            return applyStandardTemplate(getBaseLayoutResource());
        }

        private RemoteViews makeTickerView() {
            if (this.mTickerView != null) {
                return this.mTickerView;
            }
            return null;
        }

        private RemoteViews makeBigContentView() {
            if (this.mActions.size() == 0) {
                return null;
            }
            return applyStandardTemplateWithActions(getBigBaseLayoutResource());
        }

        private RemoteViews makeHeadsUpContentView() {
            if (this.mActions.size() == 0) {
                return null;
            }
            return applyStandardTemplateWithActions(getBigBaseLayoutResource());
        }

        private RemoteViews generateActionButton(Action action) {
            boolean tombstone = action.actionIntent == null;
            RemoteViews button = new BuilderRemoteViews(this.mContext.getApplicationInfo(), tombstone ? getActionTombstoneLayoutResource() : getActionLayoutResource());
            if ((this.mSecFlags & 4) == 0) {
                button.setTextViewCompoundDrawablesRelative(16909347, action.getIcon(), null, null, null);
            }
            button.setTextViewText(16909347, processLegacyText(action.title));
            if (!tombstone) {
                button.setOnClickPendingIntent(16909347, action.actionIntent);
            }
            button.setContentDescription(16909347, action.title);
            processLegacyAction(action, button);
            return button;
        }

        private boolean isLegacy() {
            return this.mColorUtil != null;
        }

        private void processLegacyAction(Action action, RemoteViews button) {
            if (!isLegacy() || this.mColorUtil.isGrayscaleIcon(this.mContext, action.getIcon())) {
                button.setTextViewCompoundDrawablesRelativeColorFilter(16909347, 0, this.mContext.getColor(17170795), Mode.MULTIPLY);
            }
        }

        private CharSequence processLegacyText(CharSequence charSequence) {
            if (isLegacy()) {
                return this.mColorUtil.invertCharSequenceColors(charSequence);
            }
            return charSequence;
        }

        private void processSmallIconAsLarge(Icon largeIcon, RemoteViews contentView) {
            if (!isLegacy() || this.mColorUtil.isGrayscaleIcon(this.mContext, largeIcon)) {
                applyLargeIconBackground(contentView);
            }
        }

        private void processLargeLegacyIcon(Icon largeIcon, RemoteViews contentView) {
            if (largeIcon != null && isLegacy() && this.mColorUtil.isGrayscaleIcon(this.mContext, largeIcon)) {
                applyLargeIconBackground(contentView);
            } else {
                removeLargeIconBackground(contentView);
            }
        }

        private void applyLargeIconBackground(RemoteViews contentView) {
            contentView.setInt(R.id.icon, "setBackgroundResource", 17303008);
            contentView.setDrawableParameters(R.id.icon, true, -1, resolveColor(), Mode.SRC_ATOP, -1);
            int padding = this.mContext.getResources().getDimensionPixelSize(17104993);
            contentView.setViewPadding(R.id.icon, padding, padding, padding, padding);
        }

        private void removeLargeIconBackground(RemoteViews contentView) {
            contentView.setInt(R.id.icon, "setBackgroundResource", 0);
        }

        private void processSmallRightIcon(Icon smallIcon, RemoteViews contentView) {
            if (!isLegacy()) {
                contentView.setDrawableParameters(16908352, false, -1, -1, Mode.SRC_ATOP, -1);
            }
            boolean gray;
            if (isLegacy() && smallIcon.getType() == 2 && this.mColorUtil.isGrayscaleIcon(this.mContext, smallIcon.getResId())) {
                gray = true;
            } else {
                gray = false;
            }
            if (!isLegacy() || gray) {
                contentView.setInt(16908352, "setBackgroundResource", 17303008);
                contentView.setDrawableParameters(16908352, true, -1, resolveColor(), Mode.SRC_ATOP, -1);
            }
        }

        private int sanitizeColor() {
            if (this.mColor != 0) {
                this.mColor |= -16777216;
            }
            return this.mColor;
        }

        private int resolveColor() {
            if (this.mColor == 0) {
                return this.mContext.getColor(17170511);
            }
            return this.mColor;
        }

        public Notification buildUnstyled() {
            Notification n = new Notification();
            n.when = this.mWhen;
            n.mSmallIcon = this.mSmallIcon;
            if (this.mSmallIcon != null && this.mSmallIcon.getType() == 2) {
                n.icon = this.mSmallIcon.getResId();
            }
            n.iconLevel = this.mSmallIconLevel;
            n.number = this.mNumber;
            n.color = sanitizeColor();
            setBuilderContentView(n, makeContentView());
            n.contentIntent = this.mContentIntent;
            n.deleteIntent = this.mDeleteIntent;
            n.fullScreenIntent = this.mFullScreenIntent;
            n.tickerText = this.mTickerText;
            n.tickerView = makeTickerView();
            n.mLargeIcon = this.mLargeIcon;
            if (this.mLargeIcon != null && this.mLargeIcon.getType() == 1) {
                n.largeIcon = this.mLargeIcon.getBitmap();
            }
            n.sound = this.mSound;
            n.audioStreamType = this.mAudioStreamType;
            n.audioAttributes = this.mAudioAttributes;
            n.vibrate = this.mVibrate;
            n.ledARGB = this.mLedArgb;
            n.ledOnMS = this.mLedOnMs;
            n.ledOffMS = this.mLedOffMs;
            n.defaults = this.mDefaults;
            n.flags = this.mFlags;
            setBuilderBigContentView(n, makeBigContentView());
            setBuilderHeadsUpContentView(n, makeHeadsUpContentView());
            if (!(this.mLedOnMs == 0 && this.mLedOffMs == 0)) {
                n.flags |= 1;
            }
            if ((this.mDefaults & 4) != 0) {
                n.flags |= 1;
            }
            n.category = this.mCategory;
            n.mGroupKey = this.mGroupKey;
            n.mSortKey = this.mSortKey;
            n.priority = this.mPriority;
            if (this.mActions.size() > 0) {
                n.actions = new Action[this.mActions.size()];
                this.mActions.toArray(n.actions);
            }
            n.visibility = this.mVisibility;
            if (this.mPublicVersion != null) {
                n.publicVersion = new Notification();
                this.mPublicVersion.cloneInto(n.publicVersion, true);
            }
            if (Build.IS_SYSTEM_SECURE) {
                n.stringNamesMap = this.mStringNamesMap;
                n.contentView.setStringMap(this.mStringNamesMap);
                if (n.bigContentView != null) {
                    n.bigContentView.setStringMap(this.mStringNamesMap);
                }
            }
            return n;
        }

        public void populateExtras(Bundle extras) {
            extras.putInt(Notification.EXTRA_ORIGINATING_USERID, this.mOriginatingUserId);
            extras.putParcelable(EXTRA_REBUILD_CONTEXT_APPLICATION_INFO, this.mContext.getApplicationInfo());
            extras.putCharSequence(Notification.EXTRA_TITLE, this.mContentTitle);
            extras.putCharSequence(Notification.EXTRA_TEXT, this.mContentText);
            extras.putCharSequence(Notification.EXTRA_SUB_TEXT, this.mSubText);
            extras.putCharSequence(Notification.EXTRA_INFO_TEXT, this.mContentInfo);
            extras.putParcelable(Notification.EXTRA_SMALL_ICON, this.mSmallIcon);
            extras.putInt(Notification.EXTRA_PROGRESS, this.mProgress);
            extras.putInt(Notification.EXTRA_PROGRESS_MAX, this.mProgressMax);
            extras.putBoolean(Notification.EXTRA_PROGRESS_INDETERMINATE, this.mProgressIndeterminate);
            extras.putBoolean(Notification.EXTRA_SHOW_CHRONOMETER, this.mUseChronometer);
            extras.putBoolean(Notification.EXTRA_SHOW_WHEN, this.mShowWhen);
            if (this.mLargeIcon != null) {
                extras.putParcelable(Notification.EXTRA_LARGE_ICON, this.mLargeIcon);
            }
            if (!this.mPeople.isEmpty()) {
                extras.putStringArray(Notification.EXTRA_PEOPLE, (String[]) this.mPeople.toArray(new String[this.mPeople.size()]));
            }
        }

        public static void stripForDelivery(Notification n) {
            String templateClass = n.extras.getString(Notification.EXTRA_TEMPLATE);
            boolean stripViews = TextUtils.isEmpty(templateClass) || getNotificationStyleClass(templateClass) != null;
            boolean isStripped = false;
            if (n.largeIcon != null && n.extras.containsKey(Notification.EXTRA_LARGE_ICON)) {
                n.largeIcon = null;
                n.extras.putBoolean(EXTRA_REBUILD_LARGE_ICON, true);
                isStripped = true;
            }
            if (stripViews && (n.contentView instanceof BuilderRemoteViews) && n.extras.getInt(EXTRA_REBUILD_CONTENT_VIEW_ACTION_COUNT, -1) == n.contentView.getSequenceNumber()) {
                n.contentView = null;
                n.extras.putBoolean(EXTRA_REBUILD_CONTENT_VIEW, true);
                n.extras.remove(EXTRA_REBUILD_CONTENT_VIEW_ACTION_COUNT);
                isStripped = true;
            }
            if (stripViews && (n.bigContentView instanceof BuilderRemoteViews) && n.extras.getInt(EXTRA_REBUILD_BIG_CONTENT_VIEW_ACTION_COUNT, -1) == n.bigContentView.getSequenceNumber()) {
                n.bigContentView = null;
                n.extras.putBoolean(EXTRA_REBUILD_BIG_CONTENT_VIEW, true);
                n.extras.remove(EXTRA_REBUILD_BIG_CONTENT_VIEW_ACTION_COUNT);
                isStripped = true;
            }
            if (stripViews && (n.headsUpContentView instanceof BuilderRemoteViews) && n.extras.getInt(EXTRA_REBUILD_HEADS_UP_CONTENT_VIEW_ACTION_COUNT, -1) == n.headsUpContentView.getSequenceNumber()) {
                n.headsUpContentView = null;
                n.extras.putBoolean(EXTRA_REBUILD_HEADS_UP_CONTENT_VIEW, true);
                n.extras.remove(EXTRA_REBUILD_HEADS_UP_CONTENT_VIEW_ACTION_COUNT);
                isStripped = true;
            }
            if (isStripped) {
                n.extras.putBoolean(EXTRA_NEEDS_REBUILD, true);
            }
        }

        public static Notification rebuild(Context context, Notification n) {
            Bundle extras = n.extras;
            if (!extras.getBoolean(EXTRA_NEEDS_REBUILD)) {
                return n;
            }
            Context builderContext;
            extras.remove(EXTRA_NEEDS_REBUILD);
            ApplicationInfo applicationInfo = (ApplicationInfo) extras.getParcelable(EXTRA_REBUILD_CONTEXT_APPLICATION_INFO);
            try {
                builderContext = context.createApplicationContext(applicationInfo, 4);
            } catch (NameNotFoundException e) {
                Log.e(Notification.TAG, "ApplicationInfo " + applicationInfo + " not found");
                builderContext = context;
            }
            return new Builder(builderContext, n).rebuild();
        }

        private Notification rebuild() {
            if (this.mRebuildNotification == null) {
                throw new IllegalStateException("rebuild() only valid when in 'rebuild' mode.");
            }
            this.mHasThreeLines = hasThreeLines();
            Bundle extras = this.mRebuildNotification.extras;
            if (extras.getBoolean(EXTRA_REBUILD_LARGE_ICON)) {
                this.mRebuildNotification.largeIcon = (Bitmap) extras.getParcelable(Notification.EXTRA_LARGE_ICON);
            }
            extras.remove(EXTRA_REBUILD_LARGE_ICON);
            if (extras.getBoolean(EXTRA_REBUILD_CONTENT_VIEW)) {
                setBuilderContentView(this.mRebuildNotification, makeContentView());
                if (this.mStyle != null) {
                    this.mStyle.populateContentView(this.mRebuildNotification);
                }
            }
            extras.remove(EXTRA_REBUILD_CONTENT_VIEW);
            if (extras.getBoolean(EXTRA_REBUILD_BIG_CONTENT_VIEW)) {
                setBuilderBigContentView(this.mRebuildNotification, makeBigContentView());
                if (this.mStyle != null) {
                    this.mStyle.populateBigContentView(this.mRebuildNotification);
                }
            }
            extras.remove(EXTRA_REBUILD_BIG_CONTENT_VIEW);
            if (extras.getBoolean(EXTRA_REBUILD_HEADS_UP_CONTENT_VIEW)) {
                setBuilderHeadsUpContentView(this.mRebuildNotification, makeHeadsUpContentView());
                if (this.mStyle != null) {
                    this.mStyle.populateHeadsUpContentView(this.mRebuildNotification);
                }
            }
            extras.remove(EXTRA_REBUILD_HEADS_UP_CONTENT_VIEW);
            this.mHasThreeLines = false;
            return this.mRebuildNotification;
        }

        private static Class<? extends Style> getNotificationStyleClass(String templateClass) {
            for (Class<? extends Style> innerClass : new Class[]{BigTextStyle.class, BigPictureStyle.class, InboxStyle.class, MediaStyle.class}) {
                if (templateClass.equals(innerClass.getName())) {
                    return innerClass;
                }
            }
            return null;
        }

        private void setBuilderContentView(Notification n, RemoteViews contentView) {
            n.contentView = contentView;
            if (contentView instanceof BuilderRemoteViews) {
                this.mRebuildBundle.putInt(EXTRA_REBUILD_CONTENT_VIEW_ACTION_COUNT, contentView.getSequenceNumber());
            }
        }

        private void setBuilderBigContentView(Notification n, RemoteViews bigContentView) {
            n.bigContentView = bigContentView;
            if (bigContentView instanceof BuilderRemoteViews) {
                this.mRebuildBundle.putInt(EXTRA_REBUILD_BIG_CONTENT_VIEW_ACTION_COUNT, bigContentView.getSequenceNumber());
            }
        }

        private void setBuilderHeadsUpContentView(Notification n, RemoteViews headsUpContentView) {
            n.headsUpContentView = headsUpContentView;
            if (headsUpContentView instanceof BuilderRemoteViews) {
                this.mRebuildBundle.putInt(EXTRA_REBUILD_HEADS_UP_CONTENT_VIEW_ACTION_COUNT, headsUpContentView.getSequenceNumber());
            }
        }

        private void restoreFromNotification(Notification n) {
            this.mWhen = n.when;
            this.mSmallIcon = n.mSmallIcon;
            this.mSmallIconLevel = n.iconLevel;
            this.mNumber = n.number;
            this.mColor = n.color;
            this.mContentView = n.contentView;
            this.mDeleteIntent = n.deleteIntent;
            this.mFullScreenIntent = n.fullScreenIntent;
            this.mTickerText = n.tickerText;
            this.mTickerView = n.tickerView;
            this.mLargeIcon = n.mLargeIcon;
            this.mSound = n.sound;
            this.mAudioStreamType = n.audioStreamType;
            this.mAudioAttributes = n.audioAttributes;
            this.mVibrate = n.vibrate;
            this.mLedArgb = n.ledARGB;
            this.mLedOnMs = n.ledOnMS;
            this.mLedOffMs = n.ledOffMS;
            this.mDefaults = n.defaults;
            this.mFlags = n.flags;
            this.mCategory = n.category;
            this.mGroupKey = n.mGroupKey;
            this.mSortKey = n.mSortKey;
            this.mPriority = n.priority;
            this.mActions.clear();
            if (n.actions != null) {
                Collections.addAll(this.mActions, n.actions);
            }
            this.mVisibility = n.visibility;
            this.mPublicVersion = n.publicVersion;
            Bundle extras = n.extras;
            this.mOriginatingUserId = extras.getInt(Notification.EXTRA_ORIGINATING_USERID);
            this.mContentTitle = extras.getCharSequence(Notification.EXTRA_TITLE);
            this.mContentText = extras.getCharSequence(Notification.EXTRA_TEXT);
            this.mSubText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
            this.mContentInfo = extras.getCharSequence(Notification.EXTRA_INFO_TEXT);
            this.mSmallIcon = (Icon) extras.getParcelable(Notification.EXTRA_SMALL_ICON);
            this.mProgress = extras.getInt(Notification.EXTRA_PROGRESS);
            this.mProgressMax = extras.getInt(Notification.EXTRA_PROGRESS_MAX);
            this.mProgressIndeterminate = extras.getBoolean(Notification.EXTRA_PROGRESS_INDETERMINATE);
            this.mUseChronometer = extras.getBoolean(Notification.EXTRA_SHOW_CHRONOMETER);
            this.mShowWhen = extras.getBoolean(Notification.EXTRA_SHOW_WHEN);
            if (extras.containsKey(Notification.EXTRA_LARGE_ICON)) {
                this.mLargeIcon = (Icon) extras.getParcelable(Notification.EXTRA_LARGE_ICON);
            }
            if (extras.containsKey(Notification.EXTRA_PEOPLE)) {
                this.mPeople.clear();
                Collections.addAll(this.mPeople, extras.getStringArray(Notification.EXTRA_PEOPLE));
            }
            if (n.secFlags != 0) {
                this.mSecFlags = n.secFlags;
            }
            if (n.commonValue == Notification.MAGIC_NUMBER_HIDE_ACTION_BUTTON_ICON) {
                this.mSecFlags |= 4;
            }
        }

        @Deprecated
        public Notification getNotification() {
            return build();
        }

        public Notification build() {
            if (this.mSmallIcon != null) {
                this.mSmallIcon.convertToAshmem();
            }
            if (this.mLargeIcon != null) {
                this.mLargeIcon.convertToAshmem();
            }
            this.mOriginatingUserId = this.mContext.getUserId();
            this.mHasThreeLines = hasThreeLines();
            Notification n = buildUnstyled();
            if (this.mStyle != null) {
                this.mStyle.purgeResources();
                n = this.mStyle.buildStyled(n);
            }
            if (this.mExtras != null) {
                n.extras.putAll(this.mExtras);
            }
            if (this.mRebuildBundle.size() > 0) {
                n.extras.putAll(this.mRebuildBundle);
                this.mRebuildBundle.clear();
            }
            if (n.extras != null) {
                populateExtras(n.extras);
            }
            if (this.mStyle != null) {
                this.mStyle.addExtras(n.extras);
            }
            this.mHasThreeLines = false;
            return n;
        }

        public Notification buildInto(Notification n) {
            build().cloneInto(n, true);
            return n;
        }

        private int getBaseLayoutResource() {
            return 17367203;
        }

        private int getBigBaseLayoutResource() {
            return 17367204;
        }

        private int getBigPictureLayoutResource() {
            return 17367207;
        }

        private int getBigTextLayoutResource() {
            return 17367208;
        }

        private int getInboxLayoutResource() {
            return 17367209;
        }

        private int getActionLayoutResource() {
            return 17367198;
        }

        private int getActionTombstoneLayoutResource() {
            return 17367200;
        }
    }

    private static class BuilderRemoteViews extends RemoteViews {
        public BuilderRemoteViews(Parcel parcel) {
            super(parcel);
        }

        public BuilderRemoteViews(ApplicationInfo appInfo, int layoutId) {
            super(appInfo, layoutId);
        }

        public BuilderRemoteViews clone() {
            Parcel p = Parcel.obtain();
            writeToParcel(p, 0);
            p.setDataPosition(0);
            BuilderRemoteViews brv = new BuilderRemoteViews(p);
            p.recycle();
            return brv;
        }
    }

    public interface Extender {
        Builder extend(Builder builder);
    }

    public static final class CarExtender implements Extender {
        private static final String EXTRA_CAR_EXTENDER = "android.car.EXTENSIONS";
        private static final String EXTRA_COLOR = "app_color";
        private static final String EXTRA_CONVERSATION = "car_conversation";
        private static final String EXTRA_LARGE_ICON = "large_icon";
        private static final String TAG = "CarExtender";
        private int mColor = 0;
        private Bitmap mLargeIcon;
        private UnreadConversation mUnreadConversation;

        public static class Builder {
            private long mLatestTimestamp;
            private final List<String> mMessages = new ArrayList();
            private final String mParticipant;
            private PendingIntent mReadPendingIntent;
            private RemoteInput mRemoteInput;
            private PendingIntent mReplyPendingIntent;

            public Builder(String name) {
                this.mParticipant = name;
            }

            public Builder addMessage(String message) {
                this.mMessages.add(message);
                return this;
            }

            public Builder setReplyAction(PendingIntent pendingIntent, RemoteInput remoteInput) {
                this.mRemoteInput = remoteInput;
                this.mReplyPendingIntent = pendingIntent;
                return this;
            }

            public Builder setReadPendingIntent(PendingIntent pendingIntent) {
                this.mReadPendingIntent = pendingIntent;
                return this;
            }

            public Builder setLatestTimestamp(long timestamp) {
                this.mLatestTimestamp = timestamp;
                return this;
            }

            public UnreadConversation build() {
                return new UnreadConversation((String[]) this.mMessages.toArray(new String[this.mMessages.size()]), this.mRemoteInput, this.mReplyPendingIntent, this.mReadPendingIntent, new String[]{this.mParticipant}, this.mLatestTimestamp);
            }
        }

        public static class UnreadConversation {
            private static final String KEY_AUTHOR = "author";
            private static final String KEY_MESSAGES = "messages";
            private static final String KEY_ON_READ = "on_read";
            private static final String KEY_ON_REPLY = "on_reply";
            private static final String KEY_PARTICIPANTS = "participants";
            private static final String KEY_REMOTE_INPUT = "remote_input";
            private static final String KEY_TEXT = "text";
            private static final String KEY_TIMESTAMP = "timestamp";
            private final long mLatestTimestamp;
            private final String[] mMessages;
            private final String[] mParticipants;
            private final PendingIntent mReadPendingIntent;
            private final RemoteInput mRemoteInput;
            private final PendingIntent mReplyPendingIntent;

            UnreadConversation(String[] messages, RemoteInput remoteInput, PendingIntent replyPendingIntent, PendingIntent readPendingIntent, String[] participants, long latestTimestamp) {
                this.mMessages = messages;
                this.mRemoteInput = remoteInput;
                this.mReadPendingIntent = readPendingIntent;
                this.mReplyPendingIntent = replyPendingIntent;
                this.mParticipants = participants;
                this.mLatestTimestamp = latestTimestamp;
            }

            public String[] getMessages() {
                return this.mMessages;
            }

            public RemoteInput getRemoteInput() {
                return this.mRemoteInput;
            }

            public PendingIntent getReplyPendingIntent() {
                return this.mReplyPendingIntent;
            }

            public PendingIntent getReadPendingIntent() {
                return this.mReadPendingIntent;
            }

            public String[] getParticipants() {
                return this.mParticipants;
            }

            public String getParticipant() {
                return this.mParticipants.length > 0 ? this.mParticipants[0] : null;
            }

            public long getLatestTimestamp() {
                return this.mLatestTimestamp;
            }

            Bundle getBundleForUnreadConversation() {
                Bundle b = new Bundle();
                String author = null;
                if (this.mParticipants != null && this.mParticipants.length > 1) {
                    author = this.mParticipants[0];
                }
                Parcelable[] messages = new Parcelable[this.mMessages.length];
                for (int i = 0; i < messages.length; i++) {
                    Bundle m = new Bundle();
                    m.putString("text", this.mMessages[i]);
                    m.putString(KEY_AUTHOR, author);
                    messages[i] = m;
                }
                b.putParcelableArray(KEY_MESSAGES, messages);
                if (this.mRemoteInput != null) {
                    b.putParcelable(KEY_REMOTE_INPUT, this.mRemoteInput);
                }
                b.putParcelable(KEY_ON_REPLY, this.mReplyPendingIntent);
                b.putParcelable(KEY_ON_READ, this.mReadPendingIntent);
                b.putStringArray(KEY_PARTICIPANTS, this.mParticipants);
                b.putLong("timestamp", this.mLatestTimestamp);
                return b;
            }

            static UnreadConversation getUnreadConversationFromBundle(Bundle b) {
                if (b == null) {
                    return null;
                }
                Parcelable[] parcelableMessages = b.getParcelableArray(KEY_MESSAGES);
                String[] messages = null;
                if (parcelableMessages != null) {
                    String[] tmp = new String[parcelableMessages.length];
                    boolean success = true;
                    for (int i = 0; i < tmp.length; i++) {
                        if (!(parcelableMessages[i] instanceof Bundle)) {
                            success = false;
                            break;
                        }
                        tmp[i] = ((Bundle) parcelableMessages[i]).getString("text");
                        if (tmp[i] == null) {
                            success = false;
                            break;
                        }
                    }
                    if (!success) {
                        return null;
                    }
                    messages = tmp;
                }
                PendingIntent onRead = (PendingIntent) b.getParcelable(KEY_ON_READ);
                PendingIntent onReply = (PendingIntent) b.getParcelable(KEY_ON_REPLY);
                RemoteInput remoteInput = (RemoteInput) b.getParcelable(KEY_REMOTE_INPUT);
                String[] participants = b.getStringArray(KEY_PARTICIPANTS);
                if (participants == null || participants.length != 1) {
                    return null;
                }
                return new UnreadConversation(messages, remoteInput, onReply, onRead, participants, b.getLong("timestamp"));
            }
        }

        public CarExtender(Notification notif) {
            Bundle carBundle = notif.extras == null ? null : notif.extras.getBundle(EXTRA_CAR_EXTENDER);
            if (carBundle != null) {
                this.mLargeIcon = (Bitmap) carBundle.getParcelable(EXTRA_LARGE_ICON);
                this.mColor = carBundle.getInt(EXTRA_COLOR, 0);
                this.mUnreadConversation = UnreadConversation.getUnreadConversationFromBundle(carBundle.getBundle(EXTRA_CONVERSATION));
            }
        }

        public Builder extend(Builder builder) {
            Bundle carExtensions = new Bundle();
            if (this.mLargeIcon != null) {
                carExtensions.putParcelable(EXTRA_LARGE_ICON, this.mLargeIcon);
            }
            if (this.mColor != 0) {
                carExtensions.putInt(EXTRA_COLOR, this.mColor);
            }
            if (this.mUnreadConversation != null) {
                carExtensions.putBundle(EXTRA_CONVERSATION, this.mUnreadConversation.getBundleForUnreadConversation());
            }
            builder.getExtras().putBundle(EXTRA_CAR_EXTENDER, carExtensions);
            return builder;
        }

        public CarExtender setColor(int color) {
            this.mColor = color;
            return this;
        }

        public int getColor() {
            return this.mColor;
        }

        public CarExtender setLargeIcon(Bitmap largeIcon) {
            this.mLargeIcon = largeIcon;
            return this;
        }

        public Bitmap getLargeIcon() {
            return this.mLargeIcon;
        }

        public CarExtender setUnreadConversation(UnreadConversation unreadConversation) {
            this.mUnreadConversation = unreadConversation;
            return this;
        }

        public UnreadConversation getUnreadConversation() {
            return this.mUnreadConversation;
        }
    }

    public static class InboxStyle extends Style {
        private ArrayList<CharSequence> mTexts = new ArrayList(5);

        public InboxStyle(Builder builder) {
            setBuilder(builder);
        }

        public InboxStyle setBigContentTitle(CharSequence title) {
            internalSetBigContentTitle(Notification.safeCharSequence(title));
            return this;
        }

        public InboxStyle setSummaryText(CharSequence cs) {
            internalSetSummaryText(Notification.safeCharSequence(cs));
            return this;
        }

        public InboxStyle addLine(CharSequence cs) {
            this.mTexts.add(Notification.safeCharSequence(cs));
            return this;
        }

        public void addExtras(Bundle extras) {
            super.addExtras(extras);
            extras.putCharSequenceArray(Notification.EXTRA_TEXT_LINES, (CharSequence[]) this.mTexts.toArray(new CharSequence[this.mTexts.size()]));
        }

        protected void restoreFromExtras(Bundle extras) {
            super.restoreFromExtras(extras);
            this.mTexts.clear();
            if (extras.containsKey(Notification.EXTRA_TEXT_LINES)) {
                Collections.addAll(this.mTexts, extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES));
            }
        }

        private RemoteViews makeBigContentView() {
            CharSequence oldBuilderContentText = this.mBuilder.mContentText;
            this.mBuilder.mContentText = null;
            RemoteViews contentView = getStandardView(this.mBuilder.getInboxLayoutResource());
            this.mBuilder.mContentText = oldBuilderContentText;
            contentView.setViewVisibility(R.id.text2, 8);
            int[] rowIds = new int[]{16909359, 16909360, 16909361, 16909362, 16909363, 16909364, 16909365};
            for (int rowId : rowIds) {
                contentView.setViewVisibility(rowId, 8);
            }
            boolean largeText = this.mBuilder.mContext.getResources().getConfiguration().fontScale > 1.0f;
            float subTextSize = (float) this.mBuilder.mContext.getResources().getDimensionPixelSize(17104988);
            int i = 0;
            while (i < this.mTexts.size() && i < rowIds.length) {
                CharSequence str = (CharSequence) this.mTexts.get(i);
                if (!(str == null || str.equals(ProxyInfo.LOCAL_EXCL_LIST))) {
                    contentView.setViewVisibility(rowIds[i], 0);
                    contentView.setTextViewText(rowIds[i], this.mBuilder.processLegacyText(str));
                    if (largeText) {
                        contentView.setTextViewTextSize(rowIds[i], 0, subTextSize);
                    }
                }
                i++;
            }
            contentView.setViewVisibility(16909367, this.mTexts.size() > 0 ? 0 : 8);
            contentView.setViewVisibility(16909366, this.mTexts.size() > rowIds.length ? 0 : 8);
            applyTopPadding(contentView);
            this.mBuilder.shrinkLine3Text(contentView);
            this.mBuilder.addProfileBadge(contentView, 16909354);
            return contentView;
        }

        public void populateBigContentView(Notification wip) {
            this.mBuilder.setBuilderBigContentView(wip, makeBigContentView());
        }
    }

    public static class MediaStyle extends Style {
        static final int MAX_MEDIA_BUTTONS = 5;
        static final int MAX_MEDIA_BUTTONS_IN_COMPACT = 3;
        private int[] mActionsToShowInCompact = null;
        private Token mToken;

        public MediaStyle(Builder builder) {
            setBuilder(builder);
        }

        public MediaStyle setShowActionsInCompactView(int... actions) {
            this.mActionsToShowInCompact = actions;
            return this;
        }

        public MediaStyle setMediaSession(Token token) {
            this.mToken = token;
            return this;
        }

        public Notification buildStyled(Notification wip) {
            super.buildStyled(wip);
            if (wip.category == null) {
                wip.category = Notification.CATEGORY_TRANSPORT;
            }
            return wip;
        }

        public void populateContentView(Notification wip) {
            this.mBuilder.setBuilderContentView(wip, makeMediaContentView());
        }

        public void populateBigContentView(Notification wip) {
            this.mBuilder.setBuilderBigContentView(wip, makeMediaBigContentView());
        }

        public void addExtras(Bundle extras) {
            super.addExtras(extras);
            if (this.mToken != null) {
                extras.putParcelable(Notification.EXTRA_MEDIA_SESSION, this.mToken);
            }
            if (this.mActionsToShowInCompact != null) {
                extras.putIntArray(Notification.EXTRA_COMPACT_ACTIONS, this.mActionsToShowInCompact);
            }
        }

        protected void restoreFromExtras(Bundle extras) {
            super.restoreFromExtras(extras);
            if (extras.containsKey(Notification.EXTRA_MEDIA_SESSION)) {
                this.mToken = (Token) extras.getParcelable(Notification.EXTRA_MEDIA_SESSION);
            }
            if (extras.containsKey(Notification.EXTRA_COMPACT_ACTIONS)) {
                this.mActionsToShowInCompact = extras.getIntArray(Notification.EXTRA_COMPACT_ACTIONS);
            }
        }

        private RemoteViews generateMediaActionButton(Action action) {
            boolean tombstone;
            if (action.actionIntent == null) {
                tombstone = true;
            } else {
                tombstone = false;
            }
            RemoteViews button = new BuilderRemoteViews(this.mBuilder.mContext.getApplicationInfo(), 17367201);
            button.setImageViewIcon(16909347, action.getIcon());
            button.setDrawableParameters(16909347, false, -1, -1, Mode.SRC_ATOP, -1);
            if (!tombstone) {
                button.setOnClickPendingIntent(16909347, action.actionIntent);
            }
            button.setContentDescription(16909347, action.title);
            return button;
        }

        private RemoteViews makeMediaContentView() {
            int N;
            RemoteViews view = this.mBuilder.applyStandardTemplate(17367210, false);
            int numActions = this.mBuilder.mActions.size();
            if (this.mActionsToShowInCompact == null) {
                N = 0;
            } else {
                N = Math.min(this.mActionsToShowInCompact.length, 3);
            }
            if (N > 0) {
                view.removeAllViews(16909356);
                for (int i = 0; i < N; i++) {
                    if (i >= numActions) {
                        throw new IllegalArgumentException(String.format("setShowActionsInCompactView: action %d out of bounds (max %d)", new Object[]{Integer.valueOf(i), Integer.valueOf(numActions - 1)}));
                    }
                    view.addView(16909356, generateMediaActionButton((Action) this.mBuilder.mActions.get(this.mActionsToShowInCompact[i])));
                }
            }
            styleText(view);
            hideRightIcon(view);
            return view;
        }

        private RemoteViews makeMediaBigContentView() {
            int actionCount = Math.min(this.mBuilder.mActions.size(), 5);
            RemoteViews big = this.mBuilder.applyStandardTemplate(getBigLayoutResource(actionCount), false);
            if (actionCount > 0) {
                big.removeAllViews(16909356);
                for (int i = 0; i < actionCount; i++) {
                    big.addView(16909356, generateMediaActionButton((Action) this.mBuilder.mActions.get(i)));
                }
            }
            styleText(big);
            hideRightIcon(big);
            applyTopPadding(big);
            big.setViewVisibility(R.id.progress, 8);
            return big;
        }

        private int getBigLayoutResource(int actionCount) {
            if (actionCount <= 3) {
                return 17367206;
            }
            return 17367205;
        }

        private void hideRightIcon(RemoteViews contentView) {
            contentView.setViewVisibility(16908352, 8);
        }

        private void styleText(RemoteViews contentView) {
            int primaryColor = this.mBuilder.mContext.getColor(17170513);
            int secondaryColor = this.mBuilder.mContext.getColor(17170514);
            contentView.setTextColor(R.id.title, primaryColor);
            if (this.mBuilder.showsTimeOrChronometer()) {
                if (this.mBuilder.mUseChronometer) {
                    contentView.setTextColor(16909368, secondaryColor);
                } else {
                    contentView.setTextColor(16908442, secondaryColor);
                }
            }
            contentView.setTextColor(R.id.text2, secondaryColor);
            contentView.setTextColor(16908419, secondaryColor);
            contentView.setTextColor(16909372, secondaryColor);
        }

        protected boolean hasProgress() {
            return false;
        }
    }

    public static final class WearableExtender implements Extender {
        private static final int DEFAULT_CONTENT_ICON_GRAVITY = 8388613;
        private static final int DEFAULT_FLAGS = 1;
        private static final int DEFAULT_GRAVITY = 80;
        private static final String EXTRA_WEARABLE_EXTENSIONS = "android.wearable.EXTENSIONS";
        private static final int FLAG_CONTENT_INTENT_AVAILABLE_OFFLINE = 1;
        private static final int FLAG_HINT_AVOID_BACKGROUND_CLIPPING = 16;
        private static final int FLAG_HINT_HIDE_ICON = 2;
        private static final int FLAG_HINT_SHOW_BACKGROUND_ONLY = 4;
        private static final int FLAG_START_SCROLL_BOTTOM = 8;
        private static final String KEY_ACTIONS = "actions";
        private static final String KEY_BACKGROUND = "background";
        private static final String KEY_CONTENT_ACTION_INDEX = "contentActionIndex";
        private static final String KEY_CONTENT_ICON = "contentIcon";
        private static final String KEY_CONTENT_ICON_GRAVITY = "contentIconGravity";
        private static final String KEY_CUSTOM_CONTENT_HEIGHT = "customContentHeight";
        private static final String KEY_CUSTOM_SIZE_PRESET = "customSizePreset";
        private static final String KEY_DISPLAY_INTENT = "displayIntent";
        private static final String KEY_FLAGS = "flags";
        private static final String KEY_GRAVITY = "gravity";
        private static final String KEY_HINT_SCREEN_TIMEOUT = "hintScreenTimeout";
        private static final String KEY_PAGES = "pages";
        public static final int SCREEN_TIMEOUT_LONG = -1;
        public static final int SCREEN_TIMEOUT_SHORT = 0;
        public static final int SIZE_DEFAULT = 0;
        public static final int SIZE_FULL_SCREEN = 5;
        public static final int SIZE_LARGE = 4;
        public static final int SIZE_MEDIUM = 3;
        public static final int SIZE_SMALL = 2;
        public static final int SIZE_XSMALL = 1;
        public static final int UNSET_ACTION_INDEX = -1;
        private ArrayList<Action> mActions = new ArrayList();
        private Bitmap mBackground;
        private int mContentActionIndex = -1;
        private int mContentIcon;
        private int mContentIconGravity = DEFAULT_CONTENT_ICON_GRAVITY;
        private int mCustomContentHeight;
        private int mCustomSizePreset = 0;
        private PendingIntent mDisplayIntent;
        private int mFlags = 1;
        private int mGravity = 80;
        private int mHintScreenTimeout;
        private ArrayList<Notification> mPages = new ArrayList();

        public WearableExtender(Notification notif) {
            Bundle wearableBundle = notif.extras.getBundle(EXTRA_WEARABLE_EXTENSIONS);
            if (wearableBundle != null) {
                List<Action> actions = wearableBundle.getParcelableArrayList(KEY_ACTIONS);
                if (actions != null) {
                    this.mActions.addAll(actions);
                }
                this.mFlags = wearableBundle.getInt("flags", 1);
                this.mDisplayIntent = (PendingIntent) wearableBundle.getParcelable(KEY_DISPLAY_INTENT);
                Notification[] pages = Notification.getNotificationArrayFromBundle(wearableBundle, KEY_PAGES);
                if (pages != null) {
                    Collections.addAll(this.mPages, pages);
                }
                this.mBackground = (Bitmap) wearableBundle.getParcelable(KEY_BACKGROUND);
                this.mContentIcon = wearableBundle.getInt(KEY_CONTENT_ICON);
                this.mContentIconGravity = wearableBundle.getInt(KEY_CONTENT_ICON_GRAVITY, DEFAULT_CONTENT_ICON_GRAVITY);
                this.mContentActionIndex = wearableBundle.getInt(KEY_CONTENT_ACTION_INDEX, -1);
                this.mCustomSizePreset = wearableBundle.getInt(KEY_CUSTOM_SIZE_PRESET, 0);
                this.mCustomContentHeight = wearableBundle.getInt(KEY_CUSTOM_CONTENT_HEIGHT);
                this.mGravity = wearableBundle.getInt(KEY_GRAVITY, 80);
                this.mHintScreenTimeout = wearableBundle.getInt(KEY_HINT_SCREEN_TIMEOUT);
            }
        }

        public Builder extend(Builder builder) {
            Bundle wearableBundle = new Bundle();
            if (!this.mActions.isEmpty()) {
                wearableBundle.putParcelableArrayList(KEY_ACTIONS, this.mActions);
            }
            if (this.mFlags != 1) {
                wearableBundle.putInt("flags", this.mFlags);
            }
            if (this.mDisplayIntent != null) {
                wearableBundle.putParcelable(KEY_DISPLAY_INTENT, this.mDisplayIntent);
            }
            if (!this.mPages.isEmpty()) {
                wearableBundle.putParcelableArray(KEY_PAGES, (Parcelable[]) this.mPages.toArray(new Notification[this.mPages.size()]));
            }
            if (this.mBackground != null) {
                wearableBundle.putParcelable(KEY_BACKGROUND, this.mBackground);
            }
            if (this.mContentIcon != 0) {
                wearableBundle.putInt(KEY_CONTENT_ICON, this.mContentIcon);
            }
            if (this.mContentIconGravity != DEFAULT_CONTENT_ICON_GRAVITY) {
                wearableBundle.putInt(KEY_CONTENT_ICON_GRAVITY, this.mContentIconGravity);
            }
            if (this.mContentActionIndex != -1) {
                wearableBundle.putInt(KEY_CONTENT_ACTION_INDEX, this.mContentActionIndex);
            }
            if (this.mCustomSizePreset != 0) {
                wearableBundle.putInt(KEY_CUSTOM_SIZE_PRESET, this.mCustomSizePreset);
            }
            if (this.mCustomContentHeight != 0) {
                wearableBundle.putInt(KEY_CUSTOM_CONTENT_HEIGHT, this.mCustomContentHeight);
            }
            if (this.mGravity != 80) {
                wearableBundle.putInt(KEY_GRAVITY, this.mGravity);
            }
            if (this.mHintScreenTimeout != 0) {
                wearableBundle.putInt(KEY_HINT_SCREEN_TIMEOUT, this.mHintScreenTimeout);
            }
            builder.getExtras().putBundle(EXTRA_WEARABLE_EXTENSIONS, wearableBundle);
            return builder;
        }

        public WearableExtender clone() {
            WearableExtender that = new WearableExtender();
            that.mActions = new ArrayList(this.mActions);
            that.mFlags = this.mFlags;
            that.mDisplayIntent = this.mDisplayIntent;
            that.mPages = new ArrayList(this.mPages);
            that.mBackground = this.mBackground;
            that.mContentIcon = this.mContentIcon;
            that.mContentIconGravity = this.mContentIconGravity;
            that.mContentActionIndex = this.mContentActionIndex;
            that.mCustomSizePreset = this.mCustomSizePreset;
            that.mCustomContentHeight = this.mCustomContentHeight;
            that.mGravity = this.mGravity;
            that.mHintScreenTimeout = this.mHintScreenTimeout;
            return that;
        }

        public WearableExtender addAction(Action action) {
            this.mActions.add(action);
            return this;
        }

        public WearableExtender addActions(List<Action> actions) {
            this.mActions.addAll(actions);
            return this;
        }

        public WearableExtender clearActions() {
            this.mActions.clear();
            return this;
        }

        public List<Action> getActions() {
            return this.mActions;
        }

        public WearableExtender setDisplayIntent(PendingIntent intent) {
            this.mDisplayIntent = intent;
            return this;
        }

        public PendingIntent getDisplayIntent() {
            return this.mDisplayIntent;
        }

        public WearableExtender addPage(Notification page) {
            this.mPages.add(page);
            return this;
        }

        public WearableExtender addPages(List<Notification> pages) {
            this.mPages.addAll(pages);
            return this;
        }

        public WearableExtender clearPages() {
            this.mPages.clear();
            return this;
        }

        public List<Notification> getPages() {
            return this.mPages;
        }

        public WearableExtender setBackground(Bitmap background) {
            this.mBackground = background;
            return this;
        }

        public Bitmap getBackground() {
            return this.mBackground;
        }

        public WearableExtender setContentIcon(int icon) {
            this.mContentIcon = icon;
            return this;
        }

        public int getContentIcon() {
            return this.mContentIcon;
        }

        public WearableExtender setContentIconGravity(int contentIconGravity) {
            this.mContentIconGravity = contentIconGravity;
            return this;
        }

        public int getContentIconGravity() {
            return this.mContentIconGravity;
        }

        public WearableExtender setContentAction(int actionIndex) {
            this.mContentActionIndex = actionIndex;
            return this;
        }

        public int getContentAction() {
            return this.mContentActionIndex;
        }

        public WearableExtender setGravity(int gravity) {
            this.mGravity = gravity;
            return this;
        }

        public int getGravity() {
            return this.mGravity;
        }

        public WearableExtender setCustomSizePreset(int sizePreset) {
            this.mCustomSizePreset = sizePreset;
            return this;
        }

        public int getCustomSizePreset() {
            return this.mCustomSizePreset;
        }

        public WearableExtender setCustomContentHeight(int height) {
            this.mCustomContentHeight = height;
            return this;
        }

        public int getCustomContentHeight() {
            return this.mCustomContentHeight;
        }

        public WearableExtender setStartScrollBottom(boolean startScrollBottom) {
            setFlag(8, startScrollBottom);
            return this;
        }

        public boolean getStartScrollBottom() {
            return (this.mFlags & 8) != 0;
        }

        public WearableExtender setContentIntentAvailableOffline(boolean contentIntentAvailableOffline) {
            setFlag(1, contentIntentAvailableOffline);
            return this;
        }

        public boolean getContentIntentAvailableOffline() {
            return (this.mFlags & 1) != 0;
        }

        public WearableExtender setHintHideIcon(boolean hintHideIcon) {
            setFlag(2, hintHideIcon);
            return this;
        }

        public boolean getHintHideIcon() {
            return (this.mFlags & 2) != 0;
        }

        public WearableExtender setHintShowBackgroundOnly(boolean hintShowBackgroundOnly) {
            setFlag(4, hintShowBackgroundOnly);
            return this;
        }

        public boolean getHintShowBackgroundOnly() {
            return (this.mFlags & 4) != 0;
        }

        public WearableExtender setHintAvoidBackgroundClipping(boolean hintAvoidBackgroundClipping) {
            setFlag(16, hintAvoidBackgroundClipping);
            return this;
        }

        public boolean getHintAvoidBackgroundClipping() {
            return (this.mFlags & 16) != 0;
        }

        public WearableExtender setHintScreenTimeout(int timeout) {
            this.mHintScreenTimeout = timeout;
            return this;
        }

        public int getHintScreenTimeout() {
            return this.mHintScreenTimeout;
        }

        private void setFlag(int mask, boolean value) {
            if (value) {
                this.mFlags |= mask;
            } else {
                this.mFlags &= mask ^ -1;
            }
        }
    }

    static {
        boolean z = true;
        if (Debug.isProductShip() == 1) {
            z = false;
        }
        DEBUG = z;
    }

    public String getGroup() {
        return this.mGroupKey;
    }

    public String getSortKey() {
        return this.mSortKey;
    }

    public Notification() {
        Map hashMap;
        if (Build.IS_SYSTEM_SECURE) {
            hashMap = new HashMap();
        } else {
            hashMap = null;
        }
        this.stringNamesMap = hashMap;
        this.audioStreamType = -1;
        this.audioAttributes = AUDIO_ATTRIBUTES_DEFAULT;
        this.color = 0;
        this.extras = new Bundle();
        this.knoxFlags = 0;
        this.when = System.currentTimeMillis();
        this.priority = 0;
    }

    public Notification(Context context, int icon, CharSequence tickerText, long when, CharSequence contentTitle, CharSequence contentText, Intent contentIntent) {
        Map hashMap;
        if (Build.IS_SYSTEM_SECURE) {
            hashMap = new HashMap();
        } else {
            hashMap = null;
        }
        this.stringNamesMap = hashMap;
        this.audioStreamType = -1;
        this.audioAttributes = AUDIO_ATTRIBUTES_DEFAULT;
        this.color = 0;
        this.extras = new Bundle();
        this.knoxFlags = 0;
        new Builder(context).setWhen(when).setSmallIcon(icon).setTicker(tickerText).setContentTitle(contentTitle).setContentText(contentText).setContentIntent(PendingIntent.getActivity(context, 0, contentIntent, 0)).buildInto(this);
    }

    @Deprecated
    public Notification(int icon, CharSequence tickerText, long when) {
        Map hashMap;
        if (Build.IS_SYSTEM_SECURE) {
            hashMap = new HashMap();
        } else {
            hashMap = null;
        }
        this.stringNamesMap = hashMap;
        this.audioStreamType = -1;
        this.audioAttributes = AUDIO_ATTRIBUTES_DEFAULT;
        this.color = 0;
        this.extras = new Bundle();
        this.knoxFlags = 0;
        this.icon = icon;
        this.tickerText = tickerText;
        this.when = when;
    }

    public Notification(Parcel parcel) {
        Map hashMap;
        if (Build.IS_SYSTEM_SECURE) {
            hashMap = new HashMap();
        } else {
            hashMap = null;
        }
        this.stringNamesMap = hashMap;
        this.audioStreamType = -1;
        this.audioAttributes = AUDIO_ATTRIBUTES_DEFAULT;
        this.color = 0;
        this.extras = new Bundle();
        this.knoxFlags = 0;
        int version = parcel.readInt();
        this.when = parcel.readLong();
        if (parcel.readInt() != 0) {
            this.mSmallIcon = (Icon) Icon.CREATOR.createFromParcel(parcel);
            if (this.mSmallIcon.getType() == 2) {
                this.icon = this.mSmallIcon.getResId();
            }
        }
        this.number = parcel.readInt();
        if (parcel.readInt() != 0) {
            this.contentIntent = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
        }
        if (parcel.readInt() != 0) {
            this.deleteIntent = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
        }
        if (parcel.readInt() != 0) {
            this.tickerText = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        }
        if (parcel.readInt() != 0) {
            this.tickerView = (RemoteViews) RemoteViews.CREATOR.createFromParcel(parcel);
        }
        if (parcel.readInt() != 0) {
            this.contentView = (RemoteViews) RemoteViews.CREATOR.createFromParcel(parcel);
        }
        if (parcel.readInt() != 0) {
            this.mLargeIcon = (Icon) Icon.CREATOR.createFromParcel(parcel);
        }
        this.defaults = parcel.readInt();
        this.flags = parcel.readInt();
        if (parcel.readInt() != 0) {
            this.sound = (Uri) Uri.CREATOR.createFromParcel(parcel);
        }
        this.audioStreamType = parcel.readInt();
        if (parcel.readInt() != 0) {
            this.audioAttributes = (AudioAttributes) AudioAttributes.CREATOR.createFromParcel(parcel);
        }
        this.vibrate = parcel.createLongArray();
        this.ledARGB = parcel.readInt();
        this.ledOnMS = parcel.readInt();
        this.ledOffMS = parcel.readInt();
        this.iconLevel = parcel.readInt();
        if (parcel.readInt() != 0) {
            this.fullScreenIntent = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
        }
        this.priority = parcel.readInt();
        this.category = parcel.readString();
        this.mGroupKey = parcel.readString();
        this.mSortKey = parcel.readString();
        this.extras = parcel.readBundle();
        this.actions = (Action[]) parcel.createTypedArray(Action.CREATOR);
        if (parcel.readInt() != 0) {
            this.bigContentView = (RemoteViews) RemoteViews.CREATOR.createFromParcel(parcel);
        }
        if (parcel.readInt() != 0) {
            this.headsUpContentView = (RemoteViews) RemoteViews.CREATOR.createFromParcel(parcel);
        }
        this.visibility = parcel.readInt();
        if (parcel.readInt() != 0) {
            this.publicVersion = (Notification) CREATOR.createFromParcel(parcel);
        }
        this.color = parcel.readInt();
        this.twQuickPanelEvent = parcel.readInt();
        this.commonValue = parcel.readInt();
        this.missedCount = parcel.readInt();
        if (parcel.readInt() != 0) {
            this.contactCharSeq = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        }
        this.haptic = parcel.readInt();
        this.threadId = parcel.readLong();
        this.secFlags = parcel.readInt();
        this.secPriority = parcel.readInt();
        if (Build.IS_SYSTEM_SECURE) {
            int size = parcel.readInt();
            for (int i = 0; i < size; i++) {
                this.stringNamesMap.put(parcel.readCharSequence(), parcel.readCharSequence());
            }
        }
    }

    public Notification clone() {
        Notification that = new Notification();
        cloneInto(that, true);
        return that;
    }

    public void cloneInto(Notification that, boolean heavy) {
        that.when = this.when;
        that.mSmallIcon = this.mSmallIcon;
        that.number = this.number;
        that.contentIntent = this.contentIntent;
        that.deleteIntent = this.deleteIntent;
        that.fullScreenIntent = this.fullScreenIntent;
        if (this.tickerText != null) {
            that.tickerText = this.tickerText.toString();
        }
        if (heavy && this.tickerView != null) {
            that.tickerView = this.tickerView.clone();
        }
        if (heavy && this.contentView != null) {
            that.contentView = this.contentView.clone();
        }
        if (heavy && this.mLargeIcon != null) {
            that.mLargeIcon = this.mLargeIcon;
        }
        that.iconLevel = this.iconLevel;
        that.sound = this.sound;
        that.audioStreamType = this.audioStreamType;
        if (this.audioAttributes != null) {
            that.audioAttributes = new android.media.AudioAttributes.Builder(this.audioAttributes).build();
        }
        long[] vibrate = this.vibrate;
        if (vibrate != null) {
            int N = vibrate.length;
            long[] vib = new long[N];
            that.vibrate = vib;
            System.arraycopy(vibrate, 0, vib, 0, N);
        }
        that.ledARGB = this.ledARGB;
        that.ledOnMS = this.ledOnMS;
        that.ledOffMS = this.ledOffMS;
        that.defaults = this.defaults;
        that.flags = this.flags;
        that.priority = this.priority;
        that.category = this.category;
        that.mGroupKey = this.mGroupKey;
        that.mSortKey = this.mSortKey;
        if (this.extras != null) {
            try {
                that.extras = new Bundle(this.extras);
                that.extras.size();
            } catch (BadParcelableException e) {
                Log.e(TAG, "could not unparcel extras from notification: " + this, e);
                that.extras = null;
            }
        }
        if (this.actions != null) {
            that.actions = new Action[this.actions.length];
            for (int i = 0; i < this.actions.length; i++) {
                that.actions[i] = this.actions[i].clone();
            }
        }
        if (heavy && this.bigContentView != null) {
            that.bigContentView = this.bigContentView.clone();
        }
        if (heavy && this.headsUpContentView != null) {
            that.headsUpContentView = this.headsUpContentView.clone();
        }
        that.visibility = this.visibility;
        if (this.publicVersion != null) {
            that.publicVersion = new Notification();
            this.publicVersion.cloneInto(that.publicVersion, heavy);
        }
        that.color = this.color;
        if (!heavy) {
            that.lightenPayload();
        }
        that.twQuickPanelEvent = this.twQuickPanelEvent;
        that.missedCount = this.missedCount;
        that.contactCharSeq = this.contactCharSeq;
        that.haptic = this.haptic;
        that.threadId = this.threadId;
        that.secFlags = this.secFlags;
        if (this.commonValue == MAGIC_NUMBER_HIDE_ACTION_BUTTON_ICON) {
            that.secFlags |= 4;
        } else if (this.commonValue == -2139062144) {
            that.secFlags |= 1;
        } else if (this.commonValue == 269488144) {
            that.secFlags |= 2;
        } else {
            that.commonValue = this.commonValue;
        }
        that.secPriority = this.secPriority;
    }

    public final void lightenPayload() {
        this.tickerView = null;
        this.contentView = null;
        this.bigContentView = null;
        this.headsUpContentView = null;
        this.mLargeIcon = null;
        if (this.extras != null) {
            this.extras.remove(EXTRA_LARGE_ICON);
            this.extras.remove(EXTRA_LARGE_ICON_BIG);
            this.extras.remove(EXTRA_PICTURE);
            this.extras.remove(EXTRA_BIG_TEXT);
            this.extras.remove(Builder.EXTRA_NEEDS_REBUILD);
        }
    }

    public static CharSequence safeCharSequence(CharSequence cs) {
        if (cs == null) {
            return cs;
        }
        if (cs.length() > 5120) {
            cs = cs.subSequence(0, 5120);
        }
        if (!(cs instanceof Parcelable)) {
            return cs;
        }
        Log.e(TAG, "warning: " + cs.getClass().getCanonicalName() + " instance is a custom Parcelable and not allowed in Notification");
        return cs.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(1);
        parcel.writeLong(this.when);
        if (this.mSmallIcon == null && this.icon != 0) {
            this.mSmallIcon = Icon.createWithResource(ProxyInfo.LOCAL_EXCL_LIST, this.icon);
        }
        if (this.mSmallIcon != null) {
            parcel.writeInt(1);
            this.mSmallIcon.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.number);
        if (this.contentIntent != null) {
            parcel.writeInt(1);
            this.contentIntent.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        if (this.deleteIntent != null) {
            parcel.writeInt(1);
            this.deleteIntent.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        if (this.tickerText != null) {
            parcel.writeInt(1);
            TextUtils.writeToParcel(this.tickerText, parcel, flags);
        } else {
            parcel.writeInt(0);
        }
        if (this.tickerView != null) {
            parcel.writeInt(1);
            this.tickerView.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        if (this.contentView != null) {
            parcel.writeInt(1);
            this.contentView.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        if (this.mLargeIcon != null) {
            parcel.writeInt(1);
            this.mLargeIcon.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.defaults);
        parcel.writeInt(this.flags);
        if (this.sound != null) {
            parcel.writeInt(1);
            this.sound.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.audioStreamType);
        if (this.audioAttributes != null) {
            parcel.writeInt(1);
            this.audioAttributes.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeLongArray(this.vibrate);
        parcel.writeInt(this.ledARGB);
        parcel.writeInt(this.ledOnMS);
        parcel.writeInt(this.ledOffMS);
        parcel.writeInt(this.iconLevel);
        if (this.fullScreenIntent != null) {
            parcel.writeInt(1);
            this.fullScreenIntent.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.priority);
        parcel.writeString(this.category);
        parcel.writeString(this.mGroupKey);
        parcel.writeString(this.mSortKey);
        parcel.writeBundle(this.extras);
        parcel.writeTypedArray(this.actions, 0);
        if (this.bigContentView != null) {
            parcel.writeInt(1);
            this.bigContentView.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        if (this.headsUpContentView != null) {
            parcel.writeInt(1);
            this.headsUpContentView.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.visibility);
        if (this.publicVersion != null) {
            parcel.writeInt(1);
            this.publicVersion.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.color);
        parcel.writeInt(this.twQuickPanelEvent);
        parcel.writeInt(this.commonValue);
        parcel.writeInt(this.missedCount);
        if (this.contactCharSeq != null) {
            parcel.writeInt(1);
            TextUtils.writeToParcel(this.contactCharSeq, parcel, flags);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.haptic);
        parcel.writeLong(this.threadId);
        parcel.writeInt(this.secFlags);
        parcel.writeInt(this.secPriority);
        if (Build.IS_SYSTEM_SECURE) {
            parcel.writeInt(this.stringNamesMap.size());
            for (CharSequence key : this.stringNamesMap.keySet()) {
                parcel.writeCharSequence(key);
                parcel.writeCharSequence((CharSequence) this.stringNamesMap.get(key));
            }
        }
    }

    @Deprecated
    public void setLatestEventInfo(Context context, CharSequence contentTitle, CharSequence contentText, PendingIntent contentIntent) {
        Builder builder = new Builder(context);
        builder.setWhen(this.when);
        builder.setSmallIcon(this.icon);
        builder.setPriority(this.priority);
        builder.setTicker(this.tickerText);
        builder.setNumber(this.number);
        builder.setColor(this.color);
        builder.mFlags = this.flags;
        builder.setSound(this.sound, this.audioStreamType);
        builder.setDefaults(this.defaults);
        builder.setVibrate(this.vibrate);
        builder.setDeleteIntent(this.deleteIntent);
        if (contentTitle != null) {
            builder.setContentTitle(contentTitle);
        }
        if (contentText != null) {
            builder.setContentText(contentText);
        }
        builder.setContentIntent(contentIntent);
        builder.buildInto(this);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Notification(pri=");
        sb.append(this.priority);
        sb.append(" contentView=");
        if (this.contentView != null) {
            sb.append(this.contentView.getPackage());
            sb.append("/0x");
            sb.append(Integer.toHexString(this.contentView.getLayoutId()));
        } else {
            sb.append("null");
        }
        sb.append(" vibrate=");
        if ((this.defaults & 2) != 0) {
            sb.append(PersonaInfo.PERSONA_TYPE_DEFAULT);
        } else if (this.vibrate != null) {
            int N = this.vibrate.length - 1;
            sb.append("[");
            for (int i = 0; i < N; i++) {
                sb.append(this.vibrate[i]);
                sb.append(',');
            }
            if (N != -1) {
                sb.append(this.vibrate[N]);
            }
            sb.append("]");
        } else {
            sb.append("null");
        }
        sb.append(" sound=");
        if ((this.defaults & 1) != 0) {
            sb.append(PersonaInfo.PERSONA_TYPE_DEFAULT);
        } else if (this.sound != null) {
            sb.append(this.sound.toString());
        } else {
            sb.append("null");
        }
        if (this.tickerText != null) {
            sb.append(" tick");
        }
        sb.append(" defaults=0x");
        sb.append(Integer.toHexString(this.defaults));
        sb.append(" flags=0x");
        sb.append(Integer.toHexString(this.flags));
        sb.append(String.format(" color=0x%08x", new Object[]{Integer.valueOf(this.color)}));
        if (this.category != null) {
            sb.append(" category=");
            sb.append(this.category);
        }
        if (this.mGroupKey != null) {
            sb.append(" groupKey=");
            sb.append(this.mGroupKey);
        }
        if (this.mSortKey != null) {
            sb.append(" sortKey=");
            sb.append(this.mSortKey);
        }
        if (this.actions != null) {
            sb.append(" actions=");
            sb.append(this.actions.length);
        }
        sb.append(" vis=");
        sb.append(visibilityToString(this.visibility));
        if (this.publicVersion != null) {
            sb.append(" publicVersion=");
            sb.append(this.publicVersion.toString());
        }
        sb.append(" secFlags=0x");
        sb.append(Integer.toHexString(this.secFlags));
        sb.append(" secPriority=");
        sb.append(this.secPriority);
        sb.append(")");
        return sb.toString();
    }

    public static String visibilityToString(int vis) {
        switch (vis) {
            case -1:
                return "SECRET";
            case 0:
                return "PRIVATE";
            case 1:
                return "PUBLIC";
            default:
                return "UNKNOWN(" + String.valueOf(vis) + ")";
        }
    }

    public static String priorityToString(int pri) {
        switch (pri) {
            case -2:
                return "MIN";
            case -1:
                return "LOW";
            case 0:
                return "DEFAULT";
            case 1:
                return "HIGH";
            case 2:
                return "MAX";
            default:
                return "UNKNOWN(" + String.valueOf(pri) + ")";
        }
    }

    public Icon getSmallIcon() {
        return this.mSmallIcon;
    }

    public void setSmallIcon(Icon icon) {
        this.mSmallIcon = icon;
    }

    public Icon getLargeIcon() {
        return this.mLargeIcon;
    }

    public boolean isValid() {
        return this.contentView != null || this.extras.getBoolean(Builder.EXTRA_REBUILD_CONTENT_VIEW);
    }

    public boolean isGroupSummary() {
        return (this.mGroupKey == null || (this.flags & 512) == 0) ? false : true;
    }

    public boolean isGroupChild() {
        return this.mGroupKey != null && (this.flags & 512) == 0;
    }

    private static Notification[] getNotificationArrayFromBundle(Bundle bundle, String key) {
        Parcelable[] array = bundle.getParcelableArray(key);
        if ((array instanceof Notification[]) || array == null) {
            return (Notification[]) array;
        }
        Notification[] typedArray = (Notification[]) Arrays.copyOf(array, array.length, Notification[].class);
        bundle.putParcelableArray(key, typedArray);
        return typedArray;
    }

    public void setKnoxFlag(int flag) {
        this.knoxFlags |= flag;
    }

    public void setOriginalPackageName(CharSequence packageName) {
        this.originalPackageName = packageName;
    }

    public void setOriginalUserId(int userId) {
        this.originalUserId = userId;
    }
}
