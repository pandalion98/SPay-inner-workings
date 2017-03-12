package com.android.internal.inputmethod;

import android.app.AppOpsManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import android.text.TextUtils.SimpleStringSplitter;
import android.util.Pair;
import android.util.Slog;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodSubtype;
import android.view.textservice.SpellCheckerInfo;
import android.view.textservice.TextServicesManager;
import com.android.internal.R;
import com.android.internal.os.PowerProfile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

public class InputMethodUtils {
    private static String CONTRY_CODE = PowerProfile.POWER_NONE;
    public static final boolean DEBUG = false;
    private static final Locale ENGLISH_LOCALE = new Locale("en");
    private static final Locale LOCALE_EN_GB = new Locale("en", "GB");
    private static final Locale LOCALE_EN_US = new Locale("en", "US");
    public static final int NOT_A_SUBTYPE_ID = -1;
    private static final String NOT_A_SUBTYPE_ID_STR = String.valueOf(-1);
    private static final Locale[] SEARCH_ORDER_OF_FALLBACK_LOCALES = new Locale[]{Locale.ENGLISH, Locale.US, Locale.UK};
    public static final String SUBTYPE_MODE_ANY = null;
    public static final String SUBTYPE_MODE_KEYBOARD = "keyboard";
    public static final String SUBTYPE_MODE_VOICE = "voice";
    private static final String TAG = "InputMethodUtils";
    private static final String TAG_ASCII_CAPABLE = "AsciiCapable";
    private static final String TAG_ENABLED_WHEN_DEFAULT_IS_NOT_ASCII_CAPABLE = "EnabledWhenDefaultIsNotAsciiCapable";

    private static final class InputMethodListBuilder {
        private final LinkedHashSet<InputMethodInfo> mInputMethodSet;

        private InputMethodListBuilder() {
            this.mInputMethodSet = new LinkedHashSet();
        }

        public InputMethodListBuilder fillImes(ArrayList<InputMethodInfo> imis, Context context, boolean checkDefaultAttribute, Locale locale, boolean checkCountry, String requiredSubtypeMode) {
            for (int i = 0; i < imis.size(); i++) {
                InputMethodInfo imi = (InputMethodInfo) imis.get(i);
                if (InputMethodUtils.isSystemImeThatHasSubtypeOf(imi, context, checkDefaultAttribute, locale, checkCountry, requiredSubtypeMode)) {
                    this.mInputMethodSet.add(imi);
                }
            }
            return this;
        }

        public InputMethodListBuilder fillAuxiliaryImes(ArrayList<InputMethodInfo> imis, Context context) {
            int i;
            Iterator i$ = this.mInputMethodSet.iterator();
            while (i$.hasNext()) {
                if (((InputMethodInfo) i$.next()).isAuxiliaryIme()) {
                    break;
                }
            }
            boolean added = false;
            for (i = 0; i < imis.size(); i++) {
                InputMethodInfo imi = (InputMethodInfo) imis.get(i);
                if (InputMethodUtils.isSystemAuxilialyImeThatHasAutomaticSubtype(imi, context, true)) {
                    this.mInputMethodSet.add(imi);
                    added = true;
                }
            }
            if (!added) {
                for (i = 0; i < imis.size(); i++) {
                    imi = (InputMethodInfo) imis.get(i);
                    if (InputMethodUtils.isSystemAuxilialyImeThatHasAutomaticSubtype(imi, context, false)) {
                        this.mInputMethodSet.add(imi);
                    }
                }
            }
            return this;
        }

        public boolean isEmpty() {
            return this.mInputMethodSet.isEmpty();
        }

        public ArrayList<InputMethodInfo> build() {
            return new ArrayList(this.mInputMethodSet);
        }
    }

    public static class InputMethodSettings {
        private static final char INPUT_METHOD_SEPARATER = ':';
        private static final char INPUT_METHOD_SUBTYPE_SEPARATER = ';';
        private int[] mCurrentProfileIds = new int[0];
        private int mCurrentUserId;
        private String mEnabledInputMethodsStrCache;
        private final SimpleStringSplitter mInputMethodSplitter = new SimpleStringSplitter(INPUT_METHOD_SEPARATER);
        private final ArrayList<InputMethodInfo> mMethodList;
        private final HashMap<String, InputMethodInfo> mMethodMap;
        private final Resources mRes;
        private final ContentResolver mResolver;
        private final SimpleStringSplitter mSubtypeSplitter = new SimpleStringSplitter(';');

        private static void buildEnabledInputMethodsSettingString(StringBuilder builder, Pair<String, ArrayList<String>> ime) {
            builder.append((String) ime.first);
            Iterator i$ = ((ArrayList) ime.second).iterator();
            while (i$.hasNext()) {
                builder.append(';').append((String) i$.next());
            }
        }

        public static String buildInputMethodsSettingString(List<Pair<String, ArrayList<String>>> allImeSettingsMap) {
            StringBuilder b = new StringBuilder();
            boolean needsSeparator = false;
            for (Pair<String, ArrayList<String>> ime : allImeSettingsMap) {
                if (needsSeparator) {
                    b.append(INPUT_METHOD_SEPARATER);
                }
                buildEnabledInputMethodsSettingString(b, ime);
                needsSeparator = true;
            }
            return b.toString();
        }

        public static List<Pair<String, ArrayList<String>>> buildInputMethodsAndSubtypeList(String enabledInputMethodsStr, SimpleStringSplitter inputMethodSplitter, SimpleStringSplitter subtypeSplitter) {
            ArrayList<Pair<String, ArrayList<String>>> imsList = new ArrayList();
            if (!TextUtils.isEmpty(enabledInputMethodsStr)) {
                inputMethodSplitter.setString(enabledInputMethodsStr);
                while (inputMethodSplitter.hasNext()) {
                    subtypeSplitter.setString(inputMethodSplitter.next());
                    if (subtypeSplitter.hasNext()) {
                        ArrayList<String> subtypeHashes = new ArrayList();
                        String imeId = subtypeSplitter.next();
                        while (subtypeSplitter.hasNext()) {
                            subtypeHashes.add(subtypeSplitter.next());
                        }
                        imsList.add(new Pair(imeId, subtypeHashes));
                    }
                }
            }
            return imsList;
        }

