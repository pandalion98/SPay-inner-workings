package android.media;

import android.content.IntentFilter;
import android.media.IMediaHTTPConnection.Stub;
import android.net.NetworkUtils;
import android.net.ProxyInfo;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.IBinder;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy.Builder;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.net.UnknownServiceException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

public class MediaHTTPConnection extends Stub {
    private static final int CONNECT_TIMEOUT_MS = 30000;
    private static final int GET_DURATION = 17;
    private static final int GET_ENDBYTE = 22;
    private static final int GET_ENDTIME = 19;
    private static final int GET_PAUSE_OPTIONS = 23;
    private static final int GET_SEEK_OPTIONS = 16;
    private static final int GET_SERVERTYPE = 20;
    private static final int GET_STARTBYTE = 21;
    private static final int GET_STARTTIME = 18;
    private static final int HTTP_TEMP_REDIRECT = 307;
    private static final int MAX_REDIRECTS = 20;
    private static final String TAG = "MediaHTTPConnection";
    private static final boolean VERBOSE = false;
    private static HashMap<String, String> sDelimitersMap = new HashMap<String, String>() {
        {
            put("TimeSeekRange.dlna.org", " =");
        }
    };
    private boolean mAllowCrossDomainRedirect = true;
    private boolean mAllowCrossProtocolRedirect = true;
    private boolean mAvailabeSeekRange = true;
    private long mCheckFlag = 0;
    private HttpURLConnection mConnection = null;
    private long mCurrentOffset = -1;
    private long mDuration = -1;
    private long mEndByte = -1;
    private long mEndTime = -1;
    private boolean mFullRandomAccess = false;
    private Map<String, String> mHeaders = null;
    private InputStream mInputStream = null;
    private boolean mIsDLNA = false;
    private boolean mIsTranscodedUrl = false;
    private boolean mLimitedRandomAccess = false;
    private long mMaxOffsetAllowed = -1;
    private long mMinOffsetAllowed = -1;
    private long mNativeContext;
    private boolean mPartialDownloadSupported = true;
    private int mPauseEnabled = -1;
    private String mProxyIP = null;
    private int mProxyPort = 80;
    private int mReadTimeoutMs = -1;
    private int mResponse = 0;
    private int mSeekOptions = -1;
    private boolean mSetProxy = false;
    private long mStartByte = -1;
    private long mStartTime = -1;
    private boolean mTimeSeekRequested = false;
    private long mTotalSize = -1;
    private URL mURL = null;

    private final native void native_finalize();

    private final native IBinder native_getIMemory();

    private static final native void native_init();

    private final native int native_readAt(long j, int i);

    private final native void native_setup();

    static {
        System.loadLibrary("media_jni");
        native_init();
    }

    public MediaHTTPConnection() {
        if (CookieHandler.getDefault() == null) {
            CookieHandler.setDefault(new CookieManager());
        }
        native_setup();
    }

