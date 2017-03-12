package com.android.internal.inputmethod;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Printer;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodSubtype;
import com.android.internal.inputmethod.InputMethodUtils.InputMethodSettings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TreeMap;

public class InputMethodSubtypeSwitchingController {
    private static final boolean DEBUG = false;
    private static final int NOT_A_SUBTYPE_ID = -1;
    private static final String TAG = InputMethodSubtypeSwitchingController.class.getSimpleName();
    private ControllerImpl mController;
    private final InputMethodSettings mSettings;
    private InputMethodAndSubtypeList mSubtypeList;

    public static class ControllerImpl {
        private final DynamicRotationList mSwitchingAwareRotationList;
        private final StaticRotationList mSwitchingUnawareRotationList;

        public static ControllerImpl createFrom(ControllerImpl currentInstance, List<ImeSubtypeListItem> sortedEnabledItems) {
            DynamicRotationList switchingAwareRotationList = null;
            List<ImeSubtypeListItem> switchingAwareImeSubtypes = filterImeSubtypeList(sortedEnabledItems, true);
            if (!(currentInstance == null || currentInstance.mSwitchingAwareRotationList == null || !Objects.equals(currentInstance.mSwitchingAwareRotationList.mImeSubtypeList, switchingAwareImeSubtypes))) {
                switchingAwareRotationList = currentInstance.mSwitchingAwareRotationList;
            }
            if (switchingAwareRotationList == null) {
                switchingAwareRotationList = new DynamicRotationList(switchingAwareImeSubtypes);
            }
            StaticRotationList switchingUnawareRotationList = null;
            List<ImeSubtypeListItem> switchingUnawareImeSubtypes = filterImeSubtypeList(sortedEnabledItems, false);
            if (!(currentInstance == null || currentInstance.mSwitchingUnawareRotationList == null || !Objects.equals(currentInstance.mSwitchingUnawareRotationList.mImeSubtypeList, switchingUnawareImeSubtypes))) {
                switchingUnawareRotationList = currentInstance.mSwitchingUnawareRotationList;
            }
            if (switchingUnawareRotationList == null) {
                switchingUnawareRotationList = new StaticRotationList(switchingUnawareImeSubtypes);
            }
            return new ControllerImpl(switchingAwareRotationList, switchingUnawareRotationList);
        }

        private ControllerImpl(DynamicRotationList switchingAwareRotationList, StaticRotationList switchingUnawareRotationList) {
            this.mSwitchingAwareRotationList = switchingAwareRotationList;
            this.mSwitchingUnawareRotationList = switchingUnawareRotationList;
        }

        public ImeSubtypeListItem getNextInputMethod(boolean onlyCurrentIme, InputMethodInfo imi, InputMethodSubtype subtype) {
            if (imi == null) {
                return null;
            }
            if (imi.supportsSwitchingToNextInputMethod()) {
                return this.mSwitchingAwareRotationList.getNextInputMethodLocked(onlyCurrentIme, imi, subtype);
            }
            return this.mSwitchingUnawareRotationList.getNextInputMethodLocked(onlyCurrentIme, imi, subtype);
        }

        public void onUserActionLocked(InputMethodInfo imi, InputMethodSubtype subtype) {
            if (imi != null && imi.supportsSwitchingToNextInputMethod()) {
                this.mSwitchingAwareRotationList.onUserAction(imi, subtype);
            }
        }

        private static List<ImeSubtypeListItem> filterImeSubtypeList(List<ImeSubtypeListItem> items, boolean supportsSwitchingToNextInputMethod) {
            ArrayList<ImeSubtypeListItem> result = new ArrayList();
            int ALL_ITEMS_COUNT = items.size();
            for (int i = 0; i < ALL_ITEMS_COUNT; i++) {
                ImeSubtypeListItem item = (ImeSubtypeListItem) items.get(i);
                if (item.mImi.supportsSwitchingToNextInputMethod() == supportsSwitchingToNextInputMethod) {
                    result.add(item);
                }
            }
            return result;
        }

        protected void dump(Printer pw) {
            pw.println("    mSwitchingAwareRotationList:");
            this.mSwitchingAwareRotationList.dump(pw, "      ");
            pw.println("    mSwitchingUnawareRotationList:");
            this.mSwitchingUnawareRotationList.dump(pw, "      ");
        }
    }

    private static class DynamicRotationList {
        private static final String TAG = DynamicRotationList.class.getSimpleName();
        private final List<ImeSubtypeListItem> mImeSubtypeList;
        private final int[] mUsageHistoryOfSubtypeListItemIndex;

