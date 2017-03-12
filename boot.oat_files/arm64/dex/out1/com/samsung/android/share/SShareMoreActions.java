package com.samsung.android.share;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.UserHandle;
import android.provider.Settings$System;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.R;
import java.util.ArrayList;

public class SShareMoreActions {
    private static final int ACTION_CHANGE_PLAYER = 101;
    private static final int ACTION_MOBILE_PRINT = 105;
    private static final int ACTION_QUICK_CONNECT = 104;
    private static final int ACTION_SCREEN_MIRRORING = 102;
    private static final int ACTION_SCREEN_SHARING = 103;
    private static final boolean DEBUG = false;
    private static final int DISABLE = 0;
    private static final int ENABLE = 1;
    private static final String INTENT_CHANGE_PLAYER = "android.intent.action.CHANGE_PLAYER_VIA_EASY_SHARE";
    private static final String INTENT_MOBILE_PRINT = "android.intent.action.MOBILE_PRINT_VIA_EASY_SHARE";
    private static final int LIMITED_ENABLE = 2;
    private static final float MAX_FONT_SCALE = 1.2f;
    private static final String MORE_ACTIONS_KNOX_STATE = "more_actions_knox_state";
    private static final String MORE_ACTIONS_PACKAGE_NAME = "more_actions_package_name";
    private static final String MORE_ACTIONS_SCREEN_SHARING_MODE = "more_actions_screen_sharing_mode";
    private static final String QUICK_CONNECT_ACTION = "com.samsung.android.qconnect.action.DEVICE_PICKER";
    private static final String QUICK_CONNECT_EXTRA_HEIGHT = "extra_height";
    private static final String QUICK_CONNECT_PKG = "com.samsung.android.qconnect";
    private static final String SCREEN_MIRRORING_CLASS = "com.samsung.wfd.LAUNCH_WFD_PICKER_DLG";
    private static final String SCREEN_MIRRORING_EXTRA_DIALOG_ONCE = "show_dialog_once";
    private static final String SCREEN_MIRRORING_EXTRA_TAG_WRITE = "tag_write_if_success";
    private static final String TAG = "SShareMoreActions";
    private ArrayList<ActionItem> arItem;
    private boolean bottomPanelExpaned = false;
    private float defaultTextSize;
    private Activity mActivity;
    private BottomPanelAdapter mBottomAdapter;
    private ViewGroup mBottomPanel;
    private Context mContext;
    private boolean mEnabledShowBtnBg = false;
    private SShareCommon mFeature;
    private GridView mGridMoreActions;
    private String mLaunchedFromPackage;
    private int mSharePanelVisibleHeight = 0;
    private Window mWindow;
    private WifiManager wifiManager = null;

    class ActionItem {
        int icon;
        int id;
        String name;

        ActionItem(int pID, int pIcon, String pName) {
            this.id = pID;
            this.icon = pIcon;
            this.name = pName;
        }
    }

    private final class BottomPanelAdapter extends BaseAdapter {
        ArrayList<ActionItem> arSrc;
        Context context;
        private final LayoutInflater inflater = ((LayoutInflater) this.context.getSystemService("layout_inflater"));
        int layout;

        public BottomPanelAdapter(Context pContext, int pLayout, ArrayList<ActionItem> pArSrc) {
            this.context = pContext;
            this.arSrc = pArSrc;
            this.layout = pLayout;
        }

        public int getCount() {
            return this.arSrc.size();
        }

        public String getItem(int position) {
            return ((ActionItem) this.arSrc.get(position)).name;
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public int getItemActionId(int position) {
            return ((ActionItem) this.arSrc.get(position)).id;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            int pos = position;
            if (convertView == null) {
                convertView = this.inflater.inflate(this.layout, parent, false);
            }
            float fontScale = SShareMoreActions.this.mContext.getResources().getConfiguration().fontScale;
            if (fontScale > SShareMoreActions.MAX_FONT_SCALE) {
                fontScale = SShareMoreActions.MAX_FONT_SCALE;
            }
            if (SShareMoreActions.this.mEnabledShowBtnBg) {
                convertView.setBackgroundResource(R.drawable.tw_resolver_bottompanel_btn_show_background_material_light);
            }
            ((ImageView) convertView.findViewById(R.id.icon)).setImageResource(((ActionItem) this.arSrc.get(position)).icon);
            TextView txt = (TextView) convertView.findViewById(R.id.text);
            SShareMoreActions.this.defaultTextSize = SShareMoreActions.this.mContext.getResources().getDimension(R.dimen.tw_resolver_item_text_size_secondary);
            txt.setTextSize(0, SShareMoreActions.this.defaultTextSize * fontScale);
            txt.setText(((ActionItem) this.arSrc.get(position)).name);
            return convertView;
        }
    }

