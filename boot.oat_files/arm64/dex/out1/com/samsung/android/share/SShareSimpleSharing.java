package com.samsung.android.share;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.HorizontalListView;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.R;
import com.samsung.android.coreapps.sdk.easysignup.EasySignUpManager;
import com.samsung.android.coreapps.sdk.easysignup.SimpleSharingManager;
import com.samsung.android.fingerprint.FingerprintManager;
import java.util.ArrayList;
import java.util.List;

public class SShareSimpleSharing {
    public static final String ACTION_REQ_AUTH = "com.samsung.android.coreapps.easysignup.ACTION_REQ_AUTH";
    private static final boolean DEBUG = false;
    private static final int DEV_TYPE_MOBILE = 1;
    private static final int DEV_TYPE_TABLET = 2;
    public static final String INTENT_ACTION_REQUESTSEND = "com.samsung.android.coreapps.rshare.action.REQUESTSEND";
    private static final String INTENT_REQUEST_RECENT_GROUP = "com.samsung.android.coreapps.rshare.requestrecentgroupcontacts";
    private static final float MAX_FONT_SCALE = 1.2f;
    private static final int MSG_INIT_RECENTLIST = 2000;
    private static final int MSG_LIST_UPDATE = 1000;
    private static final String QUICKCONNECT_PERMISSION = "com.samsung.android.sconnect.permission.REQUEST_DISCOVERY_SERIVCE";
    private static int RECENT_CONTACTS_LIST_MAX_COUNT = 5;
    private static final int RECENT_TYPE_CONTACT_GROUP = 2;
    private static final int RECENT_TYPE_CONTACT_PHOTO = 3;
    private static final int RECENT_TYPE_CONTACT_PRIV = 1;
    private static final int RECENT_TYPE_DEFAULT = 0;
    private static final int RECENT_TYPE_DEVICE_MOBILE = 4;
    private static final int RECENT_TYPE_DEVICE_TABLET = 5;
    private static final int REQUEST_CODE_REQUEST_RECENT_CONTACTS = 1;
    public static final int REQUEST_CODE_SIGNUP_REQ_AUTH = 1;
    public static final String SSHARING_CLASS_NAME = "com.samsung.android.coreapps.rshare.ui.RelayActivity";
    public static final String SSHARING_PACKAGE_NAME = "com.samsung.android.coreapps";
    private static final String TAG = "SShareSimpleSharing";
    public static final String TAG_RECIPIENT_DATAIDS = "recipientdataids";
    private static final String WIFIDIRECT_ACTIVITY_NAME = "com.sec.android.app.FileShareClient.DeviceSelectActivity";
    private static boolean mEasySignUpCertificated = false;
    private static boolean mIsRemoteShareServiceDownloaded = false;
    private static boolean mRemoteShareServiceEnabled = true;
    private static boolean mSSharingRecentContactExisted = false;
    private final char[] ELLIPSIS_NORMAL = new char[]{'…'};
    private final String EXTRA_KEY_RECENT_GROUP_CONTACTID = "recentgroupcontactids";
    private final String EXTRA_KEY_RECENT_GROUP_COUNT = "recentgroupcount";
    private final String EXTRA_KEY_RECENT_GROUP_DATAIDS = "recentgroupdataids";
    private final String EXTRA_KEY_RECENT_GROUP_NAMES = "recentgroupnames";
    private final String EXTRA_KEY_RECENT_GROUP_NAME_LIST = "recentgroupnamelist";
    private final String EXTRA_KEY_RECENT_GROUP_PHONENUM = "recentphonenumbers";
    private final String EXTRA_KEY_RECENT_GROUP_THUMBNAILS = "recentgroupthumbnail";
    private final int REMOTE_SHARE_SERVICE_ID = 2;
    private final String RESPONSE_RECENT_GROUP_CONTACTS = "com.samsung.android.coreapps.rshare.responserecentgroupcontacts";
    private float defaultTextSize;
    private Activity mActivity;
    private Context mContext;
    private List<Intent> mExtraIntentList;
    private SShareCommon mFeature;
    private HorizontalListView mGridRecentHistory;
    private boolean mGroupNameOldConcept;
    final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    SShareSimpleSharing.this.initRecentHistoryList();
                    return;
                default:
                    return;
            }
        }
    };
    private boolean mIsRecentContactsReceiverRegistered = false;
    private boolean mIsRemoteShareServiceDownloadedChecked = false;
    private boolean mListupCompleted = false;
    private Intent mOrigIntent;
    private long[] mRecentContactsId;
    private int[] mRecentContactsItemContactsCountInGroup;
    private int mRecentContactsListCount = 0;
    private ArrayList<String> mRecentContactsListName = new ArrayList();
    private List<byte[]> mRecentContactsListThumb = new ArrayList();
    BroadcastReceiver mRecentContactsReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("com.samsung.android.coreapps.rshare.responserecentgroupcontacts".equals(intent.getAction())) {
                SShareSimpleSharing.this.mRecentContactsListCount = 0;
                if (!(SShareSimpleSharing.this.mRecentContactsListThumb == null || SShareSimpleSharing.this.mRecentContactsListThumb.size() == 0)) {
                    SShareSimpleSharing.this.mRecentContactsListThumb.clear();
                }
                if (!(SShareSimpleSharing.this.mRecentContactsListName == null || SShareSimpleSharing.this.mRecentContactsListName.size() == 0)) {
                    SShareSimpleSharing.this.mRecentContactsListName.clear();
                }
                int i = 0;
                while (i < SShareSimpleSharing.RECENT_CONTACTS_LIST_MAX_COUNT && intent.hasExtra("recentgroupdataids" + i)) {
                    ArrayList<String> tmpGroupName = intent.getStringArrayListExtra("recentgroupnamelist" + i);
                    if (tmpGroupName != null) {
                        String tmpName = "";
                        for (int j = 0; j < tmpGroupName.size(); j++) {
                            tmpName = tmpName + ((String) tmpGroupName.get(j));
                            if (j != tmpGroupName.size() - 1) {
                                tmpName = tmpName + FingerprintManager.FINGER_PERMISSION_DELIMITER;
                            }
                        }
                        SShareSimpleSharing.this.mRecentContactsListName.add(tmpName);
                    } else {
                        SShareSimpleSharing.this.mGroupNameOldConcept = true;
                        SShareSimpleSharing.this.mRecentContactsListName.add(intent.getStringExtra("recentgroupnames" + i));
                    }
                    SShareSimpleSharing.this.mRecipientDataId[i] = intent.getStringExtra("recentgroupdataids" + i);
                    SShareSimpleSharing.this.mRecentContactsId[i] = intent.getLongExtra("recentgroupcontactids" + i, 0);
                    SShareSimpleSharing.this.mRecentContactsItemContactsCountInGroup[i] = intent.getIntExtra("recentgroupcount" + i, 0);
                    SShareSimpleSharing.this.mRecentContactsListThumb.add(intent.getByteArrayExtra("recentgroupthumbnail" + i));
                    if (SShareSimpleSharing.this.mRecentContactsListThumb.get(i) != null) {
                        SShareSimpleSharing.this.mRecentContactsListCount = SShareSimpleSharing.this.mRecentContactsListCount + 1;
                        i++;
                    } else {
                        SShareSimpleSharing.this.mRecentContactsListCount = SShareSimpleSharing.this.mRecentContactsListCount + 1;
                        i++;
                    }
                }
                SShareSimpleSharing.this.mHandler.sendEmptyMessage(2000);
            }
        }
    };
    private int mRecentHistoryIndex = 0;
    Intent mRecentHistoryIntent;
    private RecentHistoryListAdapter mRecentHistoryListAdapter;
    private String[] mRecipientDataId;
    private float mTunedMargin = 40.0f;

    private final class DisplayDeviceInfo {
        int devType = 0;
        CharSequence deviceId;
        CharSequence deviceName;
        CharSequence displayLabel;
        int iconType;
        int netType = 0;
        CharSequence number;

        DisplayDeviceInfo(CharSequence pInitText) {
            this.displayLabel = pInitText;
            this.deviceName = pInitText;
            this.deviceId = null;
            this.number = null;
            this.iconType = 0;
        }

        DisplayDeviceInfo(CharSequence pDeviceName, CharSequence pLabel, CharSequence pDeviceId, CharSequence pNumber, int pIconType, int network, int device) {
            this.displayLabel = pLabel;
            this.deviceName = pDeviceName;
            this.deviceId = pDeviceId;
            this.number = pNumber;
            this.iconType = pIconType;
            this.netType = network;
            this.devType = device;
        }
    }

    private final class RecentHistoryListAdapter extends BaseAdapter {
        private final LayoutInflater mInflater;
        private final Intent[] mInitialIntents;
        private final Intent mIntent;
        private List<RecentHistoryListInfo> mRecentHistoryList = new ArrayList();

        public RecentHistoryListAdapter(Context context, Intent intent, Intent[] initialIntents, int launchedFromUid) {
            this.mIntent = new Intent(intent);
            this.mIntent.setComponent(null);
            this.mInitialIntents = initialIntents;
            this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        }

        public int getCount() {
            return this.mRecentHistoryList.size();
        }

        public Object getItem(int position) {
            return this.mRecentHistoryList.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (SShareSimpleSharing.this.shouldShowRecentHistoryView()) {
                if (convertView == null || convertView.findViewById(R.id.text3) == null) {
                    view = this.mInflater.inflate((int) R.layout.tw_resolver_remote_share_list_item, parent, false);
                } else {
                    view = convertView;
                }
            } else if (convertView == null || convertView.findViewById(R.id.text3) != null) {
                view = this.mInflater.inflate((int) R.layout.tw_resolver_remote_share_recent_history_default_list_item, parent, false);
            } else {
                view = convertView;
            }
            if (SShareSimpleSharing.this.shouldShowRecentHistoryView()) {
                setItemView(view, (RecentHistoryListInfo) this.mRecentHistoryList.get(position));
            } else {
                setDefaultView(view, (RecentHistoryListInfo) this.mRecentHistoryList.get(position));
            }
            return view;
        }

        private final void setDefaultView(View view, RecentHistoryListInfo info) {
            float fontScale = SShareSimpleSharing.this.getFontScale();
            TextView text = (TextView) view.findViewById(R.id.text1);
            TextView textIconName = (TextView) view.findViewById(R.id.text2);
            SShareSimpleSharing.this.defaultTextSize = (float) SShareSimpleSharing.this.mContext.getResources().getDimensionPixelSize(R.dimen.tw_resolver_item_text_size);
            text.setTextSize(0, SShareSimpleSharing.this.defaultTextSize * fontScale);
            text.setText(info.displayLabel);
            textIconName.setTextSize(0, SShareSimpleSharing.this.defaultTextSize * fontScale);
            textIconName.setText(info.extraInfo);
            ImageView img = (ImageView) view.findViewById(R.id.tw_resolver_remote_share_recent_history_default_item_icon);
            img.setBackgroundResource(R.drawable.icon_background);
            img.setImageResource(R.drawable.ic_remote_share);
            GradientDrawable shapeDrawable = (GradientDrawable) img.getBackground();
            if (shapeDrawable != null) {
                shapeDrawable.setColor(SShareSimpleSharing.this.mContext.getResources().getColor(R.color.tw_remote_share_default_icon_color));
            }
        }

        private final void setItemView(View view, RecentHistoryListInfo info) {
            float fontScale = SShareSimpleSharing.this.getFontScale();
            TextView text = (TextView) view.findViewById(R.id.text1);
            TextView text2 = (TextView) view.findViewById(R.id.text2);
            TextView text3 = (TextView) view.findViewById(R.id.text3);
            if (info.extraInfo == null) {
                SShareSimpleSharing.this.defaultTextSize = (float) SShareSimpleSharing.this.mContext.getResources().getDimensionPixelSize(R.dimen.tw_resolver_item_text_size);
                text.setTextSize(0, SShareSimpleSharing.this.defaultTextSize * fontScale);
                text.setText(info.displayLabel);
                text.setVisibility(0);
                text2.setVisibility(8);
                text3.setVisibility(8);
            } else {
                SShareSimpleSharing.this.defaultTextSize = (float) SShareSimpleSharing.this.mContext.getResources().getDimensionPixelSize(R.dimen.tw_resolver_item_text_size_secondary);
                text2.setTextSize(0, SShareSimpleSharing.this.defaultTextSize * fontScale);
                text2.setText(info.displayLabel);
                SShareSimpleSharing.this.defaultTextSize = (float) SShareSimpleSharing.this.mContext.getResources().getDimensionPixelSize(R.dimen.tw_resolver_item_text_size);
                text3.setTextSize(0, SShareSimpleSharing.this.defaultTextSize * fontScale);
                text3.setText(info.extraInfo);
                text.setVisibility(8);
                text2.setVisibility(0);
                text3.setVisibility(0);
            }
            ImageView img = (ImageView) view.findViewById(R.id.tw_resolver_remote_share_item_icon);
            int bgColorId = R.color.tw_remote_share_default_icon_color;
            img.setBackgroundResource(R.drawable.icon_background);
            if (info.photoIcon != null || info.iconType == 3) {
                img.setImageDrawable(info.photoIcon);
                if (((GradientDrawable) img.getBackground()) != null) {
                    if (info.iconType == 3) {
                        bgColorId = R.color.tw_remote_share_contact_icon_text_color;
                    } else {
                        bgColorId = R.color.tw_remote_share_contact_icon_color;
                    }
                }
            } else {
                int bgIconId;
                switch (info.iconType) {
                    case 0:
                        bgIconId = R.drawable.ic_remote_share;
                        bgColorId = R.color.tw_remote_share_default_icon_color;
                        break;
                    case 1:
                        bgIconId = R.drawable.ic_remote_share_contacts_default_image_with_no_color;
                        bgColorId = R.color.tw_remote_share_contact_icon_color;
                        break;
                    case 2:
                        bgIconId = R.drawable.ic_remote_share_contacts_default_image_with_group;
                        bgColorId = R.color.tw_remote_share_contact_icon_color;
                        break;
                    case 4:
                        bgIconId = R.drawable.action_list_ic_mobile;
                        bgColorId = R.color.tw_remote_share_device_icon_color;
                        break;
                    case 5:
                        bgIconId = R.drawable.action_list_ic_tablet;
                        bgColorId = R.color.tw_remote_share_device_icon_color;
                        break;
                    default:
                        bgIconId = R.drawable.ic_remote_share_contacts_default_image_with_no_color;
                        bgColorId = R.color.tw_remote_share_contact_icon_color;
                        break;
                }
                img.setImageResource(bgIconId);
            }
            GradientDrawable shapeDrawable = (GradientDrawable) img.getBackground();
            if (shapeDrawable != null) {
                shapeDrawable.setColor(SShareSimpleSharing.this.mContext.getResources().getColor(bgColorId));
            }
        }

        public Intent getIntent() {
            return this.mIntent;
        }
    }

    private final class RecentHistoryListInfo {
        DisplayDeviceInfo deviceInfo;
        CharSequence displayLabel;
        CharSequence extraInfo;
        int iconType;
        Drawable photoIcon;

        RecentHistoryListInfo(CharSequence pInitText) {
            this.displayLabel = pInitText;
            this.extraInfo = null;
            this.iconType = 0;
            this.photoIcon = null;
            this.deviceInfo = null;
        }

        RecentHistoryListInfo(CharSequence pLabel, int pIconType, Drawable pPhoto, CharSequence pExtraInfo) {
            this.displayLabel = pLabel;
            this.iconType = pIconType;
            this.photoIcon = pPhoto;
            this.extraInfo = pExtraInfo;
            this.deviceInfo = null;
        }

        RecentHistoryListInfo(CharSequence pLabel, int pIconType, Drawable pPhoto, CharSequence pExtraInfo, DisplayDeviceInfo pDeviceInfo) {
            this.displayLabel = pLabel;
            this.iconType = pIconType;
            this.photoIcon = pPhoto;
            this.extraInfo = pExtraInfo;
            this.deviceInfo = pDeviceInfo;
        }
    }

    public SShareSimpleSharing(Activity activity, Context context, SShareCommon feature, Intent intent, int launchedFromUid, List<Intent> extraIntentList) {
        this.mActivity = activity;
        this.mContext = context;
        this.mFeature = feature;
        this.mOrigIntent = intent;
        this.mRecentHistoryListAdapter = new RecentHistoryListAdapter(context, intent, null, launchedFromUid);
        this.mRecipientDataId = new String[RECENT_CONTACTS_LIST_MAX_COUNT];
        this.mRecentContactsId = new long[RECENT_CONTACTS_LIST_MAX_COUNT];
        this.mRecentContactsItemContactsCountInGroup = new int[RECENT_CONTACTS_LIST_MAX_COUNT];
        this.mExtraIntentList = extraIntentList;
        checkEasySignUpCertificated();
        checkSSharingRecentContactExisted();
        checkRemoteShareServiceEnabled();
    }

    public RecentHistoryListAdapter getRecentHistoryListAdapter() {
        return this.mRecentHistoryListAdapter;
    }

    public void sendRequestRecentContactsHistoryList() {
        registerRecentContactsReceiver();
        this.mContext.sendBroadcast(new Intent(INTENT_REQUEST_RECENT_GROUP));
    }

    public void registerRecentContactsReceiver() {
        checkEasySignUpCertificated();
        checkSSharingRecentContactExisted();
        checkRemoteShareServiceEnabled();
        if (shouldShowRecentHistoryView() && mRemoteShareServiceEnabled && !this.mIsRecentContactsReceiverRegistered) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("com.samsung.android.coreapps.rshare.responserecentgroupcontacts");
            this.mContext.registerReceiver(this.mRecentContactsReceiver, filter);
            this.mIsRecentContactsReceiverRegistered = true;
        }
    }

    public void unregisterRecentContactsReceiver() {
        if (this.mIsRecentContactsReceiverRegistered) {
            this.mContext.unregisterReceiver(this.mRecentContactsReceiver);
            this.mIsRecentContactsReceiverRegistered = false;
        }
    }

    public boolean hasExtraIntentUriInfo() {
        if (this.mExtraIntentList != null) {
            for (int i = 0; i < this.mExtraIntentList.size(); i++) {
                Bundle extraBundle = ((Intent) this.mExtraIntentList.get(i)).getExtras();
                if (extraBundle != null && ((Uri) extraBundle.getParcelable("android.intent.extra.STREAM")) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setSimpleSharingView(HorizontalListView view, OnItemClickListener listener) {
        this.mGridRecentHistory = view;
        if (this.mGridRecentHistory != null) {
            this.mGridRecentHistory.setAdapter(this.mRecentHistoryListAdapter);
            this.mGridRecentHistory.setOnItemClickListener(listener);
        }
    }

    public void buildUpSimpleSharingData() {
        if (shouldShowRecentHistoryView()) {
            sendRequestRecentContactsHistoryList();
            this.mRecentHistoryListAdapter.notifyDataSetChanged();
            this.mHandler.sendEmptyMessage(2000);
            return;
        }
        initRecentHistoryDefault();
    }

    public boolean shouldShowRecentHistoryView() {
        return mEasySignUpCertificated && mSSharingRecentContactExisted;
    }

    public boolean isEasySignUpCertificated() {
        return mEasySignUpCertificated;
    }

    public boolean isRemoteShareServiceEnabled() {
        return mRemoteShareServiceEnabled;
    }

    public Intent getRecentHistoryIntent(int position) {
        Intent targetIntent = this.mRecentHistoryListAdapter.getIntent();
        this.mRecentHistoryIntent = new Intent(INTENT_ACTION_REQUESTSEND);
        this.mRecentHistoryIntent.addFlags(67108864);
        this.mRecentHistoryIntent.putExtra("android.intent.extra.INTENT", targetIntent);
        if (this.mExtraIntentList != null) {
            int nSize = this.mExtraIntentList.size();
            Intent[] initialIntents = new Intent[nSize];
            for (int i = 0; i < nSize; i++) {
                initialIntents[i] = (Intent) this.mExtraIntentList.get(i);
            }
            this.mRecentHistoryIntent.putExtra("android.intent.extra.INITIAL_INTENTS", initialIntents);
        }
        if (position > 0) {
            this.mRecentHistoryIntent.putExtra(TAG_RECIPIENT_DATAIDS, this.mRecipientDataId[position - 1]);
        }
        return this.mRecentHistoryIntent;
    }

    public void recentHistoryGridItemClick(int position) {
        if (getRecentHistoryListAdapter() != null) {
            if (this.mFeature.getSupportLogging()) {
                String detailInfo;
                SShareLogging sshareLogging = new SShareLogging(this.mContext, this.mOrigIntent);
                switch (getRecentHistoryInfo(position).iconType) {
                    case 2:
                        detailInfo = SShareLogging.SURVEY_DETAIL_FEATURE_CONTACTGROUP;
                        break;
                    default:
                        detailInfo = SShareLogging.SURVEY_DETAIL_FEATURE_CONTACTPRIV;
                        break;
                }
                sshareLogging.insertLog(SShareLogging.SURVEY_FEATURE_EASYSHARE, detailInfo);
            }
            try {
                this.mActivity.startActivity(getRecentHistoryIntent(position));
            } catch (ActivityNotFoundException e) {
                Log.e(TAG, "RecentHistoryGridItemClick : startActivity failed!!");
            }
        }
    }

    public void recentHistoryDefaultGridItemClick(int position) {
        switch (position) {
            case 0:
                Intent intent = new Intent(ACTION_REQ_AUTH);
                intent.addFlags(67108864);
                intent.putExtra("fromOOBE", false);
                intent.putExtra("agreeMarketing", false);
                intent.putExtra("AuthRequestFrom", "shareVia");
                try {
                    this.mActivity.startActivityForResult(intent, 1);
                    return;
                } catch (ActivityNotFoundException e) {
                    Log.e(TAG, "Easy signUp agent is not found");
                    return;
                }
            default:
                return;
        }
    }

    private void checkEasySignUpCertificated() {
        mEasySignUpCertificated = EasySignUpManager.isJoined(this.mContext, 2);
        Log.d(TAG, "isJoined=" + mEasySignUpCertificated);
    }

    private void checkSSharingRecentContactExisted() {
        mSSharingRecentContactExisted = SimpleSharingManager.isRecentContactExisted(this.mContext);
        Log.d(TAG, "isRecentContactExisted=" + mSSharingRecentContactExisted);
    }

    private void checkRemoteShareServiceEnabled() {
        if (mEasySignUpCertificated) {
            int retVal = EasySignUpManager.getServiceStatus(this.mContext, 2);
            Log.d(TAG, "ServiceStatus=" + retVal);
            if (retVal != 1) {
                mRemoteShareServiceEnabled = false;
                return;
            } else {
                mRemoteShareServiceEnabled = true;
                return;
            }
        }
        mRemoteShareServiceEnabled = true;
    }

    private void initRecentHistoryDefault() {
        this.mRecentHistoryListAdapter.mRecentHistoryList.add(this.mRecentHistoryIndex, new RecentHistoryListInfo(this.mContext.getResources().getText(R.string.tw_resolver_RemoteShare_RecentHistoryDefault), 0, null, this.mContext.getResources().getText(R.string.tw_resolver_RemoteShare_RecentHistory_RemoteShare)));
        this.mRecentHistoryIndex++;
        this.mRecentHistoryListAdapter.notifyDataSetChanged();
    }

    private void clearRecentHistoryList(boolean bClearAll) {
        if (bClearAll && this.mRecentHistoryListAdapter != null && this.mRecentHistoryListAdapter.mRecentHistoryList != null) {
            this.mRecentHistoryListAdapter.mRecentHistoryList.clear();
            this.mListupCompleted = false;
        }
    }

    private RecentHistoryListInfo getRecentHistoryInfo(int position) {
        return (RecentHistoryListInfo) this.mRecentHistoryListAdapter.getItem(position);
    }

    private void initRecentHistoryList() {
        if (this.mRecentHistoryListAdapter != null && this.mGridRecentHistory != null) {
            if (!(this.mRecentHistoryListAdapter == null || this.mRecentHistoryListAdapter.mRecentHistoryList == null)) {
                clearRecentHistoryList(true);
            }
            this.mRecentHistoryIndex = 0;
            if (this.mRecentContactsListCount >= 1) {
                float textWidth = (float) this.mContext.getResources().getDimensionPixelSize(R.dimen.tw_remote_share_item_text_width);
                float textLandWidth = (float) this.mContext.getResources().getDimensionPixelSize(R.dimen.tw_remote_share_item_text_width_land);
                float maxLineNum = (float) this.mContext.getResources().getInteger(R.integer.tw_resolver_pagemode_label_linenum);
                boolean bLandscape = this.mContext.getResources().getConfiguration().orientation == 2;
                this.mRecentHistoryListAdapter.mRecentHistoryList.add(this.mRecentHistoryIndex, new RecentHistoryListInfo(this.mContext.getResources().getText(R.string.tw_resolver_RemoteShare_RecentHistory_RemoteShare)));
                this.mRecentHistoryIndex++;
                float textSize = TypedValue.applyDimension(0, ((float) this.mContext.getResources().getDimensionPixelSize(R.dimen.tw_resolver_item_text_size)) * getFontScale(), this.mContext.getResources().getDisplayMetrics());
                int tempContactsIndex = this.mGroupNameOldConcept ? 0 : 0;
                while (tempContactsIndex < this.mRecentContactsListCount && tempContactsIndex < RECENT_CONTACTS_LIST_MAX_COUNT) {
                    CharSequence recentHistoryDisplayLabel;
                    Drawable photoIcon;
                    RecentHistoryListInfo recentHistoryItem;
                    CharSequence recentHistoryDisplayLabel2 = null;
                    String groupNameOrg = "";
                    String groupName = "";
                    if (this.mRecentContactsItemContactsCountInGroup[tempContactsIndex] > 1) {
                        float availNameW;
                        int i;
                        CharSequence nameStr = (CharSequence) this.mRecentContactsListName.get(tempContactsIndex);
                        CharSequence emptyStr = "";
                        String emptyGroupStr = "";
                        float totalW = 0.0f;
                        boolean needEllipsis = false;
                        TextPaint textPaint = new TextPaint();
                        textPaint.setTextSize(textSize);
                        textPaint.setTypeface(Typeface.create("sec-roboto-light", 0));
                        textPaint.setAntiAlias(true);
                        textPaint.setTextAlign(Align.CENTER);
                        textPaint.density = this.mContext.getResources().getDisplayMetrics().density;
                        if (bLandscape) {
                            availNameW = (textLandWidth * maxLineNum) - 0.0f;
                        } else {
                            availNameW = ((textWidth * maxLineNum) - 0.0f) - this.mTunedMargin;
                        }
                        if (TextUtils.ellipsize(nameStr, textPaint, availNameW, TruncateAt.END).toString().toLowerCase().endsWith("" + this.ELLIPSIS_NORMAL[0])) {
                            needEllipsis = true;
                        }
                        if (this.mGroupNameOldConcept) {
                            groupNameOrg = this.mContext.getString(R.string.tw_resolver_RemoteShare_RecentHistory_GroupName);
                            emptyGroupStr = String.format(groupNameOrg, new Object[]{emptyStr, Integer.valueOf(this.mRecentContactsItemContactsCountInGroup[tempContactsIndex] - 1)});
                        } else {
                            if (needEllipsis) {
                                groupNameOrg = "(" + this.mRecentContactsItemContactsCountInGroup[tempContactsIndex] + ")";
                            }
                            emptyGroupStr = groupNameOrg;
                        }
                        float[] othersW = new float[emptyGroupStr.length()];
                        for (i = 0; i < textPaint.getTextWidths(emptyGroupStr, othersW); i++) {
                            totalW += othersW[i];
                        }
                        if (bLandscape) {
                            availNameW = (textLandWidth * maxLineNum) - totalW;
                        } else if (needEllipsis) {
                            availNameW = ((textWidth * maxLineNum) - totalW) - this.mTunedMargin;
                        } else {
                            availNameW = (textWidth * maxLineNum) - totalW;
                        }
                        CharSequence ellipsizedNameStr = TextUtils.ellipsize(nameStr, textPaint, availNameW, TruncateAt.END);
                        if (bLandscape) {
                            if (this.mGroupNameOldConcept && ("" + this.ELLIPSIS_NORMAL[0]).equals(ellipsizedNameStr)) {
                                ellipsizedNameStr = nameStr.subSequence(0, 1).toString() + this.ELLIPSIS_NORMAL[0];
                            }
                            recentHistoryDisplayLabel = ellipsizedNameStr.toString() + groupNameOrg;
                        } else {
                            float[] nameW = new float[nameStr.toString().length()];
                            int arrayNum1 = textPaint.getTextWidths(nameStr.toString(), nameW);
                            float totalW1 = 0.0f;
                            float totalW2 = 0.0f;
                            int firstLineCount = 0;
                            String tmpStr = "";
                            String tmpStr2 = "";
                            i = 0;
                            while (i < arrayNum1) {
                                totalW1 += nameW[i];
                                if (totalW1 >= textWidth) {
                                    firstLineCount = i;
                                    break;
                                } else {
                                    tmpStr = tmpStr + nameStr.charAt(i);
                                    i++;
                                }
                            }
                            Object recentHistoryDisplayLabel3 = tmpStr;
                            if (firstLineCount > 0) {
                                float secondLineWidth;
                                if (needEllipsis) {
                                    secondLineWidth = (textWidth - totalW) - this.mTunedMargin;
                                } else {
                                    secondLineWidth = textWidth - totalW;
                                }
                                for (i = firstLineCount; i < arrayNum1; i++) {
                                    totalW2 += nameW[i];
                                    if (totalW2 >= secondLineWidth) {
                                        break;
                                    }
                                    tmpStr2 = tmpStr2 + nameStr.charAt(i);
                                }
                                if (needEllipsis) {
                                    tmpStr2 = tmpStr2 + this.ELLIPSIS_NORMAL[0] + groupNameOrg;
                                } else {
                                    tmpStr2 = tmpStr2 + groupNameOrg;
                                }
                                Object recentHistoryDisplayLabel22 = tmpStr2;
                            }
                        }
                    } else {
                        recentHistoryDisplayLabel = (CharSequence) this.mRecentContactsListName.get(tempContactsIndex);
                    }
                    int iconType = getRecentIconType((byte[]) this.mRecentContactsListThumb.get(tempContactsIndex), this.mRecentContactsItemContactsCountInGroup[tempContactsIndex], this.mRecentContactsId[tempContactsIndex], -1);
                    if (iconType == 3) {
                        photoIcon = makeContactPhotoImage((byte[]) this.mRecentContactsListThumb.get(tempContactsIndex));
                    } else {
                        if (!(iconType == 2 || TextUtils.isEmpty(recentHistoryDisplayLabel))) {
                            char c = recentHistoryDisplayLabel.charAt(0);
                            if (Character.isAlphabetic(c)) {
                                photoIcon = makeBitmapWithText(this.mContext, String.valueOf(c));
                            }
                        }
                        photoIcon = null;
                    }
                    if (this.mRecentContactsItemContactsCountInGroup[tempContactsIndex] > 1) {
                        recentHistoryItem = new RecentHistoryListInfo(recentHistoryDisplayLabel, iconType, photoIcon, recentHistoryDisplayLabel2);
                    } else {
                        recentHistoryItem = new RecentHistoryListInfo(recentHistoryDisplayLabel, iconType, photoIcon, null);
                    }
                    this.mRecentHistoryListAdapter.mRecentHistoryList.add(this.mRecentHistoryIndex, recentHistoryItem);
                    this.mRecentHistoryIndex++;
                    tempContactsIndex++;
                }
            } else if (this.mRecentContactsListCount == 0) {
                this.mRecentHistoryListAdapter.mRecentHistoryList.add(this.mRecentHistoryIndex, new RecentHistoryListInfo(this.mContext.getResources().getText(R.string.tw_resolver_RemoteShare_RecentHistory_RemoteShare)));
                this.mRecentHistoryIndex++;
            }
            this.mRecentHistoryListAdapter.notifyDataSetChanged();
            this.mListupCompleted = true;
        }
    }

    private int getRecentIconType(byte[] contactThumbArray, int contactCount, long contactId, int devType) {
        if (devType != -1) {
            if (devType == 1) {
                return 4;
            }
            if (devType == 2) {
                return 5;
            }
            return 4;
        } else if (contactThumbArray != null) {
            return 3;
        } else {
            if (contactCount > 1) {
                return 2;
            }
            return 1;
        }
    }

    private Drawable makeBitmapWithText(Context context, String text) {
        int iconSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.tw_remote_share_icon_size);
        float textSize = (float) this.mContext.getResources().getDimensionPixelSize(R.dimen.tw_remote_share_icon_text_size);
        Bitmap textBitmap = Bitmap.createBitmap(iconSize, iconSize, Config.ARGB_8888);
        Canvas canvas = new Canvas(textBitmap);
        Paint circlePaint = new Paint();
        circlePaint.setColor(this.mContext.getResources().getColor(R.color.tw_remote_share_contact_icon_color));
        circlePaint.setAntiAlias(true);
        canvas.drawCircle((float) (iconSize / 2), (float) (iconSize / 2), (float) (iconSize / 2), circlePaint);
        Paint textPaint = new Paint();
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);
        textPaint.setColor(this.mContext.getResources().getColor(R.color.tw_remote_share_contact_icon_text_color));
        textPaint.setTextAlign(Align.CENTER);
        textPaint.getTextBounds(text, 0, text.length(), new Rect());
        canvas.drawText(text, (float) (iconSize / 2), (float) ((iconSize * 3) / 4), textPaint);
        return new BitmapDrawable(this.mContext.getResources(), textBitmap);
    }

    private float getFontScale() {
        float fontScale = this.mContext.getResources().getConfiguration().fontScale;
        if (fontScale > MAX_FONT_SCALE) {
            return MAX_FONT_SCALE;
        }
        return fontScale;
    }

    private Drawable makeContactPhotoImage(byte[] contactsThumbnailByteArray) {
        if (contactsThumbnailByteArray == null) {
            return null;
        }
        Bitmap mask = BitmapFactory.decodeResource(this.mContext.getResources(), R.drawable.ic_remote_mask);
        Bitmap photo = Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(contactsThumbnailByteArray, 0, contactsThumbnailByteArray.length), mask.getWidth(), mask.getHeight(), true);
        Bitmap bm = Bitmap.createBitmap(photo.getWidth(), photo.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        canvas.drawBitmap(photo, 0.0f, 0.0f, null);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        canvas.drawBitmap(mask, 0.0f, 0.0f, paint);
        paint.setXfermode(null);
        return new BitmapDrawable(this.mContext.getResources(), bm);
    }
}