        private DynamicRotationList(List<ImeSubtypeListItem> imeSubtypeListItems) {
            this.mImeSubtypeList = imeSubtypeListItems;
            this.mUsageHistoryOfSubtypeListItemIndex = new int[this.mImeSubtypeList.size()];
            int N = this.mImeSubtypeList.size();
            for (int i = 0; i < N; i++) {
                this.mUsageHistoryOfSubtypeListItemIndex[i] = i;
            }
        }

        private int getUsageRank(InputMethodInfo imi, InputMethodSubtype subtype) {
            int currentSubtypeId = InputMethodSubtypeSwitchingController.calculateSubtypeId(imi, subtype);
            int N = this.mUsageHistoryOfSubtypeListItemIndex.length;
            for (int usageRank = 0; usageRank < N; usageRank++) {
                ImeSubtypeListItem subtypeListItem = (ImeSubtypeListItem) this.mImeSubtypeList.get(this.mUsageHistoryOfSubtypeListItemIndex[usageRank]);
                if (subtypeListItem.mImi.equals(imi) && subtypeListItem.mSubtypeId == currentSubtypeId) {
                    return usageRank;
                }
            }
            return -1;
        }

        public void onUserAction(InputMethodInfo imi, InputMethodSubtype subtype) {
            int currentUsageRank = getUsageRank(imi, subtype);
            if (currentUsageRank > 0) {
                int currentItemIndex = this.mUsageHistoryOfSubtypeListItemIndex[currentUsageRank];
                System.arraycopy(this.mUsageHistoryOfSubtypeListItemIndex, 0, this.mUsageHistoryOfSubtypeListItemIndex, 1, currentUsageRank);
                this.mUsageHistoryOfSubtypeListItemIndex[0] = currentItemIndex;
            }
        }

        public ImeSubtypeListItem getNextInputMethodLocked(boolean onlyCurrentIme, InputMethodInfo imi, InputMethodSubtype subtype) {
            int currentUsageRank = getUsageRank(imi, subtype);
            if (currentUsageRank < 0) {
                return null;
            }
            int N = this.mUsageHistoryOfSubtypeListItemIndex.length;
            for (int i = 1; i < N; i++) {
                ImeSubtypeListItem subtypeListItem = (ImeSubtypeListItem) this.mImeSubtypeList.get(this.mUsageHistoryOfSubtypeListItemIndex[(currentUsageRank + i) % N]);
                if (!onlyCurrentIme || imi.equals(subtypeListItem.mImi)) {
                    return subtypeListItem;
                }
            }
            return null;
        }

        protected void dump(Printer pw, String prefix) {
            for (int i = 0; i < this.mUsageHistoryOfSubtypeListItemIndex.length; i++) {
                ImeSubtypeListItem item = (ImeSubtypeListItem) this.mImeSubtypeList.get(i);
                pw.println(prefix + "rank=" + this.mUsageHistoryOfSubtypeListItemIndex[i] + " item=" + item);
            }
        }
    }

    public static class ImeSubtypeListItem implements Comparable<ImeSubtypeListItem> {
        public final CharSequence mImeName;
        public final InputMethodInfo mImi;
        public final boolean mIsSystemLanguage;
        public final boolean mIsSystemLocale;
        public final int mSubtypeId;
        public final CharSequence mSubtypeName;

        public ImeSubtypeListItem(CharSequence imeName, CharSequence subtypeName, InputMethodInfo imi, int subtypeId, String subtypeLocale, String systemLocale) {
            boolean z = true;
            this.mImeName = imeName;
            this.mSubtypeName = subtypeName;
            this.mImi = imi;
            this.mSubtypeId = subtypeId;
            if (TextUtils.isEmpty(subtypeLocale)) {
                this.mIsSystemLocale = false;
                this.mIsSystemLanguage = false;
                return;
            }
            this.mIsSystemLocale = subtypeLocale.equals(systemLocale);
            if (this.mIsSystemLocale) {
                this.mIsSystemLanguage = true;
                return;
            }
            String systemLanguage = parseLanguageFromLocaleString(systemLocale);
            String subtypeLanguage = parseLanguageFromLocaleString(subtypeLocale);
            if (systemLanguage.length() < 2 || !systemLanguage.equals(subtypeLanguage)) {
                z = false;
            }
            this.mIsSystemLanguage = z;
        }

        private static String parseLanguageFromLocaleString(String locale) {
            int idx = locale.indexOf(95);
            return idx < 0 ? locale : locale.substring(0, idx);
        }

