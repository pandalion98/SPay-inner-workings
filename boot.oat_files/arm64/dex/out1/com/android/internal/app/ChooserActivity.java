package com.android.internal.app;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PersonaState;
import android.content.pm.ResolveInfo;
import android.content.pm.UserInfo;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.os.PersonaManager;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings$System;
import android.service.chooser.ChooserTarget;
import android.service.chooser.ChooserTargetService;
import android.service.chooser.IChooserTargetResult;
import android.service.chooser.IChooserTargetResult.Stub;
import android.service.chooser.IChooserTargetService;
import android.text.TextUtils;
import android.util.FloatProperty;
import android.util.Log;
import android.util.Slog;
import android.util.TimedRemoteCaller;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.android.ims.ImsConferenceState;
import com.android.internal.R;
import com.android.internal.app.ResolverActivity.TargetInfo;
import com.android.internal.logging.MetricsLogger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChooserActivity extends ResolverActivity {
    private static final int CHOOSER_TARGET_SERVICE_RESULT = 1;
    private static final int CHOOSER_TARGET_SERVICE_WATCHDOG_TIMEOUT = 2;
    private static final boolean DEBUG = false;
    private static final String EXTRA_CHOOSER_DROPLIST = "extra_chooser_droplist";
    private static final int QUERY_TARGET_SERVICE_LIMIT = 5;
    private static final String TAG = "ChooserActivity";
    private static final int WATCHDOG_TIMEOUT_MILLIS = 5000;
    private final Handler mChooserHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (!ChooserActivity.this.isDestroyed()) {
                        ServiceResultInfo sri = msg.obj;
                        if (ChooserActivity.this.mServiceConnections.contains(sri.connection)) {
                            if (sri.resultTargets != null) {
                                ChooserActivity.this.mChooserListAdapter.addServiceResults(sri.originalTarget, sri.resultTargets);
                            }
                            ChooserActivity.this.unbindService(sri.connection);
                            sri.connection.destroy();
                            ChooserActivity.this.mServiceConnections.remove(sri.connection);
                            if (ChooserActivity.this.mServiceConnections.isEmpty()) {
                                ChooserActivity.this.mChooserHandler.removeMessages(2);
                                ChooserActivity.this.sendVoiceChoicesIfNeeded();
                                return;
                            }
                            return;
                        }
                        Log.w(ChooserActivity.TAG, "ChooserTargetServiceConnection " + sri.connection + " returned after being removed from active connections." + " Have you considered returning results faster?");
                        return;
                    }
                    return;
                case 2:
                    ChooserActivity.this.unbindRemainingServices();
                    ChooserActivity.this.sendVoiceChoicesIfNeeded();
                    return;
                default:
                    super.handleMessage(msg);
                    return;
            }
        }
    };
    private ChooserListAdapter mChooserListAdapter;
    private ChooserRowAdapter mChooserRowAdapter;
    private IntentSender mChosenComponentSender;
    private Intent mReferrerFillInIntent;
    private IntentSender mRefinementIntentSender;
    private RefinementResultReceiver mRefinementResultReceiver;
    private Bundle mReplacementExtras;
    private final List<ChooserTargetServiceConnection> mServiceConnections = new ArrayList();

    static class BaseChooserTargetComparator implements Comparator<ChooserTarget> {
        BaseChooserTargetComparator() {
        }

        public int compare(ChooserTarget lhs, ChooserTarget rhs) {
            return (int) Math.signum(rhs.getScore() - lhs.getScore());
        }
    }

    public class ChooserListAdapter extends ResolveListAdapter {
        private static final int MAX_SERVICE_TARGETS = 4;
        public static final int TARGET_BAD = -1;
        public static final int TARGET_CALLER = 0;
        public static final int TARGET_SERVICE = 1;
        public static final int TARGET_STANDARD = 2;
        private final BaseChooserTargetComparator mBaseTargetComparator = new BaseChooserTargetComparator();
        private final List<TargetInfo> mCallerTargets = new ArrayList();
        private float mLateFee = 1.0f;
        private final List<ChooserTargetInfo> mServiceTargets = new ArrayList();

        public /* bridge */ /* synthetic */ DisplayResolveInfo getDisplayInfoAt(int x0) {
            return super.getDisplayInfoAt(x0);
        }

        public /* bridge */ /* synthetic */ int getDisplayInfoCount() {
            return super.getDisplayInfoCount();
        }

        public /* bridge */ /* synthetic */ DisplayResolveInfo getFilteredItem() {
            return super.getFilteredItem();
        }

        public /* bridge */ /* synthetic */ int getFilteredPosition() {
            return super.getFilteredPosition();
        }

        public /* bridge */ /* synthetic */ long getItemId(int x0) {
            return super.getItemId(x0);
        }

        public /* bridge */ /* synthetic */ DisplayResolveInfo getOtherProfile() {
            return super.getOtherProfile();
        }

        public /* bridge */ /* synthetic */ float getScore(DisplayResolveInfo x0) {
            return super.getScore(x0);
        }

        public /* bridge */ /* synthetic */ void handlePackagesChanged() {
            super.handlePackagesChanged();
        }

        public /* bridge */ /* synthetic */ boolean hasExtendedInfo() {
            return super.hasExtendedInfo();
        }

        public /* bridge */ /* synthetic */ boolean hasFilteredItem() {
            return super.hasFilteredItem();
        }

        public /* bridge */ /* synthetic */ boolean hasResolvedTarget(ResolveInfo x0) {
            return super.hasResolvedTarget(x0);
        }

        public /* bridge */ /* synthetic */ ResolveInfo resolveInfoForPosition(int x0, boolean x1) {
            return super.resolveInfoForPosition(x0, x1);
        }

        public ChooserListAdapter(Context context, List<Intent> payloadIntents, Intent[] initialIntents, List<ResolveInfo> rList, int launchedFromUid, boolean filterLastUsed) {
            super(context, payloadIntents, null, rList, launchedFromUid, filterLastUsed);
            if (initialIntents != null) {
                PackageManager pm = ChooserActivity.this.getPackageManager();
                if (!(ChooserActivity.this.mExtraIntentList == null || ChooserActivity.this.mExtraIntentList.size() == 0)) {
                    ChooserActivity.this.mExtraIntentList.clear();
                }
                for (Intent ii : initialIntents) {
                    if (ii != null) {
                        ActivityInfo ai = ii.resolveActivityInfo(pm, 0);
                        if (ai == null) {
                            Log.w(ChooserActivity.TAG, "No activity found for " + ii);
                        } else {
                            if (ResolverActivity.MIME_TYPE_MEMO.equals(ii.getType()) && ChooserActivity.this.mExtraIntentList != null) {
                                Log.w(ChooserActivity.TAG, "mExtraIntentList added: intent=" + ii);
                                ChooserActivity.this.mExtraIntentList.add(ii);
                            }
                            ResolveInfo ri = new ResolveInfo();
                            ri.activityInfo = ai;
                            UserManager userManager = (UserManager) ChooserActivity.this.getSystemService(ImsConferenceState.USER);
                            if (ii instanceof LabeledIntent) {
                                LabeledIntent li = (LabeledIntent) ii;
                                ri.resolvePackageName = li.getSourcePackage();
                                ri.labelRes = li.getLabelResource();
                                ri.nonLocalizedLabel = li.getNonLocalizedLabel();
                                ri.icon = li.getIconResource();
                                ri.iconResourceId = ri.icon;
                            }
                            if (userManager.isManagedProfile()) {
                                ri.noResourceId = true;
                                ri.icon = 0;
                            }
                            this.mCallerTargets.add(new DisplayResolveInfo(ii, ri, ri.loadLabel(pm), null, ii));
                        }
                    }
                }
            }
        }

        public boolean showsExtendedInfo(TargetInfo info) {
            return false;
        }

        public View onCreateView(ViewGroup parent) {
            return this.mInflater.inflate((int) R.layout.resolve_grid_item, parent, false);
        }

        public void onListRebuilt() {
            if (this.mServiceTargets != null) {
                pruneServiceTargets();
            }
        }

        public boolean shouldGetResolvedFilter() {
            return true;
        }

        public int getCount() {
            return (super.getCount() + getServiceTargetCount()) + getCallerTargetCount();
        }

        public int getUnfilteredCount() {
            return (super.getUnfilteredCount() + getServiceTargetCount()) + getCallerTargetCount();
        }

        public int getCallerTargetCount() {
            return this.mCallerTargets.size();
        }

        public int getServiceTargetCount() {
            return Math.min(this.mServiceTargets.size(), 4);
        }

        public int getStandardTargetCount() {
            return super.getCount();
        }

        public int getPositionTargetType(int position) {
            int callerTargetCount = getCallerTargetCount();
            if (position < callerTargetCount) {
                return 0;
            }
            int offset = 0 + callerTargetCount;
            int serviceTargetCount = getServiceTargetCount();
            if (position - offset < serviceTargetCount) {
                return 1;
            }
            if (position - (offset + serviceTargetCount) < super.getCount()) {
                return 2;
            }
            return -1;
        }

        public TargetInfo getItem(int position) {
            return targetInfoForPosition(position, true);
        }

        public TargetInfo targetInfoForPosition(int position, boolean filtered) {
            int callerTargetCount = getCallerTargetCount();
            if (position < callerTargetCount) {
                return (TargetInfo) this.mCallerTargets.get(position);
            }
            int offset = 0 + callerTargetCount;
            int serviceTargetCount = getServiceTargetCount();
            if (position - offset < serviceTargetCount) {
                return (TargetInfo) this.mServiceTargets.get(position - offset);
            }
            offset += serviceTargetCount;
            return filtered ? super.getItem(position - offset) : getDisplayInfoAt(position - offset);
        }

        public void addServiceResults(DisplayResolveInfo origTarget, List<ChooserTarget> targets) {
            float parentScore = getScore(origTarget);
            Collections.sort(targets, this.mBaseTargetComparator);
            float lastScore = 0.0f;
            int N = targets.size();
            for (int i = 0; i < N; i++) {
                ChooserTarget target = (ChooserTarget) targets.get(i);
                float targetScore = (target.getScore() * parentScore) * this.mLateFee;
                if (i > 0 && targetScore >= lastScore) {
                    targetScore = lastScore * 0.95f;
                }
                insertServiceTarget(new ChooserTargetInfo(origTarget, target, targetScore));
                lastScore = targetScore;
            }
            this.mLateFee *= 0.95f;
            if (ChooserActivity.this.getPagerAdapter() != null) {
                ChooserActivity.this.updatePagerAdapter(ChooserActivity.this.mChooserListAdapter.getCount());
            } else {
                notifyDataSetChanged();
            }
        }

        private void insertServiceTarget(ChooserTargetInfo chooserTargetInfo) {
            float newScore = chooserTargetInfo.getModifiedScore();
            int N = this.mServiceTargets.size();
            for (int i = 0; i < N; i++) {
                if (newScore > ((ChooserTargetInfo) this.mServiceTargets.get(i)).getModifiedScore()) {
                    this.mServiceTargets.add(i, chooserTargetInfo);
                    return;
                }
            }
            this.mServiceTargets.add(chooserTargetInfo);
        }

        private void pruneServiceTargets() {
            for (int i = this.mServiceTargets.size() - 1; i >= 0; i--) {
                if (!hasResolvedTarget(((ChooserTargetInfo) this.mServiceTargets.get(i)).getResolveInfo())) {
                    this.mServiceTargets.remove(i);
                }
            }
        }
    }

    class ChooserRowAdapter extends BaseAdapter {
        private ChooserListAdapter mChooserListAdapter;
        private final int mColumnCount = 4;
        private final Interpolator mInterpolator;
        private final LayoutInflater mLayoutInflater;
        private RowScale[] mServiceTargetScale;

        public ChooserRowAdapter(ChooserListAdapter wrappedAdapter) {
            this.mChooserListAdapter = wrappedAdapter;
            this.mLayoutInflater = LayoutInflater.from(ChooserActivity.this);
            this.mInterpolator = AnimationUtils.loadInterpolator(ChooserActivity.this, R.interpolator.decelerate_quint);
            wrappedAdapter.registerDataSetObserver(new DataSetObserver(ChooserActivity.this) {
                public void onChanged() {
                    super.onChanged();
                    int rcount = ChooserRowAdapter.this.getServiceTargetRowCount();
                    if (ChooserRowAdapter.this.mServiceTargetScale == null || ChooserRowAdapter.this.mServiceTargetScale.length != rcount) {
                        int oldRCount;
                        int i;
                        RowScale[] old = ChooserRowAdapter.this.mServiceTargetScale;
                        if (old != null) {
                            oldRCount = old.length;
                        } else {
                            oldRCount = 0;
                        }
                        ChooserRowAdapter.this.mServiceTargetScale = new RowScale[rcount];
                        if (old != null && rcount > 0) {
                            System.arraycopy(old, 0, ChooserRowAdapter.this.mServiceTargetScale, 0, Math.min(old.length, rcount));
                        }
                        for (i = rcount; i < oldRCount; i++) {
                            old[i].cancelAnimation();
                        }
                        for (i = oldRCount; i < rcount; i++) {
                            ChooserRowAdapter.this.mServiceTargetScale[i] = new RowScale(ChooserRowAdapter.this, 0.0f, 1.0f).setInterpolator(ChooserRowAdapter.this.mInterpolator);
                        }
                        for (i = oldRCount; i < rcount; i++) {
                            ChooserRowAdapter.this.mServiceTargetScale[i].startAnimation();
                        }
                    }
                    ChooserRowAdapter.this.notifyDataSetChanged();
                }

                public void onInvalidated() {
                    super.onInvalidated();
                    ChooserRowAdapter.this.notifyDataSetInvalidated();
                    if (ChooserRowAdapter.this.mServiceTargetScale != null) {
                        for (RowScale rs : ChooserRowAdapter.this.mServiceTargetScale) {
                            rs.cancelAnimation();
                        }
                    }
                }
            });
        }

        private float getRowScale(int rowPosition) {
            int start = getCallerTargetRowCount();
            int end = start + getServiceTargetRowCount();
            if (rowPosition < start || rowPosition >= end) {
                return 1.0f;
            }
            return this.mServiceTargetScale[rowPosition - start].get();
        }

        public int getCount() {
            return (int) (((double) (getCallerTargetRowCount() + getServiceTargetRowCount())) + Math.ceil((double) (((float) this.mChooserListAdapter.getStandardTargetCount()) / 4.0f)));
        }

        public int getCallerTargetRowCount() {
            return (int) Math.ceil((double) (((float) this.mChooserListAdapter.getCallerTargetCount()) / 4.0f));
        }

        public int getServiceTargetRowCount() {
            return (int) Math.ceil((double) (((float) this.mChooserListAdapter.getServiceTargetCount()) / 4.0f));
        }

        public Object getItem(int position) {
            return Integer.valueOf(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            RowViewHolder holder;
            if (convertView == null) {
                holder = createViewHolder(parent);
            } else {
                holder = (RowViewHolder) convertView.getTag();
            }
            bindViewHolder(position, holder);
            return holder.row;
        }

        RowViewHolder createViewHolder(ViewGroup parent) {
            LayoutParams lp;
            ViewGroup row = (ViewGroup) this.mLayoutInflater.inflate((int) R.layout.chooser_row, parent, false);
            final RowViewHolder holder = new RowViewHolder(row, 4);
            int spec = MeasureSpec.makeMeasureSpec(0, 0);
            for (int i = 0; i < 4; i++) {
                View v = this.mChooserListAdapter.createView(row);
                final int column = i;
                v.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        ChooserActivity.this.startSelected(holder.itemIndices[column], false, true);
                    }
                });
                v.setOnLongClickListener(new OnLongClickListener() {
                    public boolean onLongClick(View v) {
                        ChooserActivity.this.showAppDetails(ChooserRowAdapter.this.mChooserListAdapter.resolveInfoForPosition(holder.itemIndices[column], true));
                        return true;
                    }
                });
                row.addView(v);
                holder.cells[i] = v;
                lp = v.getLayoutParams();
                v.measure(spec, spec);
                if (lp == null) {
                    row.setLayoutParams(new LayoutParams(-1, v.getMeasuredHeight()));
                } else {
                    lp.height = v.getMeasuredHeight();
                }
            }
            holder.measure();
            lp = row.getLayoutParams();
            if (lp == null) {
                row.setLayoutParams(new LayoutParams(-1, holder.measuredRowHeight));
            } else {
                lp.height = holder.measuredRowHeight;
            }
            row.setTag(holder);
            return holder;
        }

        void bindViewHolder(int rowPosition, RowViewHolder holder) {
            int start = getFirstRowPosition(rowPosition);
            int startType = this.mChooserListAdapter.getPositionTargetType(start);
            int end = (start + 4) - 1;
            while (this.mChooserListAdapter.getPositionTargetType(end) != startType && end >= start) {
                end--;
            }
            if (startType == 1) {
                holder.row.setBackgroundColor(ChooserActivity.this.getColor(R.color.chooser_service_row_background_color));
            } else {
                holder.row.setBackgroundColor(0);
            }
            int oldHeight = holder.row.getLayoutParams().height;
            holder.row.getLayoutParams().height = Math.max(1, (int) (((float) holder.measuredRowHeight) * getRowScale(rowPosition)));
            if (holder.row.getLayoutParams().height != oldHeight) {
                holder.row.requestLayout();
            }
            for (int i = 0; i < 4; i++) {
                View v = holder.cells[i];
                if (start + i <= end) {
                    v.setVisibility(0);
                    holder.itemIndices[i] = start + i;
                    this.mChooserListAdapter.bindView(holder.itemIndices[i], v);
                } else {
                    v.setVisibility(8);
                }
            }
        }

        int getFirstRowPosition(int row) {
            int callerCount = this.mChooserListAdapter.getCallerTargetCount();
            int callerRows = (int) Math.ceil((double) (((float) callerCount) / 4.0f));
            if (row < callerRows) {
                return row * 4;
            }
            int serviceCount = this.mChooserListAdapter.getServiceTargetCount();
            int serviceRows = (int) Math.ceil((double) (((float) serviceCount) / 4.0f));
            if (row < callerRows + serviceRows) {
                return ((row - callerRows) * 4) + callerCount;
            }
            return (callerCount + serviceCount) + (((row - callerRows) - serviceRows) * 4);
        }
    }

    final class ChooserTargetInfo implements TargetInfo {
        private final ResolveInfo mBackupResolveInfo;
        private CharSequence mBadgeContentDescription;
        private Drawable mBadgeIcon = null;
        private final ChooserTarget mChooserTarget;
        private Drawable mDisplayIcon;
        private final int mFillInFlags;
        private final Intent mFillInIntent;
        private final float mModifiedScore;
        private final DisplayResolveInfo mSourceInfo;

        public ChooserTargetInfo(DisplayResolveInfo sourceInfo, ChooserTarget chooserTarget, float modifiedScore) {
            this.mSourceInfo = sourceInfo;
            this.mChooserTarget = chooserTarget;
            this.mModifiedScore = modifiedScore;
            if (sourceInfo != null) {
                ResolveInfo ri = sourceInfo.getResolveInfo();
                if (ri != null) {
                    ActivityInfo ai = ri.activityInfo;
                    if (!(ai == null || ai.applicationInfo == null)) {
                        PackageManager pm = ChooserActivity.this.getPackageManager();
                        this.mBadgeIcon = pm.getApplicationIcon(ai.applicationInfo);
                        this.mBadgeContentDescription = pm.getApplicationLabel(ai.applicationInfo);
                    }
                }
            }
            Icon icon = chooserTarget.getIcon();
            this.mDisplayIcon = icon != null ? icon.loadDrawable(ChooserActivity.this) : null;
            if (sourceInfo != null) {
                this.mBackupResolveInfo = null;
            } else {
                this.mBackupResolveInfo = ChooserActivity.this.getPackageManager().resolveActivity(getResolvedIntent(), 0);
            }
            this.mFillInIntent = null;
            this.mFillInFlags = 0;
        }

        private ChooserTargetInfo(ChooserTargetInfo other, Intent fillInIntent, int flags) {
            this.mSourceInfo = other.mSourceInfo;
            this.mBackupResolveInfo = other.mBackupResolveInfo;
            this.mChooserTarget = other.mChooserTarget;
            this.mBadgeIcon = other.mBadgeIcon;
            this.mBadgeContentDescription = other.mBadgeContentDescription;
            this.mDisplayIcon = other.mDisplayIcon;
            this.mFillInIntent = fillInIntent;
            this.mFillInFlags = flags;
            this.mModifiedScore = other.mModifiedScore;
        }

        public float getModifiedScore() {
            return this.mModifiedScore;
        }

        public Intent getResolvedIntent() {
            if (this.mSourceInfo != null) {
                return this.mSourceInfo.getResolvedIntent();
            }
            return ChooserActivity.this.getTargetIntent();
        }

        public ComponentName getResolvedComponentName() {
            if (this.mSourceInfo != null) {
                return this.mSourceInfo.getResolvedComponentName();
            }
            if (this.mBackupResolveInfo != null) {
                return new ComponentName(this.mBackupResolveInfo.activityInfo.packageName, this.mBackupResolveInfo.activityInfo.name);
            }
            return null;
        }

        private Intent getBaseIntentToSend() {
            Intent result = this.mSourceInfo != null ? this.mSourceInfo.getResolvedIntent() : ChooserActivity.this.getTargetIntent();
            if (result == null) {
                Log.e(ChooserActivity.TAG, "ChooserTargetInfo: no base intent available to send");
                return result;
            }
            Intent result2 = new Intent(result);
            if (this.mFillInIntent != null) {
                result2.fillIn(this.mFillInIntent, this.mFillInFlags);
            }
            result2.fillIn(ChooserActivity.this.mReferrerFillInIntent, 0);
            return result2;
        }

        public boolean start(Activity activity, Bundle options) {
            throw new RuntimeException("ChooserTargets should be started as caller.");
        }

        public boolean startAsCaller(Activity activity, Bundle options, int userId) {
            boolean ignoreTargetSecurity = false;
            Intent intent = getBaseIntentToSend();
            if (intent == null) {
                return false;
            }
            intent.setComponent(this.mChooserTarget.getComponentName());
            intent.putExtras(this.mChooserTarget.getIntentExtras());
            if (this.mSourceInfo != null && this.mSourceInfo.getResolvedComponentName().getPackageName().equals(this.mChooserTarget.getComponentName().getPackageName())) {
                ignoreTargetSecurity = true;
            }
            activity.startActivityAsCaller(intent, options, ignoreTargetSecurity, userId);
            return true;
        }

        public boolean startAsUser(Activity activity, Bundle options, UserHandle user) {
            throw new RuntimeException("ChooserTargets should be started as caller.");
        }

        public ResolveInfo getResolveInfo() {
            return this.mSourceInfo != null ? this.mSourceInfo.getResolveInfo() : this.mBackupResolveInfo;
        }

        public CharSequence getDisplayLabel() {
            return this.mChooserTarget.getTitle();
        }

        public CharSequence getExtendedInfo() {
            return null;
        }

        public Drawable getDisplayIcon() {
            return this.mDisplayIcon;
        }

        public Drawable getBadgeIcon() {
            return this.mBadgeIcon;
        }

        public CharSequence getBadgeContentDescription() {
            return this.mBadgeContentDescription;
        }

        public TargetInfo cloneFilledIn(Intent fillInIntent, int flags) {
            return new ChooserTargetInfo(this, fillInIntent, flags);
        }

        public List<Intent> getAllSourceIntents() {
            List<Intent> results = new ArrayList();
            if (this.mSourceInfo != null) {
                results.add(this.mSourceInfo.getAllSourceIntents().get(0));
            }
            return results;
        }
    }

    static class ChooserTargetServiceConnection implements ServiceConnection {
        private ChooserActivity mChooserActivity;
        private final IChooserTargetResult mChooserTargetResult = new Stub() {
            public void sendResult(List<ChooserTarget> targets) throws RemoteException {
                synchronized (ChooserTargetServiceConnection.this.mLock) {
                    if (ChooserTargetServiceConnection.this.mChooserActivity == null) {
                        Log.e(ChooserActivity.TAG, "destroyed ChooserTargetServiceConnection received result from " + ChooserTargetServiceConnection.this.mConnectedComponent + "; ignoring...");
                        return;
                    }
                    ChooserTargetServiceConnection.this.mChooserActivity.filterServiceTargets(ChooserTargetServiceConnection.this.mOriginalTarget.getResolveInfo().activityInfo.packageName, targets);
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = new ServiceResultInfo(ChooserTargetServiceConnection.this.mOriginalTarget, targets, ChooserTargetServiceConnection.this);
                    ChooserTargetServiceConnection.this.mChooserActivity.mChooserHandler.sendMessage(msg);
                }
            }
        };
        private ComponentName mConnectedComponent;
        private final Object mLock = new Object();
        private final DisplayResolveInfo mOriginalTarget;

        public ChooserTargetServiceConnection(ChooserActivity chooserActivity, DisplayResolveInfo dri) {
            this.mChooserActivity = chooserActivity;
            this.mOriginalTarget = dri;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            synchronized (this.mLock) {
                if (this.mChooserActivity == null) {
                    Log.e(ChooserActivity.TAG, "destroyed ChooserTargetServiceConnection got onServiceConnected");
                    return;
                }
                try {
                    IChooserTargetService.Stub.asInterface(service).getChooserTargets(this.mOriginalTarget.getResolvedComponentName(), this.mOriginalTarget.getResolveInfo().filter, this.mChooserTargetResult);
                } catch (RemoteException e) {
                    Log.e(ChooserActivity.TAG, "Querying ChooserTargetService " + name + " failed.", e);
                    this.mChooserActivity.unbindService(this);
                    destroy();
                    this.mChooserActivity.mServiceConnections.remove(this);
                }
            }
        }

        public void onServiceDisconnected(ComponentName name) {
            synchronized (this.mLock) {
                if (this.mChooserActivity == null) {
                    Log.e(ChooserActivity.TAG, "destroyed ChooserTargetServiceConnection got onServiceDisconnected");
                    return;
                }
                this.mChooserActivity.unbindService(this);
                destroy();
                this.mChooserActivity.mServiceConnections.remove(this);
                if (this.mChooserActivity.mServiceConnections.isEmpty()) {
                    this.mChooserActivity.mChooserHandler.removeMessages(2);
                    this.mChooserActivity.sendVoiceChoicesIfNeeded();
                }
                this.mConnectedComponent = null;
            }
        }

        public void destroy() {
            synchronized (this.mLock) {
                this.mChooserActivity = null;
            }
        }

        public String toString() {
            return "ChooserTargetServiceConnection{service=" + this.mConnectedComponent + ", activity=" + this.mOriginalTarget.getResolveInfo().activityInfo.toString() + "}";
        }
    }

    class OffsetDataSetObserver extends DataSetObserver {
        private View mCachedView;
        private int mCachedViewType = -1;
        private final AbsListView mListView;

        public OffsetDataSetObserver(AbsListView listView) {
            this.mListView = listView;
        }

        public void onChanged() {
            if (ChooserActivity.this.mResolverDrawerLayout != null) {
                int chooserTargetRows = ChooserActivity.this.mChooserRowAdapter.getServiceTargetRowCount();
                int offset = 0;
                for (int i = 0; i < chooserTargetRows; i++) {
                    int pos = ChooserActivity.this.mChooserRowAdapter.getCallerTargetRowCount() + i;
                    int vt = ChooserActivity.this.mChooserRowAdapter.getItemViewType(pos);
                    if (vt != this.mCachedViewType) {
                        this.mCachedView = null;
                    }
                    View v = ChooserActivity.this.mChooserRowAdapter.getView(pos, this.mCachedView, this.mListView);
                    offset += (int) (((float) ((RowViewHolder) v.getTag()).measuredRowHeight) * ChooserActivity.this.mChooserRowAdapter.getRowScale(pos));
                    if (vt >= 0) {
                        this.mCachedViewType = vt;
                        this.mCachedView = v;
                    } else {
                        this.mCachedViewType = -1;
                    }
                }
                ChooserActivity.this.mResolverDrawerLayout.setCollapsibleHeightReserved(offset);
            }
        }
    }

    static class RefinementResultReceiver extends ResultReceiver {
        private ChooserActivity mChooserActivity;
        private TargetInfo mSelectedTarget;

        public RefinementResultReceiver(ChooserActivity host, TargetInfo target, Handler handler) {
            super(handler);
            this.mChooserActivity = host;
            this.mSelectedTarget = target;
        }

        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (this.mChooserActivity == null) {
                Log.e(ChooserActivity.TAG, "Destroyed RefinementResultReceiver received a result");
            } else if (resultData == null) {
                Log.e(ChooserActivity.TAG, "RefinementResultReceiver received null resultData");
            } else {
                switch (resultCode) {
                    case -1:
                        Parcelable intentParcelable = resultData.getParcelable("android.intent.extra.INTENT");
                        if (intentParcelable instanceof Intent) {
                            this.mChooserActivity.onRefinementResult(this.mSelectedTarget, (Intent) intentParcelable);
                            return;
                        } else {
                            Log.e(ChooserActivity.TAG, "RefinementResultReceiver received RESULT_OK but no Intent in resultData with key Intent.EXTRA_INTENT");
                            return;
                        }
                    case 0:
                        this.mChooserActivity.onRefinementCanceled();
                        return;
                    default:
                        Log.w(ChooserActivity.TAG, "Unknown result code " + resultCode + " sent to RefinementResultReceiver");
                        return;
                }
            }
        }

        public void destroy() {
            this.mChooserActivity = null;
            this.mSelectedTarget = null;
        }
    }

    static class RowScale {
        private static final int DURATION = 400;
        public static final FloatProperty<RowScale> PROPERTY = new FloatProperty<RowScale>("scale") {
            public void setValue(RowScale object, float value) {
                object.mScale = value;
                object.mAdapter.notifyDataSetChanged();
            }

            public Float get(RowScale object) {
                return Float.valueOf(object.mScale);
            }
        };
        ChooserRowAdapter mAdapter;
        private final ObjectAnimator mAnimator;
        float mScale;

        public RowScale(ChooserRowAdapter adapter, float from, float to) {
            this.mAdapter = adapter;
            this.mScale = from;
            if (from == to) {
                this.mAnimator = null;
                return;
            }
            this.mAnimator = ObjectAnimator.ofFloat(this, PROPERTY, new float[]{from, to}).setDuration(400);
        }

        public RowScale setInterpolator(Interpolator interpolator) {
            if (this.mAnimator != null) {
                this.mAnimator.setInterpolator(interpolator);
            }
            return this;
        }

        public float get() {
            return this.mScale;
        }

        public void startAnimation() {
            if (this.mAnimator != null) {
                this.mAnimator.start();
            }
        }

        public void cancelAnimation() {
            if (this.mAnimator != null) {
                this.mAnimator.cancel();
            }
        }
    }

    static class RowViewHolder {
        final View[] cells;
        int[] itemIndices;
        int measuredRowHeight;
        final ViewGroup row;

        public RowViewHolder(ViewGroup row, int cellCount) {
            this.row = row;
            this.cells = new View[cellCount];
            this.itemIndices = new int[cellCount];
        }

        public void measure() {
            int spec = MeasureSpec.makeMeasureSpec(0, 0);
            this.row.measure(spec, spec);
            this.measuredRowHeight = this.row.getMeasuredHeight();
        }
    }

    static class ServiceResultInfo {
        public final ChooserTargetServiceConnection connection;
        public final DisplayResolveInfo originalTarget;
        public final List<ChooserTarget> resultTargets;

        public ServiceResultInfo(DisplayResolveInfo ot, List<ChooserTarget> rt, ChooserTargetServiceConnection c) {
            this.originalTarget = ot;
            this.resultTargets = rt;
            this.connection = c;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Parcelable targetParcelable = intent.getParcelableExtra("android.intent.extra.INTENT");
        if (targetParcelable instanceof Intent) {
            int i;
            Intent target = (Intent) targetParcelable;
            if (target != null) {
                modifyTargetIntent(target);
            }
            Parcelable[] targetsParcelable = intent.getParcelableArrayExtra("android.intent.extra.ALTERNATE_INTENTS");
            if (targetsParcelable != null) {
                boolean offset = target == null;
                Intent[] additionalTargets = new Intent[(offset ? targetsParcelable.length - 1 : targetsParcelable.length)];
                i = 0;
                while (i < targetsParcelable.length) {
                    if (targetsParcelable[i] instanceof Intent) {
                        Intent additionalTarget = targetsParcelable[i];
                        if (i == 0 && target == null) {
                            target = additionalTarget;
                            modifyTargetIntent(target);
                        } else {
                            int i2;
                            if (offset) {
                                i2 = i - 1;
                            } else {
                                i2 = i;
                            }
                            additionalTargets[i2] = additionalTarget;
                            modifyTargetIntent(additionalTarget);
                        }
                        i++;
                    } else {
                        Log.w(TAG, "EXTRA_ALTERNATE_INTENTS array entry #" + i + " is not an Intent: " + targetsParcelable[i]);
                        finish();
                        super.onCreate(null);
                        return;
                    }
                }
                setAdditionalTargets(additionalTargets);
            }
            this.mReplacementExtras = intent.getBundleExtra("android.intent.extra.REPLACEMENT_EXTRAS");
            CharSequence title = intent.getCharSequenceExtra("android.intent.extra.TITLE");
            int defaultTitleRes = 0;
            if (title == null) {
                defaultTitleRes = R.string.chooseActivity;
            }
            Parcelable[] pa = intent.getParcelableArrayExtra("android.intent.extra.INITIAL_INTENTS");
            Intent[] initialIntents = null;
            if (pa != null) {
                initialIntents = new Intent[pa.length];
                i = 0;
                while (i < pa.length) {
                    if (pa[i] instanceof Intent) {
                        Intent in = pa[i];
                        modifyTargetIntent(in);
                        initialIntents[i] = in;
                        i++;
                    } else {
                        Log.w(TAG, "Initial intent #" + i + " not an Intent: " + pa[i]);
                        finish();
                        super.onCreate(null);
                        return;
                    }
                }
            }
            ArrayList<ComponentName> dropComponents = intent.getParcelableArrayListExtra(EXTRA_CHOOSER_DROPLIST);
            if (dropComponents != null) {
                for (i = 0; i < dropComponents.size(); i++) {
                    if (dropComponents.get(i) instanceof ComponentName) {
                        setDropComponent((ComponentName) dropComponents.get(i));
                    }
                }
            }
            try {
                this.mReferrerFillInIntent = new Intent().putExtra("android.intent.extra.REFERRER", getReferrer());
            } catch (Exception e) {
                Log.e(TAG, "mReferrerFillInIntent is null!!!" + e);
            }
            this.mChosenComponentSender = (IntentSender) intent.getParcelableExtra("android.intent.extra.CHOSEN_COMPONENT_INTENT_SENDER");
            this.mRefinementIntentSender = (IntentSender) intent.getParcelableExtra("android.intent.extra.CHOOSER_REFINEMENT_INTENT_SENDER");
            setSafeForwardingMode(true);
            super.onCreate(savedInstanceState, target, title, defaultTitleRes, initialIntents, null, false);
            MetricsLogger.action(this, 214);
            return;
        }
        Log.w(TAG, "Target is not an intent: " + targetParcelable);
        finish();
        super.onCreate(null);
    }

    protected void onPause() {
        super.onPause();
        UserManager userManager = (UserManager) getSystemService(ImsConferenceState.USER);
        int currUser = getBaseContext().getUserId();
        UserInfo currInfo = userManager.getUserInfo(currUser);
        if (currInfo != null && currInfo.isKnoxWorkspace()) {
            PersonaManager pm = (PersonaManager) getSystemService("persona");
            if (pm != null && pm.getStateManager(currUser).inState(PersonaState.LOCKED)) {
                finish();
            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mRefinementResultReceiver != null) {
            this.mRefinementResultReceiver.destroy();
            this.mRefinementResultReceiver = null;
        }
        unbindRemainingServices();
        this.mChooserHandler.removeMessages(1);
    }

    public Intent getReplacementIntent(ActivityInfo aInfo, Intent defIntent) {
        Intent result = defIntent;
        if (this.mReplacementExtras != null) {
            Bundle replExtras = this.mReplacementExtras.getBundle(aInfo.packageName);
            if (replExtras != null) {
                result = new Intent(defIntent);
                result.putExtras(replExtras);
            }
        }
        if (aInfo.name.equals(IntentForwarderActivity.FORWARD_INTENT_TO_USER_OWNER) || aInfo.name.equals(IntentForwarderActivity.FORWARD_INTENT_TO_MANAGED_PROFILE)) {
            return Intent.createChooser(result, getIntent().getCharSequenceExtra("android.intent.extra.TITLE"));
        }
        return result;
    }

    void onActivityStarted(TargetInfo cti) {
        if (this.mChosenComponentSender != null) {
            ComponentName target = cti.getResolvedComponentName();
            if (target != null) {
                try {
                    this.mChosenComponentSender.sendIntent(this, -1, new Intent().putExtra("android.intent.extra.CHOSEN_COMPONENT", target), null, null);
                } catch (SendIntentException e) {
                    Slog.e(TAG, "Unable to launch supplied IntentSender to report the chosen component: " + e);
                }
            }
        }
    }

    void onPrepareAdapterView(AbsListView adapterView, ResolveListAdapter adapter, boolean alwaysUseOption) {
        ListView listView = adapterView instanceof ListView ? (ListView) adapterView : null;
        this.mChooserListAdapter = (ChooserListAdapter) adapter;
        if (!getSShareCommon().isDeviceDefaultTheme()) {
            this.mChooserRowAdapter = new ChooserRowAdapter(this.mChooserListAdapter);
            this.mChooserRowAdapter.registerDataSetObserver(new OffsetDataSetObserver(adapterView));
            adapterView.setAdapter(this.mChooserRowAdapter);
            if (listView != null) {
                listView.setItemsCanFocus(true);
            }
        } else if (getSShareCommon().getSupportPageMode()) {
            buildUpPagerAdapter();
            preparePageView(this.mChooserListAdapter.getCount());
        }
    }

    int getLayoutResource() {
        if (getSShareCommon().isDeviceDefaultTheme() && getSShareCommon().getSupportPageMode()) {
            return R.layout.tw_resolver_pagemode_remote_share_scroll_dialog;
        }
        return R.layout.chooser_grid;
    }

    boolean shouldGetActivityMetadata() {
        return true;
    }

    boolean shouldAutoLaunchSingleChoice(TargetInfo target) {
        Intent intent = target.getResolvedIntent();
        ResolveInfo resolve = target.getResolveInfo();
        if (intent == null || !"android.intent.action.GET_CONTENT".equals(intent.getAction()) || resolve == null || resolve.priority <= 0 || resolve.activityInfo == null || !"com.android.documentsui".equals(resolve.activityInfo.packageName)) {
            return false;
        }
        return true;
    }

    private void modifyTargetIntent(Intent in) {
        String action = in.getAction();
        if ("android.intent.action.SEND".equals(action) || "android.intent.action.SEND_MULTIPLE".equals(action)) {
            in.addFlags(134742016);
        }
    }

    protected boolean onTargetSelected(TargetInfo target, boolean alwaysCheck) {
        if (this.mRefinementIntentSender != null) {
            Intent fillIn = new Intent();
            List<Intent> sourceIntents = target.getAllSourceIntents();
            if (!sourceIntents.isEmpty()) {
                fillIn.putExtra("android.intent.extra.INTENT", (Parcelable) sourceIntents.get(0));
                if (sourceIntents.size() > 1) {
                    Intent[] alts = new Intent[(sourceIntents.size() - 1)];
                    int N = sourceIntents.size();
                    for (int i = 1; i < N; i++) {
                        alts[i - 1] = (Intent) sourceIntents.get(i);
                    }
                    fillIn.putExtra("android.intent.extra.ALTERNATE_INTENTS", alts);
                }
                if (this.mRefinementResultReceiver != null) {
                    this.mRefinementResultReceiver.destroy();
                }
                this.mRefinementResultReceiver = new RefinementResultReceiver(this, target, null);
                fillIn.putExtra("android.intent.extra.RESULT_RECEIVER", this.mRefinementResultReceiver);
                try {
                    this.mRefinementIntentSender.sendIntent(this, 0, fillIn, null, null);
                    return false;
                } catch (SendIntentException e) {
                    Log.e(TAG, "Refinement IntentSender failed to send", e);
                }
            }
        }
        return super.onTargetSelected(target, alwaysCheck);
    }

    void startSelected(int which, boolean always, boolean filtered) {
        super.startSelected(which, always, filtered);
        if (this.mChooserListAdapter != null) {
            int cat = 0;
            int value = which;
            switch (this.mChooserListAdapter.getPositionTargetType(which)) {
                case 0:
                    cat = 215;
                    break;
                case 1:
                    cat = 216;
                    value -= this.mChooserListAdapter.getCallerTargetCount();
                    break;
                case 2:
                    cat = 217;
                    value -= this.mChooserListAdapter.getCallerTargetCount() + this.mChooserListAdapter.getServiceTargetCount();
                    break;
            }
            if (cat != 0) {
                MetricsLogger.action((Context) this, cat, value);
            }
        }
    }

    void queryTargetServices(ChooserListAdapter adapter) {
        PackageManager pm = getPackageManager();
        int targetsToQuery = 0;
        int N = adapter.getDisplayResolveInfoCount();
        for (int i = 0; i < N; i++) {
            DisplayResolveInfo dri = adapter.getDisplayResolveInfo(i);
            if (adapter.getScore(dri) != 0.0f) {
                String serviceName;
                ActivityInfo ai = dri.getResolveInfo().activityInfo;
                Bundle md = ai.metaData;
                if (md != null) {
                    serviceName = convertServiceName(ai.packageName, md.getString(ChooserTargetService.META_DATA_NAME));
                } else {
                    serviceName = null;
                }
                if (serviceName != null) {
                    ComponentName serviceComponent = new ComponentName(ai.packageName, serviceName);
                    Intent serviceIntent = new Intent(ChooserTargetService.SERVICE_INTERFACE).setComponent(serviceComponent);
                    try {
                        if (ChooserTargetService.BIND_PERMISSION.equals(pm.getServiceInfo(serviceComponent, 0).permission)) {
                            ChooserTargetServiceConnection conn = new ChooserTargetServiceConnection(this, dri);
                            if (bindServiceAsUser(serviceIntent, conn, 5, UserHandle.CURRENT)) {
                                this.mServiceConnections.add(conn);
                                targetsToQuery++;
                            }
                        } else {
                            Log.w(TAG, "ChooserTargetService " + serviceComponent + " does not require" + " permission " + ChooserTargetService.BIND_PERMISSION + " - this service will not be queried for ChooserTargets." + " add android:permission=\"" + ChooserTargetService.BIND_PERMISSION + "\"" + " to the <service> tag for " + serviceComponent + " in the manifest.");
                        }
                    } catch (NameNotFoundException e) {
                        Log.e(TAG, "Could not look up service " + serviceComponent, e);
                    }
                }
                if (targetsToQuery >= 5) {
                    break;
                }
            }
        }
        if (this.mServiceConnections.isEmpty()) {
            sendVoiceChoicesIfNeeded();
        } else {
            this.mChooserHandler.sendEmptyMessageDelayed(2, TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS);
        }
    }

    private String convertServiceName(String packageName, String serviceName) {
        if (TextUtils.isEmpty(serviceName)) {
            return null;
        }
        if (serviceName.startsWith(".")) {
            return packageName + serviceName;
        }
        if (serviceName.indexOf(46) >= 0) {
            return serviceName;
        }
        return null;
    }

    void unbindRemainingServices() {
        int N = this.mServiceConnections.size();
        for (int i = 0; i < N; i++) {
            ChooserTargetServiceConnection conn = (ChooserTargetServiceConnection) this.mServiceConnections.get(i);
            unbindService(conn);
            conn.destroy();
        }
        this.mServiceConnections.clear();
        this.mChooserHandler.removeMessages(2);
    }

    void onSetupVoiceInteraction() {
    }

    void onRefinementResult(TargetInfo selectedTarget, Intent matchingIntent) {
        if (this.mRefinementResultReceiver != null) {
            this.mRefinementResultReceiver.destroy();
            this.mRefinementResultReceiver = null;
        }
        if (selectedTarget == null) {
            Log.e(TAG, "Refinement result intent did not match any known targets; canceling");
        } else if (!checkTargetSourceIntent(selectedTarget, matchingIntent)) {
            Log.e(TAG, "onRefinementResult: Selected target " + selectedTarget + " cannot match refined source intent " + matchingIntent);
        } else if (super.onTargetSelected(selectedTarget.cloneFilledIn(matchingIntent, 0), false)) {
            finish();
            return;
        }
        onRefinementCanceled();
    }

    void onRefinementCanceled() {
        if (this.mRefinementResultReceiver != null) {
            this.mRefinementResultReceiver.destroy();
            this.mRefinementResultReceiver = null;
        }
        finish();
    }

    boolean checkTargetSourceIntent(TargetInfo target, Intent matchingIntent) {
        List<Intent> targetIntents = target.getAllSourceIntents();
        int N = targetIntents.size();
        for (int i = 0; i < N; i++) {
            if (((Intent) targetIntents.get(i)).filterEquals(matchingIntent)) {
                return true;
            }
        }
        return false;
    }

    void filterServiceTargets(String packageName, List<ChooserTarget> targets) {
        if (targets != null) {
            PackageManager pm = getPackageManager();
            for (int i = targets.size() - 1; i >= 0; i--) {
                ChooserTarget target = (ChooserTarget) targets.get(i);
                ComponentName targetName = target.getComponentName();
                if (packageName == null || !packageName.equals(targetName.getPackageName())) {
                    boolean remove;
                    try {
                        ActivityInfo ai = pm.getActivityInfo(targetName, 0);
                        if (ai.exported && ai.permission == null) {
                            remove = false;
                        } else {
                            remove = true;
                        }
                    } catch (NameNotFoundException e) {
                        Log.e(TAG, "Target " + target + " returned by " + packageName + " component not found");
                        remove = true;
                    }
                    if (remove) {
                        targets.remove(i);
                    }
                }
            }
        }
    }

    ResolveListAdapter createAdapter(Context context, List<Intent> payloadIntents, Intent[] initialIntents, List<ResolveInfo> rList, int launchedFromUid, boolean filterLastUsed) {
        ChooserListAdapter adapter = new ChooserListAdapter(context, payloadIntents, initialIntents, rList, launchedFromUid, filterLastUsed);
        if (Settings$System.getInt(getContentResolver(), Settings$System.DIRECT_SHARE, 1) == 1) {
            queryTargetServices(adapter);
        }
        return adapter;
    }

    void buildUpPagerAdapter() {
        int dataCnt = this.mChooserListAdapter.getCount();
        int serviceTargetCount = this.mChooserListAdapter.getServiceTargetCount();
        int callerTargetCount = this.mChooserListAdapter.getCallerTargetCount();
        int standardTargetCount = this.mChooserListAdapter.getStandardTargetCount();
        for (int i = 0; i < dataCnt; i++) {
            int offset;
            int pageIndex = i / getPageItemNum();
            if (i % getPageItemNum() == 0) {
                getPageResolverList().add(new PageResolverListAdapter(this));
            }
            String key = String.format(getPagerKey(), new Object[]{Integer.valueOf(pageIndex), Integer.valueOf(itemIndex)});
            if (i < serviceTargetCount) {
                offset = callerTargetCount;
            } else if (i < serviceTargetCount + callerTargetCount) {
                offset = 0 - serviceTargetCount;
            } else {
                offset = 0;
            }
            int origPosition = i + offset;
            TargetInfo targetInfo = this.mChooserListAdapter.getItem(origPosition);
            PageResolverListAdapter pageAdapter = (PageResolverListAdapter) getPageResolverList().get(pageIndex);
            if (pageAdapter != null) {
                pageAdapter.mList.add(targetInfo);
                pageAdapter.mKey.add(key);
            }
            getResolverListMap().put(key, Integer.valueOf(origPosition));
        }
    }
}