    public SShareMoreActions(Activity activity, Context context, SShareCommon feature, Window window, String referrer) {
        this.mActivity = activity;
        this.mContext = context;
        this.mWindow = window;
        this.mFeature = feature;
        this.mLaunchedFromPackage = referrer;
        ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver != null && Settings$System.getInt(contentResolver, Settings$System.SHOW_BUTTON_BACKGROUND, 0) == 1) {
            this.mEnabledShowBtnBg = true;
        }
    }

    public void setMoreActionsView(ViewGroup view, OnItemClickListener listener) {
        if (view != null) {
            boolean isAPConnected = checkAPConnection();
            boolean isScreenMirroringRunning = checkScreenMirroringRunning();
            Log.d(TAG, "isAPConnected = " + isAPConnected + " isScreenMirroringRunning = " + isScreenMirroringRunning);
            this.mBottomPanel = view;
            this.mBottomPanel.setVisibility(0);
            this.arItem = new ArrayList();
            if (this.mFeature.getChangePlayerEnable() == 1 && isAPConnected) {
                this.arItem.add(new ActionItem(101, R.drawable.ic_more_actions_change_player, this.mContext.getString(R.string.tw_resolver_change_player)));
            }
            if (this.mFeature.getScreenMirroringEnable() == 1) {
                this.arItem.add(new ActionItem(102, R.drawable.ic_more_actions_screen_mirroring, this.mContext.getString(R.string.tw_resolver_screen_mirroring)));
            }
            if ((this.mFeature.getScreenSharingEnable() == 1 || this.mFeature.getScreenSharingEnable() == 2) && !isScreenMirroringRunning) {
                this.arItem.add(new ActionItem(103, R.drawable.screenmirroring_shareport_screensharing_ic, SShareLogging.SURVEY_DETAIL_FEATURE_SCREEN_SHARING));
            }
            if (this.mFeature.getQuickConnectEnable() == 1) {
                this.arItem.add(new ActionItem(104, R.drawable.quickconnect_shareport_nearbysharing_ic, this.mContext.getString(R.string.tw_resolver_nearby_sharing)));
            }
            if (this.mFeature.getPrintEnable() == 1) {
                this.arItem.add(new ActionItem(105, R.drawable.ic_more_actions_print, this.mContext.getString(R.string.tw_resolver_print)));
            }
            this.mBottomAdapter = new BottomPanelAdapter(this.mContext, R.layout.tw_resolver_bottom_panel_item, this.arItem);
            this.mGridMoreActions = (GridView) this.mWindow.findViewById(R.id.more_actions_list_no_profile);
            if (this.mGridMoreActions != null && this.arItem.size() > 0) {
                this.mGridMoreActions.setVisibility(0);
                this.mGridMoreActions.setAdapter(this.mBottomAdapter);
                this.mGridMoreActions.setOnItemClickListener(listener);
                if (this.arItem.size() == 4) {
                    this.mGridMoreActions.setNumColumns(4);
                } else {
                    this.mGridMoreActions.setNumColumns(3);
                }
            }
        }
    }

