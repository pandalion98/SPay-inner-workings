package com.samsung.android.dualscreen.app;

import android.app.ActivityManager;
import android.app.ActivityManagerNative;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManagerGlobal;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.R;
import com.android.internal.app.AlertActivity;
import com.android.internal.app.AlertController.AlertParams;
import com.samsung.android.dualscreen.DualScreen;
import com.samsung.android.multidisplay.dualscreen.DualScreenUtils;
import java.util.ArrayList;
import java.util.List;

public class DisplayChooserActivity extends AlertActivity implements OnItemClickListener {
    private static final boolean DEBUG = true;
    private static final String TAG = "DisplayChooserActivity";
    public static final String THEME_CHOOSER = "theme";
    public static final int THEME_DEVICE_DEFAULT = 1;
    public static final int THEME_DEVICE_DEFAULT_LIGHT = 2;
    public static final int THEME_NONE = 0;
    private DisplayListAdapter mAdapter;
    private GridView mGrid;
    private int mIconDpi;
    private int mIconSize;
    private int mLaunchedFromUid;
    private PackageManager mPm;

    private final class DisplayListAdapter extends BaseAdapter {
        private final ResolveInfo mBaseResolveInfo;
        private final int mCallerDisplayId;
        private final LayoutInflater mInflater;
        private final Intent mIntent;
        private final int mLaunchedFromUid;
        private List<DisplayResolveInfo> mList;

        public DisplayListAdapter(Context context, Intent intent, ResolveInfo rInfo, int launchedFromUid, int callerDisplayId) {
            this.mIntent = new Intent(intent);
            this.mIntent.setComponent(null);
            this.mBaseResolveInfo = rInfo;
            this.mLaunchedFromUid = launchedFromUid;
            this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
            this.mCallerDisplayId = callerDisplayId;
            rebuildList();
        }

        private void rebuildList() {
            boolean isActionMain;
            this.mList = new ArrayList();
            DisplayManagerGlobal dm = DisplayManagerGlobal.getInstance();
            if (this.mIntent.getAction() == "android.intent.action.MAIN") {
                isActionMain = true;
            } else {
                isActionMain = false;
            }
            for (int displayId : dm.getDisplayIds()) {
                if (isActionMain || DualScreenUtils.displayIdToScreen(displayId) != DualScreen.FULL) {
                    String displayName;
                    Display display = dm.getRealDisplay(displayId);
                    Log.d(DisplayChooserActivity.TAG, "rebuildList() : displayId=" + displayId + " : display=" + display);
                    if (display != null) {
                        displayName = display.getName();
                    } else {
                        displayName = new String("display[" + Integer.toString(displayId) + "]");
                    }
                    Log.d(DisplayChooserActivity.TAG, "rebuildList() : displayName=" + displayName);
                    switch (displayId) {
                        case 0:
                            displayName = new String("MAIN Screen");
                            break;
                        case 1:
                            displayName = new String("SUB Screen");
                            break;
                        case 2:
                            displayName = new String("VIRTUAL(Expanded) Screen");
                            break;
                        case 4:
                            displayName = new String("EXTERNAL Screen");
                            break;
                    }
                    if (displayName != null) {
                        this.mList.add(new DisplayResolveInfo(this.mBaseResolveInfo, displayName.subSequence(0, displayName.length()), displayId));
                    }
                }
            }
        }

        public ResolveInfo resolveInfoForPosition(int position) {
            if (this.mList == null) {
                return null;
            }
            return ((DisplayResolveInfo) this.mList.get(position)).ri;
        }

        public Intent intentForPosition(int position) {
            if (this.mList == null) {
                return null;
            }
            DisplayResolveInfo dri = (DisplayResolveInfo) this.mList.get(position);
            Intent intent = new Intent(this.mIntent);
            intent.addFlags(View.SCROLLBARS_OUTSIDE_INSET);
            Log.d(DisplayChooserActivity.TAG, "resolveInfoForPosition() : mCallerDisplayId=" + this.mCallerDisplayId + " targetDisplayId=" + dri.displayId);
            switch (dri.displayId) {
                case 1:
                    intent.getLaunchParams().setScreen(DualScreen.SUB);
                    break;
                case 2:
                    intent.getLaunchParams().setScreen(DualScreen.FULL);
                    break;
                case 4:
                    intent.getLaunchParams().setScreen(DualScreen.EXTERNAL);
                    break;
                default:
                    intent.getLaunchParams().setScreen(DualScreen.MAIN);
                    break;
            }
            if (this.mCallerDisplayId != dri.displayId) {
                intent.addFlags(268435456);
            }
            ActivityInfo ai = dri.ri.activityInfo;
            intent.setComponent(new ComponentName(ai.applicationInfo.packageName, ai.name));
            return intent;
        }

        public int getCount() {
            return this.mList != null ? this.mList.size() : 0;
        }

        public Object getItem(int position) {
            return Integer.valueOf(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = this.mInflater.inflate((int) R.layout.display_chooser_list_item, parent, false);
                LayoutParams lp = ((ImageView) view.findViewById(R.id.icon)).getLayoutParams();
                int access$000 = DisplayChooserActivity.this.mIconSize;
                lp.height = access$000;
                lp.width = access$000;
            } else {
                view = convertView;
            }
            bindView(view, (DisplayResolveInfo) this.mList.get(position));
            return view;
        }