        public InputMethodSettings(Resources res, ContentResolver resolver, HashMap<String, InputMethodInfo> methodMap, ArrayList<InputMethodInfo> methodList, int userId) {
            setCurrentUserId(userId);
            this.mRes = res;
            this.mResolver = resolver;
            this.mMethodMap = methodMap;
            this.mMethodList = methodList;
        }

        public void setCurrentUserId(int userId) {
            this.mCurrentUserId = userId;
        }

        public void setCurrentProfileIds(int[] currentProfileIds) {
            synchronized (this) {
                this.mCurrentProfileIds = currentProfileIds;
            }
        }

        public boolean isCurrentProfile(int userId) {
            boolean z = true;
            synchronized (this) {
                if (userId == this.mCurrentUserId) {
                } else {
                    for (int i : this.mCurrentProfileIds) {
                        if (userId == i) {
                            break;
                        }
                    }
                    z = false;
                }
            }
            return z;
        }

        public List<InputMethodInfo> getEnabledInputMethodListLocked() {
            return createEnabledInputMethodListLocked(getEnabledInputMethodsAndSubtypeListLocked());
        }

        public List<Pair<InputMethodInfo, ArrayList<String>>> getEnabledInputMethodAndSubtypeHashCodeListLocked() {
            return createEnabledInputMethodAndSubtypeHashCodeListLocked(getEnabledInputMethodsAndSubtypeListLocked());
        }

        public List<InputMethodSubtype> getEnabledInputMethodSubtypeListLocked(Context context, InputMethodInfo imi, boolean allowsImplicitlySelectedSubtypes) {
            List<InputMethodSubtype> enabledSubtypes = getEnabledInputMethodSubtypeListLocked(imi);
            if (allowsImplicitlySelectedSubtypes && enabledSubtypes.isEmpty()) {
                enabledSubtypes = InputMethodUtils.getImplicitlyApplicableSubtypesLocked(context.getResources(), imi);
            }
            return InputMethodSubtype.sort(context, 0, imi, enabledSubtypes);
        }

        public List<InputMethodSubtype> getEnabledInputMethodSubtypeListLocked(InputMethodInfo imi) {
            List<Pair<String, ArrayList<String>>> imsList = getEnabledInputMethodsAndSubtypeListLocked();
            ArrayList<InputMethodSubtype> enabledSubtypes = new ArrayList();
            if (imi != null) {
                Iterator i$;
                for (Pair<String, ArrayList<String>> imsPair : imsList) {
                    InputMethodInfo info = (InputMethodInfo) this.mMethodMap.get(imsPair.first);
                    if (info != null && info.getId().equals(imi.getId())) {
                        int subtypeCount = info.getSubtypeCount();
                        for (int i = 0; i < subtypeCount; i++) {
                            InputMethodSubtype ims = info.getSubtypeAt(i);
                            i$ = ((ArrayList) imsPair.second).iterator();
                            while (i$.hasNext()) {
                                if (String.valueOf(ims.hashCode()).equals((String) i$.next())) {
                                    enabledSubtypes.add(ims);
                                }
                            }
                        }
                    }
                }
            }
            return enabledSubtypes;
        }

        public void enableAllIMEsIfThereIsNoEnabledIME() {
            if (TextUtils.isEmpty(getEnabledInputMethodsStr())) {
                StringBuilder sb = new StringBuilder();
                int N = this.mMethodList.size();
                for (int i = 0; i < N; i++) {
                    InputMethodInfo imi = (InputMethodInfo) this.mMethodList.get(i);
                    Slog.i(InputMethodUtils.TAG, "Adding: " + imi.getId());
                    if (i > 0) {
                        sb.append(INPUT_METHOD_SEPARATER);
                    }
                    sb.append(imi.getId());
                }
                putEnabledInputMethodsStr(sb.toString());
            }
        }

        public List<Pair<String, ArrayList<String>>> getEnabledInputMethodsAndSubtypeListLocked() {
            return buildInputMethodsAndSubtypeList(getEnabledInputMethodsStr(), this.mInputMethodSplitter, this.mSubtypeSplitter);
        }

        public void appendAndPutEnabledInputMethodLocked(String id, boolean reloadInputMethodStr) {
            if (reloadInputMethodStr) {
                getEnabledInputMethodsStr();
            }
            if (TextUtils.isEmpty(this.mEnabledInputMethodsStrCache)) {
                putEnabledInputMethodsStr(id);
            } else {
                putEnabledInputMethodsStr(this.mEnabledInputMethodsStrCache + INPUT_METHOD_SEPARATER + id);
            }
        }

        public boolean buildAndPutEnabledInputMethodsStrRemovingIdLocked(StringBuilder builder, List<Pair<String, ArrayList<String>>> imsList, String id) {
            boolean isRemoved = false;
            boolean needsAppendSeparator = false;
            for (Pair<String, ArrayList<String>> ims : imsList) {
                if (ims.first.equals(id)) {
                    isRemoved = true;
                } else {
                    if (needsAppendSeparator) {
                        builder.append(INPUT_METHOD_SEPARATER);
                    } else {
                        needsAppendSeparator = true;
                    }
                    buildEnabledInputMethodsSettingString(builder, ims);
                }
            }
            if (isRemoved) {
                putEnabledInputMethodsStr(builder.toString());
            }
            return isRemoved;
        }