    public void startMoreActions(int position, Intent origIntent) {
        int actionID = this.mBottomAdapter.getItemActionId(position);
        if (this.mFeature.getSupportLogging()) {
            String detailInfo;
            SShareLogging sshareLogging = new SShareLogging(this.mContext, origIntent);
            switch (actionID) {
                case 101:
                    detailInfo = SShareLogging.SURVEY_DETAIL_FEATURE_CHANGEPLAYER;
                    break;
                case 102:
                    detailInfo = SShareLogging.SURVEY_DETAIL_FEATURE_MIRRORING;
                    break;
                case 103:
                    detailInfo = SShareLogging.SURVEY_DETAIL_FEATURE_SCREEN_SHARING;
                    break;
                case 104:
                    detailInfo = SShareLogging.SURVEY_DETAIL_FEATURE_NEARBY_SHARING;
                    break;
                case 105:
                    detailInfo = SShareLogging.SURVEY_DETAIL_FEATURE_PRINT;
                    break;
                default:
                    detailInfo = "Wrong ID";
                    break;
            }
            sshareLogging.insertLog(SShareLogging.SURVEY_FEATURE_MOREACTION, detailInfo);
        }
        Intent intent;
        switch (actionID) {
            case 101:
                try {
                    intent = new Intent(INTENT_CHANGE_PLAYER);
                    intent.putExtra(MORE_ACTIONS_PACKAGE_NAME, this.mLaunchedFromPackage);
                    this.mContext.sendBroadcast(intent);
                    return;
                } catch (ActivityNotFoundException e) {
                    Log.w(TAG, "MoreActions : ActivityNotFoundException !!! ");
                    return;
                } catch (Exception e2) {
                    Log.w(TAG, "MoreActions : Exception !!!");
                    e2.printStackTrace();
                    return;
                }
            case 102:
            case 103:
                intent = new Intent(SCREEN_MIRRORING_CLASS);
                intent.putExtra(SCREEN_MIRRORING_EXTRA_DIALOG_ONCE, true);
                intent.putExtra(SCREEN_MIRRORING_EXTRA_TAG_WRITE, false);
                intent.putExtra(MORE_ACTIONS_PACKAGE_NAME, this.mLaunchedFromPackage);
                intent.putExtra(MORE_ACTIONS_SCREEN_SHARING_MODE, this.mFeature.getScreenSharingEnable());
                intent.putExtra(MORE_ACTIONS_KNOX_STATE, isKnoxMode());
                intent.addFlags(343932928);
                this.mActivity.startActivityAsUser(intent, UserHandle.CURRENT);
                return;
            case 104:
                intent = new Intent(origIntent);
                intent.setPackage(QUICK_CONNECT_PKG);
                intent.setAction(QUICK_CONNECT_ACTION);
                intent.putExtra(QUICK_CONNECT_EXTRA_HEIGHT, getSharePanelVisibieHeight());
                this.mActivity.startActivityAsUser(intent, UserHandle.CURRENT);
                return;
            case 105:
                Uri stream = (Uri) origIntent.getParcelableExtra("android.intent.extra.STREAM");
                intent = new Intent(INTENT_MOBILE_PRINT);
                intent.putExtra(MORE_ACTIONS_PACKAGE_NAME, this.mLaunchedFromPackage);
                intent.putExtra("android.intent.extra.STREAM", stream);
                this.mContext.sendBroadcast(intent);
                return;
            default:
                return;
        }
    }

    private boolean isKnoxMode() {
        int userId = UserHandle.myUserId();
        return userId >= 100 && userId <= 200;
    }

    private boolean checkAPConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        NetworkInfo ni_wifi = cm.getNetworkInfo(1);
        NetworkInfo ni_wifiDirect = cm.getNetworkInfo(13);
        if (ni_wifi != null && ni_wifi.getDetailedState() == DetailedState.CONNECTED) {
            return true;
        }
        if (ni_wifiDirect == null || ni_wifiDirect.getDetailedState() != DetailedState.CONNECTED) {
            return false;
        }
        return true;
    }

    private boolean checkScreenMirroringRunning() {
        DisplayManager dm = (DisplayManager) this.mContext.getSystemService("display");
        if (dm.isDLNADeviceConnected() || dm.getWifiDisplayStatus().getActiveDisplayState() == 2) {
            return true;
        }
        return false;
    }

    public void setSharePanelVisibleHeight(int pHeight) {
        this.mSharePanelVisibleHeight = pHeight;
    }

    private int getSharePanelVisibieHeight() {
        return this.mSharePanelVisibleHeight;
    }
}