        private final void bindView(View view, DisplayResolveInfo info) {
            ImageView icon = (ImageView) view.findViewById(R.id.icon);
            ((TextView) view.findViewById(R.id.text1)).setText(info.displayLabel);
            if (info.displayIcon == null) {
                info.displayIcon = DisplayChooserActivity.this.loadIconForResolveInfo(info.ri);
            }
            icon.setImageDrawable(info.displayIcon);
        }
    }

    private static final class DisplayResolveInfo {
        Drawable displayIcon;
        int displayId;
        CharSequence displayLabel;
        ResolveInfo ri;

        DisplayResolveInfo(ResolveInfo pri, CharSequence pLabel, int pDisplayId) {
            this.ri = pri;
            this.displayLabel = pLabel;
            this.displayId = pDisplayId;
        }
    }

    private Intent makeMyIntent() {
        Intent intent = new Intent(getIntent());
        intent.setFlags(intent.getFlags() & -8388609);
        return intent;
    }

    protected void onCreate(Bundle savedInstanceState) {
        onCreate(savedInstanceState, makeMyIntent(), getResources().getText(R.string.whichApplication), null, null, true);
    }

    protected void onCreate(Bundle savedInstanceState, Intent intent, CharSequence title, Intent[] initialIntents, List<ResolveInfo> list, boolean alwaysUseOption) {
        setTheme(R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        super.onCreate(savedInstanceState);
        try {
            this.mLaunchedFromUid = ActivityManagerNative.getDefault().getLaunchedFromUid(getActivityToken());
        } catch (RemoteException e) {
            this.mLaunchedFromUid = -1;
        }
        intent.setComponent(null);
        switch (intent.getIntExtra("theme", 0)) {
            case 0:
                setTheme(R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                break;
            case 1:
                setTheme(R.style.Theme_DeviceDefault_Dialog_Alert);
                break;
            case 2:
                setTheme(R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                break;
        }
        AlertParams ap = this.mAlertParams;
        ap.mTitle = title;
        ActivityManager am = (ActivityManager) getSystemService("activity");
        this.mIconDpi = am.getLauncherLargeIconDensity();
        this.mIconSize = am.getLauncherLargeIconSize();
        this.mPm = getPackageManager();
        ComponentName cn = (ComponentName) intent.getParcelableExtra("android.intent.extra.DUAL_SCREEN_ORIGINAL_TARGET_ACTIVITY");
        if (cn != null) {
            intent.removeExtra("android.intent.extra.DUAL_SCREEN_ORIGINAL_TARGET_ACTIVITY");
            intent.setComponent(cn);
        }
        int callerDisplayId = intent.getIntExtra("android.intent.extra.EXTRA_DUAL_SCREEN_CALLER_ACTIVITY_DISPLAY_ID", 0);
        intent.removeExtra("android.intent.extra.EXTRA_DUAL_SCREEN_CALLER_ACTIVITY_DISPLAY_ID");
        intent.getLaunchParams().setFromDisplayChooser(true);
        ResolveInfo rInfo = null;
        List<ResolveInfo> resolvedList = this.mPm.queryIntentActivities(intent, 65728);
        if (resolvedList != null) {
            rInfo = (ResolveInfo) resolvedList.get(0);
        }
        Log.d(TAG, "onCreate() : rInfo=" + rInfo);
        Log.d(TAG, "onCreate() : callerDisplayId=" + callerDisplayId);
        this.mAdapter = new DisplayListAdapter(this, intent, rInfo, this.mLaunchedFromUid, callerDisplayId);
        ap.mView = getLayoutInflater().inflate((int) R.layout.display_chooser_grid, null);
        this.mGrid = (GridView) ap.mView.findViewById(R.id.display_chooser_grid);
        this.mGrid.setAdapter(this.mAdapter);
        this.mGrid.setOnItemClickListener(this);
        this.mGrid.setNumColumns(2);
        setupAlert();
    }

    Drawable getIcon(Resources res, int resId) {
        try {
            return res.getDrawableForDensity(resId, this.mIconDpi);
        } catch (NotFoundException e) {
            return null;
        }
    }

    Drawable loadIconForResolveInfo(ResolveInfo ri) {
        try {
            ComponentInfo ci = ri.activityInfo;
            if (ci == null) {
                return null;
            }
            Drawable dr = this.mPm.getCSCPackageItemIcon(ci.icon != 0 ? ci.name : ci.packageName);
            if (dr != null) {
                return dr;
            }
            if (!(ri.resolvePackageName == null || ri.icon == 0)) {
                dr = getIcon(this.mPm.getResourcesForApplication(ri.resolvePackageName), ri.icon);
                if (dr != null) {
                    return dr;
                }
            }
            int iconRes = ri.getIconResource();
            if (iconRes != 0) {
                dr = getIcon(this.mPm.getResourcesForApplication(ci.packageName), iconRes);
                if (dr != null) {
                    return dr;
                }
            }
            return ri.loadIcon(this.mPm);
        } catch (NameNotFoundException e) {
            Log.e(TAG, "Couldn't find resources for package", e);
        }
    }

    protected void onStop() {
        super.onStop();
        if ((getIntent().getFlags() & 268435456) != 0 && !isChangingConfigurations()) {
            finish();
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (this.mGrid.getCheckedItemPosition() != -1) {
        }
        startSelected(position);
    }

    void startSelected(int which) {
        onIntentSelected(this.mAdapter.resolveInfoForPosition(which), this.mAdapter.intentForPosition(which));
        finish();
    }

    protected void onIntentSelected(ResolveInfo ri, Intent intent) {
        if (intent != null) {
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Log.w(TAG, "Activity Not Found");
            }
        }
    }
}