        private List<InputMethodInfo> createEnabledInputMethodListLocked(List<Pair<String, ArrayList<String>>> imsList) {
            ArrayList<InputMethodInfo> res = new ArrayList();
            for (Pair<String, ArrayList<String>> ims : imsList) {
                InputMethodInfo info = (InputMethodInfo) this.mMethodMap.get(ims.first);
                if (info != null) {
                    res.add(info);
                }
            }
            return res;
        }

        private List<Pair<InputMethodInfo, ArrayList<String>>> createEnabledInputMethodAndSubtypeHashCodeListLocked(List<Pair<String, ArrayList<String>>> imsList) {
            ArrayList<Pair<InputMethodInfo, ArrayList<String>>> res = new ArrayList();
            for (Pair<String, ArrayList<String>> ims : imsList) {
                InputMethodInfo info = (InputMethodInfo) this.mMethodMap.get(ims.first);
                if (info != null) {
                    res.add(new Pair(info, ims.second));
                }
            }
            return res;
        }

        private void putEnabledInputMethodsStr(String str) {
            Secure.putStringForUser(this.mResolver, "enabled_input_methods", str, this.mCurrentUserId);
            this.mEnabledInputMethodsStrCache = str;
        }

        public String getEnabledInputMethodsStr() {
            this.mEnabledInputMethodsStrCache = Secure.getStringForUser(this.mResolver, "enabled_input_methods", this.mCurrentUserId);
            return this.mEnabledInputMethodsStrCache;
        }

        private void saveSubtypeHistory(List<Pair<String, String>> savedImes, String newImeId, String newSubtypeId) {
            StringBuilder builder = new StringBuilder();
            boolean isImeAdded = false;
            if (!(TextUtils.isEmpty(newImeId) || TextUtils.isEmpty(newSubtypeId))) {
                builder.append(newImeId).append(';').append(newSubtypeId);
                isImeAdded = true;
            }
            for (Pair<String, String> ime : savedImes) {
                String imeId = ime.first;
                String subtypeId = ime.second;
                if (TextUtils.isEmpty(subtypeId)) {
                    subtypeId = InputMethodUtils.NOT_A_SUBTYPE_ID_STR;
                }
                if (isImeAdded) {
                    builder.append(INPUT_METHOD_SEPARATER);
                } else {
                    isImeAdded = true;
                }
                builder.append(imeId).append(';').append(subtypeId);
            }
            putSubtypeHistoryStr(builder.toString());
        }

        private void addSubtypeToHistory(String imeId, String subtypeId) {
            List<Pair<String, String>> subtypeHistory = loadInputMethodAndSubtypeHistoryLocked();
            for (Pair<String, String> ime : subtypeHistory) {
                if (((String) ime.first).equals(imeId)) {
                    subtypeHistory.remove(ime);
                    break;
                }
            }
            saveSubtypeHistory(subtypeHistory, imeId, subtypeId);
        }

        private void putSubtypeHistoryStr(String str) {
            Secure.putStringForUser(this.mResolver, "input_methods_subtype_history", str, this.mCurrentUserId);
        }

        public Pair<String, String> getLastInputMethodAndSubtypeLocked() {
            return getLastSubtypeForInputMethodLockedInternal(null);
        }

        public String getLastSubtypeForInputMethodLocked(String imeId) {
            Pair<String, String> ime = getLastSubtypeForInputMethodLockedInternal(imeId);
            if (ime != null) {
                return (String) ime.second;
            }
            return null;
        }

        private Pair<String, String> getLastSubtypeForInputMethodLockedInternal(String imeId) {
            List<Pair<String, ArrayList<String>>> enabledImes = getEnabledInputMethodsAndSubtypeListLocked();
            for (Pair<String, String> imeAndSubtype : loadInputMethodAndSubtypeHistoryLocked()) {
                String imeInTheHistory = imeAndSubtype.first;
                if (TextUtils.isEmpty(imeId) || imeInTheHistory.equals(imeId)) {
                    String subtypeHashCode = getEnabledSubtypeHashCodeForInputMethodAndSubtypeLocked(enabledImes, imeInTheHistory, imeAndSubtype.second);
                    if (!TextUtils.isEmpty(subtypeHashCode)) {
                        return new Pair(imeInTheHistory, subtypeHashCode);
                    }
                }
            }
            return null;
        }

        private String getEnabledSubtypeHashCodeForInputMethodAndSubtypeLocked(List<Pair<String, ArrayList<String>>> enabledImes, String imeId, String subtypeHashCode) {
            Iterator i$;
            for (Pair<String, ArrayList<String>> enabledIme : enabledImes) {
                if (((String) enabledIme.first).equals(imeId)) {
                    ArrayList<String> explicitlyEnabledSubtypes = enabledIme.second;
                    InputMethodInfo imi = (InputMethodInfo) this.mMethodMap.get(imeId);
                    if (explicitlyEnabledSubtypes.size() != 0) {
                        i$ = explicitlyEnabledSubtypes.iterator();
                        while (i$.hasNext()) {
                            String s = (String) i$.next();
                            if (s.equals(subtypeHashCode)) {
                                try {
                                    if (InputMethodUtils.isValidSubtypeId(imi, Integer.valueOf(subtypeHashCode).intValue())) {
                                        return s;
                                    }
                                    return InputMethodUtils.NOT_A_SUBTYPE_ID_STR;
                                } catch (NumberFormatException e) {
                                    return InputMethodUtils.NOT_A_SUBTYPE_ID_STR;
                                }
                            }
                        }
                    } else if (imi != null && imi.getSubtypeCount() > 0) {
                        List<InputMethodSubtype> implicitlySelectedSubtypes = InputMethodUtils.getImplicitlyApplicableSubtypesLocked(this.mRes, imi);
                        if (implicitlySelectedSubtypes != null) {
                            int N = implicitlySelectedSubtypes.size();
                            for (int i = 0; i < N; i++) {
                                if (String.valueOf(((InputMethodSubtype) implicitlySelectedSubtypes.get(i)).hashCode()).equals(subtypeHashCode)) {
                                    return subtypeHashCode;
                                }
                            }
                        }
                    }
                    return InputMethodUtils.NOT_A_SUBTYPE_ID_STR;
                }
            }
            return null;
        }