        public int compareTo(ImeSubtypeListItem other) {
            if (TextUtils.isEmpty(this.mImeName)) {
                return 1;
            }
            if (TextUtils.isEmpty(other.mImeName)) {
                return -1;
            }
            if (!TextUtils.equals(this.mImeName, other.mImeName)) {
                return this.mImeName.toString().compareTo(other.mImeName.toString());
            }
            if (TextUtils.equals(this.mSubtypeName, other.mSubtypeName)) {
                return 0;
            }
            if (this.mIsSystemLocale) {
                return -1;
            }
            if (other.mIsSystemLocale) {
                return 1;
            }
            if (this.mIsSystemLanguage) {
                return -1;
            }
            if (other.mIsSystemLanguage || TextUtils.isEmpty(this.mSubtypeName)) {
                return 1;
            }
            if (TextUtils.isEmpty(other.mSubtypeName)) {
                return -1;
            }
            return this.mSubtypeName.toString().compareTo(other.mSubtypeName.toString());
        }

        public String toString() {
            return "ImeSubtypeListItem{mImeName=" + this.mImeName + " mSubtypeName=" + this.mSubtypeName + " mSubtypeId=" + this.mSubtypeId + " mIsSystemLocale=" + this.mIsSystemLocale + " mIsSystemLanguage=" + this.mIsSystemLanguage + "}";
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof ImeSubtypeListItem)) {
                return false;
            }
            ImeSubtypeListItem that = (ImeSubtypeListItem) o;
            if (!Objects.equals(this.mImi, that.mImi)) {
                return false;
            }
            if (this.mSubtypeId != that.mSubtypeId) {
                return false;
            }
            return true;
        }
    }

    private static class InputMethodAndSubtypeList {
        private final Context mContext;
        private final PackageManager mPm;
        private final InputMethodSettings mSettings;
        private final TreeMap<InputMethodInfo, List<InputMethodSubtype>> mSortedImmis = new TreeMap(new Comparator<InputMethodInfo>() {
            public int compare(InputMethodInfo imi1, InputMethodInfo imi2) {
                if (imi2 == null) {
                    return 0;
                }
                if (imi1 == null) {
                    return 1;
                }
                if (InputMethodAndSubtypeList.this.mPm == null) {
                    return imi1.getId().compareTo(imi2.getId());
                }
                return (imi1.loadLabel(InputMethodAndSubtypeList.this.mPm) + "/" + imi1.getId()).toString().compareTo((imi2.loadLabel(InputMethodAndSubtypeList.this.mPm) + "/" + imi2.getId()).toString());
            }
        });
        private final String mSystemLocaleStr;

        public InputMethodAndSubtypeList(Context context, InputMethodSettings settings) {
            this.mContext = context;
            this.mSettings = settings;
            this.mPm = context.getPackageManager();
            Locale locale = context.getResources().getConfiguration().locale;
            this.mSystemLocaleStr = locale != null ? locale.toString() : "";
        }

        public List<ImeSubtypeListItem> getSortedInputMethodAndSubtypeList() {
            return getSortedInputMethodAndSubtypeList(true, false, false);
        }

        public List<ImeSubtypeListItem> getSortedInputMethodAndSubtypeList(boolean showSubtypes, boolean includeAuxiliarySubtypes, boolean isScreenLocked) {
            ArrayList<ImeSubtypeListItem> imList = new ArrayList();
            HashMap<InputMethodInfo, List<InputMethodSubtype>> immis = this.mSettings.getExplicitlyOrImplicitlyEnabledInputMethodsAndSubtypeListLocked(this.mContext);
            if (immis == null || immis.size() == 0) {
                return Collections.emptyList();
            }
            if (isScreenLocked && includeAuxiliarySubtypes) {
                includeAuxiliarySubtypes = false;
            }
            this.mSortedImmis.clear();
            this.mSortedImmis.putAll(immis);
            for (InputMethodInfo imi : this.mSortedImmis.keySet()) {
                if (imi != null) {
                    InputMethodSubtype subtype;
                    List<InputMethodSubtype> explicitlyOrImplicitlyEnabledSubtypeList = (List) immis.get(imi);
                    HashSet<String> enabledSubtypeSet = new HashSet();
                    for (InputMethodSubtype subtype2 : explicitlyOrImplicitlyEnabledSubtypeList) {
                        enabledSubtypeSet.add(String.valueOf(subtype2.hashCode()));
                    }
                    CharSequence imeLabel = imi.loadLabel(this.mPm);
                    if (!showSubtypes || enabledSubtypeSet.size() <= 0) {
                        ArrayList<ImeSubtypeListItem> arrayList = imList;
                        arrayList.add(new ImeSubtypeListItem(imeLabel, null, imi, -1, null, this.mSystemLocaleStr));
                    } else {
                        int subtypeCount = imi.getSubtypeCount();
                        for (int j = 0; j < subtypeCount; j++) {
                            subtype2 = imi.getSubtypeAt(j);
                            String subtypeHashCode = String.valueOf(subtype2.hashCode());
                            if (enabledSubtypeSet.contains(subtypeHashCode) && (includeAuxiliarySubtypes || !subtype2.isAuxiliary())) {
                                CharSequence subtypeLabel;
                                if (subtype2.overridesImplicitlyEnabledSubtype()) {
                                    subtypeLabel = null;
                                } else {
                                    subtypeLabel = subtype2.getDisplayName(this.mContext, imi.getPackageName(), imi.getServiceInfo().applicationInfo);
                                }
                                imList.add(new ImeSubtypeListItem(imeLabel, subtypeLabel, imi, j, subtype2.getLocale(), this.mSystemLocaleStr));
                                enabledSubtypeSet.remove(subtypeHashCode);
                            }
                        }
                    }
                }
            }
            Collections.sort(imList);
            return imList;
        }
    }

    private static class StaticRotationList {
        private final List<ImeSubtypeListItem> mImeSubtypeList;

        public StaticRotationList(List<ImeSubtypeListItem> imeSubtypeList) {
            this.mImeSubtypeList = imeSubtypeList;
        }

        private int getIndex(InputMethodInfo imi, InputMethodSubtype subtype) {
            int currentSubtypeId = InputMethodSubtypeSwitchingController.calculateSubtypeId(imi, subtype);
            int N = this.mImeSubtypeList.size();
            for (int i = 0; i < N; i++) {
                ImeSubtypeListItem isli = (ImeSubtypeListItem) this.mImeSubtypeList.get(i);
                if (imi.equals(isli.mImi) && isli.mSubtypeId == currentSubtypeId) {
                    return i;
                }
            }
            return -1;
        }

        public ImeSubtypeListItem getNextInputMethodLocked(boolean onlyCurrentIme, InputMethodInfo imi, InputMethodSubtype subtype) {
            if (imi == null) {
                return null;
            }
            if (this.mImeSubtypeList.size() <= 1) {
                return null;
            }
            int currentIndex = getIndex(imi, subtype);
            if (currentIndex < 0) {
                return null;
            }
            int N = this.mImeSubtypeList.size();
            for (int offset = 1; offset < N; offset++) {
                ImeSubtypeListItem candidate = (ImeSubtypeListItem) this.mImeSubtypeList.get((currentIndex + offset) % N);
                if (!onlyCurrentIme || imi.equals(candidate.mImi)) {
                    return candidate;
                }
            }
            return null;
        }

        protected void dump(Printer pw, String prefix) {
            int N = this.mImeSubtypeList.size();
            for (int i = 0; i < N; i++) {
                ImeSubtypeListItem item = (ImeSubtypeListItem) this.mImeSubtypeList.get(i);
                pw.println(prefix + "rank=" + i + " item=" + item);
            }
        }
    }

    private static int calculateSubtypeId(InputMethodInfo imi, InputMethodSubtype subtype) {
        return subtype != null ? InputMethodUtils.getSubtypeIdFromHashCode(imi, subtype.hashCode()) : -1;
    }

    private InputMethodSubtypeSwitchingController(InputMethodSettings settings, Context context) {
        this.mSettings = settings;
        resetCircularListLocked(context);
    }

    public static InputMethodSubtypeSwitchingController createInstanceLocked(InputMethodSettings settings, Context context) {
        return new InputMethodSubtypeSwitchingController(settings, context);
    }

    public void onUserActionLocked(InputMethodInfo imi, InputMethodSubtype subtype) {
        if (this.mController != null) {
            this.mController.onUserActionLocked(imi, subtype);
        }
    }

    public void resetCircularListLocked(Context context) {
        this.mSubtypeList = new InputMethodAndSubtypeList(context, this.mSettings);
        this.mController = ControllerImpl.createFrom(this.mController, this.mSubtypeList.getSortedInputMethodAndSubtypeList());
    }

    public ImeSubtypeListItem getNextInputMethodLocked(boolean onlyCurrentIme, InputMethodInfo imi, InputMethodSubtype subtype) {
        if (this.mController == null) {
            return null;
        }
        return this.mController.getNextInputMethod(onlyCurrentIme, imi, subtype);
    }

    public List<ImeSubtypeListItem> getSortedInputMethodAndSubtypeListLocked(boolean showSubtypes, boolean includingAuxiliarySubtypes, boolean isScreenLocked) {
        return this.mSubtypeList.getSortedInputMethodAndSubtypeList(showSubtypes, includingAuxiliarySubtypes, isScreenLocked);
    }

    public void dump(Printer pw) {
        if (this.mController != null) {
            this.mController.dump(pw);
        } else {
            pw.println("    mController=null");
        }
    }
}
