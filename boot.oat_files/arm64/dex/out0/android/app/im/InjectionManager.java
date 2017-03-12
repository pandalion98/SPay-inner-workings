package android.app.im;

import android.app.Activity;
import android.app.Fragment;
import android.app.im.IInjectionManager.Stub;
import android.app.im.InjectionConstants.DispatchParentCall;
import android.app.im.InjectionConstants.FeatureType;
import android.app.im.feature.IContentProvider;
import android.app.im.feature.IContextMenu;
import android.app.im.feature.IDynamicView;
import android.app.im.feature.IInjection;
import android.app.im.feature.IOptionsMenu;
import android.app.im.feature.IPreference;
import android.app.im.feature.IPreferenceHeader;
import android.app.im.feature.IScaleView;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.preference.PreferenceActivity;
import android.preference.PreferenceActivity.Header;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ElasticMenuItem;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import com.android.internal.view.menu.MenuBuilder;
import dalvik.system.PathClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public final class InjectionManager {
    public static final boolean DEBUG_ELASTIC = true;
    private static String TAG = "InjectionManager";
    private static InjectionManager mInstance;
    static HashMap<Integer, String> mLibMap = null;
    private static IInjectionManager sService;
    public PathClassLoader coreFeatureClassLoader;
    HashMap<String, IInjection> featureSourceStore = new HashMap();
    HashMap<String, HashMap<String, List<FeatureInfo>>> featureStore = new HashMap();
    private Context mContext = null;
    boolean packageHasFeatures;
    HashMap<String, PathClassLoader> pathClassLoaderStore = new HashMap();
    HashMap<String, Context> pkgContextStore = new HashMap();

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$app$im$InjectionConstants$DispatchParentCall = new int[DispatchParentCall.values().length];

        static {
            try {
                $SwitchMap$android$app$im$InjectionConstants$DispatchParentCall[DispatchParentCall.ONCONFIGURATIONCHANGED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$app$im$InjectionConstants$DispatchParentCall[DispatchParentCall.ONSTART.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$app$im$InjectionConstants$DispatchParentCall[DispatchParentCall.ONRESTART.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$app$im$InjectionConstants$DispatchParentCall[DispatchParentCall.ONSTOP.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$android$app$im$InjectionConstants$DispatchParentCall[DispatchParentCall.ONDESTROY.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$android$app$im$InjectionConstants$DispatchParentCall[DispatchParentCall.ONPAUSE.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$android$app$im$InjectionConstants$DispatchParentCall[DispatchParentCall.ONRESUME.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$android$app$im$InjectionConstants$DispatchParentCall[DispatchParentCall.ONSEARCHREQUESTED.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$android$app$im$InjectionConstants$DispatchParentCall[DispatchParentCall.ONCREATE.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$android$app$im$InjectionConstants$DispatchParentCall[DispatchParentCall.ONSAVEINSTANCESTATE.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$android$app$im$InjectionConstants$DispatchParentCall[DispatchParentCall.ONRESTOREINSTANCESTATE.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
        }
    }

    static class FeatureInfo {
        String className;
        String packageName;

        FeatureInfo(String packageName, String className) {
            this.packageName = packageName;
            this.className = className;
        }

        public String toString() {
            return "packageName =" + this.packageName + ", className =" + this.className;
        }
    }

    public static synchronized IInjectionManager getService() {
        IInjectionManager iInjectionManager;
        synchronized (InjectionManager.class) {
            if (sService != null) {
                iInjectionManager = sService;
            } else {
                sService = Stub.asInterface(ServiceManager.getService(Context.INJECTION_SERVICE));
                iInjectionManager = sService;
            }
        }
        return iInjectionManager;
    }

    private InjectionManager(Context context) {
        this.mContext = context;
        Log.d(TAG, "InjectionManager");
        if (getService() != null) {
            String packageName = context.getPackageName();
            fillFeatureStoreMap(packageName);
            Log.i(TAG, "Constructor " + packageName + ", Feature store :" + this.featureStore);
        }
        Log.i(TAG, "featureStore :" + this.featureStore);
        if (this.featureStore != null && this.featureStore.size() > 0) {
            this.packageHasFeatures = true;
            this.coreFeatureClassLoader = (PathClassLoader) this.mContext.getClassLoader();
            Log.i(TAG, "Parent ClassLoader :" + this.coreFeatureClassLoader);
        }
    }

    public boolean isPackageHasFeatures() {
        return this.packageHasFeatures;
    }

    public static synchronized InjectionManager getInstance(Context context) {
        InjectionManager injectionManager;
        synchronized (InjectionManager.class) {
            if (mInstance == null) {
                mInstance = new InjectionManager(context);
            }
            injectionManager = mInstance;
        }
        return injectionManager;
    }

    public static InjectionManager getInstance() {
        return mInstance;
    }

    public static InjectionManager from(Context context) {
        return (InjectionManager) context.getSystemService(Context.INJECTION_SERVICE);
    }

    private Context getPackageContext(String pkg) {
        Log.i(TAG, "getPackageContext :" + pkg);
        Context mPkgContext = (Context) this.pkgContextStore.get(pkg);
        if (mPkgContext != null) {
            return mPkgContext;
        }
        try {
            mPkgContext = this.mContext.createPackageContext(pkg, 1);
            this.pkgContextStore.put(pkg, mPkgContext);
            Log.i(TAG, "getPackageContext mPkgContext:" + mPkgContext);
            return mPkgContext;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return mPkgContext;
        }
    }

    private ClassLoader getClassLoader(String pkg) {
        Context mPkgContext = getPackageContext(pkg);
        Log.i(TAG, "getClassLoader mPkgContext :" + pkg + " : " + mPkgContext);
        if (mPkgContext != null) {
            return mPkgContext.getClassLoader();
        }
        return null;
    }

    public PathClassLoader getPathClassLoader(String pkg) {
        try {
            return new PathClassLoader(this.mContext.getPackageManager().getApplicationInfo(pkg, 0).sourceDir, this.mContext.getClassLoader());
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public IInjection getClassInstance(String targetClassName, String sourcePkgName, String sourceClassName) {
        Log.i(TAG, "getClassInstance : " + sourcePkgName + " : " + sourceClassName);
        if (this.featureSourceStore != null && this.featureSourceStore.size() > 0) {
            IInjection obj = (IInjection) this.featureSourceStore.get(targetClassName + sourcePkgName + sourceClassName);
            if (obj != null) {
                return obj;
            }
        }
        IInjection sourceObject = null;
        PathClassLoader mPathClass = this.coreFeatureClassLoader;
        if (mPathClass == null) {
            return null;
        }
        Log.d(TAG, "mPathClass : " + mPathClass);
        try {
            Object obj2 = mPathClass.loadClass(sourceClassName).newInstance();
            if (obj2 instanceof IInjection) {
                sourceObject = (IInjection) obj2;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e2) {
            e2.printStackTrace();
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
        }
        Log.i(TAG, "getClassInstance  obj :" + sourceObject);
        return sourceObject;
    }

    public boolean dispatchCreateOptionsMenu(Object target, Menu menu, MenuInflater menuInflater, boolean isFragment) {
        Log.i(TAG, "dispatchCreateOptionsMenu :" + target.getClass().getName());
        return optionsMenuController(1, target, menu, menuInflater, null, isFragment);
    }

    public boolean dispatchPrepareOptionsMenu(Object target, Menu menu) {
        Log.i(TAG, "dispatchPrepareOptionsMenu :" + target.getClass().getName());
        return optionsMenuController(2, target, menu, null, null, false);
    }

    public ArrayList<Cursor> dispatchContentProviderCall(String packageName, ContentResolver cr, Uri uri, String[] projectionIn, String selection, String[] selectionArgs, String sortOrder) {
        Log.i(TAG, "dispatchContentProviderCall :" + packageName);
        return contentProviderController(packageName, cr, uri, projectionIn, selection, selectionArgs, sortOrder);
    }

    private ArrayList<Cursor> contentProviderController(String packageName, ContentResolver cr, Uri uri, String[] projectionIn, String selection, String[] selectionArgs, String sortOrder) {
        ArrayList<Cursor> curList = null;
        if (!this.packageHasFeatures) {
            return null;
        }
        String targetClassName = uri.getAuthority();
        boolean targetHasFeature = this.featureStore.containsKey(targetClassName);
        Log.i(TAG, " target : " + targetClassName + " has feature :" + targetHasFeature);
        if (targetHasFeature) {
            List<FeatureInfo> optionsMenuSourceList = (List) ((HashMap) this.featureStore.get(targetClassName)).get(FeatureType.CONTENT_PROVIDER.toString());
            if (optionsMenuSourceList != null && optionsMenuSourceList.size() > 0) {
                Log.d(TAG, "process each options feature");
                for (FeatureInfo optionsMenuSource : optionsMenuSourceList) {
                    Log.d(TAG, "Option Feature from source" + optionsMenuSource.toString());
                    try {
                        IInjection obj = getClassInstance(targetClassName, optionsMenuSource.packageName, optionsMenuSource.className);
                        if (obj == null) {
                            Log.d(TAG, "not able to obtain instance of package =" + optionsMenuSource.toString());
                        } else if (obj instanceof IContentProvider) {
                            IContentProvider sourceObject = (IContentProvider) obj;
                            Log.d(TAG, "sourceobject =" + sourceObject);
                            Cursor cur = sourceObject.query(uri, uri.getPath(), cr, projectionIn, selection, selectionArgs, null, null, sortOrder);
                            if (cur != null) {
                                if (curList == null) {
                                    curList = new ArrayList();
                                }
                                curList.add(cur);
                            }
                        } else {
                            Log.d(TAG, "not desired options object " + optionsMenuSource.toString());
                        }
                    } catch (ClassCastException exp) {
                        exp.printStackTrace();
                    }
                }
            }
        }
        return curList;
    }

    public boolean dispatchOptionsItemSelected(Object target, MenuItem item) {
        Log.i(TAG, "dispatchOptionsItemSelected :" + target.getClass().getName());
        return optionsMenuController(3, target, null, null, item, false);
    }

    public boolean dispatchScaleEvent(Object target, int scaleEvent, ScaleGestureDetector scaleGD, MotionEvent event) {
        return scaleEventController(scaleEvent, target, scaleGD, event);
    }

    private boolean scaleEventController(int scaleEvent, Object target, ScaleGestureDetector scaleGD, MotionEvent event) {
        if (!this.packageHasFeatures) {
            return false;
        }
        String targetClassName = target.getClass().getName();
        boolean targetHasFeature = this.featureStore.containsKey(targetClassName);
        Log.i(TAG, " target : " + targetClassName + " has feature :" + targetHasFeature);
        if (!targetHasFeature) {
            return false;
        }
        List<FeatureInfo> dynamicMenuSourceList = (List) ((HashMap) this.featureStore.get(targetClassName)).get(FeatureType.SCALE_VIEW.toString());
        if (dynamicMenuSourceList == null || dynamicMenuSourceList.size() <= 0) {
            return false;
        }
        Log.d(TAG, "process each options feature");
        for (FeatureInfo dynamicMenuSource : dynamicMenuSourceList) {
            Log.d(TAG, "Option Feature from source" + dynamicMenuSource.toString());
            try {
                IInjection obj = getClassInstance(target.toString(), dynamicMenuSource.packageName, dynamicMenuSource.className);
                if (obj == null) {
                    Log.d(TAG, "not able to obtain instance of package =" + dynamicMenuSource.toString());
                } else if (obj instanceof IScaleView) {
                    IScaleView sourceObject = (IScaleView) obj;
                    Log.d(TAG, "sourceobject =" + sourceObject);
                    switch (scaleEvent) {
                        case 1:
                            sourceObject.onTouch(event);
                            break;
                        case 2:
                            sourceObject.onScale(scaleGD);
                            break;
                        case 3:
                            sourceObject.onScaleEnd(scaleGD);
                            break;
                        case 4:
                            sourceObject.onScaleBegin(scaleGD);
                            break;
                        default:
                            break;
                    }
                } else {
                    Log.d(TAG, "not desired options object " + dynamicMenuSource.toString());
                }
            } catch (ClassCastException exp) {
                exp.printStackTrace();
            }
        }
        return false;
    }

    public void dispatchTabHostEvent(Object target, String tabId) {
        Log.i(TAG, "dispatchTabHostEvent" + target.getClass().getName());
        tabHostController(target, tabId);
    }

    public boolean dispatchActionBarSpinnerEvent(Object target, int position, long id) {
        Log.i(TAG, "dispatchActionBarSpinnerEvent:" + target.getClass().getName());
        return actionBarSpinnerController(target, position, id);
    }

    public boolean dispatchAdapterEvent(Object target, int adapterEvent, AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "dispatchOptionsItemSelected :" + target.getClass().getName());
        return adapterEventController(adapterEvent, target, parent, view, position, id);
    }

    private void tabHostController(Object target, String tabId) {
        if (this.packageHasFeatures) {
            String targetClassName = target.getClass().getName();
            boolean targetHasFeature = this.featureStore.containsKey(targetClassName);
            Log.i(TAG, " target : " + targetClassName + " has feature :" + targetHasFeature);
            if (targetHasFeature) {
                List<FeatureInfo> dynamicMenuSourceList = (List) ((HashMap) this.featureStore.get(targetClassName)).get(FeatureType.DYNAMIC_VIEW.toString());
                if (dynamicMenuSourceList != null && dynamicMenuSourceList.size() > 0) {
                    Log.d(TAG, "process each options feature");
                    for (FeatureInfo dynamicMenuSource : dynamicMenuSourceList) {
                        Log.d(TAG, "Option Feature from source" + dynamicMenuSource.toString());
                        try {
                            IInjection obj = getClassInstance(target.toString(), dynamicMenuSource.packageName, dynamicMenuSource.className);
                            if (obj == null) {
                                Log.d(TAG, "not able to obtain instance of package =" + dynamicMenuSource.toString());
                            } else if (obj instanceof IDynamicView) {
                                IDynamicView sourceObject = (IDynamicView) obj;
                                Log.d(TAG, "sourceobject =" + sourceObject);
                                sourceObject.onTabChanged(tabId);
                            } else {
                                Log.d(TAG, "not desired options object " + dynamicMenuSource.toString());
                            }
                        } catch (ClassCastException exp) {
                            exp.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private boolean actionBarSpinnerController(Object target, int position, long id) {
        if (!this.packageHasFeatures) {
            return false;
        }
        boolean show = false;
        String targetClassName = target.getClass().getName();
        boolean targetHasFeature = this.featureStore.containsKey(targetClassName);
        Log.i(TAG, " target : " + targetClassName + " has feature :" + targetHasFeature);
        if (!targetHasFeature) {
            return false;
        }
        List<FeatureInfo> dynamicMenuSourceList = (List) ((HashMap) this.featureStore.get(targetClassName)).get(FeatureType.DYNAMIC_VIEW.toString());
        if (dynamicMenuSourceList == null || dynamicMenuSourceList.size() <= 0) {
            return false;
        }
        Log.d(TAG, "process each options feature");
        for (FeatureInfo dynamicMenuSource : dynamicMenuSourceList) {
            Log.d(TAG, "Option Feature from source" + dynamicMenuSource.toString());
            try {
                IInjection obj = getClassInstance(target.toString(), dynamicMenuSource.packageName, dynamicMenuSource.className);
                if (obj == null) {
                    Log.d(TAG, "not able to obtain instance of package =" + dynamicMenuSource.toString());
                } else if (obj instanceof IDynamicView) {
                    IDynamicView sourceObject = (IDynamicView) obj;
                    Log.d(TAG, "sourceobject =" + sourceObject);
                    show |= sourceObject.onNavigationItemSelected(position, id);
                } else {
                    Log.d(TAG, "not desired options object " + dynamicMenuSource.toString());
                }
            } catch (ClassCastException exp) {
                exp.printStackTrace();
            }
        }
        return show;
    }

    private boolean adapterEventController(int option, Object target, AdapterView<?> parent, View view, int position, long id) {
        if (!this.packageHasFeatures) {
            return false;
        }
        boolean show = false;
        String targetClassName = target.getClass().getName();
        boolean targetHasFeature = this.featureStore.containsKey(targetClassName);
        Log.i(TAG, " target : " + targetClassName + " has feature :" + targetHasFeature);
        if (!targetHasFeature) {
            return false;
        }
        List<FeatureInfo> dynamicMenuSourceList = (List) ((HashMap) this.featureStore.get(targetClassName)).get(FeatureType.DYNAMIC_VIEW.toString());
        if (dynamicMenuSourceList == null || dynamicMenuSourceList.size() <= 0) {
            return false;
        }
        Log.d(TAG, "process each options feature");
        for (FeatureInfo dynamicMenuSource : dynamicMenuSourceList) {
            Log.d(TAG, "Option Feature from source" + dynamicMenuSource.toString());
            try {
                IInjection obj = getClassInstance(target.toString(), dynamicMenuSource.packageName, dynamicMenuSource.className);
                if (obj == null) {
                    Log.d(TAG, "not able to obtain instance of package =" + dynamicMenuSource.toString());
                } else if (obj instanceof IDynamicView) {
                    IDynamicView sourceObject = (IDynamicView) obj;
                    Log.d(TAG, "sourceobject =" + sourceObject);
                    switch (option) {
                        case 1:
                            show |= sourceObject.onItemClick(parent, view, position, id);
                            break;
                        case 2:
                            show |= sourceObject.onItemLongClick(parent, view, position, id);
                            break;
                        case 3:
                            show |= sourceObject.onItemSelected(parent, view, position, id);
                            break;
                        case 4:
                            show |= sourceObject.onNothingSelected(parent);
                            break;
                        default:
                            break;
                    }
                } else {
                    Log.d(TAG, "not desired options object " + dynamicMenuSource.toString());
                }
            } catch (ClassCastException exp) {
                exp.printStackTrace();
            }
        }
        return show;
    }

    private boolean optionsMenuController(int option, Object target, Menu menu, MenuInflater menuInflater, MenuItem item, boolean isFragment) {
        if (!this.packageHasFeatures) {
            return false;
        }
        boolean show = false;
        String targetClassName = target.getClass().getName();
        boolean targetHasFeature = this.featureStore.containsKey(targetClassName);
        Log.i(TAG, " target : " + targetClassName + " has feature :" + targetHasFeature);
        if (!targetHasFeature) {
            return false;
        }
        List<FeatureInfo> optionsMenuSourceList = (List) ((HashMap) this.featureStore.get(targetClassName)).get(FeatureType.OPTIONS_MENU.toString());
        if (optionsMenuSourceList == null || optionsMenuSourceList.size() <= 0) {
            return false;
        }
        Log.d(TAG, "process each options feature");
        for (FeatureInfo optionsMenuSource : optionsMenuSourceList) {
            Log.d(TAG, "Option Feature from source" + optionsMenuSource.toString());
            try {
                IInjection obj = getClassInstance(target.toString(), optionsMenuSource.packageName, optionsMenuSource.className);
                if (obj == null) {
                    Log.d(TAG, "not able to obtain instance of package =" + optionsMenuSource.toString());
                } else if (obj instanceof IOptionsMenu) {
                    IOptionsMenu sourceObject = (IOptionsMenu) obj;
                    Log.d(TAG, "sourceobject =" + sourceObject);
                    switch (option) {
                        case 1:
                            if (getPackageContext(optionsMenuSource.packageName) != null) {
                                MenuBuilder pkgContextOptionsMenu = new MenuBuilder(getPackageContext(optionsMenuSource.packageName));
                                show |= sourceObject.onCreateOptionsMenu(pkgContextOptionsMenu, getMenuInflator(optionsMenuSource.packageName));
                                mergeOptionsMenu(pkgContextOptionsMenu, menu);
                                break;
                            }
                            break;
                        case 2:
                            show |= sourceObject.onPrepareOptionsMenu(menu);
                            break;
                        case 3:
                            show |= sourceObject.onOptionsItemSelected(item);
                            break;
                    }
                    if (option == 3 && show) {
                        return show;
                    }
                } else {
                    Log.d(TAG, "not desired options object " + optionsMenuSource.toString());
                }
            } catch (ClassCastException exp) {
                exp.printStackTrace();
            }
        }
        return show;
    }

    private void mergeOptionsMenu(Menu sourceMenu, Menu targetMenu) {
        int menuCount = sourceMenu.size();
        for (int i = 0; i < menuCount; i++) {
            MenuItem item = sourceMenu.getItem(i);
            if (item.hasSubMenu()) {
                SubMenu targetSubMenu = targetMenu.addSubMenu(item.getGroupId(), item.getItemId(), item.getOrder(), item.getTitle()).setIcon(item.getIcon());
                SubMenu sourceSubMenu = item.getSubMenu();
                int sourceSubMenuCount = sourceSubMenu.size();
                for (int j = 0; j < sourceSubMenuCount; j++) {
                    MenuItem sourceSubMenuItem = sourceSubMenu.getItem(j);
                    targetSubMenu.add(sourceSubMenuItem.getGroupId(), sourceSubMenuItem.getItemId(), sourceSubMenuItem.getOrder(), sourceSubMenuItem.getTitle()).setEnabled(sourceSubMenuItem.isEnabled());
                }
            } else {
                targetMenu.add(item.getGroupId(), item.getItemId(), item.getOrder(), item.getTitle()).setIcon(item.getIcon()).setEnabled(item.isEnabled()).setShowAsAction(((ElasticMenuItem) item).getShowAsAction());
            }
        }
    }

    public void dispatchCreateContextMenu(Object target, ContextMenu menu, View view, ContextMenuInfo menuInfo, boolean isFragment) {
        Log.i(TAG, "dispatchCreateContextMenu :" + target.getClass().getName());
        contextMenuController(1, target, menu, view, menuInfo, null, isFragment);
    }

    public boolean dispatchContextItemSelected(Object target, MenuItem item) {
        Log.i(TAG, "dispatchContextItemSelected :" + target.getClass().getName());
        return contextMenuController(2, target, null, null, null, item, false);
    }

    private boolean contextMenuController(int type, Object target, ContextMenu menu, View view, ContextMenuInfo menuInfo, MenuItem item, boolean isFragment) {
        if (!this.packageHasFeatures) {
            return false;
        }
        boolean show = false;
        String targetClassName = target.getClass().getName();
        boolean targetHasFeature = this.featureStore.containsKey(targetClassName);
        Log.i(TAG, "contextMenuController target : " + target.getClass().getName() + " has feature :" + targetHasFeature);
        if (!targetHasFeature) {
            return false;
        }
        List<FeatureInfo> contextMenuSourceList = (List) ((HashMap) this.featureStore.get(targetClassName)).get(FeatureType.CONTEXT_MENU.toString());
        Log.d(TAG, "info for context =" + ((HashMap) this.featureStore.get(targetClassName)).get(FeatureType.CONTEXT_MENU.toString()));
        Log.d(TAG, "contextMenuSource =" + contextMenuSourceList);
        if (contextMenuSourceList == null || contextMenuSourceList.size() <= 0) {
            return false;
        }
        Log.d(TAG, "process each feature");
        for (FeatureInfo contextMenuSource : contextMenuSourceList) {
            Log.d(TAG, "Feature source :" + contextMenuSource);
            IInjection obj = getClassInstance(target.toString(), contextMenuSource.packageName, contextMenuSource.className);
            if (obj == null) {
                Log.d(TAG, "not able to obtain instance of package =" + contextMenuSource.toString());
            } else if (obj instanceof IContextMenu) {
                IContextMenu sourceObject = (IContextMenu) obj;
                Log.d(TAG, "sourceobject =" + sourceObject);
                switch (type) {
                    case 1:
                        Log.d(TAG, "Context OnCreate");
                        sourceObject.onCreateContextMenu(menu, view, menuInfo);
                        break;
                    case 2:
                        Log.d(TAG, "Context OnSelect");
                        show |= sourceObject.onContextItemSelected(item);
                        break;
                }
                if (show) {
                    return show;
                }
            } else {
                Log.d(TAG, "not desired context object " + contextMenuSource.className);
            }
        }
        return show;
    }

    private MenuInflater getMenuInflator(String packageName) {
        return new MenuInflater(getPackageContext(packageName));
    }

    public void dispatchViewCreated(Object target, boolean isFragment) {
        Log.i(TAG, "dispatchOnViewCreated > Target : " + target.getClass().getName() + " isFragment :" + isFragment);
        if (this.packageHasFeatures) {
            String targetClassName = target.getClass().getName();
            boolean targetHasFeature = this.featureStore.containsKey(targetClassName);
            Log.i(TAG, " target : " + targetClassName + " has feature :" + targetHasFeature);
            if (targetHasFeature) {
                List<FeatureInfo> dynamicMenuSourceList = (List) ((HashMap) this.featureStore.get(targetClassName)).get(FeatureType.DYNAMIC_VIEW.toString());
                Log.i(TAG, "Dynamic Menu source : " + dynamicMenuSourceList);
                if (dynamicMenuSourceList != null && dynamicMenuSourceList.size() > 0) {
                    for (FeatureInfo dynamicMenuSource : dynamicMenuSourceList) {
                        try {
                            IDynamicView sourceObject = (IDynamicView) getClassInstance(target.toString(), dynamicMenuSource.packageName, dynamicMenuSource.className);
                            if (sourceObject == null) {
                                return;
                            }
                            if (isFragment) {
                                sourceObject.onViewCreated(((Fragment) target).getView());
                            } else {
                                sourceObject.onViewCreated(((Activity) target).getWindow().getDecorView());
                            }
                        } catch (ClassCastException exp) {
                            exp.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public static synchronized HashMap<Integer, String> getClassLibPath(String mPackageName) {
        HashMap<Integer, String> hashMap;
        synchronized (InjectionManager.class) {
            if (mLibMap == null && getService() != null) {
                try {
                    mLibMap = (HashMap) getService().getClassLibPath(mPackageName);
                    Log.i(TAG, "Inside getClassLibPath + mLibMap" + mLibMap);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            hashMap = mLibMap;
        }
        return hashMap;
    }

    public static String getClassLibPath(int includeSharedLib) {
        Log.i(TAG, "Inside getClassLibPath caller ");
        if (mLibMap != null) {
            return (String) mLibMap.get(Integer.valueOf(includeSharedLib));
        }
        return null;
    }

    public void dispatchBuildHeader(Object target, List<Header> headers) {
        Log.i(TAG, "dispatchBuildHeader > Target : " + target.getClass().getName());
        if (this.packageHasFeatures) {
            String targetClassName = target.getClass().getName();
            boolean targetHasFeature = this.featureStore.containsKey(targetClassName);
            Log.i(TAG, " target : " + targetClassName + " has feature :" + targetHasFeature);
            if (targetHasFeature) {
                List<FeatureInfo> preferenceHeaderSourceList = (List) ((HashMap) this.featureStore.get(targetClassName)).get(FeatureType.PREFERENCE_HEADER.toString());
                Log.i(TAG, "Preference Header source : " + preferenceHeaderSourceList);
                if (preferenceHeaderSourceList != null && preferenceHeaderSourceList.size() > 0) {
                    for (FeatureInfo preferenceHeaderSource : preferenceHeaderSourceList) {
                        try {
                            IPreferenceHeader sourceObject = (IPreferenceHeader) getClassInstance(target.toString(), preferenceHeaderSource.packageName, preferenceHeaderSource.className);
                            if (sourceObject != null) {
                                sourceObject.onBuildHeader(headers);
                            } else {
                                return;
                            }
                        } catch (ClassCastException exp) {
                            exp.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void dispatchPreferences(Object target) {
        Log.i(TAG, "dispatchPreferences > Target : " + target.getClass().getName());
        if (this.packageHasFeatures) {
            String targetClassName = target.getClass().getName();
            boolean targetHasFeature = this.featureStore.containsKey(targetClassName);
            Log.i(TAG, " target : " + targetClassName + " has feature :" + targetHasFeature);
            if (targetHasFeature) {
                List<FeatureInfo> preferenceSourceList = (List) ((HashMap) this.featureStore.get(targetClassName)).get(FeatureType.PREFERENCE.toString());
                Log.i(TAG, "Preference source : " + preferenceSourceList);
                if (preferenceSourceList != null && preferenceSourceList.size() > 0) {
                    for (FeatureInfo preferenceSource : preferenceSourceList) {
                        try {
                            IPreference sourceObject = (IPreference) getClassInstance(target.toString(), preferenceSource.packageName, preferenceSource.className);
                            if (sourceObject == null) {
                                return;
                            }
                            if (sourceObject instanceof PreferenceActivity) {
                                sourceObject.addPreference((PreferenceActivity) target);
                            } else if (sourceObject instanceof PreferenceFragment) {
                                sourceObject.addPreference((PreferenceFragment) target);
                            }
                        } catch (ClassCastException exp) {
                            exp.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void dispatchParentCallToFeature(DispatchParentCall type, Object targetObject, Configuration config) {
        if (this.packageHasFeatures) {
            String targetClassName = targetObject.getClass().getName();
            if (this.featureStore.containsKey(targetClassName)) {
                Set<String> categoryList = ((HashMap) this.featureStore.get(targetClassName)).keySet();
                if (categoryList != null && !categoryList.isEmpty()) {
                    Log.i(TAG, "categoryList :" + categoryList);
                    List<String> calledList = new ArrayList();
                    for (String category : categoryList) {
                        for (FeatureInfo featureSource : (List) ((HashMap) this.featureStore.get(targetClassName)).get(category)) {
                            String key = targetClassName + featureSource.packageName + featureSource.className;
                            if (!calledList.contains(key)) {
                                calledList.add(key);
                                IInjection sourceObject = getFeatureSourceObject(targetObject.toString(), featureSource.packageName, featureSource.className);
                                if (sourceObject != null) {
                                    switch (AnonymousClass1.$SwitchMap$android$app$im$InjectionConstants$DispatchParentCall[type.ordinal()]) {
                                        case 1:
                                            sourceObject.onConfigurationChanged(config);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void dispatchParentCallToFeature(DispatchParentCall type, String targetClassName, Object targetObject) {
        if (this.packageHasFeatures && this.featureStore.containsKey(targetClassName)) {
            Set<String> categoryList = ((HashMap) this.featureStore.get(targetClassName)).keySet();
            if (categoryList != null && !categoryList.isEmpty()) {
                Log.i(TAG, "categoryList :" + categoryList);
                List<String> calledList = new ArrayList();
                for (String category : categoryList) {
                    for (FeatureInfo featureSource : (List) ((HashMap) this.featureStore.get(targetClassName)).get(category)) {
                        String key = targetClassName + featureSource.packageName + featureSource.className;
                        if (!calledList.contains(key)) {
                            calledList.add(key);
                            IInjection sourceObject = getFeatureSourceObject(targetObject.toString(), featureSource.packageName, featureSource.className);
                            if (sourceObject != null) {
                                switch (AnonymousClass1.$SwitchMap$android$app$im$InjectionConstants$DispatchParentCall[type.ordinal()]) {
                                    case 2:
                                        sourceObject.onStart();
                                        break;
                                    case 3:
                                        sourceObject.onRestart();
                                        break;
                                    case 4:
                                        sourceObject.onStop();
                                        break;
                                    case 5:
                                        sourceObject.onDestroy();
                                        Log.i(TAG, "Removed from feature source store :" + ((IInjection) this.featureSourceStore.remove(targetClassName + featureSource.packageName + featureSource.className)) + "key =" + targetClassName + featureSource.packageName + featureSource.className);
                                        break;
                                    case 6:
                                        sourceObject.onPause();
                                        break;
                                    case 7:
                                        sourceObject.onResume();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean dispatchParentCallToFeature(DispatchParentCall type, Object targetObject) {
        if (!this.packageHasFeatures) {
            return false;
        }
        String targetClassName = targetObject.getClass().getName();
        if (!this.featureStore.containsKey(targetClassName)) {
            return false;
        }
        Set<String> categoryList = ((HashMap) this.featureStore.get(targetClassName)).keySet();
        if (categoryList == null || categoryList.isEmpty()) {
            return false;
        }
        Log.i(TAG, "categoryList :" + categoryList);
        List<String> calledList = new ArrayList();
        for (String category : categoryList) {
            boolean status = false;
            for (FeatureInfo featureSource : (List) ((HashMap) this.featureStore.get(targetClassName)).get(category)) {
                String key = targetClassName + featureSource.packageName + featureSource.className;
                if (!calledList.contains(key)) {
                    calledList.add(key);
                    IInjection sourceObject = getFeatureSourceObject(targetObject.toString(), featureSource.packageName, featureSource.className);
                    if (sourceObject != null) {
                        switch (AnonymousClass1.$SwitchMap$android$app$im$InjectionConstants$DispatchParentCall[type.ordinal()]) {
                            case 8:
                                status = sourceObject.onSearchRequested();
                                break;
                        }
                        if (status && type == DispatchParentCall.ONSEARCHREQUESTED) {
                            return status;
                        }
                    }
                    continue;
                }
            }
        }
        return false;
    }

    public void dispatchParentCallToFeature(DispatchParentCall type, Object targetObject, Bundle state, boolean isFragment) {
        if (this.packageHasFeatures) {
            String targetClassName = targetObject.getClass().getName();
            if (this.featureStore.containsKey(targetClassName)) {
                Set<String> categoryList = ((HashMap) this.featureStore.get(targetClassName)).keySet();
                if (categoryList != null && !categoryList.isEmpty()) {
                    List<String> calledList = new ArrayList();
                    Log.i(TAG, "categoryList :" + categoryList);
                    for (String category : categoryList) {
                        for (FeatureInfo featureSource : (List) ((HashMap) this.featureStore.get(targetClassName)).get(category)) {
                            String key = targetClassName + featureSource.packageName + featureSource.className;
                            if (!calledList.contains(key)) {
                                calledList.add(key);
                                IInjection sourceObject = getFeatureSourceObject(targetObject.toString(), featureSource.packageName, featureSource.className);
                                if (sourceObject != null) {
                                    Activity targetActivity;
                                    if (isFragment) {
                                        targetActivity = ((Fragment) targetObject).getActivity();
                                    } else {
                                        targetActivity = (Activity) targetObject;
                                    }
                                    switch (AnonymousClass1.$SwitchMap$android$app$im$InjectionConstants$DispatchParentCall[type.ordinal()]) {
                                        case 9:
                                            sourceObject.onCreate(targetActivity, getPackageContext(featureSource.packageName), state);
                                            break;
                                        case 10:
                                            sourceObject.onSaveInstanceState(state);
                                            break;
                                        case 11:
                                            if (!isFragment) {
                                                sourceObject.onRestoreInstanceState(state);
                                                break;
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void fillFeatureStoreMap(String packageName) {
        Log.d(TAG, "fillFeatureStoreMap " + packageName);
        try {
            HashMap<String, HashMap<String, List<String>>> packageFeatureInfo = (HashMap) getService().getPackageFeatures(packageName);
            if (packageFeatureInfo == null || packageFeatureInfo.isEmpty()) {
                this.packageHasFeatures = false;
                return;
            }
            this.packageHasFeatures = true;
            for (String activity : packageFeatureInfo.keySet()) {
                Set<String> categorySet = ((HashMap) packageFeatureInfo.get(activity)).keySet();
                HashMap<String, List<FeatureInfo>> activitymap = (HashMap) this.featureStore.get(activity);
                if (activitymap == null) {
                    activitymap = new HashMap();
                    this.featureStore.put(activity, activitymap);
                }
                for (String category : categorySet) {
                    List<String> featureSource = (List) ((HashMap) packageFeatureInfo.get(activity)).get(category);
                    List<FeatureInfo> categorymap = (List) activitymap.get(category);
                    if (categorymap == null) {
                        categorymap = new ArrayList();
                        activitymap.put(category, categorymap);
                    }
                    Log.d(TAG, "featuresource for activty =" + activity + ", category =" + category + " is " + featureSource);
                    for (String st : featureSource) {
                        String[] source = st.split("#");
                        categorymap.add(new FeatureInfo(source[0], source[1]));
                        Log.d(TAG, "Added [ " + activity + "," + category + "," + categorymap);
                    }
                }
            }
            Log.d(TAG, "FeatureStore =" + this.featureStore + ", service details =" + packageFeatureInfo);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private IInjection getFeatureSourceObject(String targetClassName, String featurePackage, String featureClass) {
        Log.e(TAG, "IN getFeatureSourceObject --> targetClassName = " + targetClassName);
        String key = targetClassName + featurePackage + featureClass;
        IInjection sourceObject = (IInjection) this.featureSourceStore.get(key);
        if (sourceObject == null) {
            try {
                sourceObject = getClassInstance(targetClassName, featurePackage, featureClass);
                this.featureSourceStore.put(key, sourceObject);
            } catch (ClassCastException exp) {
                exp.printStackTrace();
                return null;
            }
        }
        return sourceObject;
    }
}
