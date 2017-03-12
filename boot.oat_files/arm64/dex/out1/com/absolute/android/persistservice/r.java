package com.absolute.android.persistservice;

import android.content.Context;
import android.os.Build;
import com.absolute.android.crypt.HexUtilities;
import com.absolute.android.logutil.LogUtil;
import com.absolute.android.persistence.AppInfoProperties;
import com.absolute.android.persistence.IABTDownloadReceiver;
import com.absolute.android.sslutil.SslUtil;
import com.absolute.android.utils.DeviceUtil;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.net.ssl.HttpsURLConnection;
import org.apache.http.client.HttpResponseException;

public class r {
    private static SslUtil a;
    private static List b = new ArrayList();
    private static String c;

    protected static String a(String str, String str2, int i, String str3, Context context) {
        StringBuffer stringBuffer = new StringBuffer();
        String property = System.getProperty("line.separator");
        stringBuffer.append("AccessKey=");
        stringBuffer.append(str);
        stringBuffer.append(property + "Package=");
        stringBuffer.append(str2);
        stringBuffer.append(property + "PersistenceVersion=");
        stringBuffer.append(i);
        stringBuffer.append(property + "Platform=");
        stringBuffer.append(DeviceUtil.getPlatform());
        stringBuffer.append(property + "Make=");
        stringBuffer.append(DeviceUtil.getManufacturer());
        stringBuffer.append(property + "Model=");
        stringBuffer.append(DeviceUtil.getModel());
        stringBuffer.append(property + "OSVersion=");
        stringBuffer.append(DeviceUtil.getOSVersion());
        stringBuffer.append(property + "BuildFingerprint=");
        stringBuffer.append(Build.FINGERPRINT);
        stringBuffer.append(property + "Hardware=");
        stringBuffer.append(DeviceUtil.getHardwareName());
        stringBuffer.append(property + "Revision=");
        stringBuffer.append(DeviceUtil.getHardwareRevision());
        stringBuffer.append(property + "DeviceId=");
        stringBuffer.append(str3);
        stringBuffer.append(property + "MacAddress=");
        stringBuffer.append(DeviceUtil.getMacAddress(context));
        stringBuffer.append(property + "IMEI=");
        String telephonyId = DeviceUtil.getTelephonyId(context);
        if (telephonyId == null) {
            telephonyId = "";
        }
        stringBuffer.append(telephonyId);
        stringBuffer.append(property + "SerialNo=");
        stringBuffer.append(DeviceUtil.getSerialNumber());
        stringBuffer.append(property + "RilSerialNumber=");
        stringBuffer.append(DeviceUtil.getSystemProperty("ril.serialnumber"));
        stringBuffer.append(property);
        return stringBuffer.toString();
    }

