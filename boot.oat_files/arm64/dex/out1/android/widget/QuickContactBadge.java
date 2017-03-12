package android.widget;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.QuickContact;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import com.android.internal.R;

public class QuickContactBadge extends ImageView implements OnClickListener {
    static final int EMAIL_ID_COLUMN_INDEX = 0;
    static final String[] EMAIL_LOOKUP_PROJECTION = new String[]{"contact_id", "lookup"};
    static final int EMAIL_LOOKUP_STRING_COLUMN_INDEX = 1;
    private static final String EXTRA_URI_CONTENT = "uri_content";
    static final int PHONE_ID_COLUMN_INDEX = 0;
    static final String[] PHONE_LOOKUP_PROJECTION = new String[]{"_id", "lookup"};
    static final int PHONE_LOOKUP_STRING_COLUMN_INDEX = 1;
    private static final int TOKEN_EMAIL_LOOKUP = 0;
    private static final int TOKEN_EMAIL_LOOKUP_AND_TRIGGER = 2;
    private static final int TOKEN_PHONE_LOOKUP = 1;
    private static final int TOKEN_PHONE_LOOKUP_AND_TRIGGER = 3;
    private String mContactEmail;
    private String mContactPhone;
    private Uri mContactUri;
    private Drawable mDefaultAvatar;
    protected String[] mExcludeMimes;
    private Bundle mExtras;
    private Drawable mOverlay;
    private String mPrioritizedMimeType;
    private QueryHandler mQueryHandler;

