package com.android.internal.app;

import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.app.ListFragment;
import android.app.backup.BackupManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Settings.Global;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.android.internal.R;
import com.android.internal.content.NativeLibraryHelper;
import com.samsung.android.fingerprint.FingerprintManager;
import java.io.File;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LocalePicker extends ListFragment {
    private static final boolean DEBUG = false;
    private static final String LANGUAGE_XML = "/system/csc/language.xml";
    private static final String LANGUAGE_XML_OMC = "/data/omc/etc/language.xml";
    private static final String TAG = "LocalePicker";
    private static final String TAG_DISPLAY = "Display";
    private static final String TAG_LANGUAGE = "LanguageSet";
    LocaleSelectionListener mListener;

    public static class LocaleInfo implements Comparable<LocaleInfo> {
        static final Collator sCollator = Collator.getInstance();
        String label;
        Locale locale;

        public LocaleInfo(String label, Locale locale) {
            this.label = label;
            this.locale = locale;
        }

        public String getLabel() {
            return this.label;
        }

        public Locale getLocale() {
            return this.locale;
        }

        public String toString() {
            return this.label;
        }

        public int compareTo(LocaleInfo another) {
            return sCollator.compare(this.label, another.label);
        }
    }

    public interface LocaleSelectionListener {
        void onLocaleSelected(Locale locale);
    }

    public static ArrayList<String> getLocaleArray(String[] locales, Resources resources) {
        String localeCodes = resources.getString(R.string.locale_codes);
        String[] localeCodesArray = null;
        if (!(localeCodes == null || TextUtils.isEmpty(localeCodes.trim()))) {
            localeCodesArray = localeCodes.replace('_', '-').replaceAll("tl-", "fil-").split(FingerprintManager.FINGER_PERMISSION_DELIMITER);
        }
        if (localeCodesArray == null || localeCodesArray.length == 0) {
            localeCodesArray = locales;
        }
        return new ArrayList(Arrays.asList(localeCodesArray));
    }

    public static List<LocaleInfo> getAllAssetLocales(Context context, boolean isInDeveloperMode) {
        Iterator i$;
        Resources resources = context.getResources();
        String[] locales = Resources.getSystem().getAssets().getLocales();
        List<String> localeList = new ArrayList(locales.length);
        Collections.addAll(localeList, locales);
        localeList = filterSupportedLocales(localeList);
        if (!isInDeveloperMode) {
            localeList.remove("ar-XB");
            localeList.remove("en-XA");
        }
        Collections.sort(localeList);
        String[] specialLocaleCodes = resources.getStringArray(R.array.special_locale_codes);
        String[] specialLocaleNames = resources.getStringArray(R.array.special_locale_names);
        ArrayList<LocaleInfo> localeInfos = new ArrayList(localeList.size());
        for (String locale : localeList) {
            Locale l = Locale.forLanguageTag(locale.replace('_', '-'));
            if (!(l == null || "und".equals(l.getLanguage()) || l.getLanguage().isEmpty() || l.getCountry().isEmpty())) {
                if (localeInfos.isEmpty()) {
                    localeInfos.add(new LocaleInfo(toTitleCase(l.getDisplayLanguage(l)), l));
                } else {
                    LocaleInfo previous = (LocaleInfo) localeInfos.get(localeInfos.size() - 1);
                    if (!previous.locale.getLanguage().equals(l.getLanguage()) || previous.locale.getLanguage().equals("zz")) {
                        localeInfos.add(new LocaleInfo(toTitleCase(l.getDisplayLanguage(l)), l));
                    } else {
                        previous.label = toTitleCase(getDisplayName(previous.locale, specialLocaleCodes, specialLocaleNames));
                        localeInfos.add(new LocaleInfo(toTitleCase(getDisplayName(l, specialLocaleCodes, specialLocaleNames)), l));
                    }
                }
            }
        }
        if (resources.getBoolean(R.bool.config_display_country_for_locale_codes)) {
            i$ = localeInfos.iterator();
            while (i$.hasNext()) {
                LocaleInfo locale2 = (LocaleInfo) i$.next();
                l = locale2.locale;
                String languageName = toTitleCase(l.getDisplayLanguage(l));
                String displayName = toTitleCase(getDisplayName(l, specialLocaleCodes, specialLocaleNames));
                if (locale2.label.equals(languageName)) {
                    if (displayName.equals(languageName)) {
                        displayName = toTitleCase(String.format("%s (%s)", new Object[]{languageName, l.getDisplayCountry(l)}));
                    }
                    locale2.label = displayName;
                }
            }
        }
        Collections.sort(localeInfos);
        return localeInfos;
    }

    public static ArrayAdapter<LocaleInfo> constructAdapter(Context context) {
        int itemLayout = R.layout.locale_picker_item;
        TypedArray a = context.obtainStyledAttributes(R.styleable.Theme);
        try {
            if (a.getBoolean(R.styleable.Theme_parentIsDeviceDefault, false)) {
                itemLayout = R.layout.tw_locale_picker_item;
            }
            if (a != null) {
                a.recycle();
            }
        } catch (Exception e) {
            if (a != null) {
                a.recycle();
            }
        } catch (Throwable th) {
            if (a != null) {
                a.recycle();
            }
        }
        return constructAdapter(context, itemLayout, R.id.locale);
    }

    public static ArrayAdapter<LocaleInfo> constructAdapter(Context context, int layoutId, int fieldId) {
        boolean isInDeveloperMode = false;
        if (Global.getInt(context.getContentResolver(), "development_settings_enabled", 0) != 0) {
            isInDeveloperMode = true;
        }
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        final int i = layoutId;
        final int i2 = fieldId;
        return new ArrayAdapter<LocaleInfo>(context, layoutId, fieldId, getAllAssetLocales(context, isInDeveloperMode)) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View view;
                TextView text;
                if (convertView == null) {
                    view = inflater.inflate(i, parent, false);
                    text = (TextView) view.findViewById(i2);
                    view.setTag(text);
                } else {
                    view = convertView;
                    text = (TextView) view.getTag();
                }
                LocaleInfo item = (LocaleInfo) getItem(position);
                text.setText(item.toString());
                text.setTextLocale(item.getLocale());
                return view;
            }
        };
    }

    private static String toTitleCase(String s) {
        return s.length() == 0 ? s : Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private static String getDisplayName(Locale l, String[] specialLocaleCodes, String[] specialLocaleNames) {
        String code = l.toString();
        for (int i = 0; i < specialLocaleCodes.length; i++) {
            if (specialLocaleCodes[i].equals(code)) {
                return specialLocaleNames[i];
            }
        }
        return l.getDisplayName(l);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(constructAdapter(getActivity()));
    }

    public void setLocaleSelectionListener(LocaleSelectionListener listener) {
        this.mListener = listener;
    }

    public void onResume() {
        super.onResume();
        getListView().requestFocus();
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        if (this.mListener != null) {
            this.mListener.onLocaleSelected(((LocaleInfo) getListAdapter().getItem(position)).locale);
        }
    }

    public static void updateLocale(Locale locale) {
        try {
            IActivityManager am = ActivityManagerNative.getDefault();
            Configuration config = am.getConfiguration();
            config.setLocale(locale);
            config.userSetLocale = true;
            am.updateConfiguration(config);
            BackupManager.dataChanged("com.android.providers.settings");
        } catch (RemoteException e) {
        }
    }

    private static List<String> filterSupportedLocales(List<String> localeList) {
        String languageXmlPath = LANGUAGE_XML;
        if (new File(LANGUAGE_XML_OMC).exists()) {
            languageXmlPath = LANGUAGE_XML_OMC;
        }
        String supportedLocales = getLocaleListFromXML(languageXmlPath);
        if (!(supportedLocales == null || localeList == null)) {
            supportedLocales = supportedLocales.replaceAll("\\s", "").replaceAll("_", NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
            ArrayList<String> list = new ArrayList();
            for (String locale : localeList) {
                if (locale != null) {
                    if (supportedLocales.contains(locale)) {
                        list.add(locale);
                    } else if (locale.startsWith("fil") && supportedLocales.contains(locale.replace("fil", "tl"))) {
                        list.add(locale);
                    }
                }
            }
            if (list.size() > 0) {
                return list;
            }
        }
        Log.d(TAG, "Support all languages");
        return localeList;
    }

    private static String getLocaleListFromXML(String filePath) {
        Node rootNode = null;
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(filePath));
            if (doc != null) {
                rootNode = doc.getDocumentElement();
            }
        } catch (ParserConfigurationException ex) {
            Log.d(TAG, ex.toString());
        } catch (SAXException ex2) {
            Log.d(TAG, ex2.toString());
        } catch (IOException ex3) {
            Log.d(TAG, ex3.toString());
        }
        if (rootNode == null) {
            return null;
        }
        String[] tagList = new String[]{TAG_LANGUAGE, TAG_DISPLAY};
        Node node = rootNode;
        for (String tagName : tagList) {
            if (node != null) {
                NodeList children = node.getChildNodes();
                if (children != null) {
                    int n = children.getLength();
                    for (int i = 0; i < n; i++) {
                        Node child = children.item(i);
                        if (child != null && tagName.equals(child.getNodeName())) {
                            node = child;
                        }
                    }
                }
            }
        }
        if (node == null || !tagList[tagList.length - 1].equals(node.getNodeName())) {
            return null;
        }
        Node firstChild = node.getFirstChild();
        if (firstChild != null) {
            return firstChild.getNodeValue();
        }
        return null;
    }
}