        private List<Pair<String, String>> loadInputMethodAndSubtypeHistoryLocked() {
            ArrayList<Pair<String, String>> imsList = new ArrayList();
            String subtypeHistoryStr = getSubtypeHistoryStr();
            if (!TextUtils.isEmpty(subtypeHistoryStr)) {
                this.mInputMethodSplitter.setString(subtypeHistoryStr);
                while (this.mInputMethodSplitter.hasNext()) {
                    this.mSubtypeSplitter.setString(this.mInputMethodSplitter.next());
                    if (this.mSubtypeSplitter.hasNext()) {
                        String subtypeId = InputMethodUtils.NOT_A_SUBTYPE_ID_STR;
                        String imeId = this.mSubtypeSplitter.next();
                        if (this.mSubtypeSplitter.hasNext()) {
                            subtypeId = this.mSubtypeSplitter.next();
                        }
                        imsList.add(new Pair(imeId, subtypeId));
                    }
                }
            }
            return imsList;
        }

        private String getSubtypeHistoryStr() {
            return Secure.getStringForUser(this.mResolver, "input_methods_subtype_history", this.mCurrentUserId);
        }

        public void putSelectedInputMethod(String imeId) {
            Secure.putStringForUser(this.mResolver, "default_input_method", imeId, this.mCurrentUserId);
        }

        public void putSelectedSubtype(int subtypeId) {
            Secure.putIntForUser(this.mResolver, "selected_input_method_subtype", subtypeId, this.mCurrentUserId);
        }

        public String getDisabledSystemInputMethods() {
            return Secure.getStringForUser(this.mResolver, "disabled_system_input_methods", this.mCurrentUserId);
        }

        public String getSelectedInputMethod() {
            return Secure.getStringForUser(this.mResolver, "default_input_method", this.mCurrentUserId);
        }

        public boolean isSubtypeSelected() {
            return getSelectedInputMethodSubtypeHashCode() != -1;
        }

        private int getSelectedInputMethodSubtypeHashCode() {
            try {
                return Secure.getIntForUser(this.mResolver, "selected_input_method_subtype", this.mCurrentUserId);
            } catch (SettingNotFoundException e) {
                return -1;
            }
        }

        public boolean isShowImeWithHardKeyboardEnabled() {
            String deviceType = SystemProperties.get("ro.build.characteristics");
            int defaultValue = 1;
            if (deviceType != null && deviceType.contains("tablet")) {
                defaultValue = 0;
            }
            if (this.mCurrentUserId == 0) {
                if (Secure.getIntForUser(this.mResolver, "show_ime_with_hard_keyboard", defaultValue, 0) == 1) {
                    return true;
                }
                return false;
            } else if (Secure.getIntForUser(this.mResolver, "show_ime_with_hard_keyboard", defaultValue, this.mCurrentUserId) != 1) {
                return false;
            } else {
                return true;
            }
        }

        public void setShowImeWithHardKeyboard(boolean show) {
            Secure.putIntForUser(this.mResolver, "show_ime_with_hard_keyboard", show ? 1 : 0, this.mCurrentUserId);
        }

        public int getCurrentUserId() {
            return this.mCurrentUserId;
        }

        public int getSelectedInputMethodSubtypeId(String selectedImiId) {
            InputMethodInfo imi = (InputMethodInfo) this.mMethodMap.get(selectedImiId);
            if (imi == null) {
                return -1;
            }
            return InputMethodUtils.getSubtypeIdFromHashCode(imi, getSelectedInputMethodSubtypeHashCode());
        }

        public void saveCurrentInputMethodAndSubtypeToHistory(String curMethodId, InputMethodSubtype currentSubtype) {
            String subtypeId = InputMethodUtils.NOT_A_SUBTYPE_ID_STR;
            if (currentSubtype != null) {
                subtypeId = String.valueOf(currentSubtype.hashCode());
            }
            if (InputMethodUtils.canAddToLastInputMethod(currentSubtype)) {
                addSubtypeToHistory(curMethodId, subtypeId);
            }
        }

        public HashMap<InputMethodInfo, List<InputMethodSubtype>> getExplicitlyOrImplicitlyEnabledInputMethodsAndSubtypeListLocked(Context context) {
            HashMap<InputMethodInfo, List<InputMethodSubtype>> enabledInputMethodAndSubtypes = new HashMap();
            for (InputMethodInfo imi : getEnabledInputMethodListLocked()) {
                enabledInputMethodAndSubtypes.put(imi, getEnabledInputMethodSubtypeListLocked(context, imi, true));
            }
            return enabledInputMethodAndSubtypes;
        }
    }

    private InputMethodUtils() {
    }

    public static String getStackTrace() {
        StringBuilder sb = new StringBuilder();
        try {
            throw new RuntimeException();
        } catch (RuntimeException e) {
            StackTraceElement[] frames = e.getStackTrace();
            for (int j = 1; j < frames.length; j++) {
                sb.append(frames[j].toString() + "\n");
            }
            return sb.toString();
        }
    }

