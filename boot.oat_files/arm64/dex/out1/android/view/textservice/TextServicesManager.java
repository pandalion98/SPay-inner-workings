package android.view.textservice;

import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.view.textservice.SpellCheckerSession.SpellCheckerSessionListener;
import com.android.internal.textservice.ITextServicesManager;
import com.android.internal.textservice.ITextServicesManager.Stub;
import java.util.Locale;

public final class TextServicesManager {
    private static final boolean DBG = false;
    private static final String TAG = TextServicesManager.class.getSimpleName();
    private static TextServicesManager sInstance;
    private static ITextServicesManager sService;

    private TextServicesManager() {
        if (sService == null) {
            sService = Stub.asInterface(ServiceManager.getService("textservices"));
        }
    }

    public static TextServicesManager getInstance() {
        synchronized (TextServicesManager.class) {
            if (sInstance != null) {
                TextServicesManager textServicesManager = sInstance;
                return textServicesManager;
            }
            sInstance = new TextServicesManager();
            return sInstance;
        }
    }

    private static String parseLanguageFromLocaleString(String locale) {
        int idx = locale.indexOf(95);
        return idx < 0 ? locale : locale.substring(0, idx);
    }

    public SpellCheckerSession newSpellCheckerSession(Bundle bundle, Locale locale, SpellCheckerSessionListener listener, boolean referToSpellCheckerLanguageSettings) {
        if (listener == null) {
            throw new NullPointerException();
        } else if (!referToSpellCheckerLanguageSettings && locale == null) {
            throw new IllegalArgumentException("Locale should not be null if you don't refer settings.");
        } else if (referToSpellCheckerLanguageSettings && !isSpellCheckerEnabled()) {
            return null;
        } else {
            try {
                SpellCheckerInfo sci = sService.getCurrentSpellChecker(null);
                if (sci == null) {
                    return null;
                }
                SpellCheckerSubtype subtypeInUse = null;
                if (referToSpellCheckerLanguageSettings) {
                    subtypeInUse = getCurrentSpellCheckerSubtype(true);
                    if (subtypeInUse == null) {
                        return null;
                    }
                    if (locale != null) {
                        String subtypeLanguage = parseLanguageFromLocaleString(subtypeInUse.getLocale());
                        if (subtypeLanguage.length() < 2 || !locale.getLanguage().equals(subtypeLanguage)) {
                            return null;
                        }
                    }
                }
                String localeStr = locale.toString();
                for (int i = 0; i < sci.getSubtypeCount(); i++) {
                    SpellCheckerSubtype subtype = sci.getSubtypeAt(i);
                    String tempSubtypeLocale = subtype.getLocale();
                    String tempSubtypeLanguage = parseLanguageFromLocaleString(tempSubtypeLocale);
                    if (tempSubtypeLocale.equals(localeStr)) {
                        subtypeInUse = subtype;
                        break;
                    }
                    if (tempSubtypeLanguage.length() >= 2 && locale.getLanguage().equals(tempSubtypeLanguage)) {
                        subtypeInUse = subtype;
                    }
                }
                if (subtypeInUse == null) {
                    return null;
                }
                SpellCheckerSession session = new SpellCheckerSession(sci, sService, listener, subtypeInUse);
                try {
                    sService.getSpellCheckerService(sci.getId(), subtypeInUse.getLocale(), session.getTextServicesSessionListener(), session.getSpellCheckerSessionListener(), bundle);
                    return session;
                } catch (RemoteException e) {
                    return null;
                }
            } catch (RemoteException e2) {
                return null;
            }
        }
    }

    public SpellCheckerInfo[] getEnabledSpellCheckers() {
        try {
            return sService.getEnabledSpellCheckers();
        } catch (RemoteException e) {
            Log.e(TAG, "Error in getEnabledSpellCheckers: " + e);
            return null;
        }
    }

    public SpellCheckerInfo getCurrentSpellChecker() {
        SpellCheckerInfo spellCheckerInfo = null;
        try {
            spellCheckerInfo = sService.getCurrentSpellChecker(null);
        } catch (RemoteException e) {
        }
        return spellCheckerInfo;
    }

    public void setCurrentSpellChecker(SpellCheckerInfo sci) {
        if (sci == null) {
            try {
                throw new NullPointerException("SpellCheckerInfo is null.");
            } catch (RemoteException e) {
                Log.e(TAG, "Error in setCurrentSpellChecker: " + e);
                return;
            }
        }
        sService.setCurrentSpellChecker(null, sci.getId());
    }

    public SpellCheckerSubtype getCurrentSpellCheckerSubtype(boolean allowImplicitlySelectedSubtype) {
        SpellCheckerSubtype spellCheckerSubtype = null;
        try {
            if (sService == null) {
                Log.e(TAG, "sService is null.");
            } else {
                spellCheckerSubtype = sService.getCurrentSpellCheckerSubtype(null, allowImplicitlySelectedSubtype);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error in getCurrentSpellCheckerSubtype: " + e);
        }
        return spellCheckerSubtype;
    }

    public void setSpellCheckerSubtype(SpellCheckerSubtype subtype) {
        int hashCode;
        if (subtype == null) {
            hashCode = 0;
        } else {
            hashCode = subtype.hashCode();
        }
        try {
            sService.setCurrentSpellCheckerSubtype(null, hashCode);
        } catch (RemoteException e) {
            Log.e(TAG, "Error in setSpellCheckerSubtype:" + e);
        }
    }

    public void setSpellCheckerEnabled(boolean enabled) {
        try {
            sService.setSpellCheckerEnabled(enabled);
        } catch (RemoteException e) {
            Log.e(TAG, "Error in setSpellCheckerEnabled:" + e);
        }
    }

    public boolean isSpellCheckerEnabled() {
        try {
            return sService.isSpellCheckerEnabled();
        } catch (RemoteException e) {
            Log.e(TAG, "Error in isSpellCheckerEnabled:" + e);
            return false;
        }
    }

    public void switchUserForKnox(int userId) {
        try {
            sService.switchUserForKnox(userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Error in switchUserForKnox:" + e);
        }
    }
}