    protected static AppInfoProperties a(String str, String str2, String str3, String str4, v vVar, Context context) {
        HttpURLConnection a;
        Throwable th;
        BufferedReader bufferedReader;
        OutputStream outputStream = null;
        Properties appInfoProperties = new AppInfoProperties();
        HttpURLConnection httpURLConnection = null;
        URL url;
        try {
            url = new URL(str);
            a(context).trustOurHost(url.getHost(), str3, false);
            a = a(url, null, str3, true, vVar, context);
            try {
                OutputStream bufferedOutputStream = new BufferedOutputStream(a.getOutputStream());
                try {
                    bufferedOutputStream.write(str4.getBytes("UTF-8"));
                    bufferedOutputStream.flush();
                    int responseCode = a.getResponseCode();
                    if (responseCode == 200) {
                        Reader bufferedReader2 = new BufferedReader(new InputStreamReader(a.getInputStream(), "UTF-8"));
                        try {
                            appInfoProperties.load(bufferedReader2);
                        } catch (Throwable th2) {
                            th = th2;
                            outputStream = bufferedOutputStream;
                            bufferedReader = bufferedReader2;
                            if (a != null) {
                                try {
                                    a.disconnect();
                                } catch (Exception e) {
                                }
                            }
                            if (outputStream != null) {
                                try {
                                    outputStream.close();
                                } catch (Exception e2) {
                                }
                            }
                            if (bufferedReader != null) {
                                try {
                                    bufferedReader.close();
                                } catch (Exception e3) {
                                }
                            }
                            throw th;
                        }
                        if (a != null) {
                            try {
                                a.disconnect();
                            } catch (Exception e4) {
                            }
                        }
                        if (bufferedOutputStream != null) {
                            try {
                                bufferedOutputStream.close();
                            } catch (Exception e5) {
                            }
                        }
                        if (bufferedReader2 != null) {
                            try {
                                bufferedReader2.close();
                            } catch (Exception e6) {
                            }
                        }
                        return appInfoProperties;
                    }
                    throw new HttpResponseException(responseCode, "Server returned error in response to GetAppInfo HTTP request for URL: " + url.toString() + " response: " + a(a, vVar));
                } catch (Throwable th3) {
                    th = th3;
                    OutputStream outputStream2 = bufferedOutputStream;
                    bufferedReader = null;
                    outputStream = outputStream2;
                    if (a != null) {
                        a.disconnect();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    throw th;
                }
            } catch (Throwable th4) {
                th = th4;
                bufferedReader = null;
                if (a != null) {
                    a.disconnect();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                throw th;
            }
        } catch (Exception e7) {
            if (null != null) {
                try {
                    httpURLConnection.disconnect();
                } catch (Exception e8) {
                }
            }
            a = a(url, str2, str3, true, vVar, context);
        } catch (Throwable th5) {
            th = th5;
            a = null;
            bufferedReader = null;
            if (a != null) {
                a.disconnect();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            throw th;
        }
    }

    protected static void a(String str, int i, String str2, String str3, String str4, String str5, String str6, IABTDownloadReceiver iABTDownloadReceiver, int i2, v vVar, Context context) {
        URL url;
        HttpURLConnection a;
        Throwable th;
        HttpURLConnection httpURLConnection;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        DigestInputStream digestInputStream;
        try {
            url = new URL(str2);
            a(context).trustOurHost(url.getHost(), str4, false);
            a = a(url, null, str4, false, vVar, context);
            try {
                int responseCode = a.getResponseCode();
                if (responseCode == 200) {
                    int contentLength;
                    InputStream bufferedInputStream;
                    OutputStream outputStream2;
                    try {
                        contentLength = a.getContentLength();
                        MessageDigest instance = MessageDigest.getInstance("SHA256");
                        bufferedInputStream = new BufferedInputStream(a.getInputStream());
                        try {
                            digestInputStream = new DigestInputStream(bufferedInputStream, instance);
                        } catch (NoSuchAlgorithmException e) {
                            outputStream2 = null;
                            digestInputStream = null;
                            try {
                                throw new DownloadApkException("Downloaded APK for package: " + str + "failed - unsupported digest verification algorithm" + "SHA256", true);
                            } catch (Throwable th2) {
                                outputStream = outputStream2;
                                inputStream = bufferedInputStream;
                                th = th2;
                                httpURLConnection = a;
                                if (httpURLConnection != null) {
                                    try {
                                        httpURLConnection.disconnect();
                                    } catch (Exception e2) {
                                    }
                                }
                                if (outputStream != null) {
                                    try {
                                        outputStream.flush();
                                        outputStream.close();
                                    } catch (Exception e3) {
                                    }
                                }
                                if (digestInputStream != null) {
                                    try {
                                        digestInputStream.close();
                                    } catch (Exception e4) {
                                    }
                                } else if (inputStream != null) {
                                    inputStream.close();
                                }
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            inputStream = bufferedInputStream;
                            digestInputStream = null;
                            httpURLConnection = a;
                            if (httpURLConnection != null) {
                                httpURLConnection.disconnect();
                            }
                            if (outputStream != null) {
                                outputStream.flush();
                                outputStream.close();
                            }
                            if (digestInputStream != null) {
                                digestInputStream.close();
                            } else if (inputStream != null) {
                                inputStream.close();
                            }
                            throw th;
                        }
                    } catch (NoSuchAlgorithmException e5) {
                        outputStream2 = null;
                        bufferedInputStream = null;
                        digestInputStream = null;
                        throw new DownloadApkException("Downloaded APK for package: " + str + "failed - unsupported digest verification algorithm" + "SHA256", true);
                    }
                    try {
                        outputStream2 = new FileOutputStream(str6);
                        try {
                            byte[] bArr = new byte[8192];
                            int i3 = 0;
                            int i4 = 0;
                            while (true) {
                                int read = digestInputStream.read(bArr);
                                if (read == -1) {
                                    break;
                                }
                                outputStream2.write(bArr, 0, read);
                                if (iABTDownloadReceiver != null) {
                                    i3 += read;
                                    if (i3 - i4 >= i2 * 1024) {
                                        try {
                                            iABTDownloadReceiver.onDownloadProgress(str, i, contentLength, i3);
                                            i4 = i3;
                                        } catch (Throwable th22) {
                                            vVar.a("Got exception invoking IABTDownloadReceiver.onDownloadProgress() for package: " + str + " version: " + i + " Exception: ", th22);
                                            i4 = i3;
                                        }
                                    } else {
                                        continue;
                                    }
                                }
                            }
                            if (Arrays.equals(digestInputStream.getMessageDigest().digest(), HexUtilities.GetBytesFromHexString(str5))) {
                                if (a != null) {
                                    try {
                                        a.disconnect();
                                    } catch (Exception e6) {
                                    }
                                }
                                if (outputStream2 != null) {
                                    try {
                                        outputStream2.flush();
                                        outputStream2.close();
                                    } catch (Exception e7) {
                                    }
                                }
                                if (digestInputStream != null) {
                                    try {
                                        digestInputStream.close();
                                        return;
                                    } catch (Exception e8) {
                                        return;
                                    }
                                } else if (bufferedInputStream != null) {
                                    bufferedInputStream.close();
                                    return;
                                } else {
                                    return;
                                }
                            }
                            throw new DownloadApkException("Downloaded APK failed digest verification for algorithm: SHA256", false);
                        } catch (NoSuchAlgorithmException e9) {
                        }
                    } catch (NoSuchAlgorithmException e10) {
                        outputStream2 = null;
                        throw new DownloadApkException("Downloaded APK for package: " + str + "failed - unsupported digest verification algorithm" + "SHA256", true);
                    } catch (Throwable th4) {
                        th = th4;
                        inputStream = bufferedInputStream;
                        httpURLConnection = a;
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                        if (outputStream != null) {
                            outputStream.flush();
                            outputStream.close();
                        }
                        if (digestInputStream != null) {
                            digestInputStream.close();
                        } else if (inputStream != null) {
                            inputStream.close();
                        }
                        throw th;
                    }
                }
                throw new HttpResponseException(responseCode, "Server returned error in response to download APK HTTP request for URL: " + url.toString() + " response: " + a(a, vVar));
            } catch (Throwable th5) {
                th = th5;
                httpURLConnection = a;
                digestInputStream = null;
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
                if (digestInputStream != null) {
                    digestInputStream.close();
                } else if (inputStream != null) {
                    inputStream.close();
                }
                throw th;
            }
        } catch (Exception e11) {
            a = a(url, str3, str4, false, vVar, context);
        } catch (Throwable th6) {
            th = th6;
            httpURLConnection = null;
            digestInputStream = null;
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
            if (digestInputStream != null) {
                digestInputStream.close();
            } else if (inputStream != null) {
                inputStream.close();
            }
            throw th;
        }
    }

    private static HttpURLConnection a(URL url, String str, String str2, boolean z, v vVar, Context context) {
        HttpURLConnection httpURLConnection;
        c = url.getHost();
        if (str != null) {
            String str3 = url.getProtocol() + "://" + str;
            if (url.getPort() != -1) {
                str3 = str3 + ":" + url.getPort();
            }
            url = new URL((str3 + url.getPath()).replaceAll("\\s", ""));
        }
        if (url.getProtocol().compareToIgnoreCase("http") == 0) {
            httpURLConnection = (HttpURLConnection) url.openConnection();
        } else {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setHostnameVerifier(new s());
        }
        if (z) {
            httpURLConnection.setDoOutput(true);
        }
        httpURLConnection.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");
        httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
        httpURLConnection.setConnectTimeout(60000);
        httpURLConnection.setReadTimeout(60000);
        httpURLConnection.connect();
        return httpURLConnection;
    }

    private static String a(HttpURLConnection httpURLConnection, v vVar) {
        BufferedReader bufferedReader;
        Throwable th;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
            try {
                StringBuffer stringBuffer = new StringBuffer("");
                String str = "";
                str = System.getProperty("line.separator");
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    stringBuffer.append(readLine + str);
                }
                String stringBuffer2 = stringBuffer.toString();
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (Exception e) {
                        vVar.b("Got exception closing error BufferedReader after executing HTTP request. Exception: " + e.getMessage());
                    }
                }
                return stringBuffer2;
            } catch (Throwable th2) {
                th = th2;
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (Exception e2) {
                        vVar.b("Got exception closing error BufferedReader after executing HTTP request. Exception: " + e2.getMessage());
                    }
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            bufferedReader = null;
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            throw th;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void a(java.io.Reader r12, java.util.Properties r13) {
        /*
        if (r12 != 0) goto L_0x0008;
    L_0x0002:
        r0 = new java.lang.NullPointerException;
        r0.<init>();
        throw r0;
    L_0x0008:
        r6 = 0;
        r5 = 0;
        r4 = 0;
        r0 = 40;
        r3 = new char[r0];
        r2 = 0;
        r1 = -1;
        r0 = 1;
        r9 = new java.io.BufferedReader;
        r9.<init>(r12);
        r7 = r6;
        r6 = r5;
        r5 = r4;
    L_0x001a:
        r4 = r9.read();
        r8 = -1;
        if (r4 != r8) goto L_0x002f;
    L_0x0021:
        r0 = 2;
        if (r7 != r0) goto L_0x0125;
    L_0x0024:
        r0 = 4;
        if (r5 > r0) goto L_0x0125;
    L_0x0027:
        r0 = new java.lang.IllegalArgumentException;
        r1 = "Invalid Unicode sequence: expected format \\uxxxx";
        r0.<init>(r1);
        throw r0;
    L_0x002f:
        r8 = (char) r4;
        r4 = r3.length;
        if (r2 != r4) goto L_0x015d;
    L_0x0033:
        r4 = r3.length;
        r4 = r4 * 2;
        r4 = new char[r4];
        r10 = 0;
        r11 = 0;
        java.lang.System.arraycopy(r3, r10, r4, r11, r2);
    L_0x003d:
        r3 = 2;
        if (r7 != r3) goto L_0x015a;
    L_0x0040:
        r3 = 16;
        r3 = java.lang.Character.digit(r8, r3);
        if (r3 < 0) goto L_0x0053;
    L_0x0048:
        r6 = r6 << 4;
        r6 = r6 + r3;
        r3 = r5 + 1;
        r5 = 4;
        if (r3 >= r5) goto L_0x005e;
    L_0x0050:
        r5 = r3;
        r3 = r4;
        goto L_0x001a;
    L_0x0053:
        r3 = 4;
        if (r5 > r3) goto L_0x005f;
    L_0x0056:
        r0 = new java.lang.IllegalArgumentException;
        r1 = "Invalid Unicode sequence: illegal character";
        r0.<init>(r1);
        throw r0;
    L_0x005e:
        r5 = r3;
    L_0x005f:
        r7 = 0;
        r3 = r2 + 1;
        r10 = (char) r6;
        r4[r2] = r10;
        r2 = 10;
        if (r8 == r2) goto L_0x006c;
    L_0x0069:
        r2 = r3;
        r3 = r4;
        goto L_0x001a;
    L_0x006c:
        r2 = r3;
        r3 = r7;
    L_0x006e:
        r7 = 1;
        if (r3 != r7) goto L_0x00a3;
    L_0x0071:
        r7 = 0;
        switch(r8) {
            case 10: goto L_0x0088;
            case 13: goto L_0x0084;
            case 98: goto L_0x008c;
            case 102: goto L_0x008f;
            case 110: goto L_0x0092;
            case 114: goto L_0x0095;
            case 116: goto L_0x0098;
            case 117: goto L_0x009b;
            default: goto L_0x0075;
        };
    L_0x0075:
        r0 = r8;
    L_0x0076:
        r8 = r0;
    L_0x0077:
        r0 = 0;
        r3 = 4;
        if (r7 != r3) goto L_0x007d;
    L_0x007b:
        r7 = 0;
        r1 = r2;
    L_0x007d:
        r3 = r2 + 1;
        r4[r2] = r8;
        r2 = r3;
        r3 = r4;
        goto L_0x001a;
    L_0x0084:
        r3 = 3;
        r7 = r3;
        r3 = r4;
        goto L_0x001a;
    L_0x0088:
        r3 = 5;
        r7 = r3;
        r3 = r4;
        goto L_0x001a;
    L_0x008c:
        r0 = 8;
        goto L_0x0076;
    L_0x008f:
        r0 = 12;
        goto L_0x0076;
    L_0x0092:
        r0 = 10;
        goto L_0x0076;
    L_0x0095:
        r0 = 13;
        goto L_0x0076;
    L_0x0098:
        r0 = 9;
        goto L_0x0076;
    L_0x009b:
        r5 = 2;
        r3 = 0;
        r6 = r3;
        r7 = r5;
        r5 = r3;
        r3 = r4;
        goto L_0x001a;
    L_0x00a3:
        switch(r8) {
            case 10: goto L_0x00d5;
            case 13: goto L_0x00dd;
            case 33: goto L_0x00bb;
            case 35: goto L_0x00bb;
            case 58: goto L_0x010a;
            case 61: goto L_0x010a;
            case 92: goto L_0x0101;
            default: goto L_0x00a6;
        };
    L_0x00a6:
        r7 = java.lang.Character.isWhitespace(r8);
        if (r7 == 0) goto L_0x011b;
    L_0x00ac:
        r7 = 3;
        if (r3 != r7) goto L_0x00b0;
    L_0x00af:
        r3 = 5;
    L_0x00b0:
        if (r2 == 0) goto L_0x0156;
    L_0x00b2:
        if (r2 == r1) goto L_0x0156;
    L_0x00b4:
        r7 = 5;
        if (r3 != r7) goto L_0x0113;
    L_0x00b7:
        r7 = r3;
        r3 = r4;
        goto L_0x001a;
    L_0x00bb:
        if (r0 == 0) goto L_0x00a6;
    L_0x00bd:
        r7 = r9.read();
        r8 = -1;
        if (r7 != r8) goto L_0x00c8;
    L_0x00c4:
        r7 = r3;
        r3 = r4;
        goto L_0x001a;
    L_0x00c8:
        r7 = (char) r7;
        r8 = 13;
        if (r7 == r8) goto L_0x0156;
    L_0x00cd:
        r8 = 10;
        if (r7 != r8) goto L_0x00bd;
    L_0x00d1:
        r7 = r3;
        r3 = r4;
        goto L_0x001a;
    L_0x00d5:
        r7 = 3;
        if (r3 != r7) goto L_0x00dd;
    L_0x00d8:
        r3 = 5;
        r7 = r3;
        r3 = r4;
        goto L_0x001a;
    L_0x00dd:
        r3 = 0;
        r0 = 1;
        if (r2 > 0) goto L_0x00e5;
    L_0x00e1:
        if (r2 != 0) goto L_0x00fb;
    L_0x00e3:
        if (r1 != 0) goto L_0x00fb;
    L_0x00e5:
        r7 = -1;
        if (r1 != r7) goto L_0x00e9;
    L_0x00e8:
        r1 = r2;
    L_0x00e9:
        r7 = new java.lang.String;
        r8 = 0;
        r7.<init>(r4, r8, r2);
        r2 = 0;
        r2 = r7.substring(r2, r1);
        r1 = r7.substring(r1);
        r13.put(r2, r1);
    L_0x00fb:
        r1 = -1;
        r2 = 0;
        r7 = r3;
        r3 = r4;
        goto L_0x001a;
    L_0x0101:
        r7 = 4;
        if (r3 != r7) goto L_0x0105;
    L_0x0104:
        r1 = r2;
    L_0x0105:
        r3 = 1;
        r7 = r3;
        r3 = r4;
        goto L_0x001a;
    L_0x010a:
        r7 = -1;
        if (r1 != r7) goto L_0x00a6;
    L_0x010d:
        r1 = 0;
        r3 = r4;
        r7 = r1;
        r1 = r2;
        goto L_0x001a;
    L_0x0113:
        r7 = -1;
        if (r1 != r7) goto L_0x011b;
    L_0x0116:
        r3 = 4;
        r7 = r3;
        r3 = r4;
        goto L_0x001a;
    L_0x011b:
        r7 = r3;
        r0 = 5;
        if (r7 == r0) goto L_0x0122;
    L_0x011f:
        r0 = 3;
        if (r7 != r0) goto L_0x0077;
    L_0x0122:
        r7 = 0;
        goto L_0x0077;
    L_0x0125:
        r0 = -1;
        if (r1 != r0) goto L_0x012b;
    L_0x0128:
        if (r2 <= 0) goto L_0x012b;
    L_0x012a:
        r1 = r2;
    L_0x012b:
        if (r1 < 0) goto L_0x0155;
    L_0x012d:
        r0 = new java.lang.String;
        r4 = 0;
        r0.<init>(r3, r4, r2);
        r2 = 0;
        r2 = r0.substring(r2, r1);
        r0 = r0.substring(r1);
        r1 = 1;
        if (r7 != r1) goto L_0x0152;
    L_0x013f:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r0 = r1.append(r0);
        r1 = "\u0000";
        r0 = r0.append(r1);
        r0 = r0.toString();
    L_0x0152:
        r13.put(r2, r0);
    L_0x0155:
        return;
    L_0x0156:
        r7 = r3;
        r3 = r4;
        goto L_0x001a;
    L_0x015a:
        r3 = r7;
        goto L_0x006e;
    L_0x015d:
        r4 = r3;
        goto L_0x003d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.absolute.android.persistservice.r.a(java.io.Reader, java.util.Properties):void");
    }

    private static SslUtil a(Context context) {
        if (a == null) {
            a = new SslUtil(context, LogUtil.LOG_TAG);
        }
        return a;
    }
}