    private class QueryHandler extends AsyncQueryHandler {
        public QueryHandler(ContentResolver cr) {
            super(cr);
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        protected void onQueryComplete(int r22, java.lang.Object r23, android.database.Cursor r24) {
            /*
            r21 = this;
            r13 = 0;
            r5 = 0;
            r15 = 0;
            if (r23 == 0) goto L_0x0067;
        L_0x0005:
            r23 = (android.os.Bundle) r23;
            r9 = r23;
        L_0x0009:
            switch(r22) {
                case 0: goto L_0x00c6;
                case 1: goto L_0x0091;
                case 2: goto L_0x00b3;
                case 3: goto L_0x006d;
                default: goto L_0x000c;
            };
        L_0x000c:
            if (r24 == 0) goto L_0x0011;
        L_0x000e:
            r24.close();
        L_0x0011:
            r0 = r21;
            r0 = android.widget.QuickContactBadge.this;
            r16 = r0;
            r0 = r16;
            r0.mContactUri = r13;
            r0 = r21;
            r0 = android.widget.QuickContactBadge.this;
            r16 = r0;
            r16.onContactUriChanged();
            if (r15 == 0) goto L_0x00ef;
        L_0x0027:
            r0 = r21;
            r0 = android.widget.QuickContactBadge.this;
            r16 = r0;
            r16 = r16.mContactUri;
            if (r16 == 0) goto L_0x00ef;
        L_0x0033:
            r0 = r21;
            r0 = android.widget.QuickContactBadge.this;
            r16 = r0;
            r16 = r16.getContext();
            r0 = r21;
            r0 = android.widget.QuickContactBadge.this;
            r17 = r0;
            r0 = r21;
            r0 = android.widget.QuickContactBadge.this;
            r18 = r0;
            r18 = r18.mContactUri;
            r0 = r21;
            r0 = android.widget.QuickContactBadge.this;
            r19 = r0;
            r0 = r19;
            r0 = r0.mExcludeMimes;
            r19 = r0;
            r0 = r21;
            r0 = android.widget.QuickContactBadge.this;
            r20 = r0;
            r20 = r20.mPrioritizedMimeType;
            android.provider.ContactsContract.QuickContact.showQuickContact(r16, r17, r18, r19, r20);
        L_0x0066:
            return;
        L_0x0067:
            r9 = new android.os.Bundle;
            r9.<init>();
            goto L_0x0009;
        L_0x006d:
            r15 = 1;
            if (r9 == 0) goto L_0x007d;
        L_0x0070:
            if (r9 == 0) goto L_0x007f;
        L_0x0072:
            r16 = "uri_content";
            r0 = r16;
            r16 = r9.getString(r0);	 Catch:{ all -> 0x00e8 }
            if (r16 != 0) goto L_0x007f;
        L_0x007d:
            r5 = 0;
            goto L_0x000c;
        L_0x007f:
            r16 = "tel";
            r17 = "uri_content";
            r0 = r17;
            r17 = r9.getString(r0);	 Catch:{ all -> 0x00e8 }
            r18 = 0;
            r5 = android.net.Uri.fromParts(r16, r17, r18);	 Catch:{ all -> 0x00e8 }
        L_0x0091:
            if (r24 == 0) goto L_0x000c;
        L_0x0093:
            r16 = r24.moveToFirst();	 Catch:{ all -> 0x00e8 }
            if (r16 == 0) goto L_0x000c;
        L_0x0099:
            r16 = 0;
            r0 = r24;
            r1 = r16;
            r6 = r0.getLong(r1);	 Catch:{ all -> 0x00e8 }
            r16 = 1;
            r0 = r24;
            r1 = r16;
            r12 = r0.getString(r1);	 Catch:{ all -> 0x00e8 }
            r13 = android.provider.ContactsContract.Contacts.getLookupUri(r6, r12);	 Catch:{ all -> 0x00e8 }
            goto L_0x000c;
        L_0x00b3:
            r15 = 1;
            r16 = "mailto";
            r17 = "uri_content";
            r0 = r17;
            r17 = r9.getString(r0);	 Catch:{ all -> 0x00e8 }
            r18 = 0;
            r5 = android.net.Uri.fromParts(r16, r17, r18);	 Catch:{ all -> 0x00e8 }
        L_0x00c6:
            if (r24 == 0) goto L_0x000c;
        L_0x00c8:
            r16 = r24.moveToFirst();	 Catch:{ all -> 0x00e8 }
            if (r16 == 0) goto L_0x000c;
        L_0x00ce:
            r16 = 0;
            r0 = r24;
            r1 = r16;
            r6 = r0.getLong(r1);	 Catch:{ all -> 0x00e8 }
            r16 = 1;
            r0 = r24;
            r1 = r16;
            r12 = r0.getString(r1);	 Catch:{ all -> 0x00e8 }
            r13 = android.provider.ContactsContract.Contacts.getLookupUri(r6, r12);	 Catch:{ all -> 0x00e8 }
            goto L_0x000c;
        L_0x00e8:
            r16 = move-exception;
            if (r24 == 0) goto L_0x00ee;
        L_0x00eb:
            r24.close();
        L_0x00ee:
            throw r16;
        L_0x00ef:
            if (r5 == 0) goto L_0x0066;
        L_0x00f1:
            r10 = new android.content.Intent;
            r16 = "com.android.contacts.action.SHOW_OR_CREATE_CONTACT";
            r0 = r16;
            r10.<init>(r0, r5);
            r16 = com.sec.android.app.CscFeature.getInstance();
            r17 = "CscFeature_Contact_EnableDocomoAccountAsDefault";
            r16 = r16.getEnableStatus(r17);
            if (r16 == 0) goto L_0x0163;
        L_0x0106:
            r0 = r21;
            r0 = android.widget.QuickContactBadge.this;
            r16 = r0;
            r16 = r16.mContext;
            r17 = "android.permission.GET_TASKS";
            r14 = r16.checkCallingOrSelfPermission(r17);
            if (r14 != 0) goto L_0x0163;
        L_0x0118:
            r0 = r21;
            r0 = android.widget.QuickContactBadge.this;
            r16 = r0;
            r16 = r16.mContext;
            r17 = "activity";
            r4 = r16.getSystemService(r17);
            r4 = (android.app.ActivityManager) r4;
            r16 = 1;
            r0 = r16;
            r11 = r4.getRunningTasks(r0);
            r16 = 0;
            r0 = r16;
            r16 = r11.get(r0);
            r16 = (android.app.ActivityManager.RunningTaskInfo) r16;
            r0 = r16;
            r0 = r0.topActivity;
            r16 = r0;
            r2 = r16.getPackageName();
            r16 = com.sec.android.app.CscFeature.getInstance();
            r17 = "CscFeature_Contact_ReplacePackageAs";
            r3 = r16.getString(r17);
            r16 = "com.android.contacts";
            r0 = r16;
            r16 = r0.equals(r2);
            if (r16 != 0) goto L_0x0163;
        L_0x015a:
            r16 = android.text.TextUtils.isEmpty(r3);
            if (r16 != 0) goto L_0x0163;
        L_0x0160:
            r10.setPackage(r3);
        L_0x0163:
            if (r9 == 0) goto L_0x0170;
        L_0x0165:
            r16 = "uri_content";
            r0 = r16;
            r9.remove(r0);
            r10.putExtras(r9);
        L_0x0170:
            r0 = r21;
            r0 = android.widget.QuickContactBadge.this;	 Catch:{ ActivityNotFoundException -> 0x0181 }
            r16 = r0;
            r16 = r16.getContext();	 Catch:{ ActivityNotFoundException -> 0x0181 }
            r0 = r16;
            r0.startActivity(r10);	 Catch:{ ActivityNotFoundException -> 0x0181 }
            goto L_0x0066;
        L_0x0181:
            r8 = move-exception;
            r0 = r21;
            r0 = android.widget.QuickContactBadge.this;
            r16 = r0;
            r16 = r16.getContext();
            r17 = 17039979; // 0x104026b float:2.4246306E-38 double:8.418868E-317;
            r18 = 0;
            r16 = android.widget.Toast.makeText(r16, r17, r18);
            r16.show();
            goto L_0x0066;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.widget.QuickContactBadge.QueryHandler.onQueryComplete(int, java.lang.Object, android.database.Cursor):void");
        }
    }

    public QuickContactBadge(Context context) {
        this(context, null);
    }

    public QuickContactBadge(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickContactBadge(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public QuickContactBadge(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mExtras = null;
        this.mExcludeMimes = null;
        TypedArray styledAttributes = this.mContext.obtainStyledAttributes(R.styleable.Theme);
        this.mOverlay = styledAttributes.getDrawable(R.styleable.Theme_quickContactBadgeOverlay);
        styledAttributes.recycle();
        if (!isInEditMode()) {
            this.mQueryHandler = new QueryHandler(this.mContext.getContentResolver());
        }
        setOnClickListener(this);
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mOverlay != null && this.mOverlay.isStateful()) {
            this.mOverlay.setState(getDrawableState());
            invalidate();
        }
    }

    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (this.mOverlay != null) {
            this.mOverlay.setHotspot(x, y);
        }
    }

    public void setMode(int size) {
    }

    public void setPrioritizedMimeType(String prioritizedMimeType) {
        this.mPrioritizedMimeType = prioritizedMimeType;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isEnabled() && this.mOverlay != null && this.mOverlay.getIntrinsicWidth() != 0 && this.mOverlay.getIntrinsicHeight() != 0) {
            this.mOverlay.setBounds(0, 0, getWidth(), getHeight());
            if (this.mPaddingTop == 0 && this.mPaddingLeft == 0) {
                this.mOverlay.draw(canvas);
                return;
            }
            int saveCount = canvas.getSaveCount();
            canvas.save();
            canvas.translate((float) this.mPaddingLeft, (float) this.mPaddingTop);
            this.mOverlay.draw(canvas);
            canvas.restoreToCount(saveCount);
        }
    }

    private boolean isAssigned() {
        return (this.mContactUri == null && this.mContactEmail == null && this.mContactPhone == null) ? false : true;
    }

    public void setImageToDefault() {
        if (this.mDefaultAvatar == null) {
            this.mDefaultAvatar = this.mContext.getDrawable(R.drawable.ic_contact_picture);
        }
        setImageDrawable(this.mDefaultAvatar);
    }

    public void assignContactUri(Uri contactUri) {
        this.mContactUri = contactUri;
        this.mContactEmail = null;
        this.mContactPhone = null;
        onContactUriChanged();
    }

    public void assignContactFromEmail(String emailAddress, boolean lazyLookup) {
        assignContactFromEmail(emailAddress, lazyLookup, null);
    }

    public void assignContactFromEmail(String emailAddress, boolean lazyLookup, Bundle extras) {
        this.mContactEmail = emailAddress;
        this.mExtras = extras;
        if (lazyLookup || this.mQueryHandler == null) {
            this.mContactUri = null;
            onContactUriChanged();
            return;
        }
        this.mQueryHandler.startQuery(0, null, Uri.withAppendedPath(Email.CONTENT_LOOKUP_URI, Uri.encode(this.mContactEmail)), EMAIL_LOOKUP_PROJECTION, null, null, null);
    }

    public void assignContactFromPhone(String phoneNumber, boolean lazyLookup) {
        assignContactFromPhone(phoneNumber, lazyLookup, new Bundle());
    }

    public void assignContactFromPhone(String phoneNumber, boolean lazyLookup, Bundle extras) {
        this.mContactPhone = phoneNumber;
        this.mExtras = extras;
        if (lazyLookup || this.mQueryHandler == null) {
            this.mContactUri = null;
            onContactUriChanged();
            return;
        }
        this.mQueryHandler.startQuery(1, null, Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, this.mContactPhone), PHONE_LOOKUP_PROJECTION, null, null, null);
    }

    public void setOverlay(Drawable overlay) {
        this.mOverlay = overlay;
    }

    private void onContactUriChanged() {
        setEnabled(isAssigned());
    }

    public void onClick(View v) {
        Bundle extras = this.mExtras == null ? new Bundle() : this.mExtras;
        if (this.mContactUri != null) {
            QuickContact.showQuickContact(getContext(), this, this.mContactUri, this.mExcludeMimes, this.mPrioritizedMimeType);
        } else if (this.mContactEmail != null && this.mQueryHandler != null) {
            extras.putString(EXTRA_URI_CONTENT, this.mContactEmail);
            this.mQueryHandler.startQuery(2, extras, Uri.withAppendedPath(Email.CONTENT_LOOKUP_URI, Uri.encode(this.mContactEmail)), EMAIL_LOOKUP_PROJECTION, null, null, null);
        } else if (this.mContactPhone != null && this.mQueryHandler != null) {
            extras.putString(EXTRA_URI_CONTENT, this.mContactPhone);
            this.mQueryHandler.startQuery(3, extras, Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, this.mContactPhone), PHONE_LOOKUP_PROJECTION, null, null, null);
        }
    }

    public CharSequence getAccessibilityClassName() {
        return QuickContactBadge.class.getName();
    }

    public void setExcludeMimes(String[] excludeMimes) {
        this.mExcludeMimes = excludeMimes;
    }
}