    public IBinder connect(String uri, String headers) {
        try {
            disconnect();
            this.mAllowCrossDomainRedirect = true;
            if (uri.contains("type=TS")) {
                Log.d(TAG, "It is transcoded url");
                this.mIsTranscodedUrl = true;
            }
            this.mURL = new URL(uri);
            this.mHeaders = convertHeaderStringToMap(headers);
            if (this.mHeaders.containsKey("getcontentFeatures.dlna.org")) {
                Log.d(TAG, "getcontentFeatures.dlna.org key found. Setting mIsDLNA to true");
                this.mIsDLNA = true;
                if (this.mHeaders.containsKey("TimeSeekRange.dlna.org")) {
                    Log.d(TAG, "TimeSeekRange.dlna.org key found. Setting mIsDLNA to true");
                    this.mTimeSeekRequested = true;
                }
            }
            return native_getIMemory();
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private boolean parseBoolean(String val) {
        boolean z = false;
        try {
            if (Long.parseLong(val) != 0) {
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            if (AudioParameter.AUDIO_PARAMETER_VALUE_true.equalsIgnoreCase(val) || "yes".equalsIgnoreCase(val)) {
                z = true;
            }
            return z;
        }
    }

    private boolean filterOutInternalHeaders(String key, String val) {
        if (!"android-allow-cross-domain-redirect".equalsIgnoreCase(key)) {
            return false;
        }
        this.mAllowCrossDomainRedirect = parseBoolean(val);
        this.mAllowCrossProtocolRedirect = this.mAllowCrossDomainRedirect;
        return true;
    }

    private Map<String, String> convertHeaderStringToMap(String headers) {
        HashMap<String, String> map = new HashMap();
        for (String pair : headers.split("\r\n")) {
            int colonPos = pair.indexOf(":");
            if (colonPos >= 0) {
                String key = pair.substring(0, colonPos);
                String val = pair.substring(colonPos + 1);
                if (!filterOutInternalHeaders(key, val)) {
                    map.put(key, val);
                }
            }
        }
        return map;
    }

    public void disconnect() {
        teardownConnection();
        this.mHeaders = null;
        this.mURL = null;
    }

    private void teardownConnection() {
        if (this.mConnection != null) {
            this.mInputStream = null;
            this.mConnection.disconnect();
            this.mConnection = null;
            this.mCurrentOffset = -1;
        }
    }

    private static final boolean isLocalHost(URL url) {
        if (url == null) {
            return false;
        }
        String host = url.getHost();
        if (host == null) {
            return false;
        }
        try {
            if (host.equalsIgnoreCase(ProxyInfo.LOCAL_HOST)) {
                return true;
            }
            if (NetworkUtils.numericToInetAddress(host).isLoopbackAddress()) {
                return true;
            }
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private Map<String, String> convertAttributeValuesToMap(String headerName, String value) {
        String delimiters = sDelimitersMap.containsKey(headerName) ? (String) sDelimitersMap.get(headerName) : ";=";
        Log.d(TAG, "convertAttributeValuesToMap :: delimiters are " + delimiters);
        HashMap<String, String> attributeValuesMap = new HashMap();
        try {
            StringTokenizer st = new StringTokenizer(value.trim(), delimiters);
            while (st.hasMoreTokens()) {
                String key = st.nextToken();
                String vvalue = st.nextToken();
                Log.d(TAG, "key values are " + key + " " + vvalue);
                attributeValuesMap.put(key, vvalue);
            }
        } catch (Exception e) {
            Log.e(TAG, " Exception occurred: " + e);
        }
        return attributeValuesMap;
    }

    private void updateSeekOptions() {
        String contentfeatureValue = this.mConnection.getHeaderField("contentFeatures.dlna.org");
        Log.i(TAG, "contentfeature " + contentfeatureValue);
        if (contentfeatureValue != null) {
            HashMap<String, String> attributeValuesMap = (HashMap) convertAttributeValuesToMap("contentFeatures.dlna.org", contentfeatureValue);
            String seekOptionsValue = (String) attributeValuesMap.get("DLNA.ORG_OP");
            if (seekOptionsValue == null || this.mHeaders == null) {
                Log.e(TAG, "DLNA.ORG_OP not found mSeekOptions" + this.mSeekOptions);
            } else {
                this.mSeekOptions = Integer.parseInt(seekOptionsValue);
                this.mFullRandomAccess = true;
                Log.i(TAG, "mSeekOptions value " + this.mSeekOptions);
                if ((this.mSeekOptions == 1 || this.mSeekOptions == 11) && !this.mHeaders.containsKey("Range")) {
                    this.mHeaders.put("Range", "bytes=0-");
                }
            }
            String dlnaFlags = (String) attributeValuesMap.get("DLNA.ORG_FLAGS");
            if (dlnaFlags != null) {
                int lop_bytes = Integer.parseInt(dlnaFlags.substring(0, 3));
                int so_increasing = Integer.parseInt(dlnaFlags.substring(4, 5));
                int mcvt_check = Integer.parseInt(dlnaFlags.substring(1, 3));
                Log.i(TAG, "lop_bytes = " + lop_bytes);
                if (lop_bytes == 211) {
                    this.mLimitedRandomAccess = true;
                    Log.i(TAG, "mLimitedRandomAccess = true");
                } else if (mcvt_check == 11) {
                    this.mCheckFlag = 1;
                    Log.i(TAG, "MCVT TEST");
                }
                this.mPauseEnabled = Integer.parseInt(dlnaFlags.substring(10, 11));
                Log.i(TAG, "mPauseEnabled = " + this.mPauseEnabled);
                if (this.mFullRandomAccess && lop_bytes == 211 && so_increasing == 1) {
                    Log.e(TAG, "Error. Mutually exclusive values being set.");
                    return;
                }
                return;
            }
            Log.e(TAG, "DLNA.ORG_FLAGS not found");
        }
    }

    private void doCheckHeaderOptions() throws IOException {
        this.mConnection = (HttpURLConnection) this.mURL.openConnection();
        if (this.mHeaders != null) {
            for (Entry<String, String> entry : this.mHeaders.entrySet()) {
                this.mConnection.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
            }
        }
        int response = this.mConnection.getResponseCode();
        Log.i(TAG, "HeaderOptions response code = " + response);
        if (response == 200) {
            updateSeekOptions();
            this.mConnection.disconnect();
            this.mConnection = null;
            return;
        }
        Log.e(TAG, "Error code from server response " + response);
        throw new IOException();
    }

    private void parseAllowedOffset() {
        String availabeSeekRange = this.mConnection.getHeaderField("availableSeekRange.dlna.org");
        if (availabeSeekRange != null) {
            Log.i(TAG, "availabeSeekRange " + availabeSeekRange);
            int byteIndex = availabeSeekRange.indexOf("bytes=");
            int index = availabeSeekRange.indexOf("-");
            int seekIsAvailable = Integer.valueOf(availabeSeekRange.substring(0, byteIndex).trim()).intValue();
            if (index <= 0 || byteIndex <= 0 || seekIsAvailable != 1) {
                this.mAvailabeSeekRange = false;
                return;
            }
            this.mAvailabeSeekRange = true;
            this.mMinOffsetAllowed = (long) Integer.valueOf(availabeSeekRange.substring(byteIndex + 6, index)).intValue();
            this.mMaxOffsetAllowed = (long) Integer.valueOf(availabeSeekRange.substring(index + 1)).intValue();
            Log.i(TAG, "mMinOffsetAllowed = " + this.mMinOffsetAllowed + " mMaxOffsetAllowed =" + this.mMaxOffsetAllowed);
        }
    }

    private void checkForAvailableSeekRange(long offset) throws IOException {
        this.mConnection = (HttpURLConnection) this.mURL.openConnection();
        this.mConnection.setRequestMethod("HEAD");
        if (this.mHeaders != null) {
            for (Entry<String, String> entry : this.mHeaders.entrySet()) {
                this.mConnection.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
            }
        }
        this.mConnection.setRequestProperty("getAvailableSeekRange.dlna.org", WifiEnterpriseConfig.ENGINE_ENABLE);
        if ((offset >= 0 && !this.mIsDLNA) || (offset >= 0 && this.mIsDLNA && (this.mSeekOptions == 1 || this.mSeekOptions == 11))) {
            this.mConnection.setRequestProperty("Range", "bytes=" + offset + "-");
        }
        if (this.mReadTimeoutMs > 0) {
            Log.d(TAG, "SeekRange setReadTimeout with " + this.mReadTimeoutMs + "ms");
            this.mConnection.setConnectTimeout(this.mReadTimeoutMs);
            this.mConnection.setReadTimeout(this.mReadTimeoutMs);
        }
        int response = this.mConnection.getResponseCode();
        Log.i(TAG, "seekRange response code = " + response);
        if (response == 200 || response == 206) {
            parseAllowedOffset();
            this.mConnection.disconnect();
            this.mConnection = null;
            return;
        }
        Log.e(TAG, "Error code from server for checkForAvailableSeekRange " + response);
        throw new IOException();
    }

    private void seekTo(long offset) throws IOException {
        int lastSlashPos;
        teardownConnection();
        if (this.mIsDLNA && this.mSeekOptions == -1) {
            doCheckHeaderOptions();
        }
        if (this.mIsDLNA && this.mLimitedRandomAccess) {
            checkForAvailableSeekRange(offset);
            if (!this.mAvailabeSeekRange) {
                Log.d(TAG, "mAvailabeSeekRange mode is 0 we cannot seek ");
                return;
            } else if (this.mMaxOffsetAllowed >= 0 && this.mMinOffsetAllowed >= 0) {
                if (offset > this.mMaxOffsetAllowed) {
                    Log.d(TAG, "offset = " + offset + "mMaxOffsetAllowed = " + this.mMaxOffsetAllowed);
                    offset = this.mMaxOffsetAllowed - 1;
                }
                if (offset < this.mMinOffsetAllowed) {
                    Log.d(TAG, "offset = " + offset + "mMinOffsetAllowed = " + this.mMinOffsetAllowed);
                    offset = this.mMinOffsetAllowed + 1;
                }
                this.mMinOffsetAllowed = -1;
                this.mMaxOffsetAllowed = -1;
            }
        }
        int redirectCount = 0;
        URL url = this.mURL;
        boolean noProxy = isLocalHost(url);
        while (true) {
            if (noProxy) {
                this.mConnection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);
            } else {
                try {
                    if (this.mSetProxy) {
                        this.mConnection = (HttpURLConnection) url.openConnection(new Proxy(Type.HTTP, new InetSocketAddress(this.mProxyIP, this.mProxyPort)));
                    } else {
                        this.mConnection = (HttpURLConnection) url.openConnection();
                    }
                } catch (IOException e) {
                    this.mTotalSize = -1;
                    this.mInputStream = null;
                    if (this.mConnection != null) {
                        this.mConnection.disconnect();
                        this.mConnection = null;
                    }
                    this.mCurrentOffset = -1;
                    throw e;
                }
            }
            if (this.mReadTimeoutMs > 0) {
                Log.d(TAG, "setReadTimeout with " + this.mReadTimeoutMs + "ms");
                this.mConnection.setConnectTimeout(CONNECT_TIMEOUT_MS);
                this.mConnection.setReadTimeout(this.mReadTimeoutMs);
            }
            this.mConnection.setInstanceFollowRedirects(this.mAllowCrossDomainRedirect);
            if (this.mHeaders != null) {
                for (Entry<String, String> entry : this.mHeaders.entrySet()) {
                    this.mConnection.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
                }
            }
            if (!this.mTimeSeekRequested && offset > 0) {
                this.mConnection.setRequestProperty("Range", "bytes=" + offset + "-");
            }
            int response = this.mConnection.getResponseCode();
            Log.i(TAG, "response code = " + response);
            if (response != 300 && response != 301 && response != 302 && response != 303 && response != HTTP_TEMP_REDIRECT) {
                break;
            }
            redirectCount++;
            if (redirectCount > 20) {
                throw new NoRouteToHostException("Too many redirects: " + redirectCount);
            }
            String method = this.mConnection.getRequestMethod();
            if (response != HTTP_TEMP_REDIRECT || method.equals("GET") || method.equals("HEAD")) {
                String location = this.mConnection.getHeaderField("Location");
                if (location == null) {
                    throw new NoRouteToHostException("Invalid redirect");
                }
                URL url2 = new URL(this.mURL, location);
                if (url2.getProtocol().equals(IntentFilter.SCHEME_HTTPS) || url2.getProtocol().equals(IntentFilter.SCHEME_HTTP)) {
                    boolean sameProtocol = this.mURL.getProtocol().equals(url2.getProtocol());
                    if (this.mAllowCrossProtocolRedirect || sameProtocol) {
                        boolean sameHost = this.mURL.getHost().equals(url2.getHost());
                        if (!this.mAllowCrossDomainRedirect && !sameHost) {
                            throw new NoRouteToHostException("Cross-domain redirects are disallowed");
                        } else if (response != HTTP_TEMP_REDIRECT) {
                            this.mURL = url2;
                        }
                    } else {
                        throw new NoRouteToHostException("Cross-protocol redirects are disallowed");
                    }
                }
                throw new NoRouteToHostException("Unsupported protocol redirect");
            }
            throw new NoRouteToHostException("Invalid redirect");
        }
        if (this.mAllowCrossDomainRedirect) {
            this.mURL = this.mConnection.getURL();
        }
        if (response == 206) {
            String contentRange = this.mConnection.getHeaderField("Content-Range");
            this.mTotalSize = -1;
            if (contentRange != null) {
                lastSlashPos = contentRange.lastIndexOf(47);
                if (lastSlashPos >= 0) {
                    try {
                        this.mTotalSize = Long.parseLong(contentRange.substring(lastSlashPos + 1));
                    } catch (NumberFormatException e2) {
                    }
                }
            }
        } else if (response != 200) {
            this.mResponse = response;
            throw new IOException();
        } else {
            this.mTotalSize = (long) this.mConnection.getContentLength();
            if (this.mTotalSize == -1) {
                String contentSize = this.mConnection.getHeaderField("Content-Length");
                if (contentSize != null) {
                    this.mTotalSize = Long.parseLong(contentSize);
                }
            }
            Log.v(TAG, "mTotalSize is " + this.mTotalSize);
        }
        if (this.mTimeSeekRequested || offset <= 0 || response == 206) {
            if (this.mIsTranscodedUrl) {
                String duration = this.mConnection.getHeaderField("X-ASP-DURATION-TIME");
                if (duration != null) {
                    this.mDuration = Long.parseLong(duration) * 1000;
                    Log.e(TAG, "duration is " + this.mDuration);
                } else {
                    Log.e(TAG, "could not get the duration");
                }
            }
            try {
                if (this.mIsDLNA && this.mSeekOptions == 10) {
                    String timeseekrangevalue = this.mConnection.getHeaderField("TimeSeekRange.dlna.org");
                    Log.i(TAG, "timeseekrangevalue " + timeseekrangevalue);
                    if (timeseekrangevalue != null) {
                        this.mTimeSeekRequested = false;
                        HashMap<String, String> attributeValuesMap = (HashMap) convertAttributeValuesToMap("TimeSeekRange.dlna.org", timeseekrangevalue);
                        String bytesRange = (String) attributeValuesMap.get("bytes");
                        String timeseekrange = (String) attributeValuesMap.get("npt");
                        Log.i(TAG, "timeseekrange " + timeseekrange + "  byte range:" + bytesRange);
                        if (bytesRange != null) {
                            StringTokenizer bytesTok = new StringTokenizer(bytesRange, "-/");
                            long[] bytevalues = new long[3];
                            int counttoks = 0;
                            while (bytesTok.hasMoreTokens()) {
                                int counttoks2 = counttoks + 1;
                                bytevalues[counttoks] = Long.parseLong(bytesTok.nextToken());
                                Log.d(TAG, "token " + counttoks2 + " " + bytevalues[counttoks2 - 1]);
                                counttoks = counttoks2;
                            }
                            offset = bytevalues[0];
                            this.mTotalSize = bytevalues[2];
                            this.mStartByte = offset;
                            Log.d(TAG, "offset is " + this.mStartByte + " total size is " + this.mTotalSize);
                        }
                        String nptinfo = timeseekrange;
                        Log.d(TAG, "nptinfo " + nptinfo);
                        if (nptinfo != null) {
                            StringTokenizer stringTokenizer;
                            if (nptinfo.indexOf(58) > 0) {
                                stringTokenizer = new StringTokenizer(nptinfo, "-/");
                                String[] tokens = new String[3];
                                int i = 0;
                                while (i < 3 && stringTokenizer.hasMoreElements()) {
                                    tokens[i] = stringTokenizer.nextToken();
                                    i++;
                                }
                                if (i == 3) {
                                    stringTokenizer = new StringTokenizer(tokens[0], ":");
                                    stringTokenizer = new StringTokenizer(tokens[1], ":");
                                    stringTokenizer = new StringTokenizer(tokens[2], ":");
                                    String[] startTime = new String[3];
                                    String[] endTime = new String[3];
                                    String[] duration2 = new String[3];
                                    i = 0;
                                    while (i < 3 && stringTokenizer.hasMoreElements() && stringTokenizer.hasMoreElements() && stringTokenizer.hasMoreElements()) {
                                        startTime[i] = stringTokenizer.nextToken();
                                        endTime[i] = stringTokenizer.nextToken();
                                        duration2[i] = stringTokenizer.nextToken();
                                        i++;
                                    }
                                    if (i == 3) {
                                        this.mStartTime = ((((Long.parseLong(startTime[0]) * 60) * 60) * 1000) + ((Long.parseLong(startTime[1]) * 60) * 1000)) + (Long.parseLong(startTime[2]) * 1000);
                                        this.mStartTime *= 1000;
                                        this.mEndTime = ((((Long.parseLong(endTime[0]) * 60) * 60) * 1000) + ((Long.parseLong(endTime[1]) * 60) * 1000)) + (Long.parseLong(endTime[2]) * 1000);
                                        this.mEndTime *= 1000;
                                        this.mDuration = ((((Long.parseLong(duration2[0]) * 60) * 60) * 1000) + ((Long.parseLong(duration2[1]) * 60) * 1000)) + (Long.parseLong(duration2[2]) * 1000);
                                        this.mDuration *= 1000;
                                    } else {
                                        Log.e(TAG, "time info token parsing failed");
                                    }
                                } else {
                                    Log.e(TAG, "npt info token parsing failed ");
                                }
                            } else if (nptinfo.contains("*")) {
                                lastSlashPos = nptinfo.lastIndexOf(45);
                                if (lastSlashPos >= 0) {
                                    String startTime2 = nptinfo.substring(0, lastSlashPos);
                                    try {
                                        Log.d(TAG, "startTime " + startTime2);
                                        this.mStartTime = (long) (Double.parseDouble(startTime2) * 1000000.0d);
                                    } catch (NumberFormatException e3) {
                                    }
                                }
                            } else {
                                stringTokenizer = new StringTokenizer(nptinfo, "-/");
                                long[] timeValues = new long[3];
                                int count = 0;
                                while (stringTokenizer.hasMoreTokens()) {
                                    int count2 = count + 1;
                                    timeValues[count] = (long) ((Double.parseDouble(stringTokenizer.nextToken()) * 1000.0d) * 1000.0d);
                                    count = count2;
                                }
                                if (count == 3) {
                                    this.mStartTime = timeValues[0];
                                    this.mEndTime = timeValues[1];
                                    this.mDuration = timeValues[2];
                                } else if (count == 2) {
                                    this.mStartTime = timeValues[0];
                                    this.mDuration = timeValues[1];
                                } else {
                                    Log.e(TAG, "nptinfo token parsing failed-2");
                                }
                            }
                            Log.d(TAG, "mStartTime " + this.mStartTime + " mEndTime " + this.mEndTime + " mDuration " + this.mDuration);
                        }
                    } else {
                        Log.e(TAG, "TimeSeekRange.dlna.org not found");
                    }
                    Log.d(TAG, "Successfully parsed header");
                }
            } catch (ClassCastException e4) {
            }
            this.mInputStream = new BufferedInputStream(this.mConnection.getInputStream());
            this.mCurrentOffset = offset;
            return;
        }
        Log.v(TAG, "Server doesnt support Partial Request");
        this.mPartialDownloadSupported = false;
        throw new ProtocolException();
    }

    public int readAt(long offset, int size) {
        return native_readAt(offset, size);
    }

    private int readAt(long offset, byte[] data, int size) {
        StrictMode.setThreadPolicy(new Builder().permitAll().build());
        try {
            if ((this.mSeekOptions == 10 && this.mTimeSeekRequested) || !(this.mSeekOptions == 10 || offset == this.mCurrentOffset)) {
                seekTo(offset);
            }
            int n = this.mInputStream.read(data, 0, size);
            if (n == -1) {
                n = 0;
            }
            this.mCurrentOffset += (long) n;
            return n;
        } catch (ProtocolException e) {
            Log.w(TAG, "readAt " + offset + " / " + size + " => " + e);
            if (this.mPartialDownloadSupported) {
                return -1;
            }
            this.mPartialDownloadSupported = true;
            Log.v(TAG, "Return -EPIPE");
            return -32;
        } catch (NoRouteToHostException e2) {
            Log.w(TAG, "readAt " + offset + " / " + size + " => " + e2);
            return -1010;
        } catch (UnknownServiceException e3) {
            Log.w(TAG, "readAt " + offset + " / " + size + " => " + e3);
            return -1010;
        } catch (IOException e4) {
            return -1;
        } catch (Exception e5) {
            return -1;
        }
    }

    public long getSize() {
        if (this.mConnection == null) {
            try {
                seekTo(0);
            } catch (IOException e) {
                return -1;
            }
        }
        return this.mTotalSize;
    }

    public String getMIMEType() {
        if (this.mConnection == null) {
            try {
                seekTo(0);
            } catch (IOException e) {
                if (this.mResponse / 100 == 2) {
                    return "application/octet-stream";
                }
                Log.w(TAG, "request failed with error => " + this.mResponse);
                return "MEDIA_ERROR_IO";
            }
        }
        if (this.mConnection != null) {
            return this.mConnection.getContentType();
        }
        return "application/octet-stream";
    }

    public String getUri() {
        return this.mURL.toString();
    }

    public void setReadTimeOut(int timeoutMs) {
        Log.d(TAG, "setReadTimeOut =  " + timeoutMs + "ms");
        this.mReadTimeoutMs = timeoutMs;
    }

    public long getProperties(int type) {
        Log.d(TAG, "getProperties " + type);
        switch (type) {
            case 16:
                return (long) this.mSeekOptions;
            case 17:
                return this.mDuration;
            case 18:
                return this.mStartTime;
            case 21:
                return this.mStartByte;
            case 23:
                return (long) this.mPauseEnabled;
            default:
                return -1;
        }
    }

    public void setProxy(String proxy_ip, int proxy_port) {
        Log.d(TAG, "setProxy  Proxy IP = " + proxy_ip + " Proxy Port = " + proxy_port);
        this.mSetProxy = true;
        this.mProxyIP = proxy_ip;
        if (proxy_port != -1) {
            this.mProxyPort = proxy_port;
        }
    }

    protected void finalize() {
        native_finalize();
    }

    public long checkFlag() {
        return this.mCheckFlag;
    }
}
