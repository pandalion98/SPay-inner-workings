package com.sec.android.app;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class CscFeature {
    public static final boolean Bool_NoTag = false;
    private static final String FEATURE_XML = "/system/csc/feature.xml";
    public static final int Int_NoTag = 0;
    private static final String MPS_FEATURE_XML = "/system/csc/others.xml";
    private static final String OMC_MPS_FEATURE_XML = "/data/omc/others.xml";
    public static final String Str_NoTag = "";
    private static final String TAG = "CscFeature";
    private static CscFeature sInstance = null;
    private Hashtable<String, String> mFeatureList = new Hashtable();

    private CscFeature() {
        try {
            loadFeatureFile();
        } catch (Exception e) {
            Log.w(TAG, e.toString());
        }
    }

    public static CscFeature getInstance() {
        if (sInstance == null) {
            sInstance = new CscFeature();
        }
        return sInstance;
    }

    public boolean getEnableStatus(String tag) {
        try {
            if (this.mFeatureList.get(tag) != null) {
                return Boolean.parseBoolean((String) this.mFeatureList.get(tag));
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean getEnableStatus(String tag, boolean defaultValue) {
        try {
            if (this.mFeatureList.get(tag) != null) {
                defaultValue = Boolean.parseBoolean((String) this.mFeatureList.get(tag));
            }
        } catch (Exception e) {
        }
        return defaultValue;
    }

    public String getString(String tag) {
        try {
            if (this.mFeatureList.get(tag) != null) {
                return (String) this.mFeatureList.get(tag);
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    public String getString(String tag, String defaultValue) {
        try {
            if (this.mFeatureList.get(tag) != null) {
                return (String) this.mFeatureList.get(tag);
            }
            return defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public int getInteger(String tag) {
        try {
            if (this.mFeatureList.get(tag) != null) {
                return Integer.parseInt((String) this.mFeatureList.get(tag));
            }
            return -1;
        } catch (Exception e) {
            return -1;
        }
    }

    public int getInteger(String tag, int defaultValue) {
        try {
            if (this.mFeatureList.get(tag) != null) {
                defaultValue = Integer.parseInt((String) this.mFeatureList.get(tag));
            }
        } catch (Exception e) {
        }
        return defaultValue;
    }

    private void loadFeatureFile() {
        XmlPullParserException e;
        FileNotFoundException e2;
        Throwable th;
        InputStream inputStream = null;
        String TagName = null;
        try {
            this.mFeatureList.clear();
            File featureXmlFile = new File(OMC_MPS_FEATURE_XML);
            if (!featureXmlFile.exists() || featureXmlFile.length() <= 0) {
                featureXmlFile = new File(FEATURE_XML);
                if (!featureXmlFile.exists() || featureXmlFile.length() <= 0) {
                    featureXmlFile = new File(MPS_FEATURE_XML);
                    if (!featureXmlFile.exists() || featureXmlFile.length() <= 0) {
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                                return;
                            } catch (IOException e3) {
                                Log.w(TAG, e3.toString());
                                return;
                            }
                        }
                        return;
                    }
                }
            }
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            InputStream fi = new FileInputStream(featureXmlFile);
            try {
                parser.setInput(fi, null);
                int eventType = parser.getEventType();
                while (eventType != 1) {
                    if (eventType == 2) {
                        TagName = parser.getName();
                    } else if (eventType == 4) {
                        String TagValue = parser.getText();
                        if (!(TagName == null || TagValue == null)) {
                            if (this.mFeatureList.containsKey(TagName)) {
                                try {
                                    eventType = parser.next();
                                } catch (IOException e32) {
                                    Log.w(TAG, e32.toString());
                                }
                            } else {
                                try {
                                    this.mFeatureList.put(TagName, TagValue.trim());
                                } catch (Exception ex) {
                                    Log.w(TAG, ex.toString());
                                }
                            }
                        }
                    }
                    try {
                        eventType = parser.next();
                    } catch (IOException e322) {
                        Log.w(TAG, e322.toString());
                    }
                }
                try {
                    fi.close();
                } catch (IOException e3222) {
                    Log.w(TAG, e3222.toString());
                }
                if (fi != null) {
                    try {
                        fi.close();
                        return;
                    } catch (IOException e32222) {
                        Log.w(TAG, e32222.toString());
                        inputStream = fi;
                        return;
                    }
                }
            } catch (XmlPullParserException e4) {
                e = e4;
                inputStream = fi;
            } catch (FileNotFoundException e5) {
                e2 = e5;
                inputStream = fi;
            } catch (Throwable th2) {
                th = th2;
                inputStream = fi;
            }
        } catch (XmlPullParserException e6) {
            e = e6;
            try {
                Log.w(TAG, e.toString());
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e322222) {
                        Log.w(TAG, e322222.toString());
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e3222222) {
                        Log.w(TAG, e3222222.toString());
                    }
                }
                throw th;
            }
        } catch (FileNotFoundException e7) {
            e2 = e7;
            Log.w(TAG, e2.toString());
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e32222222) {
                    Log.w(TAG, e32222222.toString());
                }
            }
        }
    }
}