    public static String getApiCallStack() {
        String apiCallStack = "";
        try {
            throw new RuntimeException();
        } catch (RuntimeException e) {
            StackTraceElement[] frames = e.getStackTrace();
            for (int j = 1; j < frames.length; j++) {
                String tempCallStack = frames[j].toString();
                if (!TextUtils.isEmpty(apiCallStack)) {
                    if (tempCallStack.indexOf("Transact(") >= 0) {
                        break;
                    }
                    apiCallStack = tempCallStack;
                } else {
                    apiCallStack = tempCallStack;
                }
            }
            return apiCallStack;
        }
    }

    public static boolean isSystemIme(InputMethodInfo inputMethod) {
        return (inputMethod.getServiceInfo().applicationInfo.flags & 1) != 0;
    }

    public static boolean isSystemImeThatHasSubtypeOf(InputMethodInfo imi, Context context, boolean checkDefaultAttribute, Locale requiredLocale, boolean checkCountry, String requiredSubtypeMode) {
        if (!isSystemIme(imi)) {
            return false;
        }
        if ((!checkDefaultAttribute || imi.isDefault(context)) && containsSubtypeOf(imi, requiredLocale, checkCountry, requiredSubtypeMode)) {
            return true;
        }
        return false;
    }

    public static Locale getFallbackLocaleForDefaultIme(ArrayList<InputMethodInfo> imis, Context context) {
        for (Locale fallbackLocale : SEARCH_ORDER_OF_FALLBACK_LOCALES) {
            int i;
            for (i = 0; i < imis.size(); i++) {
                if (isSystemImeThatHasSubtypeOf((InputMethodInfo) imis.get(i), context, true, fallbackLocale, true, SUBTYPE_MODE_KEYBOARD)) {
                    return fallbackLocale;
                }
            }
        }
        for (Locale fallbackLocale2 : SEARCH_ORDER_OF_FALLBACK_LOCALES) {
            for (i = 0; i < imis.size(); i++) {
                if (isSystemImeThatHasSubtypeOf((InputMethodInfo) imis.get(i), context, false, fallbackLocale2, true, SUBTYPE_MODE_KEYBOARD)) {
                    return fallbackLocale2;
                }
            }
        }
        Slog.w(TAG, "Found no fallback locale. imis=" + Arrays.toString(imis.toArray()));
        return null;
    }

    private static boolean isSystemAuxilialyImeThatHasAutomaticSubtype(InputMethodInfo imi, Context context, boolean checkDefaultAttribute) {
        if (!isSystemIme(imi)) {
            return false;
        }
        if ((checkDefaultAttribute && !imi.isDefault(context)) || !imi.isAuxiliaryIme()) {
            return false;
        }
        int subtypeCount = imi.getSubtypeCount();
        for (int i = 0; i < subtypeCount; i++) {
            if (imi.getSubtypeAt(i).overridesImplicitlyEnabledSubtype()) {
                return true;
            }
        }
        return false;
    }

    public static Locale getSystemLocaleFromContext(Context context) {
        try {
            return context.getResources().getConfiguration().locale;
        } catch (NotFoundException e) {
            return null;
        }
    }

    private static InputMethodListBuilder getMinimumKeyboardSetWithoutSystemLocale(ArrayList<InputMethodInfo> imis, Context context, Locale fallbackLocale) {
        InputMethodListBuilder builder = new InputMethodListBuilder();
        builder.fillImes(imis, context, true, fallbackLocale, true, SUBTYPE_MODE_KEYBOARD);
        if (builder.isEmpty()) {
            builder.fillImes(imis, context, false, fallbackLocale, true, SUBTYPE_MODE_KEYBOARD);
            if (builder.isEmpty()) {
                builder.fillImes(imis, context, true, fallbackLocale, false, SUBTYPE_MODE_KEYBOARD);
                if (builder.isEmpty()) {
                    builder.fillImes(imis, context, false, fallbackLocale, false, SUBTYPE_MODE_KEYBOARD);
                    if (builder.isEmpty()) {
                        Slog.w(TAG, "No software keyboard is found. imis=" + Arrays.toString(imis.toArray()) + " fallbackLocale=" + fallbackLocale);
                    }
                }
            }
        }
        return builder;
    }

    private static InputMethodListBuilder getMinimumKeyboardSetWithSystemLocale(ArrayList<InputMethodInfo> imis, Context context, Locale systemLocale, Locale fallbackLocale) {
        InputMethodListBuilder builder = new InputMethodListBuilder();
        builder.fillImes(imis, context, true, systemLocale, true, SUBTYPE_MODE_KEYBOARD);
        if (builder.isEmpty()) {
            builder.fillImes(imis, context, true, systemLocale, false, SUBTYPE_MODE_KEYBOARD);
            if (builder.isEmpty()) {
                builder.fillImes(imis, context, true, fallbackLocale, true, SUBTYPE_MODE_KEYBOARD);
                if (builder.isEmpty()) {
                    builder.fillImes(imis, context, true, fallbackLocale, false, SUBTYPE_MODE_KEYBOARD);
                    if (builder.isEmpty()) {
                        builder.fillImes(imis, context, false, fallbackLocale, true, SUBTYPE_MODE_KEYBOARD);
                        if (builder.isEmpty()) {
                            builder.fillImes(imis, context, false, fallbackLocale, false, SUBTYPE_MODE_KEYBOARD);
                            if (builder.isEmpty()) {
                                Slog.w(TAG, "No software keyboard is found. imis=" + Arrays.toString(imis.toArray()) + " systemLocale=" + systemLocale + " fallbackLocale=" + fallbackLocale);
                            }
                        }
                    }
                }
            }
        }
        return builder;
    }

    public static ArrayList<InputMethodInfo> getDefaultEnabledImes(Context context, boolean isSystemReady, ArrayList<InputMethodInfo> imis) {
        Locale fallbackLocale = getFallbackLocaleForDefaultIme(imis, context);
        if (isSystemReady) {
            Locale systemLocale = getSystemLocaleFromContext(context);
            return getMinimumKeyboardSetWithSystemLocale(imis, context, systemLocale, fallbackLocale).fillImes(imis, context, true, systemLocale, true, SUBTYPE_MODE_ANY).fillAuxiliaryImes(imis, context).build();
        }
        return getMinimumKeyboardSetWithoutSystemLocale(imis, context, fallbackLocale).fillImes(imis, context, true, fallbackLocale, true, SUBTYPE_MODE_ANY).build();
    }

    public static Locale constructLocaleFromString(String localeStr) {
        if (TextUtils.isEmpty(localeStr)) {
            return null;
        }
        String[] localeParams = localeStr.split("_", 3);
        if (localeParams.length == 1) {
            if (localeParams.length >= 1 && "tl".equals(localeParams[0])) {
                localeParams[0] = "fil";
            }
            return new Locale(localeParams[0]);
        } else if (localeParams.length == 2) {
            return new Locale(localeParams[0], localeParams[1]);
        } else {
            if (localeParams.length == 3) {
                return new Locale(localeParams[0], localeParams[1], localeParams[2]);
            }
            return null;
        }
    }

    public static boolean containsSubtypeOf(InputMethodInfo imi, Locale locale, boolean checkCountry, String mode) {
        if (locale == null) {
            return false;
        }
        int N = imi.getSubtypeCount();
        for (int i = 0; i < N; i++) {
            InputMethodSubtype subtype = imi.getSubtypeAt(i);
            if (checkCountry) {
                Locale subtypeLocale = subtype.getLocaleObject();
                if (subtypeLocale == null) {
                    continue;
                } else if (TextUtils.equals(subtypeLocale.getLanguage(), locale.getLanguage())) {
                    if (!TextUtils.equals(subtypeLocale.getCountry(), locale.getCountry())) {
                        continue;
                    }
                    if (mode != SUBTYPE_MODE_ANY || TextUtils.isEmpty(mode) || mode.equalsIgnoreCase(subtype.getMode())) {
                        return true;
                    }
                } else {
                    continue;
                }
            } else {
                if (!TextUtils.equals(new Locale(getLanguageFromLocaleString(subtype.getLocale())).getLanguage(), locale.getLanguage())) {
                    continue;
                }
                if (mode != SUBTYPE_MODE_ANY) {
                }
                return true;
            }
        }
        return false;
    }

    public static ArrayList<InputMethodSubtype> getSubtypes(InputMethodInfo imi) {
        ArrayList<InputMethodSubtype> subtypes = new ArrayList();
        int subtypeCount = imi.getSubtypeCount();
        for (int i = 0; i < subtypeCount; i++) {
            subtypes.add(imi.getSubtypeAt(i));
        }
        return subtypes;
    }

    public static ArrayList<InputMethodSubtype> getOverridingImplicitlyEnabledSubtypes(InputMethodInfo imi, String mode) {
        ArrayList<InputMethodSubtype> subtypes = new ArrayList();
        int subtypeCount = imi.getSubtypeCount();
        for (int i = 0; i < subtypeCount; i++) {
            InputMethodSubtype subtype = imi.getSubtypeAt(i);
            if (subtype.overridesImplicitlyEnabledSubtype() && subtype.getMode().equals(mode)) {
                subtypes.add(subtype);
            }
        }
        return subtypes;
    }

    public static InputMethodInfo getMostApplicableDefaultIME(List<InputMethodInfo> enabledImes) {
        if (enabledImes == null || enabledImes.isEmpty()) {
            return null;
        }
        InputMethodInfo imi;
        int i = enabledImes.size();
        int firstFoundSystemIme = -1;
        if (PowerProfile.POWER_NONE.equals(CONTRY_CODE)) {
            CONTRY_CODE = SystemProperties.get("ro.csc.country_code").toUpperCase();
        }
        int j;
        if ("CHINA".equals(CONTRY_CODE) || "HONG KONG".equals(CONTRY_CODE) || "TAIWAN".equals(CONTRY_CODE)) {
            for (j = 0; j < i; j++) {
                imi = (InputMethodInfo) enabledImes.get(j);
                if ("com.samsung.inputmethod/.SamsungIME".equals(imi.getId())) {
                    return imi;
                }
            }
        } else {
            for (j = 0; j < i; j++) {
                imi = (InputMethodInfo) enabledImes.get(j);
                if ("com.sec.android.inputmethod/.SamsungKeypad".equals(imi.getId())) {
                    Slog.d(TAG, "getMostApplicableDefaultIME(): Set the default IME to SamsungKeypad" + imi);
                    return imi;
                }
            }
        }
        while (i > 0) {
            i--;
            imi = (InputMethodInfo) enabledImes.get(i);
            if (!imi.isAuxiliaryIme()) {
                if (isSystemIme(imi) && containsSubtypeOf(imi, ENGLISH_LOCALE, false, SUBTYPE_MODE_KEYBOARD)) {
                    return imi;
                }
                if (firstFoundSystemIme < 0 && isSystemIme(imi)) {
                    firstFoundSystemIme = i;
                }
            }
        }
        return (InputMethodInfo) enabledImes.get(Math.max(firstFoundSystemIme, 0));
    }

    public static boolean isValidSubtypeId(InputMethodInfo imi, int subtypeHashCode) {
        return getSubtypeIdFromHashCode(imi, subtypeHashCode) != -1;
    }

    public static int getSubtypeIdFromHashCode(InputMethodInfo imi, int subtypeHashCode) {
        if (imi != null) {
            int subtypeCount = imi.getSubtypeCount();
            for (int i = 0; i < subtypeCount; i++) {
                if (subtypeHashCode == imi.getSubtypeAt(i).hashCode()) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static ArrayList<InputMethodSubtype> getImplicitlyApplicableSubtypesLocked(Resources res, InputMethodInfo imi) {
        List<InputMethodSubtype> subtypes = getSubtypes(imi);
        String systemLocale = res.getConfiguration().locale.toString();
        if (TextUtils.isEmpty(systemLocale)) {
            return new ArrayList();
        }
        int i;
        String mode;
        String systemLanguage = res.getConfiguration().locale.getLanguage();
        HashMap<String, InputMethodSubtype> applicableModeAndSubtypesMap = new HashMap();
        int N = subtypes.size();
        for (i = 0; i < N; i++) {
            InputMethodSubtype subtype = (InputMethodSubtype) subtypes.get(i);
            if (subtype.overridesImplicitlyEnabledSubtype()) {
                mode = subtype.getMode();
                if (!applicableModeAndSubtypesMap.containsKey(mode)) {
                    applicableModeAndSubtypesMap.put(mode, subtype);
                }
            }
        }
        if (applicableModeAndSubtypesMap.size() > 0) {
            return new ArrayList(applicableModeAndSubtypesMap.values());
        }
        for (i = 0; i < N; i++) {
            subtype = (InputMethodSubtype) subtypes.get(i);
            String locale = subtype.getLocale();
            mode = subtype.getMode();
            if (getLanguageFromLocaleString(locale).equals(systemLanguage) && systemLocale.startsWith(locale)) {
                InputMethodSubtype applicableSubtype = (InputMethodSubtype) applicableModeAndSubtypesMap.get(mode);
                if (applicableSubtype == null || (!systemLocale.equals(applicableSubtype.getLocale()) && systemLocale.equals(locale))) {
                    applicableModeAndSubtypesMap.put(mode, subtype);
                }
            }
        }
        InputMethodSubtype keyboardSubtype = (InputMethodSubtype) applicableModeAndSubtypesMap.get(SUBTYPE_MODE_KEYBOARD);
        ArrayList<InputMethodSubtype> applicableSubtypes = new ArrayList(applicableModeAndSubtypesMap.values());
        if (!(keyboardSubtype == null || keyboardSubtype.containsExtraValueKey(TAG_ASCII_CAPABLE))) {
            for (i = 0; i < N; i++) {
                subtype = (InputMethodSubtype) subtypes.get(i);
                if (SUBTYPE_MODE_KEYBOARD.equals(subtype.getMode()) && subtype.containsExtraValueKey(TAG_ENABLED_WHEN_DEFAULT_IS_NOT_ASCII_CAPABLE)) {
                    applicableSubtypes.add(subtype);
                }
            }
        }
        if (keyboardSubtype != null) {
            return applicableSubtypes;
        }
        InputMethodSubtype lastResortKeyboardSubtype = findLastResortApplicableSubtypeLocked(res, subtypes, SUBTYPE_MODE_KEYBOARD, systemLocale, true);
        if (lastResortKeyboardSubtype == null) {
            return applicableSubtypes;
        }
        applicableSubtypes.add(lastResortKeyboardSubtype);
        return applicableSubtypes;
    }

    public static String getLanguageFromLocaleString(String locale) {
        int idx = locale.indexOf(95);
        return idx < 0 ? locale : locale.substring(0, idx);
    }

    public static InputMethodSubtype findLastResortApplicableSubtypeLocked(Resources res, List<InputMethodSubtype> subtypes, String mode, String locale, boolean canIgnoreLocaleAsLastResort) {
        if (subtypes == null || subtypes.size() == 0) {
            return null;
        }
        if (TextUtils.isEmpty(locale)) {
            locale = res.getConfiguration().locale.toString();
        }
        String language = getLanguageFromLocaleString(locale);
        boolean partialMatchFound = false;
        InputMethodSubtype applicableSubtype = null;
        InputMethodSubtype firstMatchedModeSubtype = null;
        int N = subtypes.size();
        int i = 0;
        while (i < N) {
            InputMethodSubtype subtype = (InputMethodSubtype) subtypes.get(i);
            String subtypeLocale = subtype.getLocale();
            String subtypeLanguage = getLanguageFromLocaleString(subtypeLocale);
            if (mode == null || ((InputMethodSubtype) subtypes.get(i)).getMode().equalsIgnoreCase(mode)) {
                if (firstMatchedModeSubtype == null) {
                    firstMatchedModeSubtype = subtype;
                }
                if (locale.equals(subtypeLocale)) {
                    applicableSubtype = subtype;
                    break;
                } else if (!partialMatchFound && language.equals(subtypeLanguage)) {
                    applicableSubtype = subtype;
                    partialMatchFound = true;
                }
            }
            i++;
        }
        return (applicableSubtype == null && canIgnoreLocaleAsLastResort) ? firstMatchedModeSubtype : applicableSubtype;
    }

    public static boolean canAddToLastInputMethod(InputMethodSubtype subtype) {
        if (subtype != null && subtype.isAuxiliary()) {
            return false;
        }
        return true;
    }

    public static void setNonSelectedSystemImesDisabledUntilUsed(IPackageManager packageManager, List<InputMethodInfo> enabledImis, int userId, String callingPackage) {
        String[] systemImesDisabledUntilUsed = Resources.getSystem().getStringArray(R.array.config_disabledUntilUsedPreinstalledImes);
        if (systemImesDisabledUntilUsed != null && systemImesDisabledUntilUsed.length != 0) {
            SpellCheckerInfo currentSpellChecker = TextServicesManager.getInstance().getCurrentSpellChecker();
            for (String packageName : systemImesDisabledUntilUsed) {
                boolean enabledIme = false;
                for (int j = 0; j < enabledImis.size(); j++) {
                    if (packageName.equals(((InputMethodInfo) enabledImis.get(j)).getPackageName())) {
                        enabledIme = true;
                        break;
                    }
                }
                if (!enabledIme && (currentSpellChecker == null || !packageName.equals(currentSpellChecker.getPackageName()))) {
                    try {
                        ApplicationInfo ai = packageManager.getApplicationInfo(packageName, 32768, userId);
                        if (ai != null) {
                            if ((ai.flags & 1) != 0) {
                                setDisabledUntilUsed(packageManager, packageName, userId, callingPackage);
                            }
                        }
                    } catch (RemoteException e) {
                        Slog.w(TAG, "getApplicationInfo failed. packageName=" + packageName + " userId=" + userId, e);
                    }
                }
            }
        }
    }

    private static void setDisabledUntilUsed(IPackageManager packageManager, String packageName, int userId, String callingPackage) {
        try {
            int state = packageManager.getApplicationEnabledSetting(packageName, userId);
            if (state == 0 || state == 1) {
                try {
                    packageManager.setApplicationEnabledSetting(packageName, 4, 0, userId, callingPackage);
                } catch (RemoteException e) {
                    Slog.w(TAG, "setApplicationEnabledSetting failed. packageName=" + packageName + " userId=" + userId + " callingPackage=" + callingPackage, e);
                }
            }
        } catch (RemoteException e2) {
            Slog.w(TAG, "getApplicationEnabledSetting failed. packageName=" + packageName + " userId=" + userId, e2);
        }
    }

    public static CharSequence getImeAndSubtypeDisplayName(Context context, InputMethodInfo imi, InputMethodSubtype subtype) {
        CharSequence imiLabel = imi.loadLabel(context.getPackageManager());
        if (((imi.getPackageName().equals("com.sec.android.inputmethod") || imi.getPackageName().equals("com.samsung.inputmethod")) && isSystemIme(imi)) || subtype == null) {
            return imiLabel;
        }
        CharSequence[] charSequenceArr = new CharSequence[2];
        charSequenceArr[0] = subtype.getDisplayName(context, imi.getPackageName(), imi.getServiceInfo().applicationInfo);
        charSequenceArr[1] = TextUtils.isEmpty(imiLabel) ? "" : " - " + imiLabel;
        return TextUtils.concat(charSequenceArr);
    }

    public static boolean checkIfPackageBelongsToUid(AppOpsManager appOpsManager, int uid, String packageName) {
        try {
            appOpsManager.checkPackage(uid, packageName);
            return true;
        } catch (SecurityException e) {
            return false;
        }
    }

    public static ArrayList<Locale> getSuitableLocalesForSpellChecker(Locale systemLocale) {
        Locale systemLocaleLanguageCountryVariant;
        Locale systemLocaleLanguageCountry;
        Locale systemLocaleLanguage;
        if (systemLocale != null) {
            boolean hasLanguage;
            String language = systemLocale.getLanguage();
            if (TextUtils.isEmpty(language)) {
                hasLanguage = false;
            } else {
                hasLanguage = true;
            }
            String country = systemLocale.getCountry();
            boolean hasCountry;
            if (TextUtils.isEmpty(country)) {
                hasCountry = false;
            } else {
                hasCountry = true;
            }
            String variant = systemLocale.getVariant();
            boolean hasVariant;
            if (TextUtils.isEmpty(variant)) {
                hasVariant = false;
            } else {
                hasVariant = true;
            }
            if (hasLanguage && hasCountry && hasVariant) {
                systemLocaleLanguageCountryVariant = new Locale(language, country, variant);
            } else {
                systemLocaleLanguageCountryVariant = null;
            }
            if (hasLanguage && hasCountry) {
                systemLocaleLanguageCountry = new Locale(language, country);
            } else {
                systemLocaleLanguageCountry = null;
            }
            if (hasLanguage) {
                systemLocaleLanguage = new Locale(language);
            } else {
                systemLocaleLanguage = null;
            }
        } else {
            systemLocaleLanguageCountryVariant = null;
            systemLocaleLanguageCountry = null;
            systemLocaleLanguage = null;
        }
        ArrayList<Locale> locales = new ArrayList();
        if (systemLocaleLanguageCountryVariant != null) {
            locales.add(systemLocaleLanguageCountryVariant);
        }
        if (!Locale.ENGLISH.equals(systemLocaleLanguage)) {
            if (systemLocaleLanguageCountry != null) {
                locales.add(systemLocaleLanguageCountry);
            }
            if (systemLocaleLanguage != null) {
                locales.add(systemLocaleLanguage);
            }
            locales.add(LOCALE_EN_US);
            locales.add(LOCALE_EN_GB);
            locales.add(Locale.ENGLISH);
        } else if (systemLocaleLanguageCountry != null) {
            if (systemLocaleLanguageCountry != null) {
                locales.add(systemLocaleLanguageCountry);
            }
            if (!LOCALE_EN_US.equals(systemLocaleLanguageCountry)) {
                locales.add(LOCALE_EN_US);
            }
            if (!LOCALE_EN_GB.equals(systemLocaleLanguageCountry)) {
                locales.add(LOCALE_EN_GB);
            }
            locales.add(Locale.ENGLISH);
        } else {
            locales.add(Locale.ENGLISH);
            locales.add(LOCALE_EN_US);
            locales.add(LOCALE_EN_GB);
        }
        return locales;
    }

    public static boolean isSamsungIme(InputMethodInfo imi) {
        if (imi == null) {
            return false;
        }
        String imiId = imi.getId();
        if (imiId.equals("com.samsung.inputmethod/.SamsungIME")) {
            return true;
        }
        if (imiId.equals("com.sec.android.inputmethod.iwnnime.japan/.standardcommon.IWnnLanguageSwitcher")) {
            return true;
        }
        if (imiId.equals("com.sec.android.inputmethod/.SamsungKeypad")) {
            return true;
        }
        return false;
    }
}
